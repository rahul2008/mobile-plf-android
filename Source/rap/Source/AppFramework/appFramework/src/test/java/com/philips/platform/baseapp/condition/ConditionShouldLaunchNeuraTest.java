/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.condition;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.neura.NeuraConsentProvider;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class ConditionShouldLaunchNeuraTest {


    private ConditionShouldLaunchNeura conditionShouldLaunchNeura;

    @Mock
    private FetchConsentTypeStateCallback fetchConsentTypeStateCallback;

    @Mock
    private NeuraConsentProvider neuraConsentProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        conditionShouldLaunchNeura = new ConditionShouldLaunchNeura() {
            @NonNull
            @Override
            FetchConsentTypeStateCallback getFetchConsentTypeStateCallback() {
                return fetchConsentTypeStateCallback;
            }

            @NonNull
            @Override
            NeuraConsentProvider getNeuraConsentProvider(AppFrameworkApplication appFrameworkApplication) {
                return neuraConsentProvider;
            }
        };
    }

    @Test
    public void testIsSatisfied() {
        Context contextMock = mock(Context.class);
        AppFrameworkApplication appFrameworkApplication = mock(AppFrameworkApplication.class);
        when(contextMock.getApplicationContext()).thenReturn(appFrameworkApplication);
        assertTrue(conditionShouldLaunchNeura.isSatisfied(contextMock));
        verify(neuraConsentProvider).fetchConsentHandler(fetchConsentTypeStateCallback);
        ConsentStatus consentStatus = new ConsentStatus(ConsentStates.active, BuildConfig.VERSION_CODE, new Date());
        conditionShouldLaunchNeura.onGetConsentsSuccess(consentStatus);
        assertFalse(conditionShouldLaunchNeura.isShouldLaunchNeura());
        conditionShouldLaunchNeura.onGetConsentsFailed(null);
        assertTrue(conditionShouldLaunchNeura.isShouldLaunchNeura());
    }
}