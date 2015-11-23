package prxclient.cdp.philips.com.prxsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

public class LauncherActivity extends AppCompatActivity {

    private static final String TAG = LauncherActivity.class.getSimpleName();

    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
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
        mProductAssetBuilder.setmLocale(mLocale);
        mProductAssetBuilder.setmCatalogCode(mCatalogCode);
        mProductAssetBuilder.setmCtnCode(mCtn);

        */
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

                /*SummaryModel mAssetModel = (SummaryModel) responseData.getDataModel();
                com.cdp.prx.summary.Data mData = mAssetModel.getData();



                Log.d(TAG, " Positive Response Data : " + mAssetModel.isSuccess());
                Log.d(TAG, " Positive Response Data : " + mData.getBrand());
                Log.d(TAG, " Positive Response Data : " + mData.getCtn());
                Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());*/

            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
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
