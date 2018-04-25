package io.mikeyglitz.identity.model

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(
    base = "ou=people",
    objectClasses = ["person", "inetOrgPerson", "top"]
)
data class User(
    @Id
    private var id: Name?,

    @Attribute(name = "cn")
    var username: String,
    @Attribute(name = "password")
    var password: String,
    @Attribute(name = "givenName")
    var firsName: String,
    @Attribute(name = "sn")
    var lastName: String,
    @Attribute(name = "mail")
    var email: String
)