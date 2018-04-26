package io.mikeyglitz.identity.test

import com.winterbe.expekt.expect
import io.mikeyglitz.identity.config.TestConfig
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
@ActiveProfiles("test")
@ContextConfiguration(classes = [TestConfig::class],
        loader = AnnotationConfigContextLoader::class)
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Test
    fun findUserTest() {
        val username = "jdoe"
        val users = userService.search(username)
        expect(users.size).to.be.above(0)
        val user = users[0]
        expect(user).to.not.be.`null`
        expect(user.username).to.equal(username)
    }
}
