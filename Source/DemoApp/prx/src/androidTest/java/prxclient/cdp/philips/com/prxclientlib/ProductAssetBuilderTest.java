package prxclient.cdp.philips.com.prxclientlib;

import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
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
public class ProductAssetBuilderTest extends InstrumentationTestCase {

    private static final String TAG = ProductAssetBuilderTest.class.getSimpleName();
    MockContext mContext;
    PrxDataBuilder mProductAssetBuilder = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mProductAssetBuilder = new ProductAssetBuilder("125", null);
        mProductAssetBuilder.setmCatalogCode("COnsumer");
        mProductAssetBuilder.setmLocale("nl_NL");
        mProductAssetBuilder.setmSectorCode("HAIR");
    }

    public void testAssetBuilderObject() {

        String mURL = mProductAssetBuilder.getRequestUrl();
        assertNotNull(mURL);
    }

    public void testBuilderLocale() {
        String locale = mProductAssetBuilder.getLocale();
        assertEquals("nl_NL", locale);
    }


    public void testPrxBuilderServerInfo() {

        String mURL = mProductAssetBuilder.getServerInfo();
        assertEquals("www.philips.com/prx", mURL);
    }

    public void testPrxBuilderSectorCode() {

        String response = mProductAssetBuilder.getSectorCode();
        assertEquals("HAIR", response);
    }

    public void testPrxBuilderCatalogCode() {

        String response = mProductAssetBuilder.getCatalogCode();
        assertEquals("COnsumer", response);
    }

    public void testPrxBuilderObjectWithQueueParameter() {
        mProductAssetBuilder = new ProductAssetBuilder("125", "TAGINFO");
        assertNotNull(mProductAssetBuilder);
    }

    public void testTheAssetJsonData() {

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
            assertNotNull(mJsonObject);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    public void testResponseDataofAsset() {
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
            assertNotNull(mResponseData);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }


    public void testAssetResponseSuccess() {
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
            assertNotNull(mJsonObject);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

}
