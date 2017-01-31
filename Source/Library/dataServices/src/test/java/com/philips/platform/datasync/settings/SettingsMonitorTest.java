package com.philips.platform.datasync.settings;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.consent.ConsentsClient;
import com.philips.platform.datasync.consent.ConsentsConverter;
import com.philips.platform.datasync.consent.ConsentsMonitor;
import com.philips.platform.datasync.consent.UCoreConsentDetail;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 31/01/17.
 */
public class SettingsMonitorTest {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String USER_ID = "TEST_GUID";
    private static final int REFERENCE_ID = 0;

    private SettingsMonitor settingsMonitor;

    private Void response = null;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private SettingsConverter settingsConverterMock;

    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private ConsentsClient consentsClientMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private ConsentBackendSaveRequest consentSaveRequestMock;

    @Mock
    private ConsentBackendGetRequest consentBackendGetRequestMock;

    @Mock
    private Consent consentMock;

    @Mock
    private List<UCoreConsentDetail> uCoreConsentDetailMock;

    @Mock
    private Collection<? extends ConsentDetail> consentDetailListMock;

    @Mock
    private SettingsBackendSaveRequest settingsBackendSaveRequestMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorCaptor;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveResponse> responseCaptor;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<Event> eventArgumentCaptor;

    private DataServicesManager dataServicesManager;
    private OrmCreatorTest verticalDataCreater;
    private ErrorHandlerImplTest errorHandlerImplTest;

    @Mock
    private AppComponent appComponantMock;


    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImplTest = new ErrorHandlerImplTest();
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        settingsMonitor = new SettingsMonitor(uCoreAdapterMock, settingsConverterMock, gsonConverterMock);
        settingsMonitor.uCoreAccessProvider = accessProviderMock;
        settingsMonitor.start(eventingMock);
    }

}