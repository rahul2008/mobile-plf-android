package com.philips.platform.pthdemolaunch;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.amwelluapp.uappclasses.PTHMicroAppDependencies;
import com.philips.amwelluapp.uappclasses.PTHMicroAppInterface;
import com.philips.amwelluapp.uappclasses.PTHMicroAppLaunchInput;
import com.philips.amwelluapp.uappclasses.PTHMicroAppSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

public class MainActivity extends FragmentActivity implements ActionBarListener{

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_UltraLight;

    private FragmentLauncher fragmentLauncher;
    private PTHMicroAppLaunchInput PTHMicroAppLaunchInput;
    private PTHMicroAppInterface PTHMicroAppInterface;

    private TextView mTitleTextView;
    private ImageView mBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pth_launch_activity);
        addActionBar();
        initAppInfra();
        fragmentLauncher = new FragmentLauncher(this,R.id.uappFragmentLayout,this);
        PTHMicroAppLaunchInput = new PTHMicroAppLaunchInput("Launch Uapp Input");
        PTHMicroAppInterface = new PTHMicroAppInterface();
        PTHMicroAppInterface.init(new PTHMicroAppDependencies(((AmwellDemoApplication)this.getApplicationContext()).getAppInfra()),new PTHMicroAppSettings(this.getApplicationContext()));
        PTHMicroAppInterface.launch(fragmentLauncher, PTHMicroAppLaunchInput);

    }

    private void addActionBar() {
        RelativeLayout frameLayout = (RelativeLayout) findViewById(R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mBackImage = (ImageView) findViewById(R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.uid_back_icon,getTheme());
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = (TextView) findViewById(R.id.iap_actionBar_headerTitle_lebel);
        setTitle("Am well");

    }
    private void initAppInfra() {
        ((AmwellDemoApplication)getApplicationContext()).initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {

            }
        });
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        mTitleTextView.setText(i);
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        mTitleTextView.setText(s);
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT));
    }
}
