package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.response.carts.AddToCartData;
import com.philips.cdp.di.iap.response.error.ServerError;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListner;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.ArrayList;

public class DemoAppActivity extends Activity implements View.OnClickListener, IAPHandlerListner {

    IAPHandler mIapHandler;
    private EditText mUsername, mPassword;
    private Button mSubmit;
    private TextView mCountText = null;

    private ArrayList<ShoppingCartData> mProductArrayList = new ArrayList<>();

    private String[] mCatalogNumbers = {"HX8331/11", "HX8071/10", "HX9042/64"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);
        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_userpassword);
        mSubmit = (Button) findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(this);
        mIapHandler = new IAPHandler();

        populateProduct();

        FrameLayout mShoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);
        mShoppingCart.setOnClickListener(this);

        ListView mProductListView = (ListView) findViewById(R.id.product_list);
        ProductListAdapter mProductListAdapter = new ProductListAdapter(this, mProductArrayList);
        mProductListView.setAdapter(mProductListAdapter);

        mCountText = (TextView) findViewById(R.id.count_txt);

        //  mIapHandler.initApp(this, username, password);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                showNetworkError();
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
                showNetworkError();
            }
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.shoppingCart:
                if (Utility.isInternetConnected(DemoAppActivity.this)) {
                    Intent myIntent = new Intent(DemoAppActivity.this, IAPActivity.class);
                    startActivity(myIntent);
                } else {
                    showNetworkError();
                }
                break;
            case R.id.btn_submit:
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                mIapHandler.initApp(this, username, password);
                if (!(Utility.isProgressDialogShowing())) {
                    if (Utility.isInternetConnected(this)) {
                        Utility.showProgressDialog(this, getString(R.string.loading_cart));
                        mIapHandler.getCartQuantity(this);
                    } else {
                        showNetworkError();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetCartQuantity(final int quantity) {
        if (quantity != -1) {
            mCountText.setText(String.valueOf(quantity));
            if (quantity == 0) {
                mCountText.setVisibility(View.GONE);
            } else {
                mCountText.setVisibility(View.VISIBLE);
            }
        }
        Utility.dismissProgressDialog();
    }

    @Override
    public void onAddItemToCart(final Message msg) {
        if (msg.obj instanceof AddToCartData) {
            AddToCartData addToCartData = (AddToCartData) msg.obj;
            if (addToCartData.getStatusCode().equalsIgnoreCase("success")) {
                mIapHandler.getCartQuantity(this);
            } else if (addToCartData.getStatusCode().equalsIgnoreCase("noStock")) {
                Toast.makeText(this, getString(R.string.no_stock), Toast.LENGTH_SHORT).show();
                Utility.dismissProgressDialog();
            }
        } else if (msg.obj instanceof ServerError) {
            ServerError error = (ServerError)msg.obj;
            Utility.dismissProgressDialog();
            Toast.makeText(this, error.getErrors().get(0).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBuyNow() {
        Utility.dismissProgressDialog();
        Intent myIntent = new Intent(DemoAppActivity.this, IAPActivity.class);
        startActivity(myIntent);
    }


    public void showNetworkError() {
        String alertTitle = "Network Error";
        String alertBody = "No network available. Please check your network settings and try again.";
        AlertDialog.Builder alert = new AlertDialog.Builder(DemoAppActivity.this);
        alert.setTitle(alertTitle);
        alert.setMessage(alertBody);
        alert.setPositiveButton(android.R.string.ok,
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

}