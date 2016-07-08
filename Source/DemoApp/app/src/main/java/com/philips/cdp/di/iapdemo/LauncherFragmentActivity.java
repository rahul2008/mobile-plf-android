package com.philips.cdp.di.iapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.actionlayout.IAPActionLayout;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;

/**
 * Created by 310164421 on 6/8/2016.
 */
public class LauncherFragmentActivity extends AppCompatActivity {
    IAPHandler mIapHandler;
    IAPSettings mIAPSettings;
    View mCustomView;
    private IAPActionLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launcher_layout);
        getSupportActionBar().hide();
        LaunchIAPFragment();
    }

    private void LaunchIAPFragment() {
        layout = new IAPActionLayout(this, getSupportFragmentManager());
        mCustomView = layout.getCustomView(this);
        ((ViewGroup) findViewById(R.id.ll_custom_action)).addView(mCustomView);
        ViewGroup mUPButtonLayout = (ViewGroup) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_back_button);
        mUPButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        //Launch via interface
        mIAPSettings = new IAPSettings("US", "en", R.style.Theme_Philips_BrightOrange_Gradient);

        mIAPSettings.setLaunchAsFragment(true);
        mIAPSettings.setFragProperties(getSupportFragmentManager(), R.id.vertical_Container);

        mIapHandler = IAPHandler.init(this, mIAPSettings);
        mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW, null, null);
    }

    @Override
    public void onBackPressed() {
        if (!layout.onHWBackPressed())
            super.onBackPressed();
    }
}
