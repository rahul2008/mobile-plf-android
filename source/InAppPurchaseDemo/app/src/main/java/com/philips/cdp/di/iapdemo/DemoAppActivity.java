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

import com.philips.cdp.di.iap.activity.ShoppingCartActivity;
import com.philips.cdp.di.iap.data.ProductData;
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

    FrameLayout shoppingCart = null;

    private ArrayList<ProductData> mProductArrayList = new ArrayList<>();

    String[] mCatalogNumbers = {"HX8331/11"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);

        shoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (Utility.isInternetConnected(DemoAppActivity.this)) {
                    Intent myIntent = new Intent(DemoAppActivity.this, ShoppingCartActivity.class);
                    startActivity(myIntent);
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
            Utility.showProgressDialog(this, "Loading Cart");
            HybrisDelegate.getInstance(DemoAppActivity.this).sendRequest(RequestCode.GET_CART, this);
        } else {
            Utility.showNetworkError(this, true);
        }
    }

    /**
     * Populate the product to the list with catalog numbers
     */
    private void populateProduct() {

        for (String mCatalogNumber : mCatalogNumbers) {
            ProductData product = new ProductData();
            product.setCtnNumber(mCatalogNumber);
            mProductArrayList.add(product);
        }
    }

    @Override
    public void onSuccess(Message msg) {
        Utility.dismissProgressDialog();
        switch (msg.what) {
            case RequestCode.GET_CART:
                GetCartData getCartData = (GetCartData) msg.obj;
                mCountText.setText(String.valueOf(getCartData.getEntries().get(0).getQuantity()));
                break;
            case RequestCode.ADD_TO_CART:
                AddToCartData addToCartData = (AddToCartData) msg.obj;
                mCountText.setText(String.valueOf(addToCartData.getEntry().getQuantity()));
                break;
        }


    }

    @Override
    public void onError(Message msg) {
        Utility.dismissProgressDialog();
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }
}
