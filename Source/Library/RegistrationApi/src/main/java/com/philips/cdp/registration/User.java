/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.janrain.android.Jump;
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
import com.philips.cdp.registration.controller.UpdateDateOfBirth;
import com.philips.cdp.registration.controller.UpdateGender;
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
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.security.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@code User} class represents information related to a logged in user of User Registration component.
 * Additionally, it exposes APIs to login, logout and refresh operations for traditional and social accounts.
 */
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

    /**
     * @param context
     */
    public User(Context context) {
        mContext = context;
        mUpdateUserRecordHandler = new UpdateUserRecord(mContext);
    }


    /**
     * {@code loginUsingTraditional} method logs in a user with a traditional account.
     *
     * @param emailAddress
     * @param password
     * @param traditionalLoginHandler
     */
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


    /**
     * {@code loginUserUsingSocialProvider} logs in a user via a social login provider
     *
     * @param activity
     * @param providerName
     * @param socialLoginHandler
     * @param mergeToken
     */
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

    /**
     * {@code registerUserInfoForTraditional} method creates a user account.
     *
     * @param mGivenName
     * @param mUserEmail
     * @param password
     * @param olderThanAgeLimit
     * @param isReceiveMarketingEmail
     * @param traditionalRegisterHandler
     */
    public void registerUserInfoForTraditional(String mGivenName, String mUserEmail,
                                               String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail,
                                               final TraditionalRegistrationHandler traditionalRegisterHandler) {

        RegisterTraditional registerTraditional = new RegisterTraditional(traditionalRegisterHandler, mContext, mUpdateUserRecordHandler);
        ABCD.getInstance().setmP(password);
        registerTraditional.registerUserInfoForTraditional(mGivenName, mUserEmail,
                password, olderThanAgeLimit, isReceiveMarketingEmail);

    }


    /**
     * {@code forgotPassword} method retrieves a lost password.
     *
     * @param emailAddress
     * @param forgotPasswordHandler
     */
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

    /**
     * {@code refreshLoginSession} method refreshes the session of an already logged in user.
     *
     * @param refreshLoginSessionHandler
     */
    public void refreshLoginSession(final RefreshLoginSessionHandler refreshLoginSessionHandler) {
        RefreshUserSession refreshUserSession = new RefreshUserSession(refreshLoginSessionHandler, mContext);
        refreshUserSession.refreshUserSession();
    }


    /**
     * {@code resendVerificationEmail} method sends a verification mail in case an already sent mail is not received.
     *
     * @param emailAddress
     * @param resendVerificationEmail
     */
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

    /**
     * {@code mergeToTraditionalAccount} method merges a traditional account to other existing account
     *
     * @param emailAddress
     * @param password
     * @param mergeToken
     * @param traditionalLoginHandler
     */
    public void mergeToTraditionalAccount(final String emailAddress, final String password, final String mergeToken,
                                          final TraditionalLoginHandler traditionalLoginHandler) {
        mergeTraditionalAccount(emailAddress, password, mergeToken, traditionalLoginHandler);


    }

    /**
     * {@code registerUserInfoForSocial} methods creates a new account using social provider.
     *
     * @param givenName
     * @param displayName
     * @param familyName
     * @param userEmail
     * @param olderThanAgeLimit
     * @param isReceiveMarketingEmail
     * @param socialProviderLoginHandler
     * @param socialRegistrationToken
     */
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
        CaptureRecord captureRecord = null;
        captureRecord = CaptureRecord.loadFromDisk(mContext);
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
            String gender = captureRecord.getString(UpdateGender.USER_GENDER);
            if (null != gender) {
                if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
                    diUserProfile.setGender(Gender.MALE);
                } else {
                    diUserProfile.setGender(Gender.FEMALE);
                }
            }

            String dob = captureRecord.getString(UpdateDateOfBirth.USER_DATE_OF_BIRTH);
            if(null!= dob){
                DateFormat formatter = new SimpleDateFormat(UpdateDateOfBirth.DATE_FORMAT_FOR_DOB, Locale.ROOT);
                Date date = formatter.parse(dob);
                diUserProfile.setDateOfBirth(date);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "On getUserInstance,Caught JSON Exception");
        } catch (ParseException e) {
            e.printStackTrace();
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

    /**
     * {@code isUserSignIn} method checks if a user is logged in
     *
     * @return boolean
     */
    public boolean isUserSignIn() {
        CaptureRecord capturedRecord = Jump.getSignedInUser();
        if (capturedRecord == null) {
            capturedRecord = CaptureRecord.loadFromDisk(mContext);
        }
        if (capturedRecord == null) {
            return false;
        }

        boolean signedIn = true;
        if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
            signedIn = signedIn && !capturedRecord.isNull(USER_EMAIL_VERIFIED);
        }
        if (RegistrationConfiguration.getInstance().isHsdpFlow()) {
            if (!RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
                throw new RuntimeException("Please set emailVerificationRequired field as true");
            }
            HsdpUser hsdpUser = new HsdpUser(mContext);
            signedIn = signedIn && hsdpUser.isHsdpUserSignedIn();
        }
        if (RegistrationConfiguration.getInstance().getRegistrationClientId(RegUtility.getConfiguration(
                RegistrationConfiguration.getInstance().getRegistrationEnvironment())) != null) {
            signedIn = signedIn && capturedRecord.getAccessToken() != null;
        }

        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            boolean isTermAccepted = RegPreferenceUtility.getStoredState(mContext, getEmail());
            if (!isTermAccepted) {
                signedIn = false;
                clearData();
            }
        }

        return signedIn;
    }

