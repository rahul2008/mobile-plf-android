/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.philips.platform.mya.catk.injection.CatkComponent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class NetworkHelperTest {
    private NetworkControllerMock networkController;
    private NetworkAbstractModel givenModelRequest;
    private GetConsentsModelRequest getConsentsModelRequest;
    private final String URL = "https://hdc-css-mst.cloud.pcftest.com/consent";

    @Mock
    CatkComponent catkComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConsentAccessToolKitManipulator.setCatkComponent(catkComponent);
        networkController = new NetworkControllerMock();
        ConsentsClient.getInstance().setNetworkController(networkController);
        getConsentsModelRequest = new GetConsentsModelRequest(URL, "applicationName1", "propositionName1", new ModelDataLoadListenerMock());
    }

    @After
    public void tearDown() {
        ConsentsClient.getInstance().setNetworkController(null);
    }

    @Test
    public void sendRequest() {
        givenModelRequest(getConsentsModelRequest);
        whenCallingSendRequest();
        thenNetworkControllerWasCallWith(givenModelRequest);
    }

    private void thenNetworkControllerWasCallWith(NetworkAbstractModel expectedModelRequest) {
        assertEquals(expectedModelRequest, networkController.sendConsentRequest_model);
    }

    private void givenModelRequest(NetworkAbstractModel modelRequest) {
        givenModelRequest = modelRequest;
    }

    private void whenCallingSendRequest() {
        networkController.sendConsentRequest(givenModelRequest);
    }
}