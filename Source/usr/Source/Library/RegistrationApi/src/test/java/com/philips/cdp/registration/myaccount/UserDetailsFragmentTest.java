/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.myaccount;

import android.content.Context;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class UserDetailsFragmentTest {

    private UserDetailsFragment myaDetailsFragment;
    private Context mContext;
    @Mock
    private RegistrationComponent componentMock;

    User userMock;
    @Mock
    AppTaggingInterface appTaggingInterface;
    @Mock
    AppInfraInterface appInfraInterface;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        RegistrationConfiguration.getInstance().setComponent(componentMock);
        myaDetailsFragment = new UserDetailsFragment();
        AppTagging.setMockAppTaggingInterface(appTaggingInterface);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent("usr", BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        userMock = new User(mContext);
        myaDetailsFragment.setUser(userMock);
//        SupportFragmentTestUtil.startFragment(myaDetailsFragment);
    }

    @Test
    public void testCircleTextData() {
        Label view = myaDetailsFragment.getView().findViewById(R.id.usr_myDetailsScreen_label_name);
        myaDetailsFragment.setCircleText("circle text");
        assertEquals(view.getText(), "circle text");
    }

    @Test
    public void testSetUserName() {
        Label label = myaDetailsFragment.getView().findViewById(R.id.usr_myDetailsScreen_label_nameValue);
        myaDetailsFragment.setUserName("some_name");
        assertEquals(label.getText(), "some_name");
    }

    @Test
    public void testSetEmail() {
        Label label = myaDetailsFragment.getView().findViewById(R.id.usr_myDetailsScreen_label_emailAddressValue);
        myaDetailsFragment.setEmail("some_mail@philips.com");
        assertEquals(label.getText(), "some_mail@philips.com");
    }

    @Test
    public void testSetGender() {
        Label genderLabel = myaDetailsFragment.getView().findViewById(R.id.usr_myDetailsScreen_label_genderValue);
        myaDetailsFragment.setGender("male");
        assertEquals(genderLabel.getText(), "male");
    }

    @Test
    public void testSetDateOfBirth() {
        Date date = new Date();
        Label dob_value = myaDetailsFragment.getView().findViewById(R.id.usr_myDetailsScreen_label_dobValue);
        myaDetailsFragment.setDateOfBirth(date);
    }

    @Test
    public void testSetMobileNumber() {
        Label mobile_number = myaDetailsFragment.getView().findViewById(R.id.usr_myDetailsScreen_label_mobileNumberValue);
        myaDetailsFragment.setMobileNumber("91992929929");
        assertEquals(mobile_number.getText(), "91992929929");
    }

}