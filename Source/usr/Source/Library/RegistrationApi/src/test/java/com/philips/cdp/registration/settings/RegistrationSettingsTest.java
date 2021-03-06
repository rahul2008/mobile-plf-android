package com.philips.cdp.registration.settings;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by philips on 12/3/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class RegistrationSettingsTest {

    RegistrationSettings registrationSettings;

    @Mock
    private Context contextMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        registrationSettings=new RegistrationSettings() {
            @Override
            public void initialiseConfigParameters(String locale) {

            }
        };

    }

    @Test(expected = NullPointerException.class)
    public void intializeRegistrationSettings() throws Exception {

        registrationSettings.intializeRegistrationSettings(contextMock,"capture","en-US");
    }

    @Test
    public void initialiseConfigParameters() throws Exception {
    }

    @Test
    public void getProductRegisterUrl() throws Exception {
    }

    @Test
    public void getProductRegisterListUrl() throws Exception {
    }

    @Test
    public void getPreferredCountryCode() throws Exception {
    }

    @Test
    public void getPreferredLangCode() throws Exception {
    }

    @Test
    public void getResendConsentUrl() throws Exception {
    }

    @Test
    public void getmRegisterBaseCaptureUrl() throws Exception {
    }

    @Test
    public void storeMicrositeId() throws Exception {
    }

    @Test
    public void refreshLocale() throws Exception {
    }

}