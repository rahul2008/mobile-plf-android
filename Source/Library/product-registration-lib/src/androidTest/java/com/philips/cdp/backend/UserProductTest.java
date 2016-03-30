package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.handler.ErrorType;
import com.philips.cdp.handler.ProdRegConstants;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.model.ProdRegMetaDataResponse;
import com.philips.cdp.model.ProdRegRegisteredDataResponse;
import com.philips.cdp.model.ProdRegRegisteredResults;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.registration.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProductTest extends MockitoTestCase {

    UserProduct userProduct;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER);
        context = getInstrumentation().getContext();
    }

    public void testIsUserSignedIn() {
        User userMock = mock(User.class);
        when(userMock.isUserSignIn(context)).thenReturn(true);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        assertTrue(userProduct.isUserSignedIn(userMock, context));
        when(userMock.isUserSignIn(context)).thenReturn(false);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        assertFalse(userProduct.isUserSignedIn(userMock, context));
    }

    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(userProduct.isValidaDate("2016-03-22"));
        assertTrue(userProduct.isValidaDate(null));
    }

    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(userProduct.isValidaDate("1998-03-22"));
    }

    public void testRegisterProductWhenNotSignedIn() {
        UserProduct userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean isUserSignedIn(final User mUser, final Context context) {
                return false;
            }
        };
        Product productMock = mock(Product.class);

        userProduct.registerProduct(context, productMock, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {

            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.USER_NOT_SIGNED_IN, errorType);
            }
        });
        assertEquals(userProduct.getRequestType(), (ProdRegConstants.PRODUCT_REGISTRATION));
    }

    public void testRegisterProductWhenInValidDate() {
        UserProduct userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean isUserSignedIn(final User mUser, final Context context) {
                return true;
            }

            @Override
            protected boolean isValidaDate(final String date) {
                return false;
            }
        };
        Product productMock = mock(Product.class);
        userProduct.registerProduct(context, productMock, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_DATE, errorType);
            }
        });
    }

    public void testRegisterProductOnValidParameters() {
        final UserProduct userProductMock = mock(UserProduct.class);
        final ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        final UserProduct userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean isUserSignedIn(final User mUser, final Context context) {
                return true;
            }

            @Override
            protected boolean isValidaDate(final String date) {
                return true;
            }

            @NonNull
            @Override
            UserProduct getUserProduct(final Product product) {
                return userProductMock;
            }

            @NonNull
            @Override
            ProdRegListener getRegisteredProductsListener(final Context context, final Product product, final ProdRegListener listener) {
                return prodRegListenerMock;
            }
        };
        Product productMock = mock(Product.class);
        userProduct.registerProduct(context, productMock, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
            }
        });
        verify(userProductMock).getRegisteredProducts(context, prodRegListenerMock);
    }

    public void testGetRegisteredProductsListener() {
        Product product = mock(Product.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8967/09");
        ProdRegListener getRegisteredProductsListener = userProduct.
                getRegisteredProductsListener(context, product, listener);
        ProdRegRegisteredDataResponse responseMock = mock(ProdRegRegisteredDataResponse.class);
        final ProdRegRegisteredResults prodRegRegisteredResults = new ProdRegRegisteredResults();
        prodRegRegisteredResults.setProductModelNumber("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults1 = new ProdRegRegisteredResults();
        prodRegRegisteredResults1.setProductModelNumber("HD8968/09");
        final ProdRegRegisteredResults prodRegRegisteredResults2 = new ProdRegRegisteredResults();
        prodRegRegisteredResults2.setProductModelNumber("HD8969/09");
        ProdRegRegisteredResults[] results = {prodRegRegisteredResults, prodRegRegisteredResults1, prodRegRegisteredResults2};
        when(responseMock.getResults()).thenReturn(results);
        getRegisteredProductsListener.onProdRegSuccess(responseMock);
        verify(listener).onProdRegFailed(ErrorType.PRODUCT_ALREADY_REGISTERED);
    }

    public void testGetRegisteredProductsListenerOnCtnNotRegistered() {
        Product product = mock(Product.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        final ProdRegListener metadataListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8970/09");
        UserProduct userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            ProdRegListener getMetadataListener(final Product product, final ProdRegListener listener) {
                return metadataListener;
            }
        };
        ProdRegListener getRegisteredProductsListener = userProduct.
                getRegisteredProductsListener(context, product, listener);
        ProdRegRegisteredDataResponse responseMock = mock(ProdRegRegisteredDataResponse.class);
        final ProdRegRegisteredResults prodRegRegisteredResults = new ProdRegRegisteredResults();
        prodRegRegisteredResults.setProductModelNumber("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults1 = new ProdRegRegisteredResults();
        prodRegRegisteredResults1.setProductModelNumber("HD8968/09");
        final ProdRegRegisteredResults prodRegRegisteredResults2 = new ProdRegRegisteredResults();
        prodRegRegisteredResults2.setProductModelNumber("HD8969/09");
        ProdRegRegisteredResults[] results = {prodRegRegisteredResults, prodRegRegisteredResults1, prodRegRegisteredResults2};
        when(responseMock.getResults()).thenReturn(results);
        getRegisteredProductsListener.onProdRegSuccess(responseMock);
        verify(product).getProductMetadata(context, metadataListener);
    }

    public void testHandleErrorCases() {
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_CTN, errorType);
            }
        };
        userProduct.handleError(ErrorType.INVALID_CTN.getCode(), listener);
    }

    public void testReturnCorrectRequestType() {
        Product productMock = mock(Product.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        userProduct.registerProduct(context, productMock, prodRegListener);
        assertTrue(userProduct.getRequestType().equals(ProdRegConstants.PRODUCT_REGISTRATION));

        userProduct.getRegisteredProducts(context, prodRegListener);
        assertTrue(userProduct.getRequestType().equals(ProdRegConstants.FETCH_REGISTERED_PRODUCTS));
    }

    public void testGetProduct() {
        Product productMock = mock(Product.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        userProduct.registerProduct(context, productMock, prodRegListener);
        assertTrue(userProduct.getProduct().equals(productMock));
    }

    public void testValidatingSerialNumber() {
        ProdRegMetaDataResponse data = mock(ProdRegMetaDataResponse.class);
        Product productMock = mock(Product.class);
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
        userProduct.validateSerialNumberFromMetadata(data, productMock, listener);
        when(productMock.getSerialNumber()).thenReturn("1234");
        when(data.getSerialNumberFormat()).thenReturn("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        userProduct.validateSerialNumberFromMetadata(data, productMock, listener2);
        when(productMock.getSerialNumber()).thenReturn("1344");
        assertTrue(userProduct.validateSerialNumberFromMetadata(data, productMock, listener2));
    }

    public void testValidatingPurchaseDate() {
        ProdRegMetaDataResponse data = mock(ProdRegMetaDataResponse.class);
        Product productMock = mock(Product.class);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.MISSING_DATE, errorType);
            }
        };
        final boolean condition = userProduct.validatePurchaseDateFromMetadata(data, productMock, listener);
        assertFalse(condition);

        when(productMock.getPurchaseDate()).thenReturn("2016-03-22");
        when(data.getRequiresDateOfPurchase()).thenReturn("false");
        assertTrue(condition);
        verify(productMock, atLeastOnce()).setPurchaseDate(null);
    }

    public void testRegisteredTest() {
        UserProduct userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER) {
            @Override
            public String getLocale() {
                return "en_GB";
            }
        };
        RegisteredProductsRequest registeredProductsRequest = userProduct.getRegisteredProductsRequest(context);
        assertEquals(registeredProductsRequest.getCatalog(), Catalog.CONSUMER);
        assertEquals(registeredProductsRequest.getSector(), Sector.B2C);
        assertEquals(registeredProductsRequest.getLocale(), "en_GB");
    }

    public void testModelMapping() {
        UserProduct userProduct = new UserProduct(Sector.B2C, Catalog.CONSUMER);
        userProduct.setLocale("en_GB");
        assertEquals(userProduct.getSector(), Sector.B2C);
        assertEquals(userProduct.getCatalog(), Catalog.CONSUMER);
        assertEquals(userProduct.getLocale(), "en_GB");
    }

    public void testIsCtnRegistered() {
        Product product = mock(Product.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults = new ProdRegRegisteredResults();
        prodRegRegisteredResults.setProductModelNumber("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults1 = new ProdRegRegisteredResults();
        prodRegRegisteredResults1.setProductModelNumber("HD8968/09");
        final ProdRegRegisteredResults prodRegRegisteredResults2 = new ProdRegRegisteredResults();
        prodRegRegisteredResults2.setProductModelNumber("HD8969/09");
        ProdRegRegisteredResults[] results = {prodRegRegisteredResults, prodRegRegisteredResults1, prodRegRegisteredResults2};
        assertTrue(userProduct.isCtnRegistered(results, product, prodRegListener));
    }

    public void testIsCtnNotRegistered() {
        Product product = mock(Product.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8970/09");
        final ProdRegRegisteredResults prodRegRegisteredResults = new ProdRegRegisteredResults();
        prodRegRegisteredResults.setProductModelNumber("HD8967/09");
        final ProdRegRegisteredResults prodRegRegisteredResults1 = new ProdRegRegisteredResults();
        prodRegRegisteredResults1.setProductModelNumber("HD8968/09");
        final ProdRegRegisteredResults prodRegRegisteredResults2 = new ProdRegRegisteredResults();
        prodRegRegisteredResults2.setProductModelNumber("HD8969/09");
        ProdRegRegisteredResults[] results = {prodRegRegisteredResults, prodRegRegisteredResults1, prodRegRegisteredResults2};
        assertFalse(userProduct.isCtnRegistered(results, product, prodRegListener));
    }
}