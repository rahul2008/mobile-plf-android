package com.philips.platform.appinfra.logging;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static com.philips.platform.appinfra.logging.CloudConsentProvider.CLOUD;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    public void testFetchingConsentDefinitions() {
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

    public void testFetchConsentTypeStateCallback() {
        cloudConsentProvider = new CloudConsentProvider(consentHandlerInterfaceMock);
        boolean[] status = new boolean[1];
        FetchConsentTypeStateCallback fetchConsentTypeStateCallback = cloudConsentProvider.getFetchConsentTypeStateCallback(status);
        assertNotNull(fetchConsentTypeStateCallback);
        fetchConsentTypeStateCallback.onGetConsentsSuccess(new ConsentStatus(ConsentStates.active,1));
        assertTrue(status[0]);
        fetchConsentTypeStateCallback.onGetConsentsFailed(new ConsentError("error",5));
        assertFalse(status[0]);
    }

}