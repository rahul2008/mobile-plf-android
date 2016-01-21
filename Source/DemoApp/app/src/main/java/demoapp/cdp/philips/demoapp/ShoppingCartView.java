package demoapp.cdp.philips.demoapp;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
public class ShoppingCartView extends AppCompatActivity{

    ShoppingCartAdapter adapter;
    ShoppingCartBelowListAdapter mAdapter;
    ListView list;
    ListView listBelow;
    Context mContext;
    String TAG = "SPOORTI";

    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
    private String mRequestTag = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_view);
        mContext = this;

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

                ProductSummary productInfo = new ProductSummary();
                productInfo.ImageURL = data.getImageURL();
                productInfo.productTitle = data.getProductTitle();
                productInfo.quantity = 2;
                productInfo.price = "€ " + data.getPrice().getProductPrice();

                listBelow = (ListView) findViewById(R.id.withouticon);
                mAdapter = new ShoppingCartBelowListAdapter(ShoppingCartView.this);
                listBelow.setAdapter(mAdapter);

                mAdapter.addItem(productInfo);


            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
            }
        });

    }
}
