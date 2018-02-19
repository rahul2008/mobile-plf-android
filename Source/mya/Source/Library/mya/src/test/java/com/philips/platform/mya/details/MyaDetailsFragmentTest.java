package com.philips.platform.mya.details;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.R;
import com.philips.platform.mya.activity.MyaActivity;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaDetailsFragmentTest {

    private MyaDetailsFragment myaDetailsFragment;
    private Context mContext;
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception{
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        myaDetailsFragment = new MyaDetailsFragment();
        SupportFragmentTestUtil.startFragment(myaDetailsFragment, MyaActivity.class);
    }

    @Test
    public void testEquals_getActionbarTitleResId() throws Exception{
        assertEquals(R.string.MYA_My_account,myaDetailsFragment.getActionbarTitleResId());
    }

    @Test
    public void testNotNull_getActionbarTitle() throws Exception{
        assertNotNull(myaDetailsFragment.getActionbarTitle(mContext));
    }

    @Test
    public void notNullgetBackButtonState() throws Exception {
        assertNotNull(myaDetailsFragment.getBackButtonState());
        testCircleTextData();
    }

    @Test
    public void testCircleTextData() {
        Label view = myaDetailsFragment.getView().findViewById(R.id.mya_name);
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
        View email_arrow = myaDetailsFragment.getView().findViewById(R.id.email_mobile_right_arrow);
        View email_address_heading = myaDetailsFragment.getView().findViewById(R.id.email_address_heading);
        myaDetailsFragment.setEmail("some_mail@philips.com");
        assertEquals(label.getText(),"some_mail@philips.com");
//        assertTrue(email_arrow.getVisibility() == View.VISIBLE);
        myaDetailsFragment.setEmail("");
        assertTrue(label.getVisibility() == View.GONE);
        assertTrue(email_address_heading.getVisibility() == View.GONE);
    }

    @Test
    public void testSetGender() {
        Label genderLabel = myaDetailsFragment.getView().findViewById(R.id.gender_value);
        myaDetailsFragment.setGender("male");
        assertEquals(genderLabel.getText(), "male");
        myaDetailsFragment.setGender("");
        assertEquals(genderLabel.getText(), "Not available");
        myaDetailsFragment.setGender(null);
        assertEquals(genderLabel.getText(), "Not available");
    }

    @Test
    public void testSetDateOfBirth() {
        Date date = new Date();
        Label dob_value = myaDetailsFragment.getView().findViewById(R.id.dob_value);
        myaDetailsFragment.setDateOfBirth(date);
        assertTrue(dob_value.getText()!=null);
        myaDetailsFragment.setDateOfBirth(null);
        assertEquals(dob_value.getText(), "Not available");
    }

    @Test
    public void testSetMobileNumber() {
        View mobile_number_heading = myaDetailsFragment.getView().findViewById(R.id.mobile_number_heading);
        View mobile_arrow = myaDetailsFragment.getView().findViewById(R.id.mobile_right_arrow);
        Label mobile_number = myaDetailsFragment.getView().findViewById(R.id.mobile_number_value);
        View email_divider = myaDetailsFragment.getView().findViewById(R.id.email_mobile_right_arrow);
        myaDetailsFragment.setMobileNumber("91992929929");
        assertEquals(mobile_number.getText(),"91992929929");
//        assertTrue(email_divider.getVisibility() == View.VISIBLE);
        myaDetailsFragment.setMobileNumber("");
        assertTrue(mobile_number_heading.getVisibility() == View.GONE);
        assertTrue(mobile_arrow.getVisibility() == View.GONE);
        assertEquals(mobile_number.getText(),mContext.getString(R.string.MYA_Add_mobile_number));
        assertTrue(mobile_number.getVisibility() == View.GONE);
        myaDetailsFragment.setMobileNumber("null");
        assertTrue(mobile_number_heading.getVisibility() == View.GONE);
        assertTrue(mobile_arrow.getVisibility() == View.GONE);
        assertEquals(mobile_number.getText(),mContext.getString(R.string.MYA_Add_mobile_number));
        assertTrue(mobile_number.getVisibility() == View.GONE);
    }

    @Test
    public void testHandleArrowVisibility() {
        View email_mobile_arrow = myaDetailsFragment.getView().findViewById(R.id.email_mobile_right_arrow);
        myaDetailsFragment.handleArrowVisibility("some_mail@philips.com", "91992929929");
        assertTrue(email_mobile_arrow.getVisibility() == View.VISIBLE);
    }


    @Test
    public void ShouldonSaveInstanceState() {
        Bundle savedBundle = new Bundle();
        Bundle argumentBundle = new Bundle();
        argumentBundle.putInt("some_key", 100);
        myaDetailsFragment.setArguments(argumentBundle);
        myaDetailsFragment.onSaveInstanceState(savedBundle);
        Bundle profile_bundle = savedBundle.getBundle("details_bundle");
        assertTrue(profile_bundle.equals(argumentBundle));
        assertEquals(profile_bundle.getInt("some_key"), 100);
    }

}