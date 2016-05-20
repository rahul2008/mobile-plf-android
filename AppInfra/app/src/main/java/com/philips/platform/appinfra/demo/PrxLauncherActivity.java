package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.ProductSupportRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

public class PrxLauncherActivity extends AppCompatActivity {

    private static final String TAG = PrxLauncherActivity.class.getSimpleName();

    private String mCtn = "RQ1250/17";
    private String mLocale = "en_GB";
    private String mLangUageCode = "en", mCountryCode = "GB";
    private Button msupportButton,mSummaryButton,mAssetButton;

    private String mRequestTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mSummaryButton = (Button)findViewById(R.id.summary_reqst_button);
        msupportButton = (Button)findViewById(R.id.support_rqst_button);
        mAssetButton = (Button)findViewById(R.id.assets_reqst_button);

        PILLocaleManager localeManager = new PILLocaleManager(getApplicationContext());
        localeManager.setInputLocale(mLangUageCode, mCountryCode);
        PrxLogger.enablePrxLogger(true);

        mSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSummaryRequest();
            }
        });
        msupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSupportRequest();
            }
        });
        mAssetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productAssetRequest();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

   /*   ProductAssetBuilder mProductAssetBuilder = new ProductAssetBuilder(mCtn, mRequestTag);
        mProductAssetBuilder.setmSectorCode(mSectorCode);
        mProductAssetBuilder.setLocale(mLocale);
        mProductAssetBuilder.setCatalogCode(mCatalogCode);
        mProductAssetBuilder.setmCtnCode(mCtn);

        */



    }
    private void productAssetRequest(){
        ProductAssetRequest mProductAssetBuilder = new ProductAssetRequest(mCtn, mRequestTag);

        mProductAssetBuilder.setSector(Sector.B2C);
        mProductAssetBuilder.setCatalog(Catalog.CONSUMER);
        onRequestManagerCalled(mProductAssetBuilder);

    }
    private void productSupportRequest(){
        ProductSupportRequest mProductSupportBuilder = new ProductSupportRequest(mCtn, mRequestTag);

        mProductSupportBuilder.setSector(Sector.B2C);
        mProductSupportBuilder.setCatalog(Catalog.CONSUMER);
        onRequestManagerCalled(mProductSupportBuilder);

    }
    private void productSummaryRequest(){
        ProductSummaryRequest mProductSummeryBuilder = new ProductSummaryRequest(mCtn, mRequestTag);

        mProductSummeryBuilder.setSector(Sector.B2C);
        mProductSummeryBuilder.setCatalog(Catalog.CONSUMER);
        onRequestManagerCalled(mProductSummeryBuilder);

    }
        private void onRequestManagerCalled(PrxRequest prxRequest)
        {


    RequestManager mRequestManager = new RequestManager();
    mRequestManager.init(getApplicationContext());
    Log.d(TAG, "Positive Request");
    mRequestManager.executeRequest(prxRequest, new ResponseListener() {
        @Override
        public void onResponseSuccess(ResponseData responseData) {

            SummaryModel mSummaryModel = (SummaryModel) responseData;
            SupportModel mSupportModel = (SupportModel) responseData;
            AssetModel mAssetModel = (AssetModel) responseData;

//            switch(responseData != null) {
//                case(responseData.equals(mSummaryModel)):
//
//                    break;
//            }
            Log.d(TAG, "Support Response Data : " + mSummaryModel.isSuccess());
            Data mData = mSummaryModel.getData();
            Log.d(TAG, " Positive Response Data : " + mSummaryModel.isSuccess());
            Log.d(TAG, " Positive Response Data : " + mData.getBrand());
            Log.d(TAG, " Positive Response Data : " + mData.getCtn());
            Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());


//                if(responseData.equals(mSummaryModel)){
////                onLogedResponce(mSummaryModel);
//                    Log.d(TAG, "Support Response Data : " + mSummaryModel.isSuccess());
//                    Data mData = mSummaryModel.getData();
//                    Log.d(TAG, " Positive Response Data : " + mSummaryModel.isSuccess());
//                    Log.d(TAG, " Positive Response Data : " + mData.getBrand());
//                    Log.d(TAG, " Positive Response Data : " + mData.getCtn());
//                    Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());
//                }else if(responseData.equals(mSupportModel)){

//                    Log.d(TAG, "Support Response Data : " + mSupportModel.isSuccess());
//                    mSupportModel.setData(responseData);
//                    com.philips.cdp.prxclient.datamodels.support.Data mData = mSupportModel.getData();
//                    Log.d(TAG, " Positive Response Data : " + mSupportModel.isSuccess());
//                    Log.d(TAG, " Positive Response Data : " + mSupportModel.getData().getBrand());
//                    Log.d(TAG, " Positive Response Data : " + mData.getCtn());
//                    Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());
//                }else if(responseData.equals(mAssetModel)){
//                    onLogedResponce(mAssetModel);
//                }


        }

        @Override
        public void onResponseError(PrxError prxError) {
            Log.d(TAG, "Response Error Message : " + prxError.getDescription());
        }
    });

}
//    public void onLogedResponce(ResponseData mresponceData){
//        if(mresponceData.equals(msu))
//
//        Log.d(TAG, "Support Response Data : " + mresponceData.isSuccess());
//        Data mData = mSupportModel.getData();
//        Log.d(TAG, " Positive Response Data : " + mSupportModel.isSuccess());
//        Log.d(TAG, " Positive Response Data : " + mData.getBrand());
//        Log.d(TAG, " Positive Response Data : " + mData.getCtn());
//        Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
