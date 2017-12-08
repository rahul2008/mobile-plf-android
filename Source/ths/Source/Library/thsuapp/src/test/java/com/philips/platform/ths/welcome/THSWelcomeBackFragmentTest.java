/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSWelcomeBackFragmentTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    PracticeInfo practiceInfoMock;

    THSWelcomeBackFragment mThsWelcomeBackFragment;

    @Mock
    THSWelcomeBackPresenter presenterMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    Provider providerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        mThsWelcomeBackFragment = new THSWelcomeBackFragment();
        mThsWelcomeBackFragment.setActionBarListener(actionBarListenerMock);
        mThsWelcomeBackFragment.mThsWelcomeBackPresenter = presenterMock;

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getFullName()).thenReturn("Spoorti Hallur");
        when(practiceInfoMock.getName()).thenReturn("OPD");

        Bundle bundle = new Bundle();
        Long date = 1223455L;
        bundle.putSerializable(THSConstants.THS_DATE, date);
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,practiceInfoMock);
        bundle.putParcelable(THSConstants.THS_PROVIDER,providerMock);

        mThsWelcomeBackFragment.setArguments(bundle);
    }



    @Test
    public void getPracticeInfo() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final PracticeInfo practiceInfo = mThsWelcomeBackFragment.getPracticeInfo();
        assertNotNull(practiceInfo);
    }

    @Test
    public void getProvider() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final Provider provider = mThsWelcomeBackFragment.getProvider();
        assertNotNull(provider);
    }

    @Test
    public void testWelcomeBackWithArguments() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final Provider provider = mThsWelcomeBackFragment.getProvider();
        assertNotNull(provider);
    }
    @Test
    public void testOnClick(){
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final View viewById = mThsWelcomeBackFragment.getView().findViewById(R.id.ths_get_started);
        mThsWelcomeBackFragment.mThsWelcomeBackPresenter = presenterMock;
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.ths_get_started);
    }

}