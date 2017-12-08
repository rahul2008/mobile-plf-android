package com.philips.cdp.registration.ui.customviews;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

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
public class LoginIdEditTextTest {

    @Mock
    private android.content.Context contextMock;
    private LoginIdEditText loginIdEditText;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void initUi() throws Exception {

       // loginIdEditText.initUi(Mockito.anyInt());

    }

    @Test
    public void checkingEmailorMobileSignIn() throws Exception {
    }

    @Test
    public void getEmailId() throws Exception {
    }

    @Test
    public void isValidEmail() throws Exception {
    }

    @Test
    public void setValidEmail() throws Exception {
    }

    @Test
    public void setErrDescription() throws Exception {
    }

    @Test
    public void getSavedEmailErrDescription() throws Exception {
    }

    @Test
    public void showEtEmailFocusEnable() throws Exception {
    }

    @Test
    public void showEtEmailFocusDisable() throws Exception {
    }

    @Test
    public void showValidEmailAlert() throws Exception {
    }

    @Test
    public void showInvalidAlert() throws Exception {
    }

    @Test
    public void setOnUpdateListener() throws Exception {
    }

    @Test
    public void onFocusChange() throws Exception {
    }

    @Test
    public void onClick() throws Exception {
    }

    @Test
    public void showErrPopUp() throws Exception {
    }

    @Test
    public void isEmailErrorVisible() throws Exception {
    }

    @Test
    public void beforeTextChanged() throws Exception {
    }

    @Test
    public void onTextChanged() throws Exception {
    }

    @Test
    public void afterTextChanged() throws Exception {
    }

    @Test
    public void setHint() throws Exception {
    }

    @Test
    public void setInputType() throws Exception {
    }

    @Test
    public void setClickableTrue() throws Exception {
    }

    @Test
    public void isShown() throws Exception {
    }

    @Test
    public void getLoginIdEditText() throws Exception {
    }

    @Test
    public void setImeOptions() throws Exception {
    }

}