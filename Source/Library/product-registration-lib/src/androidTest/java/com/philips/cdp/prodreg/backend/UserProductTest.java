package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.handler.MetadataListener;
import com.philips.cdp.prodreg.handler.ProdRegConstants;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.handler.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.RegisteredResponse;
import com.philips.cdp.prodreg.model.RegisteredResponseData;
import com.philips.cdp.prodreg.model.RegistrationState;
import com.philips.cdp.prodreg.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private LocalRegisteredProducts localRegisteredProducts;
    private UserProduct userProductMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        userProductMock = mock(UserProduct.class);
        localRegisteredProducts = mock(LocalRegisteredProducts.class);

        userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };


    }

    public void testIsUserSignedIn() {
        final User userMock = mock(User.class);
        when(userMock.isUserSignIn(context)).thenReturn(true);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            protected User getUser() {
                return userMock;
            }
        };
        assertTrue(userProduct.isUserSignedIn(context));
        when(userMock.isUserSignIn(context)).thenReturn(false);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        assertFalse(userProduct.isUserSignedIn(context));
    }

    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(userProduct.isValidaDate("2016-03-22"));
        assertTrue(userProduct.isValidaDate(null));
    }

    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(userProduct.isValidaDate("1998-03-22"));
    }

    public void testRegisterProductWhenNotSignedIn() {
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean isUserSignedIn(final Context context) {
                return false;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        Product productMock = mock(Product.class);

        userProduct.registerProduct(context, productMock, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {

            }

            @Override
            public void onProdRegFailed(final ProdRegError prodRegError) {
                assertEquals(ProdRegError.USER_NOT_SIGNED_IN, prodRegError);
            }
        });
        assertEquals(userProduct.getRequestType(), (ProdRegConstants.PRODUCT_REGISTRATION));
    }

    /*public void testRegisterProductWhenInValidDate() {
        Set<RegisteredProduct> registeredProductSet = new HashSet<>();
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(new RegisteredProduct(null,null,null,null,null));
        registeredProducts.add(new RegisteredProduct(null,null,null,null,null));
        registeredProductSet.add(new RegisteredProduct("ctn","1234",null,null,null));
        registeredProductSet.add(new RegisteredProduct("ctn1", "12345", null, null, null));
        when(localRegisteredProducts.getRegisteredProducts()).thenReturn(registeredProducts);
        when(localRegisteredProducts.getUniqueRegisteredProducts()).thenReturn(registeredProductSet);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean isUserSignedIn(final Context context) {
                return true;
            }

            @Override
            protected boolean isValidaDate(final String date) {
                return false;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        Product productMock = mock(Product.class);
        userProduct.registerProduct(context, productMock, new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ProdRegError prodRegError) {
                assertEquals(ProdRegError.INVALID_DATE, prodRegError);
            }
        });
    }*/

    public void testRegisterProductOnValidParameters() {
        final UserProduct userProductMock = mock(UserProduct.class);
        final RegisteredProductsListener prodRegListenerMock = mock(RegisteredProductsListener.class);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        Set<RegisteredProduct> registeredProductSet = new HashSet<>();
        registeredProducts.add(new RegisteredProduct(null, null, null, null, null));
        registeredProducts.add(new RegisteredProduct(null, null, null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn", "1234", null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn1", "12345", null, null, null));
        when(localRegisteredProducts.getRegisteredProducts()).thenReturn(registeredProducts);
        when(localRegisteredProducts.getUniqueRegisteredProducts()).thenReturn(registeredProductSet);
        final UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean isUserSignedIn(final Context context) {
                return true;
            }

            @Override
            protected boolean isValidaDate(final String date) {
                return true;
            }

            @Override
            protected boolean validateIsUserRegisteredLocally(final RegisteredProduct registeredProduct) {
                return false;
            }

            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            RegisteredProductsListener getRegisteredProductsListener(final RegisteredProduct product, final ProdRegListener appListener) {
                return prodRegListenerMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        final ProdRegListener appListener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ProdRegError prodRegError) {
            }
        };
        userProduct.registerProduct(context, new Product(null, null, null, null, null), appListener);
        verify(userProductMock).registerCachedProducts(registeredProducts, appListener);
    }

    public void testGetRegisteredProductsListener() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        when(product.getCtn()).thenReturn("HD8967/09");
        RegisteredProductsListener registeredProductsListener = userProduct.
                getRegisteredProductsListener(product, listener);
        RegisteredResponse responseMock = mock(RegisteredResponse.class);
        final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};
        when(responseMock.getResults()).thenReturn(results);
        registeredProductsListener.getRegisteredProducts(responseMock);
        verify(listener).onProdRegFailed(ProdRegError.PRODUCT_ALREADY_REGISTERED);
    }

    public void testGetRegisteredProductsListenerOnCtnNotRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        final UserProduct userProductMock = mock(UserProduct.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        final MetadataListener metadataListener = mock(MetadataListener.class);
        when(product.getCtn()).thenReturn("HD8970/09");
        final RegisteredProduct[] registeredProductsMock = {new RegisteredProduct(null, null, null, null, null), new RegisteredProduct(null, null, null, null, null)};
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            MetadataListener getMetadataListener(final RegisteredProduct product, final ProdRegListener appListener) {
                return metadataListener;
            }

            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @NonNull
            @Override
            protected Gson getGson() {
                return super.getGson();
            }

            @Override
            protected RegisteredProduct[] getRegisteredProductsFromResponse(final RegisteredResponseData[] results, final Gson gson) {
                return registeredProductsMock;
            }
        };
        RegisteredProductsListener registeredProductsListener = userProduct.
                getRegisteredProductsListener(product, listener);
        RegisteredResponse responseMock = mock(RegisteredResponse.class);
        final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};
        when(responseMock.getResults()).thenReturn(results);
        registeredProductsListener.getRegisteredProducts(responseMock);
        verify(localRegisteredProducts).syncLocalCache(registeredProductsMock);
        verify(product).getProductMetadata(context, metadataListener);
        registeredProductsListener.onErrorResponse(ProdRegError.METADATA_FAILED.getDescription(), ProdRegError.METADATA_FAILED.getCode());
        verify(userProductMock).handleError(product, ProdRegError.METADATA_FAILED.getCode(), listener);
    }

    public void testHandleErrorCases() {
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        userProduct.handleError(product, ProdRegError.INVALID_CTN.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.INVALID_CTN);
        verify(userProductMock).updateLocaleCacheOnError(product, ProdRegError.INVALID_CTN, RegistrationState.FAILED);
        userProduct.handleError(product, ProdRegError.INVALID_SERIALNUMBER.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.INVALID_SERIALNUMBER);
        userProduct.handleError(product, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.INVALID_VALIDATION);
        userProduct.handleError(product, ProdRegError.NO_INTERNET_AVAILABLE.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.NO_INTERNET_AVAILABLE);
        userProduct.handleError(product, ProdRegError.INTERNAL_SERVER_ERROR.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.INTERNAL_SERVER_ERROR);
        userProduct.handleError(product, ProdRegError.METADATA_FAILED.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.METADATA_FAILED);
        userProduct.handleError(product, 600, prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(ProdRegError.UNKNOWN);
        final UserProduct userProductMock = mock(UserProduct.class);

        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }
        };
        userProduct.handleError(product, ProdRegError.ACCESS_TOKEN_INVALID.getCode(), prodRegListenerMock);
        verify(userProductMock).onAccessTokenExpire(product, prodRegListenerMock);
    }

    public void testGettingRegisteredListener() {
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        userProduct.getRegisteredProducts(registeredProductsListener);
        assertEquals(registeredProductsListener, userProduct.getRegisteredProductsListener());
    }

    public void testReturnCorrectRequestType() {
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        userProduct.registerProduct(context, productMock, prodRegListener);
        assertTrue(userProduct.getRequestType().equals(ProdRegConstants.PRODUCT_REGISTRATION));

        userProduct.getRegisteredProducts(registeredProductsListener);
        assertTrue(userProduct.getRequestType().equals(ProdRegConstants.FETCH_REGISTERED_PRODUCTS));
    }

    public void testValidatingSerialNumber() {
        ProductMetadataResponseData data = mock(ProductMetadataResponseData.class);
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ProdRegError errorType) {
                assertEquals(ProdRegError.MISSING_SERIALNUMBER, errorType);
            }
        };

        final ProdRegListener listener2 = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ProdRegError errorType) {
                assertEquals(ProdRegError.INVALID_SERIALNUMBER, errorType);
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
        ProductMetadataResponseData data = mock(ProductMetadataResponseData.class);
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ProdRegError errorType) {
                assertEquals(ProdRegError.MISSING_DATE, errorType);
            }
        };
        assertFalse(userProduct.validatePurchaseDateFromMetadata(data, productMock, listener));

        when(productMock.getPurchaseDate()).thenReturn("2016-03-22");
        when(data.getRequiresDateOfPurchase()).thenReturn("false");
        assertTrue(userProduct.validatePurchaseDateFromMetadata(data, productMock, listener));
        verify(productMock, atLeastOnce()).setPurchaseDate(null);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        assertTrue(userProduct.validatePurchaseDateFromMetadata(data, productMock, listener));
    }

    public void testRegisteredTest() {
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @Override
            public String getLocale() {
                return "en_GB";
            }
        };
        RegisteredProductsRequest registeredProductsRequest = userProduct.getRegisteredProductsRequest();
        assertEquals(registeredProductsRequest.getCatalog(), Catalog.CONSUMER);
        assertEquals(registeredProductsRequest.getSector(), Sector.B2C);
        assertEquals(registeredProductsRequest.getLocale(), "en_GB");
    }

    public void testRegistrationRequestTest() {
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        final String ctn = "HC5410/83";
        when(productMock.getCtn()).thenReturn(ctn);
        final String serialNumber = "1344";
        when(productMock.getSerialNumber()).thenReturn(serialNumber);
        when(productMock.getSector()).thenReturn(Sector.B2C);
        when(productMock.getCatalog()).thenReturn(Catalog.CONSUMER);
        when(productMock.getSerialNumber()).thenReturn(serialNumber);
        when(productMock.getLocale()).thenReturn("en_GB");
        RegistrationRequest registrationRequest = userProduct.getRegistrationRequest(context, productMock);
        assertEquals(registrationRequest.getCatalog(), Catalog.CONSUMER);
        assertEquals(registrationRequest.getSector(), Sector.B2C);
        assertEquals(registrationRequest.getLocale(), "en_GB");
        assertEquals(registrationRequest.getCtn(), ctn);
        assertEquals(registrationRequest.getProductSerialNumber(), serialNumber);
    }

    public void testModelMapping() {
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER);
        userProduct.setLocale("en_GB");
        assertEquals(userProduct.getSector(), Sector.B2C);
        assertEquals(userProduct.getCatalog(), Catalog.CONSUMER);
        assertEquals(userProduct.getLocale(), "en_GB");
    }

    public void testIsCtnRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8967/09");
        final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};
        assertTrue(userProduct.isCtnRegistered(results, product, prodRegListener));
    }

    public void testIsCtnNotRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8970/09");
        final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};
        assertFalse(userProduct.isCtnRegistered(results, product, prodRegListener));
    }

    public void testGetPrxResponseListenerForRegisteredProducts() {
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        ResponseListener responseListener = userProduct.getPrxResponseListenerForRegisteredProducts(registeredProductsListener);
        RegisteredResponse registeredResponse = mock(RegisteredResponse.class);
        responseListener.onResponseSuccess(registeredResponse);
        verify(registeredProductsListener).getRegisteredProducts(registeredResponse);
        responseListener.onResponseError("test", 10);
        verify(registeredProductsListener).onErrorResponse("test", 10);
    }

    public void testGetPrxResponseListenerForRegisteringProducts() {
        final UserProduct userProductMock = mock(UserProduct.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        ResponseListener responseListener = userProduct.getPrxResponseListener(product, prodRegListener);
        ResponseData responseData = mock(ResponseData.class);
        responseListener.onResponseSuccess(responseData);
        verify(product).setRegistrationState(RegistrationState.REGISTERED);
        verify(localRegisteredProducts).updateRegisteredProducts(product);
        verify(prodRegListener).onProdRegSuccess(responseData);
        responseListener.onResponseError("test", 10);
        verify(userProductMock).handleError(product, 10, prodRegListener);
    }

    public void testInvokingAccessTokenWhenExpired() {
        final UserProduct userProductMock = mock(UserProduct.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        userProduct.handleError(product, 403, prodRegListener);
        verify(userProductMock).onAccessTokenExpire(product, prodRegListener);
    }

    public void testGetUserRefreshedLoginSession() {
        final UserProduct userProductMock = mock(UserProduct.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        final LocalRegisteredProducts localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProductsMock;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        RefreshLoginSessionHandler refreshLoginSessionHandler = userProduct.getRefreshLoginSessionHandler(product, prodRegListener, context);
        refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(50);
        verify(product).setRegistrationState(RegistrationState.FAILED);
        verify(product).setProdRegError(ProdRegError.ACCESS_TOKEN_INVALID);
        verify(localRegisteredProductsMock).updateRegisteredProducts(product);
        verify(prodRegListener).onProdRegFailed(ProdRegError.ACCESS_TOKEN_INVALID);
        refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
        verify(userProductMock).retryRequests(context, product, prodRegListener);
    }

    public void testGetMetadataListener() {
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        final UserProduct userProductMock = mock(UserProduct.class);
        final UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected boolean validatePurchaseDateFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct product, final ProdRegListener listener) {
                return true;
            }

            @Override
            protected boolean validateSerialNumberFromMetadata(final ProductMetadataResponseData data, final RegisteredProduct product, final ProdRegListener listener) {
                return true;
            }

            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }
        };
        MetadataListener metadataListener = userProduct.getMetadataListener(productMock, prodRegListenerMock);
        ProductMetadataResponse responseDataMock = mock(ProductMetadataResponse.class);
        metadataListener.onMetadataResponse(responseDataMock);
        verify(userProductMock).makeRegistrationRequest(context, productMock, prodRegListenerMock);
        metadataListener.onErrorResponse(ProdRegError.METADATA_FAILED.getDescription(), ProdRegError.METADATA_FAILED.getCode());
        verify(userProductMock).handleError(productMock, ProdRegError.METADATA_FAILED.getCode(), prodRegListenerMock);
    }

    public void testRegistrationRequest() {
        final RegistrationRequest registrationRequest = new RegistrationRequest(null, null, null);
        final RegisteredProduct productMock = mock(RegisteredProduct.class);
        final RequestManager requestManagerMock = mock(RequestManager.class);
        final ResponseListener responseListenerMock = mock(ResponseListener.class);
        final ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        final String locale = "en_GB";
        when(productMock.getLocale()).thenReturn(locale);
        final String purchase_Date = "2016-03-22";
        when(productMock.getPurchaseDate()).thenReturn(purchase_Date);
        final String serialNumber = "1344";
        when(productMock.getSerialNumber()).thenReturn(serialNumber);

        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {

            @NonNull
            @Override
            protected RegistrationRequest getRegistrationRequest(final Context context, final RegisteredProduct registeredProduct) {
                return registrationRequest;
            }

            @NonNull
            @Override
            protected RequestManager getRequestManager(final Context context) {
                return requestManagerMock;
            }

            @NonNull
            @Override
            ResponseListener getPrxResponseListener(final RegisteredProduct product, final ProdRegListener appListener) {
                return responseListenerMock;
            }
        };
        userProduct.makeRegistrationRequest(context, productMock, prodRegListenerMock);
        assertEquals(productMock.getPurchaseDate(), registrationRequest.getPurchaseDate());
        assertEquals(productMock.getLocale(), registrationRequest.getLocale());
        assertEquals(productMock.getSerialNumber(), registrationRequest.getProductSerialNumber());
        verify(requestManagerMock).executeRequest(registrationRequest, responseListenerMock);
    }

    public void testRetryMethod() {
        final UserProduct userProductMock = mock(UserProduct.class);
        final RegisteredProduct productMock = mock(RegisteredProduct.class);
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProductsListener registeredProductsListenerMock = mock(RegisteredProductsListener.class);
        UserProduct userProduct = new UserProduct(context, Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        userProduct.registerProduct(context, productMock, prodRegListenerMock);
        userProduct.retryRequests(context, productMock, prodRegListenerMock);
        verify(userProductMock).makeRegistrationRequest(context, productMock, prodRegListenerMock);
        userProduct.getRegisteredProducts(registeredProductsListenerMock);
        userProduct.retryRequests(context, productMock, prodRegListenerMock);
        verify(userProductMock).getRegisteredProducts(registeredProductsListenerMock);
    }
}
