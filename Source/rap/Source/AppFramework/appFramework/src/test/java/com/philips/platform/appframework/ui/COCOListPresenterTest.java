/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.ui;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import com.philips.platform.appframework.data.TestConfigManager;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.models.Chapter;
import com.philips.platform.appframework.models.CommonComponent;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.uappframework.launcher.UiLauncher;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 29/07/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class COCOListPresenterTest {

    @Mock
    Context context;

    @Mock
    COCOListContract.View view;


    COCOListPresenter cocoListPresenter;

    @Mock
    FragmentView fragmentView;

    @Mock
    TestConfigManager testConfigManager;

    @Mock
    Chapter chapter;

    @Captor
    ArgumentCaptor<TestConfigManager.TestConfigCallback> testConfigCallbackArgumentCaptor;

    TestConfigManager.TestConfigCallback testConfigCallback;

    @Mock
    AppFrameworkApplication appFrameworkApplication;

    @Mock
    BaseFlowManager flowManager;

    @Mock
    BaseState baseState;

    @Mock
    BaseState newBaseState;

    @Mock
    FragmentActivity fragmentActivity;


    @Before
    public void setUp(){
        when(fragmentView.getFragmentActivity()).thenReturn(fragmentActivity);
        cocoListPresenter=new COCOListPresenter(fragmentView,testConfigManager,context,view){
            @Override
            protected AppFrameworkApplication getApplicationContext() {
                return appFrameworkApplication;
            }
        };
        cocoListPresenter.onStateComplete(baseState);
        cocoListPresenter.onEvent(0);
    }

    @Test
    public void loadCoCoList() throws Exception {
        cocoListPresenter.loadCoCoList(chapter);
        verify(testConfigManager).getCoCoList(eq(chapter),testConfigCallbackArgumentCaptor.capture());
        testConfigCallback=testConfigCallbackArgumentCaptor.getValue();
        testConfigCallback.onChaptersLoaded(new ArrayList<Chapter>());
        testConfigCallback.onCOCOLoadError();
        testConfigCallback.onCOCOLoaded(new ArrayList<CommonComponent>());
        verify(view).displayCoCoList(any(ArrayList.class));

    }

    @Test
    public void onEvent() throws Exception {
        when(appFrameworkApplication.getTargetFlowManager()).thenReturn(flowManager);
        when(flowManager.getState(AppStates.TEST_MICROAPP)).thenReturn(baseState);
        when(flowManager.getNextState(baseState,"TestInAppPurhcaseEvent")).thenReturn(newBaseState);
        cocoListPresenter.onEvent(COCOListPresenter.IAP_DEMO_APP);
        verify(newBaseState).navigate(any(UiLauncher.class));
    }

    @Test
    public void getEventState() throws Exception {
        Assert.assertEquals(COCOListPresenter.TEST_IAP_EVENT,cocoListPresenter.getEventState(COCOListPresenter.IAP_DEMO_APP));
        Assert.assertEquals(COCOListPresenter.TEST_APP_INFRA_EVENT,cocoListPresenter.getEventState(COCOListPresenter.DEMO_APP_INFRA));
        Assert.assertEquals(COCOListPresenter.TEST_CC_EVENT,cocoListPresenter.getEventState(COCOListPresenter.CC_DEMO_APP));
        Assert.assertEquals(COCOListPresenter.TEST_DS_EVENT,cocoListPresenter.getEventState(COCOListPresenter.DS_DEMO_APP));
        Assert.assertEquals(COCOListPresenter.TEST_PR_EVENT,cocoListPresenter.getEventState(COCOListPresenter.PRODUCT_REGISTRATION));
        Assert.assertEquals(COCOListPresenter.TEST_BLUE_LIB_DEMO_APP_EVENT,cocoListPresenter.getEventState(COCOListPresenter.BLUE_LIB_DEMO_APP));
        Assert.assertEquals(COCOListPresenter.TEST_DICOMM_EVENT,cocoListPresenter.getEventState(COCOListPresenter.DICOMM_APP));
        Assert.assertEquals(COCOListPresenter.TEST_UR_EVENT,cocoListPresenter.getEventState(COCOListPresenter.USER_REGISTRATION_STANDARD));
        Assert.assertEquals(COCOListPresenter.TEST_EWS,cocoListPresenter.getEventState(COCOListPresenter.EASY_WIFI_SETUP));
        Assert.assertEquals(COCOListPresenter.TEST_THS_DEMO_EVENT,cocoListPresenter.getEventState(COCOListPresenter.THS_DEMO_APP));
//        Assert.assertEquals(COCOListPresenter.TEST_NEURA_DEMO_EVENT,cocoListPresenter.getEventState(COCOListPresenter.NEURA_DEMO_APP));
    }

}