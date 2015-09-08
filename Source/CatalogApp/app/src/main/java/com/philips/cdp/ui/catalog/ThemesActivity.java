package com.philips.cdp.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.philips.cdp.ui.catalog.activity.UiKitActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.ArrayList;

public class ThemesActivity extends UiKitActivity implements RadioGroup.OnCheckedChangeListener, Switch.OnCheckedChangeListener {

    public static int RESULT_CODE_THEME_UPDATED = 1;
    private RadioButton solidRadioButton, gradientRadioButton;
    private Switch colorSwitch;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        declareViews();
        setViewState();
        radioGroup.setOnCheckedChangeListener(this);
        colorSwitch.setOnCheckedChangeListener(this);
    }

    private void setViewState() {
        String preferences = ThemeUtils.getThemePreferences(ThemesActivity.this);
        ArrayList<String> prefData = ThemeUtils.getTokens(preferences);
        setSwitchState(prefData);
        setRadioButtonState(prefData);

    }

    private void setRadioButtonState(ArrayList<String> prefData) {
        if (prefData.size() > 2) {
            if (prefData.get(2).equalsIgnoreCase(getString(R.string.solid)))
                solidRadioButton.setChecked(true);
            else if (prefData.get(2).equalsIgnoreCase(getString(R.string.gradient)))
                gradientRadioButton.setChecked(true);
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
    }

    public void changeBackground(View v) {
        switch (v.getId()) {

            case R.id.change_theme:
                ThemeUtils.setThemePreferences(this, false);
                changeTheme();
                break;

            default:
                break;
        }
    }

    private void changeTheme() {
        Intent intent;
        setResult(RESULT_CODE_THEME_UPDATED);
        intent = new Intent(this, ThemesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.solid || checkedId == R.id.gradient) {
            String preferences = getScreenPreferences();
            ThemeUtils.setThemePreferences(ThemesActivity.this, preferences);
            changeTheme();
        } else {

        }

    }

    private String getScreenPreferences() {
        StringBuilder builder = new StringBuilder();
        builder.append(ThemeUtils.getCOLOR_STRING());
        builder.append("|");

        if (colorSwitch.isChecked())
            builder.append("true");
        else
            builder.append("false");

        builder.append("|");

        if (solidRadioButton.isChecked())
            builder.append(getString(R.string.solid));
        else if (gradientRadioButton.isChecked())
            builder.append(getString(R.string.gradient));
        else
            builder.append(getString(R.string.solid));

        builder.append("|");

        builder.append(getThemeIndex());

        return builder.toString();
    }

    private int getThemeIndex() {
        if (colorSwitch.isChecked() && solidRadioButton.isChecked())
            return 1;
        else if (!colorSwitch.isChecked() && gradientRadioButton.isChecked())
            return 2;
        else if (colorSwitch.isChecked() && gradientRadioButton.isChecked())
            return 3;
        else
            return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String preferences = getScreenPreferences();
        ThemeUtils.setThemePreferences(ThemesActivity.this, preferences);
        changeTheme();
    }
}
