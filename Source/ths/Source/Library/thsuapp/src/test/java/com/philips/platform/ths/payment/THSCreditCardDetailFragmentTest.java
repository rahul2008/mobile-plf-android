/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

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
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSCreditCardDetailFragmentTest {

    THSCreditCardDetailFragment mThsCreditCardDetailFragment;

    THSCreditCardDetailPresenter thsCreditCardDetailPresenter;

    @Mock
    THSCreditCardDetailPresenter thsCreditCardDetailPresenterMock;

    @Mock
    THSCreditCardDetailFragment thsCreditCardDetailFragmentMock;

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
    THSCreditCardDetailPresenter mThsWelcomeBackPresenterMock;

    @Mock
    Address addressMock;
    @Mock
    Throwable throwableMock;

    @Mock
    Map mapMock;
    @Mock
    THSCreditCardDetailViewInterface thsCreditCardDetailViewInterfaceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
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

        mThsCreditCardDetailFragment = new THSCreditCardDetailFragmentTestMock();
        mThsCreditCardDetailFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mThsCreditCardDetailFragment);
        thsCreditCardDetailPresenter = new THSCreditCardDetailPresenter(mThsCreditCardDetailFragment, thsCreditCardDetailViewInterfaceMock);
    }

    @Test
    public void onClick() throws Exception {
        final View viewById = mThsCreditCardDetailFragment.getView().findViewById(R.id.ths_payment_detail_continue_button);
        mThsCreditCardDetailFragment.thsCreditCardDetailPresenter = thsCreditCardDetailPresenterMock;
        viewById.performClick();
        verify(thsCreditCardDetailPresenterMock).onEvent(R.id.ths_payment_detail_continue_button);
    }

    @Test
    public void onGetPaymentFailure() throws Exception {
        thsCreditCardDetailPresenter.mTHSCreditCardDetailFragment = thsCreditCardDetailFragmentMock;
        thsCreditCardDetailPresenter.onGetPaymentFailure(throwableMock);
    }

    @Test
    public void onValidationFailure() throws Exception {
        thsCreditCardDetailPresenter.mTHSCreditCardDetailFragment = thsCreditCardDetailFragmentMock;
        thsCreditCardDetailPresenter.onValidationFailure(mapMock);
    }

}