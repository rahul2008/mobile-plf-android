package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.di.iap.activity.IapConstants;
import com.philips.cdp.di.iap.activity.IapSharedPreference;
import com.philips.cdp.di.iap.activity.Product;
import com.philips.cdp.di.iap.activity.ShoppingCartView;
import com.philips.cdp.di.iap.activity.Utility;
import com.philips.cdp.di.iap.session.AsyncTaskCompleteListener;
import com.philips.cdp.di.iap.session.InAppPurchase;

import java.util.ArrayList;

public class DemoAppActivity extends Activity implements AsyncTaskCompleteListener {

    private IapSharedPreference mIapSharedPreference = null;

    private TextView mCountText = null;

    FrameLayout shoppingCart = null;

    Context mContext = null;

    private ArrayList<Product> mProductArrayList = new ArrayList<>();

    String[] mCatalogNumbers = {"HX8331/11", "HX8372/51", "HX8071/10", "HX6064/26", "HX6064/33", "HX9044/26"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        mContext = this;

        mIapSharedPreference = new IapSharedPreference(this);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);

        shoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent myIntent = new Intent(mContext, ShoppingCartView.class);
                mContext.startActivity(myIntent);
            }
        });

        populateProduct();

        ProductListAdapter mProductListAdapter = new ProductListAdapter(this, mProductArrayList);
        mProductListView.setAdapter(mProductListAdapter);

        mCountText = (TextView) findViewById(R.id.count_txt);

        if (mIapSharedPreference.getString(IapConstants.key.CART_NUMBER) != null) {
                InAppPurchase.getCartHybrisServerRequest(mIapSharedPreference.getString(IapConstants.key.CART_NUMBER), this, this);
        }
    }

    /**
     * Populate the product to the list with catalog numbers
     */
    private void populateProduct() {

        for (String mCatalogNumber : mCatalogNumbers) {
            Product product = new Product();
            product.setCtnNumber(mCatalogNumber);
            mProductArrayList.add(product);
        }
    }

    /**
     * Update the count of the product
     */
    public void addToCart(){
        Utility.showProgressDialog(this, "Adding to cart");
        if (mIapSharedPreference.getString(IapConstants.key.CART_NUMBER) != null) {
            InAppPurchase.addToCartHybrisServerRequest(mIapSharedPreference.getString(IapConstants.key.CART_NUMBER), this, this);
        } else {
            InAppPurchase.createCartHybrisServerRequest(this, this);
        }
    }

    @Override
    public void onTaskComplete() {
        mCountText.setText(mIapSharedPreference.getString(IapConstants.key.CART_COUNT));
    }
}
