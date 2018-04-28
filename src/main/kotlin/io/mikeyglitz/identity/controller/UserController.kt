package io.mikeyglitz.identity.controller

import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.model.UserDisplay
import io.mikeyglitz.identity.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * A REST controller for performing operations on users
 * This controller maps to the "/users" location
 */
@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("")
    fun findUser(@RequestParam("username") username: String?): List<UserDisplay> =
            userService.search(username?: "*").map { user: User -> user.toDisplay() }
}