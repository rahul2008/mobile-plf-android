/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import android.os.Bundle;

import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSFaqAnswerFragmentTest {

    THSFaqAnswerFragment mThsAnswerFragment;

    @Mock
    FaqBean faqBeanMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Bundle bundle = new Bundle();
        bundle.putSerializable(THSConstants.THS_FAQ_ITEM,faqBeanMock);
        bundle.putSerializable(THSConstants.THS_FAQ_HEADER,"header");

        mThsAnswerFragment = new THSFaqAnswerFragment();
        mThsAnswerFragment.setArguments(bundle);
        mThsAnswerFragment.setActionBarListener(actionBarListenerMock);

        SupportFragmentTestUtil.startFragment(mThsAnswerFragment);
    }

    @Test
    public void assertTheFragmentNotNullAndNoCrash(){
        assertNotNull(mThsAnswerFragment);
    }
}