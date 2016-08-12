package com.philips.cdp.di.iapdemo;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPLauncher;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.utils.AppInfraHelper;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uappframework.listener.UappListener;

import java.util.ArrayList;

/**
 * Created by 310164421 on 6/8/2016.
 */
public class LauncherFragmentActivity extends UiKitActivity  {
    IAPHandler mIapHandler;
    UappListener uAppListener;
    ArrayList<String> mProductCTNs;
    private TextView mTitleTextView;
    private TextView mCountText;
    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkPink_WhiteBackground;

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
        AppInfraHelper.getInstance().getIapTaggingInterface().setPreviousPage("demoapp:home");
        //mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW, null, null);
        IAPLauncher iapLauncher = new IAPLauncher();
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPDependencies iapDependencies = new IAPDependencies("en", "US");
        iapLaunchInput.setUseLocalData(false);
        iapLauncher.init(this, iapDependencies);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, mProductCTNs);
        iapLauncher.launch(new FragmentLauncher(this, R.id.vertical_Container, new ActionBarListener() {
            @Override
            public void updateActionBar(@StringRes int i, boolean b) {

                mTitleTextView.setText(i);
            }
        }), iapLaunchInput, uAppListener);

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
        IAPLog.d(IAPLog.BASE_FRAGMENT_ACTIVITY, "IAPActivity == onCreate");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.action_bar, null); // layout which contains your button.

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        ImageView arrowImage = (ImageView) mCustomView.findViewById(R.id.iap_iv_header_back_button);
        //noinspection deprecation
        arrowImage.setBackground(getResources().getDrawable(R.drawable.back_arrow));

        mTitleTextView = (TextView) mCustomView.findViewById(R.id.iap_header_title);
        setTitle(getString(R.string.app_name));

//        FrameLayout cartLayout = (FrameLayout) mCustomView.findViewById(R.id.shopping_cart_icon);
//        cartLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                onBackPressed();
//            }
//        });
        mCountText = (TextView) mCustomView.findViewById(R.id.item_count);

        mActionBar.setCustomView(mCustomView, params);
    }

    private void setLocale(String languageCode, String countryCode) {
        PILLocaleManager localeManager = new PILLocaleManager(LauncherFragmentActivity.this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

}
