package io.mikeyglitz.identity.service

import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var repository: UserRepository

    fun authenticate(username: String, password: String): Boolean =
        repository.findByUsernameAndPassword(username, password) != null
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
                user.password = password
            if (email != null)
                user.email = email
            if (firstName != null)
                user.firstName = firstName
            if (lastName != null)
                user.lastName = lastName
        }
    }

    fun search(name: String): List<User> = repository.findByUsernameLikeIgnoreCase(name)
}