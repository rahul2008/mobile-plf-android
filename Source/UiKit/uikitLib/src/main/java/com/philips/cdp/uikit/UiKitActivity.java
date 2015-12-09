package com.philips.cdp.uikit;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.philips.cdp.uikit.customviews.VectorDrawableImageView;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiKitActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFontIconLib();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/centralesans_book.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (validateHamburger()) {
            DrawerLayout philipsDrawerLayout = setLogoAlpha();
            configureStatusBarViews();
            philipsDrawerLayout.setScrimColor(Color.TRANSPARENT);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to imageView.setAlpha(): sticking with deprecated API for now
    private DrawerLayout setLogoAlpha() {
        VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) findViewById(R.id.philips_logo);
        DrawerLayout philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        if (vectorDrawableImageView != null)
            vectorDrawableImageView.setAlpha(229);
        return philipsDrawerLayout;
    }

    private void configureStatusBarViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private boolean validateHamburger() {
        return findViewById(R.id.philips_drawer_layout) != null;
    }

    private void initFontIconLib() {
        try {
            FontIconTypefaceHolder.getTypeface();

        } catch (IllegalStateException e) {
            FontIconTypefaceHolder.init(getAssets(), "fonts/puicon.ttf");
        }
    }
}
