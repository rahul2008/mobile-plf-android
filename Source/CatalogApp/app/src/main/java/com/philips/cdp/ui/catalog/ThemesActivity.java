package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

public class ThemesActivity extends UiKitActivity {

    public static int RESULT_CODE_THEME_UPDATED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
    }

    public void changeBackground(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.change_theme:
                ThemeUtils.setThemePreferences(this, false);
                setResult(RESULT_CODE_THEME_UPDATED);
                intent = new Intent(this, ThemesActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }
}
