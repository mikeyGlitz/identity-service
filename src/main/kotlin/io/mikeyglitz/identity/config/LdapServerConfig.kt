package io.mikeyglitz.identity.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.ldap.repository.config.EnableLdapRepositories
import org.springframework.ldap.core.ContextSource
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

@Configuration
@EnableLdapRepositories
class LdapServerConfig {
    @Autowired
    private lateinit var environment: Environment

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