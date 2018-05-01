package io.mikeyglitz.identity.test.service

import com.winterbe.expekt.expect
import io.mikeyglitz.identity.config.TestLdapServerConfig
import io.mikeyglitz.identity.model.UserCreationInput
import io.mikeyglitz.identity.model.UserUpdateInput
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
        val user = userService.findByUsername(username)
        expect(user).to.not.be.`null`
        expect(user!!.username).to.equal(username)
    }

    @Test
    fun findNonExistantUser() {
        val username = "swanson"
        val user = userService.findByUsername(username)
        expect(user).to.be.`null`
    }

    @Test
    fun authenticateUserValidPassword() {
        val username = "jdoe"
        val password = "secret"
        val authed = userService.authed(username, password)
        expect(authed).to.be.`true`
    }

    @Test
    fun authenticateUserBadPassword() {
        val username = "jdoe"
        val password = "not-a-real-password"
        val authed = userService.authed(username, password)
        expect(authed).to.be.`false`
    }

    @Test
    fun createUser() {
        val username = "ljenkins"
        val password = "TEST"
        val user = userService.create(UserCreationInput(username, password, "ljenkins@blizzard.com", "Leeroy", "Jenkins"))
        expect (user).to.not.be.`null`
        expect (user.username).to.equal(username)
        expect (user.id).to.not.be.`null`
    }

    @Test
    fun updateUser() {
        val username = "jdoe"
        val email = "john.doe@example.com"
        val matchingUser = userService.findByUsername(username)
        val user = userService.update(matchingUser!!, UserUpdateInput(username, null, email, null, null))

        expect(user.username).to.equal(username)
        expect(user.email).to.equal(email)
    }
}
