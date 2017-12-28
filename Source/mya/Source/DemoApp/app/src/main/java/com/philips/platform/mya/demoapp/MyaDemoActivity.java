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

import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.demouapp.MyAccountDemoUAppInterface;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.themesettings.ThemeSettingsActivity;

public class MyaDemoActivity extends UIDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myademo);

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
        MyaDemoApplication applicationContext = (MyaDemoApplication) getApplicationContext();
        MyaDependencies uappDependencies = new MyaDependencies(applicationContext.getAppInfra(), MyaHelper.getInstance().getConsentConfigurationList());
        myAccountDemoUAppInterface.init(uappDependencies, new UappSettings(this));
        myAccountDemoUAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, null, -1, null), null);
    }
}
