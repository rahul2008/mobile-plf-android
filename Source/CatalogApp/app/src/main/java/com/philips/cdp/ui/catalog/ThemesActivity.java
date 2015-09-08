package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

public class ThemesActivity extends UiKitActivity {

    public static int RESULT_CODE_THEME_UPDATED = 1;
    private RadioButton solidRadioButton, gradientRadioButton;
    private Switch colorSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        declareViews();
        setViewState();
    }

    private void setViewState() {
//        String preferences = ThemeUtils.get
    }

    private void declareViews() {
        gradientRadioButton = (RadioButton) findViewById(R.id.gradient);
        solidRadioButton = (RadioButton) findViewById(R.id.solid);
        colorSwitch = (Switch) findViewById(R.id.colorSwitch);
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
