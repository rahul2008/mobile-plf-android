/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DemoCMLStateTest {

    private DemoCMLState demoCMLState;

    @Mock
    private CommlibUapp commlibUapp;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        ContextProvider.setTestingContext(context);

        demoCMLState = new DemoCMLStateMock();
    }

    @Test
    public void navigate() throws Exception {
        demoCMLState.init(context);
        demoCMLState.updateDataModel();

        demoCMLState.navigate(null);

        verify(commlibUapp).launch(any(UiLauncher.class), any(UappLaunchInput.class));
    }

    private class DemoCMLStateMock extends DemoCMLState {
        @Override
        public CommlibUapp getCommLibUApp() {
            return commlibUapp;
        }
    }
}