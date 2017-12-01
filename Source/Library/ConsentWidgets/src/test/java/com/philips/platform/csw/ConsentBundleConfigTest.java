/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

import com.philips.platform.catk.model.ConsentDefinition;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.emory.mathcs.backport.java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by phnl310185349 on 01/12/2017.
 */
public class ConsentBundleConfigTest {

    @Test
    public void consentDefinitionsShouldNeverBeNull() throws Exception {
        givenConsentBundleConfigWithDefinitions(null);
        thenConsentDefinitionsAreNotNull();
    }

    @Test
    public void consentDefinitionsCanBeEmpty() throws Exception {
        givenConsentBundleConfigWithDefinitions(givenConsentDefinitions(0));
        thenConsentDefinitionsAreNotNull();
        thenAmountOfConsentDefinitionsIs(0);
    }

    @Test
    public void consentDefinitionsCanHaveTwoItems() throws Exception {
        givenConsentBundleConfigWithDefinitions(givenConsentDefinitions(2));
        thenConsentDefinitionsAreNotNull();
        thenAmountOfConsentDefinitionsIs(2);
    }

    @Test
    public void consentDefinitionsCanHaveLoadsOfItems() throws Exception {
        givenConsentBundleConfigWithDefinitions(givenConsentDefinitions(1000));
        thenConsentDefinitionsAreNotNull();
        thenAmountOfConsentDefinitionsIs(1000);
    }

    private void givenConsentBundleConfigWithDefinitions(List<ConsentDefinition> definitionList) {
        givenConsentBundleConfigWithDefinitions = new ConsentBundleConfig(definitionList);
    }

    private List<ConsentDefinition> givenConsentDefinitions(int amount) {
        givenConsentDefinitions = new ArrayList<>();
        for(int i=0 ; i < amount ; i++){
            givenConsentDefinitions.add(randomConsentDefinition());
        }
        return givenConsentDefinitions;
    }

    private void thenConsentDefinitionsAreNotNull() {
        assertNotNull(givenConsentBundleConfigWithDefinitions.getConsentDefinitions());
    }

    private void thenAmountOfConsentDefinitionsIs(int amount) {
        assertEquals(amount, givenConsentDefinitions.size());
    }

    private ConsentDefinition randomConsentDefinition() {
        int random = (int) (Math.random() * randomDefinitions.size());
        return randomDefinitions.get(random);
    }

    private ConsentBundleConfig givenConsentBundleConfigWithDefinitions;
    private List<ConsentDefinition> givenConsentDefinitions;
    private List<ConsentDefinition> randomDefinitions = new ArrayList<ConsentDefinition>(){{
        add(new ConsentDefinition("text1", "help1", Collections.singletonList("moment"), 1, Locale.US));
        add(new ConsentDefinition("text2", "help2", Collections.singletonList("insight"), 2, Locale.UK));
        add(new ConsentDefinition("text3", "help3", Collections.singletonList("coaching"), 3, Locale.FRANCE));
        add(new ConsentDefinition("text4", "help4", Collections.singletonList("themon"), 4, Locale.CANADA));
        add(new ConsentDefinition("text5", "help5", Collections.singletonList("out of creative ideas"), 5, Locale.CHINA));
    }};

}