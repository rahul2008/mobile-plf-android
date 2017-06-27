
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
import android.text.Html;
import android.text.style.ClickableSpan;
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
import com.philips.cdp.registration.R2;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTaggingErrors;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.LoginIdEditText;
import com.philips.cdp.registration.ui.customviews.PasswordView;
import com.philips.cdp.registration.ui.customviews.XPasswordHint;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.XUserName;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.traditional.mobile.AddSecureEmailContract;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountFragment extends RegistrationBaseFragment implements CreateAccountContract,
        OnUpdateListener, XCheckBox.OnCheckedChangeListener {

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.ll_reg_create_account_fields)
    LinearLayout mLlCreateAccountFields;

    @BindView(R2.id.ll_reg_create_account_container)
    LinearLayout mLlCreateAccountContainer;

    @BindView(R2.id.ll_reg_accept_terms)
    LinearLayout mLlAcceptTermsContainer;

    @BindView(R2.id.rl_reg_singin_options)
    RelativeLayout mRlCreateActtBtnContainer;

    @BindView(R2.id.btn_reg_register)
    Button mBtnCreateAccount;

    @BindView(R2.id.cb_reg_register_terms)
    XCheckBox mCbMarketingOpt;

    @BindView(R2.id.cb_reg_accept_terms)
    XCheckBox mCbAcceptTerms;

    @BindView(R2.id.rl_reg_name_field)
    XUserName mEtName;

    @BindView(R2.id.rl_reg_email_field)
    LoginIdEditText mEtEmail;

    @BindView(R2.id.rl_reg_password_field)
    PasswordView mEtPassword;

    @BindView(R2.id.reg_error_msg)
    XRegError mRegError;

    @BindView(R2.id.cb_reg_accept_terms_error)
    XRegError mRegAccptTermsError;

    @BindView(R2.id.pb_reg_activate_spinner)
    ProgressBar mPbSpinner;

    @BindView(R2.id.reg_accept_terms_line)
    View mViewLine;

    @BindView(R2.id.sv_root_layout)
    ScrollView mSvRootLayout;

    @BindView(R2.id.view_reg_password_hint)
    XPasswordHint mPasswordHintView;

    @BindView(R2.id.tv_reg_email_exist)
    TextView mTvEmailExist;

    @BindView(R2.id.tv_join_now)
    TextView mJoinnow;

    @BindView(R2.id.tv_reg_first_to_know)
    TextView mTvFirstToKnow;

    @BindView(R2.id.tv_reg_accept_terms)
    TextView acceptTermsView;

    @BindView(R2.id.tv_reg_philips_news)
    TextView receivePhilipsNewsView;

    private User mUser;

    private Context mContext;

    private long mTrackCreateAccountTime;

    private boolean isTermsAndConditionVisible;

    private boolean isSavedEmailErr;

    private boolean isSavedCBTermsChecked;

    private boolean isSavedCbAcceptTermsChecked;

    private boolean isSavedPasswordErr;

    private CreateAccountPresenter createAccountPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreateView");
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        mContext = getRegistrationFragment().getActivity().getApplicationContext();

        createAccountPresenter = new CreateAccountPresenter(this);
        createAccountPresenter.registerListener();

        View view = inflater.inflate(R.layout.reg_fragment_create_account, container, false);
        ButterKnife.bind(this, view);
        initUI(view);
        handleOrientation(view);
        mTrackCreateAccountTime = System.currentTimeMillis();
        return view;
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroy");
        createAccountPresenter.unRegisterListener();

        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
    }

    private Bundle mBundle;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mBundle = outState;
        super.onSaveInstanceState(mBundle);
        if (mEtEmail.isEmailErrorVisible()) {
            isSavedEmailErr = true;
            mBundle.putBoolean("isSavedEmailErr", isSavedEmailErr);
            mBundle.putString("saveEmailErrText", mEtEmail.getSavedEmailErrDescription());
        }
        if (mEtPassword.isPasswordErrorVisible()) {
            isSavedPasswordErr = true;
            mBundle.putBoolean("isSavedPasswordErr", isSavedPasswordErr);
            mBundle.putString("savedPasswordErr", mEtPassword.getmSavedPasswordErrDescription());
        }
        if (mRegAccptTermsError.getVisibility() == View.VISIBLE) {
            isTermsAndConditionVisible = true;
            mBundle.putBoolean("isTermsAndConditionVisible", isTermsAndConditionVisible);
            mBundle.putString("saveTermsAndConditionErrText", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
        }
        if (mCbMarketingOpt.isChecked()) {
            isSavedCBTermsChecked = true;
            mBundle.putBoolean("isSavedCBTermsChecked", isSavedCBTermsChecked);
            mBundle.putString("savedCBTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
        }
        if (mCbAcceptTerms.isChecked()) {
            isSavedCbAcceptTermsChecked = true;
            mBundle.putBoolean("isSavedCbAcceptTermsChecked", isSavedCbAcceptTermsChecked);
            mBundle.putString("savedCbAcceptTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("saveEmailErrText") != null && savedInstanceState.getBoolean("isSavedEmailErr")) {
                mEtEmail.setErrDescription(savedInstanceState.getString("saveEmailErrText"));
                mEtEmail.showInvalidAlert();
                mEtEmail.showErrPopUp();
            }
            if (savedInstanceState.getString("savedPasswordErr") != null && savedInstanceState.getBoolean("isSavedPasswordErr")) {
                mEtPassword.setErrDescription(savedInstanceState.getString("savedPasswordErr"));
                mEtPassword.showInvalidPasswordAlert();
            }
            if (savedInstanceState.getString("saveTermsAndConditionErrText") != null && savedInstanceState.getBoolean("isTermsAndConditionVisible")) {
                mRegAccptTermsError.setError(savedInstanceState.getString("saveTermsAndConditionErrText"));
            }
            if (savedInstanceState.getBoolean("isSavedCBTermsChecked")) {
                mCbMarketingOpt.setChecked(true);
            }
            if (savedInstanceState.getBoolean("isSavedCbAcceptTermsChecked")) {
                mCbAcceptTerms.setChecked(true);
            }
        }
        mBundle = null;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlCreateAccountFields, width);
        applyParams(config, mLlCreateAccountContainer, width);
        applyParams(config, mRlCreateActtBtnContainer, width);
        applyParams(config, mRegError, width);
        applyParams(config, mJoinnow, width);
        applyParams(config, mRegAccptTermsError, width);
        applyParams(config, mLlAcceptTermsContainer, width);
        applyParams(config, mPasswordHintView, width);
        applyParams(config, mTvEmailExist, width);
        applyParams(config, mTvFirstToKnow, width);
    }


    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @OnClick(R2.id.btn_reg_register)
    public void registerUser() {
        RLog.d(RLog.ONCLICK, "CreateAccountFragment : Register Account");
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (mCbAcceptTerms.isChecked()) {
                register();
            } else {
                mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        } else {
            register();
        }
    }

    private void initUI(View view) {
        consumeTouch(view);

        handleABTestingFlow();
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);
        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        String sourceString = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now);
        String updateJoinNowText = " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        sourceString = String.format(sourceString, updateJoinNowText);
        mJoinnow.setText(Html.fromHtml(sourceString));
        String firstToKnow = "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Be_The_First) + "</b> ";
        mTvFirstToKnow.setText(Html.fromHtml(firstToKnow));
        mCbAcceptTerms.setOnCheckedChangeListener(this);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtName.requestFocus();
        mEtName.setOnUpdateListener(this);
        mEtEmail.setOnUpdateListener(this);
        mEtPassword.setOnUpdateListener(this);
        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);
        mEtPassword.setHint(mContext.getResources().getString(R.string.reg_Create_Account_ChoosePwd_PlaceHolder_txtField));
        handleUiAcceptTerms();
        handleUiState();
        mUser = new User(mContext);
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow) {

            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                mLlCreateAccountContainer.setVisibility(View.VISIBLE);
                mJoinnow.setVisibility(View.GONE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_CONTROL);
                break;
            case FLOW_B:

                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                mLlCreateAccountContainer.setVisibility(View.GONE);
                mJoinnow.setVisibility(View.GONE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SPLIT_SIGN_UP);
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                mLlCreateAccountContainer.setVisibility(View.VISIBLE);
                mJoinnow.setVisibility(View.VISIBLE);
                mTvFirstToKnow.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;

            default:
                break;
        }
    }

    private void register() {
        mRegAccptTermsError.setVisibility(View.GONE);
        mEtName.clearFocus();
        mEtEmail.clearFocus();
        mEtPassword.clearFocus();
        showSpinner();
        if (FieldsValidator.isValidEmail(mEtEmail.getEmailId())) {
            mEmail = mEtEmail.getEmailId();
        } else {
            mEmail = FieldsValidator.getMobileNumber(mEtEmail.getEmailId());
        }
        createAccountPresenter.registerUserInfo(mUser, mEtName.getName().toString(), mEmail
                , mEtPassword.getPassword().toString(), true, mCbMarketingOpt.isChecked());
    }

    private String mEmail;

    private ClickableSpan mTermsAndConditionClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().getUserRegistrationUIEventListener().
                    onTermsAndConditionClick(getRegistrationFragment().getParentActivity());
        }
    };

    private ClickableSpan mPhilipsNewsClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().addPhilipsNewsFragment();
            trackPage(AppTaggingPages.PHILIPS_ANNOUNCEMENT);
        }
    };

    @Override
    public void trackCheckMarketing() {
        trackRemarketing();
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (mCbAcceptTerms.isChecked()) {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            } else {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
            }
        }
    }


    private void trackRemarketing() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        if (!abTestingUIFlow.equals(UIFlow.FLOW_B)) {
            if (mCbMarketingOpt.isChecked()) {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
            } else {
                trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
            }
        }
    }

    private void showSpinner() {
        mPbSpinner.setVisibility(View.VISIBLE);
        mBtnCreateAccount.setEnabled(false);
    }

    @Override
    public void hideSpinner() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                mPbSpinner.setVisibility(View.INVISIBLE);
                mBtnCreateAccount.setEnabled(true);
            }
        });
    }

    @Override
    public void storePref() {
        RegPreferenceUtility.storePreference(mContext, mEmail, true);
    }

    @Override
    public void emailAlreadyUsed() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                mEtEmail.showInvalidAlert();
                mEtEmail.showErrPopUp();
                mPasswordHintView.setVisibility(View.GONE);
                mTvEmailExist.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void regFail() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                mPbSpinner.setVisibility(View.INVISIBLE);
                mBtnCreateAccount.setEnabled(false);
            }
        });
    }

    @Override
    public void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();

        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    private void handleUiAcceptTerms() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            mLlAcceptTermsContainer.setVisibility(View.VISIBLE);
            switch (abTestingUIFlow) {
                case FLOW_A:
                    RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                    mViewLine.setVisibility(View.VISIBLE);
                    break;
                case FLOW_B:
                    RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                    mViewLine.setVisibility(View.GONE);
                    break;
                case FLOW_C:
                    RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                    mViewLine.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        } else {
            mLlAcceptTermsContainer.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
    }


    @Override
    public void launchMarketingAccountFragment() {
        getRegistrationFragment().addFragment(new MarketingAccountFragment());
        trackPage(AppTaggingPages.MARKETING_OPT_IN);
    }

    @Override
    public void launchMobileVerifyCodeFragment() {
        getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        trackPage(AppTaggingPages.MOBILE_VERIFY_CODE);
    }

    @Override
    public void launchAccountActivateFragment() {
        getRegistrationFragment().addFragment(new AccountActivationFragment());
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    @Override
    public void launchWelcomeFragment() {
        getRegistrationFragment().replaceWelcomeFragmentOnLogin(new WelcomeFragment());
        trackPage(AppTaggingPages.WELCOME);
    }


    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }

    @Override
    public void onUpdate() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus();
            }
        });
    }

    @Override
    public void updateUiStatus() {
        if (mTvEmailExist.getVisibility() == View.VISIBLE) {
            mTvEmailExist.setVisibility(View.GONE);
        }
        if (mPasswordHintView.getVisibility() != View.VISIBLE) {
            mPasswordHintView.setVisibility(View.VISIBLE);
        }
        mPasswordHintView.updateValidationStatus(mEtPassword.getPassword());
        if (mEtName.isValidName() && mEtEmail.isValidEmail() && mEtPassword.isValidPassword()
                && networkUtility.isNetworkAvailable()) {
            mBtnCreateAccount.setEnabled(true);
            mRegError.hideError();
        } else {
            mBtnCreateAccount.setEnabled(false);
        }
    }


    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        int id = mCbAcceptTerms.getId();
        if (id == R.id.cb_reg_accept_terms) {
            if (isChecked) {
                mRegAccptTermsError.setVisibility(View.GONE);
            } else {
                mRegAccptTermsError.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
    }

    @Override
    public void emailError(int errorDesc) {
        mEtEmail.setErrDescription(mContext.getResources().getString(errorDesc));
    }

    @Override
    public void emailError(String errorDesc) {
        mEtEmail.setErrDescription(errorDesc);
    }

    @Override
    public String getEmail() {
        return mUser.getEmail();
    }

    @Override
    public long getTrackCreateAccountTime() {
        return mTrackCreateAccountTime;
    }

    @Override
    public void setTrackCreateAccountTime(long trackCreateAccountTime) {
        mTrackCreateAccountTime = trackCreateAccountTime;
    }

    @Override
    public void tractCreateActionStatus(String state, String key, String value) {
        trackActionStatus(state, key, value);
    }

    @Override
    public void scrollViewAutomaticallyToEmail() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                scrollViewAutomatically(mEtEmail, mSvRootLayout);
            }
        });

    }

    @Override
    public void scrollViewAutomaticallyToError() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                scrollViewAutomatically(mRegError, mSvRootLayout);
            }
        });

    }
}

