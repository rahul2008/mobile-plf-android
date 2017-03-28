package com.philips.cdp.registration.ui.traditional.mobile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddSecureEmailPresenterTest {

    @Mock
    AddSecureEmailContract contractMock;

    private AddSecureEmailPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new AddSecureEmailPresenter(contractMock);
    }

    @After
    public void tearDown() throws Exception {
        contractMock = null;
        presenter = null;
    }

    @Test
    public void testMaybeLaterClicked() {
        presenter.maybeLaterClicked();
        Mockito.verify(contractMock).showWelcomeScreen();
    }

    @Test
    public void testAddEmailClicked_invalidEmail() {
        String emailId = "ahkjahsdkjh";
        presenter.addEmailClicked(emailId);
        contractMock.showInvalidEmailError();
    }

    @Test
    public void testAddEmailClicked_emptyEmail() {
        String emailId = "";
        presenter.addEmailClicked(emailId);
        contractMock.showInvalidEmailError();
    }

    @Test
    public void testAddEmailClicked_nullEmail() {
        String emailId = null;
        presenter.addEmailClicked(emailId);
        contractMock.showInvalidEmailError();
    }

    @Test
    public void testAddEmailClicked_validEmail() {
        String emailId = "abcd@philips.com";
        presenter.addEmailClicked(emailId);
        contractMock.onAddRecoveryEmailSuccess();
    }

}