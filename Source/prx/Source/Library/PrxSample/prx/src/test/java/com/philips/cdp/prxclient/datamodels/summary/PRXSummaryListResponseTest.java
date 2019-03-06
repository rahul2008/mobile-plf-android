package com.philips.cdp.prxclient.datamodels.summary;


import com.philips.cdp.prxclient.request.ProductSummaryListRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by philips on 3/6/19.
 */
public class PRXSummaryListResponseTest {


    private static final String TAG = PRXSummaryListResponseTest.class.getSimpleName();
    private PrxRequest mProductSummaryListBuilder = null;
    private ReviewStatistics mReviewStatistics = null;
    private Price mPrice = null;
    private Brand mBrand = null;

    ArrayList<String> ctns ;

    @Before
    public void setUp() throws Exception {
        ctns = new ArrayList<>();
        ctns.add("HX8331/01");
        ctns.add("HX8332/02");
        mProductSummaryListBuilder = new ProductSummaryListRequest( ctns, null, null, null);
    }

    @Test
    public void testProductSummaryListResponseObject() {
        try {
            JSONObject mJsonObject = new JSONObject(getPRXSummaryListResponse());
            assertNotNull(mJsonObject);
            ResponseData mResponseData = mProductSummaryListBuilder.getResponseData(mJsonObject);

            PRXSummaryListResponse prxSummaryListResponse = (PRXSummaryListResponse) mResponseData;
            assertNotNull(prxSummaryListResponse);
            
            testDataNotNull(prxSummaryListResponse.getData());
        } catch (JSONException e) {
            fail();

        } catch (Exception e) {
            fail();
        }
    }

    private void testDataNotNull(ArrayList<Data> datas) {
        
        for(Data data:datas){
            
            assertNotNull(data.getBrandName());
            assertNotNull(data.getAlphanumeric());
            assertNotNull(data.getBrand());
            assertNotNull(data.getCareSop());
            assertNotNull(data.getCtn());
            assertNotNull(data.getDescriptor());
            assertNotNull(data.getDomain());
            assertNotNull(data.getDtn());
            assertNotNull(data.getEop());
            assertNotNull(data.getFilterKeys());
            assertNotNull(data.getImageURL());
            assertNotNull(data.getKeyAwards());
            assertNotNull(data.getLeafletUrl());
            assertNotNull(data.getLocale());
            assertNotNull(data.getPriority());
            assertNotNull(data.getProductPagePath());
            assertNotNull(data.getProductStatus());
            assertNotNull(data.getProductTitle());
            assertNotNull(data.getProductURL());
            assertNotNull(data.getSomp());
            assertNotNull(data.getSop());
            assertNotNull(data.getSubcategory());
            assertNotNull(data.getWow());
            assertFalse(data.isIsDeleted());
        }
    }

