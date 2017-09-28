
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.coppa.registration;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.janrain.android.*;
import com.janrain.android.engage.session.*;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.coppa.base.*;
import com.philips.cdp.registration.coppa.utils.*;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.hsdp.*;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.appconfiguration.*;
import com.philips.platform.uappframework.launcher.*;

public class RegistrationCoppaSampleActivity extends Activity implements OnClickListener,
        UserRegistrationListener,
        UserRegistrationUIEventListener,
        RefreshLoginSessionHandler, ResendCoppaEmailConsentHandler {

    private Button mBtnRegistrationWithAccountSettings;
    private Button mBtnRegistrationMarketingOptIn;
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
    private User mUser;
    private Switch mCountrySelectionSwitch;
    private boolean isCountrySelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity register: UserRegistrationCoppaListener");
        setContentView(R.layout.activity_main);
        mUser = new User(mContext);
        mUser.registerUserRegistrationListener(this);
        mBtnRegistrationWithAccountSettings = (Button) findViewById(R.id.btn_registration_with_account);
        mBtnRegistrationWithAccountSettings.setOnClickListener(this);
        mBtnRegistrationMarketingOptIn = (Button) findViewById(R.id.btn_marketing_opt_in);
        mBtnRegistrationMarketingOptIn.setOnClickListener(this);
        mBtnParentalConsent = (Button) findViewById(R.id.btn_parental_consent);
        mBtnParentalConsent.setOnClickListener(this);
        mBtnRegistrationWithOutAccountSettings = (Button) findViewById(R.id.btn_registration_without_account);
        mBtnRegistrationWithOutAccountSettings.setOnClickListener(this);
        mCountrySelectionSwitch = (Switch) findViewById(R.id.county_selection_switch);
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

        mCountrySelectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCountrySelection = isChecked;
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
        Jump.getSecureStorageInterface().removeValueForKey(RegConstants.DI_PROFILE_FILE);
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
        mUser.unRegisterUserRegistrationListener(this);
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationCoppaSampleActivity unregister : RegisterUserRegistrationListener");
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        CoppaLaunchInput urLaunchInput;
        ActivityLauncher activityLauncher;
        CoppaInterface urInterface;
        initCountrySelection();
        switch (v.getId()) {

            case R.id.btn_registration_with_account:
            user.logout(null);
                break;

            case R.id.btn_marketing_opt_in:
                RLog.d(RLog.ONCLICK, "RegistrationCoppaSampleActivity : Registration");
                RegistrationCoppaApplication.getInstance().getAppInfra().getTagging().setPreviousPage("demoapp:home");
                urLaunchInput = new CoppaLaunchInput();
                urLaunchInput.setEndPointScreen(RegistrationLaunchMode.MARKETING_OPT);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                urLaunchInput.setUserRegistrationUIEventListener(this);
                urLaunchInput.setUIFlow(UIFlow.FLOW_B);
                urLaunchInput.setRegistrationContentConfiguration(getRegistrationContentConfiguration());
                activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                urInterface = new CoppaInterface();
                urInterface.launch(activityLauncher, urLaunchInput);
                break;

            case R.id.btn_registration_without_account:
                RLog.d(RLog.ONCLICK, "RegistrationCoppaSampleActivity : Registration");
                RegistrationCoppaApplication.getInstance().getAppInfra().getTagging().setPreviousPage("demoapp:home");
                urLaunchInput = new CoppaLaunchInput();
                urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                urLaunchInput.setUIFlow(UIFlow.FLOW_B);
                urLaunchInput.setUserRegistrationUIEventListener(this);
                activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                urInterface = new CoppaInterface();
                urInterface.launch(activityLauncher, urLaunchInput);
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
                if (user.isUserSignIn()) {
                    urLaunchInput = new CoppaLaunchInput();
                    urLaunchInput.setParentalFragment(true);
                    urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
                    urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                    urLaunchInput.setUserRegistrationUIEventListener(this);
                    activityLauncher = new ActivityLauncher(ActivityLauncher.
                            ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                    urInterface = new CoppaInterface();
                    urInterface.launch(activityLauncher, urLaunchInput);
                } else {
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
                    showToast("Success to refresh access token");
                }

                @Override
                public void onRefreshLoginSessionFailedWithError(int error) {
                    showToast("Failed to refresh access token");

                }

                @Override
                public void onRefreshLoginSessionInProgress(String message) {
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
        if (activity != null) {
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

    private void initCountrySelection() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String countrySelection = isCountrySelection ? "true" : "false";
        RegistrationCoppaApplication.getInstance().getAppInfra().getConfigInterface().setPropertyForKey(
                URConfigurationConstants.SHOW_COUNTRY_SELECTION,
                URConfigurationConstants.UR,
                countrySelection,
                configError);
    }

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

    RegistrationContentConfiguration registrationContentConfiguration;

    public RegistrationContentConfiguration getRegistrationContentConfiguration() {
        String valueForRegistrationTitle = "sample";
        String valueForEmailVerification = "sample";
        String optInTitleText = getResources().getString(R.string.reg_DLS_OptIn_Navigation_Bar_Title);
        String optInQuessionaryText = getResources().getString(R.string.reg_DLS_OptIn_Header_Label);
        String optInDetailDescription = getResources().getString(R.string.reg_DLS_Optin_Body_Line1);
        String optInBannerText = getResources().getString(R.string.reg_Opt_In_Join_Now);
        String optInTitleBarText = getResources().getString(R.string.reg_DLS_OptIn_Navigation_Bar_Title);
        RegistrationContentConfiguration registrationContentConfiguration = new RegistrationContentConfiguration();
        registrationContentConfiguration.setValueForEmailVerification(valueForEmailVerification);
        registrationContentConfiguration.setOptInTitleText(optInTitleText);
        registrationContentConfiguration.setOptInQuessionaryText(optInQuessionaryText);
        registrationContentConfiguration.setOptInDetailDescription(optInDetailDescription);
        registrationContentConfiguration.setOptInBannerText(optInBannerText);
        registrationContentConfiguration.setOptInActionBarText(optInTitleBarText);
        return registrationContentConfiguration;
    }
}
