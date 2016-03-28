package com.philips.cdp.handler;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.error.ErrorType;
import com.philips.cdp.model.ProdRegMetaDataResponse;
import com.philips.cdp.prxclient.response.ResponseData;

import org.junit.Test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductTest extends MockitoTestCase {

    Product product;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = new Product();
    }

    @Test
    public void testProductMetadata() {

    }

    public void testHandleErrorCases() {
        product.handleError(ErrorType.INVALID_CTN.getCode(), new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {

            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_CTN, errorType);
            }
        });
    }

    public void testValidatingSerialNumber() {
        ProdRegMetaDataResponse data = mock(ProdRegMetaDataResponse.class);
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.MISSING_SERIALNUMBER, errorType);
            }
        };

        final ProdRegListener listener2 = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_SERIALNUMBER, errorType);
            }
        };
        when(data.getRequiresSerialNumber()).thenReturn("true");
        product.validateSerialNumberFromMetadata(data, prodRegRequestInfo, listener);
        when(prodRegRequestInfo.getSerialNumber()).thenReturn("1234");
        when(data.getSerialNumberFormat()).thenReturn("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        product.validateSerialNumberFromMetadata(data, prodRegRequestInfo, listener2);
        when(prodRegRequestInfo.getSerialNumber()).thenReturn("1344");
        assertTrue(product.validateSerialNumberFromMetadata(data, prodRegRequestInfo, listener2));
    }

    public void testValidatingPurchaseDate() {
        ProdRegMetaDataResponse data = mock(ProdRegMetaDataResponse.class);
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_DATE, errorType);
            }
        };
        assertFalse(product.validatePurchaseDateFromMetadata(data, prodRegRequestInfo, listener));

        when(prodRegRequestInfo.getPurchaseDate()).thenReturn("2016-03-22");
        when(data.getRequiresDateOfPurchase()).thenReturn("false");
        assertTrue(product.validatePurchaseDateFromMetadata(data, prodRegRequestInfo, listener));
        verify(prodRegRequestInfo, atLeastOnce()).setPurchaseDate(null);
    }
}