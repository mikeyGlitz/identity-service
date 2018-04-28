package io.mikeyglitz.identity.test

import io.mikeyglitz.identity.controller.UserController
import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.model.UserDisplay
import io.mikeyglitz.identity.service.UserService
import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
        val dummy3 = User("ljenkins", "secret", "Leeroy", "Jenkins", "ljenkins@example.com")
        val users = listOf(dummy, dummy2, dummy3)
        Mockito.`when`(service.search(dummy.username)).thenReturn(listOf(dummy))
        Mockito.`when`(service.search(null)).thenReturn(users)
        Mockito.`when`(service.search("*")).thenReturn(users)
        Mockito.`when`(service.search("doe")).thenReturn(listOf(dummy, dummy2))
    }

    @Test
    fun searchUsersExistingUser() {
        val username = "jdoe"
        mockMvc.perform(get("/users?username=$username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<List<UserDisplay>>(1)))
                .andExpect(jsonPath("$[0].username", `is`(username)))
    }
    fun searchUserPartial(){
        val partialName = "doe"

        mockMvc.perform(get("/users?username=$partialName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<List<UserDisplay>>(2)))
    }

    @Test
    fun searchUsersNoNameProvided() {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<List<UserDisplay>>(3)))
    }

    @Test
    fun searchUsersStarProvided() {
        mockMvc.perform(get("/users?username=*")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<List<UserDisplay>>(3)))
    }
}