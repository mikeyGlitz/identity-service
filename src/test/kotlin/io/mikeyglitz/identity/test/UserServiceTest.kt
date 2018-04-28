package io.mikeyglitz.identity.test

import com.winterbe.expekt.expect
import io.mikeyglitz.identity.config.TestLdapServerConfig
import io.mikeyglitz.identity.service.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@SpringBootTest
@RunWith(SpringRunner::class)
@ActiveProfiles("testIntegration")
@ContextConfiguration(classes = [TestLdapServerConfig::class],
        loader = AnnotationConfigContextLoader::class)
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Test
    fun findUserTest() {
        val username = "jdoe"
        val users = userService.search(username)
        expect(users).size.to.be.above(0)
        val user = users[0]
        expect(user).to.not.be.`null`
        expect(user.username).to.equal(username)
    }

    @Test
    fun findNonExistantUser() {
        val username = "swanson"
        val found = userService.search(username)
        expect(found).size.to.equal(0)
    }

    @Test
    fun authenticateUserValidPassword() {
        val username = "jdoe"
        val password = "secret"
        val authed = userService.authenticate(username, password)
        expect(authed).to.be.`true`
    }

    @Test
    fun authenticateUserBadPassword() {
        val username = "jdoe"
        val password = "not-a-real-password"
        val authed = userService.authenticate(username, password)
        expect(authed).to.be.`false`
    }

    @Test
    fun createUser(){
        val username = "ljenkins"
        val password = "TEST"
        userService.create(username, password, "Leeroy", "Jenkins", "ljenkins@blizzard.com")
        val found = userService.search(username)
        expect(found).size.to.be.above(0)
        val user = found[0]
        expect(user).to.not.be.`null`
    }

    @Test
    fun updateUser() {
        val username = "jdoe"
        val email = "john.doe@example.com"
        userService.update(username, null, null, null, email)

        val found = userService.search(username)
        expect(found).size.to.be.above(0)
        val user = found[0]
        expect(user.username).to.equal(username)
        expect(user.email).to.equal(email)
    }
}
