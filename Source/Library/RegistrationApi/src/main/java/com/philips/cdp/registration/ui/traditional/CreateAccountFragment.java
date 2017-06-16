
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

public class CreateAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalRegistrationHandler, OnUpdateListener, NetworStateListener, EventListener, XCheckBox.OnCheckedChangeListener {

    @Inject
    NetworkUtility networkUtility;

    private static final int FAILURE_TO_CONNECT = -1;

    private LinearLayout mLlCreateAccountFields;

    private LinearLayout mLlCreateAccountContainer;

    private LinearLayout mLlAcceptTermsContainer;

    private RelativeLayout mRlCreateActtBtnContainer;

    private Button mBtnCreateAccount;

    private XCheckBox mCbMarketingOpt;

    private XCheckBox mCbAcceptTerms;

    private User mUser;

    private XUserName mEtName;

    private LoginIdEditText mEtEmail;

    private PasswordView mEtPassword;

    private XRegError mRegError;

    private XRegError mRegAccptTermsError;

    private ProgressBar mPbSpinner;

    private View mViewLine;

    private Context mContext;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private ScrollView mSvRootLayout;

    private XPasswordHint mPasswordHintView;

    private TextView mTvEmailExist;

    private TextView mJoinnow;

    private long mTrackCreateAccountTime;

    private boolean isTermsAndConditionVisible;

    private boolean isSavedEmailErr;

    private boolean isSavedCBTermsChecked;

    private boolean isSavedCbAcceptTermsChecked;

    private boolean isSavedPasswordErr;

