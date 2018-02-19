package com.philips.platform.ths.welcome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSWelcomePresenterTest {

    THSWelcomePresenter pthWelcomePresenter;

    @Mock
    THSWelcomeFragment pTHBaseViewMock;

    @Mock
    Context contextMock;

    @Mock
    AWSDK awsdk;

    @Mock
    THSSDKError THSSDKError;

    @Mock
    FragmentActivity activityMock;

    @Mock
    ConsumerManager ConsumerManagerMock;



    @Mock
    Throwable throwableMock;

    @Mock
    Consumer consumerMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    Button buttonMock;

    @Mock
    User userMock;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterface;

   @Mock
   ConsumerManager consumerManagerMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthWelcomePresenter = new THSWelcomePresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdk);
        THSManager.getInstance().setUser(userMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterface);
        THSManager.getInstance().TEST_FLAG = true;
        when(userMock.getHsdpUUID()).thenReturn("abc");
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        List list = new ArrayList();
        list.add(thsConsumerMock);
        when(thsConsumerMock.getDependents()).thenReturn(list);
        when(thsConsumerMock.getDependents()).thenReturn(list);
    }

    @Test
    public void onEventAppointments() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.appointments);
        verifyNoMoreInteractions(consumerManagerMock);
       // verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventFirstTimeUser() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.appointments);
        verifyNoMoreInteractions(consumerManagerMock);
        // verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventVisitHistory() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.visit_history);
        verifyNoMoreInteractions(consumerManagerMock);
       // verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventVisitHistoryFirstTimeUser() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.visit_history);
        verifyNoMoreInteractions(consumerManagerMock);
        // verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventHowItWorkd() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.how_it_works);
        verify(pTHBaseViewMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle)isNull(),anyBoolean());
    }

    @Test
    public void onEventHowItWorkdFirstTimeUser() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.how_it_works);
            verify(pTHBaseViewMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle) isNull(),anyBoolean());

    }

    @Test
    public void onEventths_start() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.ths_start);
        verifyNoMoreInteractions(consumerManagerMock);
       // verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onEventths_startFirstTimeUser() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.ths_start);
        verifyNoMoreInteractions(consumerManagerMock);
        // verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    /*@Test
    public void initializeAwsdkThrowsMalformedURLException() throws AWSDKInitializationException {
        doThrow(MalformedURLException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }*/

   /* @Test
    public void initializeAwsdkThrowsURISyntaxException() throws AWSDKInitializationException {
        doThrow(URISyntaxException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }*/

    /*@Test
    public void initializeAwsdkThrowsAWSDKInstantiationException() throws AWSDKInitializationException {
        doThrow(AWSDKInstantiationException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }*/

   /* @Test
    public void initializeAwsdkThrowsAWSDKInitializationException() throws AWSDKInitializationException {
        doThrow(AWSDKInitializationException.class).when(awsdk).initialize(any(Map.class),any(SDKCallback.class));
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.initializeAwsdk();
        verify(awsdk).initialize(any(Map.class),any(SDKCallback.class));
    }*/

    /*@Test
    public void onInitializationResponse() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }*/

   /* @Test
    public void onInitializationResponseException() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(consumerManagerMock).checkConsumerExists(anyString(),any(SDKCallback.class));
    }

    @Test
    public void onInitializationFailure()  {
        pthWelcomePresenter.onInitializationFailure(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onInitializationFailureWhenContextIsNotNull()  {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onInitializationFailure(throwableMock);
        verify(pTHBaseViewMock).hideProgressBar();
    }*/


/*    @Test
    public void authenticateThrowsAWSDKInstantiationException() throws Exception {
        pthWelcomePresenter.onInitializationResponse(null, THSSDKError);
        verify(pTHBaseViewMock).hideProgressBar();
    }*/



    /*@Test
    public void onResponseCheckIserExisits(){
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
     //   pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.ATTACHMENT_NOT_FOUND);
       // when(sdkErrorReason.name()).thenReturn("sss");

        Map object = new HashMap();
        object.put("default","default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(null,THSSDKError);

        verify(pTHBaseViewMock).showToast(anyString());
    }*/

    /*@Test
    public void onResponseCheckIserExisitsNoErrorReason(){
        when(THSSDKError.getSdkError()).thenReturn(sdkErrorMock);
        when(sdkErrorMock.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(THSSDKError.getHttpResponseCode()).thenReturn(HttpsURLConnection.HTTP_UNAUTHORIZED);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        //   pthWelcomePresenter.onLoginResponse(null, THSSDKError);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
       // when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.ATTACHMENT_NOT_FOUND);
        // when(sdkErrorReason.name()).thenReturn("sss");

        Map object = new HashMap();
        object.put("default","default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(null,THSSDKError);

        verify(pTHBaseViewMock).hideProgressBar();
    }*/

    /*@Test
    public void onResponseCheckIserExisitsSuccessTrue() {
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        Map object = new HashMap();
        object.put("default", "default");
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(true, THSSDKError);
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }*/

   /* @Test
    public void onResponseCheckIserExisitsSuccessTrueException() {
        doThrow(AWSDKInstantiationException.class).when(awsdk).authenticateMutual(anyString(),any(SDKCallback.class));
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        Map object = new HashMap();
        object.put("default", "default");
      //  doThrow(AWSDKInitializationException.class).when(awsdk).authenticateMutual(anyString(),any(SDKCallback.class));
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onResponse(true, THSSDKError);
        verify(awsdk).authenticateMutual(anyString(), any(SDKCallback.class));
    }*/

    @Test
    public void checkIserExisitsSuccessException() {
        THSManager.getInstance().setAwsdk(awsdk);
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        ConsumerManager con = awsdk.getConsumerManager();

        doThrow(AWSDKInstantiationException.class).when(con).checkConsumerExists(eq("1234"),any(SDKCallback.class));

        when(userMock.getHsdpUUID()).thenReturn("1234");
        when(userMock.getHsdpAccessToken()).thenReturn("1234");
        Map object = new HashMap();
        object.put("default", "default");
        //  doThrow(AWSDKInitializationException.class).when(awsdk).authenticateMutual(anyString(),any(SDKCallback.class));
        when(appConfigurationInterface.getPropertyForKey(anyString(),
                anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(object);
        pthWelcomePresenter.onEvent(R.id.appointments);
        verifyNoMoreInteractions(consumerManagerMock);
        //verify(con).checkConsumerExists(eq("1234"), any(SDKCallback.class));
    }

   /* @Test
    public void onResponseCheckIserExisitsSuccessFalse() {
        when(awsdk.getConsumerManager()).thenReturn(ConsumerManagerMock);
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        pthWelcomePresenter.onResponse(false, THSSDKError);
        verify(pTHBaseViewMock).hideProgressBar();
    }

    @Test
    public void onFailure(){
        pthWelcomePresenter.onFailure(throwableMock);
    }*/

    @Test
    public void onEventCustomer_support() throws Exception {
        when(pTHBaseViewMock.getContext()).thenReturn(contextMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        when(userMock.getHsdpUUID()).thenReturn("1234");
        pthWelcomePresenter.onEvent(R.id.customer_support);
        verify(pTHBaseViewMock).addFragment(any(THSBaseFragment.class),anyString(),(Bundle)isNull(),anyBoolean());
    }

}
