
package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.CoppaConfiguration;
import com.philips.cdp.registration.coppa.CoppaExtension;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler {


    private Context mContext;

    private TraditionalLoginHandler mTraditionalLoginHandler;

    private UpdateUserRecordHandler mUpdateUserRecordHandler;

    private String mEmail;

    private String mPassword;

    public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler, Context context,
                            UpdateUserRecordHandler updateUserRecordHandler, String email, String password) {
        mTraditionalLoginHandler = traditionalLoginHandler;
        mContext = context;
        mUpdateUserRecordHandler = updateUserRecordHandler;
        mEmail = email;
        mPassword = password;
    }

    @Override
    public void onSuccess() {
        Jump.saveToDisk(mContext);
        final User user = new User(mContext);
        user.buildCoppaConfiguration();
        if (CoppaConfiguration.getCoppaCommunicationSentAt() != null && RegistrationConfiguration.getInstance().isCoppaFlow()) {
            CoppaExtension coppaExtension = new CoppaExtension();
            coppaExtension.triggerSendCoppaMailAfterLogin(user.getUserInstance(mContext).getEmail());
        }
        mUpdateUserRecordHandler.updateUserRecordLogin();
        if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow() && user.getEmailVerificationStatus(mContext)) {

            HsdpUser login = new HsdpUser(mContext);
            login.login(mEmail, mPassword, new TraditionalLoginHandler() {
                @Override
                public void onLoginSuccess() {
                    mTraditionalLoginHandler.onLoginSuccess();
                }

                @Override
                public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
                    user.logout(new LogoutHandler() {
                        @Override
                        public void onLogoutSuccess() {

                        }

                        @Override
                        public void onLogoutFailure(int responseCode, String message) {

                        }
                    });
                    mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
                }
            });


        } else {
            mTraditionalLoginHandler.onLoginSuccess();
        }
    }

    @Override
    public void onCode(String code) {

    }

    @Override
    public void onFailure(SignInError error) {
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
        userRegistrationFailureInfo.setError(error.captureApiError);
        handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
        handleInvalidCredentials(error.captureApiError, userRegistrationFailureInfo);
        userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
        mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
    }

    private void handleInvalidInputs(CaptureApiError error,
                                     UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (null != error && null != error.error
                && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
            try {
                JSONObject object = error.raw_response;
                JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
                if (jsonObject != null) {

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)) {
                        userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
                                .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)));
                    }

                    if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)) {
                        userRegistrationFailureInfo
                                .setPasswordErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInvalidCredentials(CaptureApiError error,UserRegistrationFailureInfo userRegistrationFailureInfo) {
        if (null != error && null != error.error
                && error.error.equals(RegConstants.INVALID_CREDENTIALS)) {
            try {
                JSONObject object = error.raw_response;
                JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
                if (jsonObject != null) {
                    if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
                        userRegistrationFailureInfo.setEmailErrorMessage(mContext.getResources().getString(R.string.JanRain_Invalid_Credentials));
                        /*userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
                                .getJSONArray(RegConstants.USER_INFORMATION_FORM)));*/
                    }

                    if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
                        userRegistrationFailureInfo.setPasswordErrorMessage(mContext.getResources().getString(R.string.JanRain_Invalid_Credentials));
                        /*userRegistrationFailureInfo
                                .setPasswordErrorMessage(getErrorMessage(jsonObject
                                        .getJSONArray(RegConstants.USER_INFORMATION_FORM)));*/
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String getErrorMessage(JSONArray jsonArray)
            throws JSONException {
        if (null == jsonArray) {
            return null;
        }
        return (String) jsonArray.get(0);
    }

}
