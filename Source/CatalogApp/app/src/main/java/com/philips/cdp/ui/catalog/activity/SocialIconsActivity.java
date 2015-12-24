/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.app.Activity;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.UiKitActivity;
import com.shamanland.fonticon.FontIconTypefaceHolder;


/**
 * <b></b>Social Icons can be shown in Custom view component </b>
 *
 * <p/>
 * <b>For Example to use Social Icons follow the below steps</b><br>
 * <pre>&lt; com.shamanland.fonticon.FontIconView
 * android:id="@+id/social_icon"
 * style="@style/FontIcon"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:text="@string/uikit_fonticon_twitter" &gt;</pre><br>
 * <p/>
 * <b>Use Styles as per your requirement as shown below</b>
 * <pre>
 * FontIcon (For normal icons of 32px )
 * FontIcon.Small (For normal icons of 20 px )
 * FontIcon.Inverse (For inverted icons of 32 px )
 * FontIcon.SmallInverse (For inverted of 20 px )
 *        </pre>
 */
public class SocialIconsActivity extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social_icons);
    }



}