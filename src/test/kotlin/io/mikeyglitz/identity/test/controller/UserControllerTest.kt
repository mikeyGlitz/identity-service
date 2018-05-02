package io.mikeyglitz.identity.test.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mikeyglitz.identity.controller.UserController
import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.model.UserCreationInput
import io.mikeyglitz.identity.model.UserUpdateInput
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

val creationInput = UserCreationInput(
        "ljenkins",
        "iHaveASecret",
        "ljenkins@example.com",
        "Leeroy",
        "Jenkins"
)

val updateInput = UserUpdateInput(
        "iHaveASecret",
        "john.doe@example.com",
        "John",
        "Doe"
)

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

        `when`(service.findByUsername(dummy.username)).thenReturn(dummy)
        `when`(service.create(creationInput)).thenReturn(User(
                "ljenkins",
                "iHaveASecret",
                "Leeroy",
                "Jenkins",
                "ljenkins@example.com"
        ))
        `when`(service.update(dummy, updateInput)).thenReturn(User(
                "jdoe",
                updateInput.password!!,
                updateInput.firstName!!,
                updateInput.lastName!!,
                updateInput.email!!))
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
                .content(ObjectMapper().writeValueAsString(creationInput))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("username", `is`("ljenkins")))
    }

    @Test
    fun testAddUserMissingField() {
        val missingFieldInput = UserCreationInput(
                "ljenkins",
                null,
                "ljenkins@example.com",
                "Leeroy",
                "Jenkins"
        )
        mockMvc.perform(post("/users")
                .content(ObjectMapper().writeValueAsString(missingFieldInput))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun testAddUserInvalidField() {
        val invalidEmailInput = UserCreationInput(
                "ljenkins",
                "secret",
                "ljenkins",
                "Leeroy",
                "Jenkins"
        )
        mockMvc.perform(post("/users")
                .content(ObjectMapper().writeValueAsString(invalidEmailInput))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun testDeleteUser() {
        mockMvc.perform(delete("/users/jdoe"))
                .andExpect(status().isOk)
    }

    @Test
    fun testDeleteInvalidUser() {
        mockMvc.perform(delete("/users/jjacob"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun testUpdateUser() {
        mockMvc.perform(put("/users/jdoe")
                .content(ObjectMapper().writeValueAsString(updateInput))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("email", `is`("john.doe@example.com")))
    }

    @Test
    fun testUpdateUserInvalidProperty() {
        val invalidInput = UserUpdateInput(
                "iHaveASecret",
                "jdoe",
                "John",
                "Doe"
        )
        mockMvc.perform(put("/users/jdoe")
                .content(ObjectMapper().writeValueAsString(invalidInput))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun testUpdateUserNotExists() {
        mockMvc.perform(put("/users/jjacob")
                .content(ObjectMapper().writeValueAsString(updateInput))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)
    }
}