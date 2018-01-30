package com.philips.cdp.registration.ui.traditional.mobile;

import com.janrain.android.Jump;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.app.infra.ServiceDiscoveryWrapper;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.restclient.URRequest;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.platform.appinfra.rest.request.RequestQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MobileVerifyCodePresenterTest {

    private static final int SMS_ACTIVATION_REQUEST_CODE = 100;
    private static final int RESEND_OTP_REQUEST_CODE = 101;

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    private ServiceDiscoveryWrapper mockServiceDiscoveryWrapper;

    @Mock
    private MobileVerifyCodeContract mockContract;

    @Mock
    private URRequest mockURRequest;

    @Mock
    private HttpClientServiceReceiver.Listener listenerMock;

    @Mock
    private NetworkUtility mockNetworkUtility;

    @Mock
    private Jump mockJump;

    private MobileVerifyCodePresenter presenter;
    @Mock
    private com.philips.platform.appinfra.rest.RestInterface appRestInterfaceMock;
    @Mock
    RequestQueue mockRequestQueue;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        Mockito.when(mockRegistrationComponent.getRestInterface()).thenReturn(appRestInterfaceMock);
        Mockito.when(appRestInterfaceMock.getRequestQueue()).thenReturn(mockRequestQueue);
        presenter = new MobileVerifyCodePresenter(mockContract);
        presenter.mockInjections(mockServiceDiscoveryWrapper);
    }

    @After
    public void tearDown() throws Exception {
        mockRegistrationComponent = null;
        mockServiceDiscoveryWrapper = null;
        mockContract = null;
        presenter = null;
    }

//    @Test
//    public void testVerifyButtonClicked() {
//        when(mockContract.getServiceIntent()).thenReturn(mock(Intent.class));
//        when((mockContract.getClientServiceRecevier())).thenReturn(mock(HttpClientServiceReceiver.class));
//        presenter.verifyMobileNumber("uuid", "123");
//        verify(mockContract).startService(any(Intent.class));
//    }

    @Test
    public void testVerifyButtonClicked() {
        Jump.setCaptureDomain("traditional");
        when(mockNetworkUtility.isNetworkAvailable()).thenReturn(true);
        //  when(RegistrationConfiguration.getInstance().getComponent().getRestInterface().getRequestQueue()).thenReturn(new RequestQueue(mock(Cache.class), mock(Network.class)));
        presenter.verifyMobileNumber("uuid", "123");
      //  verify(mockContract).onSuccessResponse("Success");
    }

    @Test
    public void testNetworkState_Enabled() {
        when(mockNetworkUtility.isNetworkAvailable()).thenReturn(true);
        presenter.onNetWorkStateReceived(true);
        verify(mockContract).enableVerifyButton();

    }

    @Test
    public void testNetworkState_Disabled() {
        when(mockNetworkUtility.isNetworkAvailable()).thenReturn(false);
        presenter.onNetWorkStateReceived(false);
        verify(mockContract).disableVerifyButton();
    }

    @Test
    public void testResultReceived_EmptyResult() {
//        Bundle resultData = new Bundle();
        //presenter.onReceiveResult(0, resultData);
        presenter.handleActivation("Success");
        verify(mockContract).showSmsSendFailedError();
    }

//    @Test
//    public void testResultReceived_SmsVerificationSuccess() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("stat", "ok");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(SMS_ACTIVATION_REQUEST_CODE, resultData);
//        verify(mockContract).refreshUserOnSmsVerificationSuccess();
//    }
//
//    @Test
//    public void testResultReceived_SmsVerificationSuccessCorruptBundleKey() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("stats", "ok");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(SMS_ACTIVATION_REQUEST_CODE, resultData);
//        verify(mockContract, never()).refreshUserOnSmsVerificationSuccess();
//    }
//
//    @Test
//    public void testResultReceived_SmsVerificationSuccessCorruptBundleValue() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("stats", "okay");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(SMS_ACTIVATION_REQUEST_CODE, resultData);
//        verify(mockContract, never()).refreshUserOnSmsVerificationSuccess();
//    }
//
//    @Test
//    public void testResultReceived_SmsVerificationSuccessEmptyBundle() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(SMS_ACTIVATION_REQUEST_CODE, resultData);
//        verify(mockContract, never()).refreshUserOnSmsVerificationSuccess();
//        verify(mockContract).smsVerificationResponseError();
//    }
//
//    @Test
//    public void testResultReceived_InvalidOtpWithCode() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("stat", "not ok");
//            resultJsonObject.put("code", "200");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(SMS_ACTIVATION_REQUEST_CODE, resultData);
//        verify(mockContract).setOtpInvalidErrorMessage();
//        verify(mockContract).showOtpInvalidError();
//    }
//
//    @Test
//    public void testResultReceived_InvalidOtpWithWrongCode() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("stat", "not ok");
//            resultJsonObject.put("code", "404");
//            resultJsonObject.put("error_description", "Otp is not valid");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(SMS_ACTIVATION_REQUEST_CODE, resultData);
//        verify(mockContract).setOtpErrorMessageFromJson("Otp is not valid");
//        verify(mockContract).showOtpInvalidError();
//    }
//
//    @Test
//    public void testResultReceived_OtpResendSuccess() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("errorCode", "0");
//            resultJsonObject.put("code", "404");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(RESEND_OTP_REQUEST_CODE, resultData);
//        //       verify(mockContract).enableResendButtonAndHideSpinner();
//    }
//
//    @Test
//    public void testResultReceived_OtpResendFailure() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("errorCode", "20");
//            resultJsonObject.put("code", "404");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(RESEND_OTP_REQUEST_CODE, resultData);
//        //      verify(mockContract).showSmsResendTechincalError("20");
//    }
//
//    @Test
//    public void testResultReceived_OtpResendException() {
//        Bundle resultData = new Bundle();
//        JSONObject resultJsonObject = new JSONObject();
//        try {
//            resultJsonObject.put("errorCodes", "20");
//            resultJsonObject.put("code", "404");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        resultData.putString("responseStr", resultJsonObject.toString());
//        presenter.onReceiveResult(RESEND_OTP_REQUEST_CODE, resultData);
//        //     verify(mockContract).showSmsResendTechincalError("50");
//    }
}