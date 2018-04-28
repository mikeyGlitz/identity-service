package io.mikeyglitz.identity.service

import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * A service for accessing and manipulating information for users
 */
@Service
class UserService {
    /**
     * The user repository
     */
    @Autowired
    private lateinit var repository: UserRepository

    /**
     * A method which searches for user by username and password
     * @param username The username to search for
     * @param password The password to search for
     * @return A boolean indicator to indicate whether the user was able to
     * be found or not
     */
    fun authenticate(username: String, password: String): Boolean =
        repository.findByUsernameAndPassword(username, password) != null

    /**
     * Creates a new user and adds the user to the LDAP directory
     * @param username The user's name
     * @param password The user's password
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param email The user's email address
     */
    fun create(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String
    ) {
        val user = User(username, password, firstName, lastName, email)
        repository.save(user)
    }

    /**
     * Updates a user's information
     * @param username The username to be updated
     * @param password The password to set to the user's password
     * @param firstName The user's new first name
     * @param lastName The user's new last name
     * @param email The user's new email address
     */
    fun update(
        username: String,
        password: String?,
        firstName: String?,
        lastName: String?,
        email: String?
    ) {
        val user = repository.findByUsername(username)
        if (user != null) {
            if (password != null)
                user.setPassword(password)
            if (email != null)
                user.email = email
            if (firstName != null)
                user.firstName = firstName
            if (lastName != null)
                user.lastName = lastName
            repository.save(user)
        }
    }

    /**
     * Performs a search for users by username
     * This search will be case-insensitive and contain partial name matches
     * @param name the name to search for
     * @return a list of users, or an empty list if none were found
     */
    fun search(name: String?): List<User> = repository.findByUsernameLikeIgnoreCase(name?:"*")
}