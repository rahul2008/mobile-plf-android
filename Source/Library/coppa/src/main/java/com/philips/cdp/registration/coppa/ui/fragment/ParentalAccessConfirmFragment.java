
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.listener.NumberPickerListener;
import com.philips.cdp.registration.coppa.ui.customviews.RegCoppaAlertDialog;
import com.philips.cdp.registration.coppa.ui.customviews.XNumberPickerDialog;
import com.philips.cdp.registration.coppa.utils.AppCoppaTaggingConstants;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.servertime.ServerTime;
import com.philips.cdp.tagging.Tagging;

import java.util.Calendar;

public class ParentalAccessConfirmFragment extends RegistrationCoppaBaseFragment implements OnClickListener {

    private static final int MAX_AGE_VAL = 116;
    private static final int MIN_AGE_VAL = 0;
    private int MAX_YEAR_VAL ;
    private int MIN_YEAR_VAL ;
    private Button mBtnContinue;
    private RelativeLayout mRlBtnContinueContainer;
    private TextView mTvSelectedAge;
    private TextView mTvSelectedYear;
    private LinearLayout mLlSelectAgeContainer;
    private LinearLayout mLlSelectYearContainer;
    private LinearLayout mLlSelectAgeSelector;
    private LinearLayout mLlSelectYearSelector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessConfirmFragment : onCreate");
        super.onCreate(savedInstanceState);

        MAX_YEAR_VAL = Calendar.getInstance().get(Calendar.YEAR);
        MIN_YEAR_VAL = MAX_YEAR_VAL - MAX_AGE_VAL+1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessConfirmFragment : onCreateView");
        View view = inflater.inflate(R.layout.fragment_reg_coppa_parental_access_confirm, null);
        initUi(view);
        handleOrientation(view);
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
        applyParams(config, mLlSelectAgeContainer, width);
        applyParams(config, mLlSelectYearContainer, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mBtnContinue = (Button) view.findViewById(R.id.btn_reg_continue);
        mBtnContinue.setOnClickListener(this);
        mRlBtnContinueContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_continue_container);
        mTvSelectedAge = (TextView) view.findViewById(R.id.tv_reg_selected_age);
        mTvSelectedYear = (TextView) view.findViewById(R.id.tv_reg_selected_birth_year);
        mLlSelectAgeContainer = (LinearLayout) view.findViewById(R.id.ll_reg_age_select_container);

        mLlSelectAgeSelector = (LinearLayout) view.findViewById(R.id.ll_reg_age_selector);
        mLlSelectAgeSelector.setOnClickListener(this);

        mLlSelectYearContainer = (LinearLayout) view.findViewById(R.id.ll_reg_age_year_container);

        mLlSelectYearSelector = (LinearLayout) view.findViewById(R.id.ll_reg_age_year_selector);
        mLlSelectYearSelector.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_reg_continue) {
            mBtnContinue.setEnabled(false);
            validateInputs();

        } else if ( id == R.id.ll_reg_age_selector) {

            XNumberPickerDialog dialogCoppaAgeVerification = new XNumberPickerDialog(new NumberPickerListener() {
                @Override
                public void onNumberSelect(int num) {
                    mTvSelectedAge.setText(String.valueOf(num));
                    updateUI();
                }
            });
            dialogCoppaAgeVerification.showNumberPickerDialog(getActivity(), MIN_AGE_VAL, MAX_AGE_VAL);
            dialogCoppaAgeVerification.setInitialValue(MIN_AGE_VAL);
        } else if (id== R.id.ll_reg_age_year_selector) {

            XNumberPickerDialog dialogCoppaAgeVerification = new XNumberPickerDialog(new NumberPickerListener() {
                @Override
                public void onNumberSelect(int num) {
                    mTvSelectedYear.setText(String.valueOf(num));
                    updateUI();
                }
            });
            dialogCoppaAgeVerification.showNumberPickerDialog(getActivity(), MIN_YEAR_VAL, MAX_YEAR_VAL);
        }
    }

    private void validateInputs() {
        ServerTime.init(getActivity().getApplicationContext());
        String currentTime = ServerTime.getInstance().getCurrentTime();
        int currentYear = Integer.parseInt(currentTime.substring(0, 4));
        int selectedYear = Integer.parseInt(mTvSelectedYear.getText().toString().trim());
        int caluculateAge = currentYear - selectedYear;
        int howMuchOld = Integer.parseInt(mTvSelectedAge.getText().toString().trim());

        int minAge = RegistrationConfiguration.getInstance().getFlow().getMinAgeLimitByCountry(RegistrationHelper.getInstance().getCountryCode());

        if (howMuchOld < minAge) {
            showParentalAccessDailog(minAge);
            return;
        }

        if (howMuchOld == caluculateAge || howMuchOld == caluculateAge - 1) {
            Tagging.setLaunchingPageName("coppa:ageverification");
            getRegistrationFragment().launchRegistrationFragment();
            return;
        }
        RegCoppaAlertDialog.showCoppaDialogMsg(getActivity().getString(R.string.Coppa_Age_Verification_Alert_Title),
                getActivity().getString(R.string.Coppa_Age_Verification_Age_Mismatch_Alert_Message), getActivity(), mOkBtnClick);

    }

    private void showParentalAccessDailog(int minAge) {
        String minAgeLimitTest = getActivity().getString(R.string.Coppa_Age_Verification_UnderAge_Alert_Txt);
        minAgeLimitTest = String.format(minAgeLimitTest, minAge);
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.STATUS_NOTIFICATION,
                AppCoppaTaggingConstants.MIN_AGE_LIMIT_WARNING);
        RegCoppaAlertDialog.showCoppaDialogMsg(getActivity().getResources().getString(R.string.Coppa_Age_Verification_Screen_Title_txt), minAgeLimitTest, getActivity(), mOkBtnClick);
    }


    private void updateUI() {
        if (mTvSelectedAge.length() > 0 && mTvSelectedYear.length() > 0) {
            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.Coppa_Age_Verification_Screen_Title_txt;
    }

    private View.OnClickListener mOkBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegCoppaAlertDialog.dismissDialog();
            mBtnContinue.setEnabled(true);
        }
    };
}
