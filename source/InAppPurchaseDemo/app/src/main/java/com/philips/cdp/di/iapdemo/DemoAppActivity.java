package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.di.iap.activity.IapConstants;
import com.philips.cdp.di.iap.activity.IapSharedPreference;
import com.philips.cdp.di.iap.activity.Product;
import com.philips.cdp.di.iap.activity.Utility;
import com.philips.cdp.di.iap.session.AsyncTaskCompleteListener;
import com.philips.cdp.di.iap.session.InAppPurchase;

import java.util.ArrayList;

public class DemoAppActivity extends Activity implements AsyncTaskCompleteListener {

    private IapSharedPreference mIapSharedPreference = null;

    private TextView mCountText = null;

    private ArrayList<Product> mProductArrayList = new ArrayList<>();

    String[] mCatalogNumbers = {"HX8331/11"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        mIapSharedPreference = new IapSharedPreference(this);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);

        populateProduct();

        ProductListAdapter mProductListAdapter = new ProductListAdapter(this, mProductArrayList);
        mProductListView.setAdapter(mProductListAdapter);

        mCountText = (TextView) findViewById(R.id.count_txt);

        if(Utility.isInternetConnected(this)) {
            InAppPurchase.getCurrentCartHybrisServerRequest(this);
        }else{
            Utility.showNetworkError(this, true);
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
     * Add to the current cart
     */
    public void addToCart() {
        InAppPurchase.addToCartHybrisServerRequest(this);
    }

    @Override
    public void onTaskComplete() {
        mCountText.setText(mIapSharedPreference.getString(IapConstants.key.CART_COUNT));
    }
}
