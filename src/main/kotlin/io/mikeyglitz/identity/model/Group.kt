package io.mikeyglitz.identity.model

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(
    base = "ou=groups",
    objectClasses = ["groupOfNames", "top"]
)
class Group() {
    @Id
    lateinit var id: Name

    @Attribute(name = "cn")
    lateinit var name: String
    @Attribute(name = "description")
    lateinit var description: String
    @Attribute(name = "member")
    var members: Set<Name> = emptySet()

    constructor(name: String) : this() {
        this.name = name
    }

    constructor(name: String, description: String) : this(name) {
        this.description = description
    }
}