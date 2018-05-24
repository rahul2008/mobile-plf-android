package com.philips.platform.appinfra.logging;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;

import static com.philips.platform.appinfra.logging.CloudConsentProvider.CLOUD;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/23/18.
 */
public class CloudConsentProviderTest extends TestCase {


    private CloudConsentProvider cloudConsentProvider;
    @Mock
    private Handler handler;
    @Mock
    private ConsentHandlerInterface consentHandlerInterfaceMock;
    @Mock
    private FetchConsentTypeStateCallback fetchConsentTypeStateCallback;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        cloudConsentProvider = new CloudConsentProvider(consentHandlerInterfaceMock) {
            @NonNull
            @Override
            FetchConsentTypeStateCallback getFetchConsentTypeStateCallback(boolean[] status) {
                return fetchConsentTypeStateCallback;
            }
        };
    }

    public void testConsentDefinitions() {
        ConsentDefinition cloudConsentDefinition = CloudConsentProvider.getCloudConsentDefinition();
        assertEquals(cloudConsentDefinition.getVersion(),1);
        assertEquals(cloudConsentDefinition.getHelpText(), R.string.ail_cloud_consent_help);
        assertEquals(cloudConsentDefinition.getText(), R.string.ail_cloud_consent_title);
        assertEquals(cloudConsentDefinition.getTypes().get(0), CLOUD);
    }

    public void testRegisteringConsentHandler() {
        ConsentManagerInterface consentManagerInterfaceMock = mock(ConsentManagerInterface.class);
        cloudConsentProvider.registerConsentHandler(consentManagerInterfaceMock);
        verify(consentManagerInterfaceMock).registerHandler(Collections.singletonList(CLOUD), consentHandlerInterfaceMock);
    }

    public void testIsCloudLoggingEnabled() {
        assertFalse(cloudConsentProvider.isCloudLoggingConsentProvided());
        verify(consentHandlerInterfaceMock).fetchConsentTypeState(CLOUD, fetchConsentTypeStateCallback);
    }

}