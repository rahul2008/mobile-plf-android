package com.philips.cdp.registration.app.tagging;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 12/3/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class AppTaggingErrorsTest {

    AppTaggingErrors appTaggingErrors;

    private static final String EMAIL_ALREADY_IN_USE = "email already in use";

    private static final String MOBILE_ALREADY_IN_USE = "mobile no already in use";

    private static final String EMAIL_IS_NOT_VERIFIED = "email is not verified";

    private static final String WE_RE_HAVING_TROUBLE_LOGINING_USER = "login network error";

    private static final String WE_RE_HAVING_TROUBLE_REGISTRING_USER = "registration network error";

    private static final String EMAIL_ADDRESS_NOT_EXIST_ERROR = "no account with this email address";

    private static final String RESEND_VERIFICATION_NETWORK_ERROR = "resend verification network error";

    private static final String FAILURE_FORGOT_PASSWORD_ERROR = "forgot password network error";

    private final static int NETWORK_ERROR_CODE = 111;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private final static int INVALID_CREDENTIALS_CODE = 210;

    private final static int EMAIL_NOT_VERIFIED_CODE = 112;

    private final static int FORGOT_PASSWORD_FAILURE_ERROR_CODE = 212;

    @Mock
    private UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private com.philips.platform.appinfra.tagging.AppTaggingInterface appTaggingInterfaceMock;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        Mockito.when(registrationComponentMock.getAppTaggingInterface()).thenReturn(appTaggingInterfaceMock);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);

        appTaggingErrors=new AppTaggingErrors();
    }

    @Test
    public void trackActionForgotPasswordFailure_forNETWORK_ERROR_CODE() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(NETWORK_ERROR_CODE);
        appTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfoMock,"flowType");

        String SEND_DATA = "sendData";
        String TECHNICAL_ERROR = "error";

        /*Mockito.verify(appTaggingErrors).trackActionForErrorMapping(SEND_DATA,
               TECHNICAL_ERROR, FAILURE_FORGOT_PASSWORD_ERROR);*/
    }

    @Test
    public void trackActionForgotPasswordFailure_forFORGOT_PASSWORD_FAILURE_ERROR_CODE() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(FORGOT_PASSWORD_FAILURE_ERROR_CODE);
        appTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfoMock,"flowType");

        String SEND_DATA = "sendData";
        String TECHNICAL_ERROR = "error";

        /*Mockito.verify(appTaggingErrors).trackActionForErrorMapping(SEND_DATA,
               TECHNICAL_ERROR, FAILURE_FORGOT_PASSWORD_ERROR);*/
    }

    @Test
    public void trackActionForgotPasswordFailure_forDefault() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(1000);
        Mockito.when(userRegistrationFailureInfoMock.getErrorDescription()).thenReturn("description");
        appTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfoMock,"flowType");

        String SEND_DATA = "sendData";
        String TECHNICAL_ERROR = "error";

        /*Mockito.verify(appTaggingErrors).trackActionForErrorMapping(SEND_DATA,
               TECHNICAL_ERROR, FAILURE_FORGOT_PASSWORD_ERROR);*/
    }


    @Test
    public void trackActionResendNetworkFailure() throws Exception {

        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(NETWORK_ERROR_CODE);
        appTaggingErrors.trackActionResendNetworkFailure(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionResendNetworkDefault() throws Exception {

        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(100);
        Mockito.when(userRegistrationFailureInfoMock.getErrorDescription()).thenReturn("description");
        appTaggingErrors.trackActionResendNetworkFailure(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionLoginError_NETWORK_ERROR_CODE() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(NETWORK_ERROR_CODE);
        appTaggingErrors.trackActionLoginError(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionLoginError_EMAIL_NOT_VERIFIED_CODE() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(EMAIL_NOT_VERIFIED_CODE);
        appTaggingErrors.trackActionLoginError(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionLoginError_Default() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(100);
        Mockito.when(userRegistrationFailureInfoMock.getErrorDescription()).thenReturn("description");
        appTaggingErrors.trackActionLoginError(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionRegisterError_NETWORK_ERROR_CODE() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(NETWORK_ERROR_CODE);
        appTaggingErrors.trackActionRegisterError(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionRegisterError_EMAIL_ADDRESS_ALREADY_USE_CODE() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(EMAIL_ADDRESS_ALREADY_USE_CODE);
        appTaggingErrors.trackActionRegisterError(userRegistrationFailureInfoMock,"flowType");
    }

    @Test
    public void trackActionRegisterError_Default() throws Exception {
        Mockito.when(userRegistrationFailureInfoMock.getErrorCode()).thenReturn(100);
        Mockito.when(userRegistrationFailureInfoMock.getErrorDescription()).thenReturn("description");
        appTaggingErrors.trackActionRegisterError(userRegistrationFailureInfoMock,"flowType");
    }

}