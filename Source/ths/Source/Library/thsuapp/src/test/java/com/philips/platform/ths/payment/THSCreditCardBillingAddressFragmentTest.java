/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.init.THSInitPresenter;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSCreditCardBillingAddressFragmentTest {

    THSCreditCardBillingAddressFragment mThsCreditCardBillingAddressFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    User userMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    Country countryMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    THSCreditCardBillingAddressPresenter mThsWelcomeBackPresenterMock;

    @Mock
    Address addressMock;

    List countriesList;

    List dependents;

    Bundle bundle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");

        countriesList = new ArrayList();
        countriesList.add(countryMock);

        dependents = new ArrayList();
        dependents.add(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        when(awsdkMock.getSupportedCountries()).thenReturn(countriesList);

        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);


        mThsCreditCardBillingAddressFragment = new THSCreditCardBillingAddressFragment();
        mThsCreditCardBillingAddressFragment.setActionBarListener(actionBarListenerMock);

        bundle = new Bundle();
        bundle.putParcelable("address",addressMock);
        mThsCreditCardBillingAddressFragment.setArguments(bundle);
    }

    @Test(expected = NullPointerException.class)
    public void onClick() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsCreditCardBillingAddressFragment);
        final View viewById = mThsCreditCardBillingAddressFragment.getView().findViewById(R.id.update_shipping_address);
        mThsCreditCardBillingAddressFragment.mTHSCreditCardBillingAddressPresenter = mThsWelcomeBackPresenterMock;
        viewById.performClick();
        verify(mThsWelcomeBackPresenterMock).onEvent(R.id.update_shipping_address);
    }

    @Test(expected = NullPointerException.class)
    public void validateAddress(){
        SupportFragmentTestUtil.startFragment(mThsCreditCardBillingAddressFragment);
        mThsCreditCardBillingAddressFragment.mAddressOneEditText.setText("valid address");
        verify(appTaggingInterface,atLeastOnce()).trackActionWithInfo(anyString(),any(Map.class));
    }

    @Test(expected = NullPointerException.class)
    public void validateCitiAddress(){
        SupportFragmentTestUtil.startFragment(mThsCreditCardBillingAddressFragment);
        mThsCreditCardBillingAddressFragment.mCityEditText.setText("valid address");
        verify(appTaggingInterface,atLeastOnce()).trackActionWithInfo(anyString(),any(Map.class));

    }

    @Test(expected = NullPointerException.class)
    public void validatemZipcodeEditText(){
        SupportFragmentTestUtil.startFragment(mThsCreditCardBillingAddressFragment);
        mThsCreditCardBillingAddressFragment.mZipcodeEditText.setText("35006");
        verify(appTaggingInterface,atLeastOnce()).trackActionWithInfo(anyString(),any(Map.class));

    }
}