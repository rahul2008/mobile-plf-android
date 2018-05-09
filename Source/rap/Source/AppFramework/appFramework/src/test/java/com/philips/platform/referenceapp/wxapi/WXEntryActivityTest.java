/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.referenceapp.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

/**
 * Test class for WXEntryActivity
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class WXEntryActivityTest {

    WXEntryActivity wxEntryActivity;

    @Mock
    BaseResp baseRespMock;

    @Mock
    BaseReq baseReqMock;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private ActivityController<WXEntryActivity> activityController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        activityController = Robolectric.buildActivity(WXEntryActivity.class);
        wxEntryActivity = activityController.create().start().get();
        wxEntryActivity.onReq(baseReqMock);
    }

    @Test
    public void onReqTest() {
        wxEntryActivity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                assertTrue(intent
                        .hasExtra(RegConstants.WECHAT_ERR_CODE));
            }
        }, new IntentFilter(RegConstants.WE_CHAT_AUTH));
        wxEntryActivity.onResp(baseRespMock);
    }

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        wxEntryActivity=null;
        baseRespMock=null;
        baseReqMock=null;
    }

}