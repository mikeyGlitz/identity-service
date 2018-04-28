package io.mikeyglitz.identity.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.ldap.repository.config.EnableLdapRepositories
import org.springframework.ldap.core.ContextSource
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

/**
 * A configuration bean used to connect to the LDAP server
 */
@Configuration
@EnableLdapRepositories
class LdapServerConfig {
    /**
     * A map for the environment and Spring properties
     */
    @Autowired
    private lateinit var environment: Environment

    /**
     * Sets up the context source so that the application
     * can connect to the LDAP backend
     * @return A configured context source to be used by Spring
     */
    @Bean
    fun contextSource(): ContextSource {
        val host = environment.getRequiredProperty("api.host")
        val port = environment.getRequiredProperty("api.port")
        val base = environment.getRequiredProperty("api.base")
        val user = environment.getRequiredProperty("api.user")
        val ldapUri = environment.getProperty("ID_DB_URI", "ldap://$host:$port")
        val password = environment.getProperty("ID_DB_PWD")

        val contextSource = LdapContextSource()
        contextSource.setUrl(ldapUri)
        contextSource.setBase(base)
        contextSource.userDn = user
        contextSource.password = password

        return contextSource
    }

    @Bean
    fun ldapTemplate(contextSource: ContextSource): LdapTemplate = LdapTemplate(contextSource)
}