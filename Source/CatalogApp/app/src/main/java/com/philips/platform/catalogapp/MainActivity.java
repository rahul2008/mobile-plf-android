/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.events.*;
import com.philips.platform.catalogapp.fragments.SideBarFragment;
import com.philips.platform.catalogapp.themesettings.ThemeHelper;
import com.philips.platform.uid.thememanager.*;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.*;

public class MainActivity extends UIDActivity {

    protected static final String TITLE_TEXT = "TITLE_TEXT";
    static final String THEMESETTINGS_ACTIVITY_RESTART = "THEMESETTINGS_ACTIVITY_RESTART";

    ContentColor contentColor;
    ColorRange colorRange;
    NavigationColor navigationColor;
    private NavigationController navigationController;
    private ViewDataBinding activityMainBinding;
    private SharedPreferences defaultSharedPreferences;
    private AccentRange accentColorRange;

    private DrawerLayout sideBarLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView recyclerView;

    static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initTheme();
        if (BuildConfig.DEBUG) {
            Log.d(MainActivity.class.getName(), String.format("Theme config Tonal Range :%s, Color Range :%s , Navigation Color : %s",
                    contentColor, colorRange, navigationColor));
        }

        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        EventBus.getDefault().register(this);
        navigationController = new NavigationController(this, getIntent(), activityMainBinding);
        navigationController.init(savedInstanceState);

        sideBarLayout = (DrawerLayout) findViewById(R.id.sidebar_layout);
        drawerToggle = setupDrawerToggle();
        recyclerView = ((RecyclerView) findViewById(R.id.sidebar_recyclerview));

