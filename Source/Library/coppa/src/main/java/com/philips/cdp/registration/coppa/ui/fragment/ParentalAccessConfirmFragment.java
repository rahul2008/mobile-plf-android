
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalAccessConfirmFragment extends RegistrationCoppaBaseFragment implements OnClickListener {

    private Button mBtnContinue;
    private TextView mTvHowOld;
    private TextView mTvYearOfBirth;
    private RelativeLayout mRlBtnContinueContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessConfirmFragment : onCreateView");
        View view = inflater.inflate(R.layout.fragment_reg_coppa_parental_access_confirm, null);
        initUi(view);
        handleOrientation(view);
        mBtnContinue.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessConfirmFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mRlBtnContinueContainer, width);
        applyParams(config, mTvHowOld, width);
        applyParams(config, mTvYearOfBirth, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mBtnContinue = (Button) view.findViewById(R.id.btn_reg_continue);
        mTvHowOld = (TextView) view.findViewById(R.id.tv_reg_how_old);
        mTvYearOfBirth = (TextView) view.findViewById(R.id.tv_reg_birth_year);
        mRlBtnContinueContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_continue_container);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_reg_continue) {
            getRegistrationFragment().launchRegistrationFragment(false);
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.Coppa_Age_Verification_Screen_Title_txt;
    }

}
