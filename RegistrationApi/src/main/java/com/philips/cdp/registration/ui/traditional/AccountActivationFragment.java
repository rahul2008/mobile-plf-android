
package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.AppTagging.AppTagingConstants;
import com.philips.cdp.registration.AppTagging.AppTaggingPages;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;

public class AccountActivationFragment extends RegistrationBaseFragment implements OnClickListener,
        RefreshUserHandler, ResendVerificationEmailHandler, NetworStateListener {

    private Button mBtnActivate;

    private Button mBtnResend;

    private TextView mTvVerifyEmail;

    private LinearLayout mLlWelcomeContainer;

    private TextView mTvResendDetails;

    private RelativeLayout mRlSingInOptions;

    private ProgressBar mPbActivateSpinner;

    private ProgressBar mPbResendSpinner;

    private User mUser;

    private Context mContext;

    private XRegError mRegError;

    private XRegError mEMailVerifiedError;

    private String mEmailId;

    @Override
    public void onAttach(Activity activity) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        RLog.i(RLog.EVENT_LISTENERS, "AccountActivationFragment register: NetworStateListener");
        mUser = new User(mContext);
        View view = inflater.inflate(R.layout.fragment_account_activation, null);
        initUI(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        RLog.i(RLog.EVENT_LISTENERS, "AccountActivationFragment unregister: NetworStateListener");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AccountActivationFragment : onConfigurationChanged");
        setViewParams(config);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_activate_acct) {
            RLog.d(RLog.ONCLICK, "AccountActivationFragment : Activate Account");
            handleActivate();
        } else if (id == R.id.btn_reg_resend) {
            RLog.d(RLog.ONCLICK, "AccountActivationFragment : Resend");
            handleResend();
        }
    }

    private void handleResend() {
        showResendSpinner();
        mBtnActivate.setEnabled(false);
        mBtnResend.setEnabled(false);
        mUser.resendVerificationMail(mEmailId, this);
    }

    private void handleActivate() {
        showActivateSpinner();
        mBtnActivate.setEnabled(false);
        mBtnResend.setEnabled(false);
        mUser.refreshUser(mContext, this);
    }

    private void initUI(View view) {
        consumeTouch(view);
        mTvVerifyEmail = (TextView) view.findViewById(R.id.tv_reg_veify_email);
        mLlWelcomeContainer = (LinearLayout) view.findViewById(R.id.ll_reg_welcome_container);
        mTvResendDetails = (TextView) view.findViewById(R.id.tv_reg_resend_details);
        mRlSingInOptions = (RelativeLayout) view.findViewById(R.id.rl_reg_singin_options);
        mBtnActivate = (Button) view.findViewById(R.id.btn_reg_activate_acct);
        mBtnResend = (Button) view.findViewById(R.id.btn_reg_resend);
        mBtnActivate.setOnClickListener(this);
        mBtnResend.setOnClickListener(this);

        mPbActivateSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_activate_spinner);
        mPbResendSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_resend_spinner);

        TextView tvEmail = (TextView) view.findViewById(R.id.tv_reg_email);
        TextView mTvContent = (TextView) view.findViewById(R.id.tv_explain_value_to_user);
        if (mTvContent.getText().toString().trim().length() > 0) {
            mTvContent.setVisibility(View.VISIBLE);
        } else {
            mTvContent.setVisibility(View.GONE);
        }

        DIUserProfile userProfile = mUser.getUserInstance(mContext);
        mEmailId = userProfile.getEmail();
        tvEmail.setText(getString(R.string.VerifyEmail_EmailSentto_lbltxt) + mEmailId);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mEMailVerifiedError = (XRegError) view.findViewById(R.id.reg_email_verified_error);
        setViewParams(getResources().getConfiguration());
        //trackPage(AnalyticsPages.ACCOUNT_ACTIVATION);
        handleUiState();
    }

    private void handleUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (RegistrationHelper.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
                mBtnActivate.setEnabled(true);
                mBtnResend.setEnabled(true);
            } else {
                mBtnActivate.setEnabled(false);
                mBtnResend.setEnabled(false);
                mRegError.setError(getString(R.string.NoNetworkConnection));
            }
        } else {
            mRegError.setError(getString(R.string.NoNetworkConnection));
            mBtnActivate.setEnabled(false);
            mBtnResend.setEnabled(false);
            trackActionRegisterError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private void showActivateSpinner() {
        mPbActivateSpinner.setVisibility(View.VISIBLE);
    }

    private void hideActivateSpinner() {
        mPbActivateSpinner.setVisibility(View.GONE);
    }

    private void showResendSpinner() {
        mPbResendSpinner.setVisibility(View.VISIBLE);
    }

    private void hideResendSpinner() {
        mPbResendSpinner.setVisibility(View.GONE);
    }

    private void updateActivationUIState() {
        hideActivateSpinner();
        mBtnActivate.setEnabled(true);
        mBtnActivate.setEnabled(true);
        mBtnResend.setEnabled(true);
        mBtnResend.setEnabled(true);
        if (mUser.getEmailVerificationStatus(mContext)) {
            mBtnResend.setVisibility(View.GONE);
            mEMailVerifiedError.hideError();
            mRegError.hideError();
            trackActionStatus(AppTagingConstants.SEND_DATA,
                    AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_USER_REGISTRATION);
            launchWelcomeFragment();

        } else {

            mEMailVerifiedError.setVisibility(View.VISIBLE);
            mEMailVerifiedError.setError(getResources().getString(
                    R.string.Janrain_Error_Need_Email_Verification));
            trackActionLoginError(AppTagingConstants.EMAIL_NOT_VERIFIED);
        }
    }

    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void setViewParams(Configuration config) {
        applyParams(config, mTvVerifyEmail);
        applyParams(config, mLlWelcomeContainer);
        applyParams(config, mTvResendDetails);
        applyParams(config, mRlSingInOptions);
        applyParams(config, mRegError);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.SigIn_TitleTxt;
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.i(RLog.CALLBACK, "AccountActivationFragment : onRefreshUserSuccess");
        updateActivationUIState();
    }

    @Override
    public void onRefreshUserFailed(int error) {
        RLog.i(RLog.CALLBACK, "AccountActivationFragment : onRefreshUserFailed");
        updateActivationUIState();
    }

    @Override
    public void onResendVerificationEmailSuccess() {
        RLog.i(RLog.CALLBACK, "AccountActivationFragment : onResendVerificationEmailSuccess");
        updateResendUIState();
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
    }

    private void updateResendUIState() {
        mBtnActivate.setEnabled(true);
        mBtnResend.setEnabled(false);
        hideResendSpinner();
    }

    @Override
    public void onResendVerificationEmailFailedWithError(
            UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK,
                "AccountActivationFragment : onResendVerificationEmailFailedWithError");
        updateResendUIState();
        trackActionResendVerificationFailure(userRegistrationFailureInfo.getError().code);
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription() + "\n"
                + userRegistrationFailureInfo.getEmailErrorMessage());

    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "AccountActivationFragment :onNetWorkStateReceived state :"
                + isOnline);
        handleUiState();
    }
}
