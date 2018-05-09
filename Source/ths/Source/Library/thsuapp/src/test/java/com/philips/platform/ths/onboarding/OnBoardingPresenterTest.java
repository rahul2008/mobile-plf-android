/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboarding;

import android.os.Bundle;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

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
    public void onEvent_tv_skip() throws Exception {
        mOnBoardingPresenter.onEvent(R.id.tv_skip);
        verify(onBoardingFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle)isNull(),anyBoolean());
    }

    @Test
    public void onEvent_btn_take_tour() throws Exception {
        mOnBoardingPresenter.onEvent(R.id.btn_take_tour);
        verify(onBoardingFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle)isNull(),anyBoolean());
    }

}