package cdp.cdp.philips.com.prxclientlib;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description : .
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 09-Nov-15.
 */
public class ProductAssetRequestTest extends InstrumentationTestCase {

    private static final String TAG = ProductAssetRequestTest.class.getSimpleName();
    private PrxRequest mProductAssetBuilder = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context mContext = getInstrumentation().getContext();
        mProductAssetBuilder = new ProductAssetRequest("HP8632/00", null, null, null);
        mProductAssetBuilder.setCatalog(PrxConstants.Catalog.CONSUMER);
        mProductAssetBuilder.setSector(PrxConstants.Sector.B2C);
    }


    public void testPrxBuilderObjectWithQueueParameter() {
        mProductAssetBuilder = new ProductAssetRequest("125", null, null, "TAGINFO");
        assertNotNull(mProductAssetBuilder);
    }

    public void testTheAssetJsonData() {

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
                Log.d(TAG, "Error in Input file ");
            }
            JSONObject mJsonObject = new JSONObject(sb.toString());
            assertNotNull(mJsonObject);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    public void testResponseDataofAsset() {
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
                Log.d(TAG, "Error in Input file ");
            }
            Log.d(TAG, "Parsed Data : " + sb.toString());
            JSONObject mJsonObject = new JSONObject(sb.toString());
            ResponseData mResponseData = mProductAssetBuilder.getResponseData(mJsonObject);
            assertNotNull(mResponseData);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }


    public void testAssetResponseSuccess() {
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
                Log.d(TAG, "Error in Input file ");
            }
            Log.d(TAG, "Parsed Data : " + sb.toString());
            JSONObject mJsonObject = new JSONObject(sb.toString());
            //ResponseData mResponseData = mProductAssetBuilder.getResponseData(mJsonObject);
            assertNotNull(mJsonObject);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

}
