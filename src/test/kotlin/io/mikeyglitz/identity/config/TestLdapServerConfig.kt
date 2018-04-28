package io.mikeyglitz.identity.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.data.ldap.repository.config.EnableLdapRepositories
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource
import org.springframework.ldap.test.TestContextSourceFactoryBean

/**
 * This configuration is specifically for preforming integration tests on the LDAP server
 * This configuration will bind the tests to an in-memory LDAP server called ApacheDS
 */
@Configuration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = arrayOf("io.mikeyglitz.identity.**"))
@EnableLdapRepositories(basePackages = arrayOf("io.mikeyglitz.identity.repository"))
@Profile("testIntegration")
class TestLdapServerConfig {
    /**
     * A proxy to the environment which also allows access to spring properties
     */
    @Autowired
    private lateinit var environment: Environment
    /**
     * A resource loader for accessing data from the classpath
     */
    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    /**
     * Sets up the test context
     * @return a context which is pre-configured to connect to the ApacheDS instance running in memory
     */
    @Bean
    fun testContextSource(): TestContextSourceFactoryBean {
        val contextSource = TestContextSourceFactoryBean()
        contextSource.setDefaultPartitionName(environment.getRequiredProperty("ldap.partition.name"))
        contextSource.setDefaultPartitionSuffix(environment.getRequiredProperty("ldap.partition.suffix"))
        contextSource.setPassword(environment.getRequiredProperty("ldap.principal.password"))
        contextSource.setPrincipal(environment.getRequiredProperty("ldap.principal.dn"))
        contextSource.setPort(Integer.parseInt(environment.getRequiredProperty("ldap.port")))
        contextSource.setLdifFile(resourceLoader.getResource(environment.getRequiredProperty("ldap.schema")))
        return contextSource
    }

    /**
     * Sets up the context source to be used to connect to the ApacheDS instance running in memory
     * @return a context source configuration bean
     */
    @Bean
    fun contextSource(): LdapContextSource {
        val contextSource = LdapContextSource()
        contextSource.setBase(environment.getRequiredProperty("ldap.partition.suffix"))
        contextSource.setUrl(environment.getRequiredProperty("ldap.url"))
        contextSource.userDn = environment.getRequiredProperty("ldap.principal.dn")
        contextSource.password = environment.getProperty("ldap.principal.password")
        return contextSource
    }

    @Bean
    fun ldapTemplate(): LdapTemplate {
        return LdapTemplate(this.contextSource())
    }
}
