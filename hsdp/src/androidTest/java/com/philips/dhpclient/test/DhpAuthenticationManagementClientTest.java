package com.philips.dhpclient.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.dhpclient.DhpApiClientConfiguration;
import com.philips.dhpclient.DhpAuthenticationManagementClient;


import static junit.framework.TestCase.assertNotNull;

/**
 * Created by 310202337 on 10/7/2015.
 */
public class DhpAuthenticationManagementClientTest extends InstrumentationTestCase {



    private DhpAuthenticationManagementClient authenticationManagementClient;

    private final DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration(
            /*"http://ugrow_user_registration.hsdpcph-consumer.cloud.pcftest.com?",*/
            "http://ugrowuserregistration.cloud.pcftest.com/",
            "uGrowApplication",
            "2eaec11e-1a2e-11e5-b60b-1697f925ec7b",
            "2eaec60a-1a2e-11e5-b60b-1697f925ec7b");



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        authenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
    }

    @MediumTest
    public void testCLientTest() {
      //  authenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);
        System.out.println("******************** authenticate : " + authenticationManagementClient.authenticate("maqsoodphilips@gmail.com", "mohammed123"));
        assertNotNull(authenticationManagementClient.authenticate("maqsoodphilips@gmail.com", "mohammed123"));

       // assertNotNull(authenticationManagementClient.authenticate("maqsoodphilips@gmail.com", "mohammed123"));
    }

    public void testName() throws Exception {
        System.out.println("******************** authenticate : " + authenticationManagementClient.authenticate("maqsoodphilips@gmail.com", "mohammed123"));
        assertNotNull(authenticationManagementClient.authenticate("maqsoodphilips@gmail.com", "mohammed123"));

    }

    /**
     * Created by 310202337 on 10/7/2015.
     */

}
