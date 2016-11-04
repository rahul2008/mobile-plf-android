
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
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XEmail;
import com.philips.cdp.registration.ui.customviews.XPassword;
import com.philips.cdp.registration.ui.customviews.XPasswordHint;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.XUserName;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;

public class CreateAccountFragment extends RegistrationBaseFragment implements OnClickListener,
        TraditionalRegistrationHandler, onUpdateListener, NetworStateListener, EventListener, XCheckBox.OnCheckedChangeListener {

    private LinearLayout mLlCreateAccountFields;

    private LinearLayout mLlCreateAccountContainer;

    private LinearLayout mLlAcceptTermsContainer;

    private RelativeLayout mRlCreateActtBtnContainer;

    private Button mBtnCreateAccount;

    private XCheckBox mCbTerms;

    private XCheckBox mCbAcceptTerms;

    private User mUser;

    private XUserName mEtName;

    private XEmail mEtEmail;

    private XPassword mEtPassword;

    private XRegError mRegError;

    private XRegError mRegAccptTermsError;

    private ProgressBar mPbSpinner;

    private View mViewLine;

    private Context mContext;

    private final static int EMAIL_ADDRESS_ALREADY_USE_CODE = 390;

    private ScrollView mSvRootLayout;

    private XPasswordHint mPasswordHintView;

    private TextView mTvEmailExist;

    private long mTrackCreateAccountTime;

    private boolean isTermsAndConditionVisible;

    private boolean isSavedEmailErr;

    private boolean isSavedCBTermsChecked;

    private boolean isSavedCbAcceptTermsChecked;

    private boolean isSavedPasswordErr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if(mCbTerms.isChecked()){
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
                mCbTerms.setChecked(true);
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
        applyParams(config, mRegAccptTermsError, width);
        applyParams(config, mLlAcceptTermsContainer, width);
        applyParams(config, mPasswordHintView, width);
        applyParams(config, mTvEmailExist, width);
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

        if (RegUtility.isUiFirstFlow()){
            RLog.d(RLog.AB_TESTING,"UI Flow Type A");
            mLlCreateAccountContainer.setVisibility(View.VISIBLE);
        }else {
            RLog.d(RLog.AB_TESTING,"UI Flow Type B");
            mLlCreateAccountContainer.setVisibility(View.GONE);
        }
        mLlAcceptTermsContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_accept_terms);
        mRlCreateActtBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_singin_options);

        mBtnCreateAccount = (Button) view.findViewById(R.id.btn_reg_register);
        mCbTerms = (XCheckBox) view.findViewById(R.id.cb_reg_register_terms);
        mCbTerms.setOnClickListener(this);
        mCbAcceptTerms = (XCheckBox) view.findViewById(R.id.cb_reg_accept_terms);
        mCbAcceptTerms.setOnClickListener(this);
        mTvEmailExist = (TextView) view.findViewById(R.id.tv_reg_email_exist);
        TextView acceptTermsView = (TextView) view.findViewById(R.id.tv_reg_accept_terms);
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);

        TextView receivePhilipsNewsView = (TextView) view.findViewById(R.id.tv_reg_philips_news);
        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);

        mCbAcceptTerms.setOnCheckedChangeListener(this);
        mBtnCreateAccount.setOnClickListener(this);
        mEtName = (XUserName) view.findViewById(R.id.rl_reg_name_field);
        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtName.requestFocus();
        mEtName.setOnUpdateListener(this);
        mEtEmail = (XEmail) view.findViewById(R.id.rl_reg_email_field);
        mEtEmail.setOnUpdateListener(this);
        mEtPassword = (XPassword) view.findViewById(R.id.rl_reg_password_field);
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

    private void register() {
        mRegAccptTermsError.setVisibility(View.GONE);
        mEtName.clearFocus();
        mEtEmail.clearFocus();
        mEtPassword.clearFocus();
        showSpinner();
        mEmail = mEtEmail.getEmailId();
        mUser.registerUserInfoForTraditional(mEtName.getName().toString(), mEtEmail.getEmailId()
                .toString(), mEtPassword.getPassword().toString(), true, mCbTerms.isChecked(), this);
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
        if (mCbTerms.isChecked()) {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
        } else {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
        }
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if(mCbAcceptTerms.isChecked()){
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            }else{
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
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
        if (NetworkUtility.isNetworkAvailable(mContext)) {

            mRegError.hideError();

        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            trackActionRegisterError(AppTagingConstants.NETWORK_ERROR_CODE);
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    private void handleUiAcceptTerms() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            mLlAcceptTermsContainer.setVisibility(View.VISIBLE);
            if (RegUtility.isUiFirstFlow()){
                RLog.d(RLog.AB_TESTING,"UI Flow Type A");
                mViewLine.setVisibility(View.VISIBLE);
            }else {
                RLog.d(RLog.AB_TESTING,"UI Flow Type B");
                mViewLine.setVisibility(View.GONE);
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
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_USER_CREATION);
        if (RegUtility.isUiFirstFlow()){
            RLog.d(RLog.AB_TESTING,"UI Flow Type A");
            if (RegistrationConfiguration.getInstance().isEmailVerificationRequired()) {
                launchAccountActivateFragment();
            } else {
                launchWelcomeFragment();
            }
        }else {
            RLog.d(RLog.AB_TESTING,"UI Flow Type B");
            getRegistrationFragment().replaceWelcomeFragmentOnLogin(new MarketingAccountFragment());
        }
        if(mTrackCreateAccountTime == 0 && RegUtility.getCreateAccountStartTime() > 0){
            mTrackCreateAccountTime =  (System.currentTimeMillis() - RegUtility.getCreateAccountStartTime())/1000;
        }else{
            mTrackCreateAccountTime =  (System.currentTimeMillis() - mTrackCreateAccountTime)/1000;
        }
        trackActionStatus(AppTagingConstants.SEND_DATA,AppTagingConstants.TOTAL_TIME_CREATE_ACCOUNT,String.valueOf(mTrackCreateAccountTime));
        mTrackCreateAccountTime =0;
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
            mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
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
        if(userRegistrationFailureInfo.getErrorCode() == -1 ){
            mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
        }
        trackActionRegisterError(userRegistrationFailureInfo.getErrorCode());
        mPbSpinner.setVisibility(View.INVISIBLE);
        mBtnCreateAccount.setEnabled(false);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }

    @Override
    public void onUpadte() {
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
                && NetworkUtility.isNetworkAvailable(mContext)) {
            mBtnCreateAccount.setEnabled(true);
            mRegError.hideError();
        } else {
            mBtnCreateAccount.setEnabled(false);
        }
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "CreateAccoutFragment :onEventReceived : " + event);
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

