package com.philips.cdp.di.iap.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.data.ProductData;
import com.philips.cdp.di.iap.data.ShoppingCartAdapter;
import com.philips.cdp.di.iap.response.cart.Entries;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.LinkedList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener{

    ShoppingCartAdapter mAdapter;
    ListView list;
    ListView listBelow;
    Context mContext;
   // LinkedList<ProductSummary> productList;
    private static final String TAG = ShoppingCartActivity.class.getName();
    LinkedList<ProductData> productData;

    private Button mCheckoutBtn = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_view);
        mContext = this;

        mCheckoutBtn = (Button)findViewById(R.id.checkout_btn);

        listBelow = (ListView) findViewById(R.id.withouticon);
        mAdapter = new ShoppingCartAdapter(ShoppingCartActivity.this);
      // productList = new LinkedList<ProductSummary>();
        productData = new LinkedList<ProductData>();

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
        Utility.showProgressDialog(mContext, "Getting Cart Details");

        if (Utility.isInternetConnected(this)) {
            HybrisDelegate.getInstance(ShoppingCartActivity.this).sendRequest(RequestCode.GET_CART,
                    new RequestListener() {
                        @Override
                        public void onSuccess(Message msg) {
                            GetCartData data = (GetCartData) msg.obj;

                            if(data.getEntries()==null){
                                Toast.makeText(mContext, "Your Shopping Cart is Currently Empty", Toast.LENGTH_LONG).show();
                                Utility.dismissProgressDialog();
                                return;
                            }

                            ProductData item = new ProductData();
                            item.setQuantity(data.getEntries().get(0).getQuantity());
                            item.setPrice(data.getTotalPrice().getValue());
                            item.setCurrency(data.getTotalPrice().getCurrencyIso());
                            item.setTotalItems(data.getTotalItems());

                            List<Entries> list = data.getEntries();
                            for(int i=0;i<list.size();i++) {
                                getProductDetails(item, list.get(i));
                            }
                        }

                        @Override
                        public void onError(Message msg) {
                            Toast.makeText(ShoppingCartActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            Utility.dismissProgressDialog();
                        }
                    });
        } else {
            Utility.showNetworkError(this, true);
        }
    }

    public void getProductDetails(final ProductData summary, Entries entry){
        if (Utility.isInternetConnected(this)) {
            final String code = entry.getProduct().getCode();
            String mCtn = code.replaceAll("_", "/");
            String mSectorCode = "B2C";
            String mLocale = "en_US";
            String mCatalogCode = "CONSUMER";
            String mRequestTag = null;

            PrxLogger.enablePrxLogger(true);
            ProductSummaryBuilder mProductAssetBuilder = new ProductSummaryBuilder(mCtn, mRequestTag);
            mProductAssetBuilder.setmSectorCode(mSectorCode);
            mProductAssetBuilder.setmLocale(mLocale);
            mProductAssetBuilder.setmCatalogCode(mCatalogCode);

            RequestManager mRequestManager = new RequestManager();
            mRequestManager.init(mContext);
            mRequestManager.executeRequest(mProductAssetBuilder, new ResponseListener() {
                @Override
                public void onResponseSuccess(ResponseData responseData) {

                    SummaryModel mAssetModel = (SummaryModel) responseData;

                    Data data = mAssetModel.getData();

                    summary.setImageUrl(data.getImageURL());
                    summary.setProductTitle(data.getProductTitle());
                    summary.setCtnNumber(code);

                    addToCart(summary);
                }

                @Override
                public void onResponseError(String error, int code) {
                    Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_LONG).show();
                    Utility.dismissProgressDialog();
                }
            });

        } else {
            Utility.showNetworkError(this, true);
        }
    }

    boolean checkDuplicateValues(ProductData item){
        String ctn = item.getCtnNumber();
        for(int i=0;i<productData.size();i++) {
            if (productData.get(i).getCtnNumber().equalsIgnoreCase(ctn)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == mCheckoutBtn){
            Toast.makeText(ShoppingCartActivity.this, "Navigate to shipping View", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToCart(ProductData summary){
        if(!checkDuplicateValues(summary)) {
            productData.add(summary);
            mAdapter.addItem(summary);
        }

        listBelow.setAdapter(mAdapter);
        Utility.dismissProgressDialog();
    }

}
