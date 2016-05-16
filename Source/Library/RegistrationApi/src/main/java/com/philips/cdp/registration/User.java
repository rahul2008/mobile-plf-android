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
import com.philips.cdp.registration.controller.ContinueSocialProviderLogin;
import com.philips.cdp.registration.controller.ForgotPassword;
import com.philips.cdp.registration.controller.LoginSocialProvider;
import com.philips.cdp.registration.controller.LoginTraditional;
import com.philips.cdp.registration.controller.RefreshLoginSession;
import com.philips.cdp.registration.controller.RegisterTraditional;
import com.philips.cdp.registration.controller.ResendVerificationEmail;
import com.philips.cdp.registration.controller.UpdateReceiveMarketingEmail;
import com.philips.cdp.registration.controller.UpdateUserRecord;
import com.philips.cdp.registration.coppa.CoppaConfiguration;
import com.philips.cdp.registration.dao.ConsumerArray;
import com.philips.cdp.registration.dao.ConsumerInterest;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.security.SecureStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledExecutorService;

public class User {

    private String mEmail, mGivenName, mPassword, mDisplayName;

    private boolean mOlderThanAgeLimit, mReceiveMarketingEmails, mEmailVerified;

    private Context mContext;

    private JSONObject mConsumerInterestObject;

    private JSONArray mConsumerInterestArray;

    private CaptureRecord mCapturedData;

    private String USER_EMAIL = "email";

    private String USER_GIVEN_NAME = "givenName";

    private String USER_FAMILY_NAME = "familyName";

    private String USER_PASSWORD = "password";

    private String USER_DISPLAY_NAME = "displayName";

    private String USER_OLDER_THAN_AGE_LIMIT = "olderThanAgeLimit";

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

    private UserRegistrationInitializer mRegistrationHelper;

    private ScheduledExecutorService mScheduledExecutorService;

    public User(Context context) {
        mContext = context;
        mUpdateUserRecordHandler = new UpdateUserRecord(context);
        mRegistrationHelper = UserRegistrationInitializer.getInstance();
    }

