package io.mikeyglitz.identity.test

import com.winterbe.expekt.expect
import io.mikeyglitz.identity.config.TestLdapServerConfig
import io.mikeyglitz.identity.service.GroupService
import io.mikeyglitz.identity.service.UserService
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@SpringBootTest
@RunWith(SpringRunner::class)
@ContextConfiguration(
        classes = [TestLdapServerConfig::class],
        loader = AnnotationConfigContextLoader::class
)
@ActiveProfiles("testIntegration")
class GroupServiceTest {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var groupService: GroupService


    @Test
    fun testFindGroup() {
        val name = "joes"
        val group = groupService.findGroup(name)
        expect(group).to.not.be.`null`
        expect(group!!.name).to.equal(name)
    }

    @Test
    fun testFindGroupNoExist() {
        val name = "khans"
        val group = groupService.findGroup(name)
        expect(group).to.be.`null`
    }

    @Test
    fun testAddValidUserToGroup() {
        userService.create("jjacob", "test", "John", "Jacob", "jjacob@example.com")
        val name = "johns"
        groupService.addUserToGroup("jjacob", name)
        val group = groupService.findGroup(name)
        expect(group).to.not.be.`null`
        expect(group!!.members).size.to.equal(2)
    }

    @Test
    fun testCreateGroup() {
        val groupName = "tyrants"
        val foundUsers= userService.search("jdoe")
        val user = foundUsers[0]
        groupService.createGroup(groupName, user!!)

        val group = groupService.findGroup(groupName)
        expect(group).to.not.be.`null`
        expect(group!!.name).to.equal(groupName)
    }

    @Test
    fun removeGroup() {
        val groupName = "heroes"
        val foundUsers= userService.search("jdoe")
        val user = foundUsers[0]
         groupService.createGroup(groupName, user!!)

        val createdGroup = groupService.findGroup(groupName)
        groupService.deleteGroup(groupName)
        expect(groupService.allGroups()).not.to.contain(createdGroup!!)
    }

    @Test
    fun updateGroup() {
        val groupName = "joes"
        val newGroupName = "josephs"
        groupService.updateGroup(groupName, newGroupName, null)
        val group = groupService.findGroup(newGroupName)
        expect(group).to.not.be.`null`
        expect(group!!.name).to.equal(newGroupName)
    }
}