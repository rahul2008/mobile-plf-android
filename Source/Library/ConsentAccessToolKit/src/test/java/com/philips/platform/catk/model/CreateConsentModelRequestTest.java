package com.philips.platform.catk.model;

import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.MockitoConfiguration;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.when;


/**
 * Created by Maqsood on 10/31/17.
 */

@RunWith(CustomRobolectricRunnerCATK.class)
@PrepareForTest(CatkInterface.class)
@Config(constants = BuildConfig.class, sdk = 25)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class CreateConsentModelRequestTest extends MockitoConfiguration {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private CreateConsentModelRequest consentModelRequest;

    @Mock
    User mockUser;

    @Mock
    NetworkAbstractModel.DataLoadListener mockDataLoadListener;

    @Mock
    private CatkComponent mockCatkComponent;

    @Mock
    private CatkInterface mockCatkInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(CatkInterface.class);
        consentModelRequest = new CreateConsentModelRequest(CatkConstants.APPLICATION_NAME,"active",
               "af-ZA",CatkConstants.PROPOSITION_NAME,mockDataLoadListener);
        mockCatkInterface.setCatkComponent(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent()).thenReturn(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent().getUser()).thenReturn(mockUser);
        when(mockUser.getHsdpAccessToken()).thenReturn("x73ywf56h46h5p25");
        when(mockUser.getHsdpUUID()).thenReturn("17f7ce85-403c-4824-a17f-3b551f325ce0");
    }

    @Test
    public void parseResponse() throws Exception {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("success");
        Assert.assertNull(consentModelRequest.parseResponse(jsonArray));
    }

    @Test
    public void testGetMethod() throws Exception {
        Assert.assertNotNull(consentModelRequest.getMethod());
    }

    @Test
    public void testRequestHeader() throws Exception {
        Assert.assertNotNull(consentModelRequest.requestHeader());
    }

    @Test
    public void testRequestBody() throws Exception {
        Assert.assertNotNull(consentModelRequest.requestBody());
    }

    @Test
    public void testGetUrl() throws Exception {
      Assert.assertNotNull(consentModelRequest.getUrl());
    }
}