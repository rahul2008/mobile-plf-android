package prxclient.cdp.philips.com.prxclientlib;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 09-Nov-15.
 */
public class AssetModelTest extends InstrumentationTestCase {

    private static final String TAG = AssetModelTest.class.getSimpleName();
    PrxDataBuilder mProductAssetBuilder = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mProductAssetBuilder = new ProductAssetBuilder("125", null);
        mProductAssetBuilder.setmCatalogCode("COnsumer");
        mProductAssetBuilder.setmLocale("nl_NL");
        mProductAssetBuilder.setmSectorCode("HAIR");
    }

    public void testAssetDataLoad() {
        JSONObject mJsonObject = null;
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("asset_template_one.txt")));

                // do reading, usually loop until end of file reading
                String mLine = reader.readLine();
                while (mLine != null) {
                    // process line
                    sb.append(mLine);
                    mLine = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                // log the exception
                e.printStackTrace();
            }
            Log.d(TAG, "Parsed Data : " + sb.toString());
            mJsonObject = new JSONObject(sb.toString());
            ResponseData mResponseData = mProductAssetBuilder.getResponseData(mJsonObject);
            AssetModel mAssetModel = new AssetModel();
            ResponseData responseData = mAssetModel.parseJsonResponseData(mJsonObject);
            assertNotNull(responseData);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    public void testAssetDataSuccess() {
        JSONObject mJsonObject = null;
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("asset_template_one.txt")));

                // do reading, usually loop until end of file reading
                String mLine = reader.readLine();
                while (mLine != null) {
                    // process line
                    sb.append(mLine);
                    mLine = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                // log the exception
                e.printStackTrace();
            }
            Log.d(TAG, "Parsed Data : " + sb.toString());
            mJsonObject = new JSONObject(sb.toString());
            ResponseData mResponseData = mProductAssetBuilder.getResponseData(mJsonObject);
            AssetModel assetModel = new AssetModel();
            ResponseData responseData = assetModel.parseJsonResponseData(mJsonObject);
            AssetModel assetModelObject = (AssetModel) responseData;
            assertEquals( assetModelObject.isSuccess() , true);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }


}
