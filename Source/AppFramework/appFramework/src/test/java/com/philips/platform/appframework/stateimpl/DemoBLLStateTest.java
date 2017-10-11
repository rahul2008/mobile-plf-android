/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppInterface;
import com.example.cdpp.bluelibexampleapp.uapp.BleDemoMicroAppSettings;
import com.example.cdpp.bluelibexampleapp.uapp.DefaultBleDemoMicroAppDependencies;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DemoBLLStateTest {

    private DemoBLLState demoBLLState;

    @Mock
    private BleDemoMicroAppInterface bleUapp;

    @Mock
    private BleUtilities bleUtilities;

    @Mock
    private Context context;

    @Mock
    private BleDemoMicroAppSettings bleDemoMicroAppSettings;

    @Mock
    private DefaultBleDemoMicroAppDependencies defaultBleDemoMicroAppDependencies;

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

        verify(bleUapp).launch(any(UiLauncher.class), any(UappLaunchInput.class));
    }

    private class DemoBLLStateMock extends DemoBLLState {
        @Override
        public BleDemoMicroAppInterface getBleUApp() {
            return bleUapp;
        }

        @Override
        protected DefaultBleDemoMicroAppDependencies getUappDependencies() {
            return defaultBleDemoMicroAppDependencies;
        }

        @Override
        protected BleDemoMicroAppSettings getUappSettings() {
            return bleDemoMicroAppSettings;
        }
    }

    @After
    public void tearDown(){
        demoBLLState = null;
    }
}