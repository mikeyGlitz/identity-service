package io.mikeyglitz.identity.service

import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.model.UserCreationInput
import io.mikeyglitz.identity.model.UserUpdateInput
import io.mikeyglitz.identity.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * A service for accessing and manipulating information for users
 */
@Service
class UserService(@Autowired userRepository: UserRepository): UserRepository by userRepository{
    /**
     * Checks to see if the user has previously signed up
     * @param username The name of the user to log in
     * @param password The user's password
     * @return true if  the user was found, false if the user wasn't
     */
    fun authed(username: String, password: String): Boolean = findByUsernameAndPassword(username, password) != null

    /**
     * Creates a new user and adds the user to the LDAP directory
     * @param input An input argument whose fields correspond with the input types
     * from the request body
     */
    fun create(input: UserCreationInput): User {
        val user = User(input.username!!, input.password!!, input.firstName!!, input.lastName!!, input.email!!)
        return save(user)
    }

    /**
     * Updates a user's information
     * @param user The user to be updated
     * @param input An input argument whose fields correspond with the input types
     */
    fun update(user: User, input: UserUpdateInput): User {
        if (input.password != null)
            user.setPassword(input.password)
        if (input.email != null)
            user.email = input.email
        if (input.firstName != null)
            user.firstName = input.firstName
        if (input.lastName != null)
            user.lastName = input.lastName
        return save(user)
    }
}