package com.philips.platform.core.injection;

import android.content.Context;

import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.OkClientFactory;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsMonitor;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.consent.ConsentsMonitor;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.moments.MomentsMonitor;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 30/11/16.
 */
public class BackendModuleTest {

    private BackendModule backendModule;

    @Mock
    Eventing eventingMock;
    @Mock
    MomentsMonitor momentsMonitor;
    @Mock
    ConsentsMonitor consentsMonitor;
    @Mock
    UserCharacteristicsMonitor userCharacteristicsMonitor;

    ExecutorService executorService;
    @Mock
    MomentsDataFetcher momentsDataFetcher;
    @Mock
    ConsentsDataFetcher consentsDataFetcher;
    @Mock
    MomentsDataSender momentsDataSender;
    @Mock
    ConsentDataSender consentDataSender;
    @Mock
    OkClientFactory okClientFactory;
    @Mock
    RestAdapter.Builder builder;
    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        backendModule = new BackendModule(eventingMock);
    }

    @Test
    public void ShouldReturnOkHttpClient_WhenProvideOkHttpClientIsCalled() throws Exception {
        final OkHttpClient okHttpClient = backendModule.provideOkHttpClient(Collections.<Interceptor>emptyList());

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient).isInstanceOf(OkHttpClient.class);
    }

    @Test
    public void ShouldReturnOkHttpClient_WhenProvideOkHttpClientWithListIsCalled() throws Exception {
        List<Interceptor> list = new ArrayList<>();
        list.add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return null;
            }
        });
        final OkHttpClient okHttpClient = backendModule.provideOkHttpClient(list);

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient).isInstanceOf(OkHttpClient.class);
    }

    @Test
    public void ShouldReturnRestAdapterBuilder_WhenProvideRestAdapterBuilderIsCalled() throws Exception {
        final RestAdapter.Builder restAdapterBuilder = backendModule.provideRestAdapterBuilder();

        assertThat(restAdapterBuilder).isNotNull();
        assertThat(restAdapterBuilder).isInstanceOf(RestAdapter.Builder.class);
    }

    @Test
    public void ShouldReturnGsonConverter_WhenProvidesGsonConverterIsCalled() throws Exception {
        final GsonConverter gsonConverter = backendModule.providesGsonConverter();

        assertThat(gsonConverter).isNotNull();
        assertThat(gsonConverter).isInstanceOf(GsonConverter.class);
    }

    @Test
    public void ShouldReturnMomentGsonConverter_WhenProvidesMomentsGsonConverterIsCalled() throws Exception {
        final MomentGsonConverter momentGsonConverter = backendModule.providesMomentsGsonConverter();

        assertThat(momentGsonConverter).isNotNull();
        assertThat(momentGsonConverter).isInstanceOf(MomentGsonConverter.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesBackendIsCalled() throws Exception {
        final Backend backend = backendModule.providesBackend(momentsMonitor, consentsMonitor,userCharacteristicsMonitor);
        assertThat(backend).isNotNull();
        assertThat(backend).isInstanceOf(Backend.class);
    }


    @Test
    public void ShouldReturnDataPullSynchronise_WhenProvidesDataPullSynchroniseIsCalled() throws Exception {
        final DataPullSynchronise dataPullSynchronise = backendModule.providesDataSynchronise(momentsDataFetcher, consentsDataFetcher, eventingMock, executorService);
        assertThat(dataPullSynchronise).isNotNull();
        assertThat(dataPullSynchronise).isInstanceOf(DataPullSynchronise.class);
    }

    @Test
    public void ShouldReturnDataPushSynchronise_WhenProvidesDataPushSynchroniseIsCalled() throws Exception {
        final DataPushSynchronise dataPushSynchronise = backendModule.providesDataPushSynchronise(momentsDataSender, consentDataSender, eventingMock);
        assertThat(dataPushSynchronise).isNotNull();
        assertThat(dataPushSynchronise).isInstanceOf(DataPushSynchronise.class);
    }

    @Test
    public void ShouldReturnUCoreAdapter_WhenProvidesUCoreAdapterIsCalled() throws Exception {
        final UCoreAdapter uCoreAdapter = backendModule.providesUCoreAdapter(okClientFactory, builder, context);
        assertThat(uCoreAdapter).isNotNull();
        assertThat(uCoreAdapter).isInstanceOf(UCoreAdapter.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesEventingIsCalled() throws Exception {
        final Eventing eventing = backendModule.provideEventing();
        assertThat(eventing).isNotNull();
        assertThat(eventing).isInstanceOf(Eventing.class);
    }

}