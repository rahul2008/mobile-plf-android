
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XButton;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONObject;

public class MergeSocialToSocialAccountFragment extends RegistrationBaseFragment implements EventListener,
        onUpdateListener, NetworStateListener, SocialProviderLoginHandler, OnClickListener {


    private LinearLayout mLlUsedEMailAddressContainer;

    private RelativeLayout mRlSingInOptions;

    private XRegError mRegError;

    private XButton mBtnMerge;

    private XButton mBtnCancel;

    private String mEmailId;

    private ProgressBar mPbMergeSpinner;

    private String mMergeToken;

    private User mUser;

    private Context mContext;

    private TextView mTvCurrentProviderDetails;

    private TextView mTvBoxText;

    private String mConflictProvider;

    private ScrollView mSvRootLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance()
                .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.reg_fragment_social_to_social_merge_account, container, false);
        RLog.i(RLog.EVENT_LISTENERS,
                "MergeSocialToSocialAccountFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS");
        mUser = new User(mContext);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        initUI(view);
        handleUiErrorState();
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onDestroy");
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
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MergeSocialToSocialAccountFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    private void initUI(View view) {
        consumeTouch(view);
        Bundle bundle = this.getArguments();
        mBtnMerge = (XButton) view.findViewById(R.id.btn_reg_merg);
        mBtnMerge.setOnClickListener(this);

        mEmailId = bundle.getString(RegConstants.SOCIAL_MERGE_EMAIL);

        mBtnCancel = (XButton) view.findViewById(R.id.btn_reg_cancel);
        mBtnCancel.setOnClickListener(this);
        mLlUsedEMailAddressContainer = (LinearLayout) view
                .findViewById(R.id.ll_reg_account_merge_container);

        mRlSingInOptions = (RelativeLayout) view.findViewById(R.id.rl_reg_btn_container);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

        mTvBoxText = (TextView) view.findViewById(R.id.tv_reg_merge_account_box);
        mPbMergeSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_merge_sign_in_spinner);
        mPbMergeSpinner.setClickable(false);
        mPbMergeSpinner.setEnabled(true);

        mMergeToken = bundle.getString(RegConstants.SOCIAL_MERGE_TOKEN);
        mTvCurrentProviderDetails = (TextView) view.findViewById(R.id.tv_reg_provider_Details);

        trackActionStatus(AppTagingConstants.SEND_DATA,
                AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.START_SOCIAL_MERGE);

        String socialProvider = "reg_"+bundle.getString(RegConstants.SOCIAL_PROVIDER);
        String conflictingProvider = "reg_"+bundle.getString(RegConstants.CONFLICTING_SOCIAL_PROVIDER);

        int currentSocialProviderId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(socialProvider, "string",
                getRegistrationFragment().getParentActivity().getPackageName());

        int conflictSocialProviderId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(conflictingProvider, "string",
                getRegistrationFragment().getParentActivity().getPackageName());

        TextView currentProviderView = (TextView) view.findViewById(R.id.tv_reg_conflict_provider);
        String currentProvider = getString(R.string.reg_Social_Merge_Accounts_lbltxt);
        currentProvider = String.format(currentProvider, mContext.getResources().getString(conflictSocialProviderId));
        currentProviderView.setText(currentProvider);
        mConflictProvider = conflictingProvider;

        String previousSocialProviderDetails = getString(R.string.reg_Social_Merge_Used_EmailError_lbltxt);
        previousSocialProviderDetails = String.format(previousSocialProviderDetails, mContext.getResources().getString(conflictSocialProviderId)
                ,mEmailId,mContext.getResources().getString(currentSocialProviderId));
        mTvCurrentProviderDetails.setText(previousSocialProviderDetails);

        TextView mergeAccountBoxView = (TextView) view.findViewById(R.id.tv_reg_merge_account_box);
        String signInWith = getString(R.string.reg_Social_Merge_Cancel_And_Restart_Registration_lbltxt);
        signInWith = String.format(signInWith,mContext.getResources().getString(currentSocialProviderId) , mContext.getResources().getString(conflictSocialProviderId));
        mergeAccountBoxView.setText(signInWith);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reg_merg) {
            RLog.d(RLog.ONCLICK, "MergeSocialToSocialAccountFragment : Merge");
            getView().requestFocus();
            mergeAccount();

        } else if (v.getId() == R.id.btn_reg_cancel) {
            RLog.d(RLog.ONCLICK, "MergeSocialToSocialAccountFragment : Cancel");
            trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                    AppTagingConstants.SIGN_OUT);
            trackPage(AppTaggingPages.HOME);
            mUser.logout(null);
            getFragmentManager().popBackStack();
        }
    }

    private void mergeAccount() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            mUser.loginUserUsingSocialProvider(getActivity(), mConflictProvider, this, mMergeToken);
            showMergeSpinner();
        } else {
            mRegError.setError(getString(R.string.reg_JanRain_Error_Check_Internet));
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    private void showMergeSpinner() {
        getView().findViewById(R.id.sv_root_layout).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.ll_root_layout).setVisibility(View.VISIBLE);
        mPbMergeSpinner.setVisibility(View.VISIBLE);
        mBtnMerge.setEnabled(false);
    }

    private void hideMergeSpinner() {
        getView().findViewById(R.id.sv_root_layout).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.ll_root_layout).setVisibility(View.INVISIBLE);
        mPbMergeSpinner.setVisibility(View.INVISIBLE);
        mBtnMerge.setEnabled(true);
    }

    private void handleUiErrorState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        } else {
            scrollViewAutomatically(mRegError, mSvRootLayout);
            mRegError.setError(getString(R.string.reg_NoNetworkConnection));
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private void updateUiStatus() {
        RLog.i("MergeSocialToSocialAccountFragment", "updateUiStatus");
        if (NetworkUtility.isNetworkAvailable(mContext)
                && UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
            mBtnMerge.setEnabled(true);
            mBtnCancel.setEnabled(true);
            mRegError.hideError();
        } else {
            mBtnMerge.setEnabled(false);
        }
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "MergeSocialToSocialAccountFragment :onEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            updateUiStatus();
        }
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlUsedEMailAddressContainer, width);
        applyParams(config, mRlSingInOptions, width);
        applyParams(config, mRegError, width);
        applyParams(config, mTvBoxText, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
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

    @Override
    public int getTitleResourceId() {
        return R.string.reg_SigIn_TitleTxt;
    }

    private void launchWelcomeFragment() {
        getRegistrationFragment().addWelcomeFragmentOnVerification();
        trackPage(AppTaggingPages.WELCOME);
    }


    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "MergeSocialToSocialAccountFragment :onNetWorkStateReceived state :"
                + isOnline);
        handleUiErrorState();
        updateUiStatus();
    }

    @Override
    public void onLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                hideMergeSpinner();
                trackActionStatus(AppTagingConstants.SEND_DATA,
                        AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.SUCCESS_SOCIAL_MERGE);
                launchWelcomeFragment();
            }
        });
    }

    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                handleLoginFaiedWithError(userRegistrationFailureInfo);
            }
        });

    }

    private void handleLoginFaiedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
        RLog.i(RLog.CALLBACK, "MergeSocialToSocialAccountFragment : onLoginFailedWithError");
        hideMergeSpinner();
        if (null != userRegistrationFailureInfo) {
            mRegError.setError(userRegistrationFailureInfo.getErrorDescription());
            trackActionLoginError(userRegistrationFailureInfo.getErrorCode());
            scrollViewAutomatically(mRegError, mSvRootLayout);
        }
    }

    @Override
    public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "MergeSocialToSocialAccountFragment : onLoginFailedWithTwoStepError");
                hideMergeSpinner();
            }
        });
    }

    @Override
    public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider
            , String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "MergeSocialToSocialAccountFragment : onLoginFailedWithMergeFlowError");
                hideMergeSpinner();
            }
        });

    }

    @Override
    public void onContinueSocialProviderLoginSuccess() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "MergeSocialToSocialAccountFragment : onContinueSocialProviderLoginSuccess");
                hideMergeSpinner();
                launchWelcomeFragment();
            }
        });
    }

    @Override
    public void onContinueSocialProviderLoginFailure(final UserRegistrationFailureInfo userRegistrationFailureInfo) {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                RLog.i(RLog.CALLBACK, "MergeSocialToSocialAccountFragment : onContinueSocialProviderLoginFailure");
                hideMergeSpinner();
                if (null != userRegistrationFailureInfo) {
                    trackActionLoginError(userRegistrationFailureInfo.getErrorCode());
                }
            }
        });
    }
}
