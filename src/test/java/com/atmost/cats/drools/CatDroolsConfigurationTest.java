package com.atmost.cats.drools;

import com.atmostadam.cats.drools.service.CatDroolsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = CatDroolsService.class)
@TestConfiguration
public class CatDroolsConfigurationTest {
}
