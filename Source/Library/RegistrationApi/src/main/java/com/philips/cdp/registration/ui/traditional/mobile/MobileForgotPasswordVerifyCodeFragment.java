
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.janrain.android.Jump;
import com.philips.cdp.registration.HttpClientService;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.ClientIDConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XMobileHavingProblems;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.XVerifyNumber;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.traditional.WelcomeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegChinaUtil;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class MobileForgotPasswordVerifyCodeFragment extends RegistrationBaseFragment implements RefreshUserHandler, HttpClientServiceReceiver.Listener {

    @Inject
    NetworkUtility networkUtility;

    private LinearLayout mLlCreateAccountFields;

    private RelativeLayout mRlCreateActtBtnContainer;

    private Button mBtnVerify;

    private XVerifyNumber mEtCodeNUmber;

    private ProgressBar mPbSpinner;

    private Context mContext;

    private ScrollView mSvRootLayout;

    private XMobileHavingProblems mVeifyHintView;

    private ForgotPasswordVerifyCodeFragmentController mobileActivationController;


    private XRegError mRegError;
    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;
    private boolean isResendRequested;
    private String verificationSmsCodeURL;
    private String mobileNumber;
    private String responseToken;
    private String redirectUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreate");
        super.onCreate(savedInstanceState);
        mobileActivationController = new ForgotPasswordVerifyCodeFragmentController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        final String mobileNumberKey = "mobileNumber";
        final String responseTokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";


        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreateView");
        trackActionStatus(AppTagingConstants.REGISTRATION_ACTIVATION_SMS, "", "");
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        Bundle bundle = getArguments();
        mobileNumber = bundle.getString(mobileNumberKey);
        responseToken = bundle.getString(responseTokenKey);
        redirectUri = bundle.getString(redirectUriKey);
        verificationSmsCodeURL = bundle.getString(verificationSmsCodeURLKey);
        View view = inflater.inflate(R.layout.reg_mobile_forgot_password_verify_fragment, container, false);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        handleOrientation(view);
        mEtCodeNUmber.setOnClickListener(mResendBtnClick);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlCreateAccountFields, width);
        applyParams(config, mRlCreateActtBtnContainer, width);
        applyParams(config, mVeifyHintView, width);
        applyParams(config, mRegError, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);

        mVeifyHintView = (XMobileHavingProblems) view.findViewById(R.id.view_reg_verify_hint);

        mLlCreateAccountFields = (LinearLayout) view.findViewById(R.id.ll_reg_create_account_fields);
        mRlCreateActtBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_singin_options);

        mBtnVerify = (Button) view.findViewById(R.id.btn_reg_Verify);
        mBtnVerify.setOnClickListener(mobileActivationController);
        mEtCodeNUmber = (XVerifyNumber) view.findViewById(R.id.rl_reg_name_field);
        mEtCodeNUmber.setOnUpdateListener(mobileActivationController);
        mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_activate_spinner);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);
        updateUiStatus();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Account_ActivationCode_Verify_Account;
    }

    private void updateUiStatus() {
        if (mEtCodeNUmber.getNumber().length() >= RegConstants.VERIFY_CODE_MINIMUM_LENGTH) {
            mBtnVerify.setEnabled(true);
        } else {
            mBtnVerify.setEnabled(false);
        }
    }

    public void resendMobileNumberService() {
        getActivity().startService(createResendSMSIntent());
    }


    public Intent createSMSPasswordResetIntent() {

        RLog.i("MobileVerifyCodeFragment ", "response val 2 token " + mEtCodeNUmber.getNumber() + " " + responseToken);

        redirectUri=redirectUri+"?code="+mEtCodeNUmber.getNumber()+"&token="+responseToken;
        final String redirectUriKey = "redirectUri";
        ResetPasswordWebView resetPasswordWebView = new ResetPasswordWebView();
        Bundle bundle = new Bundle();
        bundle.putString(redirectUriKey,redirectUri);
        resetPasswordWebView.setArguments(bundle);
        getRegistrationFragment().addFragment(resetPasswordWebView);
        return null;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String response = resultData.getString("responseStr");
        RLog.i("MobileVerifyCodeFragment ", "onReceiveResult Response Val = " + response);
        try {
            String timer = String.valueOf(mEtCodeNUmber.getTimer().charAt(0));
            if (Integer.parseInt(timer) < 1) {
                countDownTimer.onFinish();
            }
        } catch (NumberFormatException ignore) {

        }
        if (response == null) {
            mEtCodeNUmber.hideResendSpinnerAndEnableResendButton();
            mEtCodeNUmber.showEmailIsInvalidAlert();
            mEtCodeNUmber.setErrDescription(mContext.getResources().getString(R.string.reg_URX_SMS_InternalServerError));
            return;
        } else {
            handleResendSMSRespone(response);
        }
        JSONObject json = null;
        String payload = null;
        String token = null;
        try {
            json = new JSONObject(response);
            payload = json.getString("payload");
            json = new JSONObject(payload);
            token = json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RLog.i("MobileVerifyCodeFragment ", " isAccountActivate is " + token + " -- " + response);
        responseToken = token;
    }


    private void resetTimer() {
        countDownTimer.onFinish();
        countDownTimer.cancel();
        countDownTimer = null;
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
    }


    public void handleUI() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus();
            }
        });
    }

    public void networkUiState() {
        if (networkUtility.isNetworkAvailable()) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.hideError();
            }
            mBtnVerify.setEnabled(true);
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            mBtnVerify.setEnabled(false);
        }
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
        hideSpinner();
        getRegistrationFragment().addFragment(new WelcomeFragment());
    }

    @Override
    public void onRefreshUserFailed(int error) {
        hideSpinner();
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserFailed");
    }

    private void hideSpinner() {
        mPbSpinner.setVisibility(View.GONE);
        mBtnVerify.setEnabled(true);
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : counter");
            mEtCodeNUmber.setCounterFinish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : " + millisUntilFinished / 1000);
            mEtCodeNUmber.setCountertimer(String.format("%02d", +millisUntilFinished / 1000) + "s");
        }
    }


    private String getClientId() {
        ClientIDConfiguration clientIDConfiguration = new ClientIDConfiguration();
        return clientIDConfiguration.getResetPasswordClientId(RegConstants.HTTPS_CONST+Jump.getCaptureDomain());
    }

    private String getRedirectUri() {

        return redirectUri;
    }



    private Intent createResendSMSIntent() {

        final String receiverKey= "receiver";
        final String bodyContentKey= "bodyContent";
        final String urlKey= "url";

        RLog.d(RLog.EVENT_LISTENERS, "MOBILE NUMBER *** : " + mobileNumber);
        RLog.d("Configration : ", " envir :" + RegistrationConfiguration.getInstance().getRegistrationEnvironment());
        String url = verificationSmsCodeURL;

        Intent httpServiceIntent = new Intent(mContext, HttpClientService.class);
        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
        receiver.setListener(this);

        String bodyContent = "provider=JANRAIN-CN&phonenumber=" + FieldsValidator.getMobileNumber(mobileNumber) +
                "&locale=zh_CN&clientId=" + getClientId() + "&code_type=short&" +
                "redirectUri="+getRedirectUri();
        RLog.d("Configration : ", " envir :" + getClientId()+getRedirectUri());

        httpServiceIntent.putExtra(receiverKey, receiver);
        httpServiceIntent.putExtra(bodyContentKey, bodyContent);
        httpServiceIntent.putExtra(urlKey, url);
        return httpServiceIntent;
    }

    private void handleResendSMSRespone(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("errorCode").toString().equals("0")) {
                mEtCodeNUmber.setEnabled(true);
                trackMultipleActionsOnMobileSuccess();
                mEtCodeNUmber.hideResendSpinnerAndEnableResendButton();
                handleResendVerificationEmailSuccess();
                resetTimer();
            } else {
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.TECHNICAL_ERROR, AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION_FAILURE);
                String errorMsg = RegChinaUtil.getErrorMsgDescription(jsonObject.getString("errorCode").toString(), mContext);
                mEtCodeNUmber.hideResendSpinnerAndEnableResendButton();
                RLog.i("MobileVerifyCodeFragment ", " SMS Resend failure = " + response);
                mEtCodeNUmber.showEmailIsInvalidAlert();
                mEtCodeNUmber.setErrDescription(errorMsg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleResendVerificationEmailSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
        if(isResendRequested) {
            RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.reg_Resend_SMS_title),
                    mContext.getResources().getString(R.string.reg_Resend_SMS_Success_Content), getRegistrationFragment().getParentActivity(), mContinueVerifyBtnClick);
            isResendRequested = false;
        }
    }

    private View.OnClickListener mResendBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mEtCodeNUmber.showResendSpinnerAndDisableResendButton();
            mEtCodeNUmber.showValidEmailAlert();
            mBtnVerify.setEnabled(false);
            isResendRequested = true;
            mPbSpinner.setVisibility(View.GONE);
            resendMobileNumberService();
        }
    };

    private View.OnClickListener mContinueVerifyBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RegAlertDialog.dismissDialog();
        }
    };

    /* private String initializePRXLinks(String registrationEnv) {
         if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.DEVELOPMENT.getValue())) {
             return DEV_VERFICATION_CODE;
         }
         if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.PRODUCTION.getValue())) {
             return PROD_VERFICATION_CODE;
         }
         if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.STAGING.getValue())) {
             return STAGE_VERFICATION_CODE;
         }
         if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.TESTING.getValue())) {
             return TEST_VERFICATION_CODE;
         }
         if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.EVALUATION.getValue())) {
             return EVAL_VERFICATION_CODE;
         }
         return null;
     }*/
    private void trackMultipleActionsOnMobileSuccess() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.MOBILE_RESEND_EMAIL_VERFICATION);
        map.put(AppTagingConstants.MOBILE_INAPPNATIFICATION, AppTagingConstants.MOBILE_RESEND_SMS_VERFICATION);
        AppTagging.trackMultipleActions(AppTagingConstants.SEND_DATA, map);
    }

}
