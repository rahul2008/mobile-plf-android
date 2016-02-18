
package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.CoppaExtension;
import com.philips.cdp.registration.coppa.CoppaResendError;
import com.philips.cdp.registration.coppa.ResendCoppaEmailConsentHandler;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;
import com.philips.cdp.tagging.Tagging;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

public class RegistrationSampleActivity extends Activity implements OnClickListener,
        UserRegistrationListener, RefreshLoginSessionHandler, ResendCoppaEmailConsentHandler{

    private Button mBtnRegistrationWithAccountSettings;
    private Button mBtnRegistrationWithOutAccountSettings;
    private Button mBtnHsdpRefreshAccessToken;
    private Button mBtnResendCoppaMail;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        setContentView(R.layout.activity_main);
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        mBtnRegistrationWithAccountSettings = (Button) findViewById(R.id.btn_registration_with_account);
        mBtnRegistrationWithAccountSettings.setOnClickListener(this);

        mBtnRegistrationWithOutAccountSettings = (Button) findViewById(R.id.btn_registration_without_account);
        mBtnRegistrationWithOutAccountSettings.setOnClickListener(this);

        mBtnHsdpRefreshAccessToken = (Button) findViewById(R.id.btn_refresh_token);
        mBtnHsdpRefreshAccessToken.setOnClickListener(this);
        mBtnResendCoppaMail = (Button) findViewById(R.id.btn_resend_coppa_email);
        mBtnResendCoppaMail.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(RegistrationSampleActivity.this);
        mProgressDialog.setCancelable(false);
     //  if (RegistrationHelper.getInstance().isHsdpFlow()) {
            mBtnHsdpRefreshAccessToken.setVisibility(View.GONE);
      //  }
        if (RegistrationConfiguration.getInstance().isCoppaFlow()) {
            mBtnResendCoppaMail.setVisibility(View.VISIBLE);
        }
        user = new User(mContext);
    }

    @Override
    protected void onStart() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Tagging.collectLifecycleData();
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
        Tagging.pauseCollectingLifecycleData();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStop");

        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity unregister : RegisterUserRegistrationListener");
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registration_with_account:
                RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
                RegistrationLaunchHelper.launchDefaultRegistrationActivity(this);
                break;

            case R.id.btn_registration_without_account:
                RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
                RegistrationLaunchHelper.launchRegistrationActivityWithOutAccountSettings(this);
                break;

            case R.id.btn_refresh_token:
                if (RegistrationConfiguration.getInstance().getHsdpConfiguration().isHsdpFlow()) {
                    User user = new User(mContext);
                    if (!user.isUserSignIn(mContext)) {
                        Toast.makeText(this, "Please login before refreshing access token", Toast.LENGTH_LONG).show();
                    } else {
                        mProgressDialog.setMessage("Refreshing...");
                        mProgressDialog.show();
                        user.refreshLoginSession(this,mContext);
                    }
                }
                break;
            case R.id.btn_resend_coppa_email:
                User user = new User(mContext);
                DIUserProfile userProfile = user.getUserInstance(mContext);
                CoppaExtension coppaExtension = new CoppaExtension();
                if (null != userProfile) {
                    mProgressDialog.setMessage("sending...");
                    mProgressDialog.show();
                    coppaExtension.resendCoppaEmailConsentForUserEmail(userProfile.getEmail(), this);
                } else {
                    Toast.makeText(this, "Please login b4 going to resend coppa mail", Toast.LENGTH_LONG).show();
                }
            default:
                break;
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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+getResources().getString(
                com.philips.cdp.registration.R.string.Philips_URL_txt)));
        activity.startActivity(browserIntent);
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onTermsAndConditionClick");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+getResources().getString(
                com.philips.cdp.registration.R.string.Philips_URL_txt)));
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
        RLog.d(RLog.HSDP, "didResendCoppaEmailConsentFailedWithError RegistrationSampleActivity : failure");
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
    User user ;

    @Override
    public void onRefreshLoginSessionSuccess() {
        dimissDialog();
        RLog.d(RLog.HSDP, "RegistrationSampleActivity Access token: "+user.getUserInstance(mContext).getHsdpAccessToken());
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
}
