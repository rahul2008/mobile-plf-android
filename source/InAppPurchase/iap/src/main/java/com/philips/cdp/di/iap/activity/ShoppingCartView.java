package com.philips.cdp.di.iap.activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.AsyncTaskCompleteListener;
import com.philips.cdp.di.iap.session.CartInfo;
import com.philips.cdp.di.iap.session.InAppPurchase;
import com.philips.cdp.di.iap.session.UpdateProductInfoFromHybris;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartView extends AppCompatActivity implements UpdateProductInfoFromHybris{

    ShoppingCartPriceAdapter mAdapter;
    ListView list;
    ListView listBelow;
    Context mContext;
    String TAG = "SPOORTI";

    //private String mCtn = "RQ1250/17";
    private String mCtn = "HX8331/11";
    private String mSectorCode = "B2C";
    private String mLocale = "en_US";
    private String mCatalogCode = "CONSUMER";
    private String mRequestTag = null;
    ProductSummary productInfo;
    CartInfo cartInfo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_view);
        mContext = this;

        listBelow = (ListView) findViewById(R.id.withouticon);
        mAdapter = new ShoppingCartPriceAdapter(ShoppingCartView.this);
      //  listBelow.setAdapter(mAdapter);

        productInfo = new ProductSummary();
        cartInfo = CartInfo.getInstance();

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.showProgressDialog(mContext,"getting cart details");
        InAppPurchase.getCartCurrentCartRequest(this, this, productInfo,cartInfo);
    }

    @Override
    public void updateProductInfo(final ProductSummary summary, final CartInfo cartInfo) {
        if(summary == null){
            Utility.dismissProgressDialog();
            Toast.makeText(mContext,"Network Error",Toast.LENGTH_LONG).show();
            return;
        }
        summary.price = productInfo.price;
        summary.quantity = productInfo.quantity;
       // Toast.makeText(mContext,"productInfo = " + "price = " + summary.price + "quantity = " + summary.quantity,Toast.LENGTH_SHORT).show();

        PrxLogger.enablePrxLogger(true);
        ProductSummaryBuilder mProductAssetBuilder = new ProductSummaryBuilder(mCtn, mRequestTag);
        mProductAssetBuilder.setmSectorCode(mSectorCode);
        mProductAssetBuilder.setmLocale(mLocale);
        mProductAssetBuilder.setmCatalogCode(mCatalogCode);

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(getApplicationContext());
        mRequestManager.executeRequest(mProductAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                SummaryModel mAssetModel = (SummaryModel) responseData;

                Log.d(TAG, "Positive Response Data : " + mAssetModel.isSuccess());

                Data data = mAssetModel.getData();
                data.getImageURL();
                Log.i(TAG, data.getProductTitle());
                Log.i(TAG, data.getProductURL());
                Log.i(TAG, data.getPrice().toString());
                Log.i(TAG, data.getPriority() + "");

                productInfo.ImageURL = data.getImageURL();
                productInfo.productTitle = data.getProductTitle();

                mAdapter.addItem(productInfo);
                listBelow.setAdapter(mAdapter);
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(mContext,"Network Error",Toast.LENGTH_LONG).show();
                Utility.dismissProgressDialog();
            }
        });
    }

}
