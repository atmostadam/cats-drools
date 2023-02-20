package com.atmostadam.cats.drools.service;

import com.atmostadam.cats.api.exception.CatException;
import com.atmostadam.cats.drools.listener.CatAgendaEventListener;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class CatDroolsService {
    Logger logger = LoggerFactory.getLogger(CatDroolsService.class);

    public static final String SRC_DIR = "./src";

    public static final String DROOLS_EXT = ".drl";

    private KieContainer kieContainer;

    @PostConstruct
    protected void setup() throws CatException {
        KieServices ks = KieServices.Factory.get();
        Map<String, Resource> kieFiles = loadKieFiles(SRC_DIR, DROOLS_EXT);
        if (kieFiles.size() == 0) {
            throw new IllegalStateException(
                    "Illegal State in Classloader. Kie Maven dependency found zero drl rules files.");
        }
        KieFileSystem kfs = writeResources(ks, kieFiles);
        ks.newKieBuilder(kfs).buildAll();
        kieContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
    }

    public void fireAllRules(String agendaGroup, Map<String, Object> facts) throws CatException {
        KieSession kieSession = kieContainer.newKieSession();
        CatAgendaEventListener eventListener = new CatAgendaEventListener();
        kieSession.addEventListener(eventListener);

        logger.info("Agenda being executed is [{}]", agendaGroup);
        try {
            kieSession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
        } catch (Exception e) {
            logger.info("Unable to set agenda group [{}] with error message [{}]", agendaGroup, e.getMessage(), e);
            throw new CatException(String.format("Exception thrown [%s] with message [%s] because unable to set agenda group [%s]",
                    e.getClass().getSimpleName(), e.getMessage(), agendaGroup));
        }

        for(Map.Entry<String, Object> fact : facts.entrySet()) {
            logger.info("Input Fact Named [{}] With Fact Value [{}]", fact.getKey(), fact.getValue());
            kieSession.insert(fact.getValue());
        }

        int numOfRulesFired;
        try {
            numOfRulesFired = kieSession.fireAllRules();
            logger.info("Total Rules Fired [{}]", numOfRulesFired);
        } catch (Exception e) {
            logger.info("Unknown exception returned from Kie or Drools when executing fireAllRules() [{}] [{}]",
                    e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new CatException(String.format("Exception thrown [%s] with message [%s] when returned from Kie or Drools.",
                    e.getClass().getSimpleName(), e.getMessage(), agendaGroup));
        } finally {
            kieSession.dispose();
        }

        for(Map.Entry<String, Object> fact : facts.entrySet()) {
            logger.info("Output Fact Named [{}] With Fact Value [{}]", fact.getKey(), fact.getValue());
        }
    }

    public Map<String, Resource> loadKieFiles(String dir, String ext) throws CatException {
        logger.info("Begin Kie Resources loading...");
        Map<String, Resource> kr = new HashMap<>();
        try (Stream<Path> stream = Files.walk(Paths.get(dir))) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains(ext))
                    .forEach(e -> {
                        File file = e.toFile();
                        Resource resource = ResourceFactory.newFileResource(file);
                        kr.put(file.getName(), resource);
                        logger.info("Loading [{}] File Name [{}] Resource [{}]", ext, file.getName(), resource);
                    });
        } catch (Exception e) {
            logger.warn("Unable to load files due to " + e.getMessage(), e);
            throw new CatException(String.format("Exception thrown [%s] with message [%s] when returned attempting to load files.",
                    e.getClass().getSimpleName(), e.getMessage()));
        }
        logger.info("Processed [{}] [{}] Files", kr.size(), ext);
        return kr;
    }

    public KieFileSystem writeResources(KieServices ks, Map<String, Resource> resources) {
        KieFileSystem kfs = ks.newKieFileSystem();
        for (Resource resource : resources.values()) {
            kfs.write(resource);
        }
        return kfs;
    }
}
