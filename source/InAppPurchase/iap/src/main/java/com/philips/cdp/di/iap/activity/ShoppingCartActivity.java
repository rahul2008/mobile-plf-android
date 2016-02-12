package com.philips.cdp.di.iap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartAdapter;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.UiKitActivity;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartActivity extends UiKitActivity implements View.OnClickListener {

    private static final String TAG = ShoppingCartActivity.class.getName();
    public ShoppingCartAdapter mAdapter;
    public ListView mListView;
    private Button mCheckoutBtn = null;
    private Button mContinueBtn = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_view);

        mCheckoutBtn = (Button) findViewById(R.id.checkout_btn);
        mCheckoutBtn.setOnClickListener(this);

        mContinueBtn = (Button) findViewById(R.id.continues);
        mContinueBtn.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.withouticon);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.iap_action_bar, null); // layout which contains your button.

        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);
        mTitleTextView.setText(getString(R.string.iap_shopping_cart));

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        ImageView imageButton = (ImageView) mCustomView
                .findViewById(R.id.arrow);

        mActionBar.setCustomView(mCustomView, params);
        mActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        mAdapter = new ShoppingCartAdapter(this, new ArrayList<ShoppingCartData>());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.showProgressDialog(this, getString(R.string.iap_get_cart_details));
        ShoppingCartPresenter presenter = new ShoppingCartPresenter(this, mAdapter);
        presenter.getCurrentCartDetails();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.onStop();
    }

    @Override
    public void onClick(View v) {
        if (v == mCheckoutBtn) {
            Intent lIntent = new Intent(ShoppingCartActivity.this, ShippingAddressActivity.class);
            this.startActivity(lIntent);
        } else if (v == mContinueBtn) {
            finish();
        }
    }

    /**
     * Set the checkout button state i.e enable / disable based on the stock availability
     *
     * @param isEnable bool
     */
    public void setCheckoutBtnState(boolean isEnable) {
        mCheckoutBtn.setEnabled(isEnable);
    }
}
