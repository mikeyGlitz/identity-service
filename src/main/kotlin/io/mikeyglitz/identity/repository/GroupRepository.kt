package io.mikeyglitz.identity.repository

import io.mikeyglitz.identity.model.Group
import org.springframework.data.ldap.repository.LdapRepository
import org.springframework.data.ldap.repository.Query
import org.springframework.stereotype.Repository
import javax.naming.Name

@Repository
interface GroupRepository : LdapRepository<Group> {
    @Query("(member={0})")
    fun findByMember(member: Name): List<Group>
}
