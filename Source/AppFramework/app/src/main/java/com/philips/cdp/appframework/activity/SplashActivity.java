/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.appframework.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import sample.com.appframework.R;

/**
 * <H1>Dev Guide</H1>
 * <p>
 *     UIKit provides 3 basic templates that can be filled via target activity.<br>
 *     By default themed gradient background is applied.<br>
 *     To change default background following code can be used
 *     <pre>
 *          ViewGroup group = (ViewGroup) findViewById(<font color="red">R.id.splash_layout</font>);
 *          group.setBackgroundResource(R.drawable.splashscreen_background);
 *     </pre>
 *
 * </p>
 * <H5>With Philips logo</H5>
 * Inflate <pre>com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_top</pre> or
 *     <pre>com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_bottom</pre> or
 *     <pre>com.philips.cdp.uikit.R.layout.uikit_splash_screen_logo_center_tb</pre>
 *     as per the requirement.
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
 *
 *
 */
public class SplashActivity extends UiKitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(R.layout.uikit_splash_screen_logo_center_tb);

        ViewGroup group = (ViewGroup) findViewById(R.id.splash_layout);
        group.setBackgroundResource(R.drawable.splashscreen_background);

        ImageView logo = (ImageView) findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_philips_logo));


        String splashScreenTitle = getResources().getString(R.string.splash_screen_title);
        CharSequence titleText = Html.fromHtml(splashScreenTitle);

        TextView title = (TextView) findViewById(R.id.splash_title);
        title.setText(titleText);
    }
}
