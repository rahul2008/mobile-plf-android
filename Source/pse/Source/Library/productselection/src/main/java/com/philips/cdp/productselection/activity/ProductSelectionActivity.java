package com.philips.cdp.productselection.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.shamanland.fonticon.FontIconTypefaceHolder;


public class ProductSelectionActivity extends ProductSelectionBaseActivity {
    private static final String TAG = ProductSelectionActivity.class.getSimpleName();
    private static final String USER_SELECTED_PRODUCT_CTN = "mCtnFromPreference";
    private static final String USER_PREFERENCE = "user_product";
    private static int mEnterAnimation = -1;
    private static int mExitAnimation = -1;
    private SharedPreferences prefs = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDlSThemeLauncher();
        if (savedInstanceState != null) {
            // if app killed by vm.
            savedInstanceState = null;
            finish();
            super.onCreate(savedInstanceState);
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productselection_layout);
        animateThisScreen();
        loadFirstFragment();
        initDLSActionBar();
    }

    private void loadFirstFragment() {
        if (getCtnFromPreference()) {
            showFragment(new WelcomeScreenFragmentSelection());
        } else {
            showFragment(new ProductSelectionListingFragment());
        }
    }

    private void initDLSActionBar() {
        UIDHelper.setupToolbar(this);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_icon);
    }

    private void getDlSThemeLauncher() {
        ThemeConfiguration config = ProductModelSelectionHelper.getInstance().getThemeConfiguration();
        setTheme(ProductModelSelectionHelper.getInstance().getDlsTheme());
        UIDHelper.init(config);
        FontIconTypefaceHolder.init(getAssets(), "fonts/iconfont.ttf");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected boolean getCtnFromPreference() {
        String ctn;
        prefs = getSharedPreferences(
                USER_PREFERENCE, Context.MODE_PRIVATE);
        ctn = prefs.getString(USER_SELECTED_PRODUCT_CTN, "");
        return !(ctn != null && !ctn.isEmpty());
    }

    private void animateThisScreen() {
        Bundle bundleExtras = getIntent().getExtras();

        String startAnim;
        String endAnim;

        int startAnimation = bundleExtras.getInt(Constants.START_ANIMATION_ID);
        int endAnimation = bundleExtras.getInt(Constants.STOP_ANIMATION_ID);
        int orientation = bundleExtras.getInt(Constants.SCREEN_ORIENTATION);

        if (startAnimation == 0 && endAnimation == 0) {
            return;
        }

        startAnim = getResources().getResourceName(startAnimation);
        endAnim = getResources().getResourceName(endAnimation);

        String packageName = getPackageName();
        mEnterAnimation = getApplicationContext().getResources().getIdentifier(startAnim,
                "anim", packageName);
        mExitAnimation = getApplicationContext().getResources().getIdentifier(endAnim, "anim",
                packageName);
        setRequestedOrientation(orientation);
        overridePendingTransition(mEnterAnimation, mExitAnimation);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
