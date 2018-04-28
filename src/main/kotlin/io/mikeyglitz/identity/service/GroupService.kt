package io.mikeyglitz.identity.service

import io.mikeyglitz.identity.model.Group
import io.mikeyglitz.identity.model.User
import io.mikeyglitz.identity.repository.GroupRepository
import io.mikeyglitz.identity.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ldap.support.LdapNameBuilder
import org.springframework.ldap.support.LdapUtils
import org.springframework.stereotype.Service
import javax.naming.ldap.LdapName

@Service
class GroupService {
    @Autowired
    private lateinit var groupRepository: GroupRepository
    @Autowired
    private lateinit var userRepository: UserRepository

    fun addUserToGroup(username: String, groupName: String) {
        val user = userRepository.findByUsername(username)
        val group = groupRepository.findByName(groupName)

        if (user != null && group != null) {
            group.members.add(user.id)
            groupRepository.save(group)
        }
    }

    fun removeUserFromGroup(username: String, groupName: String) {
        val user = userRepository.findByUsername(username)
        val group = groupRepository.findByName(groupName)

        if (user != null && group != null) {
            group.members.remove(user.id)
            groupRepository.save(group)
        }
    }

    fun findGroup(name: String): Group? = groupRepository.findByName(name)

    fun createGroup(name: String, user: User) {
        val group = Group(name)
        group.members.add(user.id)
        groupRepository.save(group)
    }

    fun deleteGroup(name: String) {
        val group = groupRepository.findByName(name)
        groupRepository.delete(group)
    }

    fun allGroups(): List<Group> = groupRepository.findAll() as List<Group>

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