package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.ArrayList;

public class ThemesActivity extends UiKitActivity implements RadioGroup.OnCheckedChangeListener {

    public static int RESULT_CODE_THEME_UPDATED = 1;
    private RadioButton solidRadioButton, gradientRadioButton;
    private Switch colorSwitch;
    private RadioGroup radioGroup;
    private String DEFAULT_COLOR_STRING = "blue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        declareViews();
        setViewState();
    }

    private void setViewState() {
        String preferences = ThemeUtils.getThemePreferences(ThemesActivity.this);
        ArrayList<String> prefData = ThemeUtils.getTokens(preferences);
        setSwitchState(prefData);

        setRadioButtonState(prefData);

    }

    private void setRadioButtonState(ArrayList<String> prefData) {
        if (prefData.size() > 2) {
            if (prefData.get(2).equalsIgnoreCase("solid"))
                solidRadioButton.setChecked(true);
            else if (prefData.get(2).equalsIgnoreCase("gradient"))
                gradientRadioButton.setChecked(false);
        }
    }

    private void setSwitchState(ArrayList<String> prefData) {
        if (prefData.size() > 1 && Boolean.parseBoolean(prefData.get(1)))
            colorSwitch.setChecked(true);
        else
            colorSwitch.setChecked(false);
    }

    private void declareViews() {
        gradientRadioButton = (RadioButton) findViewById(R.id.gradient);
        solidRadioButton = (RadioButton) findViewById(R.id.solid);
        colorSwitch = (Switch) findViewById(R.id.colorSwitch);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.solid) {
            String s = getScreenPreferences();
        } else if (checkedId == R.id.gradient) {

        } else {

        }

    }

    private String getScreenPreferences() {
        return null;
    }
}
