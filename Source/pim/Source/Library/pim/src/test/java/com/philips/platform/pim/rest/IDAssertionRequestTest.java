package com.philips.platform.pim.rest;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.manager.PIMSettingManager;

import junit.framework.TestCase;

import org.apache.tools.ant.taskdefs.Length;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest(PIMSettingManager.class)
@RunWith(PowerMockRunner.class)
public class IDAssertionRequestTest extends TestCase {

    private IDAssertionRequest idAssertionRequest;

    private final String accessToken = "vsu46sctqqpjwkbn";
    private final String ID_ASSERTION_ENDPOINT = "https://stg.api.eu-west-1.philips.com/consumerIdentityService/identityAssertions/";


    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        idAssertionRequest = new IDAssertionRequest(ID_ASSERTION_ENDPOINT, accessToken);
    }

    @Test
    public void testGetUrl() {
        String idAssertionRequestUrl = idAssertionRequest.getUrl();
        assertEquals(ID_ASSERTION_ENDPOINT, idAssertionRequestUrl);
    }

    @Test
    public void testGetHeader() {
        PowerMockito.mockStatic(PIMSettingManager.class);
        PIMSettingManager mockPimSettingManager = mock(PIMSettingManager.class);
        PIMOIDCConfigration mockPimoidcConfigration = mock(PIMOIDCConfigration.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getPimOidcConfigration()).thenReturn(mockPimoidcConfigration);
        when(mockPimoidcConfigration.getAPIKey()).thenReturn("nYO1gXoy5J7AaHT8KPu2D9JxN2cZo77M8zdBD2iJ");
        Map<String, String> header = idAssertionRequest.getHeader();
        int size = header.size();
        assertEquals(size, 4);
    }

    @Test
    public void testGetBody() throws JSONException {
        String body = idAssertionRequest.getBody();
        JSONObject jsonObject = new JSONObject(body);
        String accessTokenFromBody = jsonObject.getJSONObject("data").getString("accessToken");
        assertEquals(accessToken, accessTokenFromBody);
    }

    @Test
    public void testGetMethodType() {
        int methodType = idAssertionRequest.getMethodType();
        assertEquals(methodType, PIMRequest.Method.POST);
    }

    public void tearDown() throws Exception {
    }
}