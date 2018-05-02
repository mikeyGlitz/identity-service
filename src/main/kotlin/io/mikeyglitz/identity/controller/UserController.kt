package io.mikeyglitz.identity.controller

import io.mikeyglitz.identity.error.ResourceNotFoundException
import io.mikeyglitz.identity.model.UserCreationInput
import io.mikeyglitz.identity.model.UserDisplay
import io.mikeyglitz.identity.model.UserUpdateInput
import io.mikeyglitz.identity.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        return user.getDisplay()
    }

    @PostMapping(path = ["", "/"])
    fun createUser(@Valid @RequestBody input: UserCreationInput): UserDisplay =
            userService.create(input).getDisplay()

    @PutMapping("/{id}")
    fun updateUser(@PathVariable("id") username: String, @Valid @RequestBody input: UserUpdateInput): UserDisplay {
        val matchingUser = userService.findByUsername(username)
        matchingUser ?: throw ResourceNotFoundException()

        val user = userService.update(matchingUser, input)
        return user.getDisplay()
    }
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable("id") username: String): ResponseEntity<HttpStatus> {
        val user = userService.findByUsername(username)
        user ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        userService.delete(user!!)
        return ResponseEntity(HttpStatus.OK)
    }
}