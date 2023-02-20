package com.atmostadam.cats.drools.service;

import com.atmostadam.cats.api.model.Cat;
import com.atmostadam.cats.drools.configuration.CatDroolsTestConfiguration;
import com.atmostadam.cats.framework.model.adoptapet.AdoptAPetRequest;
import com.atmostadam.cats.framework.model.adoptapet.LimitedPetDetails;
import com.atmostadam.cats.framework.model.adoptapet.LimitedPetImage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static com.atmostadam.cats.framework.data.CatTestValues.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringJUnitConfig({CatDroolsTestConfiguration.class})
class CatDroolsServiceTest {
    @Autowired
    CatDroolsService service;

    @Test
    void testFireAllRules() throws Exception {
        LimitedPetImage adoptAPetImage = new LimitedPetImage();
        LimitedPetDetails adoptAPetDetails = new LimitedPetDetails();
        AdoptAPetRequest adoptAPetRequest = new AdoptAPetRequest();

        Map<String, Object> facts = new HashMap<>();
        facts.put("adoptAPetImage", adoptAPetImage);
        facts.put("adoptAPetDetails", adoptAPetDetails);
        facts.put("adoptAPetRequest", adoptAPetRequest);
        facts.put("cat", catTestData());

        service.fireAllRules("adopt-a-pet-rules", facts);

        assertThat(facts.size(), Matchers.equalTo(4));
    }
}
