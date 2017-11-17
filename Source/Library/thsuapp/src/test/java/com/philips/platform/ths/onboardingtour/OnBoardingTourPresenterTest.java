/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class OnBoardingTourPresenterTest {

    OnBoardingTourPresenter mOnBoardingTourPresenter;

    @Mock
    OnBoardingTourFragment onBoardingTourFragmentMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOnBoardingTourPresenter = new OnBoardingTourPresenter(onBoardingTourFragmentMock);
    }

    @Test
    public void onEvent_welcome_rightarrow() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_rightarrow);
    }

    @Test
    public void onEvent_welcome_leftarrow() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_leftarrow);
    }

    @Test
    public void onEvent_welcome_start_registration_button() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_start_registration_button);
        verify(onBoardingTourFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void onEvent_welcome_welcome_skip_button() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_skip_button);
        verify(onBoardingTourFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

}