package com.philips.cdp.di.iapdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

public class LauncherFragmentActivity extends UiKitActivity
        implements ActionBarListener, IAPListener, View.OnClickListener {

    private TextView mTitleTextView;
    private TextView mCountText;
    private ImageView mBackImage;
    private FrameLayout mCartContainer;

    ArrayList<String> mProductCTNs;

    IAPInterface mIapInterface;
    IAPFlowInput mIapFlowInput;
    IAPLaunchInput mIapLaunchInput;
    IAPFlowInput iapFlowInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int DEFAULT_THEME = com.philips.cdp.di.iap.R.style.Theme_Philips_DarkBlue_WhiteBackground;
        setTheme(DEFAULT_THEME);

        super.onCreate(savedInstanceState);

        initIAP();
        addActionBar();
        setContentView(R.layout.fragment_launcher_layout);

        mProductCTNs = new ArrayList<>();
        mProductCTNs.add("HX8331/11");
        setLocale("en", "US");

        launchInAppAsFragment();
    }

    private void initIAP() {
        mIapInterface = new IAPInterface();
        mIapLaunchInput = new IAPLaunchInput();
        IAPSettings iapSettings = new IAPSettings(this);
        DemoApplication application = (DemoApplication) getApplicationContext();
        IAPDependencies iapDependencies = new IAPDependencies(application.getAppInfra());
        iapSettings.setUseLocalData(false);
        iapFlowInput = new IAPFlowInput(mProductCTNs);
        mIapInterface.init(iapDependencies, iapSettings);
        mIapLaunchInput.setIapListener(this);
    }

    private void launchInAppAsFragment() {
        mIapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, iapFlowInput);
        try {
            mIapInterface.launch(new FragmentLauncher(this, R.id.vertical_Container, this), mIapLaunchInput);
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.vertical_Container);
        if (fragmentManager.getBackStackEntryCount() == 1) finish();
        boolean backState = false;
        if (currentFrag != null && currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
        }
        if (!backState) {
            super.onBackPressed();
        }
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
                inflate(com.philips.cdp.di.iap.R.layout.iap_action_bar, null);

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById
                (com.philips.cdp.di.iap.R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mBackImage = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawable.create(getApplicationContext(),
                com.philips.cdp.di.iap.R.drawable.iap_back_arrow);
        mBackImage.setBackground(mBackDrawable);

        mTitleTextView = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_title);
        setTitle(getResources().getString(R.string.demo_app_name));

        mCartContainer = (FrameLayout) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.cart_container);
        ImageView mCartIcon = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.cart_icon);
        Drawable mCartIconDrawable = VectorDrawable.create(getApplicationContext(),
                com.philips.cdp.di.iap.R.drawable.iap_shopping_cart);
        mCartIcon.setBackground(mCartIconDrawable);
        mCartContainer.setOnClickListener(this);
        mCountText = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.item_count);

        mActionBar.setCustomView(mCustomView, params);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void setLocale(String languageCode, String countryCode) {
        PILLocaleManager localeManager = new PILLocaleManager(LauncherFragmentActivity.this);
        localeManager.setInputLocale(languageCode, countryCode);
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
        mIapInterface.getProductCartCount(this);
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
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(int errorCode) {
    }

    @Override
    public void onClick(View v) {
        if (v == mCartContainer) {
            mIapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, mIapFlowInput);
            mIapInterface.launch(new FragmentLauncher(this, R.id.vertical_Container, this), mIapLaunchInput);
        }
    }
}
