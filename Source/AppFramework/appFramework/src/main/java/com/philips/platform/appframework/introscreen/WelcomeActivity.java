/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.shamanland.fonticon.FontIconView;



public class WelcomeActivity extends AppFrameworkBaseActivity {
    private static String TAG = WelcomeActivity.class.getSimpleName();
    private FontIconView appframework_leftarrow, appframework_rightarrow;
    private TextView startRegistrationScreenButton, appframeworkSkipButton;
    private CircleIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WelcomePresenter();
        setContentView(R.layout.af_welcome_screen);
    }

    @Override
    protected void onResume() {
        presenter.onLoad(this);
        super.onResume();
    }

    void changeActionBarState(boolean isVisible){
        if(!isVisible){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }

    }
    void loadWelcomeFragment(){
        FragmentManager mFragmentManager = getSupportFragmentManager();
        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_container, welcomeScreenFragment, "");
        fragmentTransaction.commit();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
