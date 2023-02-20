package com.atmostadam.cats.drools.service;

import com.atmostadam.cats.api.model.out.CatResponse;
import com.atmostadam.cats.drools.configuration.CatDroolsTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static com.atmostadam.cats.framework.data.CatTestValues.catRequestTestData;

@SpringJUnitConfig({CatDroolsTestConfiguration.class})
class PostAdoptAPetCatTest {
    @Autowired
    PostAdoptAPetCat service;

    @Test
    void httpStatus200() {
        ResponseEntity<CatResponse> response = service.invoke(UUID.randomUUID().toString(), catRequestTestData());
    }
}
