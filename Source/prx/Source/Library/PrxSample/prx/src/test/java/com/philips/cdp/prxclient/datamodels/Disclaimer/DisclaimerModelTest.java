package com.philips.cdp.prxclient.datamodels.Disclaimer;



import com.philips.cdp.prxclient.request.ProductDisclaimerRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class DisclaimerModelTest {
    ProductDisclaimerRequest mProductDisclaimerBuilder;


    @Before
    public void setUp() throws Exception {
        mProductDisclaimerBuilder = new ProductDisclaimerRequest("HX8331/01", null, null, null);

    }


    @Test
    public void testDisclaimerDataLoad() {
        try {
            JSONObject mJsonObject = new JSONObject(getDisclaimerResponse());
            DisclaimerModel disclaimerModel = new DisclaimerModel();
            ResponseData responseData = disclaimerModel.parseJsonResponseData(mJsonObject);
            assertNotNull(responseData);
        } catch (JSONException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSummaryDataLoadToModel() {
        try {
            JSONObject mJsonObject = new JSONObject(getDisclaimerResponse());
            ResponseData mResponseData = mProductDisclaimerBuilder.getResponseData(mJsonObject);
            DisclaimerModel disclaimerModel = (DisclaimerModel) mResponseData;
            assertNotNull(disclaimerModel.getData().getDisclaimers());
            assertNotNull(disclaimerModel.getData().getDisclaimers().getDisclaimer());
            assertNotNull(disclaimerModel.getData().getDisclaimers().getDisclaimer().get(0));
        } catch (JSONException e) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }


    //https://www.philips.com/prx/product/B2C/en_CA/CONSUMER/products/HX8331/01.disclaimers
    private String getDisclaimerResponse() {
        String str = "{\"success\":true, \"data\": {\"disclaimers\":{\"disclaimer\":[{\"disclaimerText\":\"or your money back\",\"code\":\"DIS40008110\",\"rank\":\"1\",\"referenceName\":\"or your money back\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"* when used in conjunction with a manual toothbrush and anti-microbial mouth wash in patients with mild to moderate gingivitis; AirFloss is designed to help inconsistent flossers develop a healthy daily interdental cleaning routine. Please see Q&A under Support tab for further details.\",\"code\":\"DIS40008150\",\"rank\":\"2\",\"referenceName\":\"as effective as floss [AirFloss Pro]\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"* * In a lab study, actual in-mouth results may vary.\",\"code\":\"DIS40008151\",\"rank\":\"3\",\"referenceName\":\"99.9% plaque removal [AirFloss Pro]\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"***AirFloss Ultra and Pro are the same product but may be named differently depending on the country and channel.\",\"code\":\"DIS40012759\",\"rank\":\"4\",\"referenceName\":\"****AirFloss Ultra and Pro are the same product\",\"disclaimElements\":[{}]},{\"disclaimerText\":\"****depending on burst setting used\",\"code\":\"DIS40012816\",\"rank\":\"5\",\"referenceName\":\"*****depending on burst setting used\",\"disclaimElements\":[{}]}]}}}";
        return str;

    }
}