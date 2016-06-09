package com.philips.cdp.di.iapdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.actionlayout.ActionLayoutCallBack;
import com.philips.cdp.di.iap.actionlayout.IAPActionLayout;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPSettings;

/**
 * Created by 310164421 on 6/8/2016.
 */
public class LauncherFragmentActivity extends AppCompatActivity implements ActionLayoutCallBack {
    IAPHandler mIapHandler;
    IAPSettings mIAPSettings;
    View mCustomView;
    private IAPActionLayout layout;

    private int getContainerId() {
        return R.id.vertical_Container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launcher_layout);
//        ((AppCompatActivity) this).getSupportActionBar().hide();
        LaunchIAPFragment();
    }

    private void LaunchIAPFragment() {
        layout = new IAPActionLayout(this, getSupportFragmentManager());
        mCustomView = layout.getCustomView(this);
        ((ViewGroup) findViewById(R.id.ll_custom_action)).addView(mCustomView);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(getContainerId(), new ProductCatalogFragment(), ProductCatalogFragment.class.getName());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (!layout.onHWBackPressed())
            super.onBackPressed();
    }

    @Override
    public View getCustomView(Context context) {
        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(R.id.ll_custom_action, null);
        return mCustomView;
    }

    @Override
    public void setBackGroundDrawable(Drawable drawable) {

    }

    @Override
    public void setBackButtonDrawable(Drawable drawable) {

    }

    @Override
    public boolean onHWBackPressed() {
        return false;
    }
}
