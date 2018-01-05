package com.philips.cdp.registration.ui.traditional.mobile;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.update.UpdateUserProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
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
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        presenter = new AddSecureEmailPresenter(contractMock);
        presenter.injectMocks(updateUserProfileMock);
    }

    @After
    public void tearDown() throws Exception {
        contractMock = null;
        presenter = null;
        updateUserProfileMock = null;
        registrationComponentMock = null;

    }

    @Test
    public void testMaybeLaterClicked() {
        presenter.maybeLaterClicked();
        verify(contractMock).registrationComplete();
    }

    @Test
    public void testAddEmailClicked_invalidEmail() {
        String emailId = "ahkjahsdkjh";
        presenter.addEmailClicked(emailId);
        verify(contractMock).showInvalidEmailError();
        verify(contractMock, never()).showProgress();
        verifyNoMoreInteractions(contractMock);
    }

    @Test
    public void testAddEmailClicked_emptyEmail() {
        String emailId = "";
        presenter.addEmailClicked(emailId);
        verify(contractMock).showInvalidEmailError();
        verify(contractMock, never()).showProgress();
        verifyNoMoreInteractions(contractMock);
    }

    @Test
    public void testAddEmailClicked_nullEmail() {
        String emailId = null;
        presenter.addEmailClicked(emailId);
        verify(contractMock).showInvalidEmailError();
        verify(contractMock, never()).showProgress();
        verifyNoMoreInteractions(contractMock);
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