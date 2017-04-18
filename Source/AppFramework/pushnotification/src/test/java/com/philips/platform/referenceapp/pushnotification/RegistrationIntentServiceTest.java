package com.philips.platform.referenceapp.pushnotification;

import com.philips.platform.referenceapp.RegistrationIntentService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by philips on 14/04/17.
 */
@RunWith(RobolectricTestRunner.class)
public class RegistrationIntentServiceTest {
    @Test
    public void onHandleIntent() throws Exception {
        RegistrationIntentService registrationIntentService=new RegistrationIntentService();
//        registrationIntentService.onHandleIntent(new Intent());
    }

}