package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.Jump.CaptureApiResultHandler;
import com.janrain.android.capture.Capture.InvalidApidChangeException;
import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.engage.session.JRSession;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.controller.AddConsumerInterest;
import com.philips.cdp.registration.controller.ForgotPassword;
import com.philips.cdp.registration.controller.LoginSocialProvider;
import com.philips.cdp.registration.controller.LoginTraditional;
import com.philips.cdp.registration.controller.RefreshUserSession;
import com.philips.cdp.registration.controller.RegisterSocial;
import com.philips.cdp.registration.controller.RegisterTraditional;
import com.philips.cdp.registration.controller.ResendVerificationEmail;
import com.philips.cdp.registration.controller.UpdateReceiveMarketingEmail;
import com.philips.cdp.registration.controller.UpdateUserRecord;
import com.philips.cdp.registration.dao.ConsumerArray;
import com.philips.cdp.registration.dao.ConsumerInterest;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.RefreshandUpdateUserHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.security.SecureStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class User {

    private boolean mEmailVerified;

    private Context mContext;

    private JSONObject mConsumerInterestObject;

    private JSONArray mConsumerInterestArray;

    private String USER_EMAIL = "email";

    private String USER_GIVEN_NAME = "givenName";

    private String USER_DISPLAY_NAME = "displayName";

    private String USER_RECEIVE_MARKETING_EMAIL = "receiveMarketingEmail";

    private String USER_JANRAIN_UUID = "uuid";

    private String USER_EMAIL_VERIFIED = "emailVerified";

    private String USER_CAPTURE = "capture";

    private String CONSUMER_CAMPAIGN_NAME = "campaignName";

    private String CONSUMER_SUBJECT_AREA = "subjectArea";

    private String CONSUMER_TOPIC_COMMUNICATION_KEY = "topicCommunicationKey";

    private String CONSUMER_TOPIC_VALUE = "topicValue";

    private String CONSUMER_INTERESTS = "consumerInterests";

    private String LOG_TAG = "User Registration";

    private String CONSUMER_COUNTRY = "country";

    private String CONSUMER_PREFERED_LANGUAGE = "preferredLanguage";

    private String CONSUMER_PRIMARY_ADDRESS = "primaryAddress";

    private UpdateUserRecordHandler mUpdateUserRecordHandler;


    public User(Context context) {
        mContext = context;
        mUpdateUserRecordHandler = new UpdateUserRecord(mContext);
    }


    // For Traditional SignIn
    public void loginUsingTraditional(final String emailAddress, final String password,
                                      final TraditionalLoginHandler traditionalLoginHandler) {
        if (traditionalLoginHandler == null && emailAddress == null && password == null) {
            throw new RuntimeException("Email , Password , TraditionalLoginHandler can't be null");
        }

        LoginTraditional loginTraditionalResultHandler = new LoginTraditional(
                new TraditionalLoginHandler() {
                    @Override
                    public void onLoginSuccess() {
                        DIUserProfile diUserProfile = getUserInstance();
                        if (diUserProfile != null && traditionalLoginHandler != null) {
                            diUserProfile.setPassword(password);
                            saveDIUserProfileToDisk(diUserProfile);
                            traditionalLoginHandler.onLoginSuccess();
                        } else {
                            if (traditionalLoginHandler != null) {
                                UserRegistrationFailureInfo
                                        userRegistrationFailureInfo =
                                        new UserRegistrationFailureInfo();
                                userRegistrationFailureInfo.
                                        setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
                                traditionalLoginHandler.
                                        onLoginFailedWithError(userRegistrationFailureInfo
                                        );
                            }
                        }
                    }

                    @Override
                    public void onLoginFailedWithError(UserRegistrationFailureInfo
                                                               userRegistrationFailureInfo) {
                        if (traditionalLoginHandler != null) {
                            traditionalLoginHandler.
                                    onLoginFailedWithError(userRegistrationFailureInfo);
                        }
                    }
                }, mContext, mUpdateUserRecordHandler, emailAddress,
                password);

        loginTraditionalResultHandler.loginTraditionally(emailAddress, password);

    }

    // For Social SignIn Using Provider
    public void loginUserUsingSocialProvider(final Activity activity, final String providerName,
                                             final SocialProviderLoginHandler socialLoginHandler, final String mergeToken) {

        if (providerName != null && activity != null) {
            LoginSocialProvider loginSocialResultHandler = new LoginSocialProvider(
                    socialLoginHandler, mContext, mUpdateUserRecordHandler);
            loginSocialResultHandler.loginSocial(activity, providerName, mergeToken);
        } else {
            if (socialLoginHandler != null) {
                UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
                socialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
            }
        }

    }

    // moved app logic to set user info (traditional login) in diuserprofile to
    // framework.
    public void registerUserInfoForTraditional(String mGivenName, String mUserEmail,
                                               String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail,
                                               final TraditionalRegistrationHandler traditionalRegisterHandler) {

        RegisterTraditional registerTraditional = new RegisterTraditional(traditionalRegisterHandler, mContext, mUpdateUserRecordHandler);
        ABCD.getInstance().setmP(password);
        registerTraditional.registerUserInfoForTraditional(mGivenName, mUserEmail,
                password, olderThanAgeLimit, isReceiveMarketingEmail);

    }


    // For Forgot password

    public void forgotPassword(final String emailAddress, final ForgotPasswordHandler forgotPasswordHandler) {
        if (emailAddress != null) {
            ForgotPassword forgotPasswordResultHandler = new ForgotPassword(mContext, forgotPasswordHandler);
            forgotPasswordResultHandler.performForgotPassword(emailAddress);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);

            forgotPasswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
        }
    }

    // For Refresh login Session
    public void refreshLoginSession(final RefreshLoginSessionHandler refreshLoginSessionHandler) {
        RefreshUserSession refreshUserSession = new RefreshUserSession(refreshLoginSessionHandler, mContext);
        refreshUserSession.refreshUserSession();
    }


    // For Resend verification emails
    public void resendVerificationMail(final String emailAddress,
                                       final ResendVerificationEmailHandler resendVerificationEmail) {

        if (emailAddress != null) {
            ResendVerificationEmail resendVerificationEmailHandler = new ResendVerificationEmail(mContext, resendVerificationEmail);
            resendVerificationEmailHandler.resendVerificationMail(emailAddress);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            resendVerificationEmail.onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
        }
    }


    private void mergeTraditionalAccount(final String emailAddress, final String password, final String mergeToken,
                                         final TraditionalLoginHandler traditionalLoginHandler) {
        if (emailAddress != null && password != null) {
            LoginTraditional loginTraditionalResultHandler = new LoginTraditional(
                    traditionalLoginHandler, mContext, mUpdateUserRecordHandler, emailAddress,
                    password);
            loginTraditionalResultHandler.mergeTraditionally(emailAddress, password, mergeToken);

        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);

            traditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
        }

    }

    // For handling merge scenario
    public void mergeToTraditionalAccount(final String emailAddress, final String password, final String mergeToken,
                                          final TraditionalLoginHandler traditionalLoginHandler) {
        mergeTraditionalAccount(emailAddress, password, mergeToken, traditionalLoginHandler);


    }

    // moved app logic to set user info ( social sign in ) in diuserprofile to
    // framework.
    public void registerUserInfoForSocial(final String givenName, final String displayName, final String familyName,
                                          final String userEmail, final boolean olderThanAgeLimit, final boolean isReceiveMarketingEmail,
                                          final SocialProviderLoginHandler socialProviderLoginHandler, final String socialRegistrationToken) {

        if (socialProviderLoginHandler != null) {
            RegisterSocial registerSocial = new RegisterSocial(socialProviderLoginHandler, mContext, mUpdateUserRecordHandler);
            registerSocial.registerUserForSocial(givenName, displayName, familyName, userEmail, olderThanAgeLimit, isReceiveMarketingEmail, socialRegistrationToken);
        }

    }


    // For getting values from Captured and Saved Json object
    private DIUserProfile getUserInstance() {
        CaptureRecord captureRecord = Jump.getSignedInUser();
        if (captureRecord == null) {
            captureRecord = CaptureRecord.loadFromDisk(mContext);
        }
        if (captureRecord == null) {
            return null;
        }

        DIUserProfile diUserProfile = new DIUserProfile();
        HsdpUser hsdpUser = new HsdpUser(mContext);
        HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
        if (hsdpUserRecord != null) {
            diUserProfile.setHsdpUUID(hsdpUserRecord.getUserUUID());
            diUserProfile.setHsdpAccessToken(hsdpUserRecord.getAccessCredential().getAccessToken());
        }

        try {
            diUserProfile.setEmail(captureRecord.getString(USER_EMAIL));
            diUserProfile.setGivenName(captureRecord.getString(USER_GIVEN_NAME));
            diUserProfile.setDisplayName(captureRecord.getString(USER_DISPLAY_NAME));
            diUserProfile
                    .setReceiveMarketingEmail(captureRecord.getBoolean(USER_RECEIVE_MARKETING_EMAIL));
            diUserProfile.setJanrainUUID(captureRecord.getString(USER_JANRAIN_UUID));
            JSONObject userAddress = new JSONObject(captureRecord.getString(CONSUMER_PRIMARY_ADDRESS));
            diUserProfile.setCountryCode(userAddress.getString(CONSUMER_COUNTRY));
            diUserProfile.setLanguageCode(captureRecord.getString(CONSUMER_PREFERED_LANGUAGE));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "On getUserInstance,Caught JSON Exception");
        }
        return diUserProfile;
    }

    // For checking email verification
    public boolean getEmailVerificationStatus() {
        mEmailVerified = false;
        CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);

        if (captured == null)
            return false;
        try {
            JSONObject mObject = new JSONObject(captured.toString());
            if (mObject.isNull(USER_EMAIL_VERIFIED)) {
                mEmailVerified = false;
            } else {
                mEmailVerified = true;
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "On getEmailVerificationStatus,Caught JSON Exception");
        }
        return mEmailVerified;
    }


    public boolean isUserSignIn() {
        CaptureRecord capturedRecord = Jump.getSignedInUser();
        if (capturedRecord == null) {
            capturedRecord = CaptureRecord.loadFromDisk(mContext);
        }
        if (capturedRecord == null) {
            return false;
        }

        boolean signedIn = true;
        if (RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
            signedIn = signedIn && !capturedRecord.isNull(USER_EMAIL_VERIFIED);
        }
        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
            if (!RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
                throw new RuntimeException("Please set emailVerificationRequired field as true");
            }
            HsdpUser hsdpUser = new HsdpUser(mContext);
            signedIn = signedIn && hsdpUser.isHsdpUserSignedIn();
        }
        if (RegistrationConfiguration.getInstance().getJanRainConfiguration() != null) {
            signedIn = signedIn && capturedRecord.getAccessToken() != null;
        }

        if (RegistrationConfiguration.getInstance().getFlow().isTermsAndConditionsAcceptanceRequired()) {
            boolean isTermAccepted = RegPreferenceUtility.getStoredState(mContext, getEmail());
            if (!isTermAccepted) {
                signedIn = false;
                clearData();
            }
        }

        return signedIn;
    }

    private boolean isJanrainUserRecord() {
        CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);
        if (captured != null) {
            return true;
        }
        return false;
    }

    // check merge flow error for capture
    public boolean handleMergeFlowError(String existingProvider) {
        if (existingProvider.equals(USER_CAPTURE)) {
            return true;
        }
        return false;
    }

    private void refreshReceiveMarketignEmail(final UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmail,
                                              final boolean receiveMarketingEmail) {
        final User user = new User(mContext);
        user.refreshLoginSession(new RefreshLoginSessionHandler() {

            @Override
            public void onRefreshLoginSessionSuccess() {
                updateMarketingEmailAfterRefreshAccessToken(updateReceiveMarketingEmail,
                        receiveMarketingEmail);
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                if (error == Integer.parseInt(RegConstants.INVALID_ACCESS_TOKEN_CODE)
                        || error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                    clearData();
                    RegistrationHelper.getInstance().getUserRegistrationListener()
                            .notifyOnLogoutSuccessWithInvalidAccessToken();
                }
                updateReceiveMarketingEmail.onUpdateReceiveMarketingEmailFailedWithError(error);
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {

            }
        });

    }

    // For update receive marketing email
    public void updateReceiveMarketingEmail(
            final UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmail,
            final boolean receiveMarketingEmail) {

        refreshReceiveMarketignEmail(updateReceiveMarketingEmail, receiveMarketingEmail);

    }

    private void updateMarketingEmailAfterRefreshAccessToken(
            UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmail,
            boolean receiveMarketingEmail) {

        UpdateReceiveMarketingEmail updateReceiveMarketingEmailHandler = new UpdateReceiveMarketingEmail(updateReceiveMarketingEmail, mContext, receiveMarketingEmail);
        updateReceiveMarketingEmailHandler.updateMarketingEmailStatus(receiveMarketingEmail);

    }

    // For updating consumer interests
    public void addConsumerInterest(AddConsumerInterestHandler addConsumerInterestHandler,
                                    ConsumerArray consumerArray) {

        AddConsumerInterest addConsumerInterest = new AddConsumerInterest(
                addConsumerInterestHandler);
        CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);
        JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
        mConsumerInterestArray = new JSONArray();
        ConsumerArray consumer = ConsumerArray.getInstance();

        if (consumer != null) {
            for (ConsumerInterest diConsumerInterest : consumer.getConsumerArraylist()) {
                try {

                    mConsumerInterestObject = new JSONObject();
                    mConsumerInterestObject.put(CONSUMER_CAMPAIGN_NAME,
                            diConsumerInterest.getCampaignName());
                    mConsumerInterestObject.put(CONSUMER_SUBJECT_AREA,
                            diConsumerInterest.getSubjectArea());
                    mConsumerInterestObject.put(CONSUMER_TOPIC_COMMUNICATION_KEY,
                            diConsumerInterest.getTopicCommunicationKey());
                    mConsumerInterestObject.put(CONSUMER_TOPIC_VALUE,
                            diConsumerInterest.getTopicValue());

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "On addConsumerInterest,Caught JSON Exception");
                }
                mConsumerInterestArray.put(mConsumerInterestObject);
            }
        }

        if (captured != null) {
            try {
                captured.remove(CONSUMER_INTERESTS);
                captured.put(CONSUMER_INTERESTS, mConsumerInterestArray);
                try {
                    captured.synchronize(addConsumerInterest, originalUserInfo);

                } catch (InvalidApidChangeException e) {

                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // For Log out
    public void logout(LogoutHandler logoutHandler) {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow() && null != hsdpUser.getHsdpUserRecord()) {
            logoutHsdp(logoutHandler);
        } else {
            clearData();
            if (logoutHandler != null) {

                RegistrationHelper.getInstance().getUserRegistrationListener()
                        .notifyOnUserLogoutSuccess();
                logoutHandler.onLogoutSuccess();
            }
        }
    }

    // For getting access token
    public String getAccessToken() {

        CaptureRecord captureRecord = CaptureRecord.loadFromDisk(mContext);

        if (captureRecord == null) {
            return null;
        }
        return captureRecord.getAccessToken();
    }

    private void refreshandUpdateUser(final RefreshUserHandler handler) {

        if (Jump.getSignedInUser() == null) {
            handler.onRefreshUserFailed(0);
            return;
        }
        Jump.performFetchCaptureData(new CaptureApiResultHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Jump.saveToDisk(mContext);
                if (!RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
                    handler.onRefreshUserSuccess();
                    return;
                }

                if (getEmailVerificationStatus()) {
                    DIUserProfile userProfile = getDIUserProfileFromDisk();
                    HsdpUser hsdpUser = new HsdpUser(mContext);
                    HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
                    if (userProfile != null && null != userProfile.getEmail() && null != ABCD.getInstance().getmP() && hsdpUserRecord == null) {
                        LoginTraditional loginTraditional = new LoginTraditional(new TraditionalLoginHandler() {
                            @Override
                            public void onLoginSuccess() {
                                ABCD.getInstance().setmP(null);
                                handler.onRefreshUserSuccess();
                            }

                            @Override
                            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                                handler.onRefreshUserFailed(RegConstants.HSDP_ACTIVATE_ACCOUNT_FAILED);
                            }
                        }, mContext, mUpdateUserRecordHandler, userProfile.getEmail(), ABCD.getInstance().getmP());
                        loginTraditional.loginIntoHsdp();
                    } else {
                        handler.onRefreshUserSuccess();
                    }
                } else {
                    handler.onRefreshUserSuccess();
                }
            }

            @Override
            public void onFailure(CaptureAPIError failureParam) {

                System.out.println("Error " + failureParam.captureApiError);
                System.out.println("Error code" + failureParam.captureApiError.code);
                System.out.println("Error error " + failureParam.captureApiError.error);

                if (failureParam.captureApiError.code == 414 && failureParam.captureApiError.error.equalsIgnoreCase("access_token_expired")) {
                    //refresh login session

                    refreshLoginSession(new RefreshLoginSessionHandler() {
                        @Override
                        public void onRefreshLoginSessionSuccess() {
                            handler.onRefreshUserSuccess();
                            return;
                        }

                        @Override
                        public void onRefreshLoginSessionFailedWithError(int error) {
                            handler.onRefreshUserFailed(error);
                            return;
                        }

                        @Override
                        public void onRefreshLoginSessionInProgress(String message) {

                        }
                    });
                }
                handler.onRefreshUserFailed(0);
            }
        });
    }

    /**
     * Refresh User object and align with Server
     *
     * @param handler Callback mHandler
     */
    public void refreshUser(final RefreshUserHandler handler) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            new RefreshandUpdateUserHandler(mUpdateUserRecordHandler, mContext).refreshAndUpdateUser(handler, this, ABCD.getInstance().getmP());
            //ABCD.getInstance().setmP(null);
        } else {
            handler.onRefreshUserFailed(-1);
        }
        //refreshandUpdateUser(handler);
    }

    private void logoutHsdp(final LogoutHandler logoutHandler) {
        final HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.logOut(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                clearData();
                if (logoutHandler != null) {
                    logoutHandler.onLogoutSuccess();
                    RegistrationHelper.getInstance().getUserRegistrationListener()
                            .notifyOnUserLogoutSuccess();
                }
            }

            @Override
            public void onLogoutFailure(int responseCode, String message) {

                if (responseCode == Integer.parseInt(RegConstants.INVALID_ACCESS_TOKEN_CODE)
                        || responseCode == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                    clearData();
                    if (logoutHandler != null) {
                        logoutHandler.onLogoutSuccess();
                        RegistrationHelper.getInstance().getUserRegistrationListener()
                                .notifyOnLogoutSuccessWithInvalidAccessToken();
                    }
                    return;
                } else {
                    if (logoutHandler != null) {
                        logoutHandler.onLogoutFailure(responseCode, message);
                        RegistrationHelper.getInstance().getUserRegistrationListener()
                                .notifyOnUserLogoutFailure();
                    }
                }
            }
        });
    }


    public String getEmail() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getEmail();
    }


    public String getPassword() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getPassword();
    }


    public String getGivenName() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getGivenName();
    }


    public boolean getOlderThanAgeLimit() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return false;
        }
        return diUserProfile.getOlderThanAgeLimit();
    }


    public boolean getReceiveMarketingEmail() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return false;
        }
        return diUserProfile.getReceiveMarketingEmail();
    }


    public String getDisplayName() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getDisplayName();
    }


    public String getFamilyName() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getFamilyName();
    }


    public String getJanrainUUID() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getJanrainUUID();
    }


    public String getHsdpUUID() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getHsdpUUID();

    }


    public String getHsdpAccessToken() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getHsdpAccessToken();
    }


    public String getLanguageCode() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getLanguageCode();
    }


    public String getCountryCode() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getCountryCode();
    }


    private void saveDIUserProfileToDisk(DIUserProfile diUserProfile) {
        try {
            diUserProfile.setPassword(null);
            FileOutputStream fos = mContext.openFileOutput(RegConstants.DI_PROFILE_FILE, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            String objectPlainString = SecureStorage.objectToString(diUserProfile);
            byte[] ectext = SecureStorage.encrypt(objectPlainString);
            oos.writeObject(ectext);
            oos.close();
            fos.close();
        } catch (Exception e) {
            RLog.e("Exception : ",e.getMessage());
            e.printStackTrace();
        }
    }


    private DIUserProfile getDIUserProfileFromDisk() {
        DIUserProfile diUserProfile = null;
        try {
            FileInputStream fis = mContext.openFileInput(RegConstants.DI_PROFILE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] enctText = (byte[]) ois.readObject();
            byte[] decrtext = SecureStorage.decrypt(enctText);
            diUserProfile = (DIUserProfile) SecureStorage.stringToObject(new String(decrtext));
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diUserProfile;
    }


    private void clearData() {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.deleteFromDisk();
        mContext.deleteFile(RegConstants.DI_PROFILE_FILE);
        if (JRSession.getInstance() != null) {
            JRSession.getInstance().signOutAllAuthenticatedUsers();
        }
        Jump.signOutCaptureUser(mContext);

    }

}
