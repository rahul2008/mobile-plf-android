/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;

import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.R;
import com.philips.platform.ths.intake.THSSymptomsFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSWelcomeBackPresenterTest {

    @Mock
    THSWelcomeBackFragment thsWelcomeBackFragmentMock;

    @Mock
    Provider providerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void onEvent() throws Exception {
        THSWelcomeBackPresenter thsWelcomeBackPresenter = new THSWelcomeBackPresenter(thsWelcomeBackFragmentMock);
        when(thsWelcomeBackFragmentMock.getProvider()).thenReturn(providerMock);
        thsWelcomeBackPresenter.onEvent(R.id.ths_get_started);
        verify(thsWelcomeBackFragmentMock).addFragment(any(THSSymptomsFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

}