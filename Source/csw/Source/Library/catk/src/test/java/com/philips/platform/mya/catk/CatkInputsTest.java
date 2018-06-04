/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.catk.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.catk.mock.ContextMock;

import org.junit.Before;
import org.junit.Test;

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
        whenBuilding();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void build_whenAppInfraNotSetThrowsException() {
        givenContext(someContext);
        whenBuilding();
    }

    private void givenAppInfraInterface(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    private void givenContext(ContextMock context) {
        this.context = context;
    }

    private void whenBuilding() {
        inputBuilder.setAppInfraInterface(appInfra).setContext(context).build();
    }

    private CatkInputs.Builder inputBuilder;
    private AppInfraInterface appInfra;
    private ContextMock context;

    private ContextMock someContext;
    private AppInfraInterface someAppInfraInterface;
}