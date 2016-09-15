/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils.test;


import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.controller.RegisterTraditional;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UserTest extends InstrumentationTestCase {

    User mUser = null;

    //	public UserTest() {
    //	super(RegistrationActivity.class);
    //}
    Context context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getTargetContext();
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
        mUser = new User(context);


        //getInstrumentation().get
    }

    public void testUser() throws Exception {

        User result = new User(getInstrumentation().getTargetContext());
        assertNotNull(result);
    }

    public void testRegisterUserInfoForTraditionalIsOnSuccess() throws  RuntimeException{


        TraditionalRegistrationHandler regHandler = new TraditionalRegistrationHandler() {
            @Override
            public void onRegisterSuccess() {

            }

            @Override
            public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };

        UpdateUserRecordHandler updateHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };
        SocialProviderLoginHandler socialProviderLoginHandler = new SocialProviderLoginHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }

            @Override
            public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {

            }

            @Override
            public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {

            }

            @Override
            public void onContinueSocialProviderLoginSuccess() {

            }

            @Override
            public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };

        mUser.loginUserUsingSocialProvider(null,null,
                socialProviderLoginHandler, "mergeToken");

        Jump.SignInResultHandler mockJump = new Jump.SignInResultHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(SignInError error) {

            }
        };
        mockJump.onSuccess();
        RegisterTraditional handler = new RegisterTraditional(regHandler,
                getInstrumentation().getTargetContext(), updateHandler);

        handler.onSuccess();

        TraditionalLoginHandler traditionalLoginHandler = new TraditionalLoginHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        try {
            mUser.loginUsingTraditional("sample","sample", null);
        }catch(Exception e){

        }
        try {
            mUser.loginUsingTraditional(null, null, null);
        }catch(Exception e){

        }
    }

    public void testRegisterUserInfoForSocialIsOnSuccess() {

        String SOCIAL_REG_TOKEN = "socialRegistrationToken";
        SocialProviderLoginHandler socialRegHandler = new SocialProviderLoginHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }

            @Override
            public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {

            }

            @Override
            public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {

            }

            @Override
            public void onContinueSocialProviderLoginSuccess() {

            }

            @Override
            public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };

        UpdateUserRecordHandler updateHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };


        Jump.SignInResultHandler mockJump = new Jump.SignInResultHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(SignInError error) {

            }
        };
        mockJump.onSuccess();

    }
    public void testForgotPasswordForEmailNull() throws Exception {

        String emailAddress = null;

        ForgotPasswordHandler forgotpasswordhandler = new ForgotPasswordHandler() {

            @Override
            public void onSendForgotPasswordSuccess() {

            }

            @Override
            public void onSendForgotPasswordFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        mUser.forgotPassword(emailAddress, forgotpasswordhandler);


    }

    public void testResendVerificationMail(){
        synchronized (context) {
            try {
                RegistrationHelper.getInstance().setAppInfraInstance(new AppInfra.Builder().build(context));
            }catch(Exception e){

            }
            try {

                final ResendVerificationEmailHandler resendVerificationEmail = new ResendVerificationEmailHandler() {
                    @Override
                    public void onResendVerificationEmailSuccess() {

                    }

                    @Override
                    public void onResendVerificationEmailFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

                    }
                };


                mUser.resendVerificationMail("emailAddress", resendVerificationEmail);
            } catch (Exception e) {

            }

            try{

                TraditionalLoginHandler traditionalLoginHandler = new TraditionalLoginHandler() {
                    @Override
                    public void onLoginSuccess() {

                    }

                    @Override
                    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

                    }
                };
                mUser.mergeToTraditionalAccount("emailAddress", "password", "mergeToken",
                        traditionalLoginHandler);

            } catch (Exception e) {


            }


            try{

                RegistrationHelper.getInstance().setAppInfraInstance(new AppInfra.Builder().build(context));

                SocialProviderLoginHandler socialProviderLoginHandler = new SocialProviderLoginHandler() {
                    @Override
                    public void onLoginSuccess() {

                    }

                    @Override
                    public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

                    }

                    @Override
                    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {

                    }

                    @Override
                    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {

                    }

                    @Override
                    public void onContinueSocialProviderLoginSuccess() {

                    }

                    @Override
                    public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

                    }
                };

                mUser.registerUserInfoForSocial("givenName", "displayName", "familyName",
                        "userEmail", true, false,
                        socialProviderLoginHandler, "socialRegistrationToken");
            }
            catch(Exception e){}

//            UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmail = new UpdateReceiveMarketingEmailHandler() {
//                @Override
//                public void onUpdateReceiveMarketingEmailSuccess() {
//
//                }
//
//                @Override
//                public void onUpdateReceiveMarketingEmailFailedWithError(int error) {
//
//                }
//            };
//            mUser.updateReceiveMarketingEmail(updateReceiveMarketingEmail,true);
            LogoutHandler logoutHandle = new LogoutHandler() {
                @Override
                public void onLogoutSuccess() {

                }

                @Override
                public void onLogoutFailure(int responseCode, String message) {

                }
            };
            mUser.logout(logoutHandle);
        }
    }

    public void testUserr(){
        mUser.getAccessToken();
        mUser.getEmail();
        mUser.getPassword();
        mUser.getGivenName();
        mUser.getDisplayName();
        mUser.getFamilyName();
        mUser.getJanrainUUID();
        mUser.getHsdpUUID();
        mUser.getHsdpAccessToken();
        mUser.getLanguageCode();
        mUser.getCountryCode();
        mUser.getEmailVerificationStatus();
        mUser.getOlderThanAgeLimit();
        mUser.getReceiveMarketingEmail();
        mUser.isUserSignIn();
        mUser.handleMergeFlowError("sample");
        assertNotNull(mUser);
    }
    public void testSaveDIUserProfileToDisk(){
        Method method = null;
        DIUserProfile diUserProfile = new DIUserProfile();
        diUserProfile.setHsdpUUID("TestUUID");
        diUserProfile.setHsdpAccessToken("TestHsdpToken");
        diUserProfile.setLanguageCode("en");
        diUserProfile.setCountryCode("US");
        diUserProfile.setEmail("test@test.com");
        diUserProfile.setPassword("@#$%^");
        diUserProfile.setGivenName("TestName");
        diUserProfile.setOlderThanAgeLimit(true);
        diUserProfile.setReceiveMarketingEmail(true);
        diUserProfile.setDisplayName("TestDisplayName");
        diUserProfile.setFamilyName("TestFamilyName");
        diUserProfile.setJanrainUUID("TestJanrainID");


        try {
            method = User.class.getDeclaredMethod("saveDIUserProfileToDisk", DIUserProfile.class);
            method.setAccessible(true);
            method.invoke(mUser,diUserProfile);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
