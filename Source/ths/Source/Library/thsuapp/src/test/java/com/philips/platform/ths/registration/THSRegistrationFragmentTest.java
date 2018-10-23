/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import com.americanwell.sdk.AWSDK;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSRegistrationFragmentTest {

    THSRegistrationFragment mThsRegistrationFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    User userMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        mThsRegistrationFragment = new THSRegistrationFragment();
        mThsRegistrationFragment.setActionBarListener(actionBarListenerMock);

        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");
       // SupportFragmentTestUtil.startFragment(mThsRegistrationFragment);
    }

    @Test
    public void onClick() throws Exception {
        assert true;
    }

    @Test
    public void updateDobView() throws Exception {

    }

    @Test
    public void onItemSelected() throws Exception {

    }

    @Test
    public void onNothingSelected() throws Exception {

    }

}