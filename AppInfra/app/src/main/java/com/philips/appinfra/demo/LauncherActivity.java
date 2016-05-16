package com.philips.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();

    private String mCtn = "RQ1250/17";
    private String mLocale = "en_GB";
    private String mLangUageCode = "en", mCountryCode = "GB";

    private String mRequestTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
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
        PrxLogger.enablePrxLogger(true);

        PILLocaleManager localeManager = new PILLocaleManager(getApplicationContext());
        localeManager.setInputLocale(mLangUageCode, mCountryCode);

        ProductSummaryRequest mProductAssetBuilder = new ProductSummaryRequest(mCtn, mRequestTag);
        mProductAssetBuilder.setSector(Sector.B2C);
        mProductAssetBuilder.setCatalog(Catalog.CONSUMER);

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(getApplicationContext());
        Log.d(TAG, "Positive Request");
        mRequestManager.executeRequest(mProductAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                SummaryModel mAssetModel = (SummaryModel) responseData;

                Log.d(TAG, "Support Response Data : " + mAssetModel.isSuccess());
                Data mData = mAssetModel.getData();


                Log.d(TAG, " Positive Response Data : " + mAssetModel.isSuccess());
                Log.d(TAG, " Positive Response Data : " + mData.getBrand());
                Log.d(TAG, " Positive Response Data : " + mData.getCtn());
                Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());

            }

            @Override
            public void onResponseError(PrxError prxError) {
                Log.d(TAG, "Response Error Message : " + prxError.getDescription());
            }
        });
    }

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
