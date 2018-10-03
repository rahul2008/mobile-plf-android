package com.philips.platform.modularui.stateimpl;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(Intent.class)
public class EWSFragmentStateTest {
    private EWSFragmentState ewsState;
    @Mock
    private AppFrameworkApplication context;
    @Mock
    private FragmentLauncher fragUiLauncher;

    @Before
    public void setUp() throws Exception {
        initMocks(this);


        ContextProvider.setTestingContext(context);
        ewsState = new EWSFragmentMock();
        ewsState.init(context);
    }

    @Test
    public void itShouldNavigateToEWSLauchScreen() {
        ewsState.init(context);
        ewsState.updateDataModel();
        ewsState.navigate(null);
    }

    public class EWSFragmentMock extends EWSFragmentState {
        @NonNull
        protected FragmentLauncher getFragmentLauncher(UiLauncher uiLauncher) {
            return fragUiLauncher;
        }

    }
}