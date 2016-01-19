/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.activity.CatalogActivity;

/**
 * <H1>Dev Guide</H1>
 * <p>
 *     UIKit provides 3 basic templates that can be filled via target activity.<br>
 *     By default themed gradient background is applied.<br>
 *     To change default background following code can be used
 *     <pre>
 *          ViewGroup group = (ViewGroup) findViewById(<font color="red">R.id.splash_layout</font>);
 *          group.setBackgroundResource(R.drawable.uikit_food);
 *          ImageView logo = (ImageView) findViewById(R.id.splash_logo);
 logo.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_philips_logo));
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
 * Update TextView with id <pre><font color="red"> R.id.splash_title </font></pre> to update the
 * desired
 * text.
 * </p>
 *
 */
public class SplashLauncher extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_buttons);
    }

    public void launchSplashScreen(View v) {
        startActivity(getSplashIntent(v.getId()));
    }

    public Intent getSplashIntent(int id) {
        Class<?> targetClass = SplashActivityLogoTop.class;

        switch (id) {
            case R.id.lt:
                targetClass = SplashActivityLogoTop.class;
                break;
            case R.id.lc_tb:
                targetClass = SplashActivityLogoCenterTitleBottom.class;
                break;
            case R.id.lb:
                targetClass = SplashActivityLogoBottom.class;
                break;
        }
        return new Intent(this, targetClass);
    }
}
