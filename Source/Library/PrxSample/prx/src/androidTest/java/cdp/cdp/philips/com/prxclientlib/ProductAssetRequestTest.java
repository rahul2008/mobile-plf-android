package cdp.cdp.philips.com.prxclientlib;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.prxclient.PRXDependencies;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfra;

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
    Context mContext;
    PrxRequest mProductAssetBuilder = null;
    PRXDependencies prxDependencies;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        prxDependencies = new PRXDependencies(mContext, new AppInfra.Builder().build(mContext));

        mProductAssetBuilder = new ProductAssetRequest("HP8632/00", null, null, null);
        //   mProductAssetBuilder.setCatalogCode("COnsumer");
        mProductAssetBuilder.setCatalog(PrxConstants.Catalog.CONSUMER);
        mProductAssetBuilder.setSector(PrxConstants.Sector.B2C);
        //  mProductAssetBuilder.setSectorCode("HAIR");
    }

    public void testAssetBuilderObject() {

        //    String mURL = mProductAssetBuilder.getRequestUrl();

//        mProductAssetBuilder.getRequestUrlFromAppInfra(prxDependencies.getAppInfra(), new PrxRequest.OnUrlReceived() {
//            @Override
//            public void onSuccess(String url) {
//                Log.e("KAVYA", url);
//               // assertNotNull(url);
//            }
//
//            @Override
//            public void onError(ERRORVALUES errorvalues, String s) {
//                Log.e("KAVYA", errorvalues.toString());
//               // assertNotNull(errorvalues);
//            }
//        });
        //   assertNotNull(mURL);
    }

//    public void testBuilderLocale() {
//        String locale = mProductAssetBuilder.getLocaleMatchResult();
//        assertEquals("nl_NL", locale);
//    }


    public void testPrxBuilderServerInfo() {

        // String mURL = mProductAssetBuilder.getRequestUrl();
        //   assertNotNull("http://www.philips.com/prx/product/HAIR/nl_NL/COnsumer/products/125.assets", mURL);

    }

    /*public void testPrxBuilderSectorCode() {

        String response = mProductAssetBuilder.getSectorCode();
        assertEquals("HAIR", response);
    }

    public void testPrxBuilderCatalogCode() {

        String response = mProductAssetBuilder.getCatalogCode();
        assertEquals("COnsumer", response);
    }*/

    public void testPrxBuilderObjectWithQueueParameter() {
        mProductAssetBuilder = new ProductAssetRequest("125", null, null, "TAGINFO");
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
