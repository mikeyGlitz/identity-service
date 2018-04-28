package io.mikeyglitz.identity.repository

import io.mikeyglitz.identity.model.User
import org.springframework.data.ldap.repository.LdapRepository
import org.springframework.stereotype.Repository

/**
 * A repository for users
 * The UserRepository provides functions for accessing and manipulating users
 */
@Repository
interface UserRepository : LdapRepository<User> {
    /**
     * Finds a user by username
     * @param username The username to search for
     * @return A User if found, or null if no results were able to match
     */
    fun findByUsername(username: String): User?

    /**
     * Finds a user by its username and password
     * This method was intended to be used for authentication against the LDAP backend
     * @param username The username to search for
     * @param password The password to search for
     * @return The matching User if one was found, or null if there was no matching user
     */
    fun findByUsernameAndPassword(username: String, password: String): User?

    /**
     * Searches the users to see if the search string is contained or is equal to the username field
     * This method will perform a case-insensitive search
     * @param username A whole or partial username
     * @return A list of matching users, or an empty list if no users were found
     */
    fun findByUsernameLikeIgnoreCase(username: String): List<User>
}