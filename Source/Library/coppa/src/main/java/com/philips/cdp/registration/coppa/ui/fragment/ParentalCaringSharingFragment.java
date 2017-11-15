/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.utils.AppTaggingCoppaPages;
import com.philips.cdp.registration.coppa.utils.RegCoppaUtility;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;

public class ParentalCaringSharingFragment extends RegistrationCoppaBaseFragment implements OnClickListener, NetworkStateListener {

    private LinearLayout mLlRootContainer;
    private Button mBtnDashboard;
    private TextView mTextDetailsContant;
    private Context mContext;
    private TextView mTextContantTitle;
    private String mCoppaStatus;
    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                    onPrivacyPolicyClick(getActivity());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onCreate");
        Bundle bunble = getArguments();
        if (bunble != null) {
            mCoppaStatus = bunble.getString(RegConstants.COPPA_STATUS);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");

        View view = inflater.inflate(R.layout.reg_fragment_coppa_thank_you_parental_consent, null);
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        mContext = getParentFragment().getActivity().getApplicationContext();
        initUi(view);
        handleOrientation(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalCaringSharingFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlRootContainer, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mBtnDashboard = (Button) view.findViewById(R.id.coppa_reg_btn_dashboard);
        mBtnDashboard.setOnClickListener(this);
        mLlRootContainer = (LinearLayout) view.findViewById(R.id.ll_reg_parent_access_container);
        mTextDetailsContant = (TextView) view.findViewById(R.id.tv_coppa_reg_thanks_consent_details);
        mTextContantTitle = (TextView) view.findViewById(R.id.coppa_reg_thank_you_id);
        RLog.d(RegConstants.COPPA_STATUS, "Status : " + mCoppaStatus);
        if (mCoppaStatus == CoppaStatus.kDICOPPAConsentGiven.toString() && RegUtility.isCountryUS(RegistrationHelper.getInstance().getLocale(mContext).toString())) {
            mTextDetailsContant.setText(getUsText());
            mTextContantTitle.setText(getResources().getString(R.string.reg_Coppa_US_Parental_Access_Thank_You_Txt));
            if(getActivity()!=null){
                RegCoppaUtility.linkifyTermAndPolicy(mTextDetailsContant, getActivity(), privacyLinkClick);
            }
            trackPage(AppTaggingCoppaPages.COPPA_THANK_AFTER_FIRST_CONSENT);
        } else if (mCoppaStatus == CoppaStatus.kDICOPPAConfirmationGiven.toString()) {
            mTextDetailsContant.setText(getAlreadyUsText());
            mTextContantTitle.setText(getResources().getString(R.string.reg_Coppa_US_Parental_Access_Consent_Given_Thank_You_Txt));
            trackPage(AppTaggingCoppaPages.COPPA_CONSENT_PROCESS_COMPLETED);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.coppa_reg_btn_dashboard) {
            RLog.d("Dash Board ", "Clicked : *******");
            if (RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener() != null) {
                RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                        onUserRegistrationComplete(getActivity());
            }
        }
    }

    private String getUsText() {
        return mContext.getString(R.string.reg_Coppa_US_Parental_Access_Content_Txt) +
                "\n\n" + String.format(mContext.getString(R.string.reg_Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.reg_PrivacyNoticeText));
    }
    private String getAlreadyUsText() {
        return mContext.getString(R.string.reg_Coppa_US_Parental_Access_Consent_Given_Content_Txt);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Coppa_Age_Confirmation_Screen_Title_txt;
    }

    @Override
    public void onNetWorkStateReceived(final boolean isOnline) {

    }
   }
