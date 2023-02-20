package com.atmostadam.cats.drools.configuration;

import com.atmostadam.cats.api.configuration.CatWebClientProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.atmostadam.cats")
@EntityScan("com.atmostadam.cats")
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(CatWebClientProperties.class)
@EnableAutoConfiguration
@Configuration
public class CatDroolsConfiguration {
    @Bean("CatWebClientPropertiesAdoptAPet")
    @ConfigurationProperties(prefix = "cats.service.http.webclient.adoptapet")
    public CatWebClientProperties catWebClientPropertiesAdoptAPet() {
        return new CatWebClientProperties();
    }

    @Bean("CatWebClientPropertiesPetfinder")
    @ConfigurationProperties(prefix = "cats.service.http.webclient.petfinder")
    public CatWebClientProperties catWebClientPropertiesPetfinder() {
        return new CatWebClientProperties();
    }
}
