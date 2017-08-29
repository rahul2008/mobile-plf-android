
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.social;

import android.content.*;
import android.content.res.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.*;

import javax.inject.*;

import butterknife.*;

public class MergeAccountFragment extends RegistrationBaseFragment implements EventListener,
        OnUpdateListener, TraditionalLoginHandler, NetworStateListener,
        OnClickListener {

    @Inject
    NetworkUtility networkUtility;


    private XRegError mRegError;


    private String mEmailId;

    private String mMergeToken;

    private User mUser;

    private Context mContext;

    private TextView mTvUsedEmail;

    private ScrollView mSvRootLayout;

    @BindView(R2.id.usr_mergeScreen_merge_button)
    ProgressBarButton mBtnMerge;

    @BindView(R2.id.usr_mergeScreen_forgotPassword_button)
    Button mBtnForgotPassword;

    @BindView(R2.id.usr_mergeScreen_password_inputLayout)
    InputValidationLayout mEtPassword;


    @BindView(R2.id.usr_mergeScreen_password_textField)
    ValidationEditText passwordValidationEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.reg_fragment_social_merge_account, container, false);
        ButterKnife.bind(this, view);
        RLog.i(RLog.EVENT_LISTENERS,
                "MergeAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        mUser = new User(mContext);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.usr_mergeScreen_rootLayout_scrollView);
        initUI(view);
        handleUiErrorState();
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
                this);
        RLog.i(RLog.EVENT_LISTENERS,
                "MergeAccountFragment unregister: JANRAIN_INIT_SUCCESS,NetworStateListener");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeAccountFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    private void initUI(View view) {
        consumeTouch(view);
        Bundle bundle = this.getArguments();
        mBtnMerge.setOnClickListener(this);

        mEmailId = bundle.getString(RegConstants.SOCIAL_MERGE_EMAIL);

        mBtnForgotPassword.setOnClickListener(this);


        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtPassword.requestFocus();


        mEtPassword.setOnClickListener(this);
        mEtPassword.setValidator(password -> password.length() > 0);
        mEtPassword.setErrorMessage(getString(R.string.reg_EmptyField_ErrorMsg));


        mTvUsedEmail = (TextView) view.findViewById(R.id.usr_mergeScreen_used_email_label);

        mMergeToken = bundle.getString(RegConstants.SOCIAL_MERGE_TOKEN);


        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.START_SOCIAL_MERGE);

        String usedEmail = getString(R.string.reg_Account_Merge_UsedEmail_Error_lbltxt);
        usedEmail = String.format(usedEmail, mEmailId);
        mTvUsedEmail.setText(usedEmail);


        passwordValidationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>1){
                    mBtnMerge.setEnabled(true);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.usr_mergeScreen_merge_button) {
            RLog.d(RLog.ONCLICK, "MergeAccountFragment : Merge");
            if (mEtPassword.hasFocus()) {
                mEtPassword.clearFocus();
            }
            getView().requestFocus();
            mergeAccount();

        } else if (v.getId() == R.id.usr_mergeScreen_forgotPassword_button) {
            RLog.d(RLog.ONCLICK, "MergeAccountFragment : Forgot Password");
            resetPassword();
        }
    }

    private void mergeAccount() {
        if (networkUtility.isNetworkAvailable()) {
            mUser.mergeToTraditionalAccount(mEmailId, passwordValidationEditText.getText().toString(), mMergeToken, this);
            showMergeSpinner();
        } else {
            mRegError.setError(getString(R.string.reg_JanRain_Error_Check_Internet));
        }
    }

    private void resetPassword() {
        boolean validatorResult = FieldsValidator.isValidEmail(mEmailId);
        if (validatorResult) {

            if (networkUtility.isNetworkAvailable()) {
                getRegistrationFragment().addResetPasswordFragment();
                trackPage(AppTaggingPages.FORGOT_PASSWORD);
            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        }
    }

    private void showMergeSpinner() {
        mBtnMerge.showProgressIndicator();
        mBtnMerge.setEnabled(false);
    }

    private void hideMergeSpinner() {
        mBtnMerge.hideProgressIndicator();
        mBtnMerge.setEnabled(true);
    }


    private void handleUiErrorState() {
        if (networkUtility.isNetworkAvailable()) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        } else {
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    private void updateUiStatus() {
        RLog.i("MergeAccountFragment", "updateUiStatus");
        if (FieldsValidator.isValidPassword(passwordValidationEditText.getText().toString())
                && networkUtility.isNetworkAvailable()
                && UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            mBtnMerge.setEnabled(true);
            mBtnForgotPassword.setEnabled(true);
            mRegError.hideError();
        } else {
            mBtnMerge.setEnabled(false);
        }
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "MergeAccountFragment :onCounterEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            updateUiStatus();
        }
    }

    @Override
    public void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onUpdate() {
        updateUiStatus();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    @Override
    public void onLoginSuccess() {
        RLog.i(RLog.CALLBACK, "MergeAccountFragment : onLoginSuccess");
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_SOCIAL_MERGE);
        hideMergeSpinner();
        launchWelcomeFragment();
    }

    private void launchAlmostDoneScreen() {
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
        trackPage(AppTaggingPages.ALMOST_DONE);
    }

    private void launchWelcomeFragment() {
        if (!mUser.getReceiveMarketingEmail()) {
            launchAlmostDoneScreen();
            return;
        }
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleLoginFailed(userRegistrationFailureInfo);
    }

    private void handleLoginFailed(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "MergeAccountFragment : onLoginFailedWithError");
        hideMergeSpinner();
        scrollViewAutomatically(mRegError, mSvRootLayout);
        if (userRegistrationFailureInfo.getErrorCode() == RegConstants.INVALID_CREDENTIALS_ERROR_CODE) {
            mRegError.setError(mContext.getString(R.string.reg_Merge_validate_password_mismatch_errortxt));
            return;
        }
        mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
    }


    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "MergeAccountFragment :onNetWorkStateReceived state :"
                + isOnline);
        handleUiErrorState();
        updateUiStatus();
    }


}
