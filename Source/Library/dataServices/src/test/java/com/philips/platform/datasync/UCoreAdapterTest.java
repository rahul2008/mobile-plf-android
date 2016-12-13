
package com.philips.platform.datasync;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.consent.ConsentsClient;
import com.philips.platform.datasync.userprofile.ErrorHandler;
import com.squareup.okhttp.OkHttpClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.Strftime;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UCoreAdapterTest {


    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final Class<ConsentsClient> CLIENT_CLASS = ConsentsClient.class;
    private static final String BASE_HSDP_URL = "https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com";
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
    ErrorHandler userRegistrationImplMock;

    @Mock
    BaseAppDataCreator baseAppDataCreatorMock;

    Eventing mEventing;

//    @Mock
//    private IcpClientFacade icpClientFacadeMock;

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
        when(restAdapterBuilderMock.setEndpoint(anyString())).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.setLogLevel(UCoreAdapter.LOG_LEVEL)).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.setRequestInterceptor(any(RequestInterceptor.class))).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.setClient(okClientMock)).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.setConverter(gsonConverterMock)).thenReturn(restAdapterBuilderMock);
        when(restAdapterBuilderMock.build()).thenReturn(restAdapterMock);

        when(contextMock.getPackageManager()).thenReturn(packageManagerMock);
        String test_package_name = "TEST_PACKAGE_NAME";
        when(contextMock.getPackageName()).thenReturn(test_package_name);
        when(packageManagerMock.getPackageInfo(test_package_name, 0)).thenReturn(packageInfoMock);
        packageInfoMock.versionName = "0.24.0-SNAPSHOT";
        when(restAdapterMock.create(CLIENT_CLASS)).thenReturn(clientMock);
    }

    public void ShouldCreateClient_WhenGetClientIsCalled() throws Exception {
        //mEventing = new EventingImpl(new EventBus(), new Handler());
        ConsentsClient client = uCoreAdapter.getAppFrameworkClient(CLIENT_CLASS, ACCESS_TOKEN, gsonConverterMock);
        verify(uCoreAdapter.getAppFrameworkClient(CLIENT_CLASS, ACCESS_TOKEN, gsonConverterMock));
        // assertThat(client).isSameAs(clientMock);
    }

   /* @Test(expected = NullPointerException.class)
    public void ShouldSetCorrectBaseUrl_WhenGetUGrowClientIsCalled() throws Exception {
        uCoreAdapter.getAppFrameworkClient(CLIENT_CLASS, ACCESS_TOKEN, gsonConverterMock);

        verify(restAdapterBuilderMock).setEndpoint(TEST_BASE_URL);
    }*/

//    @Test
//    public void ShouldSetCorrectBaseUrl_WhenGetInsightsClientIsCalled() throws Exception {
//        uCoreAdapter.getInsightsClient(CLIENT_CLASS, ACCESS_TOKEN, gsonConverterMock);
//
//        verify(restAdapterBuilderMock).setEndpoint(TEST_INSIGHTS_URL);
//        verify(restAdapterMock).setLogLevel(UCoreAdapter.LOG_LEVEL);
//    }

   /* @Test
    public void ShouldSetHeaders_WhenRequestIsIntercepted() throws Exception {
        uCoreAdapter.getAppFrameworkClient(CLIENT_CLASS, ACCESS_TOKEN, gsonConverterMock);

        interceptRequest();

        verify(requestFacadeMock).addHeader("Content-Type", "application/json");
        verify(requestFacadeMock).addHeader("Authorization", "bearer " + ACCESS_TOKEN);
    }*/


    protected void interceptRequest() {
        verify(restAdapterBuilderMock).setRequestInterceptor(requestInterceptorCaptor.capture());
        RequestInterceptor interceptor = requestInterceptorCaptor.getValue();

        interceptor.intercept(requestFacadeMock);
    }

    public void ShouldAddVersionAPIHeader_WhenRequestIsIntercepted() throws Exception {
        uCoreAdapter.getAppFrameworkClient(CLIENT_CLASS, ACCESS_TOKEN, gsonConverterMock);
        interceptRequest();

        verify(requestFacadeMock).addHeader(UCoreAdapter.API_VERSION_CUSTOM_HEADER, String.valueOf(UCoreAdapter.API_VERSION));
        verify(requestFacadeMock).addHeader(eq(UCoreAdapter.APP_AGENT_HEADER), anyString());
    }

    @Test(expected = NullPointerException.class)
    public void ShouldGetClient_WhenRequestIsIntercepted() throws Exception {
        uCoreAdapter.getClient(CLIENT_CLASS, BASE_HSDP_URL, ACCESS_TOKEN, gsonConverterMock);
        interceptRequest();

        verify(restAdapterBuilderMock).setEndpoint(TEST_INSIGHTS_URL);
        verify(restAdapterMock).setLogLevel(UCoreAdapter.LOG_LEVEL);

        verify(requestFacadeMock).addHeader(UCoreAdapter.API_VERSION_CUSTOM_HEADER, String.valueOf(UCoreAdapter.API_VERSION));
        verify(requestFacadeMock).addHeader(eq(UCoreAdapter.APP_AGENT_HEADER), anyString());
    }

    @Test
    public void ShouldGetAppAgentHeader_WhenRequestIsIntercepted() throws Exception {
        String agentHeader = uCoreAdapter.getAppAgentHeader();
       assertThat(agentHeader).isNotEmpty();
    }

//    @Test
//    public void ShouldGetBuildTime_WhenRequestIsIntercepted() throws Exception {
//        String agentHeader = uCoreAdapter.getBuildTime();
//        assertThat(agentHeader).isNotEmpty();
//    }
}
