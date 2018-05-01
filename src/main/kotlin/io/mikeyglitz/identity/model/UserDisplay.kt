package io.mikeyglitz.identity.model

/**
 * A data class which is used to safely display user attributes
 */
data class UserDisplay(
    /**
     * The username
     */
    val username: String,
    /**
     * A user's email address
     */
    val email: String,
    /**
     * The user's first name
     */
    val firstName: String,
    /**
     * The user's last name
     */
    val lastName: String
)
