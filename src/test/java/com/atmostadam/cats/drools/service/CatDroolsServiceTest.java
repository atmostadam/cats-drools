package com.atmostadam.cats.drools.service;

import com.atmostadam.cats.api.model.Microchip;
import com.atmostadam.cats.drools.test.CatDroolsConfigurationTest;
import com.atmostadam.cats.api.model.in.CatRequest;
import com.atmostadam.cats.api.model.out.CatResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static com.atmostadam.cats.test.data.CatTestData.staticCatRequest;
import static com.atmostadam.cats.test.data.CatTestData.staticCatResponse;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringJUnitConfig({CatDroolsConfigurationTest.class})
class CatDroolsServiceTest {
    @Autowired
    CatDroolsService service;

    @Test
    void testFireAllRules() throws Exception {
        CatRequest catRequest = staticCatRequest();
        CatResponse catResponse = staticCatResponse();
        Microchip microchip = new Microchip();
        Map<String, Object> facts = new HashMap<>();
        facts.put("catRequest", catRequest);
        facts.put("catResponse", catResponse);
        facts.put("microchip", microchip);
        service.fireAllRules("cat-request-rules", facts);
        assertThat(facts.size(), Matchers.equalTo(3));
        assertThat(catRequest.getCats().get(0).getMicrochip().getMicrochipNumber(), Matchers.equalTo(999999999999999L));
        assertThat(microchip.getMicrochipNumber(), Matchers.equalTo(999999999999999L));
    }
}
