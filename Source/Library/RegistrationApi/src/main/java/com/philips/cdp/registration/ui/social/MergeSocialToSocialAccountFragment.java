
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.social;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.utils.*;
import com.philips.platform.uid.view.widget.*;
import com.philips.platform.uid.view.widget.Button;

import javax.inject.*;

import butterknife.*;

public class MergeSocialToSocialAccountFragment extends RegistrationBaseFragment implements
        MergeSocialToSocialAccountContract {

    @Inject
    NetworkUtility networkUtility;

    @Inject
    User user;

    @BindView(R2.id.reg_error_msg)
    XRegError mRegError;

    @BindView(R2.id.usr_mergeScreen_login_button)
    ProgressBarButton usr_mergeScreen_login_button;

    @BindView(R2.id.usr_mergeScreen_logout_button)
    Button usr_mergeScreen_logout_button;

    @BindView(R2.id.usr_mergeScreen_used_social_label)
    Label usr_mergeScreen_used_social_label;

    @BindView(R2.id.usr_mergeScreen_used_social_again_label)
    Label usr_mergeScreen_used_social_again_label;

    @BindView(R2.id.usr_mergeScreen_rootLayout_scrollView)
    ScrollView usr_mergeScreen_rootLayout_scrollView;

    @BindView(R2.id.ll_root_layout)
    LinearLayout ll_root_layout;

    @BindView(R2.id.usr_mergeScreen_baseLayout_LinearLayout)
    LinearLayout usr_mergeScreen_baseLayout_LinearLayout;

    private String mConflictProvider;

    private String mMergeToken;

    private Context mContext;

    private MergeSocialToSocialAccountPresenter mergeSocialToSocialAccountPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);
        View view = inflater.inflate(R.layout.reg_fragment_social_to_social_merge_account, container, false);
        ButterKnife.bind(this, view);
        initUI(view);
        networkChangeStatus(networkUtility.isNetworkAvailable());
        handleOrientation(view);
        mergeSocialToSocialAccountPresenter = new MergeSocialToSocialAccountPresenter(this,user);
        return view;
    }

    @Override
    public void onDestroy() {
        mergeSocialToSocialAccountPresenter.cleanUp();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    private void initUI(View view) {
        consumeTouch(view);
        Bundle bundle = this.getArguments();
        mMergeToken = bundle.getString(RegConstants.SOCIAL_MERGE_TOKEN);
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.START_SOCIAL_MERGE);
        mConflictProvider = bundle.getString(RegConstants.CONFLICTING_SOCIAL_PROVIDER);
        String conflictingProvider = "reg_" + mConflictProvider;
        int conflictSocialProviderId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(conflictingProvider, "string",
                getRegistrationFragment().getParentActivity().getPackageName());
        String conflictSocialProvider = mContext.getResources().getString(conflictSocialProviderId);

        usr_mergeScreen_used_social_label.setText(RegUtility.fromHtml(String.format(usr_mergeScreen_used_social_label.getText().toString(),  "<b>" + conflictSocialProvider+"</b>")));
        usr_mergeScreen_used_social_again_label.setText(RegUtility.fromHtml(String.format(usr_mergeScreen_used_social_again_label.getText().toString(), "<b>" + conflictSocialProvider+"</b>")));
        usr_mergeScreen_login_button.setText(String.format(usr_mergeScreen_login_button.getText(), conflictSocialProvider));
    }


    private AlertDialogFragment alertDialogFragment;

    @OnClick(R2.id.usr_mergeScreen_logout_button)
    void showLogoutAlert() {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogLayout(R.layout.social_merge_dialog)
                .setNegativeButton(mContext.getString(R.string.reg_Social_Merge_Cancel_btntxt), v -> alertDialogFragment.dismiss())
                .setPositiveButton(mContext.getString(R.string.reg_DLS_Merge_Accounts_Logout_Dialog__Button_Title), v -> {
                    alertDialogFragment.dismiss();
                    performAction();
                })
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);
        builder.setTitle(mContext.getString(R.string.reg_DLS_Merge_Accounts_Logout_Dialog_Title));
        alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), null);

    }

    private void performAction() {
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SIGN_OUT);
        trackPage(AppTaggingPages.HOME);
        mergeSocialToSocialAccountPresenter.logout();
        getFragmentManager().popBackStack();
    }

    @OnClick(R2.id.usr_mergeScreen_login_button)
    void mergeAccount() {
        if (networkUtility.isNetworkAvailable()) {
            mergeSocialToSocialAccountPresenter.loginUserUsingSocialProvider(mConflictProvider, mMergeToken);
            showMergeSpinner();
        } else {
            mRegError.setError(getString(R.string.reg_JanRain_Error_Check_Internet));
            scrollViewAutomatically(mRegError, usr_mergeScreen_rootLayout_scrollView);
        }
    }

    private void showMergeSpinner() {
        usr_mergeScreen_rootLayout_scrollView.setVisibility(View.INVISIBLE);
        ll_root_layout.setVisibility(View.VISIBLE);
        usr_mergeScreen_login_button.showProgressIndicator();
        usr_mergeScreen_login_button.setEnabled(false);
    }

    private void hideMergeSpinner() {
        usr_mergeScreen_rootLayout_scrollView.setVisibility(View.VISIBLE);
        ll_root_layout.setVisibility(View.INVISIBLE);
        usr_mergeScreen_login_button.hideProgressIndicator();
        usr_mergeScreen_login_button.setEnabled(true);
    }

    private void networkChangeStatus(boolean isOnline) {
        if (isOnline) {
            mRegError.hideError();
        } else {
            scrollViewAutomatically(mRegError, usr_mergeScreen_rootLayout_scrollView);
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
        }
    }

    private void updateUiOnNetworkChange(boolean isOnline) {
        if (isOnline) {
            usr_mergeScreen_login_button.setEnabled(true);
            usr_mergeScreen_logout_button.setEnabled(true);
            mRegError.hideError();
        } else {
            usr_mergeScreen_login_button.setEnabled(false);
        }
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

    private void completeRegistration() {
        String emailorMobile = mergeSocialToSocialAccountPresenter.getLoginWithDetails();
        if (emailorMobile != null && RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired() &&
                !RegPreferenceUtility.getStoredState(mContext, emailorMobile) || !mergeSocialToSocialAccountPresenter.getReceiveMarketingEmail()) {
            launchAlmostDoneForTermsAcceptanceFragment();
            return;
        }
        getRegistrationFragment().userRegistrationComplete();
    }

    private void launchAlmostDoneForTermsAcceptanceFragment() {
        trackPage(AppTaggingPages.ALMOST_DONE);
        getRegistrationFragment().addAlmostDoneFragmentforTermsAcceptance();
    }


    @Override
    public void connectionStatus(boolean isOnline) {
        networkChangeStatus(isOnline);
    }

    @Override
    public void mergeStatus(boolean isOnline) {
        updateUiOnNetworkChange(isOnline);
    }

    @Override
    public void mergeSuccess() {
        hideMergeSpinner();
        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_SOCIAL_MERGE);
        completeRegistration();
    }

    @Override
    public void mergeFailure(String errorDescription) {
        hideMergeSpinner();
        if (null != errorDescription) {
            mRegError.setError(errorDescription);
            scrollViewAutomatically(mRegError, usr_mergeScreen_rootLayout_scrollView);
        }
    }

    @Override
    public void mergeFailureIgnored() {
        hideMergeSpinner();
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }
}