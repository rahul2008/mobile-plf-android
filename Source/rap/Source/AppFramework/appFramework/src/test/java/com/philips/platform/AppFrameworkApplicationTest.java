/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform;/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppInitializationCallback;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;
import com.philips.platform.baseapp.screens.utility.RALog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(application = AppFrameworkApplicationTest.class)
public class AppFrameworkApplicationTest extends AppFrameworkApplication {

    public AppInfraInterface appInfra;
    private UserRegistrationOnBoardingState userRegistrationOnBoardingState;
    private IAPState iapState;
    private LanguagePackInterface languagePackInterface;

    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(base);
        } catch (RuntimeException ignored) {
            RALog.e("TestAppFrameworkApplication", " multidex exception with Roboelectric");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {
                initialize(new AppInitializationCallback.AppStatesInitializationCallback() {
                    @Override
                    public void onAppStatesInitialization() {

                    }
                });
            }
        });

        appInfra = mock(AppInfra.class);

        userRegistrationOnBoardingState = new UserRegistrationOnBoardingState();
        userRegistrationOnBoardingState.init(this);
        setTargetFlowManager();

    }

    @Override
    public void initDataServiceState() {
        DemoDataServicesState mockDSState = mock(DemoDataServicesState.class);
        doNothing().when(mockDSState).init(mock(Context.class));
    }

    public IAPState getIap() {
        return iapState;
    }

    public void setIapState(IAPState state) {
        iapState = state;
    }

    public void setTargetFlowManager() {
        this.targetFlowManager = new FlowManager();
        this.targetFlowManager.initialize(getApplicationContext(), R.raw.appflow, new FlowManagerListener() {
            @Override
            public void onParseSuccess() {

            }
        });
    }

    @Test
    public void testLanguagePack() {
        appInfra = mock(AppInfra.class);
        languagePackInterface = mock(LanguagePackInterface.class);
        Mockito.when(appInfra.getLanguagePack()).thenReturn(languagePackInterface);
        languagePackInterface.refresh(new LanguagePackInterface.OnRefreshListener() {
            @Override
            public void onError(AILPRefreshResult ailpRefreshResult, String s) {

            }

            @Override
            public void onSuccess(AILPRefreshResult ailpRefreshResult) {
                assertEquals(ailpRefreshResult, AILPRefreshResult.REFRESHED_FROM_SERVER);

                languagePackInterface.activate(new LanguagePackInterface.OnActivateListener() {
                    @Override
                    public void onSuccess(String s) {
                        assertNotNull(s);
                    }

                    @Override
                    public void onError(AILPActivateResult ailpActivateResult, String s) {

                    }
                });
            }
        });

    }

}