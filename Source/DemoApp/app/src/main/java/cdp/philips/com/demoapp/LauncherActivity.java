/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package cdp.philips.com.demoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.dprdemo.uappdependencies.DevicePairingUappInterface;
import com.philips.platform.dprdemo.uappdependencies.DevicePairingUappSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class LauncherActivity extends AppCompatActivity {

    private DevicePairingUappInterface devicePairingUappInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        devicePairingUappInterface = new DevicePairingUappInterface();
        AppInfra gAppInfra = new AppInfra.Builder().build(getApplicationContext());

        UappDependencies uappDependencies = new UappDependencies(gAppInfra);
        devicePairingUappInterface.init(uappDependencies, new DevicePairingUappSettings(getApplicationContext()));
    }

    public void launchAsActivity(View v){
        devicePairingUappInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }

    public void launchAsFragment(View v){

    }
}
