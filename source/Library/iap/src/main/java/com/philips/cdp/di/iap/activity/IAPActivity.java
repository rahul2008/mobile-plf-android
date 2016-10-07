/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.di.iap.screens.InAppBaseFragment;
import com.philips.cdp.di.iap.screens.BuyDirectFragment;
import com.philips.cdp.di.iap.screens.ProductCatalogFragment;
import com.philips.cdp.di.iap.screens.ProductDetailFragment;
import com.philips.cdp.di.iap.screens.PurchaseHistoryFragment;
import com.philips.cdp.di.iap.screens.ShoppingCartFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;
import java.util.Locale;

import static com.philips.cdp.di.iap.utils.Utility.dismissProgressDialog;

public class IAPActivity extends UiKitActivity implements ActionBarListener, IAPListener {
    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkBlue_WhiteBackground;
    private TextView mTitleTextView;
    private TextView mCountText;
    private ImageView mBackImage;
    private FrameLayout mCartContainer;

    private IAPCartListener mProductCountListener = new IAPCartListener() {
        @Override
        public void onSuccess(final int count) {
            onGetCartCount(count);
        }

        @Override
        public void onFailure(final Message msg) {
            Utility.dismissProgressDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        addActionBar();
        setContentView(R.layout.iap_activity);
        addLandingViews(savedInstanceState);
    }

    private void addLandingViews(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            updateConfigurationWithCurrentLocale();
            int landingScreen = getIntent().getIntExtra(IAPConstant.IAP_LANDING_SCREEN, -1);
            ArrayList<String> CTNs = getIntent().getExtras().getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS);
            Bundle bundle = new Bundle();
            switch (landingScreen) {
                case IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW:
                    bundle.putStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS, CTNs);
                    addFragment(ProductCatalogFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), ProductCatalogFragment.TAG);
                    break;
                case IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW:
                    addFragment(ShoppingCartFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), ShoppingCartFragment.TAG);
                    break;
                case IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW:
                    addFragment(PurchaseHistoryFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), PurchaseHistoryFragment.TAG);
                    break;
                case IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW:
                    if (getIntent().hasExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER)) {
                        bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER,
                                getIntent().getStringExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER));
                        addFragment(ProductDetailFragment.createInstance(bundle,
                                InAppBaseFragment.AnimationType.NONE), ProductDetailFragment.TAG);
                    }
                    break;
                case IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW:
                    if (getIntent().hasExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER)) {
                        bundle.putString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER,
                                getIntent().getStringExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER));
                        addFragment(BuyDirectFragment.createInstance(bundle,
                                InAppBaseFragment.AnimationType.NONE), BuyDirectFragment.TAG);
                    }
                    break;
            }
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        setTheme(themeIndex);
    }

    private void updateConfigurationWithCurrentLocale() {
        PILLocaleManager localeManager = new PILLocaleManager(getApplicationContext());
        String localeAsString = localeManager.getInputLocale();
        if (localeAsString != null) {
            String[] localeArray = localeAsString.split("_");
            Locale locale = new Locale(localeArray[0], localeArray[1]);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        NetworkUtility.getInstance().dismissErrorDialog();
        super.onDestroy();
    }

    public void addFragment(InAppBaseFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(this, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + newFragmentTag + ")");
    }

    public void showFragment(String fragmentTag) {
        if (!NetworkUtility.getInstance().isNetworkAvailable(this)) {
            NetworkUtility.getInstance().showErrorDialog(this,
                    getSupportFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_you_are_offline), getString(R.string.iap_no_internet));
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
            if (fragment == null) {
                addFragment(ShoppingCartFragment.createInstance(new Bundle(),
                        InAppBaseFragment.AnimationType.NONE), fragmentTag);
            } else {
                getFragmentManager().popBackStack(ProductCatalogFragment.TAG, 0);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

    @Override
    public void onBackPressed() {
        IAPLog.i(IAPLog.LOG, "OnBackpressed Called");
        Utility.hideKeypad(this);
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.BACK_BUTTON_PRESS);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.fl_mainFragmentContainer);
        boolean backState = false;
        if (currentFrag != null && currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
        }
        if (!backState) {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        CartModelContainer.getInstance().resetApplicationFields();
    }

    @Override
    protected void onPause() {
        IAPAnalytics.pauseCollectingLifecycleData();
        super.onPause();
    }

    @Override
    protected void onResume() {
        IAPAnalytics.collectLifecycleData(this);
        super.onResume();
    }

    @Override
    public void updateActionBar(int resourceId, boolean visibility) {
        mTitleTextView.setText(getString(resourceId));
        if (visibility) {
            mBackImage.setVisibility(View.VISIBLE);
        } else {
            mBackImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateActionBar(String resourceString, boolean visibility) {
        mTitleTextView.setText(resourceString);
        if (visibility) {
            mBackImage.setVisibility(View.VISIBLE);
        } else {
            mBackImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetCartCount(int count) {
        if (count > 0) {
            mCountText.setText(String.valueOf(count));
            mCountText.setVisibility(View.VISIBLE);
        } else {
            mCountText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUpdateCartCount() {
        ShoppingCartPresenter shoppingCartAPI = new ShoppingCartPresenter();
        shoppingCartAPI.getProductCartCount(getApplicationContext(), mProductCountListener);
    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {
        if (shouldShow) {
            mCartContainer.setVisibility(View.VISIBLE);
        } else {
            mCartContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {
        dismissProgressDialog();
    }

    @Override
    public void onSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void onFailure(int errorCode) {
        dismissProgressDialog();
    }

    private void addActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar == null) return;

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        View mCustomView = LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.iap_action_bar, null);
        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mBackImage = (ImageView) mCustomView.findViewById(R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawable.create(getApplicationContext(), R.drawable.iap_back_arrow);
        mBackImage.setBackground(mBackDrawable);

        mTitleTextView = (TextView) mCustomView.findViewById(R.id.iap_header_title);
        setTitle(getString(R.string.iap_app_name));

        mCartContainer = (FrameLayout) mCustomView.findViewById(R.id.cart_container);
        ImageView mCartIcon = (ImageView) mCustomView.findViewById(R.id.cart_icon);
        Drawable mCartIconDrawable = VectorDrawable.create(getApplicationContext(), R.drawable.iap_shopping_cart);
        mCartIcon.setBackground(mCartIconDrawable);
        mCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ShoppingCartFragment.TAG);
            }
        });

        mCountText = (TextView) mCustomView.findViewById(R.id.item_count);

        mActionBar.setCustomView(mCustomView, params);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }
}
