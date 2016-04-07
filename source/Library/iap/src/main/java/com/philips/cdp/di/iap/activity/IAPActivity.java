package com.philips.cdp.di.iap.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class IAPActivity extends UiKitActivity implements IAPFragmentListener {
    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkBlue_WhiteBackground;
    private TextView mTitleTextView;
    private ImageView mBackButton;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        IAPLog.d(IAPLog.LOG, "OnCreate");
        setContentView(R.layout.iap_activity);
        addActionBar();
        Boolean isShoppingCartViewSelected = getIntent().getBooleanExtra(IAPConstant.IAP_IS_SHOPPING_CART_VIEW_SELECTED,true);
        if(isShoppingCartViewSelected)
            addShoppingFragment();
        else
            addProductCatalog();
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        //Handle invalid index
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        setTheme(themeIndex);
    }

    private void addShoppingFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, new ShoppingCartFragment());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void addProductCatalog() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, new ProductCatalogFragment());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
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

        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.iap_action_bar, null); // layout which contains your button.

        mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

        mBackButton = (ImageView) mCustomView.findViewById(R.id.arrow);
        mBackButton.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_up_arrow));

        frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mActionBar.setCustomView(mCustomView, params);
        mActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void onBackPressed() {
        Utility.hideKeypad(this);
        dispatchBackToFragments();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        CartModelContainer.getInstance().resetApplicationFields();
    }

    @Override
    public void setHeaderTitle(final int pResourceId) {
        mTitleTextView.setText(pResourceId);
    }

    @Override
    public void setBackButtonVisibility(final int isVisible) {
        if(isVisible == View.GONE){
            frameLayout.setEnabled(false);
            frameLayout.setClickable(false);
        }else if (isVisible == View.VISIBLE){
            frameLayout.setEnabled(true);
            frameLayout.setClickable(true);
        }
        mBackButton.setVisibility(isVisible);
    }

    public void dispatchBackToFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible() && (fragment instanceof IAPBackButtonListener)) {
                ((IAPBackButtonListener) fragment).onBackPressed();
            }
        }
    }
}
