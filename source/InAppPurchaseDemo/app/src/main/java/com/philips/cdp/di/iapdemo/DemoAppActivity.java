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

import com.philips.cdp.di.iap.Response.Cart.GetCart.GetCartData;
import com.philips.cdp.di.iap.activity.IapConstants;
import com.philips.cdp.di.iap.activity.IapSharedPreference;
import com.philips.cdp.di.iap.activity.Product;
import com.philips.cdp.di.iap.activity.ShoppingCartView;
import com.philips.cdp.di.iap.activity.Utility;
import com.philips.cdp.di.iap.session.AsyncTaskCompleteListener;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.InAppPurchase;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;

import java.util.ArrayList;

public class DemoAppActivity extends Activity implements AsyncTaskCompleteListener {

    private IapSharedPreference mIapSharedPreference = null;

    private TextView mCountText = null;

    FrameLayout shoppingCart = null;

    private ArrayList<Product> mProductArrayList = new ArrayList<>();

    String[] mCatalogNumbers = {"HX8331/11"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        mIapSharedPreference = new IapSharedPreference(this);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);

        shoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (Utility.isInternetConnected(DemoAppActivity.this)) {
                    Intent myIntent = new Intent(DemoAppActivity.this, ShoppingCartView.class);
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

        HybrisDelegate.getInstance(DemoAppActivity.this).initStore("", "");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utility.isInternetConnected(this)) {
//            InAppPurchase.getCurrentCartHybrisServerRequest(this);

            HybrisDelegate.getInstance(DemoAppActivity.this).sendRequest(RequestCode.GET_CART,
                    new RequestListener() {
                        @Override
                        public void onSuccess(Message msg) {
                            GetCartData data = (GetCartData) msg.obj;
                            mCountText.setText(data.getEntries().get(0).getQuantity());
                        }

                        @Override
                        public void onError(Message msg) {
                            Toast.makeText(DemoAppActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            Utility.dismissProgressDialog();
                        }
                    });
        } else {
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
    public void addToCart(boolean isCountZero) {
        InAppPurchase.addToCartHybrisServerRequest(this, isCountZero);
    }

    @Override
    public void onTaskComplete() {
        mCountText.setText(mIapSharedPreference.getString(IapConstants.key.CART_COUNT));
    }
}
