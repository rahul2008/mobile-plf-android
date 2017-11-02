package com.philips.platform.catk.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by philips on 11/2/17.
 */

public class CreateConsentModelTest {

    private CreateConsentModel createConsentModel;

    @Before
    public void setUp() throws Exception {
        createConsentModel = new CreateConsentModel();
    }

    @After
    public void tearDown() throws Exception {
        createConsentModel = null;
    }

    @Test
    public void getResourceType() throws Exception {
        createConsentModel.setResourceType("Consent");
        Assert.assertNotNull(createConsentModel.getResourceType());
    }

    @Test
    public void getLanguage() throws Exception {
        createConsentModel.setLanguage("af-ZA");
        Assert.assertNotNull(createConsentModel.getLanguage());
    }

    @Test
    public void getStatus() throws Exception {
        createConsentModel.setStatus("active");
        Assert.assertNotNull(createConsentModel.getStatus());
    }

    @Test
    public void getSubject() throws Exception {
        createConsentModel.setSubject("17f7ce85-403c-4824-a17f-3b551f325ce0");
        Assert.assertNotNull(createConsentModel.getSubject());
    }

    @Test
    public void getPolicyRule() throws Exception {
        createConsentModel.setPolicyRule("urn:com.philips.consent:moment/");
        Assert.assertNotNull(createConsentModel.getPolicyRule());
    }

}