/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.americanwell.sdk.AWSDK;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;

public class THSWelcomeBackFragmentTest {

    @Mock
    AWSDK awsdkMock;

    THSWelcomeBackFragment mThsWelcomeBackFragment;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        mThsWelcomeBackFragment = new THSWelcomeBackFragment();
        mThsWelcomeBackFragment.setActionBarListener(actionBarListenerMock);

       // SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
    }

    @Test
    public void onClick() throws Exception {
        assert true;
    }

    @Test
    public void getPracticeInfo() throws Exception {

    }

    @Test
    public void getProvider() throws Exception {

    }

}