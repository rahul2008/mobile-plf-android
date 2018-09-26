package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.response.ResponseData;



import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductDisclaimerRequestTest {
    private static final String TAG = ProductDisclaimerRequestTest.class.getSimpleName();

    private PrxRequest mProductDisclaimerBuilder = null;

    @Before
    public void setUp() throws Exception {
        mProductDisclaimerBuilder = new ProductSummaryRequest("HX8331/01", null, null, null);

    }

    @Test
    public void testPrxBuilderObjectWithQueueParameter() {
        mProductDisclaimerBuilder = new ProductAssetRequest("HX8331/01", null, null, "TAGINFO");
        assertNotNull(mProductDisclaimerBuilder);
    }

    @Test
    public void testSummaryResponseObject() {
        try {
            JSONObject mJsonObject = new JSONObject(getDisclaimerResponse());
            assertNotNull(mJsonObject);
            ResponseData mResponseData = mProductDisclaimerBuilder.getResponseData(mJsonObject);
            assertNotNull(mResponseData);
        } catch (JSONException e) {
            fail();

        } catch (Exception e) {
            fail();
        }
    }

   /*
   * This is sample response for disclaimer request:
   * Below url is used to fetch sample response data and can be verified in browser
   * https://www.philips.com/prx/product/B2C/en_CA/CONSUMER/products/HX8331/01.disclaimers
   * */
    private String getDisclaimerResponse() {
        String str = "{\"success\":true, \"data\": {\"disclaimers\":{\"disclaimer\":[{\"disclaimerText\":\"or your money back\",\"code\":\"DIS40008110\",\"rank\":\"1\",\"referenceName\":\"or your money back\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"* when used in conjunction with a manual toothbrush and anti-microbial mouth wash in patients with mild to moderate gingivitis; AirFloss is designed to help inconsistent flossers develop a healthy daily interdental cleaning routine. Please see Q&A under Support tab for further details.\",\"code\":\"DIS40008150\",\"rank\":\"2\",\"referenceName\":\"as effective as floss [AirFloss Pro]\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"* * In a lab study, actual in-mouth results may vary.\",\"code\":\"DIS40008151\",\"rank\":\"3\",\"referenceName\":\"99.9% plaque removal [AirFloss Pro]\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"***AirFloss Ultra and Pro are the same product but may be named differently depending on the country and channel.\",\"code\":\"DIS40012759\",\"rank\":\"4\",\"referenceName\":\"****AirFloss Ultra and Pro are the same product\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"****depending on burst setting used\",\"code\":\"DIS40012816\",\"rank\":\"5\",\"referenceName\":\"*****depending on burst setting used\",\"disclaimElements\":[{}]}]}}}";
        return str;
    }

}