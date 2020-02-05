package com.philips.cdp.prxclient.request;

import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductFeaturesRequestTest {

    private static final String TAG = ProductFeaturesRequest.class.getSimpleName();

    private PrxRequest mProductFeaturesRequest = null;

    @Before
    public void setUp() throws Exception {
        mProductFeaturesRequest = new ProductFeaturesRequest("HD9630/96", null, null, null);

    }

    @Test
    public void testPrxBuilderObjectWithQueueParameter() {
        mProductFeaturesRequest = new ProductAssetRequest("HD9630/96", null, null, "TAGINFO");
        assertNotNull(mProductFeaturesRequest);
    }

    @Test
    public void testFeaturesResponseObject() {
        try {
            JSONObject mJsonObject = new JSONObject(getFeatureResponse());
            assertNotNull(mJsonObject);
            ResponseData mResponseData = mProductFeaturesRequest.getResponseData(mJsonObject);
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
    private String getFeatureResponse() {
        String str = "{\n" +
                "\"success\": true,\n" +
                "\"data\": {\n" +
                "\"keyBenefitArea\": [\n" +
                "{\n" +
                "\"keyBenefitAreaCode\": \"K4038339\",\n" +
                "\"keyBenefitAreaName\": \"The healthiest way to fry\",\n" +
                "\"keyBenefitAreaRank\": \"1\",\n" +
                "\"feature\": [\n" +
                "{\n" +
                "\"featureCode\": \"F400093026\",\n" +
                "\"featureReferenceName\": \"Fat Removal technology\",\n" +
                "\"featureName\": \"Fat Removal technology\",\n" +
                "\"featureLongDescription\": \"Fat Removal technology separates and captures excess fat\",\n" +
                "\"featureGlossary\": \"Eat healthier dishes with excess fat removed from food. The Philips Airfryer is the only Airfryer with Fat Removal Technology that separates and captures excess fat. Enjoy delicious food that’s crispy on the outside and tender on the inside with maximum taste and minimum fat.\",\n" +
                "\"featureRank\": \"1\",\n" +
                "\"featureTopRank\": \"4\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093027\",\n" +
                "\"featureReferenceName\": \"Rapid Air technology\",\n" +
                "\"featureName\": \"Rapid Air technology\",\n" +
                "\"featureLongDescription\": \"Rapid Air technology for delicious crispier results\",\n" +
                "\"featureGlossary\": \"Philips' Rapid Air technology creates 7x faster airfow for deliciously crispy results*. Enjoy healthier and tasty snacks and meals that are crisped to perfection yet tender on the inside\",\n" +
                "\"featureRank\": \"2\",\n" +
                "\"featureTopRank\": \"5\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093028\",\n" +
                "\"featureReferenceName\": \"Fry with little or no oil\",\n" +
                "\"featureName\": \"Fry with little or no oil\",\n" +
                "\"featureLongDescription\": \"Fry with little or no oil\",\n" +
                "\"featureGlossary\": \"The Airfryer uses hot air to cook your favorite food with little or no added oil, so you can fry with up to 90% less fat*. Enjoy great-tasting, crispy results like deep fried, with the least amount of fat.\",\n" +
                "\"featureRank\": \"3\",\n" +
                "\"featureTopRank\": \"6\"\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "{\n" +
                "\"keyBenefitAreaCode\": \"K4038340\",\n" +
                "\"keyBenefitAreaName\": \"Designed for your daily cooking\",\n" +
                "\"keyBenefitAreaRank\": \"2\",\n" +
                "\"feature\": [\n" +
                "{\n" +
                "\"featureCode\": \"F400093103\",\n" +
                "\"featureReferenceName\": \"XXL family size meals\",\n" +
                "\"featureName\": \"XXL family size meals\",\n" +
                "\"featureLongDescription\": \"XXL family size fits a whole chicken or 1.4 kg of fries\",\n" +
                "\"featureGlossary\": \"Yes, you can cook family-size meals in the new Airfryer XXL. It's full-size capacity makes cooking a large, delicious meal easy. You can cook a whole chicken or even up to 1.4 kg of fries to satisfy hungry family or friends. Serve up to six portions with the large 7.3L capacity basket.\",\n" +
                "\"featureRank\": \"1\",\n" +
                "\"featureTopRank\": \"3\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093031\",\n" +
                "\"featureReferenceName\": \"1.5 times faster than an oven\",\n" +
                "\"featureName\": \"1.5 times faster than an oven\",\n" +
                "\"featureLongDescription\": \"1.5 times faster than an oven*\",\n" +
                "\"featureGlossary\": \"Cooking is faster and more convenient than ever with the Philips Airfryer. Thanks to our instant heat and Rapid Airflow technology, your food will cook 1.5 times faster than in an oven. Best of all you don't need to preheat your AirFryer. Just turn it on and start cooking.\",\n" +
                "\"featureRank\": \"2\",\n" +
                "\"featureTopRank\": \"7\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093033\",\n" +
                "\"featureReferenceName\": \"QuickClean & dishwasher-safe\",\n" +
                "\"featureName\": \"QuickClean & dishwasher-safe\",\n" +
                "\"featureLongDescription\": \"QuickClean and dishwasher-safe for all removable parts\",\n" +
                "\"featureGlossary\": \"Clean-up is fast and easy thanks to the Airfryer QuickClean basket with removable non-stick mesh insert. Both the basket and removable drawer with non-stick coating are also dishwasher-safe for no-fuss cleaning.\",\n" +
                "\"featureRank\": \"3\",\n" +
                "\"featureTopRank\": \"8\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093032\",\n" +
                "\"featureReferenceName\": \"Keep Warm mode\",\n" +
                "\"featureName\": \"Keep Warm mode\",\n" +
                "\"featureLongDescription\": \"Keep Warm mode for flexible serving time\",\n" +
                "\"featureGlossary\": \"With our handy Keep Warm mode, you can enjoy your meal when you're ready. It will keep your food warm and at the ideal temperature for up to 30 minutes.\",\n" +
                "\"featureRank\": \"4\",\n" +
                "\"featureTopRank\": \"9\"\n" +
                "}\n" +
                "]\n" +
                "},\n" +
                "{\n" +
                "\"keyBenefitAreaCode\": \"K4038341\",\n" +
                "\"keyBenefitAreaName\": \"Variety of cooking possibilities\",\n" +
                "\"keyBenefitAreaRank\": \"3\",\n" +
                "\"feature\": [\n" +
                "{\n" +
                "\"featureCode\": \"F400093035\",\n" +
                "\"featureReferenceName\": \"Bake. Grill. Roast. Or reheat\",\n" +
                "\"featureName\": \"Bake. Grill. Roast. Or reheat\",\n" +
                "\"featureLongDescription\": \"Fry. Bake. Grill. Roast. And even reheat.\",\n" +
                "\"featureGlossary\": \"You can make hundreds of dishes in your Airfryer. Fry, bake, grill, roast and even reheat your meals. Every bite is as delicious as the last thanks to Philips Air flow and starfish design. It cooks food uniformly from all sides for perfect meals every time.\",\n" +
                "\"featureRank\": \"1\",\n" +
                "\"featureTopRank\": \"1\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093038\",\n" +
                "\"featureReferenceName\": \"Inspiring recipes.\",\n" +
                "\"featureName\": \"Inspiring recipes\",\n" +
                "\"featureLongDescription\": \"Hundreds of recipes in app and free recipe book included\",\n" +
                "\"featureGlossary\": \"From quick healthy snacks to full family meals, our free recipe book has more than 30 delicious ideas and easy-to-follow instructions from professional chefs. Our Philips Airfryer app is full of more tips, tutorials and easy-to follow recipes.\",\n" +
                "\"featureRank\": \"2\",\n" +
                "\"featureTopRank\": \"2\"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "],\n" +
                "\"featureHighlight\": [\n" +
                "{\n" +
                "\"featureCode\": \"F400093035\",\n" +
                "\"featureReferenceName\": \"Bake. Grill. Roast. Or reheat\",\n" +
                "\"featureHighlightRank\": \"1\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093038\",\n" +
                "\"featureReferenceName\": \"Inspiring recipes.\",\n" +
                "\"featureHighlightRank\": \"2\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093103\",\n" +
                "\"featureReferenceName\": \"XXL family size meals\",\n" +
                "\"featureHighlightRank\": \"3\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093026\",\n" +
                "\"featureReferenceName\": \"Fat Removal technology\",\n" +
                "\"featureHighlightRank\": \"4\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093027\",\n" +
                "\"featureReferenceName\": \"Rapid Air technology\",\n" +
                "\"featureHighlightRank\": \"5\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093028\",\n" +
                "\"featureReferenceName\": \"Fry with little or no oil\",\n" +
                "\"featureHighlightRank\": \"6\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093031\",\n" +
                "\"featureReferenceName\": \"1.5 times faster than an oven\",\n" +
                "\"featureHighlightRank\": \"7\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093033\",\n" +
                "\"featureReferenceName\": \"QuickClean & dishwasher-safe\",\n" +
                "\"featureHighlightRank\": \"8\"\n" +
                "},\n" +
                "{\n" +
                "\"featureCode\": \"F400093032\",\n" +
                "\"featureReferenceName\": \"Keep Warm mode\",\n" +
                "\"featureHighlightRank\": \"9\"\n" +
                "}\n" +
                "],\n" +
                "\"code\": [\n" +
                "{\n" +
                "\"code\": \"F400093026\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"699292\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093026-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093027\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"710376\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093027-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093028\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"871246\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093028-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093031\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"625054\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093031-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093032\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"713460\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093032-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093033\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"696774\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093033-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093035\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"777486\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093035-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093038\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"830368\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093038-FIL-global-001\"\n" +
                "},\n" +
                "{\n" +
                "\"code\": \"F400093103\",\n" +
                "\"description\": \"Feature photograph - highres 2100x1170\",\n" +
                "\"extension\": \"tif\",\n" +
                "\"extent\": \"662934\",\n" +
                "\"lastModified\": \"2019-12-31\",\n" +
                "\"locale\": \"global\",\n" +
                "\"number\": \"001\",\n" +
                "\"type\": \"FIL\",\n" +
                "\"asset\": \"https://images.philips.com/is/image/PhilipsConsumer/F400093103-FIL-global-001\"\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
        return str;
    }
}