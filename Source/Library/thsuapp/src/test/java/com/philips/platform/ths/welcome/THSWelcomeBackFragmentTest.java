/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSWelcomeBackFragmentTest {

    @Mock
    AWSDK awsdkMock;

    THSWelcomeBackFragment mThsWelcomeBackFragment;

    @Mock
    THSWelcomeBackPresenter presenterMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        mThsWelcomeBackFragment = new THSWelcomeBackFragment();
        mThsWelcomeBackFragment.setActionBarListener(actionBarListenerMock);
        mThsWelcomeBackFragment.mThsWelcomeBackPresenter = presenterMock;
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
    }



    @Test
    public void getPracticeInfo() throws Exception {
        final PracticeInfo practiceInfo = mThsWelcomeBackFragment.getPracticeInfo();
        assertNull(practiceInfo);
    }

    @Test
    public void getProvider() throws Exception {
        final Provider provider = mThsWelcomeBackFragment.getProvider();
        assertNull(provider);
    }

}