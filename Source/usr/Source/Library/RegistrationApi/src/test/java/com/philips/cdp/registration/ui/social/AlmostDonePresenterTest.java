package com.philips.cdp.registration.ui.social;

import android.os.Bundle;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationSettingsURL;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static com.philips.cdp.registration.ui.utils.RegConstants.EMAIL_ADDRESS_ALREADY_USE_CODE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AlmostDonePresenterTest {

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    private AlmostDoneContract mockContract;

    @Mock
    User mockUser;

    @Mock
    FieldsValidator mockFieldsValidator;

    private UserRegistrationFailureInfo userRegistrationFailureInfo;

    private RegistrationSettingsURL registrationSettingsURL;

    private AlmostDonePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        presenter = new AlmostDonePresenter(mockContract, mockUser);
        userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        registrationSettingsURL = new RegistrationSettingsURL();
    }

    @After
    public void tearDown() throws Exception {
        mockRegistrationComponent = null;
        mockContract = null;
        presenter = null;
        mockUser = null;
        userRegistrationFailureInfo = null;
        registrationSettingsURL = null;
    }

    @Test
    public void testNetworkState_enabled() {
        presenter.onNetWorkStateReceived(true);
        verify(mockContract).validateEmailFieldUI();
    }

    @Test
    public void testNetworkState_disabled() {
        presenter.onNetWorkStateReceived(false);
        verify(mockContract).handleOfflineMode();
    }

    @Test
    public void testUpdateTermsAndReceiveMarketingOpt_true(){
        when(mockUser.isTermsAndConditionAccepted()).thenReturn(true);
        when(mockUser.getReceiveMarketingEmail()).thenReturn(true);
        presenter.updateTermsAndReceiveMarketingOpt(true);
        verify(mockContract).hideMarketingOptCheck();
        verify(mockContract).updateTermsAndConditionView();
    }

    @Test
    public void testUpdateTermsAndReceiveMarketingOpt_false(){
        when(mockUser.getReceiveMarketingEmail()).thenReturn(true);
        presenter.updateTermsAndReceiveMarketingOpt(false);
        verify(mockContract).hideMarketingOptCheck();
    }

    @Test
    public void testReceiveMarketingOpt_false(){
        when(mockUser.isTermsAndConditionAccepted()).thenReturn(true);
        when(mockUser.getReceiveMarketingEmail()).thenReturn(false);
        presenter.updateTermsAndReceiveMarketingOpt(true);
        verify(mockContract).showMarketingOptCheck();
    }

    @Test
    public void testParseRegistrationInfo(){
        Bundle bundle = new Bundle();
        JSONObject resultJsonObject = new JSONObject();
        try {
            resultJsonObject.put(RegConstants.REGISTER_GIVEN_NAME, "Health care");
            resultJsonObject.put(RegConstants.REGISTER_DISPLAY_NAME, "Philips");
            resultJsonObject.put(RegConstants.REGISTER_FAMILY_NAME, "HSDP");
            resultJsonObject.put(RegConstants.REGISTER_EMAIL, "maqsoodkhan@gmail.com");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle.putString(RegConstants.SOCIAL_TWO_STEP_ERROR,resultJsonObject.toString());
        presenter.parseRegistrationInfo(bundle);
        assertEquals("Health care",presenter.getGivenName());
        assertEquals("Philips",presenter.getDisplayName());
        assertEquals("HSDP",presenter.getFamilyName());
        assertEquals("maqsoodkhan@gmail.com",presenter.getEmail());
        assertEquals(true,presenter.isEmailExist());
    }

    @Test
    public void testParseRegistrationInfo_isEmailExist(){
        Bundle bundle = new Bundle();
        JSONObject resultJsonObject = new JSONObject();
        bundle.putString(RegConstants.SOCIAL_TWO_STEP_ERROR,resultJsonObject.toString());
        presenter.parseRegistrationInfo(bundle);
        assertEquals(false,presenter.isEmailExist());
    }

   @Test
    public void testUpdateReceivingMarketingEmail_success(){
        presenter.updateUser(true);
        presenter.onUpdateSuccess();
        verify(mockContract).hideMarketingOptSpinner();
        verify(mockContract).trackMarketingOpt();
    }

   @Test
    public void testUpdateReceivingMarketingEmail_failure_invalid_refresh_token(){
        presenter.onUpdateFailedWithError(Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE));
        verify(mockContract).hideMarketingOptSpinner();
        verify(mockContract).replaceWithHomeFragment();
    }

    @Test
    public void testUpdateReceivingMarketingEmail_failure_to_connect(){
        presenter.onUpdateFailedWithError(RegConstants.FAILURE_TO_CONNECT);
        verify(mockContract).failedToConnectToServer();
    }

    @Test
    public void testUpdateReceivingMarketingEmail_marketing_opt_failure() {
        presenter.onUpdateFailedWithError(33);
        verify(mockContract).updateMarketingOptFailedError();
    }

    @Test
    public void testRegisterSocialAccount(){
        presenter.setOnline(true);
        presenter.setDisplayName("Maqsood");
        presenter.setEmail("maqsoodkhan89@gmail.com");
        presenter.setFamilyName("Khan");
        presenter.setGivenName("MaqsoodKhan");
        presenter.setEmailExist(true);
        presenter.register(true,presenter.getEmail());
        verify(mockContract).hideErrorMessage();
        verify(mockContract).showMarketingOptSpinner();
        verify(mockUser).registerUserInfoForSocial(presenter.getGivenName(),
               presenter.getDisplayName(),presenter.getFamilyName(),presenter.getEmail(),true,true,presenter,null);
    }

    @Test
    public void testLoginFailedWithError_is_china_flow() {
        userRegistrationFailureInfo.setErrorCode(EMAIL_ADDRESS_ALREADY_USE_CODE);
        registrationSettingsURL.setMobileFlow(true);
        presenter.onLoginFailedWithError(userRegistrationFailureInfo);
        verify(mockContract).hideMarketingOptSpinner();
        verify(mockContract).phoneNumberAlreadyInuseError();
        verify(mockContract).showLoginFailedError();
    }

    @Test
    public void testLoginFailedWithError_email_flow() {
        userRegistrationFailureInfo.setErrorCode(EMAIL_ADDRESS_ALREADY_USE_CODE);
        registrationSettingsURL.setMobileFlow(false);
        presenter.onLoginFailedWithError(userRegistrationFailureInfo);
        verify(mockContract).hideMarketingOptSpinner();
        verify(mockContract).emailAlreadyInuseError();
        verify(mockContract).showLoginFailedError();
    }

    @Test
    public void testLoginFailedWithTwoStepError() {
        JSONObject jsonObject = new JSONObject();
        String socialRegistrationToken = null;
        presenter.onLoginFailedWithTwoStepError(jsonObject,socialRegistrationToken);
        verify(mockContract).hideMarketingOptSpinner();
    }

    @Test
    public void testLoginFailedWithMergeFlowError() {
        presenter.onLoginFailedWithMergeFlowError("scmksdwsdwo","existingProvider","conflictingIdentityProvider",
                "conflictingIdpNameLocalized","existingIdpNameLocalized","emailId");
        verify(mockContract).hideMarketingOptSpinner();
        verify(mockContract).addMergeAccountFragment();
    }

    @Test
    public void testContinueSocialProviderLoginFailure_displayNameErrorMessage() {
        userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        presenter.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
        verify(mockContract).hideMarketingOptSpinner();
    }

    @Test
    public void testContinueSocialProviderLoginFailure_emailErrorMessage() {
        userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        presenter.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
        verify(mockContract).hideMarketingOptSpinner();
        //verify(mockContract).emailErrorMessage(userRegistrationFailureInfo);
    }

    @Test
    public void testContinueSocialProviderLoginFailure_emailAlreadyInUse_mobile() {
        userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(EMAIL_ADDRESS_ALREADY_USE_CODE);
        registrationSettingsURL.setMobileFlow(true);
        presenter.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
        verify(mockContract).phoneNumberAlreadyInuseError();
    }

    @Test
    public void testContinueSocialProviderLoginFailure_emailAlreadyInUse_email() {
        userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setErrorCode(EMAIL_ADDRESS_ALREADY_USE_CODE);
        registrationSettingsURL.setMobileFlow(false);
        presenter.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
        verify(mockContract).emailAlreadyInuseError();
    }

    @Test
    public void testContinueSocialProviderLoginSuccess() {
        presenter.onContinueSocialProviderLoginSuccess();
        verify(mockContract).handleContinueSocialProvider();
    }

    @Test
    public void testIsValidEmail() {
        presenter.isValidEmail();
        assertTrue(mockFieldsValidator.isValidEmail("maqsoodkhan@gmail.com"));
    }

//    @Test
//    public void testIsEmailVerificationStatus() {
//        presenter.isEmailVerificationStatus();
//        verify(mockUser).getEmailVerificationStatus();
//    }

    @Test
    public void testHandleClearUserData() {
        presenter.handleClearUserData();
        verify(mockUser).logout(null);
    }

    @Test
    public void testHandleUpdate_marketing_opt_online() {
        presenter.onNetWorkStateReceived(true);
        presenter.handleUpdateMarketingOpt();
        verify(mockContract).handleUpdateUser();
    }

    @Test
    public void testHandleUpdate_marketing_opt_offline() {
        presenter.onNetWorkStateReceived(false);
        presenter.handleUpdateMarketingOpt();
        verify(mockContract).marketingOptCheckDisable();
    }
}