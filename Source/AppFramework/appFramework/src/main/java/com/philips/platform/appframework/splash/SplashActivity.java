/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/


package com.philips.platform.appframework.splash;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.appframework.modularui.statecontroller.UIBaseNavigation;
import com.philips.platform.appframework.modularui.statecontroller.UIState;
import com.philips.platform.appframework.modularui.statecontroller.UIStateManager;
import com.philips.platform.appframework.modularui.util.LaunchScreen;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;

/**
 * <H1>Dev Guide</H1>
 * <p>
 * UIKit provides 3 basic templates that can be filled via target activity.<br>
 * By default themed gradient background is applied.<br>
 * To change default background following code can be used
 * <pre>
 *          ViewGroup group = (ViewGroup) findViewById(<font color="red">R.id.splash_layout</font>);
 *          group.setBackgroundResource(R.drawable.splashscreen_background);
 *     </pre>
 * <p/>
 * </p>
 * <H5>With Philips logo</H5>
 * Inflate <pre>com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_top</pre> or
 * <pre>com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_bottom</pre> or
 * <pre>com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_center_tb</pre>
 * as per the requirement.
 *
 * <p>
 * <H3>Modifying title</H3>
 * <br>
 * Update TextView with id <pre><font color="red"> R.id.splash_title </font></pre> to update the desired
 * text.
 * </p>
 *
 *
 * SplashActivity is class which will appear at the very start when user
 * opens the app.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 2 June 2016
 */
public class SplashActivity extends AppFrameworkBaseActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private UIBaseNavigation mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        mNavigator = UIStateManager.getInstance().getCurrentState().getNavigator();
        initView();
        startTimer();
    }

    /*
     * Initialize the views.
     */
    private void initView() {
        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(R.layout.uikit_splash_screen_logo_center_tb);

        ViewGroup group = (ViewGroup) findViewById(R.id.splash_layout);
       // group.setBackground(uikit_splashGradient));

        ImageView logo = (ImageView) findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_philips_logo));


        String splashScreenTitle = getResources().getString(R.string.splash_screen_title);
        CharSequence titleText = Html.fromHtml(splashScreenTitle);

        TextView title = (TextView) findViewById(R.id.splash_title);
        title.setText(titleText);
    }

    /*
     * Splash screen has to go off after specified timeframe.
     */
    private void startTimer() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                UIState returnedState =  (UIState) mNavigator.onPageLoad(SplashActivity.this);
                UIStateManager.getInstance().setCurrentState(returnedState);
                LaunchScreen.getInstance().launchScreen(SplashActivity.this,returnedState.getStateID());
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
