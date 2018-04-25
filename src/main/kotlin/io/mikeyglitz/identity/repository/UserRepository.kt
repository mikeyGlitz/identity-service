package io.mikeyglitz.identity.repository

import io.mikeyglitz.identity.model.User
import org.springframework.data.ldap.repository.LdapRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : LdapRepository<User> {
    fun findByUsername(username: String): User
}