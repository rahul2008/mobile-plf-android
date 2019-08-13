package com.philips.cdp.prodreg.model.metadata;

import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.PrxConstants;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class ProductMetadataResponseTest extends TestCase {
    ProductMetadataResponse productMetadataResponse;
    @Mock
    ProductMetadataResponseData data;
    @Mock
    RegistrationRequest mRegistrationRequest;
    private String TAG = getClass() + "";
    private String jsonData = "{\"success\": true,\t\"data\": {\"message\": \"If, after the purchase date, you register your product within three months, you are eligible for extended warranty. After product registration we can extend your warranty for another 3 years on top of the two years that you already have (5 years warranty in total). \\nMake sure the date of purchase and serialNumber number are correct. \\nTo find the serialNumber number, see the text next to the field for entering the serialNumber number. \\nPlease have your proof of purchase on hand in case you need to activate your warranty. That's why we provide you with product registration, the ability to upload the proof of purchase.\\nIf, after the purchase date, you register your product within three months, you are eligible for extended warranty. After product registration we can extend your warranty for another 3 years on top of the two years that you already have (5 years warranty in total). \\nMake sure the date of purchase and serialNumber number are correct. \\nTo find the serialNumber number, see the text nexto the field for entering the serialNumber number. \\nPlease have your proof of purchase on hand in case you need to activate your warranty. That's why we provide you with product registration, the ability to upload the proof of purchase.\\n\",\"isConnectDevice\": false,\"requiresSerialNumber\": true,\"extendedWarrantyMonths\": 60,\"hasGiftPack\": true,\"ctn\": \"HC5410/83\",\"hasExtendedWarranty\": true,\"serialNumberFormat\": \"^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$\",\"requiresDateOfPurchase\": true,\"serialNumberSampleContent\": {\"title\": \"Find the serialNumber number\",\"asset\": \"/consumerfiles/assets/img/registerproducts/HC.jpg\",\"snFormat\": \"Format: XXXX\",\"snExample\": \"Example: 1344\",\"description\": \"The serialNumber number is on the back of the unit. This number consists of four digits.\"}}}";
    String mCTN = "HD8967/01", mSerialNumber = "1344", mAccessToken = "abq21238xbshs";
    PrxConstants.Sector sector;
    PrxConstants.Catalog catalog;

    @Override
    public void setUp() throws Exception {
        productMetadataResponse = new ProductMetadataResponse();
        sector = PrxConstants.Sector.B2C;
        catalog = PrxConstants.Catalog.CONSUMER;
        mRegistrationRequest = new RegistrationRequest(mCTN, mSerialNumber,sector,catalog, true);
    }

    public void testSetData() throws Exception {
        productMetadataResponse.setData(data);
        assertEquals(data, productMetadataResponse.getData());
    }

    public void testGetData() throws Exception {
        productMetadataResponse.setData(data);
        assertEquals(data, productMetadataResponse.getData());
    }

    @Test
    public void testSummaryResponseObject() {
        try {
            ProductMetadataResponse productMetaData = (ProductMetadataResponse) mRegistrationRequest.getResponseData(new JSONObject(jsonData));
            ProductMetadataResponseData mResponseData = productMetaData.getData();
            assertNotNull(mResponseData);

            ProductMetadataResponseData productData = setMetadataObject(mResponseData);
            TestAssertionOnResponse(mResponseData, productData);
        } catch (Exception e) {
        }
    }

    @Test
    private void TestAssertionOnResponse(final ProductMetadataResponseData mResponseData, final ProductMetadataResponseData productData) {
        assertEquals(mResponseData.getMessage(), productData.getMessage());
        assertEquals(mResponseData.getIsConnectedDevice(), productData.getIsConnectedDevice());
        assertEquals(mResponseData.getRequiresDateOfPurchase(), productData.getRequiresDateOfPurchase());
        assertEquals(mResponseData.getExtendedWarrantyMonths(), productData.getExtendedWarrantyMonths());
        assertEquals(mResponseData.getHasGiftPack(), productData.getHasGiftPack());
        assertEquals(mResponseData.getSerialNumberFormat(), productData.getSerialNumberFormat());
        assertEquals(mResponseData.getRequiresDateOfPurchase(), productData.getRequiresDateOfPurchase());
        assertEquals(mResponseData.getSerialNumberSampleContent(), productData.getSerialNumberSampleContent());
    }

    @Test
    private ProductMetadataResponseData setMetadataObject(final ProductMetadataResponseData mResponseData) {
        ProductMetadataResponseData productData = new ProductMetadataResponseData();
        productData.setMessage(mResponseData.getMessage());
        productData.setIsConnectedDevice(mResponseData.getIsConnectedDevice());
        productData.setRequiresDateOfPurchase(mResponseData.getRequiresDateOfPurchase());
        productData.setExtendedWarrantyMonths(mResponseData.getExtendedWarrantyMonths());
        productData.setHasGiftPack(mResponseData.getHasGiftPack());
        productData.setSerialNumberFormat(mResponseData.getSerialNumberFormat());
        productData.setRequiresDateOfPurchase(mResponseData.getRequiresDateOfPurchase());
        productData.setSerialNumberSampleContent(mResponseData.getSerialNumberSampleContent());
        return productData;
    }
}