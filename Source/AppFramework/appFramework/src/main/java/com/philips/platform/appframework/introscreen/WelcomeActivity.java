/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public class WelcomeActivity extends AppFrameworkBaseActivity implements ActionBarListener ,BackEventListener{
    ImageView arrowImage;
    TextView textView;
    FragmentManager mFragmentManager;
    WelcomeScreenFragment welcomeScreenFragment;
    FragmentTransaction fragmentTransaction;
    final static int backButtonClick = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WelcomePresenter();
        initCustomActionBar();
        setContentView(R.layout.af_welcome_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onLoad(this);
    }

    @Override
    public void onBackPressed() {
       /* FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment =  (RegistrationFragment)fragmentManager
                .findFragmentById(R.id.fl_reg_fragment_container);
        if (fragment != null && fragment instanceof BackEventListener) {*/
            boolean isConsumed = (this).handleBackEvent();
            if (isConsumed)
                presenter.onClick(backButtonClick, this);;

            super.onBackPressed();


     //   if (!RegistrationLaunchHelper.isBackEventConsumedByRegistration(this)) {

     //   }
    }

    void changeActionBarState(boolean isVisible) {
        if (!isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.af_home_action_bar, null); // layout which contains your button.


            final FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onBackPressed();
                }
            });
            arrowImage = (ImageView) mCustomView
                    .findViewById(R.id.arrow_left);
            textView = (TextView) mCustomView.findViewById(R.id.action_bar_text);
            //noinspection deprecation
            arrowImage.setBackground(VectorDrawable.create(this, R.drawable.left_arrow));
            mActionBar.setCustomView(mCustomView, params);
            textView.setText(R.string.af_app_name);
        }
    }

    void updateTitle() {
        arrowImage.setVisibility(View.VISIBLE);
        textView.setText(R.string.af_app_name);
    }

    void updateTitleWithBack() {
        arrowImage.setVisibility(View.VISIBLE);
        textView.setText(R.string.af_app_name);
    }

    void updateTitleWithoutBack() {
        arrowImage.setVisibility(View.INVISIBLE);
        textView.setText(R.string.af_app_name);
    }

    void loadWelcomeFragment() {
        mFragmentManager = this.getSupportFragmentManager();
        welcomeScreenFragment = new WelcomeScreenFragment();
        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_frame_container, welcomeScreenFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    @Override
    public boolean handleBackEvent() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment =  (RegistrationFragment)fragmentManager
                .findFragmentById(R.id.fl_reg_fragment_container);

        if (fragment != null && fragment instanceof BackEventListener)
            return false;
        else return true;
    }
}
