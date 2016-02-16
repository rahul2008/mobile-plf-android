package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.activity.EmptyCartActivity;
import com.philips.cdp.di.iap.activity.MainActivity;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class DemoAppActivity extends Activity {
    IAPHandler mIapHandler;
    private TextView mCountText = null;

    private FrameLayout mShoppingCart = null;

    private ArrayList<ShoppingCartData> mProductArrayList = new ArrayList<>();

    private String[] mCatalogNumbers = {"HX8331/11", "HX8071/10"};

    private int mCount = 0;

    private boolean mIsFromBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);
        ListView mProductListView = (ListView) findViewById(R.id.product_list);
        mIapHandler = new IAPHandler();
        mShoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);

        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (Utility.isInternetConnected(DemoAppActivity.this)) {
                    if (mCount == 0) {
                        Intent intent = new Intent(DemoAppActivity.this, EmptyCartActivity.class);
                        startActivity(intent);
                    } else {
                        Intent myIntent = new Intent(DemoAppActivity.this, MainActivity.class);
                        startActivity(myIntent);
                    }
                } else {
                    Utility.showNetworkError(DemoAppActivity.this, false);
                }
            }
        });

        populateProduct();

        ProductListAdapter mProductListAdapter = new ProductListAdapter(this, mProductArrayList);
        mProductListView.setAdapter(mProductListAdapter);

        mCountText = (TextView) findViewById(R.id.count_txt);

        mIapHandler.initApp(this, "", "");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!(Utility.isProgressDialogShowing())) {
            if (Utility.isInternetConnected(this)) {
                Utility.showProgressDialog(this, getString(R.string.loading_cart));
                mIapHandler.getCartQuantity();
            } else {
                Utility.showNetworkError(this, true);
            }
        }
    }

    /**
     * Populate the product to the list with catalog numbers
     */
    private void populateProduct() {
        for (String mCatalogNumber : mCatalogNumbers) {
            ShoppingCartData product = new ShoppingCartData();
            product.setCtnNumber(mCatalogNumber.replaceAll("/", "_")); //need to be checked
            mProductArrayList.add(product);
        }
    }

    /**
     * Returns the current cart mCount
     *
     * @return mCount as integer
     */
    int getCount() {
        return mCount;
    }

    /**
     * Add to cart
     *
     * @param isFromBuy bool
     */
    void addToCart(boolean isFromBuy, String ctnNumber) {
        if (!(Utility.isProgressDialogShowing())) {
            if (Utility.isInternetConnected(this)) {
                Utility.showProgressDialog(this, getString(R.string.adding_to_cart));
                mIapHandler.addItemtoCart(ctnNumber);
            } else {
                Utility.showNetworkError(this, true);
            }
        }
        mIsFromBuy = isFromBuy;
    }
}