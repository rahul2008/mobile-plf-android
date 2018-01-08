package com.philips.platform.datasync.synchronisation;

import com.philips.platform.mya.catk.ConsentAccessToolKit;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSender;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.insights.InsightDataSender;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.settings.SettingsDataSender;
import com.philips.platform.datasync.spy.UserAccessProviderSpy;
import com.philips.spy.EventingSpy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataPushSynchroniseTest {

    private final int EVENT_ID = 1;

    private final String REGISTERED_CLASS = "DataPushSynchronise";

    private DataPushSynchronise synchronise;

    private UserAccessProviderSpy userAccessProviderSpy;

    private EventingSpy eventingSpy;

    @Mock
    private DataSender firstDataSenderMock;

    @Mock
    private DataSender secondDataSenderMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    private AppComponent appComponentMock;

    @Mock
    MomentsDataSender momentsDataSenderMock;

    @Mock
    ConsentDataSender consentDataSenderMock;

    @Mock
    UserCharacteristicsSender userCharacteristicsSenderMock;

    @Mock
    InsightDataSender insightDataSenderMock;

    @Mock
    SettingsDataSender settingsDataSenderMock;

    @Mock
    ConsentAccessToolKit consentAccessToolKitMock;

    ConsentInteractorMock getConsentInteractorMock;

    @Mock
    Moment moment;

    @Mock
    Consent consent;

    @Captor
    private ArgumentCaptor<PostConsentCallback> consentCallbackCaptor;


    @Before
    public void setUp() {
        initMocks(this);

        userAccessProviderSpy = new UserAccessProviderSpy();
        eventingSpy = new EventingSpy();

        DataServicesManager.getInstance().setAppComponent(appComponentMock);

        synchronise = new DataPushSynchronise(Arrays.asList(firstDataSenderMock, secondDataSenderMock));

        synchronise.momentsDataSender = momentsDataSenderMock;
        synchronise.consentsDataSender = consentDataSenderMock;
        synchronise.insightDataSender = insightDataSenderMock;
        synchronise.userCharacteristicsSender = userCharacteristicsSenderMock;
        synchronise.settingsDataSender = settingsDataSenderMock;

        synchronise.userAccessProvider = userAccessProviderSpy;
        synchronise.eventing = eventingSpy;
        synchronise.synchronisationManager = synchronisationManagerMock;
        getConsentInteractorMock = new ConsentInteractorMock(consentAccessToolKitMock);
        synchronise.consentInteractor = getConsentInteractorMock;
    }

    @Test
    public void postErrorWhenUserIsNotLoggedIn() {
        givenUserIsNotLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
        thenAnErrorIsPostedWithReferenceId(EVENT_ID);
    }

    @Test
    public void postSyncCompleteWhenNoSenders() {
        givenUserIsLoggedIn();
        givenNoConfigurableSenderList();
        givenNoSenders();
        whenSynchronisationIsStarted(EVENT_ID);
        thenDataSyncIsCompleted();
    }

    @Test
    public void postSyncCompleteWhenNoConfigurableSenders() {
        givenUserIsLoggedIn();
        givenNoConfigurableSenderList();
        givenSenderList();
        whenSynchronisationIsStarted(EVENT_ID);
        thenAnEventIsRegisteredWithClass(REGISTERED_CLASS);
        thenAnEventIsPostedWithReferenceId(EVENT_ID);
        thenDataSyncIsCompleted();
    }

    @Test
    public void postSyncCompleteWhenConfigurableSenders() {
        givenUserIsLoggedIn();
        givenConfigurableSenderList();
        whenSynchronisationIsStarted(EVENT_ID);
        thenAnEventIsRegisteredWithClass(REGISTERED_CLASS);
        thenAnEventIsPostedWithReferenceId(EVENT_ID);
        thenDataSyncIsCompleted();
    }

    @Test
    public void onEventAsync_checksForMomentConsent() {
        givenUserIsLoggedIn();
        givenConfigurableMomentSender();
        givenMomentToSink(moment);
        whenSynchronisationIsStarted(EVENT_ID);

        whenCallingOnEventAsync();
        thenMomentConsentWasChecked();
    }

    private void thenMomentConsentWasChecked() {
        assertEquals("moment", getConsentInteractorMock.getStatusForConsentType_consentType);
    }

    private void givenMomentToSink(Moment moment) {
        dataToSync.put(Moment.class, Collections.singletonList(moment));
    }

    private void whenCallingOnEventAsync() {
        synchronise.onEventAsync(new GetNonSynchronizedDataResponse(EVENT_ID, dataToSync));
    }

    private void givenUserIsLoggedIn() {
        userAccessProviderSpy.isLoggedIn = true;
    }

    private void givenUserIsNotLoggedIn() {
        userAccessProviderSpy.isLoggedIn = false;
    }

    private void givenConfigurableSenderList() {
        Set<String> set = new HashSet<>();
        set.add("moment");
        set.add("Settings");
        set.add("characteristics");
        set.add("consent");
        set.add("insight");
        DataServicesManager.getInstance().configureSyncDataType(set);
    }

    private void givenNoConfigurableSenderList() {
        DataServicesManager.getInstance().configureSyncDataType(new HashSet<String>());
        synchronise.configurableSenders = new ArrayList<>();
    }

    private void givenSenderList() {
        ArrayList<DataSender> senderArrayList = new ArrayList<>();
        senderArrayList.add(firstDataSenderMock);
        senderArrayList.add(secondDataSenderMock);
        synchronise.senders = senderArrayList;
    }

    private void givenConfigurableMomentSender() {
        Set<String> set = new HashSet<>();
        set.add("moment");
        DataServicesManager.getInstance().configureSyncDataType(set);
    }

    private void givenNoSenders() {
        synchronise.senders = new ArrayList<>();
    }

    private void whenSynchronisationIsStarted(final int eventId) {
        synchronise.startSynchronise(eventId);
    }

    private void thenAnErrorIsPostedWithReferenceId(final int expectedEventId) {
        assertEquals(expectedEventId, eventingSpy.postedEvent.getReferenceId());
    }

    private void thenDataSyncIsCompleted() {
        verify(synchronisationManagerMock).dataSyncComplete();
    }

    private void thenAnEventIsRegisteredWithClass(String expectedClass) {
        assertEquals(expectedClass, eventingSpy.registeredClass.getClass().getSimpleName());
    }

    private void thenAnEventIsPostedWithReferenceId(int expectedEventId) {
        assertEquals(expectedEventId, eventingSpy.postedEvent.getReferenceId());
    }

    Map<Class, List<?>> dataToSync = new HashMap<>();
}
