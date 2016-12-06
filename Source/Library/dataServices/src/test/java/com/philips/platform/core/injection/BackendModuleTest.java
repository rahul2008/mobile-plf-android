package com.philips.platform.core.injection;

import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAdapter;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sangamesh on 30/11/16.
 */
public class BackendModuleTest {

    private BackendModule backendModule;

    @Mock
    Eventing eventingMock;

    @Before
    public void setUp() throws Exception {
        backendModule = new BackendModule(eventingMock);
    }

    @Test
    public void ShouldReturnOkHttpClient_WhenProvideOkHttpClientIsCalled() throws Exception {
        final OkHttpClient okHttpClient = backendModule.provideOkHttpClient(Collections.<Interceptor>emptyList());

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

}