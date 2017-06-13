package com.philips.cdp.registration.coppa.utils;


import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;

import org.junit.Before;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class CoppaLaunchInputTest extends RegistrationApiInstrumentationBase {

    CoppaLaunchInput coppaLaunchInput;

    @Mock
    RegistrationFunction registrationFunction;

//    @Mock
//    UserRegistrationUIEventListener userRegistrationListener;

    @Mock
    UserRegistrationUIEventListener mUserRegistrationCoppaUIEventListener;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        coppaLaunchInput = new CoppaLaunchInput();
    }

    public void testinit(){
        assertNotNull(coppaLaunchInput);
    }

    public void testIsAccountSettingsTrue(){
        coppaLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
        assertEquals(RegistrationLaunchMode.ACCOUNT_SETTINGS,coppaLaunchInput.getEndPointScreen());
    }
    public void testIsParentalFragmentTrue(){
        coppaLaunchInput.setParentalFragment(true);
        assertEquals(true,coppaLaunchInput.isParentalFragment());
    }

    public void testIsParentalFragmentFalse(){
        coppaLaunchInput.setParentalFragment(false);
        assertEquals(false,coppaLaunchInput.isParentalFragment());
    }
    public void testRegistrationFunction(){
        coppaLaunchInput.setRegistrationFunction(registrationFunction);
        assertEquals(registrationFunction,coppaLaunchInput.getRegistrationFunction());
    }
    public void testUserRegistrationListener(){
        coppaLaunchInput.setUserRegistrationUIEventListener(mUserRegistrationCoppaUIEventListener);
        assertEquals(mUserRegistrationCoppaUIEventListener,coppaLaunchInput.getUserRegistrationUIEventListener());
    }

}