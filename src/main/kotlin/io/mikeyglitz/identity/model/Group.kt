package io.mikeyglitz.identity.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.DnAttribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

/**
 * A Data Access Object representing a LDAP Group entry
 */
@Entry(
    base = "ou=groups",
    objectClasses = ["groupOfUniqueNames", "top"]
)
class Group() {
    @JsonIgnore
    @Id
    lateinit var id: Name

    @Attribute(name = "cn")
    @DnAttribute(value = "cn", index = 1)
    lateinit var name: String
    @Attribute(name = "description")
    lateinit var description: String
    @Attribute(name = "uniqueMember")
    var members: MutableSet<Name> = HashSet()

    constructor(name: String) : this() {
        this.name = name
    }

    constructor(name: String, description: String) : this(name) {
        this.description = description
    }
}