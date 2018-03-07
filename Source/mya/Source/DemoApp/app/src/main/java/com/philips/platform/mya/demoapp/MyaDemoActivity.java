/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.demoapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.mya.demouapp.MyAccountDemoUAppInterface;
import com.philips.platform.mya.demouapp.MyaDemouAppLaunchInput;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.themesettings.ThemeSettingsActivity;
import java.util.List;

public class MyaDemoActivity extends UIDActivity {

    private AppInfra appInfra;
    private MyaDemoApplication applicationContext;
    private List<ConsentConfiguration> consentConfigurationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myademo);
        applicationContext = (MyaDemoApplication) getApplicationContext();
        appInfra = (AppInfra) applicationContext.getAppInfra();
        Button changeTheme = (Button) findViewById(R.id.change_theme);
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyaDemoActivity.this, ThemeSettingsActivity.class);
                startActivity(intent);
            }
        });
        setMyaccountFlow();
    }

    private void setMyaccountFlow() {
        TextView myAccountFlow = (TextView) findViewById(R.id.myademo_mainScreen_text);
        myAccountFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch();
            }
        });
    }

    public void launch() {
        MyAccountDemoUAppInterface myAccountDemoUAppInterface = new MyAccountDemoUAppInterface();
        myAccountDemoUAppInterface.init(new UappDependencies(appInfra),new URSettings(this));
        myAccountDemoUAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_NOSENSOR,null,-1,null),new MyaDemouAppLaunchInput(applicationContext.getUserDataInterface(),applicationContext.getUserObject(this)));

    }



}
