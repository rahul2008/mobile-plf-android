package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.activity.EmptyCartActivity;
import com.philips.cdp.di.iap.activity.ShoppingCartActivity;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.InAppPurchase;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class DemoAppActivity extends Activity implements RequestListener {

    private TextView mCountText = null;

    FrameLayout mShoppingCart = null;

    private ArrayList<ShoppingCartData> mProductArrayList = new ArrayList<>();

    String[] mCatalogNumbers = {"HX8331/11"};

    int mCount = 0;

    boolean mIsFromBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);

        mShoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);

        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (Utility.isInternetConnected(DemoAppActivity.this)) {
                    if (mCount == 0) {
                        Intent intent = new Intent(DemoAppActivity.this, EmptyCartActivity.class);
                        startActivity(intent);
                    } else {
                        Intent myIntent = new Intent(DemoAppActivity.this, ShoppingCartActivity.class);
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

        InAppPurchase.initApp(this, "", "");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utility.isInternetConnected(this)) {
            Utility.showProgressDialog(this, getString(R.string.loading_cart));
            HybrisDelegate.getInstance(DemoAppActivity.this).sendRequest(RequestCode.GET_CART, this, null);
        } else {
            Utility.showNetworkError(this, true);
        }
    }

    /**
     * Populate the product to the list with catalog numbers
     */
    private void populateProduct() {
        for (String mCatalogNumber : mCatalogNumbers) {
            ShoppingCartData product = new ShoppingCartData();
            product.setCtnNumber(mCatalogNumber);
            mProductArrayList.add(product);
        }
    }

    @Override
    public void onSuccess(Message msg) {
        switch (msg.what) {
            case RequestCode.GET_CART: {
                GetCartData getCartData = (GetCartData) msg.obj;

                if (getCartData.getTotalItems() != 0 && getCartData.getEntries() != null) {
                    mCount = getCartData.getEntries().get(0).getQuantity();
                } else if (getCartData.getTotalItems() == 0) {
                    mCount = 0;
                }
                mCountText.setText(String.valueOf(mCount));
                break;
            }
            case RequestCode.ADD_TO_CART: {
                AddToCartData addToCartData = (AddToCartData) msg.obj;
                if (addToCartData.getStatusCode().equalsIgnoreCase("success")) {
                    mCount = addToCartData.getEntry().getQuantity();
                    mCountText.setText(String.valueOf(mCount));
                    if (getCount() == 1 && mIsFromBuy) {
                        Intent shoppingCartIntent = new Intent(this, ShoppingCartActivity.class);
                        startActivity(shoppingCartIntent);
                    }
                } else if (addToCartData.getStatusCode().equalsIgnoreCase("noStock")) {
                    Toast.makeText(this, getString(R.string.no_stock), Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case RequestCode.CREATE_CART:
                mCountText.setText(String.valueOf(mCount));
                break;
        }
        Utility.dismissProgressDialog();
    }

    @Override
    public void onError(Message msg) {
        Utility.dismissProgressDialog();
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
    void addToCart(boolean isFromBuy) {
        Utility.showProgressDialog(this, getString(R.string.adding_to_cart));
        HybrisDelegate.getInstance(this).sendRequest(RequestCode.ADD_TO_CART, this, null);
        mIsFromBuy = isFromBuy;
    }
}
