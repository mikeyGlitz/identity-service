package io.mikeyglitz.identity.test.controller

import io.mikeyglitz.identity.controller.UserController
import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.model.UserCreationInput
import io.mikeyglitz.identity.service.UserService
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(UserController::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: UserService

    @Before
    fun initialize() {
        // Set up dummy database info
        val dummy = User("jdoe", "secret", "John", "Doe", "jdoe@example.com")
        val dummy2 = User("kdoe", "secret", "Karen", "Doe", "kdoe@example.com")
        val dummy3 = User("jblow", "secret", "Joe", "Blow", "jblow@example.com")
        val users = listOf(dummy, dummy2, dummy3)

        `when`(service.findByUsername(dummy.username)).thenReturn(dummy)
        `when`(service.create(UserCreationInput(
                "ljenkins",
                "iHaveASecret",
                "ljenkins@example.com",
                "Leeroy",
                "Jenkins"
        ))).thenReturn(User(
                "ljenkins",
                "iHaveASecret",
                "Leeroy",
                "Jenkins",
                "ljenkins@example.com"
        ))
    }

    @Test
    fun testSearchValidUser() {
        mockMvc.perform(get("/users/jdoe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("username", `is`("jdoe")))
    }

    @Test
    fun testSearchInvalidUser() {
        mockMvc.perform(get("/users/jjacob")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }

    @Test
    fun testAddUser() {
        mockMvc.perform(post("/users")
                .content("{" +
                        "\"username\": \"ljenkins\"," +
                        "\"password\": \"iHaveASecret\"," +
                        "\"email\": \"ljenkins@example.com\"," +
                        "\"firstName\": \"Leeroy\"," +
                        "\"lastName\": \"Jenkins\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("username", `is`("ljenkins")))
    }

    @Test
    fun testAddUserMissingField() {
        mockMvc.perform(post("/users")
                .content("{" +
                        "\"username\": \"ljenkins\"," +
                        "\"email\": \"ljenkins@example.com\"," +
                        "\"firstName\": \"Leeroy\"," +
                        "\"lastName\": \"Jenkins\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun testAddUserInvalidField() {
        mockMvc.perform(post("/users")
                .content("{" +
                        "\"username\": \"ljenkins\"," +
                        "\"password\": \"secret\"," +
                        "\"email\": \"ljenkins\"," +
                        "\"firstName\": \"Leeroy\"," +
                        "\"lastName\": \"Jenkins\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }
}