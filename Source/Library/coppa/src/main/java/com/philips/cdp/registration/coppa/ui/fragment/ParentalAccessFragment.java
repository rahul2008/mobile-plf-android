
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.ui.customviews.RegCoppaAlertDialog;
import com.philips.cdp.registration.coppa.utils.AppCoppaTaggingConstants;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalAccessFragment extends RegistrationCoppaBaseFragment implements OnClickListener {

    private Button mBtnUnderAge;
    private Button mBtnOverAge;
    private LinearLayout mLlRootContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");

        View view = inflater.inflate(R.layout.reg_fragment_coppa_parental_access, null);
        initUi(view);
        handleOrientation(view);
        mBtnUnderAge.setOnClickListener(this);
        mBtnOverAge.setOnClickListener(this);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDetach");
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
        mBtnUnderAge = (Button) view.findViewById(R.id.btn_reg_under_age);
        mBtnOverAge = (Button) view.findViewById(R.id.btn_reg_over_age);
        mLlRootContainer = (LinearLayout) view.findViewById(R.id.ll_reg_parent_access_container);

        String underAge = getActivity().getString(R.string.reg_Coppa_Age_Verification_UnderAge_Txt);
        int minAge = RegistrationConfiguration.getInstance().getMinAgeLimitByCountry(RegistrationHelper.getInstance().getCountryCode());
        underAge = String.format(underAge, minAge);
        mBtnUnderAge.setText(underAge);

        String overAge = getActivity().getString(R.string.reg_Coppa_Age_Verification_OverAge_Txt);
        overAge = String.format(overAge, minAge);
        mBtnOverAge.setText(overAge);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.btn_reg_over_age){
            getRegistrationFragment().addParentalConfirmFragment();
        }
        if (id == R.id.btn_reg_under_age) {
            showParentalAccessDailog();
        }
    }

    private void showParentalAccessDailog() {
        mBtnUnderAge.setEnabled(false);
        String minAgeLimitTest = getActivity().getString(R.string.reg_Coppa_Age_Verification_UnderAge_Alert_Txt);
        int minAge = RegistrationConfiguration.getInstance().getMinAgeLimitByCountry(RegistrationHelper.getInstance().getCountryCode());
        minAgeLimitTest = String.format(minAgeLimitTest, minAge);
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppCoppaTaggingConstants.MIN_AGE_LIMIT_WARNING);
        RegCoppaAlertDialog.showCoppaDialogMsg(getActivity().getResources().getString(R.string.reg_Coppa_Age_Verification_Screen_Title_txt),minAgeLimitTest,getActivity(), mOkBtnClick);
    }

    private OnClickListener mOkBtnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            mBtnUnderAge.setEnabled(true);
            RegCoppaAlertDialog.dismissDialog();
        }
    };

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Coppa_Age_Verification_Screen_Title_txt;
    }

}
