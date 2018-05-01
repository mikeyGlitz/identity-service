package io.mikeyglitz.identity.controller

import io.mikeyglitz.identity.error.ResourceNotFoundException
import io.mikeyglitz.identity.model.UserCreationInput
import io.mikeyglitz.identity.model.UserDisplay
import io.mikeyglitz.identity.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

/**
 * A REST controller for performing operations on users
 * This controller maps to the "/users" location
 */
@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") username: String): UserDisplay {
        val user = userService.findByUsername(username)
        user ?: throw ResourceNotFoundException()
        return user!!.toDisplay()
    }

    @PostMapping(path = ["", "/"])
    fun createUser(@Valid @RequestBody input: UserCreationInput): UserDisplay =
            userService.create(input).toDisplay()
}