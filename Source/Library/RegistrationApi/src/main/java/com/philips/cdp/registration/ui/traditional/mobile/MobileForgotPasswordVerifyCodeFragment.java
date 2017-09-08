
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;

import com.jakewharton.rxbinding2.widget.*;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.*;

import org.greenrobot.eventbus.*;

import javax.inject.*;

import static com.janrain.android.Jump.*;

public class MobileForgotPasswordVerifyCodeFragment extends RegistrationBaseFragment implements
        View.OnClickListener, OnUpdateListener, NetworkStateListener, EventListener,
        RefreshUserHandler {

    @Inject
    NetworkUtility networkUtility;

    private LinearLayout mLlCreateAccountFields;

    private ProgressBarButton mBtnVerify;

    private ValidationEditText mEtCodeNUmber;

    private Context mContext;

    private ScrollView mSvRootLayout;

    Button btn_reg_resend_code;

    Label tv_reg_verify;

    private XRegError mRegError;
    private String verificationSmsCodeURL;
    private String mobileNumber;
    private String responseToken;
    private String redirectUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreate");
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.reg_mobile_forgot_password_verify_code_fragment, container, false);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        getRegistrationFragment().startCountDownTimer();
        initUI(view);
        setDescription();
        handleOrientation(view);
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }
    private void setDescription() {
        String userId = mobileNumber;
        String normalText = getString(R.string.reg_verify_mobile_desc1);
        SpannableString str = new SpannableString(normalText + " " + userId);
        str.setSpan(new StyleSpan(Typeface.BOLD), normalText.length(), str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_reg_verify.setText(str);
    }

    @Subscribe
    public void onEvent(UpdateToken event){
        // your implementation
        Toast.makeText(getActivity(), event.getToken(), Toast.LENGTH_SHORT).show();

        responseToken = event.getToken();
    }

    @Subscribe
    public void onEvent(UpdateMobile event){

        mobileNumber = event.getMobileNumber();

        setDescription();
        // your implementation
        Toast.makeText(getActivity(), event.getMobileNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(getRegistrationFragment());
        EventBus.getDefault().unregister(this); // this == your class instance

        super.onDestroy();
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
        applyParams(config, mRegError, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);
        mLlCreateAccountFields = (LinearLayout) view.findViewById(R.id.ll_reg_root_container);
        mBtnVerify = (ProgressBarButton) view.findViewById(R.id.btn_reg_Verify);
        mBtnVerify.setOnClickListener(this);
        mEtCodeNUmber = (ValidationEditText) view.findViewById(R.id.usr_forgotpassword_inputId_ValidationEditText);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        tv_reg_verify = (Label) view.findViewById(R.id.tv_reg_verify);
        btn_reg_resend_code = (Button) view.findViewById(R.id.btn_reg_resend_code);
        btn_reg_resend_code.setOnClickListener(this);
        handleVerificationCode();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Account_ActivationCode_Verify_Account;
    }

    private void handleVerificationCode() {
        RxTextView.textChangeEvents(mEtCodeNUmber)
                .subscribe(aBoolean -> {
                    if (mEtCodeNUmber.getText().length() == 6)
                        mBtnVerify.setEnabled(true);
                    else
                        mBtnVerify.setEnabled(false);
                });
    }


    public Intent createSMSPasswordResetIntent() {

        RLog.i("MobileVerifyCodeFragment ", "response val 2 token " + mEtCodeNUmber.getText() + " " + redirectUri + " " + responseToken);

        redirectUri = redirectUri + "?code=" + mEtCodeNUmber.getText() + "&token=" + responseToken;
        final String redirectUriKey = "redirectUri";
        ResetPasswordWebView resetPasswordWebView = new ResetPasswordWebView();
        Bundle bundle = new Bundle();
        bundle.putString(redirectUriKey, redirectUri);
        resetPasswordWebView.setArguments(bundle);
        getRegistrationFragment().addFragment(resetPasswordWebView);
        return null;
    }

    public void handleUI() {
        handleVerificationCode();
    }

    public void networkUiState() {
        if (networkUtility.isNetworkAvailable()) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.hideError();
            }
            handleVerificationCode();
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            mBtnVerify.setEnabled(false);
        }
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
        hideSpinner();
        getRegistrationFragment().userRegistrationComplete();
    }

    @Override
    public void onRefreshUserFailed(int error) {
        hideSpinner();
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserFailed");
    }

    private void hideSpinner() {
        mBtnVerify.hideProgressIndicator();
        mBtnVerify.setEnabled(true);
    }

    private View.OnClickListener mContinueVerifyBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RegAlertDialog.dismissDialog();
        }
    };

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        if (id == R.id.btn_reg_Verify) {
            RLog.d(RLog.ONCLICK, "Verify Account : Activiate Account");
            createSMSPasswordResetIntent();
        } else if (id == R.id.btn_reg_resend_code){
            RLog.d(RLog.ONCLICK, "Verify Account : Activiate Account resend");
            constructMobileVerifyCodeFragment();
        }
    }

    private void constructMobileVerifyCodeFragment() {

        final String mobileNumberKey = "mobileNumber";
        final String tokenKey = "token";
        final String redirectUriKey = "redirectUri";
        final String verificationSmsCodeURLKey = "verificationSmsCodeURL";

        MobileForgotPassVerifyResendCodeFragment mobileForgotPasswordVerifyCodeFragment = new MobileForgotPassVerifyResendCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(mobileNumberKey, mobileNumber);
        bundle.putString(tokenKey, responseToken);
        bundle.putString(redirectUriKey, getRedirectUri());
        bundle.putString(verificationSmsCodeURLKey, verificationSmsCodeURL);
        mobileForgotPasswordVerifyCodeFragment.setArguments(bundle);
        getRegistrationFragment().addFragment(mobileForgotPasswordVerifyCodeFragment);
    }

    @Override
    public void onUpdate() {
        handleUI();
    }

    @Override
    public void onEventReceived(final String event) {
        handleUI();
    }

    @Override
    public void onNetWorkStateReceived(final boolean isOnline) {
        handleUI();
        networkUiState();
    }
}
