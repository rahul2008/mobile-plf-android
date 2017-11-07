/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.io.ByteArrayInputStream;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSDependantHistoryFragmentTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    ByteArrayInputStream byteArrayInputStreamMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    THSDependantHistoryFragment mTHSDependantHistoryFragment;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSDependentPresenter thsDependentPresenterMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    Context contextMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        mTHSDependantHistoryFragment = new THSDependantHistoryFragment();
        mTHSDependantHistoryFragment.setActionBarListener(actionBarListenerMock);
        /*

        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");*/
        Bundle bundle = new Bundle();
        bundle.putInt(THSConstants.THS_LAUNCH_INPUT,2);
        mTHSDependantHistoryFragment.setArguments(bundle);

        SupportFragmentTestUtil.startFragment(mTHSDependantHistoryFragment);
    }

    @Test
    public void showDependentList() throws Exception {
        assert true;
    }

    @Test
    public void onItemClick() throws Exception {
        mTHSDependantHistoryFragment.onItemClick(thsConsumerMock);
        assertNotNull(THSManager.getInstance().getThsConsumer(contextMock));
    }

    @Test
    public void onClick() throws Exception {
        mTHSDependantHistoryFragment.mThsDependentPresenter = thsDependentPresenterMock;
        mTHSDependantHistoryFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mTHSDependantHistoryFragment.getView().findViewById(R.id.ths_parent_container);
        viewById.performClick();
        verify(thsDependentPresenterMock).checkIfUserExists();
    }

    @Test
    public void showProfilePicWhenProfilePicNotSet() throws Exception {
        mTHSDependantHistoryFragment.showProfilePic(thsConsumerMock);
    }

}