package io.mikeyglitz.identity.repository

import io.mikeyglitz.identity.model.Group
import org.springframework.data.ldap.repository.LdapRepository
import org.springframework.data.ldap.repository.Query
import org.springframework.stereotype.Repository
import javax.naming.Name

/**
 * A repository object for groups.
 * The GroupRepository contains functions for manipulating and querying
 * groups in the LDAP database
 */
@Repository
interface GroupRepository : LdapRepository<Group> {
    /**
     * Finds a group by its name
     * @param name The group name to search for
     * @return A matching group or null if no group was able to match
     */
    fun findByName(name: String): Group?

    /**
     * Finds a group by a member name
     * @param member The DN of the member to search for in the group
     * @return A list of groups containing the member or an empty list
     * if that member was not part of any groups
     */
    @Query("(uniqueMember={0})")
    fun findByMember(member: Name): List<Group>
}
