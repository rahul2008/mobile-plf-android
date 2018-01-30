package com.philips.platform.thsdemolaunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.myapplicationlibrary.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ProgressBar;


/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
public class FirstActivity extends AppCompatActivity implements View.OnClickListener, ActionBarListener {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_Purple_Bright;
    Button launchAmwell;
    Button logout;
    User user;
    private Toolbar toolbar;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initTheme();
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.PURPLE, NavigationColor.BRIGHT, AccentRange.ORANGE ));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        setSupportActionBar(toolbar);
        UIDHelper.setTitle(this, "Amwell");
        user = new User(this);
        launchAmwell = (Button) findViewById(R.id.launch_amwell);
        launchAmwell.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        logout = (Button) findViewById(R.id.logout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!user.isUserSignIn()){
            logout.setVisibility(View.GONE);
        }

        logout.setOnClickListener(this);

        if(user.isUserSignIn()){
            logout.setText("Logout");
        }else {
            logout.setText("Login");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.launch_amwell){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }if(id == R.id.logout){
            mProgress.setVisibility(View.VISIBLE);
            user.logout(new LogoutHandler() {
                @Override
                public void onLogoutSuccess() {
                    logout.setText("Login");
                    mProgress.setVisibility(View.GONE);
                    Toast.makeText(FirstActivity.this,"Logout Success!!!",Toast.LENGTH_SHORT).show();
                    logout.setVisibility(View.GONE);
                }

                @Override
                public void onLogoutFailure(int i, String s) {
                    Toast.makeText(FirstActivity.this,"Logout failed!!!",Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.GONE);
                }
            });
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

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        UIDHelper.setTitle(this, getString(i));
        showBackImage(b);
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        UIDHelper.setTitle(this, s);
        showBackImage(b);
    }

    private void showBackImage(boolean isVisible) {
        if (isVisible) {
            toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        } else {
            toolbar.setNavigationIcon(null);
        }

    }

    @Override
    public void onBackPressed() {
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.uappFragmentLayout);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (currentFrag != null && currentFrag instanceof BackEventListener && !((BackEventListener) currentFrag).handleBackEvent()) {
            super.onBackPressed();
        }*/
        this.finish();
    }

}