    private void loginTraditionally(final String emailAddress, final String password,
                                    final TraditionalLoginHandler traditionalLoginHandler) {
        if (traditionalLoginHandler == null && emailAddress == null && password == null) {
            throw new RuntimeException("Email , Password , TraditionalLoginHandler can't be null");
        }

        LoginTraditional loginTraditionalResultHandler = new LoginTraditional(
                new TraditionalLoginHandler() {
                    @Override
                    public void onLoginSuccess() {

                        DIUserProfile diUserProfile = getUserInstance(mContext);
                        diUserProfile.setPassword(password);
                        saveDIUserProfileToDisk(diUserProfile);
                        traditionalLoginHandler.onLoginSuccess();
                    }

                    @Override
                    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                        traditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                    }
                }, mContext, mUpdateUserRecordHandler, emailAddress,
                password);
        Jump.performTraditionalSignIn(emailAddress, password, loginTraditionalResultHandler,
                null);


    }

    private boolean isJumpInitializated() {

        return !mRegistrationHelper.isJumpInitializationInProgress() && mRegistrationHelper.isJanrainIntialized();
    }

    private boolean isJumpInitializationInProgress() {
        return mRegistrationHelper.isJumpInitializationInProgress() && !mRegistrationHelper.isJanrainIntialized();

    }

    // For Traditional SignIn
    public void loginUsingTraditional(final String emailAddress, final String password,
                                      final TraditionalLoginHandler traditionalLoginHandler) {

        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, now performing traditional login");
                if (traditionalLoginHandler != null) {
                    loginTraditionally(emailAddress, password, traditionalLoginHandler);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (traditionalLoginHandler != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.TRADITIONAL_LOGIN_FAILED_SERVER_ERROR);
                    traditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });

        if (isJumpInitializated()) {
            loginTraditionally(emailAddress, password, traditionalLoginHandler);
            mRegistrationHelper.unregisterJumpFlowDownloadListener();
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }
    }

    private void loginIntoHsdp(final String emailAddress, String password, final TraditionalLoginHandler traditionalLoginHandler) {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
        if (hsdpUserRecord == null && getEmailVerificationStatus(mContext)) {
            hsdpUser.login(emailAddress, password, new TraditionalLoginHandler() {
                @Override
                public void onLoginSuccess() {
                    traditionalLoginHandler.onLoginSuccess();

                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    traditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
            });
        }
    }

    private void loginUserWithSocialProvider(final Activity activity, final String providerName,
                                             final SocialProviderLoginHandler socialLoginHandler, final String mergeToken) {
        if (providerName != null && activity != null) {
            LoginSocialProvider loginSocialResultHandler = new LoginSocialProvider(
                    socialLoginHandler, mContext, mUpdateUserRecordHandler);
            Jump.showSignInDialog(activity, providerName, loginSocialResultHandler, mergeToken);
        } else {
            if (socialLoginHandler != null) {
                UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
                socialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
            }
        }
    }

    // For Social SignIn Using Provider
    public void loginUserUsingSocialProvider(final Activity activity, final String providerName,
                                             final SocialProviderLoginHandler socialLoginHandler, final String mergeToken) {
        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, now performing social login");
                if (socialLoginHandler != null) {
                    loginUserWithSocialProvider(activity, providerName, socialLoginHandler, mergeToken);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();

            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (socialLoginHandler != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.SOCIAL_LOGIN_FAILED_SERVER_ERROR);
                    socialLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });
        if (isJumpInitializated()) {
            RLog.i(LOG_TAG, "Jump  initialized ");
            if (socialLoginHandler != null) {
                loginUserWithSocialProvider(activity, providerName, socialLoginHandler, mergeToken);
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
            return;
        } else if (!isJumpInitializationInProgress()) {
            RLog.i(LOG_TAG, "Jump  not initialized, initializing again ");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }
    }

    // moved app logic to set user info (traditional login) in diuserprofile to
    // framework.
    public void registerUserInfoForTraditional(String mGivenName, String mUserEmail,
                                               String password, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail,
                                               final TraditionalRegistrationHandler traditionalRegisterHandler) {

        final DIUserProfile profile = new DIUserProfile();
        profile.setGivenName(mGivenName);
        profile.setEmail(mUserEmail);
        profile.setPassword(password);
        profile.setOlderThanAgeLimit(olderThanAgeLimit);
        profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
        ABCD.getInstance().setmP(password);
        final TraditionalRegistrationHandler traditionalRegistrationHandler = new TraditionalRegistrationHandler() {
            @Override
            public void onRegisterSuccess() {
                saveDIUserProfileToDisk(profile);
                traditionalRegisterHandler.onRegisterSuccess();
            }

            @Override
            public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                traditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
            }
        };

        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                if (traditionalRegistrationHandler != null) {
                    RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, registering user");
                    registerNewUserUsingTraditional(profile, traditionalRegistrationHandler);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (traditionalRegistrationHandler != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.REGISTER_TRADITIONAL_FAILED_SERVER_ERROR);
                    traditionalRegistrationHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
                }

            }
        });

        if (isJumpInitializated()) {
            if (traditionalRegisterHandler != null) {
                RLog.i(LOG_TAG, "Jump initialized, registering");
                registerNewUserUsingTraditional(profile, traditionalRegistrationHandler);
            }
            return;

        } else if (!isJumpInitializationInProgress()) {
            RLog.i(LOG_TAG, "Jump not initialized, initializing");
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }

    }

    // For Traditional Registration
    public void registerNewUserUsingTraditional(DIUserProfile diUserProfile,
                                                TraditionalRegistrationHandler traditionalRegisterHandler) {

        if (diUserProfile != null) {

            mEmail = diUserProfile.getEmail();
            mGivenName = diUserProfile.getGivenName();
            mPassword = diUserProfile.getPassword();
            mOlderThanAgeLimit = diUserProfile.getOlderThanAgeLimit();
            mReceiveMarketingEmails = diUserProfile.getReceiveMarketingEmail();

            JSONObject newUser = new JSONObject();
            try {
                newUser.put(USER_EMAIL, mEmail).put(USER_GIVEN_NAME, mGivenName)
                        .put(USER_PASSWORD, mPassword)
                        .put(USER_OLDER_THAN_AGE_LIMIT, mOlderThanAgeLimit)
                        .put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmails);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "On registerNewUserUsingTraditional,Caught JSON Exception");
            }
            RegisterTraditional traditionalRegisterResultHandler = new RegisterTraditional(
                    traditionalRegisterHandler, mContext, mUpdateUserRecordHandler);

            Jump.registerNewUser(newUser, null, traditionalRegisterResultHandler);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
            userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
            traditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
        }
    }

    // For Forgot password

    private void performForgotPassword(String emailAddress, ForgotPasswordHandler forgotPasswordHandler) {
        if (emailAddress != null) {
            ForgotPassword forgotPasswordResultHandler = new ForgotPassword(forgotPasswordHandler);
            Jump.performForgotPassword(emailAddress, forgotPasswordResultHandler);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);

            forgotPasswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
        }

    }

    public void forgotPassword(final String emailAddress, final ForgotPasswordHandler forgotPasswordHandler) {
        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, now performing forgot password");
                if (forgotPasswordHandler != null) {
                    performForgotPassword(emailAddress, forgotPasswordHandler);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (forgotPasswordHandler != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.FORGOT_PASSWORD_FAILED_SERVER_ERROR);
                    forgotPasswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });
        if (isJumpInitializated()) {
            if (forgotPasswordHandler != null) {
                performForgotPassword(emailAddress, forgotPasswordHandler);
            }
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }


    }

    // For Refresh login Session
    public void refreshLoginSession(final RefreshLoginSessionHandler refreshLoginSessionHandler, final Context context) {


        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, now performing forgot password");
                refreshSession(refreshLoginSessionHandler, context);
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(-1);
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });
        if (isJumpInitializated()) {
            refreshSession(refreshLoginSessionHandler, context);
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }


    }

    protected void refreshSession(final RefreshLoginSessionHandler refreshLoginSessionHandler, final Context context) {
        CaptureRecord captureRecord = CaptureRecord.loadFromDisk(mContext);
        if (captureRecord == null) {
            return;
        }
        captureRecord.refreshAccessToken(new RefreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
                    // refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
                    refreshHsdpAccessToken(context, refreshLoginSessionHandler);
                    return;
                }
                refreshLoginSessionHandler.onRefreshLoginSessionSuccess();

            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error);

            }
        }), context);
    }

    private void refreshHsdpAccessToken(Context context, final RefreshLoginSessionHandler refreshLoginSessionHandler) {
        final HsdpUser hsdpUser = new HsdpUser(context);
        hsdpUser.refreshToken(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                if (error == Integer.parseInt(RegConstants.INVALID_ACCESS_TOKEN_CODE)
                        || error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
                    clearData();
                    RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnLogoutSuccessWithInvalidAccessToken();
                }
                refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(error);
            }
        });
    }

    private void resendMail(final String emailAddress, final ResendVerificationEmailHandler resendVerificationEmail) {
        if (emailAddress != null) {
            ResendVerificationEmail resendVerificationEmailHandler = new ResendVerificationEmail(
                    resendVerificationEmail);
            Jump.resendEmailVerification(emailAddress, resendVerificationEmailHandler);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);

            resendVerificationEmail
                    .onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
        }

    }

    // For Resend verification emails
    public void resendVerificationMail(final String emailAddress,
                                       final ResendVerificationEmailHandler resendVerificationEmail) {


        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                if (resendVerificationEmail != null) {
                    RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier, resending mail now");
                    resendMail(emailAddress, resendVerificationEmail);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (resendVerificationEmail != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.RESEND_MAIL_FAILED_SERVER_ERROR);
                    resendVerificationEmail.onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });

        if (isJumpInitializated()) {
            if (resendVerificationEmail != null) {
                resendMail(emailAddress, resendVerificationEmail);
            }
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }

    }


    private void mergeTraditionalAccount(final String emailAddress, final String password, final String mergeToken,
                                         final TraditionalLoginHandler traditionalLoginHandler) {
        if (emailAddress != null && password != null) {
            LoginTraditional loginTraditionalResultHandler = new LoginTraditional(
                    traditionalLoginHandler, mContext, mUpdateUserRecordHandler, emailAddress,
                    password);
            Jump.performTraditionalSignIn(emailAddress, password, loginTraditionalResultHandler,
                    mergeToken);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);

            traditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
        }

    }

    // For handling merge scenario
    public void mergeToTraditionalAccount(final String emailAddress, final String password, final String mergeToken,
                                          final TraditionalLoginHandler traditionalLoginHandler) {

        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                if (traditionalLoginHandler != null) {
                    RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier");
                    mergeTraditionalAccount(emailAddress, password, mergeToken, traditionalLoginHandler);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (traditionalLoginHandler != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.MERGE_TRADITIONAL_FAILED_SERVER_ERROR);
                    traditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });
        if (isJumpInitializated()) {
            if (traditionalLoginHandler != null) {
                mergeTraditionalAccount(emailAddress, password, mergeToken, traditionalLoginHandler);
            }
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }

    }

    // moved app logic to set user info ( social sign in ) in diuserprofile to
    // framework.
    public void registerUserInfoForSocial(final String givenName, final String displayName, final String familyName,
                                          final String userEmail, final boolean olderThanAgeLimit, final boolean isReceiveMarketingEmail,
                                          final SocialProviderLoginHandler socialProviderLoginHandler, final String socialRegistrationToken) {


        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                if (socialProviderLoginHandler != null) {
                    RLog.i(LOG_TAG, "Jump  initialized now after coming to this screen,  was in progress earlier");
                    registerUserForSocial(givenName, displayName, familyName, userEmail, olderThanAgeLimit, isReceiveMarketingEmail, socialProviderLoginHandler, socialRegistrationToken);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }

            @Override
            public void onFlowDownloadFailure() {
                RLog.i(LOG_TAG, "Jump not initialized, was initialized but failed");
                if (socialProviderLoginHandler != null) {
                    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
                    userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
                    userRegistrationFailureInfo.setErrorCode(RegConstants.REGISTER_SOCIAL_FAILED_SERVER_ERROR);
                    socialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });

        if (isJumpInitializated()) {
            if (socialProviderLoginHandler != null) {
                registerUserForSocial(givenName, displayName, familyName, userEmail, olderThanAgeLimit, isReceiveMarketingEmail, socialProviderLoginHandler, socialRegistrationToken);
            }
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }
    }

    private void registerUserForSocial(final String givenName, final String displayName, final String familyName,
                                       final String userEmail, final boolean olderThanAgeLimit, final boolean isReceiveMarketingEmail,
                                       final SocialProviderLoginHandler socialProviderLoginHandler, final String socialRegistrationToken) {
        DIUserProfile profile = new DIUserProfile();
        profile.setGivenName(givenName);
        profile.setDisplayName(displayName);
        profile.setFamilyName(familyName);
        profile.setEmail(userEmail);
        profile.setOlderThanAgeLimit(olderThanAgeLimit);
        profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
        completeSocialProviderLogin(profile, new SocialProviderLoginHandler() {
            @Override
            public void onLoginSuccess() {
                if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow() && getEmailVerificationStatus(mContext)) {
                    DIUserProfile userProfile = getUserInstance(mContext);

                    HsdpUser hsdpUser = new HsdpUser(mContext);
                    hsdpUser.socialLogin(userProfile.getEmail(), getAccessToken(), new SocialLoginHandler() {

                        @Override
                        public void onLoginSuccess() {
                            socialProviderLoginHandler.onLoginSuccess();
                        }

                        @Override
                        public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                            socialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                        }
                    });

                } else {
                    socialProviderLoginHandler.onLoginSuccess();
                }
            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                socialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
            }

            @Override
            public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
                socialProviderLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord, socialRegistrationToken);
            }

            @Override
            public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
                socialProviderLoginHandler.onLoginFailedWithMergeFlowError(mergeToken, existingProvider, conflictingIdentityProvider, conflictingIdpNameLocalized, existingIdpNameLocalized, emailId);
            }

            @Override
            public void onContinueSocialProviderLoginSuccess() {
                socialProviderLoginHandler.onContinueSocialProviderLoginSuccess();
            }

            @Override
            public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                socialProviderLoginHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
            }
        }, socialRegistrationToken);
    }

    // For Two Step registration
    public void completeSocialProviderLogin(DIUserProfile diUserProfile,
                                            SocialProviderLoginHandler socialProviderLoginHandler, String socialRegistrationToken) {
        String familyName = "";
        if (diUserProfile != null) {

            mEmail = diUserProfile.getEmail();
            mGivenName = diUserProfile.getGivenName();
            familyName = diUserProfile.getFamilyName();
            mPassword = diUserProfile.getPassword();
            mDisplayName = diUserProfile.getDisplayName();
            mOlderThanAgeLimit = diUserProfile.getOlderThanAgeLimit();
            mReceiveMarketingEmails = diUserProfile.getReceiveMarketingEmail();

            JSONObject newUser = new JSONObject();
            try {
                newUser.put(USER_EMAIL, mEmail).put(USER_GIVEN_NAME, mGivenName)
                        .put(USER_FAMILY_NAME, familyName).put(USER_PASSWORD, mPassword)
                        .put(USER_DISPLAY_NAME, mDisplayName)
                        .put(USER_OLDER_THAN_AGE_LIMIT, mOlderThanAgeLimit)
                        .put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmails);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "On completeSocialProviderLogin,Caught JSON Exception");
            }

            ContinueSocialProviderLogin continueSocialProviderLogin = new ContinueSocialProviderLogin(
                    socialProviderLoginHandler, mContext, mUpdateUserRecordHandler);
            Jump.registerNewUser(newUser, socialRegistrationToken, continueSocialProviderLogin);
        } else {
            UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
            userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);

            socialProviderLoginHandler
                    .onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
        }
    }

    // For getting values from Captured and Saved Json object
    public DIUserProfile getUserInstance(Context context) {
        CaptureRecord captureRecord = Jump.getSignedInUser();
        if (captureRecord == null) {
            captureRecord = CaptureRecord.loadFromDisk(context);
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
            //Get JSON String for user address to set his/her country code
            JSONObject userAddress = new JSONObject(captureRecord.getString(CONSUMER_PRIMARY_ADDRESS));
            diUserProfile.setCountryCode(userAddress.getString(CONSUMER_COUNTRY));
            diUserProfile.setLanguageCode(captureRecord.getString(CONSUMER_PREFERED_LANGUAGE));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "On getUserInstance,Caught JSON Exception");
        }
        return diUserProfile;
    }

    // For checking email verification
    public boolean getEmailVerificationStatus(Context context) {
        mEmailVerified = false;
        CaptureRecord captured = CaptureRecord.loadFromDisk(context);

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
/*

    public boolean isUserSignIn(Context context) {

        boolean signedIn = true;
        if (RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
            signedIn = signedIn && getEmailVerificationStatus(context);
        }
        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
            HsdpUser hsdpUser = new HsdpUser(context);

            signedIn = signedIn && hsdpUser.isHsdpUserSignedIn();
        }
        if (RegistrationConfiguration.getInstance().getJanRainConfiguration() != null) {
            signedIn = signedIn && getAccessToken()!=null;
        }
        return signedIn;
    }
*/


    public boolean isUserSignIn(Context context) {
        CaptureRecord capturedRecord = Jump.getSignedInUser();
        if (capturedRecord == null) {
            capturedRecord = CaptureRecord.loadFromDisk(context);
        }
        if (capturedRecord == null) {
            return false;
        }

        boolean signedIn = true;
        if (RegistrationConfiguration.getInstance().getFlow().isEmailVerificationRequired()) {
            signedIn = signedIn && !capturedRecord.isNull(USER_EMAIL_VERIFIED);
            Log.i("Signin ", "isEmailVerificationRequired + Value :" + signedIn);
        }
        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
            HsdpUser hsdpUser = new HsdpUser(context);
            signedIn = signedIn && hsdpUser.isHsdpUserSignedIn();
            Log.i("Signin ", "isHsdpFlow + Value :" + signedIn);
        }
        if (RegistrationConfiguration.getInstance().getJanRainConfiguration() != null) {
            signedIn = signedIn && capturedRecord.getAccessToken() != null;
            Log.i("Signin ", "getJanRainConfiguration + Value :" + signedIn);
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
        }, mContext);

    }

    // For update receive marketing email
    public void updateReceiveMarketingEmail(
            final UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmail,
            final boolean receiveMarketingEmail) {

        mRegistrationHelper.registerJumpFlowDownloadListener(new JumpFlowDownloadStatusListener() {
            @Override
            public void onFlowDownloadSuccess() {
                if (updateReceiveMarketingEmail != null) {
                    refreshReceiveMarketignEmail(updateReceiveMarketingEmail, receiveMarketingEmail);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();

            }

            @Override
            public void onFlowDownloadFailure() {
                if (updateReceiveMarketingEmail != null) {
                    updateReceiveMarketingEmail.onUpdateReceiveMarketingEmailFailedWithError(RegConstants.UPDATE_MARKETING_EMAIL_FAILED_SERVER_ERROR);
                }
                mRegistrationHelper.unregisterJumpFlowDownloadListener();
            }
        });


        if (isJumpInitializated()) {
            if (updateReceiveMarketingEmail != null) {
                refreshReceiveMarketignEmail(updateReceiveMarketingEmail, receiveMarketingEmail);
            }
            return;
        } else if (!isJumpInitializationInProgress()) {
            RegistrationHelper.getInstance().initializeUserRegistration(mContext, RegistrationHelper.getInstance().getLocale());
        }

    }

    private void updateMarketingEmailAfterRefreshAccessToken(
            UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmail,
            boolean receiveMarketingEmail) {
        mCapturedData = CaptureRecord.loadFromDisk(mContext);
        JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
        UpdateReceiveMarketingEmail updateReceiveMarketingEmailHandler = new UpdateReceiveMarketingEmail(
                updateReceiveMarketingEmail, mContext, receiveMarketingEmail);
        if (mCapturedData != null) {
            try {
                mCapturedData.put(USER_RECEIVE_MARKETING_EMAIL, receiveMarketingEmail);
                try {
                    mCapturedData.synchronize(updateReceiveMarketingEmailHandler, originalUserInfo);
                } catch (InvalidApidChangeException e) {
                    Log.e(LOG_TAG,
                            "On updateReceiveMarketingEmail,Caught InvalidApidChange Exception");
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "On updateReceiveMarketingEmail,Caught JSON Exception");
            }
        }
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
            logoutJanrainUser();
            if (logoutHandler != null) {
                logoutHandler.onLogoutSuccess();
                RegistrationHelper.getInstance().getUserRegistrationListener()
                        .notifyOnUserLogoutSuccess();
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

    private void refreshandUpdateUser(final Context context, final RefreshUserHandler handler) {

        if (Jump.getSignedInUser() == null) {
            handler.onRefreshUserFailed(0);
            return;
        }
        Jump.fetchCaptureUserFromServer(new CaptureApiResultHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                Jump.saveToDisk(context);
                buildCoppaConfiguration();
                if (!RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
                    handler.onRefreshUserSuccess();
                    return;
                }

                if (getEmailVerificationStatus(context)) {
                    DIUserProfile userProfile = getDIUserProfileFromDisk();
                    HsdpUser hsdpUser = new HsdpUser(context);
                    HsdpUserRecord hsdpUserRecord = hsdpUser.getHsdpUserRecord();
                    if (userProfile != null && null != userProfile.getEmail() && null != ABCD.getInstance().getmP() && hsdpUserRecord == null) {
                        loginIntoHsdp(userProfile.getEmail(), ABCD.getInstance().getmP(), new TraditionalLoginHandler() {
                            @Override
                            public void onLoginSuccess() {
                                ABCD.getInstance().setmP(null);
                                handler.onRefreshUserSuccess();
                            }

                            @Override
                            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                                handler.onRefreshUserFailed(RegConstants.HSDP_ACTIVATE_ACCOUNT_FAILED);
                            }
                        });
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
                    }, context);
                }
                handler.onRefreshUserFailed(0);
            }
        });
    }

    /**
     * Refresh User object and align with Server
     *
     * @param context Application context
     * @param handler Callback mHandler
     */
    public void refreshUser(final Context context, final RefreshUserHandler handler) {
        refreshandUpdateUser(context, handler);
    }

    public void buildCoppaConfiguration() {
        if (Jump.getSignedInUser() != null) {
            CoppaConfiguration.getCoopaConfigurationFlields(Jump.getSignedInUser());
        }
    }

    private void logoutHsdp(final LogoutHandler logoutHandler) {
        final HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.logOut(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                logoutJanrainUser();
                hsdpUser.deleteFromDisk();
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

    public void clearData() {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.deleteFromDisk();
        logoutJanrainUser();
    }

    private void logoutJanrainUser() {
        deleteDIUserProfileFromDisk();
        CoppaConfiguration.clearConfiguration();

        if (JRSession.getInstance() != null) {
            JRSession.getInstance().signOutAllAuthenticatedUsers();
        }
        CaptureRecord.deleteFromDisk(mContext);
        mContext.deleteFile(DI_PROFILE_FILE);
    }

    private final String DI_PROFILE_FILE = "diProfile";

    private void saveDIUserProfileToDisk(DIUserProfile diUserProfile) {
        try {
            diUserProfile.setPassword(null);
            FileOutputStream fos = mContext.openFileOutput(DI_PROFILE_FILE, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            String objectPlainString = SecureStorage.objectToString(diUserProfile);
            byte[] ectext = SecureStorage.encrypt(objectPlainString);
            oos.writeObject(ectext);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DIUserProfile getDIUserProfileFromDisk() {
        System.out.println("*********** getDIUserProfileFromDisk");
        DIUserProfile diUserProfile = null;
        try {
            FileInputStream fis = mContext.openFileInput(DI_PROFILE_FILE);
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

    private void deleteDIUserProfileFromDisk() {
        mContext.deleteFile(DI_PROFILE_FILE);
    }
}
