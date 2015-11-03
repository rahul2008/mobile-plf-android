package horizontal.cdp.prxsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cdp.prx.assets.Asset;
import com.cdp.prx.assets.AssetModel;
import com.cdp.prx.assets.Assets;
import com.cdp.prx.assets.Data;
import com.cdp.prx.databuilder.ProductAssetBuilder;
import com.cdp.prx.databuilder.ProductSummaryBuilder;
import com.cdp.prx.summary.SummaryModel;

import java.util.List;

import horizontal.cdp.prxcomponent.RequestManager;
import horizontal.cdp.prxcomponent.ResponseData;
import horizontal.cdp.prxcomponent.listeners.ResponseHandler;
import horizontal.cdp.prxcomponent.listeners.ResponseListener;

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
        mProductAssetBuilder.setmCtnCode(mCtn);*/

       ProductSummaryBuilder mProductAssetBuilder = new ProductSummaryBuilder(mCtn, mRequestTag);
        mProductAssetBuilder.setmSectorCode(mSectorCode);
        mProductAssetBuilder.setmLocale(mLocale);
        mProductAssetBuilder.setmCatalogCode(mCatalogCode);
        mProductAssetBuilder.setmCtnCode(mCtn);

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(getApplicationContext());
        mRequestManager.executeRequest(mProductAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                SummaryModel mAssetModel = (SummaryModel) responseData.getDataModel();
                com.cdp.prx.summary.Data mData = mAssetModel.getData();



                Log.d(TAG, " Positive Response Data : " + mAssetModel.isSuccess());
                Log.d(TAG, " Positive Response Data : " + mData.getBrand());
                Log.d(TAG, " Positive Response Data : " + mData.getCtn());
                Log.d(TAG, " Positive Response Data : " + mData.getProductTitle());

            }

            @Override
            public void onResponseError(String error) {
                Log.d(TAG, "Negative Response Data : " + error);
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
