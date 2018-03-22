package com.philips.platform.datasync.consent;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 14/03/17.
 */
public class ConsentDataSenderTest {

    private ConsentDataSender consentDataSender;

    @Mock
    private Eventing eventingMock;

    @Mock
    private UCoreAccessProvider uCoreAccessProviderMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    GsonConverter gsonConverterMock;

    @Mock
    ConsentsConverter consentsConverterMock;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    @Mock
    ConsentsClient consentsClientMock;

    @Mock
    private List<UCoreConsentDetail> uCoreConsentDetailsMock;

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        consentDataSender = new ConsentDataSender(uCoreAdapterMock, gsonConverterMock, consentsConverterMock);
        consentDataSender.eventing = eventingMock;
        consentDataSender.uCoreAccessProvider = uCoreAccessProviderMock;
        consentDataSender.synchronisationManager=synchronisationManagerMock;
        consentDataSender.client=consentsClientMock;
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenSettingsListIsEmpty() throws Exception {

        consentDataSender.sendDataToBackend(Collections.<ConsentDetail>emptyList());

    }

    @Test
    public void ShouldSendDataToBackend_WhenSettingsListIsNotEmpty() throws Exception {

        when(consentDataSender.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(consentDataSender.uCoreAccessProvider.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(uCoreAdapterMock.getAppFrameworkClient(ConsentsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(consentsClientMock);
        when(consentsConverterMock.convertToUCoreConsentDetails(anyListOf(ConsentDetail.class))).thenReturn(uCoreConsentDetailsMock);
        when(uCoreConsentDetailsMock.get(0)).thenReturn(new UCoreConsentDetail("dfs", "dfs", "dsfs", "dfs"));
        List<ConsentDetail> consentDetails=new ArrayList<>();
        consentDetails.add(mock((ConsentDetail.class)));
        consentDataSender.sendDataToBackend(consentDetails);

    }

    @Test
    public void shouldPostError_WhenUserIsInvalid() throws Exception {

        consentDataSender.uCoreAccessProvider=null;
        consentDataSender.isUserInvalid();
        consentDataSender.postError(1,consentDataSender.getNonLoggedInError());
        verify(eventingMock).post(isA(BackendResponse.class));
    }

}