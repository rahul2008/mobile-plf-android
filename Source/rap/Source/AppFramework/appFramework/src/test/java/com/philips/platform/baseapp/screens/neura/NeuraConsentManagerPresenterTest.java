/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.homefragment.HomeFragmentState;
import com.philips.platform.baseapp.screens.privacysettings.PrivacySettingsState;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class NeuraConsentManagerPresenterTest {

    private NeuraConsentManagerPresenter neuraConsentManagerPresenter;
    @Mock
    private FragmentView fragmentView;
    @Mock
    private NeuraConsentProvider neuraConsentProviderMock;
    @Mock
    private PostConsentTypeCallback postConsentTypeCallback;
    @Mock
    private FragmentLauncher fragmentLauncher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        neuraConsentManagerPresenter = new NeuraConsentManagerPresenter(fragmentView) {
            @NonNull
            @Override
            NeuraConsentProvider getNeuraConsentProvider(AppFrameworkApplication appFrameworkApplication) {
                return neuraConsentProviderMock;
            }

            @NonNull
            @Override
            PostConsentTypeCallback getPostConsentTypeCallback() {
                return postConsentTypeCallback;
            }

            @NonNull
            @Override
            FragmentLauncher getUiLauncher() {
                return fragmentLauncher;
            }
        };
    }

    @Test
    public void testOnEvent() {
        FragmentActivity fragmentActivityMock = mock(FragmentActivity.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        NeuraState neuraStateMock = mock(NeuraState.class);
        HomeFragmentState homeFragmentState = mock(HomeFragmentState.class);
        PrivacySettingsState privacySettingsState = mock(PrivacySettingsState.class);
        FlowManager flowManager = mock(FlowManager.class);
        when(appFrameworkApplicationMock.getTargetFlowManager()).thenReturn(flowManager);
        when(fragmentActivityMock.getApplication()).thenReturn(appFrameworkApplicationMock);
        when(fragmentView.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(flowManager.getNextState("allow")).thenReturn(neuraStateMock);
        when(flowManager.getNextState("mayBeLater")).thenReturn(homeFragmentState);
        when(flowManager.getNextState("privacyPhilips")).thenReturn(privacySettingsState);
        neuraConsentManagerPresenter.onEvent(R.id.allowButton);
        verify(neuraConsentProviderMock).storeConsentTypeState(true, postConsentTypeCallback);
        neuraConsentManagerPresenter.onEvent(R.id.mayBeLater);
        verify(neuraConsentProviderMock).storeConsentTypeState(false, postConsentTypeCallback);
        verify(homeFragmentState).navigate(fragmentLauncher);

    }

    @Test
    public void testGetUiStateData() {
        assertNull(neuraConsentManagerPresenter.getUiStateData(123));
        assertNotNull(neuraConsentManagerPresenter.getUiStateData(R.id.philipsPrivacy));
        assertTrue(neuraConsentManagerPresenter.getUiStateData(R.id.philipsPrivacy) instanceof WebViewStateData);
    }


}