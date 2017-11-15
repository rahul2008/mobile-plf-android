package cdp.philips.com.mydemoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class LauncherActivity extends Activity {
    private DSDemoAppuAppInterface dsDemoAppuAppInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
    }

    public void launch(View v) {
        dsDemoAppuAppInterface.init(new UappDependencies(DemoApplication.getInstance().getAppInfra()), new UappSettings(getApplicationContext()));
        dsDemoAppuAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);
    }
}
