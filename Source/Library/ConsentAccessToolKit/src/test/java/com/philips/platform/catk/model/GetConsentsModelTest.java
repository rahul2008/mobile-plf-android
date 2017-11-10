/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.model;

import com.philips.platform.catk.response.ConsentStatus;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Maqsood on 11/2/17.
 */

public class GetConsentsModelTest {

    private GetConsentsModel getConsentsModel;

    @Before
    public void setUp() throws Exception {
        getConsentsModel = new GetConsentsModel();
    }

    @After
    public void tearDown() throws Exception {
        getConsentsModel = null;
    }

    @Test
    public void getDateTime() throws Exception {
        getConsentsModel.setDateTime("017-11-01T17:27:16.000Z");
        Assert.assertNotNull(getConsentsModel.getDateTime());
    }

    @Test
    public void getResourceType() throws Exception {
        getConsentsModel.setResourceType("Consent");
        Assert.assertNotNull(getConsentsModel.getResourceType());
    }

    @Test
    public void getLanguage() throws Exception {
        getConsentsModel.setLanguage("af-ZA");
        Assert.assertNotNull(getConsentsModel.getLanguage());
    }

    @Test
    public void getStatus() throws Exception {
        getConsentsModel.setStatus(ConsentStatus.active);
        Assert.assertNotNull(getConsentsModel.getStatus());
    }

    @Test
    public void getSubject() throws Exception {
        getConsentsModel.setSubject("17f7ce85-403c-4824-a17f-3b551f325ce0");
        Assert.assertNotNull(getConsentsModel.getSubject());
    }

    @Test
    public void getPolicyRule() throws Exception {
        getConsentsModel.setPolicyRule("urn:com.philips.consent:moment/");
        Assert.assertNotNull(getConsentsModel.getPolicyRule());
    }

}