package com.atmostadam.cats.drools.service;

import com.atmostadam.cats.api.configuration.CatWebClientProperties;
import com.atmostadam.cats.api.exception.CatException;
import com.atmostadam.cats.api.model.Cat;
import com.atmostadam.cats.api.model.in.CatRequest;
import com.atmostadam.cats.api.model.out.CatResponse;
import com.atmostadam.cats.api.service.CatService;
import com.atmostadam.cats.api.service.CatSpringBeanServiceNames;
import com.atmostadam.cats.framework.model.adoptapet.AdoptAPetRequest;
import com.atmostadam.cats.framework.model.adoptapet.LimitedPetDetails;
import com.atmostadam.cats.framework.model.adoptapet.LimitedPetImage;
import com.atmostadam.cats.framework.rest.CatWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(CatSpringBeanServiceNames.POST_ADOPT_A_PET_CAT)
public class PostAdoptAPetCat implements CatService {
    @Autowired
    private CatWebClient client;

    @Autowired
    @Qualifier("CatWebClientPropertiesAdoptAPet")
    private CatWebClientProperties properties;

    @Autowired
    private CatDroolsService drools;

    @Override
    public ResponseEntity<CatResponse> invoke(String requestId, CatRequest request) {
        LimitedPetImage adoptAPetImage = new LimitedPetImage();
        LimitedPetDetails adoptAPetDetails = new LimitedPetDetails();
        AdoptAPetRequest adoptAPetRequest = new AdoptAPetRequest();

        Cat cat = request.getCats().get(0); // Safe. Checked Up Front.

        Map<String, Object> facts = new HashMap<>();
        facts.put("adoptAPetImage", adoptAPetImage);
        facts.put("adoptAPetDetails", adoptAPetDetails);
        facts.put("adoptAPetRequest", adoptAPetRequest);
        facts.put("cat", cat);

        try {
            drools.fireAllRules("adopt-a-pet-rules", facts);
        } catch(CatException e) {
            return new CatResponse()
                    .setException(e)
                    .addCat(cat)
                    .newResponseEntity(requestId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return client.invoke(properties, requestId, adoptAPetRequest);
    }
}
