/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.debugtest;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class DebugTestFragmentJunitTest extends TestCase {
    private DebugTestFragment debugFragment;
    private AppFrameworkApplication appFrameworkApplication;
    private AppInfra appInfra;
    private AppConfigurationInterface appConfigurationInterface;
    private AppConfigurationInterface.AppConfigurationError error;
    private FragmentActivity fragmentActivityMock;
    private View view;
    private AdapterView.OnItemSelectedListener adapterView;
    private TextView textView;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        super.setUp();
        debugFragment = mock(DebugTestFragment.class);
        fragmentActivityMock = mock(FragmentActivity.class);
        appFrameworkApplication = mock(AppFrameworkApplication.class);
        appInfra = mock(AppInfra.class);
        error = mock(AppConfigurationInterface.AppConfigurationError.class);
        adapterView = mock(AdapterView.OnItemSelectedListener.class);
        when(fragmentActivityMock.getApplicationContext()).thenReturn(appFrameworkApplication);
    }

    @Test
    public void setupView() throws Exception{
        AppIdentityInterface.AppState appState = AppIdentityInterface.AppState.DEVELOPMENT;
        when(appFrameworkApplication.getAppState()).thenReturn(appState);

        AppIdentityInterface.AppState configurationType[] =
                {
                        AppIdentityInterface.AppState.STAGING,
                        AppIdentityInterface.AppState.TEST,
                        AppIdentityInterface.AppState.DEVELOPMENT
                };
        textView = mock(TextView.class);
        List<AppIdentityInterface.AppState> list = Arrays.asList(configurationType);
        when(debugFragment.getList(configurationType)).thenReturn(list);
        view = mock(View.class);
        debugFragment.setUpView(view);
        assertEquals(2, list.indexOf(appState));
        verify(debugFragment).setUpView(view);
        verifyNoMoreInteractions(debugFragment);
    }

    @Test
    public void valideState() throws Exception{
        String APPIDENTITY_APP_STATE = "appidentity.appState";
        String appInfraValue = "appinfra";
        AppIdentityInterface.AppState appState = AppIdentityInterface.AppState.DEVELOPMENT;
        when(appFrameworkApplication.getAppState()).thenReturn(appState);

        when(appFrameworkApplication.getAppInfra()).thenReturn(appInfra);

        appConfigurationInterface = new AppConfigurationInterface() {
            @Override
            public Object getPropertyForKey(String s, String s1, AppConfigurationError appConfigurationError) throws IllegalArgumentException {
                return null;
            }

            @Override
            public boolean setPropertyForKey(String s, String s1, Object o, AppConfigurationError appConfigurationError) throws IllegalArgumentException {
                return true;
            }

            @Override
            public Object getDefaultPropertyForKey(String s, String s1, AppConfigurationError appConfigurationError) throws IllegalArgumentException {
                return null;
            }

            @Override
            public void refreshCloudConfig(OnRefreshListener onRefreshListener) {

            }

            @Override
            public void resetConfig() {

            }
        };

        when(appInfra.getConfigInterface()).thenReturn(appConfigurationInterface);

        boolean isStored = appConfigurationInterface.setPropertyForKey(APPIDENTITY_APP_STATE,
                appInfraValue, appState, error);

        debugFragment.setState(appState);

        assert(isStored);
    }
}