package com.philips.platform.baseapp.screens.neura;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManager;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static com.philips.platform.baseapp.screens.neura.NeuraConsentProvider.NEURA;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class NeuraConsentProviderTest {


    private NeuraConsentProvider neuraConsentProvider;

    @Mock
    private ConsentHandlerInterface consentHandlerInterface;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        neuraConsentProvider = new NeuraConsentProvider(consentHandlerInterface);
    }

    public void testRegisteringHandler() {
        AppInfraInterface appInfraInterfaceMock = mock(AppInfraInterface.class);
        ConsentManager consentManagerMock = mock(ConsentManager.class);
        when(appInfraInterfaceMock.getConsentManager()).thenReturn(consentManagerMock);
        neuraConsentProvider.registerConsentHandler(appInfraInterfaceMock);
        verify(consentManagerMock).registerHandler(Collections.singletonList(NEURA), consentHandlerInterface);
    }

    public void testGetNeuraConsentDefinition() {
        ConsentDefinition neuraConsentDefinition = neuraConsentProvider.getNeuraConsentDefinition();
        assertEquals(neuraConsentDefinition.getHelpText(), R.string.RA_neura_consent_help);
        assertEquals(neuraConsentDefinition.getText(), R.string.RA_neura_consent_title);
        assertEquals(neuraConsentDefinition.getVersion(), BuildConfig.VERSION_CODE);
    }

    public void testFetchConsentHandler() {
        FetchConsentTypeStateCallback fetchConsentTypeStateCallback = mock(FetchConsentTypeStateCallback.class);
        neuraConsentProvider.fetchConsentHandler(fetchConsentTypeStateCallback);
        verify(consentHandlerInterface).fetchConsentTypeState(NEURA, fetchConsentTypeStateCallback);
    }

    public void testStoreConsentTypeState() {
        PostConsentTypeCallback postConsentTypeCallback = mock(PostConsentTypeCallback.class);
        neuraConsentProvider.storeConsentTypeState(true, postConsentTypeCallback);
        verify(consentHandlerInterface).storeConsentTypeState(NEURA, true, BuildConfig.VERSION_CODE, postConsentTypeCallback);
        neuraConsentProvider.storeConsentTypeState(false, postConsentTypeCallback);
        verify(consentHandlerInterface).storeConsentTypeState(NEURA, false, BuildConfig.VERSION_CODE, postConsentTypeCallback);
    }

}