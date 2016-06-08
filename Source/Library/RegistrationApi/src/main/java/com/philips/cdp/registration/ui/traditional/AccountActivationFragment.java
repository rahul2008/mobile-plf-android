
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XHavingProblems;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegAlertDialog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONException;

import java.util.HashMap;

public class AccountActivationFragment extends RegistrationBaseFragment implements OnClickListener,
        RefreshUserHandler, ResendVerificationEmailHandler, NetworStateListener, TraditionalLoginHandler {

    private Button mBtnActivate;

    private Button mBtnResend;

    private TextView mTvVerifyEmail;

    private LinearLayout mLlWelcomeContainer;

    private XHavingProblems mViewHavingProblem;

    private RelativeLayout mRlSingInOptions;

    private ProgressBar mPbActivateSpinner;

    private ProgressBar mPbResendSpinner;

    private User mUser;

    private Context mContext;

    private XRegError mRegError;

    private XRegError mEMailVerifiedError;

    private String mEmailId;

    private int RESEND_ENABLE_BUTTON_INTERVAL = 300000;

    private ScrollView mSvRootLayout;

    private boolean isSocialProvider;

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

        Bundle bundle = getArguments();
        if (null != bundle) {
            isSocialProvider = bundle.getBoolean(RegConstants.IS_SOCIAL_PROVIDER);
        }
        mUser = new User(mContext);
        View view = inflater.inflate(R.layout.fragment_account_activation, null);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        handleOrientation(view);
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
        setCustomParams(config);
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
       	mUser.refreshUser( this);
    }

    private void initUI(View view) {
        consumeTouch(view);
        mTvVerifyEmail = (TextView) view.findViewById(R.id.tv_reg_veify_email);
        mLlWelcomeContainer = (LinearLayout) view.findViewById(R.id.ll_reg_welcome_container);
        mViewHavingProblem = (XHavingProblems) view.findViewById(R.id.view_having_problem);
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

        mEmailId = mUser.getEmail();

        String email = getString(R.string.VerifyEmail_EmailSentto_lbltxt);
        email = String.format(email, mEmailId);
        tvEmail.setText(email);

        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mEMailVerifiedError = (XRegError) view.findViewById(R.id.reg_email_verified_error);
        handleUiState();
    }

    private void handleUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
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
            scrollViewAutomatically(mRegError, mSvRootLayout);
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
        if (mUser.getEmailVerificationStatus()) {
            mBtnResend.setVisibility(View.GONE);
            mEMailVerifiedError.hideError();
            mRegError.hideError();
            trackActionStatus(AppTagingConstants.SEND_DATA,
                    AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_USER_REGISTRATION);
            launchWelcomeFragment();

        } else {
            mEMailVerifiedError.setVisibility(View.VISIBLE);
            mEMailVerifiedError.setError(mContext.getResources().getString(
                    R.string.RegEmailNotVerified_AlertPopupErrorText));
            trackActionLoginError(AppTagingConstants.EMAIL_NOT_VERIFIED);
            scrollViewAutomatically(mEMailVerifiedError, mSvRootLayout);
        }
    }

    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    private void launchAlmostFragment() {
        getRegistrationFragment().addPlainAlmostDoneFragment();
        trackPage(AppTaggingPages.ALMOST_DONE);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mTvVerifyEmail, width);
        applyParams(config, mLlWelcomeContainer, width);
        applyParams(config, mViewHavingProblem, width);
        applyParams(config, mRlSingInOptions, width);
        applyParams(config, mRegError, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        if(isSocialProvider){
            return R.string.SigIn_TitleTxt;
        }else{
            return R.string.RegCreateAccount_NavTitle;
        }
    }

    @Override
    public void onRefreshUserSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "AccountActivationFragment : onRefreshUserSuccess");
                updateActivationUIState();
            }
        });
    }

    @Override
    public void onRefreshUserFailed(final int error) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleRefreshUserFailed(error);
            }
        });
    }

    private void handleRefreshUserFailed(int error) {
        RLog.i(RLog.CALLBACK, "AccountActivationFragment : onRefreshUserFailed");
        if (error == RegConstants.HSDP_ACTIVATE_ACCOUNT_FAILED) {
            mEMailVerifiedError.setError(mContext.getString(R.string.JanRain_Server_Connection_Failed));
            hideActivateSpinner();
            mBtnActivate.setEnabled(true);
        } else {
            updateActivationUIState();
        }
    }

    @Override
    public void onResendVerificationEmailSuccess() {
       handleOnUIThread(new Runnable() {
           @Override
           public void run() {
               handleResendVerificationEmailSuccess();
           }
       });
    }

    private void handleResendVerificationEmailSuccess() {
        RLog.i(RLog.CALLBACK, "AccountActivationFragment : onResendVerificationEmailSuccess");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_RESEND_EMAIL_VERIFICATION);
        map.put(AppTagingConstants.STATUS_NOTIFICATION, AppTagingConstants.RESEND_VERIFICATION_MAIL_LINK_SENT);
        trackMultipleActionsMap(AppTagingConstants.SEND_DATA,map);
        updateResendUIState();
        RegAlertDialog.showResetPasswordDialog(mContext.getResources().getString(R.string.Verification_email_Title),
                mContext.getResources().getString(R.string.Verification_email_Message), getRegistrationFragment().getParentActivity(), mContinueBtnClick);
        mBtnResend.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBtnResend.setEnabled(true);
            }
        }, RESEND_ENABLE_BUTTON_INTERVAL);
    }

    private void updateResendUIState() {
        mBtnActivate.setEnabled(true);
        mBtnResend.setEnabled(false);
        hideResendSpinner();
    }

    @Override
    public void onResendVerificationEmailFailedWithError(
            final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
            }
        });

    }

    private void handleResendVerificationEmailFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK,"AccountActivationFragment : onResendVerificationEmailFailedWithError");
        updateResendUIState();
        trackActionResendVerificationFailure(userRegistrationFailureInfo.getErrorCode());
        try {
            mRegError.setError(userRegistrationFailureInfo.getError().raw_response.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mBtnResend.setEnabled(true);
        mEMailVerifiedError.setVisibility(View.GONE);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "AccountActivationFragment :onNetWorkStateReceived state :"
                + isOnline);
        handleUiState();
    }

    @Override
    public void onLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateActivationUIState();
            }
        });
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                mEMailVerifiedError.setError(userRegistrationFailureInfo.getErrorDescription());
                hideActivateSpinner();
                mBtnActivate.setEnabled(true);
            }
        });

    }

    private OnClickListener mContinueBtnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            RegAlertDialog.dismissDialog();
        }
    };
}
