package com.atmostadam.cats.drools.listener;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CatAgendaEventListener extends DefaultAgendaEventListener {
    Logger logger = LoggerFactory.getLogger(CatAgendaEventListener.class);

    private Map<String, Object> history = new HashMap<>();

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent event) {
        Rule rule = event.getMatch().getRule();
        String ruleName = rule.getName();
        Map<String, Object> ruleMetaData = rule.getMetaData();
        logger.info(ruleName, ruleMetaData);
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        Rule rule = event.getMatch().getRule();
        String ruleName = rule.getName();
        Map<String, Object> ruleMetaData = rule.getMetaData();
        history.put(ruleName, ruleMetaData);
        logger.info(ruleName, ruleMetaData);
    }
}