//    private boolean isJanrainUserRecord() {
//        CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);
//        if (captured != null) {
//            return true;
//        }
//        return false;
//    }

    // check merge flow error for capture
    public boolean handleMergeFlowError(String existingProvider) {
        if (existingProvider.equals(USER_CAPTURE)) {
            return true;
        }
        return false;
    }


    // For update receive marketing email
    public void updateReceiveMarketingEmail(
            final UpdateUserDetailsHandler updateReceiveMarketingEmail,
            final boolean receiveMarketingEmail) {
        UpdateReceiveMarketingEmail updateReceiveMarketingEmailHandler = new
                UpdateReceiveMarketingEmail(
                mContext);
        updateReceiveMarketingEmailHandler.
                updateMarketingEmailStatus(updateReceiveMarketingEmail, receiveMarketingEmail);
    }

    /**
     * Update Date of bith of user.
     *
     * @param updateUserDetailsHandler
     * @param date
     */
    public void updateDateOfBirth(
            final UpdateUserDetailsHandler updateUserDetailsHandler,
            final Date date) {
        UpdateDateOfBirth updateDateOfBirth = new UpdateDateOfBirth(mContext);
        updateDateOfBirth.updateDateOfBirth(updateUserDetailsHandler, date);
    }


    /**
     * Update Date of bith of user.
     *
     * @param updateUserDetailsHandler
     * @param gender
     */
    public void updateGender(
            final UpdateUserDetailsHandler updateUserDetailsHandler,
            final Gender gender) {
        UpdateGender updateGender = new UpdateGender(mContext);
        updateGender.updateGender(updateUserDetailsHandler, gender);
    }


    // For updating consumer interests
    public void addConsumerInterest(AddConsumerInterestHandler addConsumerInterestHandler,
                                    ConsumerArray consumerArray) {

        AddConsumerInterest addConsumerInterest = new AddConsumerInterest(
                addConsumerInterestHandler);
        CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);
        JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
        mConsumerInterestArray = new JSONArray();
        ConsumerArray consumer = consumerArray;

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

    /**
     * {@code logout} method logs out a logged in user.
     *
     * @param logoutHandler
     */
    public void logout(LogoutHandler logoutHandler) {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        if (RegistrationConfiguration.getInstance().isHsdpFlow() && null != hsdpUser.getHsdpUserRecord()) {
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

    /**
     * {@code getEmail} method returns the email address of a logged in user.
     *
     * @return String
     */
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

    /**
     * {@code getGivenName} method returns the given name of a logged in user.
     *
     * @return String
     */
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

    /**
     * {@code getReceiveMarketingEmail} method checks if the user has subscribed to receive marketing email.
     *
     * @return boolean
     */
    public boolean getReceiveMarketingEmail() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return false;
        }
        return diUserProfile.getReceiveMarketingEmail();
    }


    /**
     * Get Date of birth
     *
     * @return Date object
     */
    public Date getDateOfBirth() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getDateOfBirth();
    }

    /**
     * Get Date of birth
     *
     * @return Date object
     */
    public Gender getGender() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getGender();
    }


    /**
     * {@code getGivenName} method returns the display name of a logged in user.
     *
     * @return String
     */
    public String getDisplayName() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getDisplayName();
    }

    /**
     * {@code getFamilyName} method returns the family name of a logged in user.
     *
     * @return String
     */
    public String getFamilyName() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getFamilyName();
    }

    /**
     * {@code getJanrainUUID} method returns the Janrain UUID of a logged in user.
     *
     * @return String
     */
    public String getJanrainUUID() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getJanrainUUID();
    }

    /**
     * {@code getHsdpUUID} method returns the HSDP UUID of a logged in user.
     *
     * @return String
     */
    public String getHsdpUUID() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getHsdpUUID();

    }

    /**
     * {@code getHsdpAccessToken} method returns the access token for a logged in user.
     *
     * @return String
     */
    public String getHsdpAccessToken() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getHsdpAccessToken();
    }

    /**
     * {@code getLanguageCode} method returns the language code for a logged in user
     *
     * @return String
     */
    public String getLanguageCode() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getLanguageCode();
    }

    /**
     * {@code getCountryCode} method returns country code for a logged in user.
     *
     * @return String
     */
    public String getCountryCode() {
        DIUserProfile diUserProfile = getUserInstance();
        if (diUserProfile == null) {
            return null;
        }
        return diUserProfile.getCountryCode();
    }


    private void saveDIUserProfileToDisk(DIUserProfile diUserProfile) {
        diUserProfile.setPassword(null);
        String objectPlainString = SecureStorage.objectToString(diUserProfile);
        try {
            mContext.deleteFile(RegConstants.DI_PROFILE_FILE);
            Jump.getSecureStorageInterface().storeValueForKey(RegConstants.DI_PROFILE_FILE,
                    new String(objectPlainString) ,new SecureStorageInterface.SecureStorageError());
        } catch (Exception e) {
            RLog.d("Exception :", e.getMessage());
        }
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

    public void registerUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        RegistrationHelper.getInstance().registerUserRegistrationListener(userRegistrationListener);
    }

    public void unRegisterUserRegistrationListener(UserRegistrationListener
                                                           userRegistrationListener) {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(
                userRegistrationListener);
    }

}
