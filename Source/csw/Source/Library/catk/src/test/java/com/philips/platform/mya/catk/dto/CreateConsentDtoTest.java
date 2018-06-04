/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.dto;

import org.junit.*;

public class CreateConsentDtoTest {

    private CreateConsentDto createConsentDto;

    @Before
    public void setUp() throws Exception {
        createConsentDto = new CreateConsentDto();
    }

    @After
    public void tearDown() throws Exception {
        createConsentDto = null;
    }

    @Test
    public void getResourceType() throws Exception {
        createConsentDto.setResourceType("Consent");
        Assert.assertNotNull(createConsentDto.getResourceType());
    }

    @Test
    public void getLanguage() throws Exception {
        createConsentDto.setLanguage("af-ZA");
        Assert.assertNotNull(createConsentDto.getLanguage());
    }

    @Test
    public void getStatus() throws Exception {
        createConsentDto.setStatus("active");
        Assert.assertNotNull(createConsentDto.getStatus());
    }

    @Test
    public void getSubject() throws Exception {
        createConsentDto.setSubject("17f7ce85-403c-4824-a17f-3b551f325ce0");
        Assert.assertNotNull(createConsentDto.getSubject());
    }

    @Test
    public void getPolicyRule() throws Exception {
        createConsentDto.setPolicyRule("urn:com.philips.consent:moment/");
        Assert.assertNotNull(createConsentDto.getPolicyRule());
    }

}