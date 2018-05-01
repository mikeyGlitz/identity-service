package io.mikeyglitz.identity.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * An input class for user creation
 */
data class UserCreationInput(
    @field: NotNull
    @field: NotEmpty
    val username: String?,

    @field: NotNull
    @field: NotEmpty
    @field: Size(min = 8)
    val password: String?,

    @field: NotNull
    @field: NotEmpty
    @field: Email
    val email: String?,

    @field: NotNull
    @field: NotEmpty
    val firstName: String?,

    @field: NotNull
    @field: NotEmpty
    val lastName: String?
)

/**
 * An input class for user updates
 */
data class UserUpdateInput(
    @field: NotEmpty
    var username: String?,

    @field: NotEmpty
    @field: Size(min = 8)
    val password: String?,

    @field: NotEmpty
    @field: Email
    val email: String?,

    @field: NotEmpty
    val firstName: String?,

    @field: NotEmpty
    val lastName: String?
)
