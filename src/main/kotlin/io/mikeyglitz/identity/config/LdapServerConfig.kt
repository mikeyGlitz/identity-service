package io.mikeyglitz.identity.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${api.host}")
    private lateinit var host: String

    @Value("\${api.port}")
    private lateinit var port: String

    @Value("\${api.base}")
    private lateinit var base: String

    @Value("\${api.user")
    private lateinit var user: String

    @Autowired
    private lateinit var environment: Environment

    @Bean
    fun contextSource(): ContextSource {
        val ldapUri: String = environment.getProperty("#{ID_DB_URI}", "ldap://$host:$port")
        val password: String? = environment.getProperty("#{ID_DB_PWD}")

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