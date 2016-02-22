package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.activity.MainActivity;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListner;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.ArrayList;

public class DemoAppActivity extends Activity implements View.OnClickListener, IAPHandlerListner {

    IAPHandler mIapHandler;

    private TextView mCountText = null;

    private ArrayList<ShoppingCartData> mProductArrayList = new ArrayList<>();

    private String[] mCatalogNumbers = {"HX8331/11", "HX8071/10", "HX9042/64"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        mIapHandler = new IAPHandler();

        populateProduct();

        FrameLayout mShoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);
        mShoppingCart.setOnClickListener(this);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);
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
                mIapHandler.getCartQuantity(this);
            } else {
                NetworkUtility.getInstance().showNetworkError(this);
            }
        }
        /** Should be commented for debug builds */
        final String HOCKEY_APP_ID = "dc402a11ae984bd18f99c07d9b4fe6a4";
        CrashManager.register(this, HOCKEY_APP_ID, new CrashManagerListener() {

            public boolean shouldAutoUploadCrashes() {
                return !IAPLog.isLoggingEnabled();
            }
        });
    }

    private void populateProduct() {
        for (String mCatalogNumber : mCatalogNumbers) {
            ShoppingCartData product = new ShoppingCartData();
            product.setCtnNumber(mCatalogNumber);
            mProductArrayList.add(product);
        }
    }

    void addToCart(String ctnNumber) {
        if (!(Utility.isProgressDialogShowing())) {
            if (Utility.isInternetConnected(this)) {
                Utility.showProgressDialog(this, getString(R.string.adding_to_cart));
                mIapHandler.addItemtoCart(ctnNumber, this, false);
                IAPLog.d(IAPLog.DEMOAPPACTIVITY, "addItemtoCart");
            } else {
                NetworkUtility.getInstance().showNetworkError(this);
            }
        }
    }

    /**
     * Buy Now particular product
     *
     * @param ctnNumber product id
     */
    void buyNow(String ctnNumber) {
        if (!(Utility.isProgressDialogShowing())) {
            if (Utility.isInternetConnected(this)) {
                Utility.showProgressDialog(this, getString(R.string.please_wait));
                mIapHandler.buyNow(ctnNumber, this);
            } else {
                NetworkUtility.getInstance().showNetworkError(this);
            }
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.shoppingCart:
                if (Utility.isInternetConnected(DemoAppActivity.this)) {
                    Intent myIntent = new Intent(DemoAppActivity.this, MainActivity.class);
                    startActivity(myIntent);
                } else {
                    NetworkUtility.getInstance().showNetworkError(this);
                }
                break;
        }
    }

    @Override
    public void onGetCartQuantity(final int quantity) {
        if (quantity != -1) {
            mCountText.setText(String.valueOf(quantity));
        }
        Utility.dismissProgressDialog();
    }

    @Override
    public void onAddItemToCart(final String responseStatus) {
        if (responseStatus != null) {
            if (responseStatus.equalsIgnoreCase("success")) {
                mIapHandler.getCartQuantity(this);
            } else if (responseStatus.equalsIgnoreCase("noStock")) {
                Toast.makeText(this, getString(R.string.no_stock), Toast.LENGTH_SHORT).show();
                Utility.dismissProgressDialog();
            } else {
                Toast.makeText(this, responseStatus, Toast.LENGTH_SHORT).show();
                Utility.dismissProgressDialog();
            }
        }
    }

    @Override
    public void onBuyNow() {
        Utility.dismissProgressDialog();
        Intent myIntent = new Intent(DemoAppActivity.this, MainActivity.class);
        startActivity(myIntent);
    }
}