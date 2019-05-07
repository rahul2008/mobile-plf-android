/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.URNotification;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.LoginIdValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.ValidLoginId;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.ValidationEditText;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordFragment extends RegistrationBaseFragment implements
        ForgotPasswordContract {

    private static final String TAG = "ForgotPasswordFragment";

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.usr_forgotpassword_inputId_inputValidation)
    InputValidationLayout usr_forgotpassword_inputId_inputValidation;

    @BindView(R2.id.usr_forgotpassword_inputId_textField)
    ValidationEditText userIdEditText;

    @BindView(R2.id.usr_forgotpassword_sendRequest_button)
    ProgressBarButton sendEmailOrSMSButton;

    @BindView(R2.id.usr_forgotpassword_error_msg)
    XRegError mRegError;

    @BindView(R2.id.usr_forgotpassword_rootLayout_scrollView)
    ScrollView layoutScrollView;

    @BindView(R2.id.usr_forgotpassword_email_label)
    Label usr_forgotpassword_email_label;

    @BindView(R2.id.usr_forgotpassword_input_label)
    Label usr_forgotpassword_input_label;

    @BindView(R2.id.usr_reg_root_layout)
    LinearLayout usrForgotPasswordBaseLayout;

    //Temp name:
    boolean isRequestSent;

    private final int SOCIAL_SIGIN_IN_ONLY_CODE = 540;

    private Context context;

    @Inject
    User user;


    @Inject
    RegistrationHelper registrationHelper;


    @Inject
    EventHelper eventHelper;

    ForgotPasswordPresenter forgotPasswordPresenter;

    boolean isValidLoginId;

    public LoginIdValidator loginIdValidator = new LoginIdValidator(new ValidLoginId() {
        @Override
        public int isValid(boolean valid) {
            isValidLoginId = valid;
            if (valid)
                enableSendButton();
            else
                disableSendButton();
            return 0;
        }

        @Override
        public int isEmpty(boolean emptyField) {
            if (emptyField) {
                usr_forgotpassword_inputId_inputValidation.setErrorMessage(R.string.USR_EmptyField_ErrorMsg);
            } else {
                usr_forgotpassword_inputId_inputValidation.setErrorMessage(R.string.USR_InvalidEmailAdddress_ErrorMsg);
            }
            disableSendButton();
            return 0;
        }
    });


    void enableSendButton() {
        sendEmailOrSMSButton.setEnabled(true);
    }

    void disableSendButton() {
        sendEmailOrSMSButton.setEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
        RLog.i(TAG,"Screen name is "+ TAG);

        registerInlineNotificationListener(this);
        View view = inflater.inflate(R.layout.reg_fragment_forgot_password, container, false);

        forgotPasswordPresenter = new ForgotPasswordPresenter(registrationHelper, eventHelper, this, context);
        forgotPasswordPresenter.registerListener();

        ButterKnife.bind(this, view);
        user = new User(context);

        usr_forgotpassword_inputId_inputValidation.setValidator(loginIdValidator);

        initUI();
        handleUiState();
        handleOrientation(view);
        return view;
    }

    private void initUI() {
        if (RegistrationHelper.getInstance().isMobileFlow()) {
            usr_forgotpassword_email_label.setText(R.string.USR_DLS_Email_Phone_Label_Text);
            usr_forgotpassword_input_label.setText(R.string.USR_DLS_Forgot_Password_Body_With_Phone_No);
        }
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        userIdEditText.requestFocus();
        userIdEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sendEmailOrSMSButton.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (forgotPasswordPresenter != null)
            forgotPasswordPresenter.clearDisposable();
        RLog.d(TAG, "ResetPasswordFragment : onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        RLog.d(TAG, "onDestroy");
        if (forgotPasswordPresenter != null)
            forgotPasswordPresenter.unRegisterListener();

        RLog.d(TAG,
                "unregister: NetworkStateListener,JANRAIN_INIT_SUCCESS");

        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(TAG, " onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
    }

    private void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();
            hideNotificationBarView();
        } else {
            showNotificationBarOnNetworkNotAvailable();
            scrollViewAutomatically(mRegError, layoutScrollView);
        }
    }

    private void updateUiStatus() {
        if (networkUtility.isNetworkAvailable()) {
            if (isValidLoginId) {
                sendEmailOrSMSButton.setEnabled(true);
            }
            sendEmailOrSMSButton.hideProgressIndicator();
            mRegError.hideError();
            hideNotificationBarView();
        } else {
            sendEmailOrSMSButton.hideProgressIndicator();
            sendEmailOrSMSButton.setEnabled(false);
            showNotificationBarOnNetworkNotAvailable();
        }
    }

    @OnClick(R2.id.usr_forgotpassword_sendRequest_button)
    public void sendRequestButton() {
        RLog.i(TAG, TAG+".forgotpassword sendRequest  clicked");
        showForgotPasswordSpinner();
        getRegistrationFragment().hideKeyBoard();
        resetPassword();
    }

    private void resetPassword() {
        if (networkUtility.isNetworkAvailable()) {
            if (user != null) {
                userIdEditText.clearFocus();
                if (FieldsValidator.isValidEmail(userIdEditText.getText().toString())) {
                    forgotPasswordPresenter.forgotPasswordRequest(userIdEditText.getText().toString(),
                            user);
                    AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.KEY_FORGOT_PASSWORD_CHANNEL,
                            AppTagingConstants.VALUE_FORGOT_PASSWORD_CHANNEL_EMAIL);
                } else {
                    forgotPasswordPresenter.initateCreateResendSMSIntent(
                            userIdEditText.getText().toString());
                    AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.KEY_FORGOT_PASSWORD_CHANNEL,
                            AppTagingConstants.VALUE_FORGOT_PASSWORD_CHANNEL_PHONE_NUMBER);
                }
            }

        } else {
            hideForgotPasswordSpinner();
            showNotificationBarOnNetworkNotAvailable();
        }
    }

    private void showForgotPasswordSpinner() {
        isRequestSent = true;
        sendEmailOrSMSButton.showProgressIndicator();
    }

    @Override
    public void hideForgotPasswordSpinner() {
        isRequestSent = false;
        sendEmailOrSMSButton.hideProgressIndicator();
    }

    @Override
    public void handleSendForgotPasswordSuccess() {
        RLog.d(TAG, "ResetPasswordFragment : onSendForgotPasswordSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppTagingConstants.RESET_PASSWORD_SUCCESS);
        showAlert();
        hideForgotPasswordSpinner();
        mRegError.hideError();
    }

    private AlertDialogFragment alertDialogFragment;

    void showAlert() {
        try {
            if (alertDialogFragment == null && isRequestSent == true) {
                final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                        .setDialogType(DialogConstants.TYPE_DIALOG)
                        .setDialogLayout(R.layout.reg_forgot_password_alert)
                        .setPositiveButton(getString(R.string.USR_DLS_Forgot_Password_Alert_Button_Title), v -> {
                            trackPage(AppTaggingPages.SIGN_IN_ACCOUNT);
                            alertDialogFragment.dismiss();
                            alertDialogFragment = null;
                            getFragmentManager().popBackStack();
                        })
                        .setDimLayer(DialogConstants.DIM_STRONG)
                        .setCancelable(false);
                builder.setTitle(getString(R.string.USR_DLS_Forgot_Password_Alert_Title));
                alertDialogFragment = builder.create();
                alertDialogFragment.show(getFragmentManager(), null);
            }
        } catch (Exception e) {
            RLog.e(TAG,"showAlert : Exception " + e.getMessage());
        }

    }

    @Override
    public void handleSendForgotPasswordFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        hideForgotPasswordSpinner();
        RLog.e(TAG, " handleSendForgotPasswordFailedWithError : Error code = " + userRegistrationFailureInfo.getErrorCode() +
                userRegistrationFailureInfo.getLocalizedValidationErrorMessages());

        if (userRegistrationFailureInfo.getErrorCode() == SOCIAL_SIGIN_IN_ONLY_CODE) {
            if (RegistrationHelper.getInstance().isMobileFlow())
                forgotPasswordErrorMessage(getString(R.string.USR_DLS_Forgot_Password_Body_With_Phone_No));
            else
                forgotPasswordErrorMessage(getString(R.string.USR_DLS_Forgot_Password_Body_Without_Phone_No));
            userRegistrationFailureInfo.setErrorTagging(AppTagingConstants.REG_TRADITIONAL_SIGN_IN_FORGOT_PWD_SOCIAL_ERROR);
            sendEmailOrSMSButton.setEnabled(false);
        } else {
            if (userRegistrationFailureInfo.getErrorCode() == -1) return;
            forgotPasswordErrorMessage(userRegistrationFailureInfo.getLocalizedValidationErrorMessages());
            sendEmailOrSMSButton.setEnabled(false);
        }
        AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.USR_DLS_SigIn_TitleTxt;
    }

    @Override
    public void trackAction(String state, String key, String value) {
        trackActionStatus(state, key, value);
    }


    @Override
    public void addFragment(Fragment fragment) {
        getRegistrationFragment().addFragment(fragment);
    }

    @Override
    public void onSuccessResponse(String response) {
        forgotPasswordPresenter.handleResendSMSRespone(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        hideForgotPasswordSpinner();
        final String message = error.getMessage();
        if (message == null) {
            usr_forgotpassword_inputId_inputValidation.setErrorMessage(new URError(context).getLocalizedError(ErrorType.URX, RegConstants.UNKNOWN_ERROR_ID));
            usr_forgotpassword_inputId_inputValidation.showError();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            final String errorCode = jsonObject.getString("errorCode");
            hideForgotPasswordSpinner();
            RLog.e(TAG, "onErrorResponse : Error from Request " + error.getMessage());
            final Integer code = Integer.parseInt(errorCode);
            if (URNotification.INLINE_ERROR_CODE.contains(code)) {
                forgotPasswordErrorMessage(new URError(context).getLocalizedError(ErrorType.URX, code));
            } else {
                forgotPasswordErrorMessage(new URError(context).getLocalizedError(ErrorType.URX, code));
            }
        } catch (JSONException e) {
            RLog.e(TAG, "onErrorResponse : Exception "+ e.getMessage());
        }
    }

    @Override
    public void handleUiState(boolean isOnline) {
        handleUiState();
        updateUiStatus();
    }

    @Override
    public void handleUiStatus() {
        updateUiStatus();
    }

    @Override
    public void forgotPasswordErrorMessage(String errorMsg) {
        usr_forgotpassword_inputId_inputValidation.setErrorMessage(errorMsg);
        usr_forgotpassword_inputId_inputValidation.showError();
    }

    public void backPressed() {
        hideForgotPasswordSpinner();
    }

    @Override
    public void notificationInlineMsg(String msg) {
        mRegError.setError(msg);
    }
}
