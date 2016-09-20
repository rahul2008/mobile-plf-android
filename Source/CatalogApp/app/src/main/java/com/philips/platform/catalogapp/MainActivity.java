package com.philips.platform.catalogapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.catalogapp.fragments.ComponentListFragment;
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
//        initDemoListFragment();
    }

    private void initDemoListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_container, new ComponentListFragment());
        transaction.commit();
    }

    public ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration(ColorRange.GROUP_BLUE, TonalRange.ULTRA_LIGHT, this);
    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
