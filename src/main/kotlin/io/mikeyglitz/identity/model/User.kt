package io.mikeyglitz.identity.model

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.DnAttribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import javax.naming.Name

/**
 * An LDAP user entry
 * This class searches as a user Data Access Object
 */
@Entry(
        base = "ou=people",
        objectClasses = ["person", "inetOrgPerson", "top"]
)
class User() {
    /**
     * The user id
     * This field corresponds to the LDAP DN or Distinguished Name
     */
    @Id
    lateinit var id: Name

    /**
     * The user id -- usually a short name or generic user name
     */
    @Attribute(name = "uid")
    @DnAttribute(value = "uid", index = 1)
    lateinit var username: String
    /**
     * The user's full name
     * This field matches the LDAP Common Name, or CN
     * This field is of the format "[firstName] [lastName]"
     */
    @Attribute(name = "cn")
    lateinit var fullName: String
    /**
     * The user's password
     * This field is the direct ByteArray
     */
    @Attribute(name = "userPassword", type = Attribute.Type.BINARY)
    lateinit var password: ByteArray
    /**
     * The user's first name
     */
    @Attribute(name = "givenName")
    lateinit var firstName: String
    /**
     * The user's last name
     */
    @Attribute(name = "sn")
    lateinit var lastName: String
    /**
     * The user's email address
     */
    @Attribute(name = "mail")
    lateinit var email: String

    /**
     * Instantiates a User
     * Since Spring LDAP requires that an entry has a zero-argument constructor,
     * this constructor had to be created. This constructor is used by the [UserService]
     * to create new users before they are saved into the LDAP Directory
     */
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

    /**
     * Sets the user's password
     * Since the [password] field defined above was a [ByteArray], a separate setter
     * method had to be created to set the user's password using a string value
     * @param password The new password to set to the user password
     */
    fun setPassword(password: String) {
        this.password = password.toByteArray(Charsets.UTF_8)
    }

    /**
     * Gets the user's password
     * This getter method translates the stored [password] [ByteArray] into a [String]
     * @return The user's password as a string
     */
    fun getPassword(): String = password.toString(Charsets.UTF_8)

    /**
     * Converts the user into a display object
     * @return A [UserDisplay] object which can be safely sent over REST.
     * Due to secrurity purposes, the DN and the password fields are omitted from transferring user
     * data across HTTP.
     * Best practices dictate that passwords are only to be used for authentication and should be only set
     * and compared on the server-side
     */
    fun getDisplay(): UserDisplay = UserDisplay(username, email, firstName, lastName)
}