package com.philips.platform.urdemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRSession;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.urdemolibrary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class URStandardDemoActivity extends AppCompatActivity implements View.OnClickListener,
        UserRegistrationUIEventListener, UserRegistrationListener, RefreshLoginSessionHandler {

    private Button mBtnRegistrationWithAccountSettings;
    private Button mBtnRegistrationMarketingOptIn;
    private Button mBtnRegistrationWithOutAccountSettings;
    private Button mBtnHsdpRefreshAccessToken;
    private Button mBtnRefresh;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    String restoredText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        setContentView(R.layout.usr_demoactivity);


        mBtnRegistrationWithAccountSettings = (Button) findViewById(R.id.usr_btn_registration_with_account);
        mBtnRegistrationWithAccountSettings.setOnClickListener(this);

        mBtnRegistrationMarketingOptIn = (Button) findViewById(R.id.usr_btn_marketing_opt_in);
        mBtnRegistrationMarketingOptIn.setOnClickListener(this);

        mBtnRegistrationWithOutAccountSettings = (Button) findViewById(R.id.usr_btn_registration_without_account);
        mBtnRegistrationWithOutAccountSettings.setOnClickListener(this);

        mBtnHsdpRefreshAccessToken = (Button) findViewById(R.id.usr_btn_refresh_token);
        mBtnHsdpRefreshAccessToken.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(URStandardDemoActivity.this);
        mProgressDialog.setCancelable(false);
        if (RegistrationConfiguration.getInstance().isHsdpFlow()) {
            mBtnHsdpRefreshAccessToken.setVisibility(View.VISIBLE);
        } else {
            mBtnHsdpRefreshAccessToken.setVisibility(View.GONE);
        }

        mUser = new User(mContext);
        mUser.registerUserRegistrationListener(this);
        mBtnRefresh = (Button) findViewById(R.id.usr_btn_refresh_user);
        mBtnRefresh.setOnClickListener(this);

        mBtnUpdateDOB = (Button) findViewById(R.id.usr_btn_update_date_of_birth);
        mBtnUpdateDOB.setOnClickListener(this);
        mBtnUpdateGender = (Button) findViewById(R.id.usr_btn_update_gender);
        mBtnUpdateGender.setOnClickListener(this);
        mRadioGender = (RadioGroup) findViewById(R.id.usr_genderRadio);
        mRadioGender.check(R.id.usr_Male);


        mLlConfiguration = (LinearLayout) findViewById(R.id.usr_ll_configuartion);
        mRadioGroup = (RadioGroup) findViewById(R.id.usr_myRadioGroup);
        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
        restoredText = prefs.getString("reg_environment", null);
        final String restoredHSDPText = prefs.getString("reg_hsdp_environment", null);
        if (restoredText != null) {

            switch (RegUtility.getConfiguration(restoredText)) {
                case EVALUATION:
                    mRadioGroup.check(R.id.usr_Evalution);
                    break;
                case DEVELOPMENT:
                    mRadioGroup.check(R.id.usr_Development);
                    break;
                case PRODUCTION:
                    mRadioGroup.check(R.id.usr_Production);
                    break;
                case STAGING:
                    mRadioGroup.check(R.id.usr_Stagging);
                    break;
                case TESTING:
                    mRadioGroup.check(R.id.usr_Testing);
                    break;
            }

        }

        mLlConfiguration.setVisibility(View.GONE);
        mBtnChangeConfiguaration = (Button) findViewById(R.id.usr_btn_change_configuration);
        mBtnChangeConfiguaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlConfiguration.setVisibility(View.VISIBLE);
            }
        });
        mBtnApply = (Button) findViewById(R.id.usr_Apply);
        mBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlConfiguration.setVisibility(View.GONE);

                //Resetn
                UserRegistrationInitializer.getInstance().resetInitializationState();
                //Logout mUser
                clearData();

                int checkedId = mRadioGroup.getCheckedRadioButtonId();
                // find which radio button is selected

                if (checkedId == R.id.usr_Evalution) {
                    Toast.makeText(getApplicationContext(), "choice: Evalution",
                            Toast.LENGTH_SHORT).show();
                    restoredText = Configuration.EVALUATION.getValue();
                } else if (checkedId == R.id.usr_Testing) {
                    Toast.makeText(getApplicationContext(), "choice: Testing",
                            Toast.LENGTH_SHORT).show();
                    restoredText = Configuration.TESTING.getValue();
                } else if (checkedId == R.id.usr_Development) {
                    Toast.makeText(getApplicationContext(), "choice: Development",
                            Toast.LENGTH_SHORT).show();
                    restoredText = Configuration.DEVELOPMENT.getValue();
                } else if (checkedId == R.id.usr_Production) {
                    Toast.makeText(getApplicationContext(), "choice: Production",
                            Toast.LENGTH_SHORT).show();
                    restoredText = Configuration.PRODUCTION.getValue();
//                    RegistrationSampleApplication.getInstance().initRegistration(Configuration.PRODUCTION);
                } else if (checkedId == R.id.usr_Stagging) {
                    Toast.makeText(getApplicationContext(), "choice: Stagging",
                            Toast.LENGTH_SHORT).show();
                    restoredText = Configuration.STAGING.getValue();
                }

                if (restoredText != null) {
                    if (mCheckBox.isChecked()) {
//                        RegistrationSampleApplication.getInstance().initHSDP(RegUtility.getConfiguration(restoredText));
                    } else {
                        SharedPreferences prefs = getSharedPreferences("reg_dynamic_config", MODE_PRIVATE);
                        prefs.edit().remove("reg_hsdp_environment").commit();
                    }

//                    RegistrationSampleApplication.getInstance().initRegistration(RegUtility.getConfiguration(restoredText));
                }

            }
        });
        mBtnCancel = (Button) findViewById(R.id.usr_Cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlConfiguration.setVisibility(View.GONE);
            }
        });


        mCheckBox = (CheckBox) findViewById(R.id.usr_cd_hsdp);
        if (restoredHSDPText != null) {
            mCheckBox.setChecked(true);
        }
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
    public void onClick(View v) {
        URLaunchInput urLaunchInput;
        ActivityLauncher activityLauncher;
        URInterface urInterface;
        int viewId = v.getId();
        if (viewId == R.id.usr_btn_registration_with_account) {
            RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
            urLaunchInput = new URLaunchInput();
            urLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
            urLaunchInput.setAccountSettings(true);
            urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
            urLaunchInput.setUserRegistrationUIEventListener(this);
            activityLauncher = new ActivityLauncher(ActivityLauncher.
                    ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);
            urInterface = new URInterface();
            urInterface.launch(activityLauncher, urLaunchInput);
            final UIFlow abTestingUIFlow = RegUtility.getUiFlow();
            switch (abTestingUIFlow) {
                case FLOW_A:
                    Toast.makeText(mContext, "UI Flow Type A", Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                    break;
                case FLOW_B:
                    Toast.makeText(mContext, "UI Flow Type B", Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                    break;
                case FLOW_C:
                    Toast.makeText(mContext, "UI Flow Type C", Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                    break;
                default:
                    break;
            }

        } else if (viewId == R.id.usr_btn_marketing_opt_in) {
            RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
            urLaunchInput = new URLaunchInput();
            urLaunchInput.setEndPointScreen(RegistrationLaunchMode.MARKETING_OPT);
            urLaunchInput.setAccountSettings(false);
            urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
            urLaunchInput.setUserRegistrationUIEventListener(this);
            activityLauncher = new ActivityLauncher(ActivityLauncher.
                    ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

            urInterface = new URInterface();
            urInterface.launch(activityLauncher, urLaunchInput);
            final UIFlow uiFlow = RegUtility.getUiFlow();

            switch (uiFlow) {

                case FLOW_A:
                    Toast.makeText(mContext, "UI Flow Type A", Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING, "UI Flow Type A");
                    break;
                case FLOW_B:
                    Toast.makeText(mContext, "UI Flow Type B", Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING, "UI Flow Type B");
                    break;
                case FLOW_C:
                    Toast.makeText(mContext, "UI Flow Type C", Toast.LENGTH_LONG).show();
                    RLog.d(RLog.AB_TESTING, "UI Flow Type C");
                    break;
                default:
                    break;
            }

        } else if (viewId == R.id.usr_btn_registration_without_account) {
            RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
            urLaunchInput = new URLaunchInput();
            urLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
            urLaunchInput.setUserRegistrationUIEventListener(this);
            urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
            urLaunchInput.setAccountSettings(false);
            activityLauncher = new ActivityLauncher(ActivityLauncher.
                    ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null,0,null);
            urInterface = new URInterface();
            urInterface.launch(activityLauncher, urLaunchInput);

        } else if (viewId == R.id.usr_btn_refresh_user) {
            RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Refresh User ");
            handleRefreshAccessToken();

        } else if (viewId == R.id.usr_btn_refresh_token) {
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

        } else if (viewId == R.id.usr_btn_update_gender) {
            User user1 = new User(mContext);
            if (!user1.isUserSignIn()) {
                Toast.makeText(this, "Please login before refreshing access token", Toast.LENGTH_LONG).show();
            } else {
                handleGender();
            }


        } else if (viewId == R.id.usr_btn_update_date_of_birth) {
            User user = new User(mContext);
            if (!user.isUserSignIn()) {
                Toast.makeText(this, "Please login before updating user", Toast.LENGTH_LONG).show();
            } else {
                handleDoBUpdate(user.getDateOfBirth());
            }

        } else {
        }
    }

    private void handleGender() {

        mProgressDialog.setMessage("Updating...");
        mProgressDialog.show();
        Gender gender;

        if (mRadioGender.getCheckedRadioButtonId() == R.id.usr_Male) {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }

        final User user1 = new User(mContext);
        user1.updateGender(new UpdateUserDetailsHandler() {
            @Override
            public void onUpdateSuccess() {
                mProgressDialog.hide();
                showToast("onUpdateSuccess");
            }

            @Override
            public void onUpdateFailedWithError(int error) {
                mProgressDialog.hide();
                showToast("onUpdateFailedWithError" + error);
            }
        }, gender);


    }

    private void handleDoBUpdate(Date userDOB) {
        int year, month, day;
        Calendar calendar = new GregorianCalendar();
        if (userDOB != null) {
            calendar.setTime(userDOB);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
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

                        final User user1 = new User(mContext);
                        user1.updateDateOfBirth(new UpdateUserDetailsHandler() {
                            @Override
                            public void onUpdateSuccess() {
                                mProgressDialog.hide();
                                showToast("onUpdateSuccess");
                            }

                            @Override
                            public void onUpdateFailedWithError(int error) {
                                mProgressDialog.hide();
                                showToast("onUpdateFailedWithError" + error);
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
                    showToast("Success to refresh access token"+user.getHsdpAccessToken());
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
                Toast.makeText(URStandardDemoActivity.this, msg, Toast.LENGTH_LONG).show();
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
