/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.BluelibUappAppDependencies;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DemoBLLStateTest {

    private DemoBLLState demoBLLState;

    @Mock
    private BluelibUapp bleUapp;

    @Mock
    private BleUtilities bleUtilities;

    @Mock
    private Context context;

    @Mock
    private UappSettings bleDemoMicroAppSettings;

    @Mock
    private BluelibUappAppDependencies defaultBleDemoMicroAppDependencies;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        ContextProvider.setTestingContext(context);

        demoBLLState = new DemoBLLStateMock();
    }

    @Test
    public void navigate() throws Exception {
        when(bleUtilities.isBleFeatureAvailable()).thenReturn(true);
        demoBLLState.init(context);
        demoBLLState.updateDataModel();

        demoBLLState.navigate(null);

        verify(bleUapp).launch(any(UiLauncher.class), (UappLaunchInput)isNull());
    }

    private class DemoBLLStateMock extends DemoBLLState {
        @Override
        public UappInterface getBleUApp() {
            return bleUapp;
        }

        @NonNull
        @Override
        protected UappDependencies getUappDependencies() {
            return defaultBleDemoMicroAppDependencies;
        }

        @NonNull
        @Override
        protected UappSettings getUappSettings() {
            return bleDemoMicroAppSettings;
        }

        @Override
        protected ThemeConfiguration getDLSThemeConfiguration(Context context) {
            return new ThemeConfiguration(context);
        }
    }

    @After
    public void tearDown(){
        demoBLLState = null;
    }
}