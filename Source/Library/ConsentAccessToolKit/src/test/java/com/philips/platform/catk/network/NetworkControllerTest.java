package com.philips.platform.catk.network;


import com.android.volley.Response;
import com.google.gson.JsonArray;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.rest.request.RequestQueue;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.request.ConsentRequest;
import com.philips.platform.catk.util.CustomRobolectricRunnerCATK;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.After;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * Created by Maqsood on 10/27/17.
 */


@RunWith(CustomRobolectricRunnerCATK.class)
@PrepareForTest({CatkInterface.class})
@Config(constants = BuildConfig.class, sdk = 25)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class NetworkControllerTest {

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private NetworkControllerCustom networkController;

    @Mock
    RestInterface mockRestInterface;

    GetConsentsModelRequest consentsModelRequest;

    @Mock
    ConsentRequest mockConsentRequest;

    @Mock
    NetworkAbstractModel.DataLoadListener mockDataLoadListener;

    @Mock
    User mockUser;

    @Mock
    RequestQueue mockRequestQueue;

    @Mock
    private CatkComponent mockCatkComponent;

    @Mock
    private CatkInterface mockCatkInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(CatkInterface.class);
        networkController = new NetworkControllerCustom();
        when(mockUser.getHsdpAccessToken()).thenReturn("x73ywf56h46h5p25");
        when(mockUser.getHsdpUUID()).thenReturn("17f7ce85-403c-4824-a17f-3b551f325ce0");
        mockCatkInterface.setCatkComponent(mockCatkComponent);
        when(mockCatkInterface.getCatkComponent()).thenReturn(mockCatkComponent);
        when(mockConsentRequest.getMethod()).thenReturn(0);
        when(mockConsentRequest.getUrl()).thenReturn("https://philips.com");
        when(mockConsentRequest.getHeaders()).thenReturn(getRequestHeader());
        when(mockRestInterface.getRequestQueue()).thenReturn(mockRequestQueue);
        consentsModelRequest = new GetConsentsModelRequest(CatkConstants.APPLICATION_NAME,CatkConstants.PROPOSITION_NAME,mockDataLoadListener);
    }

    @After
    public void tearDown(){
        networkController = null;
    }

    @Test
    public void testSendRequest(){
        networkController.sendConsentRequest(0, consentsModelRequest, consentsModelRequest);
    }

    private Map<String, String> getRequestHeader() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api-version", "1");
        params.put("content-type", "application/json");
        params.put("authorization","bearer x73ywf56h46h5p25");
        params.put("performerid","17f7ce85-403c-4824-a17f-3b551f325ce0");
        params.put("cache-control", "no-cache");
        return params;
    }

    class NetworkControllerCustom extends NetworkController{

        protected void init(){
            restInterface = mockRestInterface;
        }

        protected ConsentRequest getConsentJsonRequest(final NetworkAbstractModel model, final Response.ErrorListener error, final Response.Listener<JsonArray> response) {
            return mockConsentRequest;
        }

    }
}