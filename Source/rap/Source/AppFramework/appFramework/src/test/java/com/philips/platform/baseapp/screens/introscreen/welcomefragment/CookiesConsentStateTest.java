/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CookiesConsentStateTest {

    @Mock
    private FragmentLauncher fragmentLauncher;

    @Mock
    private Context context;

    @Mock
    private LaunchActivity launchActivity;

    private WelcomeState welcomeState;

    @Before
    public void setUp() {
        welcomeState = new WelcomeState();
        welcomeState.init(context);
        welcomeState.updateDataModel();
        when(fragmentLauncher.getFragmentActivity()).thenReturn(launchActivity);
    }

    @Test
    public void navigateTest() {
        welcomeState.navigate(fragmentLauncher);
        verify(launchActivity).addFragment(any(Fragment.class), any(String.class));
    }

    @After
    public void tearDown() {
        fragmentLauncher = null;
        context = null;
        launchActivity = null;
        welcomeState = null;
    }
}
