package com.philips.platform.datasync.synchronisation;

import android.app.Application;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.moments.MomentsClient;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static com.philips.platform.datasync.moments.MomentsDataSenderTest.DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataPullSynchroniseTest {
//    DateTime DATE_TIME = new DateTime();
    private static final int EVENT_ID = 2344;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private MomentsConverter momentsConverterMock;

    @Mock
    private MomentsClient momentsClientMock;

//    @Mock
//    private InsightsClient insightsClientMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorEventCaptor;

    private DataPullSynchronise synchronise;

    @Mock
    private DataFetcher secondFetcherMock;

    @Mock
    private DataFetcher firstFetcherMock;

    @Mock
    private ExecutorService executorMock;

    @Mock
    private Eventing eventingMock;


    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Mock
    private retrofit.RetrofitError retrofitErrorMock;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Mock
    private Moment momentMock;

    @Mock
    private ConsentDetail consentDetailMock;
    private Application context;
    private OrmCreatorTest verticalDataCreater;
    private ErrorHandlerImplTest errorHandlerImpl;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    private AppComponent appComponantMock;


    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImpl = new ErrorHandlerImplTest();

        DataServicesManager.getInstance().setAppComponant(appComponantMock);

        Set set=new HashSet();
        set.add("moment");
        set.add("Settings");
        set.add("characteristics");
        set.add("consent");

        DataServicesManager.getInstance().configureSyncDataType(set);
        Set<String> dataTypeList = DataServicesManager.getInstance().getSyncTypes();

        //  when(accessProviderMock.isLoggedIn()).thenReturn(true);
        synchronise = new DataPullSynchronise(
                Arrays.asList(firstFetcherMock, secondFetcherMock)
        );
        synchronise.accessProvider = accessProviderMock;
        synchronise.eventing = eventingMock;
        synchronise.synchronisationManager=synchronisationManagerMock;
        synchronise.executor = executorMock;
        ArrayList list = new ArrayList();
        list.add(firstFetcherMock);
        list.add(secondFetcherMock);
        synchronise.fetchers = list;
        synchronise.configurableFetchers = list;
    }

    @Test
    public void ShouldReturnError_WhenUserIsNotLoggedIn() {
         when(accessProviderMock.isLoggedIn()).thenReturn(false);

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

        verify(eventingMock).post(errorEventCaptor.capture());
        assertThat(errorEventCaptor.getValue().getReferenceId()).isEqualTo(EVENT_ID);
    }

    @Test
    public void ShouldPostError_WhenUserIsNotLoggedIn() {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        verify(eventingMock).post(isA(BackendResponse.class));
    }


    @Test
    public void ShouldFetchData_WhenUserIsNotLoggedIn() {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
    }




    /*@Test
    public void ShouldPostSuccess_WhenSyncSucceed() throws Exception {
        when(firstFetcherMock.fetchDataSince(DATE_TIME)).thenReturn(null);
        when(secondFetcherMock.fetchDataSince(DATE_TIME)).thenReturn(null);

        synchronise.onEventAsync(new GetNonSynchronizedConsentssResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

       // runExecutor();

       // verify(eventingMock).post(successCaptor.capture());
    }*/

    private void runExecutor() {
       // verify(executorMock, atLeastOnce()).execute(runnableCaptor.capture());
        for (Runnable runnable : runnableCaptor.getAllValues()) {
            runnable.run();
        }
    }


   /* @Test
    public void ShouldNotPostAnything_WhenSyncIsNotFinished() throws Exception {

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

        //final GetNonSynchronizedConsentsRequest event = getEvent(GetNonSynchronizedConsentsRequest.class);
       // assertThat(event).isNull();

      //  verify(eventingMock).register(synchronise);
    }
*/
    /*@Test
    public void ShouldIgnoreSyncStart_WhenSyncIsAlreadyStarted() throws Exception {

        synchronise.onEventAsync(new GetNonSynchronizedConsentssResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

       // verify(executorMock, times(2)).execute(any(Runnable.class));
    }*/

    private <T> T getEvent(Class<T> clazz) {
        verify(eventingMock, atLeastOnce()).post(eventCaptor.capture());

        final List<Event> allValues = eventCaptor.getAllValues();
        for (Event e : allValues) {
            if (clazz.isInstance(e)) {
                return clazz.cast(e);
            }
        }

        return null;
    }

    @Test
    public void Should_Call_performFetch(){
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        synchronise.startSynchronise(new DateTime(),2);
        runExecutor();
        //TODO: Spoorti - Fix it and see what has to be verified
//        verify(synchronisationManagerMock).shutdownAndAwaitTermination(executorMock);
    }

    @Test
    public void Should_Call_synchronize_when_user_logged_out(){
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        synchronise.startSynchronise(new DateTime(),2);
      //  verify(synchronisationManagerMock).shutdownAndAwaitTermination(executorMock);
        verify(eventingMock).post(isA(BackendResponse.class));
    }

    @Test
    public void Should_Call_synchronize_when_no_sync_type_configured(){
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        synchronise.configurableFetchers = new ArrayList<>();
        synchronise.startSynchronise(new DateTime(),2);
        verifyNoMoreInteractions(executorMock);
    }

    //TODO: Spoort -  Not sure Y its giving error, uncomment and check it later
 /*   @Test
    public void Should_Call_synchronize_when_fetchResult_isError(){
        when(accessProviderMock.isLoggedIn()).thenReturn(true);

        when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.HTTP);
        when(firstFetcherMock.fetchDataSince(null)).thenThrow(retrofitErrorMock);

        synchronise.startSynchronise(null,2);
        runExecutor();

    }*/

    @Test
    public void ShouldRunSyncInDifferentThreads() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        runExecutor();
//        verify(executorMock, times(2)).execute(any(Runnable.class));
    }

}