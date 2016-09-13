/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.theme_settings:
                loadThemeSettingsPage();
                break;
            default:
                break;
        }
        return true;
    }

    private void initDemoListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainContainer, new DemoListFragment());
        transaction.commit();
    }

    public ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration(ColorRange.GROUP_BLUE, TonalRange.ULTRA_LIGHT, this);
    }

    private void loadThemeSettingsPage() {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, new ThemeSettingsFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
