package com.philips.platform.ths.insurance;

import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.RadioButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSInsuranceConfirmationFragmentTest {

    THSInsuranceConfirmationFragmentMock tHSInsuranceConfirmationFragment;

    @Mock
    THSInsuranceConfirmationPresenter THSInsuranceConfirmationPresenterMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    Consumer consumerMoxk;

    @Mock
    THSConsumerWrapper thsConsumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(thsConsumerMock.getConsumer()).thenReturn(consumerMoxk);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        tHSInsuranceConfirmationFragment = new THSInsuranceConfirmationFragmentMock();
        tHSInsuranceConfirmationFragment.setActionBarListener(actionBarListenerMock);

    }

    @Test
    public void onContinueClick() throws Exception {
        SupportFragmentTestUtil.startFragment(tHSInsuranceConfirmationFragment);
        tHSInsuranceConfirmationFragment.mPresenter = THSInsuranceConfirmationPresenterMock;
        tHSInsuranceConfirmationFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = tHSInsuranceConfirmationFragment.getView().findViewById(R.id.pth_insurance_confirmation_continue_button);

        RadioButton yesRadioButton= (RadioButton)tHSInsuranceConfirmationFragment.mConfirmationRadioGroup.findViewById(R.id.pth_insurance_confirmation_radio_option_yes);
        yesRadioButton.setChecked(true);
        assertTrue(viewById.performClick());
        verify(THSInsuranceConfirmationPresenterMock, atLeastOnce()).onEvent(R.id.pth_insurance_confirmation_radio_option_yes);

        RadioButton noRadioButton= (RadioButton)tHSInsuranceConfirmationFragment.mConfirmationRadioGroup.findViewById(R.id.pth_insurance_confirmation_radio_option_no);
        noRadioButton.setChecked(true);
        assertTrue(viewById.performClick());
        verify(THSInsuranceConfirmationPresenterMock, atLeastOnce()).onEvent(R.id.pth_insurance_confirmation_radio_option_no);
    }

}