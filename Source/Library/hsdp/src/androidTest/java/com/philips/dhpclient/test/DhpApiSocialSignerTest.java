package com.philips.dhpclient.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 310190722 on 9/8/2015.
 */
public class DhpApiSocialSignerTest extends InstrumentationTestCase {

    private DhpAuthenticationManagementClient authenticationManagementClient;

    private final DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration(
            "http://ugrow-userregistration15.cloud.pcftest.com",
            "uGrowApp",
            "f129afcc-55f4-11e5-885d-feff819cdc9f",
            "f129b5a8-55f4-11e5-885d-feff819cdc9f");


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        authenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
    }

    @MediumTest
    public void testCLientTest() {
       // assertNotNull(authenticationManagementClient.authenticate("maqsoodphilips@gmail.com", "mohammed123"));
    }

    public void testSocialLogin() throws Exception {
       // assertNotNull(authenticationManagementClient.loginSocialProviders("maqsoodphilips@gmail.com", "Aabcscshishisio"));
    }
    public void testSocialLoginResponse() {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", "10");
        headers.put("SignedDate", "2015-07-02T07:52:03.100+0000");

     //  final DhpAuthenticationResponse dhpAuthenticationResponse = authenticationManagementClient.loginSocialProviders("maqsoodphilips@gmail.com", "Aabcscshishisio");
      // assertNotNull(dhpAuthenticationResponse);
    }
}