    private TextView mTvFirstToKnow;

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

        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        View view = inflater.inflate(R.layout.reg_fragment_create_account, container, false);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);

        initUI(view);
        handleOrientation(view);
        mTrackCreateAccountTime = System.currentTimeMillis();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
                this);
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDetach");
    }

    private Bundle mBundle;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        mBundle = outState;
        super.onSaveInstanceState(mBundle);
        if(mEtEmail.isEmailErrorVisible()){
            isSavedEmailErr = true;
            mBundle.putBoolean("isSavedEmailErr", isSavedEmailErr);
            mBundle.putString("saveEmailErrText", mEtEmail.getSavedEmailErrDescription());
        }
        if(mEtPassword.isPasswordErrorVisible()){
            isSavedPasswordErr = true;
            mBundle.putBoolean("isSavedPasswordErr", isSavedPasswordErr);
            mBundle.putString("savedPasswordErr", mEtPassword.getmSavedPasswordErrDescription());
        }
        if(mRegAccptTermsError.getVisibility() == View.VISIBLE){
            isTermsAndConditionVisible = true;
            mBundle.putBoolean("isTermsAndConditionVisible", isTermsAndConditionVisible);
            mBundle.putString("saveTermsAndConditionErrText", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
        }
        if(mCbMarketingOpt.isChecked()){
            isSavedCBTermsChecked = true;
            mBundle.putBoolean("isSavedCBTermsChecked", isSavedCBTermsChecked);
            mBundle.putString("savedCBTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
        }
        if(mCbAcceptTerms.isChecked()){
            isSavedCbAcceptTermsChecked = true;
            mBundle.putBoolean("isSavedCbAcceptTermsChecked", isSavedCbAcceptTermsChecked);
            mBundle.putString("savedCbAcceptTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            if(savedInstanceState.getString("saveEmailErrText")!=null && savedInstanceState.getBoolean("isSavedEmailErr")){
                mEtEmail.setErrDescription(savedInstanceState.getString("saveEmailErrText"));
                mEtEmail.showInvalidAlert();
                mEtEmail.showErrPopUp();
            }
            if(savedInstanceState.getString("savedPasswordErr")!=null && savedInstanceState.getBoolean("isSavedPasswordErr")){
                mEtPassword.setErrDescription(savedInstanceState.getString("savedPasswordErr"));
                mEtPassword.showInvalidPasswordAlert();
            }
            if(savedInstanceState.getString("saveTermsAndConditionErrText")!=null && savedInstanceState.getBoolean("isTermsAndConditionVisible")){
                mRegAccptTermsError.setError(savedInstanceState.getString("saveTermsAndConditionErrText"));
            }
            if(savedInstanceState.getBoolean("isSavedCBTermsChecked")){
                mCbMarketingOpt.setChecked(true);
            }
            if(savedInstanceState.getBoolean("isSavedCbAcceptTermsChecked")){
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reg_register) {
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
    }

    private void initUI(View view) {
        consumeTouch(view);

        mPasswordHintView = (XPasswordHint) view.findViewById(R.id.view_reg_password_hint);

        mLlCreateAccountFields = (LinearLayout) view
                .findViewById(R.id.ll_reg_create_account_fields);
        mLlCreateAccountContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_create_account_container);
        mJoinnow = (TextView) view.findViewById(R.id.tv_join_now);
        mTvFirstToKnow = (TextView)view.findViewById(R.id.tv_reg_first_to_know);

        handleABTestingFlow();

        mLlAcceptTermsContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_accept_terms);
        mRlCreateActtBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_singin_options);

        mBtnCreateAccount = (Button) view.findViewById(R.id.btn_reg_register);
        mCbMarketingOpt = (XCheckBox) view.findViewById(R.id.cb_reg_register_terms);
        mCbMarketingOpt.setOnClickListener(this);
        mCbAcceptTerms = (XCheckBox) view.findViewById(R.id.cb_reg_accept_terms);
        mCbAcceptTerms.setOnClickListener(this);
        mTvEmailExist = (TextView) view.findViewById(R.id.tv_reg_email_exist);
        TextView acceptTermsView = (TextView) view.findViewById(R.id.tv_reg_accept_terms);
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);

        TextView receivePhilipsNewsView = (TextView) view.findViewById(R.id.tv_reg_philips_news);
        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);

        String sourceString = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now) ;
        String updateJoinNowText =  " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        sourceString = String.format(sourceString, updateJoinNowText);
        mJoinnow.setText(Html.fromHtml(sourceString));

        String firstToKnow = "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Be_The_First) + "</b> ";
        mTvFirstToKnow.setText(Html.fromHtml(firstToKnow));

        mCbAcceptTerms.setOnCheckedChangeListener(this);
        mBtnCreateAccount.setOnClickListener(this);
        mEtName = (XUserName) view.findViewById(R.id.rl_reg_name_field);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtName.requestFocus();
        mEtName.setOnUpdateListener(this);
        mEtEmail = (LoginIdEditText) view.findViewById(R.id.rl_reg_email_field);
        mEtEmail.setOnUpdateListener(this);
        mEtPassword = (PasswordView) view.findViewById(R.id.rl_reg_password_field);
        mEtPassword.setOnUpdateListener(this);
        mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_activate_spinner);
        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);
        mViewLine = view.findViewById(R.id.reg_accept_terms_line);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mRegAccptTermsError = (XRegError) view.findViewById(R.id.cb_reg_accept_terms_error);
        mEtPassword.setHint(mContext.getResources().getString(R.string.reg_Create_Account_ChoosePwd_PlaceHolder_txtField));
        handleUiAcceptTerms();
        handleUiState();
        mUser = new User(mContext);
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow){

            case FLOW_A:
                RLog.d(RLog.AB_TESTING,"UI Flow Type A");
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
                RLog.d(RLog.AB_TESTING,"UI Flow Type C");
                mLlCreateAccountContainer.setVisibility(View.VISIBLE);
                mJoinnow.setVisibility(View.VISIBLE);
                mTvFirstToKnow.setVisibility(View.VISIBLE);
                trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.AB_TEST,
                        AppTagingConstants.REGISTRATION_SOCIAL_PROOF);
                break;

            default:break;
        }
    }

    private void register() {
        mRegAccptTermsError.setVisibility(View.GONE);
        mEtName.clearFocus();
        mEtEmail.clearFocus();
        mEtPassword.clearFocus();
        showSpinner();
        if(FieldsValidator.isValidEmail(mEtEmail.getEmailId())){
            mEmail = mEtEmail.getEmailId();
        }else{
            mEmail = FieldsValidator.getMobileNumber(mEtEmail.getEmailId());
        }
        mUser.registerUserInfoForTraditional(mEtName.getName().toString(), mEmail
                , mEtPassword.getPassword().toString(), true, mCbMarketingOpt.isChecked(), this);
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

    private void trackCheckMarketing() {
        trackRemarketing();
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if(mCbAcceptTerms.isChecked()){
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            }else{
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

    private void hideSpinner() {
        mPbSpinner.setVisibility(View.INVISIBLE);
        mBtnCreateAccount.setEnabled(true);
    }

    private void handleUiState() {
        if (networkUtility.isNetworkAvailable()) {
            mRegError.hideError();

        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    private void handleUiAcceptTerms() {
        final UIFlow abTestingUIFlow=RegUtility.getUiFlow();
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            mLlAcceptTermsContainer.setVisibility(View.VISIBLE);
            switch (abTestingUIFlow){
                case FLOW_A:
                    RLog.d(RLog.AB_TESTING,"UI Flow Type A");
                    mViewLine.setVisibility(View.VISIBLE);
                    break;
                case FLOW_B:
                    RLog.d(RLog.AB_TESTING,"UI Flow Type B");
                    mViewLine.setVisibility(View.GONE);
                    break;
                case FLOW_C:
                    RLog.d(RLog.AB_TESTING,"UI Flow Type C");
                    mViewLine.setVisibility(View.VISIBLE);
                    break;
                default:break;
            }
        } else {
            mLlAcceptTermsContainer.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRegisterSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleRegistrationSuccess();
            }
        });
    }

    private void handleRegistrationSuccess() {
        RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterSuccess");
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            RegPreferenceUtility.storePreference(mContext, mEmail, true);
        }
        hideSpinner();
        trackCheckMarketing();
        final UIFlow abTestingUIFlow=RegUtility.getUiFlow();
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_USER_CREATION);

        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A ");
                if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
                    if (FieldsValidator.isValidEmail(mUser.getEmail().toString())) {
                        launchAccountActivateFragment();
                    } else {
                        launchMobileVerifyCodeFragment();
                    }
                } else {
                    launchWelcomeFragment();
                }
                break;
            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                launchMarketingAccountFragment();
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type  C");
                if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
                    if (FieldsValidator.isValidEmail(mUser.getEmail().toString())) {
                        launchAccountActivateFragment();
                    } else {
                        launchMobileVerifyCodeFragment();
                    }
                } else {
                    launchWelcomeFragment();
                }
                break;
        }

        if(mTrackCreateAccountTime == 0 && RegUtility.getCreateAccountStartTime() > 0){
            mTrackCreateAccountTime =  (System.currentTimeMillis() - RegUtility.getCreateAccountStartTime())/1000;
        }else{
            mTrackCreateAccountTime =  (System.currentTimeMillis() - mTrackCreateAccountTime)/1000;
        }
        trackActionStatus(AppTagingConstants.SEND_DATA,AppTagingConstants.TOTAL_TIME_CREATE_ACCOUNT,String.valueOf(mTrackCreateAccountTime));
        mTrackCreateAccountTime =0;
    }

    private void launchMarketingAccountFragment() {
        getRegistrationFragment().addFragment(new MarketingAccountFragment());
        trackPage(AppTaggingPages.MARKETING_OPT_IN);
    }

    private void launchMobileVerifyCodeFragment() {
        getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        trackPage(AppTaggingPages.MOBILE_VERIFY_CODE);
    }

    private void launchAccountActivateFragment() {
        getRegistrationFragment().addFragment(new AccountActivationFragment());
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    private void launchWelcomeFragment() {
        getRegistrationFragment().replaceWelcomeFragmentOnLogin(new WelcomeFragment());
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void onRegisterFailedWithFailure(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleRegisterFailedWithFailure(userRegistrationFailureInfo);
            }
        });
    }

    private void handleRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterFailedWithFailure");

        if (userRegistrationFailureInfo.getErrorCode() == EMAIL_ADDRESS_ALREADY_USE_CODE) {
            if (RegistrationHelper.getInstance().isChinaFlow()){
                mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_CreateAccount_Using_Phone_Alreadytxt));
            }else {
                mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
            }
            mEtEmail.showInvalidAlert();
            mEtEmail.showErrPopUp();
            scrollViewAutomatically(mEtEmail, mSvRootLayout);
            mPasswordHintView.setVisibility(View.GONE);
            mTvEmailExist.setVisibility(View.VISIBLE);
        }
        if (userRegistrationFailureInfo.getErrorCode() != EMAIL_ADDRESS_ALREADY_USE_CODE) {
            mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
        if(userRegistrationFailureInfo.getErrorCode() == FAILURE_TO_CONNECT){
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
        }
        AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo,AppTagingConstants.JANRAIN);
        mPbSpinner.setVisibility(View.INVISIBLE);
        mBtnCreateAccount.setEnabled(false);
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

    private void updateUiStatus() {
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
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "CreateAccoutFragment :onCounterEventReceived : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            updateUiStatus();
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "CreateAccoutFragment :onNetWorkStateReceived : " + isOnline);
        handleUiState();
        updateUiStatus();
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
}

