
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRSession;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.Calendar;

public class RegistrationSampleActivity extends Activity implements OnClickListener,
        UserRegistrationUIEventListener, UserRegistrationListener, RefreshLoginSessionHandler {

    private Button mBtnRegistrationWithAccountSettings;
    private Button mBtnRegistrationWithOutAccountSettings;
    private Button mBtnHsdpRefreshAccessToken;
    private Button mBtnRefresh;
    private Context mContext;
    private ProgressDialog mProgressDialog;


    //Configuartion

    private Button mBtnChangeConfiguaration;
    private Button mBtnApply;
    private Button mBtnCancel;
    private Button mBtnUpdateDOB;
    private Button mBtnUpdateGender;
    private RadioGroup mRadioGender;

    private LinearLayout mLlConfiguration;

    private RadioGroup mRadioGroup;
    private CheckBox mCheckBox;
    private User mUser;
    private RadioGroup mABTestingRadioGroup;
    private RadioButton radioABButton;

    private boolean abtesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        setContentView(R.layout.activity_main);


        mBtnRegistrationWithAccountSettings = (Button) findViewById(R.id.btn_registration_with_account);
        mBtnRegistrationWithAccountSettings.setOnClickListener(this);

        mBtnRegistrationWithOutAccountSettings = (Button) findViewById(R.id.btn_registration_without_account);
        mBtnRegistrationWithOutAccountSettings.setOnClickListener(this);

        mBtnHsdpRefreshAccessToken = (Button) findViewById(R.id.btn_refresh_token);
        mBtnHsdpRefreshAccessToken.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(RegistrationSampleActivity.this);
        mProgressDialog.setCancelable(false);
        if (RegistrationConfiguration.getInstance().isHsdpFlow()) {
            mBtnHsdpRefreshAccessToken.setVisibility(View.VISIBLE);
        } else {
            mBtnHsdpRefreshAccessToken.setVisibility(View.GONE);
        }

        mUser = new User(mContext);
        mUser.registerUserRegistrationListener(this);
        mBtnRefresh = (Button) findViewById(R.id.btn_refresh_user);
        mBtnRefresh.setOnClickListener(this);

        mBtnUpdateDOB = (Button) findViewById(R.id.btn_update_date_of_birth);
        mBtnUpdateDOB.setOnClickListener(this);
        mBtnUpdateGender = (Button) findViewById(R.id.btn_update_gender);
        mBtnUpdateGender.setOnClickListener(this);
        mRadioGender = (RadioGroup) findViewById(R.id.genderRadio);
        mRadioGender.check(R.id.Male);


        mLlConfiguration = (LinearLayout) findViewById(R.id.ll_configuartion);
        mRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        mABTestingRadioGroup= (RadioGroup) findViewById(R.id.abTestingRadio);

        mABTestingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton abtesting=(RadioButton)findViewById(R.id.Atesting);
                if (abtesting.isChecked()){
                    RegConstants.AB_TESTING=true;
                }
                else{
                    RegConstants.AB_TESTING=false;
                }
            }
        });


        /*if (mABTestingRadioGroup.getCheckedRadioButtonId() == R.id.Atesting) {
            abtesting = RegConstants.A_TESTING;
        } else {
            abtesting = RegConstants.B_TESTING;
        }*/

        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        final String restoredText = prefs.getString("reg_environment", null);
        final String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
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
                //Logout mUser
                clearData();

                int checkedId = mRadioGroup.getCheckedRadioButtonId();
                // find which radio button is selected

                if (checkedId == R.id.Evalution) {
                    Toast.makeText(getApplicationContext(), "choice: Evalution",
                            Toast.LENGTH_SHORT).show();
                    RegistrationApplication.getInstance().initRegistration(Configuration.EVALUATION);
                } else if (checkedId == R.id.Testing) {
                    Toast.makeText(getApplicationContext(), "choice: Testing",
                            Toast.LENGTH_SHORT).show();
                    RegistrationApplication.getInstance().initRegistration(Configuration.TESTING);
                } else if (checkedId == R.id.Development) {
                    Toast.makeText(getApplicationContext(), "choice: Development",
                            Toast.LENGTH_SHORT).show();
                    RegistrationApplication.getInstance().initRegistration(Configuration.DEVELOPMENT);
                } else if (checkedId == R.id.Production) {
                    Toast.makeText(getApplicationContext(), "choice: Production",
                            Toast.LENGTH_SHORT).show();
                    RegistrationApplication.getInstance().initRegistration(Configuration.PRODUCTION);
                } else if (checkedId == R.id.Stagging) {
                    Toast.makeText(getApplicationContext(), "choice: Stagging",
                            Toast.LENGTH_SHORT).show();
                    RegistrationApplication.getInstance().initRegistration(Configuration.STAGING);
                }

                if (mCheckBox.isChecked()) {
                    if (restoredText != null) {
                        RegistrationApplication.getInstance().initHSDP(RegUtility.getConfiguration(restoredText));
                    }


                } else {
                    SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                    prefs.edit().remove("reg_hsdp_environment").commit();
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


        mCheckBox = (CheckBox) findViewById(R.id.cd_hsdp);
        if (restoredHSDPText != null) {
            mCheckBox.setChecked(true);
        }


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
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        AppTagging.collectLifecycleData(this);
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onResume");
        super.onResume();

        /** Should be commented for debug builds */
        CrashManager.register(this, RegConstants.HOCKEY_APPID, new CrashManagerListener() {

            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        AppTagging.pauseCollectingLifecycleData();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStop");

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mUser.unRegisterUserRegistrationListener(this);
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity unregister : RegisterUserRegistrationListener");
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        URLaunchInput urLaunchInput;
        ActivityLauncher activityLauncher;
        URInterface urInterface;
        switch (v.getId()) {
            case R.id.btn_registration_with_account:
                RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
                RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");

                urLaunchInput = new URLaunchInput();
                urLaunchInput.setAccountSettings(true);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                urLaunchInput.setUserRegistrationUIEventListener(this);
                activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                urInterface = new URInterface();
                urInterface.launch(activityLauncher, urLaunchInput);
                if (RegUtility.isUiFirstFlow()){
                    Toast.makeText(mContext,"UI Flow Type A",Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING,"UI Flow Type A");
                }else {
                    Toast.makeText(mContext,"UI Flow Type B",Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING,"UI Flow Type B");
                }
                //RegistrationLaunchHelper.launchDefaultRegistrationActivity(this);
                break;

            case R.id.btn_registration_without_account:


                RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
                RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");

                urLaunchInput = new URLaunchInput();
                urLaunchInput.setAccountSettings(false);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
                urLaunchInput.setUserRegistrationUIEventListener(this);

                activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);
                urInterface = new URInterface();
                urInterface.launch(activityLauncher, urLaunchInput);

                // RegistrationLaunchHelper.launchRegistrationActivityWithOutAccountSettings(this);
                break;

            case R.id.btn_refresh_user:
                RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Refresh User ");
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

            case R.id.btn_update_gender:


                    User user1 = new User(mContext);
                System.out.println("before login"+user1.getGender());

                if (!user1.isUserSignIn()) {
                        Toast.makeText(this, "Please login before refreshing access token", Toast.LENGTH_LONG).show();
                    } else {
                    System.out.println("preset login"+user1.getGender());
                        handleGender();
                    }

                break;

            case R.id.btn_update_date_of_birth:
                User user = new User(mContext);
                System.out.println("before login"+user.getDateOfBirth());
                if (!user.isUserSignIn()) {
                    Toast.makeText(this, "Please login before updating user", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("pre  login"+user.getDateOfBirth());
                    handleDoBUpdate();


                }
                break;

            default:
                break;
        }
    }

    private void handleGender() {

        mProgressDialog.setMessage("Updating...");
        mProgressDialog.show();
        Gender gender;

        if (mRadioGender.getCheckedRadioButtonId() == R.id.Male) {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }

        final User user1 = new User(mContext);
        user1.updateGender(new UpdateUserDetailsHandler() {
            @Override
            public void onUpdateSuccess() {
                System.out.println("onUpdateSuccess");
                mProgressDialog.hide();
                showToast("onUpdateSuccess");
                System.out.println("post login"+user1.getGender());
            }

            @Override
            public void onUpdateFailedWithError(int error) {
                System.out.println("onUpdateFailedWithError");
                mProgressDialog.hide();
                showToast("onUpdateFailedWithError" + error);
                System.out.println("post login"+user1.getGender());

            }
        }, gender);


    }

    private void handleDoBUpdate() {
        final int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mProgressDialog.setMessage("Updating...");
                        mProgressDialog.show();

                        Calendar c = Calendar.getInstance();
                        c.set(year, monthOfYear, dayOfMonth, 0, 0);
                        System.out.println("date" + c.getTime());

                        System.out.println("onDateSet" + year + monthOfYear + dayOfMonth);
                        final User user1 = new User(mContext);
                        user1.updateDateOfBirth(new UpdateUserDetailsHandler() {
                            @Override
                            public void onUpdateSuccess() {
                                System.out.println("onUpdateSuccess");
                                mProgressDialog.hide();
                                showToast("onUpdateSuccess");
                                System.out.println("post  login"+user1.getDateOfBirth());
                            }

                            @Override
                            public void onUpdateFailedWithError(int error) {
                                System.out.println("onUpdateFailedWithError");
                                mProgressDialog.hide();
                                showToast("onUpdateFailedWithError" + error);
                                System.out.println("post  login"+user1.getDateOfBirth());

                            }
                        }, c.getTime());
                    }
                }, year, month, day);
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

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

                ;

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
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserRegistrationComplete");
        activity.finish();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onPrivacyPolicyClick");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + getResources().getString(
                com.philips.cdp.registration.R.string.reg_Philips_URL_txt)));
        activity.startActivity(browserIntent);
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onTermsAndConditionClick");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + getResources().getString(
                com.philips.cdp.registration.R.string.reg_Philips_URL_txt)));
        activity.startActivity(browserIntent);
    }

    @Override
    public void onUserLogoutSuccess() {
        RLog.d(RLog.HSDP, "RegistrationSampleActivity : onUserLogoutSuccess");
    }

    @Override
    public void onUserLogoutFailure() {
        RLog.d(RLog.HSDP, "  RegistrationSampleActivity : onUserLogoutFailure");
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        RLog.d(RLog.HSDP, "RegistrationSampleActivity  : onUserLogoutSuccessWithInvalidAccessToken");
        showToast("onUserLogoutSuccessWithInvalidAccessToken ");
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
                Toast.makeText(RegistrationSampleActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onRefreshLoginSessionSuccess() {
        dimissDialog();
        RLog.d(RLog.HSDP, "RegistrationSampleActivity Access token: " + mUser.getHsdpAccessToken());
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
