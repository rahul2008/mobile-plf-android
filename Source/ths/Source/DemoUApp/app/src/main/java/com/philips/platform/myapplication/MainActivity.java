package com.philips.platform.myapplication;




import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Button;

public class MainActivity extends UIDActivity {

    private Button mDemoButton;
    private ThemeConfiguration themeConfiguration;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeConfiguration = new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
        UIDHelper.init(themeConfiguration);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        setSupportActionBar(toolbar);
        UIDHelper.setTitle(this, "DemoApp");

        mDemoButton = (Button) findViewById(R.id.demo_micro_app_launch_amwell);

        mDemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDemoMicroApp();
            }
        });
    }

    private void launchDemoMicroApp() {
        final DemoMicroAppApplicationuAppDependencies uappDependencies = new DemoMicroAppApplicationuAppDependencies(((THSDemoApplication) this.getApplicationContext()).getAppInfra());
        ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT,new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE), R.style.Theme_DLS_GroupBlue_UltraLight, null);

        DemoMicroAppApplicationuAppInterface uAppInterface = new DemoMicroAppApplicationuAppInterface();
        uAppInterface.init(uappDependencies, new DemoMicroAppApplicationuAppSettings(this));// pass App-infra instance instead of null
        uAppInterface.launch(activityLauncher, null);// pass launch input if required
    }

}
