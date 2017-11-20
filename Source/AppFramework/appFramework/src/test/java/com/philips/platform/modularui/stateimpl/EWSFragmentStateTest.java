package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSFragmentStateTest {
    private EWSFragmentState ewsState;
    @Mock
    private Context context;
    @Mock
    private AppInfraInterface appInfraInterface;
    @Mock
    private FragmentLauncher fragUiLauncher;

    @Mock
    private UappSettings ewsMicroAppSettings;
    @Mock
    private EWSInterface ewsInterface;
    @Mock
    private ContentConfiguration contentConfiguration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        ContextProvider.setTestingContext(context);
        ewsState = new EWSFragmentMock();
        ewsState.init(context);
        ewsState.createProductMap();
    }

    @Test
    public void itShouldNavigateToEWSLauchScreen() {
        ewsState.init(context);
        ewsState.updateDataModel();

        ewsState.navigate(null);

        verify(ewsInterface).launch(any(UiLauncher.class), any(UappLaunchInput.class));
    }

    public class EWSFragmentMock extends EWSFragmentState {
        @NonNull
        @Override
        protected EWSInterface getEwsApp() {
            return ewsInterface;
        }


        @NonNull
        @Override
        protected UappDependencies getUappDependencies() {

            return new EWSDependencies(appInfraInterface, createProductMap(), contentConfiguration);
        }

        @NonNull
        @Override
        protected UappSettings getUappSettings() {
            return ewsMicroAppSettings;
        }

        @NonNull
        @Override
        protected FragmentLauncher getFragmentLauncher(UiLauncher uiLauncher) {
            return fragUiLauncher;
        }

        @NonNull
        protected Map<String, String> createProductMap() {
            Map<String, String> productKeyMap = new HashMap<>();
            productKeyMap.put(EWSInterface.PRODUCT_NAME, "RefApp-test");
            return productKeyMap;
        }
    }
}