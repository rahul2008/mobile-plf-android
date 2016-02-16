package com.philips.multiproduct.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.R;
import com.philips.multiproduct.utils.ProductSelectionLogger;

/**
 * MultiProductBaseActivity is the main container class which can contain Digital Care fragments.
 *
 * @author : Ritesh.jha@philips.com
 *         naveen@philips.com
 * @since : 20 Jan 2016
 */
public abstract class MultiProductBaseActivity extends UiKitActivity {
    private static String TAG = MultiProductBaseActivity.class.getSimpleName();
    private FragmentManager fragmentManager = null;
    private ProductModelSelectionHelper mProductModelSelectionHelper = null;
    private static ThemeUtils themeUtils;
    private int noActionBarTheme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ProductSelectionLogger.i(TAG, "onCreate");
        ProductModelSelectionHelper.getInstance();
        fragmentManager = getSupportFragmentManager();

        if (themeUtils == null) {
            themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name_multiproduct),
                    Context.MODE_PRIVATE));
        }
        setTheme(themeUtils.getTheme());
        initActionBar();
    }

    protected ThemeUtils getUiKitThemeUtil(){
        if (themeUtils == null) {
            themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name_multiproduct),
                    Context.MODE_PRIVATE));
        }

        return themeUtils;
    }

    private void initActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.uikit_action_bar, null); // layout which contains your button.

        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        ImageView arrowImage = (ImageView) mCustomView
                .findViewById(R.id.arrow);
        arrowImage.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_up_arrow));
        arrowImage.bringToFront();
        mActionBar.setCustomView(mCustomView, params);
        mActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    protected void setNoActionBarTheme() {
        themeUtils = new ThemeUtils(this.getSharedPreferences(this.getString(R.string.app_name),
                Context.MODE_PRIVATE));
        noActionBarTheme = themeUtils.getNoActionBarTheme();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ProductSelectionLogger.i(TAG, TAG + " : onConfigurationChanged ");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backstackFragment();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (mProductModelSelectionHelper != null) {
            mProductModelSelectionHelper = null;
        }
    }

    private boolean backstackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
//            enableActionBarHome();
            fragmentManager.popBackStack();
            removeCurrentFragment();
        }
        return true;
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }


    protected void showFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            fragmentTransaction.replace(R.id.mainContainer, fragment, "tagname");
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, e.getMessage());
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow() != null && getWindow().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

}