    /*
    * This is sample response for PRXSummaryListResponse request:
    * Below url is used to fetch sample response data and can be verified in browser
    * https://www.philips.co.uk/prx/product/B2C/en_GB/CONSUMER/listproducts?ctnlist=HD5061_01,HD7870_10
    * */
    private String getPRXSummaryListResponse() {
        String str = "{\n" +
                "\"success\": true,\n" +
                "\"data\": [\n" +
                "{\n" +
                "\"locale\": \"en_GB\",\n" +
                "\"ctn\": \"HD5061/01\",\n" +
                "\"dtn\": \"HD5061\",\n" +
                "\"leafletUrl\": \"https://www.download.p4c.philips.com/files/h/hd5061_01/hd5061_01_pss_enggb.pdf\",\n" +
                "\"productTitle\": \"Brew group grease\",\n" +
                "\"alphanumeric\": \"HD5061\",\n" +
                "\"brandName\": \"Philips\",\n" +
                "\"brand\": {\n" +
                "\"brandLogo\": \"https://images.philips.com/is/image/PhilipsConsumer/PHI-BRP-global-001\"\n" +
                "},\n" +
                "\"productURL\": \"/c-p/HD5061_01/brew-group-grease\",\n" +
                "\"productPagePath\": \"/content/B2C/en_GB/product-catalog/HD5061_01\",\n" +
                "\"subcategoryName\": \"Saeco accessories and parts\",\n" +
                "\"categoryPath\": \"/content/B2C/en_GB/marketing-catalog/ac/coffee-appliances-accessories-and-parts/saeco-accessories-and-parts\",\n" +
                "\"descriptor\": \"Brew group grease\",\n" +
                "\"domain\": \"https://www.philips.co.uk\",\n" +
                "\"versions\": [\n" +
                "\"for Saeco Espresso machines\",\n" +
                "\"for Philips Espresso machines\",\n" +
                "\"for Gaggia Espresso machines\"\n" +
                "],\n" +
                "\"productStatus\": \"SUPPORT\",\n" +
                "\"imageURL\": \"https://images.philips.com/is/image/PhilipsConsumer/HD5061_01-IMS-en_GB\",\n" +
                "\"sop\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"somp\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"eop\": \"2016-08-14T00:00:00.000+02:00\",\n" +
                "\"isDeleted\": false,\n" +
                "\"priority\": 2572,\n" +
                "\"wow\": \"For maintaining your espresso machine\",\n" +
                "\"catalogs\": [\n" +
                "{\n" +
                "\"catalogId\": \"SHOPEMP\",\n" +
                "\"status\": \"NORMAL\",\n" +
                "\"sop\": \"2000-01-01T00:00:00.000+01:00\",\n" +
                "\"somp\": \"2000-01-01T00:00:00.000+01:00\",\n" +
                "\"eop\": \"2100-01-01T00:00:00.000+01:00\",\n" +
                "\"priority\": 0,\n" +
                "\"visibility\": \"true\",\n" +
                "\"clearance\": \"false\",\n" +
                "\"isDeleted\": false\n" +
                "},\n" +
                "{\n" +
                "\"catalogId\": \"SHOPPUB\",\n" +
                "\"status\": \"NORMAL\",\n" +
                "\"sop\": \"2000-01-01T00:00:00.000+01:00\",\n" +
                "\"somp\": \"2000-01-01T00:00:00.000+01:00\",\n" +
                "\"eop\": \"2100-01-01T00:00:00.000+01:00\",\n" +
                "\"priority\": 0,\n" +
                "\"visibility\": \"true\",\n" +
                "\"clearance\": \"false\",\n" +
                "\"isDeleted\": false\n" +
                "},\n" +
                "{\n" +
                "\"catalogId\": \"CONSUMER\",\n" +
                "\"status\": \"SUPPORT\",\n" +
                "\"sop\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"somp\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"eop\": \"2016-08-14T00:00:00.000+02:00\",\n" +
                "\"priority\": 2572,\n" +
                "\"isDeleted\": false\n" +
                "},\n" +
                "{\n" +
                "\"catalogId\": \"CARE\",\n" +
                "\"status\": \"NORMAL\",\n" +
                "\"sop\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"somp\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"eop\": \"2110-01-01T00:00:00.000+01:00\",\n" +
                "\"priority\": 0,\n" +
                "\"isDeleted\": false\n" +
                "}\n" +
                "],\n" +
                "\"productType\": \"CRP\",\n" +
                "\"careSop\": \"2011-11-01T00:00:00.000+01:00\",\n" +
                "\"filterKeys\": [\n" +
                "\"ACCESSORIES_GR\",\n" +
                "\"COFFEEMAKERS_AND_KETTLES_CA\",\n" +
                "\"COFFEE_APPLIANCES_ACC_CA\",\n" +
                "\"FG_COFFEE_ACC_SAECO_MAIN_KLUBER\",\n" +
                "\"FK_CLEA_HOU\",\n" +
                "\"FK_CLEA_HOUSEHOLD\",\n" +
                "\"FK_COFFEE_ACC_SAECO_REP_DUMP_BOX\",\n" +
                "\"FK_COFFEE_ACC_SUB_TYPE_MAINTAIN\",\n" +
                "\"FK_COFFEE_ACC_SUB_TYPE_REP_PARTS\",\n" +
                "\"FK_COFFEE_MAINTENANCE\",\n" +
                "\"FK_COFFEE_REPLACEMENT_PARTS\",\n" +
                "\"FK_ESPRESSOMACHINES\",\n" +
                "\"FK_ESPRESSOMACHINES\",\n" +
                "\"FK_ESPRESSO_ACCESSORIES_SHOP\",\n" +
                "\"FK_REF_HOU\",\n" +
                "\"FK_REF_HOUSEHOLD\",\n" +
                "\"HOUSEHOLD_PRODUCTS_GR\",\n" +
                "\"SAECO_ACCESSORIES_SU\",\n" +
                "\"SAECO_ACCESSORIES_SU3\"\n" +
                "],\n" +
                "\"subcategory\": \"SAECO_ACCESSORIES_SU\",\n" +
                "\"gtin\": \"08710103549505\",\n" +
                "\"accessory\": true\n" +
                "},\n" +
                "{\n" +
                "\"locale\": \"en_GB\",\n" +
                "\"ctn\": \"HD7870/10\",\n" +
                "\"dtn\": \"HD7870/10\",\n" +
                "\"leafletUrl\": \"https://www.download.p4c.philips.com/files/h/hd7870_10/hd7870_10_pss_enggb.pdf\",\n" +
                "\"productTitle\": \"SENSEO® Twist Coffee pod machine\",\n" +
                "\"alphanumeric\": \"HD7870/10\",\n" +
                "\"brandName\": \"SENSEO®\",\n" +
                "\"brand\": {\n" +
                "\"partnerLogo\": \"https://images.philips.com/is/image/PhilipsConsumer/SEN-BRP-global-001\"\n" +
                "},\n" +
                "\"familyName\": \"Twist\",\n" +
                "\"productURL\": \"/c-p/HD7870_10/twist-coffee-pod-machine\",\n" +
                "\"productPagePath\": \"/content/B2C/en_GB/product-catalog/HD7870_10\",\n" +
                "\"subcategoryName\": \"SENSEO® coffee machines\",\n" +
                "\"categoryPath\": \"/content/B2C/en_GB/marketing-catalog/ho/coffee/senseo-coffee-machines\",\n" +
                "\"descriptor\": \"Coffee pod machine\",\n" +
                "\"domain\": \"https://www.philips.co.uk\",\n" +
                "\"versions\": [\n" +
                "\"Adjustable tray height\",\n" +
                "\"Lime Yellow and White\",\n" +
                "\"Strength select function\"\n" +
                "],\n" +
                "\"productStatus\": \"SUPPORT\",\n" +
                "\"imageURL\": \"https://images.philips.com/is/image/PhilipsConsumer/HD7870_10-IMS-en_GB\",\n" +
                "\"sop\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"somp\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"eop\": \"2015-02-05T00:00:00.000+01:00\",\n" +
                "\"isDeleted\": false,\n" +
                "\"priority\": 442818,\n" +
                "\"reviewStatistics\": {\n" +
                "\"averageOverallRating\": 5,\n" +
                "\"totalReviewCount\": 8\n" +
                "},\n" +
                "\"wow\": \"Brew your SENSEO® coffee the way you like it\",\n" +
                "\"catalogs\": [\n" +
                "{\n" +
                "\"catalogId\": \"CONSUMER\",\n" +
                "\"status\": \"SUPPORT\",\n" +
                "\"sop\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"somp\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"eop\": \"2015-02-05T00:00:00.000+01:00\",\n" +
                "\"priority\": 442818,\n" +
                "\"isDeleted\": false\n" +
                "},\n" +
                "{\n" +
                "\"catalogId\": \"CARE\",\n" +
                "\"status\": \"NORMAL\",\n" +
                "\"sop\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"somp\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"eop\": \"2034-07-15T00:00:00.000+02:00\",\n" +
                "\"priority\": 0,\n" +
                "\"isDeleted\": false\n" +
                "}\n" +
                "],\n" +
                "\"variations\": {\n" +
                "\"name\": \"Color\",\n" +
                "\"clusterCode\": \"VAR400000181\"\n" +
                "},\n" +
                "\"careSop\": \"2012-09-01T00:00:00.000+02:00\",\n" +
                "\"filterKeys\": [\n" +
                "\"COFFEEMAKERS_AND_KETTLES_CA\",\n" +
                "\"FK_CLEA_HOU\",\n" +
                "\"FK_CLEA_HOUSEHOLD\",\n" +
                "\"FK_COFFEE_AUTO_SHUT_OFF\",\n" +
                "\"FK_COFFEE_CALC_INDICATOR\",\n" +
                "\"FK_COFFEE_DIFFERENT_CUP_SIZES\",\n" +
                "\"FK_COFFEE_DISHWASHER_PROOF_PARTS\",\n" +
                "\"FK_COFFEE_FILTER_COFFEE\",\n" +
                "\"FK_COFFEE_PLASTIC\",\n" +
                "\"FK_COFFEE_SENSEO_TWIST\",\n" +
                "\"FK_COFFEE_STRENGTH_SELECT\",\n" +
                "\"FK_GENERIC_GREEN\",\n" +
                "\"FK_REF_HOU\",\n" +
                "\"FK_REF_HOUSEHOLD\",\n" +
                "\"HOUSEHOLD_PRODUCTS_GR\",\n" +
                "\"SENSEO_SU\"\n" +
                "],\n" +
                "\"subcategory\": \"SENSEO_SU\",\n" +
                "\"gtin\": \"08710103602170\",\n" +
                "\"accessory\": false\n" +
                "}\n" +
                "]\n" +
                "}";
        return str;
    }

}