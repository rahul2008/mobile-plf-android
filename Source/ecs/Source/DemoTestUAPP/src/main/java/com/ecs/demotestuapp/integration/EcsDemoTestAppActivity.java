
package com.ecs.demotestuapp.integration;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.adapter.EcsExpandableListAdapter;
import com.ecs.demotestuapp.model.Config;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.view.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;



public class EcsDemoTestAppActivity extends AppCompatActivity implements View.OnClickListener,
        UserRegistrationUIEventListener {

    private Button mRegister;

    private UserDataInterface mUserDataInterface;


    URInterface urInterface;
    private long mLastClickTime = 0;

    private ECSServices ecsServices;

    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        urInterface = new URInterface();
        urInterface.init(new EcsDemoTestUAppDependencies(new AppInfra.Builder().build(getApplicationContext())), new EcsDemoTestAppSettings(getApplicationContext()));

        setContentView(R.layout.demo_app_test_layout);
        mRegister = findViewById(R.id.btn_register);

        showAppVersion();

        mUserDataInterface = urInterface.getUserDataInterface();
        actionBar();

        Config config = readConfigJsonFile("testDemoUAPP.json");

        expandableListView = findViewById(R.id.expandable_list);

        EcsExpandableListAdapter expandableListAdapter = new EcsExpandableListAdapter(this,config.buttonConfig);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private Config readConfigJsonFile(String file) {

        Config config = new Gson().fromJson(loadJSONFromAsset(file), Config.class);
        return config;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeRegistrationComponant();
    }

    private void initializeRegistrationComponant() {
        if (isUserLoggedIn()) {
            mRegister.setText("Log out");
        } else {
            mRegister.setText("Log in");
        }
    }


    private void actionBar() {
        setTitle("ECS Demo Test App");
    }

    @Override
    public void onClick(final View view) {
        if (!isClickable()) return;

    }

    private void gotoLogInScreen() {

        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.enableAddtoBackStack(true);
        RegistrationContentConfiguration contentConfiguration = new RegistrationContentConfiguration();
        contentConfiguration.enableLastName(true);
        contentConfiguration.enableContinueWithouAccount(true);
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        urLaunchInput.setRegistrationContentConfiguration(contentConfiguration);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);


        ActivityLauncher activityLauncher = new ActivityLauncher(this, ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null, 0, null);
        urInterface.launch(activityLauncher, urLaunchInput);


    }


    private void showAppVersion() {
        String code = null;
        try {
            code = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        TextView versionView = findViewById(R.id.appversion);
        versionView.setText(String.valueOf(code));
    }

    //User Registration interface functions
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        activity.finish();
        mRegister.setText("Log out");
        initializeRegistrationComponant();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }



    boolean isClickable() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        return true;
    }

    public JSONObject getResponseJson(String fileName) {

        String jsonString = loadJSONFromAsset(fileName);

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return json;
    }


    boolean isUserLoggedIn(){
        return  mUserDataInterface != null && mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN ;
    }

    private void register(){
        if (mRegister.getText().toString().equalsIgnoreCase("Log out")) {
            if (mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
                mUserDataInterface.logoutSession(new LogoutSessionListener() {
                    @Override
                    public void logoutSessionSuccess() {
                        finish();
                    }

                    @Override
                    public void logoutSessionFailed(Error error) {
                        Toast.makeText(EcsDemoTestAppActivity.this, "Logout went wrong", Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                Toast.makeText(EcsDemoTestAppActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
            }
        } else {

            gotoLogInScreen();
        }

    }

    public void register(View view) {
        register();
    }
}
