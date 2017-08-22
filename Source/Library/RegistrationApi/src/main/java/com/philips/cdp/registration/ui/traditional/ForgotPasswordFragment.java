/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.*;
import android.content.res.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import javax.inject.*;

import butterknife.*;

import static com.philips.cdp.registration.ui.utils.RegAlertDialog.*;

public class ForgotPasswordFragment extends RegistrationBaseFragment implements
         ForgotPasswordContract {

    private static final int FAILURE_TO_CONNECT = -1;

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.usr_forgotpassword_inputId_inputValidation)
    InputValidationLayout usr_forgotpassword_inputId_inputValidation;

    @BindView(R2.id.usr_forgotpassword_inputId_textField)
    ValidationEditText userIdEditText;

    @BindView(R2.id.usr_forgotpassword_continue_button)
    ProgressBarButton sendButton;

    @BindView(R2.id.usr_forgotpassword_error_msg)
    XRegError mRegError;

    @BindView(R2.id.usr_forgotpassword_rootLayout_scrollView)
    ScrollView layoutScrollView;

    private final int SOCIAL_SIGIN_IN_ONLY_CODE = 540;

    private final int BAD_RESPONSE_CODE = 7004;

    private Context context;

    private User user;

    ForgotPasswordPresenter forgotPasswordPresenter;

    public LoginIdValidator loginIdValidator = new LoginIdValidator(new ValidLoginId() {
        @Override
        public int isValid(boolean valid) {
            if(valid)
                enableSendButton();
            else
                disableSendButton();
            return 0;
        }

        @Override
        public int isEmpty(boolean emptyField) {
            if (emptyField) {
                usr_forgotpassword_inputId_inputValidation.setErrorMessage(R.string.reg_EmptyField_ErrorMsg);
            } else {
                usr_forgotpassword_inputId_inputValidation.setErrorMessage(R.string.reg_InvalidEmailAdddress_ErrorMsg);
            }
            disableSendButton();
            return 0;
        }
    });


    void enableSendButton(){
        sendButton.setEnabled(true);
    }

    void disableSendButton(){
        sendButton.setEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ResetPasswordFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        context = getRegistrationFragment().getActivity().getApplicationContext();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ResetPasswordFragment : onCreateView");
        forgotPasswordPresenter = new ForgotPasswordPresenter(this, context);
        forgotPasswordPresenter.registerListener();
        RLog.i(RLog.EVENT_LISTENERS,
                "ResetPasswordFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        View view = inflater.inflate(R.layout.reg_fragment_forgot_password, container, false);
        ButterKnife.bind(this, view);
        user = new User(context);

        usr_forgotpassword_inputId_inputValidation.setValidator(loginIdValidator);

        initUI(view);
        handleUiState();
        handleOrientation(view);
        return view;
    }

    private void initUI(View view) {
//        consumeTouch(view);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        userIdEditText.requestFocus();
        userIdEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sendButton.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        forgotPasswordPresenter.clearDisposal();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ResetPasswordFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ResetPasswordFragment : onDestroy");
        forgotPasswordPresenter.unRegisterListener();

        RLog.i(RLog.EVENT_LISTENERS,
                "ResetPasswordFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ResetPasswordFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mRegError, width);
    }

    private void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();
        } else {
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, layoutScrollView);
        }
    }

    private void updateUiStatus() {
        if (networkUtility.isNetworkAvailable()) {
            sendButton.setEnabled(true);
            sendButton.hideProgressIndicator();
            mRegError.hideError();
        } else {
            sendButton.hideProgressIndicator();
            sendButton.setEnabled(false);
        }
    }

    @OnClick(R2.id.usr_forgotpassword_continue_button)
    public void sendButton() {
        RLog.d(RLog.ONCLICK, "SignInAccountFragment : Forgot Password");
        resetPassword();
    }

    private void resetPassword() {
        boolean validatorResult;
        if (FieldsValidator.isValidEmail(userIdEditText.getText().toString())) {
            validatorResult = true;
        } else {
            validatorResult = FieldsValidator.isValidMobileNumber(userIdEditText.getText().toString());
        }

        if (!validatorResult) {
            usr_forgotpassword_inputId_inputValidation.showError();
        } else {
            if (networkUtility.isNetworkAvailable()) {

                if (user != null) {
                    userIdEditText.clearFocus();
                    showForgotPasswordSpinner();
                    if (FieldsValidator.isValidEmail(userIdEditText.getText().toString())) {
                        forgotPasswordPresenter.forgotPasswordRequest(userIdEditText.getText().toString(),
                                user);
                    } else {
                        forgotPasswordPresenter.initateCreateResendSMSIntent(userIdEditText.getText().toString(),getRegistrationFragment());
                    }
                }

            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        }
    }

    private void showForgotPasswordSpinner() {
        sendButton.showProgressIndicator();
    }

    @Override
    public void hideForgotPasswordSpinner() {
        sendButton.hideProgressIndicator();
    }

    @Override
    public void handleSendForgotPasswordSuccess() {
        RLog.i(RLog.CALLBACK, "ResetPasswordFragment : onSendForgotPasswordSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppTagingConstants.RESET_PASSWORD_SUCCESS);
        hideForgotPasswordSpinner();
        showDialog(context.getResources().getString(R.string.reg_forgotpassword_dialog_title),
                context.getResources().getString(R.string.reg_forgotpassword_dialog_desc1),
                context.getResources().getString(R.string.reg_forgotpassword_dialog_desc2),
                context.getResources().getString(R.string.reg_forgotpassword_dialog_button),
                getRegistrationFragment().getParentActivity(), mContinueBtnClick);
        hideForgotPasswordSpinner();
        mRegError.hideError();
    }

    @Override
    public void handleSendForgotPasswordFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "SignInAccountFragment : onSendForgotPasswordFailedWithError");
        hideForgotPasswordSpinner();
        if (userRegistrationFailureInfo.getErrorCode() == FAILURE_TO_CONNECT || userRegistrationFailureInfo.getErrorCode() == BAD_RESPONSE_CODE) {
            mRegError.setError(context.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
            usr_forgotpassword_inputId_inputValidation.showError();
            return;
        }
        if (userRegistrationFailureInfo.getErrorCode() == SOCIAL_SIGIN_IN_ONLY_CODE) {
            usr_forgotpassword_inputId_inputValidation.setErrorMessage(getString(R.string.reg_TraditionalSignIn_ForgotPwdSocialError_lbltxt));
            usr_forgotpassword_inputId_inputValidation.showError();
            sendButton.setEnabled(false);
        } else {
            usr_forgotpassword_inputId_inputValidation.showError();
            usr_forgotpassword_inputId_inputValidation.setErrorMessage(userRegistrationFailureInfo.getErrorDescription());
            sendButton.setEnabled(false);
        }
        scrollViewAutomatically(userIdEditText, layoutScrollView);
        AppTaggingErrors.trackActionForgotPasswordFailure(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
    }

    private View.OnClickListener mContinueBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            trackPage(AppTaggingPages.SIGN_IN_ACCOUNT);
            getFragmentManager().popBackStack();
            RegAlertDialog.dismissDialog();
        }
    };

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    @Override
    public void trackAction(String state, String key, String value) {
        trackActionStatus(state,key,value);
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
    public void forgotPasswordErrorMessage(String errorMsg){
        usr_forgotpassword_inputId_inputValidation.setErrorMessage(errorMsg);
        usr_forgotpassword_inputId_inputValidation.showError();
    }
}
