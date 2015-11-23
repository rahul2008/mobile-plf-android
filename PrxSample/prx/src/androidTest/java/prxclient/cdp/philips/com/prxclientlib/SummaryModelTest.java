package prxclient.cdp.philips.com.prxclientlib;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.PrxDataBuilder;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.summary.Price;
import com.philips.cdp.prxclient.prxdatamodels.summary.ReviewStatistics;
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
    private PrxDataBuilder mProductAssetBuilder = null;
    private ReviewStatistics mReviewStatistics = null;
    private Price mPrice = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mProductAssetBuilder = new ProductAssetBuilder("125", null);
        mProductAssetBuilder.setmCatalogCode("Consumer");
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


    public void testReviewStaticTotalCountLogic() {
        mReviewStatistics = new ReviewStatistics();
        mReviewStatistics.setAverageOverallRating(PRXComponentConstant.REVIEW_STATICS_TOTAL);
        mReviewStatistics.setTotalReviewCount(PRXComponentConstant.REVIEW_STATICS_AVAERAGE);

        assertNotNull(mReviewStatistics);
        assertEquals(mReviewStatistics.getTotalReviewCount(), 1206l);
    }


    public void testReviewStaticAverageCountLogic() {
        mReviewStatistics = new ReviewStatistics();
        mReviewStatistics.setAverageOverallRating(PRXComponentConstant.REVIEW_STATICS_TOTAL);
        mReviewStatistics.setTotalReviewCount(PRXComponentConstant.REVIEW_STATICS_AVAERAGE);

        assertNotNull(mReviewStatistics);
        assertEquals(mReviewStatistics.getAverageOverallRating(), 1245.6);
    }

    public void testReviewStaticAverageCountLogic2() {
        mReviewStatistics = new ReviewStatistics(PRXComponentConstant.REVIEW_STATICS_TOTAL, PRXComponentConstant.REVIEW_STATICS_AVAERAGE);
        assertEquals(1245.6, mReviewStatistics.getAverageOverallRating());
    }


    public void testPriceCurrencyCode() {
        mPrice = new Price();
        mPrice.setCurrencyCode(PRXComponentConstant.CURRENCYCODE);
        mPrice.setDisplayPrice(PRXComponentConstant.DISPLAYPRICE);
        mPrice.setDisplayPriceType(PRXComponentConstant.DISPLAYPRICETYPE);
        mPrice.setFormattedDisplayPrice(PRXComponentConstant.FORMATTEDDISPLAYPRICE);
        mPrice.setFormattedPrice(PRXComponentConstant.FORMATTED_PRICE);
        mPrice.setProductPrice(PRXComponentConstant.PRODUCT_PRICE);
        assertEquals("INR", mPrice.getCurrencyCode());
    }


    public void testDisplayPrice() {
        mPrice = new Price();
        mPrice.setCurrencyCode(PRXComponentConstant.CURRENCYCODE);
        mPrice.setDisplayPrice(PRXComponentConstant.DISPLAYPRICE);
        mPrice.setDisplayPriceType(PRXComponentConstant.DISPLAYPRICETYPE);
        mPrice.setFormattedDisplayPrice(PRXComponentConstant.FORMATTEDDISPLAYPRICE);
        mPrice.setFormattedPrice(PRXComponentConstant.FORMATTED_PRICE);
        mPrice.setProductPrice(PRXComponentConstant.PRODUCT_PRICE);
        assertEquals("12.5", mPrice.getDisplayPrice());
    }


    public void testDisplayPriceType() {
        mPrice = new Price();
        mPrice.setCurrencyCode(PRXComponentConstant.CURRENCYCODE);
        mPrice.setDisplayPrice(PRXComponentConstant.DISPLAYPRICE);
        mPrice.setDisplayPriceType(PRXComponentConstant.DISPLAYPRICETYPE);
        mPrice.setFormattedDisplayPrice(PRXComponentConstant.FORMATTEDDISPLAYPRICE);
        mPrice.setFormattedPrice(PRXComponentConstant.FORMATTED_PRICE);
        mPrice.setProductPrice(PRXComponentConstant.PRODUCT_PRICE);
        assertEquals("Rupees", mPrice.getDisplayPriceType());
    }


    public void testFormattedDisplayPrice() {
        mPrice = new Price();
        mPrice.setCurrencyCode(PRXComponentConstant.CURRENCYCODE);
        mPrice.setDisplayPrice(PRXComponentConstant.DISPLAYPRICE);
        mPrice.setDisplayPriceType(PRXComponentConstant.DISPLAYPRICETYPE);
        mPrice.setFormattedDisplayPrice(PRXComponentConstant.FORMATTEDDISPLAYPRICE);
        mPrice.setFormattedPrice(PRXComponentConstant.FORMATTED_PRICE);
        mPrice.setProductPrice(PRXComponentConstant.PRODUCT_PRICE);
        assertEquals("12.8", mPrice.getFormattedDisplayPrice());
    }


    public void testFormattedPrice() {
        mPrice = new Price();
        mPrice.setCurrencyCode(PRXComponentConstant.CURRENCYCODE);
        mPrice.setDisplayPrice(PRXComponentConstant.DISPLAYPRICE);
        mPrice.setDisplayPriceType(PRXComponentConstant.DISPLAYPRICETYPE);
        mPrice.setFormattedDisplayPrice(PRXComponentConstant.FORMATTEDDISPLAYPRICE);
        mPrice.setFormattedPrice(PRXComponentConstant.FORMATTED_PRICE);
        mPrice.setProductPrice(PRXComponentConstant.PRODUCT_PRICE);
        assertEquals("12", mPrice.getFormattedPrice());
    }


    public void testProductPrice() {
        mPrice = new Price(PRXComponentConstant.CURRENCYCODE, PRXComponentConstant.DISPLAYPRICE, PRXComponentConstant.DISPLAYPRICETYPE, PRXComponentConstant.FORMATTEDDISPLAYPRICE
                , PRXComponentConstant.FORMATTED_PRICE, PRXComponentConstant.PRODUCT_PRICE);
        assertEquals("12.8 Rupees", mPrice.getProductPrice());
    }


}
