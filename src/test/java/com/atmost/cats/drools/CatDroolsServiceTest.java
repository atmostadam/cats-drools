package io.atmost.cats.drools;

import io.atmostadam.cats.api.model.in.CatRequest;
import io.atmostadam.cats.api.model.out.CatResponse;
import io.atmostadam.cats.drools.CatDroolsService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringJUnitConfig({CatDroolsConfigurationTest.class})
class CatDroolsServiceTest {
    @Autowired
    CatDroolsService service;

    @Test
    void testFireAllRules() throws Exception {
        Map<String, Object> facts = new HashMap<>();
        facts.put("catRequest", new CatRequest());
        facts.put("catResponse", new CatResponse());
        service.fireAllRules("cat-request-rules", facts);
        assertThat(facts.size(), Matchers.equalTo(2));
        CatRequest catRequest = (CatRequest) facts.get("catRequest");
        assertThat(catRequest.getMicrochipNumber(), Matchers.equalTo(1000000000011L));
    }

}
