package cdp.philips.com.mydemoapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import cdp.philips.com.DSDemoAppuAppInterface;
import cdp.philips.com.TestClass;

/**
 * Created by philips on 6/20/17.
 */

public class LuncherActivity extends Activity {

    private DSDemoAppuAppInterface dsDemoAppuAppInterface;
    private AppInfra gAppInfra;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luncher_activity);
        TestClass testClass = new TestClass();
        dsDemoAppuAppInterface = new DSDemoAppuAppInterface();
        gAppInfra = new AppInfra.Builder().build(getApplicationContext());
    }

    public void lunch(View v) {
        dsDemoAppuAppInterface.init(new UappDependencies(gAppInfra), new UappSettings(getApplicationContext()));
        dsDemoAppuAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), null);

    }
}
