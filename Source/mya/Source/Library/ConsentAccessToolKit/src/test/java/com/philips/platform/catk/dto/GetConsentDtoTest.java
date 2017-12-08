/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.dto;

import org.junit.*;

import com.philips.platform.catk.model.ConsentStatus;

public class GetConsentDtoTest {

    private GetConsentDto getConsentModel;

    @Before
    public void setUp() throws Exception {
        getConsentModel = new GetConsentDto();
    }

    @After
    public void tearDown() throws Exception {
        getConsentModel = null;
    }

    @Test
    public void getDateTime() throws Exception {
        getConsentModel.setDateTime("017-11-01T17:27:16.000Z");
        Assert.assertNotNull(getConsentModel.getDateTime());
    }

    @Test
    public void getResourceType() throws Exception {
        getConsentModel.setResourceType("Consent");
        Assert.assertNotNull(getConsentModel.getResourceType());
    }

    @Test
    public void getLanguage() throws Exception {
        getConsentModel.setLanguage("af-ZA");
        Assert.assertNotNull(getConsentModel.getLanguage());
    }

    @Test
    public void getStatus() throws Exception {
        getConsentModel.setStatus(ConsentStatus.active);
        Assert.assertNotNull(getConsentModel.getStatus());
    }

    @Test
    public void getSubject() throws Exception {
        getConsentModel.setSubject("17f7ce85-403c-4824-a17f-3b551f325ce0");
        Assert.assertNotNull(getConsentModel.getSubject());
    }

    @Test
    public void getPolicyRule() throws Exception {
        getConsentModel.setPolicyRule("urn:com.philips.consent:moment/");
        Assert.assertNotNull(getConsentModel.getPolicyRule());
    }

}