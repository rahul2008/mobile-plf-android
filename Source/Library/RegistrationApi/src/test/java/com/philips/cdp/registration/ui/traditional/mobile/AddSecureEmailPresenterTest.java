package com.philips.cdp.registration.ui.traditional.mobile;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.update.UpdateUserProfile;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddSecureEmailPresenterTest {

    @Mock
    RegistrationComponent registrationComponentMock;

    @Mock
    UpdateUserProfile updateUserProfileMock;

    @Mock
    AddSecureEmailContract contractMock;

    private AddSecureEmailPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(registrationComponentMock);
        presenter = new AddSecureEmailPresenter(contractMock);
        presenter.injectMocks(updateUserProfileMock);
    }

    @After
    public void tearDown() throws Exception {
        contractMock = null;
        presenter = null;
    }

    @Test
    public void testMaybeLaterClicked() {
        presenter.maybeLaterClicked();
        verify(contractMock).showWelcomeScreen();
    }

    @Test
    public void testAddEmailClicked_invalidEmail() {
        String emailId = "ahkjahsdkjh";
        presenter.addEmailClicked(emailId);
        verify(contractMock).showInvalidEmailError();
    }

    @Test
    public void testAddEmailClicked_emptyEmail() {
        String emailId = "";
        presenter.addEmailClicked(emailId);
        verify(contractMock).showInvalidEmailError();
    }

    @Test
    public void testAddEmailClicked_nullEmail() {
        String emailId = null;
        presenter.addEmailClicked(emailId);
        verify(contractMock).showInvalidEmailError();
    }

    @Test
    public void testAddEmailClicked_validEmail() {
        String emailId = "abcd@philips.com";
        presenter.addEmailClicked(emailId);
        verify(updateUserProfileMock).updateUserEmail(emailId, presenter);
    }

    @Test
    public void testUpdateUserProfileSuccess() {
        presenter.onSuccess();
        verify(contractMock).onAddRecoveryEmailSuccess();
    }

    @Test
    public void testUpdateUserProfileFailure() {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "Internal error");
        JSONObject errorJson = new JSONObject(errorMap);
        CaptureApiError error = new CaptureApiError(errorJson, "engageMock", "providerMock");
        presenter.onFailure(error);
        verify(contractMock).onAddRecoveryEmailFailure("Internal error");
    }

    @Test
    public void testNetwork_online(){
        presenter.onNetWorkStateReceived(true);
        verify(contractMock).enableButtons();
        verify(contractMock).hideError();
    }

    @Test
    public void testNetwork_offline(){
        presenter.onNetWorkStateReceived(false);
        verify(contractMock).disableButtons();
        verify(contractMock).showNetworkUnavailableError();
    }

}