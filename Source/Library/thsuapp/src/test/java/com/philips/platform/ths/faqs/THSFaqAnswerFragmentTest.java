/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSFaqAnswerFragmentTest {

    THSFaqAnswerFragment mThsAnswerFragment;

    @Mock
    FaqBeanPojo faqBeanPojoMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        Bundle bundle = new Bundle();
        bundle.putSerializable(THSConstants.THS_FAQ_ITEM, faqBeanPojoMock);
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