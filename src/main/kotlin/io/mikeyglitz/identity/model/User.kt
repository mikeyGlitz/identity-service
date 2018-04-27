package io.mikeyglitz.identity.model

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.DnAttribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

@Entry(
    base = "ou=people",
    objectClasses = ["person", "inetOrgPerson", "top"]
)
class User() {
    @Id
    lateinit var id: Name

    @Attribute(name = "uid")
    @DnAttribute(value = "uid", index = 1)
    lateinit var username: String
    @Attribute(name = "cn")
    lateinit var fullName: String
    @Attribute(name = "userPassword", type = Attribute.Type.BINARY)
    lateinit var password: ByteArray
    @Attribute(name = "givenName")
    lateinit var firstName: String
    @Attribute(name = "sn")
    lateinit var lastName: String
    @Attribute(name = "mail")
    lateinit var email: String

    constructor(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String
    ) : this() {
        this.username = username
        this.password = password.toByteArray(Charsets.UTF_8)
        this.email = email
        this.lastName = lastName
        this.firstName = firstName
        this.fullName = "$firstName $lastName"
    }

    fun setPassword(password: String) {
        this.password = password.toByteArray(Charsets.UTF_8)
    }

    fun getPassword(): String = password.toString(Charsets.UTF_8)
}