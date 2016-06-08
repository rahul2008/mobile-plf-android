/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.ui.controllers.ParentalApprovalFragmentController;
import com.philips.cdp.registration.coppa.utils.AppTaggingCoppaPages;
import com.philips.cdp.registration.coppa.utils.RegCoppaUtility;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;

import org.w3c.dom.Text;

public class ParentalApprovalFragment extends RegistrationCoppaBaseFragment implements NetworStateListener {

    private LinearLayout mLlConfirmApprovalParent;
    private TextView mTvConfirmApprovalDesc;
    private TextView mTvRegConfirmApproval;
    private TextView mTVRegConfirmApprovalTitle;
    private Button mBtnAgree;
    private Button mBtnDisAgree;
    private ParentalApprovalFragmentController mParentalApprovalFragmentController;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private XRegError mRegError;
    private ScrollView mSvRootLayout;
    private View mShadowLineView;
    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnPrivacyPolicyClickEventOccurred(getActivity());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onCreate");
        super.onCreate(savedInstanceState);
        mParentalApprovalFragmentController = new ParentalApprovalFragmentController(this);
        mParentalApprovalFragmentController.refreshUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ParentalApprovalFragment : onCreateView");
        View view;
        if (getResources().getBoolean(R.bool.isTablet)) {
            view = inflater.inflate(R.layout.fragment_reg_coppa_parental_approval_tablet, null);
        } else {
            view = inflater.inflate(R.layout.fragment_reg_coppa_parental_approval_phone, null);
        }


        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        mContext = getParentFragment().getActivity().getApplicationContext();
        initUi(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onActivityCreated");
        hideContent();
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onStart");
    }

    @Override
    public void onResume() {
        mContext = getParentFragment().getActivity().getApplicationContext();
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onResume");
        mContext = getParentFragment().getActivity().getApplicationContext();
        mParentalApprovalFragmentController.getCoppaExtension().buildConfiguration();
        checkApprovalStatus();
    }

    private void checkApprovalStatus() {
        if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus().equals(CoppaStatus.kDICOPPAConfirmationGiven)) {
            return;
        } else {
            if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConsentPending) {
                trackPage(AppTaggingCoppaPages.COPPA_FIRST_CONSENT);
            } else if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConfirmationPending) {
                trackPage(AppTaggingCoppaPages.COPPA_SECOND_CONSENT);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        hideRefreshProgress();
        super.onDestroy();
        if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus() != null
                && mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus().equals(CoppaStatus.kDICOPPAConsentPending)) {
            trackPage(AppTaggingPages.WELCOME);
            return;
        }
        if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus() != null
                && !mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus().equals(CoppaStatus.kDICOPPAConfirmationGiven)) {
            trackPage(AppTaggingPages.WELCOME);
            return;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalApprovalFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlConfirmApprovalParent, width);
        applyParams(config, mBtnAgree, width);
        applyParams(config, mBtnDisAgree, width);
        applyParams(config, mRegError, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mLlConfirmApprovalParent = (LinearLayout) view.findViewById(R.id.ll_reg_confirm_root_container);
        mTvConfirmApprovalDesc = (TextView) view.findViewById(R.id.tv_reg_confirm_approval_details);
        mTvRegConfirmApproval = (TextView) view.findViewById(R.id.tv_reg_confirm_approval);
        mTVRegConfirmApprovalTitle = (TextView) view.findViewById(R.id.tv_reg_confirm_approval_title);
        mBtnAgree = (Button) view.findViewById(R.id.reg_btn_agree);
        mBtnAgree.setOnClickListener(mParentalApprovalFragmentController);
        mBtnDisAgree = (Button) view.findViewById(R.id.reg_btn_dis_agree);
        mBtnDisAgree.setOnClickListener(mParentalApprovalFragmentController);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        mShadowLineView = (View) view.findViewById(R.id.reg_view_shadow_line);
        handleUiState();
    }

    public void setConfirmApproval() {
        mTvConfirmApprovalDesc.setText(getNonUsText());
        if (mParentalApprovalFragmentController.isCountryUS()) {
            mTvConfirmApprovalDesc.setText(getUsText());
        }
        RegCoppaUtility.linkifyTermAndPolicy(mTvConfirmApprovalDesc, getActivity(), privacyLinkClick);
        mTvConfirmApprovalDesc.setVisibility(View.VISIBLE);
        mBtnAgree.setVisibility(View.VISIBLE);
        mBtnDisAgree.setVisibility(View.VISIBLE);
        if (mShadowLineView != null)
            mShadowLineView.setVisibility(View.VISIBLE);
    }

    private String getUsText() {
        return mContext.getString(R.string.Coppa_Give_Approval_txt) +
                "\n" + mContext.getString(R.string.Coppa_Give_Approval_US_txt) +
                String.format(mContext.getString(R.string.Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.PrivacyPolicyText));
    }

    private String getNonUsText() {
        return mContext.getString(R.string.Coppa_Give_Approval_txt)
                + String.format(mContext.getString(R.string.Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.PrivacyPolicyText));
    }

    public void setIsUSRegionCode() {
        mTvRegConfirmApproval.setVisibility(View.VISIBLE);
        mTvConfirmApprovalDesc.setText(String.format(mContext.getString(R.string.Coppa_Confirm_Approval_Content_txt) + mContext.getString(R.string.Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.Coppa_Privacy_Notice_Screen_Title_txt)));
        RegCoppaUtility.linkifyTermAndPolicy(mTvConfirmApprovalDesc, getActivity(), privacyLinkClick);
        mTvConfirmApprovalDesc.setVisibility(View.VISIBLE);
        mBtnAgree.setVisibility(View.VISIBLE);
        mBtnDisAgree.setVisibility(View.VISIBLE);
        if (mShadowLineView != null)
            mShadowLineView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.Coppa_Parental_Consent_Screen_Title_txt;
    }

    public void showRefreshProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        } else {
            mProgressDialog.show();
        }
    }

    public void hideRefreshProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "ParentalApprovalFragment :onNetWorkStateReceived state :" + isOnline);

        handleUiState();
    }

    private void handleUiState() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtility.isNetworkAvailable(mContext)) {
                    if (mRegError != null)
                        mRegError.hideError();
                    if (mBtnAgree != null)
                        mBtnAgree.setEnabled(true);
                    if (mBtnDisAgree != null)
                        mBtnDisAgree.setEnabled(true);
                } else {
                    if (mRegError != null)
                        mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
                    if (mRegError != null && mSvRootLayout != null)
                        scrollViewAutomatically(mRegError, mSvRootLayout);
                    if (mBtnAgree != null)
                        mBtnAgree.setEnabled(false);
                    if (mBtnDisAgree != null)
                        mBtnDisAgree.setEnabled(false);
                }
            }
        });
    }

    public void showContent() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mSvRootLayout != null)
                    mSvRootLayout.setVisibility(View.VISIBLE);
                if (mShadowLineView != null)
                    mShadowLineView.setVisibility(View.VISIBLE);
            }
        });

    }

    public void hideContent() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mSvRootLayout != null)
                    mSvRootLayout.setVisibility(View.INVISIBLE);
                if (mShadowLineView != null)
                    mShadowLineView.setVisibility(View.INVISIBLE);
            }
        });
    }

}
