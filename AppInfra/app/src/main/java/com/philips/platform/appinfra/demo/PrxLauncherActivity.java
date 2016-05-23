package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    private String mCtn;
    private String mLocale ;
    private String mLangUageCode, mCountryCode ;
    private Button msupportButton,mSummaryButton,mAssetButton;
    private EditText mCTNEditText;

    private String mRequestTag = null;
    Spinner mSector_spinner_prx,mSector_catalog_prx;
    private String  mSector[],  mCatalog[];
    Sector selectedSector ;
    Catalog selectedCatalog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mSummaryButton = (Button)findViewById(R.id.summary_reqst_button);
        msupportButton = (Button)findViewById(R.id.support_rqst_button);
        mAssetButton = (Button)findViewById(R.id.assets_reqst_button);
        mCTNEditText = (EditText) findViewById(R.id.EditTextCTN);



        ///////////SECTOR AND CATALOG////////////
        // setting sector spinner
        mSector_spinner_prx = (Spinner) findViewById(R.id.prxSpinnerSector);
        mSector = getResources().getStringArray(R.array.sector_list);
        // mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mSector_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mSector);
        mSector_spinner_prx.setAdapter(mSector_adapter);
        mSector_spinner_prx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSector = Sector.valueOf(parent.getAdapter().getItem(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // setting catalog spinner
        mSector_catalog_prx = (Spinner) findViewById(R.id.prxSpinnerCatalog);
        mCatalog = getResources().getStringArray(R.array.catalog_list);
        // mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCatalogy_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCatalog);
        mSector_catalog_prx.setAdapter(mCatalogy_adapter);
        mSector_catalog_prx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCatalog = Catalog.valueOf(parent.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ////////////////
        PILLocaleManager localeManager = new PILLocaleManager(getApplicationContext());
       /* mCountryCode=localeManager.getCountryCode();
        mLangUageCode=localeManager.getLanguageCode();*/
        mCountryCode="GB";
        mLangUageCode="en";
        mLocale=localeManager.getInputLocale();
//        localeManager.setInputLocale(mLangUageCode, mCountryCode);
        PrxLogger.enablePrxLogger(true);

        mSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCtn = mCTNEditText.getText().toString();
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

        mProductAssetBuilder.setSector(selectedSector);
        mProductAssetBuilder.setCatalog(selectedCatalog);
        onRequestManagerCalled(mProductAssetBuilder);

    }
    private void productSupportRequest(){
        ProductSupportRequest mProductSupportBuilder = new ProductSupportRequest(mCtn, mRequestTag);

        mProductSupportBuilder.setSector(selectedSector);
        mProductSupportBuilder.setCatalog(selectedCatalog);
        onRequestManagerCalled(mProductSupportBuilder);

    }
    private void productSummaryRequest(){
        ProductSummaryRequest mProductSummeryBuilder = new ProductSummaryRequest(mCtn, mRequestTag);

        mProductSummeryBuilder.setSector(selectedSector);
        mProductSummeryBuilder.setCatalog(selectedCatalog);
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
        String str = responseData.getClass().toString();
            if(responseData instanceof SummaryModel)
            {
                SummaryModel mSummaryModel = (SummaryModel) responseData;
                Log.d(TAG, "Support Response Data : " + mSummaryModel.isSuccess());
                Data mData = mSummaryModel.getData();
                Log.d(TAG, " Positive Response Data : " + mSummaryModel.isSuccess());
                Log.d(TAG, " Positive Response Data : " + mData.getBrand());
                Log.d(TAG, " Positive Response Data : " + mData.getCtn());
                Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());

            }else    if(responseData instanceof AssetModel){
                AssetModel mAssetModel = (AssetModel) responseData;
            }else {
                SupportModel mSupportModel = (SupportModel) responseData;
            }
//            SupportModel mSupportModel = (SupportModel) responseData;
//            AssetModel mAssetModel = (AssetModel) responseData;

//            switch(responseData != null) {
//                case(responseData.equals(mSummaryModel)):
//
//                    break;
//            }



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
