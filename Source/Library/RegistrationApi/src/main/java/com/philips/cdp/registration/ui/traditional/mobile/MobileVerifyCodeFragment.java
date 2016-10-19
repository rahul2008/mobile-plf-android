
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.philips.cdp.registration.HttpClientService;
import com.philips.cdp.registration.HttpClientServiceReceiver;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.customviews.XMobileHavingProblems;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.customviews.XVerifyNumber;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.traditional.WelcomeFragment;
import com.philips.cdp.registration.ui.utils.FieldsValidator;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONObject;

public class MobileVerifyCodeFragment extends RegistrationBaseFragment implements RefreshUserHandler,HttpClientServiceReceiver.Listener {

    private LinearLayout mLlCreateAccountFields;

    private RelativeLayout mRlCreateActtBtnContainer;

    private Button mBtnVerify;

    private XVerifyNumber mEtCodeNUmber;

    private ProgressBar mPbSpinner;

    private Context mContext;

    private ScrollView mSvRootLayout;

    private XMobileHavingProblems mVeifyHintView;

    private VerifyCodeFragmentController mobileActivationController;

    private FragmentManager mFragmentManager;

    private User mUser;

    private XRegError mRegError;
    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreate");
        super.onCreate(savedInstanceState);
        mobileActivationController = new VerifyCodeFragmentController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onCreateView");
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        mUser = new User(mContext);
        View view = inflater.inflate(R.layout.reg_mobile_activatiom_fragment, container, false);
        mSvRootLayout = (ScrollView) view.findViewById(R.id.sv_root_layout);
        mFragmentManager = getChildFragmentManager();
        initUI(view);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        handleOrientation(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "MobileActivationFragment : onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config, mLlCreateAccountFields, width);
        applyParams(config, mRlCreateActtBtnContainer, width);
        applyParams(config, mVeifyHintView, width);
        applyParams(config, mRegError, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUI(View view) {
        consumeTouch(view);

        mVeifyHintView = (XMobileHavingProblems) view.findViewById(R.id.view_reg_verify_hint);

        mLlCreateAccountFields = (LinearLayout) view.findViewById(R.id.ll_reg_create_account_fields);
        mRlCreateActtBtnContainer = (RelativeLayout) view.findViewById(R.id.rl_reg_singin_options);

        mBtnVerify = (Button) view.findViewById(R.id.btn_reg_Verify);
        mBtnVerify.setOnClickListener(mobileActivationController);
        mEtCodeNUmber = (XVerifyNumber) view.findViewById(R.id.rl_reg_name_field);
        mEtCodeNUmber.setOnUpdateListener(mobileActivationController);
        mPbSpinner = (ProgressBar) view.findViewById(R.id.pb_reg_activate_spinner);
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mPbSpinner.setClickable(false);
        mPbSpinner.setEnabled(true);
        updateUiStatus();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_RegCreateAccount_NavTitle;
    }

    private void updateUiStatus() {
        if (mEtCodeNUmber.getNumber().length() >= RegConstants.VERIFY_CODE_ENTER) {
            mBtnVerify.setEnabled(true);
        } else {
            mBtnVerify.setEnabled(false);
        }
    }

    public void verifyMobileNumberService() {

        mPbSpinner.setVisibility(View.VISIBLE);
        mBtnVerify.setEnabled(false);
        getActivity().startService(createCallingIntent());
    }

    private Intent createCallingIntent() {
        String UUid = mUser.getJanrainUUID();
        String verifiedMobileNumber = FieldsValidator.getVerifiedMobileNumber(UUid,mEtCodeNUmber.getNumber());
        String url = "https://philips-china-eu.eu-dev.janraincapture.com/access/useVerificationCode";
        Intent httpServiceIntent = new Intent(mContext,HttpClientService.class);
        HttpClientServiceReceiver receiver = new HttpClientServiceReceiver(new Handler());
        receiver.setListener(this);

        String bodyContent = "verification_code="+verifiedMobileNumber;
        httpServiceIntent.putExtra("receiver", receiver);
        httpServiceIntent.putExtra("bodyContent", bodyContent);
        httpServiceIntent.putExtra("url", url);
        return httpServiceIntent;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String response = resultData.getString("responseStr");
        Log.i("onReceiveResult ", "Response Val = " + response);
        handleActivate(response);
    }

    private void handleActivate(String response ) {
        if(response!=null){
            try {
                countDownTimer.cancel();
                countDownTimer.onFinish();
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getString("stat").toString().equals("ok")){
                    mUser.refreshUser(this);
                }else{
                    hideSpinner();
                    Log.i("sms Failure ", "Val = " + response);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void handleUI() {
        handleOnUIThread(new Runnable() {
            @Override
            public void run() {
                updateUiStatus();
            }
        });
    }

    public void networkUiState() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                mRegError.hideError();
            } else {
                mRegError.hideError();
            }
            mBtnVerify.setEnabled(true);
        } else {
            mRegError.setError(mContext.getResources().getString(R.string.reg_NoNetworkConnection));
            trackActionLoginError(AppTagingConstants.NETWORK_ERROR_CODE);
            mBtnVerify.setEnabled(false);
        }
    }

    @Override
    public void onRefreshUserSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserSuccess");
        hideSpinner();
        getRegistrationFragment().addFragment(new WelcomeFragment());
    }

    @Override
    public void onRefreshUserFailed(int error) {
        hideSpinner();
        RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : onRefreshUserFailed");
    }

    private void hideSpinner(){
        mPbSpinner.setVisibility(View.GONE);
        mBtnVerify.setEnabled(true);
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {

            super(startTime, interval);
        }

        @Override
        public void onFinish() {
             RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : counter");
            mEtCodeNUmber.setCounterFinish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            RLog.d(RLog.EVENT_LISTENERS, "MobileActivationFragment : "+millisUntilFinished / 1000);
            mEtCodeNUmber.setCountertimer(String.format("%02d", +millisUntilFinished / 1000)+"s");
        }
    }
}
