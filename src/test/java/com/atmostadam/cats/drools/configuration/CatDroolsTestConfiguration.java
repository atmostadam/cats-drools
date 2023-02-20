package com.atmostadam.cats.drools.configuration;

import com.atmostadam.cats.api.configuration.CatConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(CatConfiguration.class)
@ComponentScan("com.atmostadam.cats")
@EntityScan("com.atmostadam.cats")
@EnableAutoConfiguration
@TestConfiguration
public class CatDroolsTestConfiguration {
}
