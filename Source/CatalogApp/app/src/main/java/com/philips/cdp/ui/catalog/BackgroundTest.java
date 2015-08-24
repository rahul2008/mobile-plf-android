package com.philips.cdp.ui.catalog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

public class BackgroundTest extends UiKitActivity {

    private final static String THEME_EXTRA = "theme_extra";
    private final static String TAG = BackgroundTest.class.getSimpleName();

    private int mCurrentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_test);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeBackground(View v) {
        ThemeUtils.setThemePreferences(this);
        Intent intent = new Intent(this, BackgroundTest.class);
        startActivity(intent);
        finish();
    }
}
