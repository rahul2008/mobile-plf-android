package com.philips.platform.catalogapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.fragments.ComponentListFragment;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.ThemeConfiguration;
import com.philips.platform.uit.thememanager.UITHelper;

import java.util.List;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    ImageView themeSettingsIcon;
    TextView setThemeTextView;
    private ContentTonalRange contentTonalRange = ContentTonalRange.ULTRA_LIGHT;
    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initColorRange();
        initNavigationRange();
        initTonalRange();
        UITHelper.init(getThemeConfig());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initDemoListFragment();
            final Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
            toolbar.setTitle(R.string.catalog_app_name);
            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.catalog_app_name);
            initSetThemeSettings(toolbar);

            initThemeSettingsIcon(toolbar);
            setSupportActionBar(toolbar);
        }
    }

    private void initSetThemeSettings(final Toolbar toolbar) {
        setThemeTextView = (TextView) toolbar.findViewById(R.id.set_theme_settings);
        setThemeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                initTonalRange();
                initColorRange();
                initNavigationRange();
            }
        });
    }

    private void initNavigationRange() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int navigation = sharedPreferences.getInt(UITHelper.NAVIGATION_RANGE, 1);
    }

    private void initColorRange() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String color = sharedPreferences.getString(UITHelper.COLOR_RANGE, ColorRange.GROUP_BLUE.name());
        colorRange = ColorRange.valueOf(color);
    }

    private void initTonalRange() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tonalRange = sharedPreferences.getString(UITHelper.CONTENT_TONAL_RANGE, ContentTonalRange.ULTRA_LIGHT.name());
        contentTonalRange = ContentTonalRange.valueOf(tonalRange);
    }

    private void initThemeSettingsIcon(final Toolbar toolbar) {
        themeSettingsIcon = (ImageView) toolbar.findViewById(R.id.theme_settings);
        themeSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                loadThemeSettingsPage();
            }
        });
    }

    private void initDemoListFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mainContainer, new ComponentListFragment());
        transaction.commit();
    }

    public ThemeConfiguration getThemeConfig() {
        return new ThemeConfiguration(colorRange, contentTonalRange, this);
    }

    public boolean switchFragment(Fragment fragment) {
        supportFragmentManager = getSupportFragmentManager();
        final List<Fragment> fragments = supportFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragmentFromList : fragments) {
                if (fragmentFromList instanceof ThemeSettingsFragment && fragment instanceof ThemeSettingsFragment) {
                    return false;
                }
            }
        }
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
        toggle(themeSettingsIcon, setThemeTextView);
        return true;
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void loadThemeSettingsPage() {
        final boolean switchedFragment = switchFragment(new ThemeSettingsFragment());
        if (switchedFragment) {
            setTitle(R.string.tittle_theme_settings);
            toggle(setThemeTextView, themeSettingsIcon);
        }
    }

    private void toggle(final View visibleView, final View goneView) {
        visibleView.setVisibility(View.VISIBLE);
        goneView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (hasBackStack()) {
            final Fragment fragmentAtTopOfBackStack = getFragmentAtTopOfBackStack();
            if (!(fragmentAtTopOfBackStack instanceof ThemeSettingsFragment)) {
                toggle(themeSettingsIcon, setThemeTextView);
            }
        }
        super.onBackPressed();
    }

    private boolean hasBackStack() {
        return supportFragmentManager != null && supportFragmentManager.getBackStackEntryCount() > 0;
    }

    @NonNull
    private Fragment getFragmentAtTopOfBackStack() {
        return getFragmentAtBackStackIndex(supportFragmentManager.getBackStackEntryCount() - 1);
    }

    @NonNull
    private Fragment getFragmentAtBackStackIndex(final int index) {
        final FragmentManager.BackStackEntry backStackEntry = supportFragmentManager.getBackStackEntryAt(index);
        final String name = backStackEntry.getName();
        return supportFragmentManager.findFragmentByTag(name);
    }
}
