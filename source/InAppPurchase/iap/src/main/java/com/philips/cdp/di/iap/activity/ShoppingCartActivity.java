package com.philips.cdp.di.iap.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartAdapter;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener{

    public ShoppingCartAdapter mAdapter;
    public ListView mListView;
    Context mContext;
    ShoppingCartPresenter mShoppingCartPresenter;

    private static final String TAG = ShoppingCartActivity.class.getName();
    private Button mCheckoutBtn = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_view);
        mContext = this;

        mCheckoutBtn = (Button)findViewById(R.id.checkout_btn);

        mCheckoutBtn.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.withouticon);


        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

            View mCustomView = LayoutInflater.from(this).inflate(R.layout.uikit_action_bar, null); // layout which contains your button.

        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);
        mTitleTextView.setText("Shopping Cart");

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

        mShoppingCartPresenter = new ShoppingCartPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.showProgressDialog(this, "Getting Cart Details");
        mShoppingCartPresenter.getCurrentCartDetails();
    }

    @Override
    public void onClick(View v) {
        if(v == mCheckoutBtn){
            Intent lIntent = new Intent(ShoppingCartActivity.this, ShoppingCartActivity.class);
            this.startActivity(lIntent);
            //startActivity(new Intent(ShoppingCartActivity.this, ShippingAddressActivity.class));
        }
    }

    public void addToCart(ArrayList<ShoppingCartData> pShoppingCartDatas) {
        mAdapter = new ShoppingCartAdapter(this,pShoppingCartDatas);
        mListView.setAdapter(mAdapter);
        mShoppingCartPresenter.refreshList();
        Utility.dismissProgressDialog();
    }

    public void deleteEntryFromCart(ShoppingCartData summary){
        mShoppingCartPresenter.deleteProduct(summary);
    }

}
