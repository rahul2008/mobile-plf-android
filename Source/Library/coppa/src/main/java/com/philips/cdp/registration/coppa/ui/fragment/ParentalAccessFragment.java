
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.utils.RegCoppaAlertDialog;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalAccessFragment extends RegistrationCoppaBaseFragment implements OnClickListener {

    private Button mBtnUnder16;
    private Button mBtnOver16;
    private LinearLayout mLlRootContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");

        View view = inflater.inflate(R.layout.fragment_reg_coppa_parental_access, null);
        initUi(view);
        handleOrientation(view);
        mBtnUnder16.setOnClickListener(this);
        mBtnOver16.setOnClickListener(this);
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
        mBtnUnder16 = (Button) view.findViewById(R.id.btn_reg_under_16);
        mBtnOver16 = (Button) view.findViewById(R.id.btn_reg_over_16);
        mLlRootContainer = (LinearLayout) view.findViewById(R.id.ll_reg_parent_access_container);

        String under16 = getActivity().getString(R.string.Coppa_Age_Verification_UnderAge_Txt);
        int minAge = RegistrationConfiguration.getInstance().getFlow().getMinAgeLimitByCountry(RegistrationHelper.getInstance().getCountryCode());
        under16 = String.format(under16, minAge);
        mBtnUnder16.setText(under16);

        String over16 = getActivity().getString(R.string.Coppa_Age_Verification_OverAge_Txt);
        over16 = String.format(over16, minAge);
        mBtnOver16.setText(over16);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.btn_reg_over_16){
            ParentalAccessConfirmFragment parentalAccessConfirmFragment = new ParentalAccessConfirmFragment();
            getRegistrationFragment().addFragment(parentalAccessConfirmFragment);
        }
        if (id == R.id.btn_reg_under_16) {
            String minAgeLimitTest = getActivity().getString(R.string.Coppa_Age_Verification_UnderAge_Alert_Txt);
            int minAge = RegistrationConfiguration.getInstance().getFlow().getMinAgeLimitByCountry(RegistrationHelper.getInstance().getCountryCode());
            minAgeLimitTest = String.format(minAgeLimitTest, minAge);
            RegCoppaAlertDialog.showResetPasswordDialog(getActivity().getResources().getString(R.string.Coppa_Age_Verification_Screen_Title_txt),minAgeLimitTest,getActivity(), mOkBtnClick);
        }
    }

    private OnClickListener mOkBtnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            RegCoppaAlertDialog.dismissDialog();
        }
    };

    @Override
    public int getTitleResourceId() {
        return R.string.Coppa_Age_Verification_Screen_Title_txt;
    }

}
