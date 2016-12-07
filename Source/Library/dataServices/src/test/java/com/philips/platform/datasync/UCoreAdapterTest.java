
package com.philips.platform.datasync;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import android.content.Context;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.datasync.consent.ConsentsClient;
import com.squareup.okhttp.OkHttpClient;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.assertj.core.api.Assertions.assertThat;



public class UCoreAdapterTest {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final Class<ConsentsClient> CLIENT_CLASS = ConsentsClient.class;
    @Mock
    private OkHttpClient okHttpClientMock;

    @Mock
    private OkClientFactory okClientFactoryMock;

    @Mock
    private OkClient okClientMock;

    @Mock
    private RestAdapter.Builder restAdapterBuilderMock;

    @Mock
    private RestAdapter restAdapterMock;

    @Mock
    private ConsentsClient clientMock;
    @Mock
    private RequestInterceptor.RequestFacade requestFacadeMock;

    @Captor
    private ArgumentCaptor<RequestInterceptor> requestInterceptorCaptor;

    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private Context contextMock;

    private UCoreAdapter uCoreAdapter;
    private String TEST_BASE_URL = "TEST_BASE_URL";

    private String TEST_INSIGHTS_URL = "TEST_INSIGHTS_URL";

    @Mock
    private PackageManager packageManagerMock;

    @Mock
    private PackageInfo packageInfoMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        uCoreAdapter = new UCoreAdapter(okClientFactoryMock, restAdapterBuilderMock, contextMock);
        when(okClientFactoryMock.create(okHttpClientMock)).thenReturn(okClientMock);


        //final String TEST_CQ5_URL = "TEST_CQ5_URL";
        //when(icpClientFacadeMock.getUrl(IcpClientFacade.CQ5_SERVICE_TAG)).thenReturn(TEST_CQ5_URL);
        //when(icpClientFacadeMock.getUrl(IcpClientFacade.BASE_SERVICE_TAG)).thenReturn(TEST_BASE_URL);
        //when(icpClientFacadeMock.getUrl(IcpClientFacade.INSIGHTS_SERVICE_TAG)).thenReturn(TEST_INSIGHTS_URL);

        when(restAdapterBuilderMock.setEndpoint(anyString())).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.setLogLevel(UCoreAdapter.LOG_LEVEL)).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.setRequestInterceptor(any(RequestInterceptor.class))).thenReturn(restAdapterBuilderMock);
        //when(restAdapterBuilderMock.setClient(okClientMock)).thenReturn(restAdapterBuilderMock);
        //when(restAdapterBuilderMock.setConverter(gsonConverterMock)).thenReturn(restAdapterBuilderMock);
        //when(restAdapterBuilderMock.build()).thenReturn(restAdapterMock);

        //when(contextMock.getPackageManager()).thenReturn(packageManagerMock);
        //String test_package_name = "TEST_PACKAGE_NAME";
        //when(contextMock.getPackageName()).thenReturn(test_package_name);
        //when(packageManagerMock.getPackageInfo(test_package_name, 0)).thenReturn(packageInfoMock);
        //packageInfoMock.versionName = "0.24.0-SNAPSHOT";
        //when(restAdapterMock.create(CLIENT_CLASS)).thenReturn(clientMock);
    }

//    @Test
//    public void shoudReturnClient_WhenGetClientIsCalled() throws Exception {
//        ConsentsClient client = uCoreAdapter.getClient(CLIENT_CLASS, anyString(), ACCESS_TOKEN, gsonConverterMock);
//        assertThat(client).isSameAs(clientMock);
//    }
}
