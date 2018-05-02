package io.mikeyglitz.identity.service

import io.mikeyglitz.identity.model.Group
import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.repository.GroupRepository
import io.mikeyglitz.identity.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * A service for accessing and manipulating data for groups
 */
@Service
class GroupService {
    /**
     * The group repository
     * This property allows groups to be queried or manipulated
     */
    @Autowired
    private lateinit var groupRepository: GroupRepository
    /**
     * The user repository
     * In this context, the user repository is only pulled in to query and find users
     */
    @Autowired
    private lateinit var userRepository: UserRepository

    /**
     * Adds a user to a group
     * @param username The name of the user to add to the group
     * @param groupName The name of the group to add the user to
     */
    fun addUserToGroup(username: String, groupName: String) {
        val user = userRepository.findByUsername(username)
        val group = groupRepository.findByName(groupName)

        if (user != null && group != null) {
            group.members.add(user.id)
            groupRepository.save(group)
        }
    }

    /**
     * Removes a user from a group
     * @param username The name of the user to remove from the group
     * @param groupName The name of the group to remove the user from
     */
    fun removeUserFromGroup(username: String, groupName: String) {
        val user = userRepository.findByUsername(username)
        val group = groupRepository.findByName(groupName)

        if (user != null && group != null) {
            group.members.remove(user.id)
            groupRepository.save(group)
        }
    }

    /**
     * Finds a group by name
     * @param name The name to look for
     * @return a group if one was found, null if a group was not found
     */
    fun findGroup(name: String): Group? = groupRepository.findByName(name)

    /**
     * Creates a group
     * When the group is created, the user that created the group will automatically be added to it.
     * Automatically adding the user to the group was a design choice since the LDAP API did not allow
     * for the creation of empty groups
     * @param name The name of the group
     * @param user The user that created the group. This user will be added as a member of the newly
     * created group
     */
    fun createGroup(name: String, user: User) {
        val group = Group(name)
        group.members.add(user.id)
        groupRepository.save(group)
    }

    /**
     * Deletes a group from the system
     * @param name The name of the group to delete
     */
    fun deleteGroup(name: String) {
        val group = groupRepository.findByName(name)
        groupRepository.delete(group!!)
    }

    /**
     * Lists all of the groups stored in the repository
     * @return a list of groups, or no groups if the repository is currently empty
     */
    fun allGroups(): List<Group> = groupRepository.findAll() as List<Group>

    /**
     * Modifies a group's information
     * @param oldName The current name of the group
     * @param newName The name to change the group name to
     * @param description The new description for the group
     */
    fun updateGroup(oldName: String, newName: String?, description: String?) {
        val group = groupRepository.findByName(oldName)
        if (group != null) {
            if (newName != null)
                group.name = newName
            if (description != null)
                group.description = description
            groupRepository.save(group)
        }
    }
}