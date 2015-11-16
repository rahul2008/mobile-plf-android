package com.philips.cdp.uikit;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (validateHamburger()) {
            getMenuInflater().inflate(R.menu.uikit_hamburger_menu_item, menu);
            MenuItem reload = menu.findItem(R.id.action_reload);
            reload.setIcon(VectorDrawable.create(this, R.drawable.uikit_reload));
            VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) findViewById(R.id.philips_logo);
            vectorDrawableImageView.setAlpha(229);
            return true;
        }
        return super.onCreateOptionsMenu(menu);

    }

    private boolean validateHamburger() {
        return findViewById(R.id.philips_drawer_layout) != null;
    }
}
