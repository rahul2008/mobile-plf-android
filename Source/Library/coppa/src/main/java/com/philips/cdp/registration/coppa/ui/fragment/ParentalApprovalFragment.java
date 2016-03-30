
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.ui.controllers.ParentalApprovalFragmentController;
import com.philips.cdp.registration.coppa.utils.RegCoppaUtility;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.CustomCircularProgress;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalApprovalFragment extends RegistrationCoppaBaseFragment {

    private LinearLayout mLlConfirmApprovalParent;
    private TextView tvConfirmApprovalDesc;
    private TextView tvRegConfirmApproval;
    private Button mBtnAgree;
    private Button mBtnDisAgree;
    private ParentalApprovalFragmentController mParentalApprovalFragmentController;
    private CustomCircularProgress mCustomCircularProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onCreate");
        super.onCreate(savedInstanceState);
        mParentalApprovalFragmentController = new ParentalApprovalFragmentController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "ParentalApprovalFragment : onCreateView");

        View view = inflater.inflate(R.layout.fragment_reg_coppa_parental_approval, null);
        mContext = getParentFragment().getActivity().getApplicationContext();
        initUi(view);
        handleOrientation(view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onActivityCreated");
        mParentalApprovalFragmentController.refreshUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalApprovalFragment : onResume");
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
        super.onDestroy();
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
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mLlConfirmApprovalParent = (LinearLayout) view.findViewById(R.id.ll_reg_confirm_root_container);
        tvConfirmApprovalDesc = (TextView)view.findViewById(R.id.tv_reg_confirm_approval_details);
        tvRegConfirmApproval = (TextView)view.findViewById(R.id.tv_reg_confirm_approval);
        mBtnAgree = (Button) view.findViewById(R.id.reg_btn_agree);
        mBtnAgree.setOnClickListener(mParentalApprovalFragmentController);
        mBtnDisAgree = (Button) view.findViewById(R.id.reg_btn_dis_agree);
        mBtnDisAgree.setOnClickListener(mParentalApprovalFragmentController);

    }
    private Context mContext;

    private boolean isCountryUS(){
        boolean isCountryUS = false;
        if(mParentalApprovalFragmentController.getCoppaExtension().getConsent().getLocale() != null){
            isCountryUS = mParentalApprovalFragmentController.getCoppaExtension().getConsent().getLocale().equalsIgnoreCase("en_US");
        }else{
            isCountryUS = RegistrationHelper.getInstance().getCountryCode().equalsIgnoreCase("US");
        }
        return isCountryUS;
    }


    public void setConfirmApproval(){

        tvConfirmApprovalDesc.setText( getNonUsText());
        if( isCountryUS()){
            tvConfirmApprovalDesc.setText(getUsText());
        }

        //RegCoppaUtility.linkifyTermAndPolicy(tvConfirmApprovalDesc, getActivity(), privacyLinkClick,);
        //tvRegConfirmApproval.setVisibility(View.VISIBLE);
        tvConfirmApprovalDesc.setVisibility(View.VISIBLE);
        mBtnAgree.setVisibility(View.VISIBLE);
        mBtnDisAgree.setVisibility(View.VISIBLE);

    }

    @NonNull
    private String getUsText() {
        return getActivity().getText(R.string.Coppa_Give_Approval_txt) +
                "\n" +getActivity().getText(R.string.Coppa_Give_Approval_US_txt) +
                String.format(mContext.getString(R.string.Coppa_Give_Approval_PrivacyNotes_txt), mContext.getString(R.string.PrivacyPolicyText));
    }

    private String getNonUsText(){
        return mContext.getText(R.string.Coppa_Give_Approval_txt)
                 + String.format(mContext.getString(R.string.Coppa_Give_Approval_PrivacyNotes_txt),mContext.getString(R.string.PrivacyPolicyText));
    }

    public void setIsUSRegionCode(){
        tvRegConfirmApproval.setVisibility(View.VISIBLE);
        tvConfirmApprovalDesc.setText(String.format(mContext.getString(R.string.Coppa_Confirm_Approval_Content_txt),mContext.getString(R.string.PrivacyPolicyText)));
        tvConfirmApprovalDesc.setVisibility(View.VISIBLE);
        mBtnAgree.setVisibility(View.VISIBLE);
        mBtnDisAgree.setVisibility(View.VISIBLE);


    }

    @Override
    public int getTitleResourceId() {
        return R.string.Coppa_Consent_Approval_Screen_Title_txt;
    }

    private ClickableSpan privacyLinkClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            RegistrationHelper.getInstance().getUserRegistrationListener().notifyOnPrivacyPolicyClickEventOccurred(getActivity());
        }
    };

    public void showRefreshProgress(){
        mCustomCircularProgress = new CustomCircularProgress(getContext());
        mCustomCircularProgress.show();

    }

    public void hideRefreshProgress(){
        if(mCustomCircularProgress != null){
            mCustomCircularProgress.hide();
        }
    }


}
