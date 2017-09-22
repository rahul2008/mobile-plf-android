/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class THSPreWelcomePresenterTest {

    THSPreWelcomePresenter mTHSPreWelcomePresenter;

    @Mock
    THSPreWelcomeFragment thsPreWelcomeFragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTHSPreWelcomePresenter = new THSPreWelcomePresenter(thsPreWelcomeFragment);
    }

    @Test
    public void onEvent() throws Exception {
        mTHSPreWelcomePresenter.onEvent(R.id.ths_go_see_provider);
        verify(thsPreWelcomeFragment).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class));
    }

}