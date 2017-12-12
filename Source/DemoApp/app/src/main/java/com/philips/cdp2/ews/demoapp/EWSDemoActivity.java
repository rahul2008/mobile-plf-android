package com.philips.cdp2.ews.demoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.ews.demoapplication.microapp.DemoUapp;
import com.philips.cdp2.ews.demoapplication.microapp.DemoUappDependencies;
import com.philips.cdp2.ews.demoapplication.microapp.UAppActionBarListener;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener, UAppActionBarListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.btn_launch_ews).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_launch_ews:
                launchDemoUApp();
                break;
            default:
                break;
        }
    }


    private void launchDemoUApp() {
        DemoUappDependencies demoUappDependencies = new DemoUappDependencies(((DemoAppApplication) getApplication()).appInfraInterface) {
            @Override
            public CommCentral getCommCentral() {
                return ((DemoAppApplication) getApplication()).commCentral;
            }
        };
        DemoUapp demoUapp = new DemoUapp();
        demoUapp.init(demoUappDependencies, new UappSettings(getApplicationContext()));
        //its up to proposition to pass theme or not, if not passing theme then it will show default theme of library
        demoUapp.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, AccentRange.ORANGE, NavigationColor.BRIGHT), -1, null),
                (new EWSLauncherInput()));
    }

    @Override
    public void closeButton(boolean visibility) {
        //user can override close button visibility
    }

    @Override
    public void updateActionBar(int i, boolean b) {
        //user can override actionbar behaviour
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        //user can override actionbar behaviour
    }
}