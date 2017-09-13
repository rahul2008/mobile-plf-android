/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.view.View;

import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSPreWelcomeFragmentTest {
    THSPreWelcomeFragment mTHSPreWelcomeFragment;

    @Mock
    THSPreWelcomePresenter thsPreWelcomePresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTHSPreWelcomeFragment = new THSPreWelcomeFragment();
        SupportFragmentTestUtil.startFragment(mTHSPreWelcomeFragment);
        mTHSPreWelcomeFragment.mThsPreWelcomeScreenPresenter = thsPreWelcomePresenter;
    }

    @Test
    public void onClick() throws Exception {
        final View viewById = mTHSPreWelcomeFragment.getView().findViewById(R.id.ths_go_see_provider);
        viewById.performClick();
        verify(thsPreWelcomePresenter).onEvent(R.id.ths_go_see_provider);
    }

}