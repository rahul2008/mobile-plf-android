package com.philips.cdp.ui.catalog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

public class BackgroundTest extends UiKitActivity {

    public static int RESULT_CODE_THEME_UPDATED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_test);
    }

    public void changeBackground(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.next :
                ThemeUtils.setThemePreferences(this, false);
                setResult(RESULT_CODE_THEME_UPDATED);
                intent = new Intent(this, BackgroundTest.class);
                startActivity(intent);
                finish();
                break;
            case R.id.previous :
                ThemeUtils.setThemePreferences(this, true);
                setResult(RESULT_CODE_THEME_UPDATED);
                intent = new Intent(this, BackgroundTest.class);
                startActivity(intent);
                finish();
                break;
        }

    }


}
