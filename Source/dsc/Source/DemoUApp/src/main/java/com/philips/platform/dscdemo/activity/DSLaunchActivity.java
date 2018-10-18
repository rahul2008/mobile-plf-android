package com.philips.platform.dscdemo.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.UserLoginState;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.database.DatabaseHelper;
import com.philips.platform.dscdemo.moments.MomentFragment;
import com.philips.platform.dscdemo.utility.DemoUAppLog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.sql.SQLException;

public class DSLaunchActivity extends AppCompatActivity implements ActionBarListener {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_user_registration_activity);

        User user = new User(this);
        if (savedInstanceState == null) {
            if (user.getUserLoginState() == UserLoginState.USER_LOGGED_IN) {
                showFragment();
                DatabaseHelper databaseHelper = DemoAppManager.getInstance().getDatabaseHelper();
                try {
                    databaseHelper.getWriteDbPermission();
                } catch (SQLException e) {
                    DemoUAppLog.e(this.getClass().getSimpleName(), e.getMessage());
                }
            } else {
                showNotLoggedInAlert();
            }
        } else {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
    }

    private void showNotLoggedInAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.RA_Warning);
        alertBuilder.setMessage(R.string.RA_You_are_not_LoggedIn);
        alertBuilder.setPositiveButton(R.string.ok, (dialog, which) -> finish());
        alertBuilder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showFragment() {
        int containerId = R.id.frame_container_user_reg;

        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, containerId, this);
        DSBaseFragment momentsFragment = new MomentFragment();
        momentsFragment.showFragment(momentsFragment, fragmentLauncher);
    }

    @Override
    public void updateActionBar(@StringRes final int i, final boolean b) {

    }

    @Override
    public void updateActionBar(final String s, final boolean b) {

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.frame_container_user_reg);
        if (currentFrag instanceof MomentFragment || (new User(this).getUserLoginState() != UserLoginState.USER_LOGGED_IN)) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
