package prxclient.cdp.philips.com.prxclientlib;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetRequest;
import com.philips.cdp.prxclient.prxdatabuilder.PrxRequest;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
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
public class SummaryModelTest extends InstrumentationTestCase {

    private static final String TAG = SummaryModelTest.class.getSimpleName();
    PrxRequest mProductAssetBuilder = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mProductAssetBuilder = new ProductAssetRequest("125", null);
        mProductAssetBuilder.setmCatalogCode("COnsumer");
        mProductAssetBuilder.setmLocale("nl_NL");
        mProductAssetBuilder.setmSectorCode("HAIR");
    }

    public void testAssetDataLoad() {
        JSONObject mJsonObject = null;
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("ssummary_template_one.txt")));

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
            SummaryModel summaryModel = new SummaryModel();
            ResponseData responseData = summaryModel.parseJsonResponseData(mJsonObject);
            assertNotNull(responseData);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }
}
