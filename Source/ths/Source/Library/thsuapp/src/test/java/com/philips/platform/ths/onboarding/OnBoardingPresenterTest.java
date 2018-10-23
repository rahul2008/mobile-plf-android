/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboarding;

import android.os.Bundle;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class OnBoardingPresenterTest {

    OnBoardingPresenter mOnBoardingPresenter;

    @Mock
    OnBoardingFragment onBoardingFragmentMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOnBoardingPresenter = new OnBoardingPresenter(onBoardingFragmentMock);
    }

    @Test
    public void onEvent_continue_returning_user() throws Exception {
        THSManager.getInstance().setIsReturningUser(true);
        mOnBoardingPresenter.onEvent(R.id.btn_continue);
        verify(onBoardingFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle)isNull(),anyBoolean());
    }

    @Test
    public void onEvent_continue_first_time_user() throws Exception {
        THSManager.getInstance().setIsReturningUser(false);
        mOnBoardingPresenter.onEvent(R.id.btn_continue);
        verify(onBoardingFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle)isNull(),anyBoolean());
    }
}