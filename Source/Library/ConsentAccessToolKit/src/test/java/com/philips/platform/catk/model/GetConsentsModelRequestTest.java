package com.philips.platform.catk.model;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.philips.cdp.registration.User;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.CatkInterface;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.when;
/**
 * Created by Maqsood on 11/1/17.
 */

@RunWith(CustomRobolectricRunnerCATK.class)
@PrepareForTest({NetworkHelper.class,CatkInterface.class})
@Config(constants = BuildConfig.class, sdk = 25)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class GetConsentsModelRequestTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private GetConsentsModelRequest consentModelRequest;

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
        PowerMockito.mockStatic(NetworkHelper.class);
        PowerMockito.mockStatic(CatkInterface.class);
        mockCatkInterface.setCatkComponent(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent()).thenReturn(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent().getUser()).thenReturn(mockUser);
        when(mockUser.getHsdpAccessToken()).thenReturn("x73ywf56h46h5p25");
        when(mockUser.getHsdpUUID()).thenReturn("17f7ce85-403c-4824-a17f-3b551f325ce0");
        consentModelRequest = new GetConsentsModelRequest(CatkConstants.APPLICATION_NAME,
                CatkConstants.PROPOSITION_NAME,mockDataLoadListener);
    }

    @Test
    public void parseResponse() throws Exception {
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTime","2017-11-01T17:27:16.000Z");
        jsonObject.addProperty("language","af-ZA");
        jsonObject.addProperty("policyRule","urn:com.philips.consent");
        jsonObject.addProperty("status","active");
        jsonObject.addProperty("subject","17f7ce85-403c-4824-a17f-3b551f325ce0");
        jsonArray.add(jsonObject);
        Assert.assertNotNull(consentModelRequest.parseResponse(jsonArray));
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
        Assert.assertNull(consentModelRequest.requestBody());
    }

    @Test
    public void testGetUrl() throws Exception {
        Assert.assertNotNull(consentModelRequest.getUrl());
    }
}