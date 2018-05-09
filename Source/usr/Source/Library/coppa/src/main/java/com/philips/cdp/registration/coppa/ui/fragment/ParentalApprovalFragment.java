/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.*;
import android.content.res.*;
import android.os.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.*;
import com.philips.cdp.registration.coppa.base.*;
import com.philips.cdp.registration.coppa.ui.controllers.*;
import com.philips.cdp.registration.coppa.utils.*;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.utils.*;

public class ParentalApprovalFragment extends RegistrationCoppaBaseFragment implements
        NetworkStateListener {

    private NetworkUtility networkUtility;

    private LinearLayout mLlConfirmApprovalParent;
    private TextView mTvConfirmApprovalDesc;
    private TextView mTvRegConfirmApproval;
    private TextView mTVRegConfirmApprovalTitle;
    private Button mBtnAgree;
    private Button mBtnDisAgree;
    private ParentalApprovalFragmentController mParentalApprovalFragmentController;

    private Context mContext;
    private XRegError mRegError;
    private ScrollView mSvRootLayout;
    private View mShadowLineView;
    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {

            if(RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener() !=null){

                RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                        onPrivacyPolicyClick(getActivity());

            }else {

                RegUtility.showErrorMessage(getActivity());
            }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ParentalApprovalFragment : onCreateView");
        networkUtility = RegistrationConfiguration.getInstance().getComponent().getNetworkUtility();
        View view = null;
        if (getResources().getBoolean(R.bool.isTablet)) {
            view = inflater.inflate(R.layout.reg_fragment_coppa_parental_approval_tablet, null);
        } else {
            view = inflater.inflate(R.layout.reg_fragment_coppa_parental_approval_phone, null);
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
        if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus()
                .equals(CoppaStatus.kDICOPPAConfirmationGiven)) {
            return;
        } else {
            if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus()
                    == CoppaStatus.kDICOPPAConsentPending) {
                trackPage(AppTaggingCoppaPages.COPPA_FIRST_CONSENT);
            } else if (mParentalApprovalFragmentController.getCoppaExtension().
                    getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConfirmationPending) {
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
        getRegistrationFragment();
        RegistrationCoppaFragment.hideRefreshProgress();
        super.onDestroy();
        if (mParentalApprovalFragmentController.getCoppaExtension().getCoppaEmailConsentStatus()
                != null
                && mParentalApprovalFragmentController.getCoppaExtension().
                getCoppaEmailConsentStatus().equals(CoppaStatus.kDICOPPAConsentPending)) {
            return;
        }
        if (mParentalApprovalFragmentController.getCoppaExtension().
                getCoppaEmailConsentStatus() != null
                && !mParentalApprovalFragmentController.getCoppaExtension().
                getCoppaEmailConsentStatus().equals(CoppaStatus.kDICOPPAConfirmationGiven)) {
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
        mLlConfirmApprovalParent = (LinearLayout) view.findViewById(
                R.id.ll_reg_confirm_root_container);
        mTvConfirmApprovalDesc = (TextView) view.findViewById(R.id.tv_reg_confirm_approval_details);
        mTvRegConfirmApproval = (TextView) view.findViewById(R.id.tv_reg_confirm_approval);
        mTVRegConfirmApprovalTitle = (TextView) view.findViewById(
                R.id.tv_reg_confirm_approval_title);
        mBtnAgree = (Button) view.findViewById(R.id.reg_btn_agree);
        mBtnAgree.setOnClickListener(mParentalApprovalFragmentController);
        mBtnDisAgree = (Button) view.findViewById(R.id.reg_btn_dis_agree);
        mBtnDisAgree.setOnClickListener(mParentalApprovalFragmentController);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        mShadowLineView = view.findViewById(R.id.reg_view_shadow_line);
        handleUiState();
    }

    public void setConfirmApproval() {
        mTvConfirmApprovalDesc.setText(getNonUsText());
        if (mParentalApprovalFragmentController.isCountryUs()) {
            mTvConfirmApprovalDesc.setText(getUsText());
        }
        if (getActivity() != null) {
            RegCoppaUtility.linkifyTermAndPolicy(mTvConfirmApprovalDesc, getActivity(),
                    privacyLinkClick);
        }
        mTvConfirmApprovalDesc.setVisibility(View.VISIBLE);
        mBtnAgree.setVisibility(View.VISIBLE);
        mBtnDisAgree.setVisibility(View.VISIBLE);
        mTVRegConfirmApprovalTitle.setVisibility(View.VISIBLE);
        if (mShadowLineView != null)
            mShadowLineView.setVisibility(View.VISIBLE);
    }

    private String getUsText() {
        return mContext.getString(R.string.reg_Coppa_Give_Approval_txt) +
                String.format(mContext.getString(R.string.reg_Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.reg_PrivacyNoticeText));
    }

    private String getNonUsText() {
        return mContext.getString(R.string.reg_Coppa_Give_Approval_txt)
                + String.format(mContext.getString(R.string.reg_Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.reg_PrivacyNoticeText));
    }

    public void setIsUSRegionCode() {
        mTvRegConfirmApproval.setVisibility(View.VISIBLE);
        mTvConfirmApprovalDesc.setText(String.format(
                mContext.getString(R.string.reg_Coppa_Confirm_Approval_Content_txt) +
                        mContext.getString(R.string.reg_Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.reg_Coppa_Privacy_Notice_Screen_Title_txt)));
        if (getActivity() != null)
            RegCoppaUtility.linkifyTermAndPolicy(mTvConfirmApprovalDesc,
                    getActivity(), privacyLinkClick);
        mTvConfirmApprovalDesc.setVisibility(View.VISIBLE);
        mBtnAgree.setVisibility(View.VISIBLE);
        mBtnDisAgree.setVisibility(View.VISIBLE);
        if (mShadowLineView != null)
            mShadowLineView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Coppa_Age_Confirmation_Screen_Title_txt;
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.d(RLog.NETWORK_STATE, "ParentalApprovalFragment :onNetWorkStateReceived state :"
                + isOnline);

        handleUiState();
    }

    private void handleUiState() {
        handleOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (networkUtility.isNetworkAvailable()) {
                    if (mRegError != null)
                        mRegError.hideError();
                    if (mBtnAgree != null)
                        mBtnAgree.setEnabled(true);
                    if (mBtnDisAgree != null)
                        mBtnDisAgree.setEnabled(true);
                } else {
                    if (mRegError != null)
                        mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
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
        handleOnUiThread(new Runnable() {
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
        handleOnUiThread(new Runnable() {
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
