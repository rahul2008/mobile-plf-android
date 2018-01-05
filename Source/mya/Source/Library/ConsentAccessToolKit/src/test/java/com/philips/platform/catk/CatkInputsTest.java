/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;


import org.junit.Before;
import org.junit.Test;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.catk.mock.AppInfraInterfaceMock;
import com.philips.platform.catk.mock.ContextMock;
import com.philips.platform.catk.model.ConsentDefinition;

import java.util.List;

public class CatkInputsTest {

    @Before
    public void setup () {
        someContext = new ContextMock();
        someAppInfraInterface = new AppInfraInterfaceMock();
        this.inputBuilder = new CatkInputs.Builder();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void build_whenConsentDefinitionsNotSetThrowsException (){
        givenContext(someContext);
        givenAppInfraInterface(someAppInfraInterface);
        whenBuilding();
    }

    private void givenAppInfraInterface(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    private void givenContext(ContextMock context) {
        this.context = context;
    }

    private void whenBuilding() {
        inputBuilder.setAppInfraInterface(appInfra).setContext(context).setConsentDefinitions(consentDefinitions).build();
    }

    CatkInputs.Builder inputBuilder;
    AppInfraInterface appInfra;
    ContextMock context;
    List<ConsentDefinition> consentDefinitions;

    ContextMock someContext;
    AppInfraInterface someAppInfraInterface;
}