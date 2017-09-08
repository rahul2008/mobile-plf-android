/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.baseapp.base;

import com.philips.platform.appframework.flowmanager.AppStates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AbstractUIBasePresenterTest {

    @Mock
    UIView uiView;

    ConcreteUiBasePresenterTest concreteUiBasePresenterTest;

    @Before
    public void setUp(){
        concreteUiBasePresenterTest=new ConcreteUiBasePresenterTest(uiView);
        concreteUiBasePresenterTest.setState("");
    }

    @Test
    public void setStateDataTest(){
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.HOME_FRAGMENT));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.SETTINGS));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.IAP));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.SUPPORT));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.ABOUT));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.PR));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.CONNECTIVITY));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.COCO_VERSION_INFO));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.DEBUG));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.TEST_MICROAPP));
        assertNotNull(concreteUiBasePresenterTest.setStateData(AppStates.TEST_DEVICE_PAIRING));
        assertNotNull(concreteUiBasePresenterTest.setStateData(""));
    }

    @After
    public void tearDown(){
        uiView=null;
        concreteUiBasePresenterTest=null;
    }
    class ConcreteUiBasePresenterTest extends AbstractUIBasePresenter{

        public ConcreteUiBasePresenterTest(UIView uiView) {
            super(uiView);
        }

        @Override
        public void onEvent(int componentID) {

        }


    }

}