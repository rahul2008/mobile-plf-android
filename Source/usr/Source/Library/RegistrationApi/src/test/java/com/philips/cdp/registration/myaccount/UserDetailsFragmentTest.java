package com.philips.cdp.registration.myaccount;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserDetailsFragmentTest {

    private UserDetailsFragment myaDetailsFragment;
    private Context mContext;
    @Mock
    private com.philips.cdp.registration.injection.RegistrationComponent componentMock;


    User userMock;

    @Before
    public void setUp() throws Exception{
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        RegistrationConfiguration.getInstance().setComponent(componentMock);
        myaDetailsFragment = new UserDetailsFragment();

        userMock=new User(mContext);
        myaDetailsFragment.setUser(userMock);
        SupportFragmentTestUtil.startFragment(myaDetailsFragment);
    }
    @Test
    public void testCircleTextData() {
        Label view = myaDetailsFragment.getView().findViewById(R.id.reg_user_detail_lbl_name);
        myaDetailsFragment.setCircleText("circle text");
        assertEquals(view.getText(), "circle text");
    }

    @Test
    public void testSetUserName() {
        Label label = myaDetailsFragment.getView().findViewById(R.id.name_value);
        myaDetailsFragment.setUserName("some_name");
        assertEquals(label.getText(),"some_name");
    }

    @Test
    public void testSetEmail() {
        Label label = myaDetailsFragment.getView().findViewById(R.id.email_address_value);
        myaDetailsFragment.setEmail("some_mail@philips.com");
        assertEquals(label.getText(),"some_mail@philips.com");
    }

   @Test
    public void testSetGender() {
        Label genderLabel = myaDetailsFragment.getView().findViewById(R.id.gender_value);
        myaDetailsFragment.setGender("male");
        assertEquals(genderLabel.getText(), "male");
    }

     @Test
    public void testSetDateOfBirth() {
        Date date = new Date();
        Label dob_value = myaDetailsFragment.getView().findViewById(R.id.dob_value);
        myaDetailsFragment.setDateOfBirth(date);
    }

   @Test
    public void testSetMobileNumber() {
        Label mobile_number = myaDetailsFragment.getView().findViewById(R.id.mobile_number_value);
        myaDetailsFragment.setMobileNumber("91992929929");
        assertEquals(mobile_number.getText(),"91992929929");
    }

}