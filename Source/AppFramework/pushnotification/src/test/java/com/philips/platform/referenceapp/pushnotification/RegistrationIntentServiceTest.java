package com.philips.platform.referenceapp.pushnotification;

import android.content.Intent;

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
    public void testHandleIntent() throws Exception {
        RegistrationIntentServiceOveriden registrationIntentService=new RegistrationIntentServiceOveriden();
        registrationIntentService.onHandleIntent(new Intent());
    }

    public static class  RegistrationIntentServiceOveriden extends RegistrationIntentService{
        @Override
        protected void onHandleIntent(Intent intent) {
            super.onHandleIntent(intent);
        }
    }

}