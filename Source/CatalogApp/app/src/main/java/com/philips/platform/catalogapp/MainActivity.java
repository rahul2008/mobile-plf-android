package com.philips.platform.catalogapp;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.philips.platform.catalogapp.fragments.DemoListFragment;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.TonalRange;
import com.philips.platform.uit.thememanager.UITHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UITHelper.init(getThemeConfig());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDemoListFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDemoListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_container, new DemoListFragment());
        transaction.commit();
    }

    public ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration(ColorRange.GROUP_BLUE, TonalRange.ULTRA_LIGHT, this);
    }
}
