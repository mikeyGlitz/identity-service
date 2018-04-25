package io.mikeyglitz.identity.model

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(
    base = "ou=groups",
    objectClasses = ["groupOfNames", "top"]
)
data class Group(
    @Id
    private var id: Name?,

    @Attribute(name = "cn")
    var name: String,
    @Attribute(name = "description")
    var description: String?,
    @Attribute(name = "member")
    var members: Set<Name>
)