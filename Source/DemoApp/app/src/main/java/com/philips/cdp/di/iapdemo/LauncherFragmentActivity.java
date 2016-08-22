package com.philips.cdp.di.iapdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
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

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

/**
 * Created by 310164421 on 6/8/2016.
 */
public class LauncherFragmentActivity extends UiKitActivity implements ActionBarListener {
    //IAPHandler mIapHandler;
    // UappListener uAppListener;
    ArrayList<String> mProductCTNs;
    private TextView mTitleTextView;
    private TextView mCountText;
    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkPink_WhiteBackground;
    private ImageView mBackImage;
    private ImageView mCartIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DEFAULT_THEME);
        super.onCreate(savedInstanceState);

        addActionBar();
        setContentView(R.layout.fragment_launcher_layout);
        mProductCTNs = new ArrayList<>();
        mProductCTNs.add("HX8331/11");
        setLocale("en", "US");
        LaunchIAPFragment();
    }

    private void LaunchIAPFragment() {
        // mIapHandler = IAPHandler.init(this, mIAPSettings);
        //AppInfraHelper.getInstance().getIapTaggingInterface().setPreviousPage("demoapp:home");
        //mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW, null, null);
        IAPInterface iapInterface = new IAPInterface();
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPSettings iapSettings = new IAPSettings(this);
        IAPDependencies iapDependencies = new IAPDependencies(AppInfraSingleton.getInstance());
        iapSettings.setUseLocalData(false);
        // iapInterface.init(iapDependencies, new IAPSettings(getApplicationContext()));
        IAPFlowInput iapFlowInput = new IAPFlowInput(mProductCTNs);
        iapInterface.init(iapDependencies, iapSettings);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, iapFlowInput);
        iapInterface.launch(new FragmentLauncher(this, R.id.vertical_Container, this), iapLaunchInput);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.vertical_Container);
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
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        IAPLog.d(IAPLog.BASE_FRAGMENT_ACTIVITY, "DemoAppActivity == onCreate");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(com.philips.cdp.di.iap.R.layout.iap_action_bar, null); // layout which contains your button.
        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        mBackImage = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawable.create(getApplicationContext(), com.philips.cdp.di.iap.R.drawable.iap_back_arrow);
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_title);
        setTitle(getString(com.philips.cdp.di.iap.R.string.app_name));
        mCartIcon = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.cart_icon);
        Drawable mCartIconDrawable = VectorDrawable.create(getApplicationContext(), com.philips.cdp.di.iap.R.drawable.iap_shopping_cart);
        mCartIcon.setBackground(mCartIconDrawable);
        mCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(ShoppingCartFragment.createInstance(new Bundle(),
                        BaseAnimationSupportFragment.AnimationType.NONE), ShoppingCartFragment.TAG);
            }
        });
        mCountText = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.item_count);
        mActionBar.setCustomView(mCustomView, params);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(com.philips.cdp.di.iap.R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + newFragmentTag + ")");
    }


    private void setLocale(String languageCode, String countryCode) {
        PILLocaleManager localeManager = new PILLocaleManager(LauncherFragmentActivity.this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    @Override
    public void updateActionBar(@IdRes int resourceId, boolean visibility) {
        if (visibility) {
            mTitleTextView.setText(getString(resourceId));
            mBackImage.setVisibility(View.VISIBLE);
            mCartIcon.setVisibility(View.VISIBLE);
        } else {
            mTitleTextView.setText(getString(resourceId));
            mBackImage.setVisibility(View.INVISIBLE);
            mCartIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateActionBar(String resourceString, boolean visibility) {
        if (visibility) {
            mTitleTextView.setText(resourceString);
            mBackImage.setVisibility(View.VISIBLE);
            mCartIcon.setVisibility(View.VISIBLE);
        } else {
            mTitleTextView.setText(resourceString);
            mBackImage.setVisibility(View.INVISIBLE);
            mCartIcon.setVisibility(View.INVISIBLE);
        }
    }
}
