
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.coppa.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRSession;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.base.CoppaResendError;
import com.philips.cdp.registration.coppa.base.ResendCoppaEmailConsentHandler;
import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaListener;
import com.philips.cdp.registration.coppa.utils.CoppaInterface;
import com.philips.cdp.registration.coppa.utils.CoppaLaunchInput;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class RegistrationCoppaSampleActivity extends Activity implements OnClickListener,
        UserRegistrationCoppaListener,
        UserRegistrationUIEventListener,
        RefreshLoginSessionHandler, ResendCoppaEmailConsentHandler {

    private Button mBtnRegistrationWithAccountSettings;
    private Button mBtnRegistrationWithOutAccountSettings;
    private Button mBtnRefresh;
    private Button mBtnParentalConsent;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    private Button mBtnChangeConfiguaration;
    private Button mBtnApply;
    private Button mBtnCancel;
    private LinearLayout mLlConfiguration;

    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity register: UserRegistrationCoppaListener");
        setContentView(R.layout.activity_main);
        RegistrationCoppaHelper.getInstance().registerUserRegistrationListener(this);
        mBtnRegistrationWithAccountSettings = (Button) findViewById(R.id.btn_registration_with_account);
        mBtnRegistrationWithAccountSettings.setOnClickListener(this);
        mBtnParentalConsent = (Button)findViewById(R.id.btn_parental_consent);
        mBtnParentalConsent.setOnClickListener(this);
        mBtnRegistrationWithOutAccountSettings = (Button) findViewById(R.id.btn_registration_without_account);
        mBtnRegistrationWithOutAccountSettings.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(RegistrationCoppaSampleActivity.this);
        mProgressDialog.setCancelable(false);

        user = new User(mContext);
        mBtnRefresh = (Button) findViewById(R.id.btn_refresh_user);
        mBtnRefresh.setOnClickListener(this);


        mLlConfiguration = (LinearLayout) findViewById(R.id.ll_configuartion);
        mRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);


        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        final String restoredText = prefs.getString("reg_environment", null);
        if (restoredText != null) {

            switch (RegUtility.getConfiguration(restoredText)) {
                case EVALUATION:
                    mRadioGroup.check(R.id.Evalution);
                    break;
                case DEVELOPMENT:
                    mRadioGroup.check(R.id.Development);
                    break;
                case PRODUCTION:
                    mRadioGroup.check(R.id.Production);
                    break;
                case STAGING:
                    mRadioGroup.check(R.id.Stagging);
                    break;
                case TESTING:
                    mRadioGroup.check(R.id.Testing);
                    break;
            }

        }

        mLlConfiguration.setVisibility(View.GONE);
        mBtnChangeConfiguaration = (Button) findViewById(R.id.btn_change_configuration);
        mBtnChangeConfiguaration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlConfiguration.setVisibility(View.VISIBLE);
            }
        });
        mBtnApply = (Button) findViewById(R.id.Apply);
        mBtnApply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlConfiguration.setVisibility(View.GONE);

                //Resetn
                UserRegistrationInitializer.getInstance().resetInitializationState();
                //Logout user
                clearData();



                int checkedId = mRadioGroup.getCheckedRadioButtonId();
                // find which radio button is selected
                if (checkedId == R.id.Evalution) {
                    Toast.makeText(getApplicationContext(), "choice: Evalution",
                            Toast.LENGTH_SHORT).show();
                    RegistrationCoppaApplication.getInstance().initRegistration(Configuration.EVALUATION);
                } else if (checkedId == R.id.Testing) {
                    Toast.makeText(getApplicationContext(), "choice: Testing",
                            Toast.LENGTH_SHORT).show();
                    RegistrationCoppaApplication.getInstance().initRegistration(Configuration.TESTING);
                } else if (checkedId == R.id.Development) {
                    Toast.makeText(getApplicationContext(), "choice: Development",
                            Toast.LENGTH_SHORT).show();
                    RegistrationCoppaApplication.getInstance().initRegistration(Configuration.DEVELOPMENT);
                } else if (checkedId == R.id.Production) {
                    Toast.makeText(getApplicationContext(), "choice: Production",
                            Toast.LENGTH_SHORT).show();
                    RegistrationCoppaApplication.getInstance().initRegistration(Configuration.PRODUCTION);
                } else if (checkedId == R.id.Stagging) {
                    Toast.makeText(getApplicationContext(), "choice: Stagging",
                            Toast.LENGTH_SHORT).show();
                    RegistrationCoppaApplication.getInstance().initRegistration(Configuration.STAGING);
                }


            }
        });
        mBtnCancel = (Button) findViewById(R.id.Cancel);
        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlConfiguration.setVisibility(View.GONE);
            }
        });




    }

    private void clearData() {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.deleteFromDisk();
        mContext.deleteFile(RegConstants.DI_PROFILE_FILE);
        if (JRSession.getInstance() != null) {
            JRSession.getInstance().signOutAllAuthenticatedUsers();
        }
        Jump.signOutCaptureUser(mContext);

    }

    @Override
    protected void onStart() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaSampleActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        AppTagging.collectLifecycleData(this);
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaSampleActivity : onResume");
        super.onResume();

    }

    @Override
    protected void onPause() {
        AppTagging.pauseCollectingLifecycleData();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaSampleActivity : onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaSampleActivity : onStop");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RegistrationCoppaHelper.getInstance().unRegisterUserRegistrationListener(this);
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity unregister : RegisterUserRegistrationListener");
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        CoppaLaunchInput urLaunchInput;
        ActivityLauncher activityLauncher;
        CoppaInterface urInterface;
        switch (v.getId()) {

            case R.id.btn_registration_with_account:
                RLog.d(RLog.ONCLICK, "RegistrationCoppaSampleActivity : Registration");
                RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
                urLaunchInput = new CoppaLaunchInput();
                urLaunchInput.setAccountSettings(true);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                urLaunchInput.setUserRegistrationUIEventListener(this);
                activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                urInterface = new CoppaInterface();
                urInterface.launch(activityLauncher, urLaunchInput);
                break;

            case R.id.btn_registration_without_account:
                RLog.d(RLog.ONCLICK, "RegistrationCoppaSampleActivity : Registration");
                RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");

                urLaunchInput = new CoppaLaunchInput();
                urLaunchInput.setAccountSettings(false);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                urLaunchInput.setUserRegistrationUIEventListener(this);
                activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                urInterface = new CoppaInterface();
                urInterface.launch(activityLauncher, urLaunchInput);

             //   RegistrationCoppaLaunchHelper.launchRegistrationActivityWithOutAccountSettings(this);
                break;

            case R.id.btn_refresh_user:
                RLog.d(RLog.ONCLICK, "RegistrationCoppaSampleActivity : Refresh User ");
                handleRefreshAccessToken();
                break;

            case R.id.btn_refresh_token:
                if (RegistrationConfiguration.getInstance().isHsdpFlow()) {
                    User user = new User(mContext);
                    if (!user.isUserSignIn()) {
                        Toast.makeText(this, "Please login before refreshing access token", Toast.LENGTH_LONG).show();
                    } else {
                        mProgressDialog.setMessage("Refreshing...");
                        mProgressDialog.show();
                        user.refreshLoginSession(this);
                    }
                }
                break;

            case R.id.btn_parental_consent:
                User user = new User(mContext);
                if(user.isUserSignIn()){
                    urLaunchInput = new CoppaLaunchInput();
                    urLaunchInput.setAccountSettings(true);
                    urLaunchInput.setParentalFragment(true);
                    urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                    urLaunchInput.setUserRegistrationUIEventListener(this);
                    activityLauncher = new ActivityLauncher(ActivityLauncher.
                            ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                    urInterface = new CoppaInterface();
                    urInterface.launch(activityLauncher, urLaunchInput);
                }else{
                    Toast.makeText(this, "Please login before accessing parental consent", Toast.LENGTH_LONG).show();
                }


            default:
                break;
        }

    }

    private void handleRefreshAccessToken() {

        final User user = new User(this);
        if (user.isUserSignIn()) {
            user.refreshLoginSession(new RefreshLoginSessionHandler() {
                @Override
                public void onRefreshLoginSessionSuccess() {
                    System.out.println("Access token : " + user.getAccessToken());
                    showToast("Success to refresh access token");
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int error) {
                    showToast("Failed to refresh access token");

                }

                @Override
                public void onRefreshLoginSessionInProgress(String message) {
                    System.out.println("Message " + message);
                    showToast(message);
                }
            });
        } else {
            Toast.makeText(this, "Plase login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity : onUserRegistrationComplete");
        if(activity != null) {
            activity.finish();
        }
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity : onPrivacyPolicyClick");
        showToast("This call back is for vertical");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + getResources().getString(
                com.philips.cdp.registration.R.string.reg_Philips_URL_txt)));
        activity.startActivity(browserIntent);
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity : onTermsAndConditionClick");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + getResources().getString(
                com.philips.cdp.registration.R.string.reg_Philips_URL_txt)));
        activity.startActivity(browserIntent);
    }

    @Override
    public void onUserLogoutSuccess() {
        RLog.d(RLog.HSDP, "RegistrationCoppaSampleActivity : onUserLogoutSuccess");
    }

    @Override
    public void onUserLogoutFailure() {
        RLog.d(RLog.HSDP, "  RegistrationCoppaSampleActivity : onUserLogoutFailure");
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        RLog.d(RLog.HSDP, "RegistrationCoppaSampleActivity  : onUserLogoutSuccessWithInvalidAccessToken");
        showToast("onUserLogoutSuccessWithInvalidAccessToken ");
    }

    @Override
    public void didResendCoppaEmailConsentSucess() {
        dimissDialog();
        showToast("Success to resend coppa mail");
        RLog.d(RLog.HSDP, "didResendCoppaEmailConsentSucess RegistratikonSampleActivity : Success");
    }

    @Override
    public void didResendCoppaEmailConsentFailedWithError(CoppaResendError coppaResendError) {
        dimissDialog();
        showToast("Failed to resend coppa mail");
        RLog.d(RLog.HSDP, "didResendCoppaEmailConsentFailedWithError RegistrationCoppaSampleActivity : failure");
    }

    private void dimissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    final Handler handler = new Handler();

    private void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegistrationCoppaSampleActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    User user;

    @Override
    public void onRefreshLoginSessionSuccess() {
        dimissDialog();
        showToast("Success to refresh hsdp access token");
    }

    @Override
    public void onRefreshLoginSessionFailedWithError(int error) {
        dimissDialog();
        if (error == Integer.parseInt(RegConstants.INVALID_ACCESS_TOKEN_CODE)
                || error == Integer.parseInt(RegConstants.INVALID_REFRESH_TOKEN_CODE)) {
            showToast("Failed to refresh hsdp Invalid access token");
            return;
        }
        showToast("Failed to refresh hsdp access token");
    }

    @Override
    public void onRefreshLoginSessionInProgress(String message) {
        showToast(message);
    }
}
