package com.philips.amwelluapp.welcome;

import com.philips.amwelluapp.base.PTHBaseView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PTHWelcomePresenterTest {

    PTHWelcomePresenter pthWelcomePresenter;

    @Mock
    PTHBaseView PTHBaseViewMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthWelcomePresenter = new PTHWelcomePresenter(PTHBaseViewMock);
    }

    @Test
    public void onEvent() throws Exception {
        //TODO: Since its empty I am not writing any assertions for it
        pthWelcomePresenter.onEvent(-1);
    }

    @Test
    public void initializeAwsdk() throws Exception {
        //pthWelcomePresenter.initializeAwsdk();
    }

    @Test
    public void onInitializationResponse() throws Exception {

    }

    @Test
    public void onInitializationFailure() throws Exception {

    }

    @Test
    public void onLoginResponse() throws Exception {

    }

    @Test
    public void onLoginFailure() throws Exception {

    }

}