
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.social;

import android.content.res.Configuration;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.jakewharton.rxbinding2.widget.*;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import javax.inject.*;

import butterknife.*;

public class MergeAccountFragment extends RegistrationBaseFragment implements MergeAccountContract {

    @Inject
    NetworkUtility networkUtility;

    @BindView(R2.id.reg_error_msg)
    XRegError mRegError;

    @BindView(R2.id.usr_mergeScreen_used_email_label)
    Label mTvUsedEmail;

    @BindView(R2.id.usr_mergeScreen_rootLayout_scrollView)
    ScrollView mSvRootLayout;

    @BindView(R2.id.usr_mergeScreen_merge_button)
    ProgressBarButton mBtnMerge;

    @BindView(R2.id.usr_mergeScreen_password_inputLayout)
    InputValidationLayout mEtPassword;


    @BindView(R2.id.usr_mergeScreen_password_textField)
    ValidationEditText passwordValidationEditText;

    @BindView(R2.id.usr_mergeScreen_baseLayout_LinearLayout)
    LinearLayout usr_mergeScreen_baseLayout_LinearLayout;

    private MergeAccountPresenter mergeAccountPresenter;

    private String mEmailId;

    private String mMergeToken;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        View view = inflater.inflate(R.layout.reg_fragment_social_merge_account, container, false);
        ButterKnife.bind(this, view);
        initUI(view);
        connectionStatus(networkUtility.isNetworkAvailable());
        handleOrientation(view);
        mergeAccountPresenter = new MergeAccountPresenter(this);
        return view;
    }


    private void enableMergeButton() {
        RxTextView.textChanges(passwordValidationEditText)
                .map(password -> (password.toString().length() > 0 && networkUtility.isNetworkAvailable()))
                .subscribe(enabled -> mBtnMerge.setEnabled(enabled));
    }

    @Override
    public void onDestroy() {
        mergeAccountPresenter.cleanUp();
        super.onDestroy();
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

        mEmailId = bundle.getString(RegConstants.SOCIAL_MERGE_EMAIL);
        mMergeToken = bundle.getString(RegConstants.SOCIAL_MERGE_TOKEN);

        ((RegistrationFragment) getParentFragment()).showKeyBoard();
        mEtPassword.requestFocus();
        mEtPassword.setValidator(password -> password.length() > 0);
        mEtPassword.setErrorMessage(getString(R.string.reg_EmptyField_ErrorMsg));


        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.START_SOCIAL_MERGE);
        mTvUsedEmail.setText(RegUtility.fromHtml(String.format(mTvUsedEmail.getText().toString(), "<b>" + mEmailId + "</b>")));
        enableMergeButton();
    }


    @OnClick(R2.id.usr_mergeScreen_merge_button)
    public void mergeButtonClick() {
        if (mEtPassword.hasFocus()) {
            mEtPassword.clearFocus();
        }
        getView().requestFocus();
        mergeAccount();
    }

    @OnClick(R2.id.usr_mergeScreen_forgotPassword_button)
    public void forgotButtonClick() {
        performAction();
    }

    private void mergeAccount() {
        if (networkUtility.isNetworkAvailable()) {
            mergeAccountPresenter.mergeToTraditionalAccount(mEmailId, passwordValidationEditText.getText().toString(), mMergeToken);
            mEtPassword.hideError();
            showMergeSpinner();
        } else {
            mRegError.setError(getString(R.string.reg_JanRain_Error_Check_Internet));
        }
    }

    private void performAction() {
        getRegistrationFragment().addResetPasswordFragment();
        trackPage(AppTaggingPages.FORGOT_PASSWORD);
    }

    private void showMergeSpinner() {
        mBtnMerge.showProgressIndicator();
        mBtnMerge.setEnabled(false);
    }

    private void hideMergeSpinner() {
        mBtnMerge.hideProgressIndicator();
        mBtnMerge.setEnabled(true);
    }

    @Override
    public void connectionStatus(boolean isOnline) {
        if (isOnline) {
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


    private void enableMerge() {
        mBtnMerge.setEnabled(true);
    }

    private void disableMerge() {
        mBtnMerge.setEnabled(false);
    }


    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, usr_mergeScreen_baseLayout_LinearLayout, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    @Override
    public void mergeSuccess() {
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_SOCIAL_MERGE);
        hideMergeSpinner();
        completeRegistration();
    }


    private void launchAlmostDoneForTermsAcceptanceFragment() {
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
        trackPage(AppTaggingPages.ALMOST_DONE);
    }

    private void completeRegistration() {
        String emailorMobile = mergeAccountPresenter.getLoginWithDetails();
        if (emailorMobile != null && RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() &&
                (!RegPreferenceUtility.getTermsAndConditionState(getContext(),RegConstants.TERMS_N_CONDITIONS_ACCEPTED, emailorMobile) || !mergeAccountPresenter.getReceiveMarketingEmail())) {
            launchAlmostDoneForTermsAcceptanceFragment();
            return;
        }
        getRegistrationFragment().userRegistrationComplete();
    }


    @Override
    public void mergeFailure(String reason) {
        hideMergeSpinner();
        mEtPassword.setErrorMessage(reason);
        mEtPassword.showError();
    }

    @Override
    public void mergePasswordFailure() {
        hideMergeSpinner();
        mEtPassword.setErrorMessage(getString(R.string.reg_Merge_validate_password_mismatch_errortxt));
        mEtPassword.showError();
    }


    @Override
    public void mergeStatus(boolean isOnline) {
        if (isOnline && passwordValidationEditText.getText().toString().length() > 0) {
            mEtPassword.hideError();
            enableMerge();
            return;
        }
        disableMerge();
    }
}
