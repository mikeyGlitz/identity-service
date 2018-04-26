package io.mikeyglitz.identity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.TestContextSourceFactoryBean;


@Configuration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackages = "io.mikeyglitz.identity.**")
@EnableLdapRepositories(basePackages = "io.mikeyglitz.identity.repository")
@Profile("test")
public class TestConfig {
    @Autowired
    private Environment environment;
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public TestContextSourceFactoryBean testContextSource(){
        TestContextSourceFactoryBean contextSource = new TestContextSourceFactoryBean();
        contextSource.setDefaultPartitionName(environment.getRequiredProperty("ldap.partition.name"));
        contextSource.setDefaultPartitionSuffix(environment.getRequiredProperty("ldap.partition.suffix"));
        contextSource.setPassword(environment.getRequiredProperty("ldap.principal.password"));
        contextSource.setPrincipal(environment.getRequiredProperty("ldap.principal.dn"));
        contextSource.setPort(Integer.parseInt(environment.getRequiredProperty("ldap.port")));
        contextSource.setLdifFile(resourceLoader.getResource(environment.getProperty("ldap.schema")));
        return contextSource;
    }
    @Bean
    public LdapContextSource contextSource(){
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setBase(environment.getRequiredProperty("ldap.partition.suffix"));
        contextSource.setUrl(environment.getRequiredProperty("ldap.url"));
        contextSource.setUserDn(environment.getRequiredProperty("ldap.principal.dn"));
        contextSource.setPassword(environment.getProperty("ldap.principal.password"));
        return contextSource;
    }
    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(this.contextSource());
    }
}
