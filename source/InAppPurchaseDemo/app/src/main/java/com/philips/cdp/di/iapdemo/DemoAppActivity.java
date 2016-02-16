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

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.activity.EmptyCartActivity;
import com.philips.cdp.di.iap.activity.ShoppingCartActivity;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.response.cart.Entries;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.InAppPurchase;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.UiKitActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DemoAppActivity extends UiKitActivity implements RequestListener {

    private TextView mCountText = null;

    FrameLayout mShoppingCart = null;

    private ArrayList<ShoppingCartData> mProductArrayList = new ArrayList<>();

    String[] mCatalogNumbers = {"HX8331/11"};

    int mCount = 0;
    private ModalAlertDemoFragment modalAlertDemoFragment;
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

    private void showAlert() {
        modalAlertDemoFragment = new ModalAlertDemoFragment();
        modalAlertDemoFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!(Utility.isProgressDialogShowing())) {
            if (Utility.isInternetConnected(this)) {
                Utility.showProgressDialog(this, getString(R.string.loading_cart));
                HybrisDelegate.getInstance(DemoAppActivity.this).sendRequest(RequestCode.GET_CART, this, null);
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
            product.setCtnNumber(mCatalogNumber.replaceAll("/", "_"));
            mProductArrayList.add(product);
        }
    }

    @Override
    public void onSuccess(Message msg) {
        switch (msg.what) {
            case RequestCode.GET_CART: {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    HybrisDelegate.getInstance(this).sendRequest(RequestCode.CREATE_CART, this, null);
                } else {
                    GetCartData getCartData = (GetCartData) msg.obj;

                    int totalItems = getCartData.getCarts().get(0).getTotalItems();
                    List<Entries> entries = getCartData.getCarts().get(0).getEntries();

                    if (totalItems != 0 && entries != null) {

                        mCount = entries.get(0).getQuantity();
                       /* for(int i = 0 ; i < entries.size(); i++){
                            mCount = mCount + entries.get(i).getQuantity();
                        }*/

                    } else if (totalItems == 0) {
                        mCount = 0;
                    }
                    mCountText.setText(String.valueOf(mCount));
                }
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
                    showAlert();
                }

                break;
            }
            case RequestCode.CREATE_CART: {
                mCount = 0;
                mCountText.setText(String.valueOf(mCount));
                break;
            }
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
    void addToCart(boolean isFromBuy, String ctnNumber) {
        if (!(Utility.isProgressDialogShowing())) {
            Utility.showProgressDialog(this, getString(R.string.adding_to_cart));

            HashMap<String, String> params = new HashMap<>();
            params.put(CartModel.PRODUCT_CODE, ctnNumber);

            HybrisDelegate.getInstance(this).sendRequest(RequestCode.ADD_TO_CART, this, params);
        }
        mIsFromBuy = isFromBuy;
    }
}
