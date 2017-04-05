
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.social;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.B;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTaggingPages;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.ui.customviews.LoginIdEditText;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.customviews.XButton;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.traditional.AccountActivationFragment;
import com.philips.cdp.registration.ui.traditional.MarketingAccountFragment;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.traditional.mobile.MobileVerifyCodeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegPreferenceUtility;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;

import butterfork.Bind;
import butterfork.ButterFork;
import butterfork.OnClick;

public class AlmostDoneFragment extends RegistrationBaseFragment implements AlmostDoneContract,
        OnUpdateListener, XCheckBox.OnCheckedChangeListener {

    @Bind(B.id.tv_reg_sign_in_with)
    TextView signInWithTextView;

    @Bind(B.id.ll_reg_almost_done)
    LinearLayout almostDoneContainer;

    @Bind(B.id.fl_reg_receive_philips_news)
    FrameLayout periodicOffersCheck;

    @Bind(B.id.ll_reg_accept_terms)
    LinearLayout acceptTermsContainer;

    @Bind(B.id.cb_reg_accept_terms)
    XCheckBox acceptTermsCheck;

    @Bind(B.id.cb_reg_accept_terms_error)
    XRegError errorMessage;

    @Bind(B.id.rl_reg_btn_continue_container)
    RelativeLayout continueBtnContainer;

    @Bind(B.id.cb_reg_receive_philips_news)
    XCheckBox remarketingOptCheck;

    @Bind(B.id.reg_error_msg)
    XRegError mRegError;

    @Bind(B.id.rl_reg_email_field)
    LoginIdEditText mEtEmail;

    @Bind(B.id.reg_btn_continue)
    XButton mBtnContinue;

    @Bind(B.id.pb_reg_marketing_opt_spinner)
    ProgressBar mPbSpinner;

    @Bind(B.id.sv_root_layout)
    ScrollView mSvRootLayout;

    @Bind(B.id.tv_join_now)
    TextView mJoinNow;

    @Bind(B.id.reg_view_accep_terms_line)
    View mViewAcceptTermsLine;

    @Bind(B.id.tv_reg_accept_terms)
    TextView acceptTermsView;

    @Bind(B.id.tv_reg_first_to_know)
    TextView mTvFirstToKnow;

    @Bind(B.id.tv_reg_philips_news)
    TextView receivePhilipsNewsView;

    @Bind(B.id.reg_view_line)
    View fieldViewLine;

    @Bind(B.id.reg_recieve_email_line)
    View receivePhilipsNewsLineView;

    private AlmostDonePresenter almostDonePresenter;

    private Context mContext;

    private boolean isSavedCBTermsChecked;

    private boolean isSavedCbAcceptTermsChecked;

    private boolean isTermsAndConditionVisible;

    private Bundle mBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onCreateView");
        mBundle = getArguments();
        almostDonePresenter = new AlmostDonePresenter(this);
        RLog.i(RLog.EVENT_LISTENERS,
                "AlmostDoneFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        View view = inflater.inflate(R.layout.reg_fragment_social_almost_done, container, false);
        ButterFork.bind(this, view);
        initUI(view);
        almostDonePresenter.parseRegistrationInfo(mBundle);
        almostDonePresenter.updateUIStatus();
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDestroy");
        RLog.i(RLog.EVENT_LISTENERS,
                "AlmostDoneFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
        almostDonePresenter.cleanUp();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onDetach");
    }

    private Bundle mSavedBundle;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mSavedBundle = outState;
        super.onSaveInstanceState(mSavedBundle);
        if (acceptTermsCheck != null) {
            if (remarketingOptCheck.isChecked()) {
                isSavedCBTermsChecked = true;
                mSavedBundle.putBoolean("isSavedCBTermsChecked", isSavedCBTermsChecked);
                mSavedBundle.putString("savedCBTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
        if (acceptTermsCheck != null) {
            if (acceptTermsCheck.isChecked()) {
                isSavedCbAcceptTermsChecked = true;
                mSavedBundle.putBoolean("isSavedCbAcceptTermsChecked", isSavedCbAcceptTermsChecked);
                mSavedBundle.putString("savedCbAcceptTerms", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
        if (errorMessage != null) {
            if (errorMessage.getVisibility() == View.VISIBLE) {
                isTermsAndConditionVisible = true;
                mSavedBundle.putBoolean("isTermsAndConditionVisible", isTermsAndConditionVisible);
                mSavedBundle.putString("saveTermsAndConditionErrText", mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isSavedCBTermsChecked")) {
                remarketingOptCheck.setChecked(true);
            }
            if (savedInstanceState.getBoolean("isSavedCbAcceptTermsChecked")) {
                acceptTermsCheck.setChecked(true);
            }
            if (savedInstanceState.getString("saveTermsAndConditionErrText") != null && savedInstanceState.getBoolean("isTermsAndConditionVisible")) {
                errorMessage.setError(savedInstanceState.getString("saveTermsAndConditionErrText"));
            }
        }
        mSavedBundle = null;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "AlmostDoneFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, signInWithTextView, width);
        applyParams(config, almostDoneContainer, width);
        applyParams(config, periodicOffersCheck, width);
        applyParams(config, continueBtnContainer, width);
        applyParams(config, mRegError, width);
        applyParams(config, errorMessage, width);
        applyParams(config, acceptTermsContainer, width);
        applyParams(config, mTvFirstToKnow, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        remarketingOptCheck.setPadding(RegUtility.getCheckBoxPadding(mContext), remarketingOptCheck.getPaddingTop(),
                remarketingOptCheck.getPaddingRight(), remarketingOptCheck.getPaddingBottom());
        RegUtility.linkifyTermsandCondition(acceptTermsView, getRegistrationFragment().getParentActivity(), mTermsAndConditionClick);

        RegUtility.linkifyPhilipsNews(receivePhilipsNewsView, getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        String sourceString = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now) ;
        String updateJoinNowText =  " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        sourceString = String.format(sourceString, updateJoinNowText);
        mJoinNow.setText(Html.fromHtml(sourceString));

        remarketingOptCheck.setOnCheckedChangeListener(this);
        mEtEmail.setOnUpdateListener(this);
        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);
        handleUiAcceptTerms();
    }

    @Override
    public void enableBtnContinue(){
        mBtnContinue.setEnabled(true);
    }
    @Override
    public void emailFieldHide() {
        mEtEmail.setVisibility(View.GONE);
        mBtnContinue.setEnabled(true);
    }

    @Override
    public void showEmailField() {
        fieldViewLine.setVisibility(View.VISIBLE);
        mEtEmail.setVisibility(View.VISIBLE);
    }

    private ClickableSpan mTermsAndConditionClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().getUserRegistrationUIEventListener()
                    .onTermsAndConditionClick(getRegistrationFragment().getParentActivity());
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
    public void handleUiAcceptTerms() {
        mJoinNow.setVisibility(View.GONE);
        almostDonePresenter.handleAcceptTermsAndReceiveMarketingOpt();
    }

    @Override
    public void hideAcceptTermsView() {
        mViewAcceptTermsLine.setVisibility(View.GONE);
        acceptTermsContainer.setVisibility(View.GONE);
    }

    @Override
    public void updateTermsAndConditionView() {
        if(!almostDonePresenter.isReceiveMarketingEmailOpt()){
            periodicOffersCheck.setVisibility(View.VISIBLE);
        }else{
            periodicOffersCheck.setVisibility(View.GONE);
        }
        acceptTermsCheck.setChecked(true);
        fieldViewLine.setVisibility(View.GONE);
        mJoinNow.setVisibility(View.GONE);
        acceptTermsContainer.setVisibility(View.GONE);
    }

    @Override
    public void updateReceiveMarktingView() {
        periodicOffersCheck.setVisibility(View.GONE);
        fieldViewLine.setVisibility(View.GONE);
        mJoinNow.setVisibility(View.GONE);
        mViewAcceptTermsLine.setVisibility(View.GONE);
    }

    @Override
    public void updateABTestingUIFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();

        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                acceptTermsContainer.setVisibility(View.VISIBLE);
                mJoinNow.setVisibility(View.GONE);
                break;

            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                acceptTermsContainer.setVisibility(View.VISIBLE);
                periodicOffersCheck.setVisibility(View.GONE);
                receivePhilipsNewsLineView.setVisibility(View.GONE);
                mJoinNow.setVisibility(View.GONE);
                break;

            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                String firstToKnow = "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Be_The_First) + "</b> ";
                mTvFirstToKnow.setText(Html.fromHtml(firstToKnow));
                mTvFirstToKnow.setVisibility(View.VISIBLE);
                acceptTermsContainer.setVisibility(View.VISIBLE);
                mJoinNow.setVisibility(View.VISIBLE);
                break;
            default:break;

        }

    }

    @Override
    public void validateEmailFieldUI() {
        if ((mEtEmail.isShown() && mEtEmail.isValidEmail()) ||
                (mEtEmail.getVisibility() != View.VISIBLE)) {
            mBtnContinue.setEnabled(true);
        }
        mRegError.hideError();
    }

    @Override
    public void enableContinueBtn() {
        mBtnContinue.setEnabled(true);
        mRegError.hideError();
    }

    @Override
    public void handleOfflineMode() {
        mRegError.setError(getString(R.string.reg_NoNetworkConnection));
        mBtnContinue.setEnabled(false);
        scrollViewAutomatically(mRegError, mSvRootLayout);
    }

    @Override
    public void showMarketingOptSpinner() {
        remarketingOptCheck.setEnabled(false);
        mPbSpinner.setVisibility(View.VISIBLE);
        mBtnContinue.setEnabled(false);
    }

    @Override
    public void hideMarketingOptSpinner() {
        remarketingOptCheck.setEnabled(true);
        mPbSpinner.setVisibility(View.INVISIBLE);
        mBtnContinue.setEnabled(true);
    }

    @OnClick(B.id.reg_btn_continue)
    public void continueButtonClicked() {
        RLog.d(RLog.ONCLICK, "AlmostDoneFragment : Continue");
        mEtEmail.clearFocus();
        if (mEtEmail.isShown() && !mEtEmail.isValidEmail()) return;
        if (mBundle == null) {
            handleTraditionalTermsAndCondition();
            return;
        }
        handleSocialTermsAndCondition();
    }

    private void handleSocialTermsAndCondition() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() && acceptTermsContainer.getVisibility() == View.VISIBLE) {
            if (acceptTermsCheck.isChecked()) {
                almostDonePresenter.register(remarketingOptCheck.isChecked(), FieldsValidator.getMobileNumber(mEtEmail.getEmailId().trim()));
            } else {
                errorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        } else {
            almostDonePresenter.register(remarketingOptCheck.isChecked(),FieldsValidator.getMobileNumber(mEtEmail.getEmailId().trim()));
        }
    }

    private void handleTraditionalTermsAndCondition() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (acceptTermsCheck.isChecked()) {
                almostDonePresenter.storeEmailOrMobileInPreference();
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
                launchWelcomeFragment();
            } else {
                errorMessage.setError(mContext.getResources().getString(R.string.reg_TermsAndConditionsAcceptanceText_Error));
            }
        } else {
            launchWelcomeFragment();
        }
    }

    @Override
    public void storePreference(String emailOrMobileNumber){
        RegPreferenceUtility.storePreference(mContext, emailOrMobileNumber, true);
    }


    private void trackMultipleActions() {
        trackABTestingUIFlow();
        trackTermsAndConditionAccepted();
    }

    private void trackTermsAndConditionAccepted() {
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            if (acceptTermsCheck.isChecked()) {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_IN);
            } else {
                trackActionForAcceptTermsOption(AppTagingConstants.ACCEPT_TERMS_OPTION_OUT);
            }
        }
    }

    private void trackABTestingUIFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        if (!abTestingUIFlow.equals(UIFlow.FLOW_B)) {
            trackMarketingOpt();
        }
    }

    @Override
    public void trackMarketingOpt() {
        if (remarketingOptCheck.isChecked()) {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
        } else {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    @Override
    public void phoneNumberAlreadyInuseError(){
        mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_CreateAccount_Using_Phone_Alreadytxt));
    }

    @Override
    public void emailAlreadyInuseError(){
        mEtEmail.setErrDescription(mContext.getResources().getString(R.string.reg_EmailAlreadyUsed_TxtFieldErrorAlertMsg));
    }

    @Override
    public void showLoginFailedError() {
        mEtEmail.showInvalidAlert();
        mEtEmail.showErrPopUp();
        scrollViewAutomatically(mEtEmail, mSvRootLayout);
    }


    @Override
    public void addMergeAccountFragment() {
        getRegistrationFragment().addFragment(new MergeAccountFragment());
        trackPage(AppTaggingPages.MERGE_ACCOUNT);
    }

    @Override
    public void handleContinueSocialProvider() {
        RLog.i(RLog.CALLBACK, "AlmostDoneFragment : onContinueSocialProviderLoginSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SUCCESS_USER_CREATION);
        trackMultipleActions();
        handleABTestingFlow();
        hideMarketingOptSpinner();
    }

    private void handleABTestingFlow() {
        final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
        switch (abTestingUIFlow){
            case FLOW_A:
                RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                if (almostDonePresenter.isEmailVerificationStatus()) {
                    launchWelcomeFragment();
                } else {
                    launchAccountActivateFragment();
                }
                break;
            case FLOW_B:
                RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                launchMarketingAccountFragment();
                break;
            case FLOW_C:
                RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                if (almostDonePresenter.isEmailVerificationStatus()) {
                    launchWelcomeFragment();
                } else {
                    launchAccountActivateFragment();
                }
                break;
            default:break;
        }
    }

    public void clearUserData() {
        if (null != acceptTermsCheck && !acceptTermsCheck.isChecked() &&
                RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            almostDonePresenter.handleClearUserData();
        }
    }

    private void launchMarketingAccountFragment() {
        getRegistrationFragment().addFragment(new MarketingAccountFragment());
        trackPage(AppTaggingPages.MARKETING_OPT_IN);
    }

    private void launchAccountActivateFragment() {
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
        if (almostDonePresenter.isValidEmail()) {
            getRegistrationFragment().addFragment(new AccountActivationFragment());
        } else {
            getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        }
    }


    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void displayNameErrorMessage(UserRegistrationFailureInfo userRegistrationFailureInfo,String displayName){
        mEtEmail.setErrDescription(userRegistrationFailureInfo.getDisplayNameErrorMessage());
        mEtEmail.showInvalidAlert();
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription() + ".\n'"
                + displayName + "' "
                + userRegistrationFailureInfo.getDisplayNameErrorMessage());
    }

    @Override
    public void emailErrorMessage(UserRegistrationFailureInfo userRegistrationFailureInfo){
        mEtEmail.setErrDescription(userRegistrationFailureInfo.getEmailErrorMessage());
        mEtEmail.showInvalidAlert();
        mEtEmail.showErrPopUp();
    }

    private void handleUpdate() {
        if (almostDonePresenter.isNetworkAvailable()) {
            mRegError.hideError();
            showMarketingOptSpinner();
            almostDonePresenter.updateUser(remarketingOptCheck.isChecked());
        } else {
            remarketingOptCheck.setOnCheckedChangeListener(null);
            remarketingOptCheck.setChecked(!remarketingOptCheck.isChecked());
            remarketingOptCheck.setOnCheckedChangeListener(this);
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
        }
    }

    @Override
    public void onCheckedChanged(View view, boolean isChecked) {
        handleUpdate();
    }

    @Override
    public void failedToConnectToServer(){
        mRegError.setError(mContext.getResources().getString(R.string.reg_JanRain_Server_Connection_Failed));
    }

    @Override
    public void replaceWithHomeFragment() {
        if (getRegistrationFragment() != null) {
            getRegistrationFragment().replaceWithHomeFragment();
        }
    }

    @Override
    public void onUpdate() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                almostDonePresenter.updateUIStatus();
            }
        });
    }

    @Override
    public void updateMarketingOptFailedError(){
        remarketingOptCheck.setOnCheckedChangeListener(null);
        remarketingOptCheck.setChecked(!remarketingOptCheck.isChecked());
        remarketingOptCheck.setOnCheckedChangeListener(AlmostDoneFragment.this);
    }

    @Override
    public void hideErrorMessage(){
        errorMessage.setVisibility(View.GONE);
    }

    public boolean getPreferenceStoredState(String emailOrMobileNumber){
        return RegPreferenceUtility.getStoredState(mContext, emailOrMobileNumber);
    }

}