        //enableRecyclerViewSeparator(separatorItemDecoration, recyclerView);
        initializeRecyclerView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawerToggle.setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_hamburger_icon, getTheme()));
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // event when click home button
                if (navigationController.hasBackStack()) {
                    onBackPressed();
                } else {
                    //showSnackBar();
                    sideBarLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, sideBarLayout, navigationController.getToolbar(), R.string.sidebar_open,  R.string.sidebar_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void initializeRecyclerView(Context context) {
        DataHolderView dataHolderView = getIconDataHolderView(context);
        recyclerView.setAdapter(new MainActivity.RecyclerViewAdapter(dataHolderView.dataHolders));
        //findViewById(R.id.uid_recyclerview_header).setVisibility(View.GONE);
    }

    @NonNull
    private DataHolderView getIconDataHolderView(Context context) {
        DataHolderView dataHolderView = new DataHolderView();
        dataHolderView.addIconItem(R.drawable.ic_add_folder, R.string.menu1, context);
        dataHolderView.addIconItem(R.drawable.ic_home, R.string.menu2, context);
        dataHolderView.addIconItem(R.drawable.ic_lock, R.string.menu3, context);
        dataHolderView.addIconItem(R.drawable.ic_alarm, R.string.menu4, context);
        dataHolderView.addIconItem(R.drawable.ic_bottle, R.string.menu5, context);
        dataHolderView.addIconItem(R.drawable.ic_location, R.string.menu6, context);
        return dataHolderView;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;

        public RecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_one_line_icon, parent, false);
            MainActivity.RecyclerViewAdapter.BindingHolder holder = new MainActivity.RecyclerViewAdapter.BindingHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((MainActivity.RecyclerViewAdapter.BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((MainActivity.RecyclerViewAdapter.BindingHolder) holder).getBinding().executePendingBindings();

            Resources.Theme theme = ThemeUtils.getTheme(holder.itemView.getContext(), null);
            Context themedContext = UIDContextWrapper.getThemedContext(holder.itemView.getContext(), theme);
            ColorStateList colorStateList = ThemeUtils.buildColorStateList(themedContext, R.color.uid_recyclerview_background_selector);
            final int selectedStateColor = colorStateList.getDefaultColor();

            ((MainActivity.RecyclerViewAdapter.BindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    boolean isSelected = holder.itemView.isSelected();
                    holder.itemView.setSelected(!isSelected);
                    holder.itemView.setBackgroundColor(isSelected ? Color.TRANSPARENT : selectedStateColor);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataHolders.size();
        }

        class BindingHolder extends RecyclerView.ViewHolder {
            private ViewDataBinding binding;

            public BindingHolder(@NonNull View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }


    private void initTheme() {
        final ThemeConfiguration themeConfig = getThemeConfig();
        final int themeResourceId = getThemeResourceId(getResources(), getPackageName(), colorRange, contentColor);
        themeConfig.add(navigationColor);
        themeConfig.add(accentColorRange);
        setTheme(themeResourceId);
        UIDLocaleHelper.getInstance().setFilePath(getCatalogAppJSONAssetPath());

        UIDHelper.init(themeConfig);
    }

    @StyleRes
    int getThemeResourceId(Resources resources, final String packageName, final ColorRange colorRange, final ContentColor contentColor) {
        final String themeName = String.format("Theme.DLS.%s.%s", toCamelCase(colorRange.name()), toCamelCase(contentColor.name()));

        return resources.getIdentifier(themeName, "style", packageName);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(ContentTonalRangeChangedEvent event) {
        contentColor = event.getContentColor();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(ColorRangeChangedEvent event) {
        colorRange = event.getColorRange();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(NavigationColorChangedEvent event) {
        navigationColor = event.getNavigationColor();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(AccentColorChangedEvent event) {
        accentColorRange = event.getAccentRange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void restartActivity() {
        Intent intent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(THEMESETTINGS_ACTIVITY_RESTART, true);
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationController.initIconState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        navigationController.onCreateOptionsMenu(menu, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        EventBus.getDefault().post(new OptionMenuClickedEvent(item.toString()));
        switch (item.getItemId()) {
            case R.id.menu_theme_settings:
                navigationController.loadThemeSettingsPage();
                break;
            case R.id.menu_set_theme_settings:
                saveThemeSettings();
                restartActivity();
                break;
            /*case android.R.id.home:
                if (navigationController.hasBackStack()) {
                    onBackPressed();
                } else {
                    //showSnackBar();
                    sideBarLayout.openDrawer(GravityCompat.START);
                }*/
        }

        return true;
    }

    private void showSnackBar() {
        Snackbar.make(navigationController.getToolbar(), R.string.hamburger_not_ready, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        navigationController.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        navigationController.onSaveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    public ThemeConfiguration getThemeConfig() {
        final ThemeHelper themeHelper = new ThemeHelper(defaultSharedPreferences);
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        accentColorRange = themeHelper.initAccentRange();
        return new ThemeConfiguration(this, colorRange, navigationColor, contentColor, accentColorRange);
    }

    @SuppressLint("CommitPrefEdits")
    private void saveThemeValues(final String key, final String name) {
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(key, name);
        edit.commit();
    }

    @Override
    public void setTitle(final int titleId) {
        navigationController.setTitleText(titleId);
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void saveThemeSettings() {
        saveThemeValues(UIDHelper.COLOR_RANGE, colorRange.name());
        saveThemeValues(UIDHelper.NAVIGATION_RANGE, navigationColor.name());
        saveThemeValues(UIDHelper.CONTENT_TONAL_RANGE, contentColor.name());
        saveThemeValues(UIDHelper.ACCENT_RANGE, accentColorRange.name());
    }

    @Override
    public void onBackPressed() {
        if (navigationController.updateStack()) {
            super.onBackPressed();
        }
        navigationController.processBackButton();
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    @VisibleForTesting
    public void setColorRange(final ColorRange colorRange) {
        this.colorRange = colorRange;
    }

    @VisibleForTesting
    public void setContentColor(final ContentColor contentColor) {
        this.contentColor = contentColor;
    }

    public String getCatalogAppJSONAssetPath() {
        try {
            File f = new File(getCacheDir() + "/catalogapp.json");
            InputStream is = getAssets().open("catalogapp.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
            return f.getPath();
        } catch (FileNotFoundException e) {
            Log.e(MainActivity.class.getName(), e.getMessage());
        } catch (IOException e) {
            Log.e(MainActivity.class.getName(), e.getMessage());
        }
        return null;
    }
}
