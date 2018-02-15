/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import java.util.ArrayList;
import java.util.List;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.catk.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.catk.mock.ContextMock;

import edu.emory.mathcs.backport.java.util.Collections;

public class CatkInputsTest {

    @Before
    public void setup() {
        someContext = new ContextMock();
        someAppInfraInterface = new AppInfraInterfaceMock();
        this.inputBuilder = new CatkInputs.Builder();
    }


    @Test(expected = CatkInputs.InvalidInputException.class)
    public void build_whenContextNotSetThrowsException() {
        givenAppInfraInterface(someAppInfraInterface);
        givenConsentDefinitionTypes();
        whenBuilding();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void build_whenAppInfraNotSetThrowsException() {
        givenContext(someContext);
        givenConsentDefinitionTypes();
        whenBuilding();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void build_whenConsentDefinitionsNotSetThrowsException() {
        givenContext(someContext);
        givenAppInfraInterface(someAppInfraInterface);
        whenBuilding();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void build_whenConsentDefinitionsHaveDuplicateTypeThrowsException() throws Exception {
        givenContext(someContext);
        givenAppInfraInterface(someAppInfraInterface);
        givenConsentDefinitionTypes("moment", "moment");
        whenBuilding();
    }

    @Test
    public void build_shouldReturnInstanceWhenEmptyConsentDefinitionsAreSet() throws Exception {
        givenContext(someContext);
        givenAppInfraInterface(someAppInfraInterface);
        givenConsentDefinitionTypes();
        whenBuilding();
    }

    @Test
    public void build_shouldReturnInstanceWhenNoDuplicateConsentDefinitionsAreSet() throws Exception {
        givenContext(someContext);
        givenAppInfraInterface(someAppInfraInterface);
        givenConsentDefinitionTypes("moment", "coaching");
        whenBuilding();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnUnModifiableListWhenGettingConsentDefinitions() throws Exception {
        givenValidCatkInputs();
        List<ConsentDefinition> definitions = whenGettingConsentDefinitions();
        thenModifyingListThrowsException(definitions);
    }

    private void givenConsentDefinitionTypes(String... types) {
        List<ConsentDefinition> definitions = new ArrayList<>();
        for (String type : types) {
            definitions.add(new ConsentDefinition("", "", Collections.singletonList(type), 2));
        }
        consentDefinitions = definitions;
    }

    private void givenAppInfraInterface(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    private void givenContext(ContextMock context) {
        this.context = context;
    }

    private void givenValidCatkInputs() {
        givenContext(someContext);
        givenAppInfraInterface(someAppInfraInterface);
        givenConsentDefinitionTypes("moment", "coaching");
        whenBuilding();
    }

    private void whenBuilding() {
        givenCatkInputs = inputBuilder.setAppInfraInterface(appInfra).setContext(context).setConsentDefinitions(consentDefinitions).build();
    }

    private List<ConsentDefinition> whenGettingConsentDefinitions() {
        return givenCatkInputs.getConsentDefinitions();
    }

    private void thenModifyingListThrowsException(List<ConsentDefinition> definitions) {
        definitions.remove(0);
    }

    CatkInputs givenCatkInputs;
    CatkInputs.Builder inputBuilder;
    AppInfraInterface appInfra;
    ContextMock context;
    List<ConsentDefinition> consentDefinitions;

    ContextMock someContext;
    AppInfraInterface someAppInfraInterface;
}