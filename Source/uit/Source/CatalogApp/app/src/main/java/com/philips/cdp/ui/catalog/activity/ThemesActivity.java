package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.cdp.uikit.customviews.PuiSwitch;

import java.util.ArrayList;

public class ThemesActivity extends CatalogActivity implements RadioGroup.OnCheckedChangeListener, Switch.OnCheckedChangeListener {

    public static int RESULT_CODE_THEME_UPDATED = 1;
    private RadioButton solidRadioButton, gradientRadioButton;
    private PuiSwitch colorSwitch;
    private RadioGroup radioGroup;
    private ThemeUtils themeUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        themeUtils = new ThemeUtils(this.getSharedPreferences(
                this.getString(R.string.app_name), Context.MODE_PRIVATE));
        declareViews();
        setViewState();
        setListeners();
    }

    private void setListeners() {
        radioGroup.setOnCheckedChangeListener(this);
        colorSwitch.setOnCheckedChangeListener(this);
    }

    private void setViewState() {
        String preferences = themeUtils.getThemePreferences();
        ArrayList<String> prefData = themeUtils.getThemeTokens(preferences);
        themeUtils.setColorString(prefData.get(0));
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
        gradientRadioButton = (RadioButton) findViewById(R.id.rbgradient);
        solidRadioButton = (RadioButton) findViewById(R.id.rbsolid);
        colorSwitch = (PuiSwitch) findViewById(R.id.colorSwitch);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
    }

    public void changeBackground(View v) {
        String preferences = null;
        switch (v.getId()) {
            case R.id.change_theme:
                themeUtils.setThemePreferences(false);
                relaunchActivity();
                break;

            case R.id.blue_theme:
                preferences = "blue|false|solid|0";
                themeUtils.setThemePreferences(preferences);
                relaunchActivity();
                break;

            case R.id.orange_theme:
                preferences = "orange|false|solid|0";
                themeUtils.setThemePreferences(preferences);
                relaunchActivity();
                break;

            case R.id.aqua_theme:
                preferences = "aqua|false|solid|0";
                themeUtils.setThemePreferences(preferences);
                relaunchActivity();
                break;
            default:
                break;
        }
    }

    private void relaunchActivity() {
        Intent intent;
        setResult(RESULT_CODE_THEME_UPDATED);
        intent = new Intent(this, ThemesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.rbsolid || checkedId == R.id.rbgradient) {
            changeTheme();
        }
    }

    private void changeTheme() {
        String preferences = getScreenPreferences();
        themeUtils.setThemePreferences(preferences);
        relaunchActivity();
    }

    private String getScreenPreferences() {
        StringBuilder builder = new StringBuilder();
        appendColorString(builder);
        appendColorState(builder);
        appendColorType(builder);
        builder.append(getThemeIndex());
        return builder.toString();
    }

    private void appendColorType(StringBuilder builder) {
        if (solidRadioButton.isChecked())
            builder.append(getString(R.string.solid));
        else if (gradientRadioButton.isChecked())
            builder.append(getString(R.string.gradient));
        else
            builder.append(getString(R.string.solid));

        builder.append(ThemeUtils.DELIMITER);
    }

    private void appendColorState(StringBuilder builder) {
        if (colorSwitch.isChecked())
            builder.append("true");
        else
            builder.append("false");

        builder.append(ThemeUtils.DELIMITER);
    }

    private void appendColorString(StringBuilder builder) {
        builder.append(themeUtils.getColorString());
        builder.append(ThemeUtils.DELIMITER);
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
        changeTheme();
    }
}
