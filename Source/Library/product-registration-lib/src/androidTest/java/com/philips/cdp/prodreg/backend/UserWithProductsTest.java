package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.error.ProdRegError;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponse;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponseData;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponse;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponseData;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Rule;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserWithProductsTest extends MockitoTestCase {

    UserWithProducts userWithProducts;
    private Context context;
    private LocalRegisteredProducts localRegisteredProducts;
    private UserWithProducts userWithProductsMock;
    private ErrorHandler errorHandlerMock;

    @Rule

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        userWithProductsMock = mock(UserWithProducts.class);
        localRegisteredProducts = mock(LocalRegisteredProducts.class);
        errorHandlerMock = mock(ErrorHandler.class);
        userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @Override
            protected ErrorHandler getErrorHandler() {
                return errorHandlerMock;
            }
        };
    }

    public void testIsUserSignedIn() {
        final User userMock = mock(User.class);
        when(userMock.isUserSignIn(context)).thenReturn(true);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            protected User getUser() {
                return userMock;
            }
        };
        assertTrue(userWithProducts.isUserSignedIn(context));
        when(userMock.isUserSignIn(context)).thenReturn(false);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        assertFalse(userWithProducts.isUserSignedIn(context));
    }

    public void testSetUUID() {
        final User userMock = mock(User.class);
        when(userMock.isUserSignIn(context)).thenReturn(true);
        when(userMock.getEmailVerificationStatus(context)).thenReturn(true);
        final DIUserProfile userInstance = mock(DIUserProfile.class);
        when(userInstance.getJanrainUUID()).thenReturn("Janrain_id");
        when(userMock.getUserInstance(context)).thenReturn(userInstance);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            protected User getUser() {
                return userMock;
            }
        };
        userWithProducts.setUuid();
        assertEquals(userWithProducts.getUuid(), "Janrain_id");
    }

    public void testReturnTrueForValidDate() throws Exception {
        assertTrue(userWithProducts.getLocalRegisteredProductsInstance() instanceof LocalRegisteredProducts);
        assertTrue(userWithProducts.isValidDate("2016-03-22"));
    }

    public void testReturnFalseForInValidDate() throws Exception {
        assertFalse(userWithProducts.isValidDate("1998-03-22"));
    }

    public void testRegisterProductWhenNotSignedIn() {
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
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
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        userWithProducts.setProductRegistrationListener(prodRegListener);
        userWithProducts.registerProduct(productMock);
        assertEquals(userWithProducts.getRequestType(), (UserWithProducts.PRODUCT_REGISTRATION));
    }

    public void testRegisterProductOnValidParameters() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final RegisteredProductsListener prodRegListenerMock = mock(RegisteredProductsListener.class);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        Set<RegisteredProduct> registeredProductSet = new HashSet<>();
        registeredProducts.add(new RegisteredProduct(null, null, null, null, null));
        registeredProducts.add(new RegisteredProduct(null, null, null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn", "1234", null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn1", "12345", null, null, null));
        when(localRegisteredProducts.getRegisteredProducts()).thenReturn(registeredProducts);
        when(localRegisteredProducts.getUniqueRegisteredProducts()).thenReturn(registeredProductSet);
        final UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @Override
            protected boolean isUserSignedIn(final Context context) {
                return true;
            }

            @Override
            protected boolean isValidDate(final String date) {
                return true;
            }

            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
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
        final ProdRegListener appListener = mock(ProdRegListener.class);

        final Product product = new Product("ctn", "serial", "purchase_date", null, null);
        RegisteredProduct registeredProduct = mock(RegisteredProduct.class);
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        when(registeredProduct.IsUserRegisteredLocally(localRegisteredProducts)).thenReturn(true);
        userWithProducts.setProductRegistrationListener(appListener);
        userWithProducts.registerProduct(product);
        verify(userWithProductsMock).createDummyRegisteredProduct(product);
        verify(userWithProductsMock).registerCachedProducts(registeredProducts, appListener);
        verify(registeredProduct).IsUserRegisteredLocally(localRegisteredProducts);
        verify(appListener).onProdRegFailed(registeredProduct);
        testMapProductToRegisteredProduct(product);
        testThrowExceptionWhenListenerNull(userWithProducts, product);
    }

    private void testThrowExceptionWhenListenerNull(final UserWithProducts userWithProducts, final Product product) {
        try {
            userWithProducts.registerProduct(product);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
            assertTrue(e.getMessage().equals("Listener not Set"));
        }
    }

    public void testMapProductToRegisteredProduct(Product product) {
        RegisteredProduct registeredProduct = userWithProducts.createDummyRegisteredProduct(product);
        assertEquals(registeredProduct.getCtn(), product.getCtn());
        assertEquals(registeredProduct.getSerialNumber(), product.getSerialNumber());
        assertEquals(registeredProduct.getPurchaseDate(), product.getPurchaseDate());
        product = null;
        assertNull(userWithProducts.createDummyRegisteredProduct(product));
    }

    /*public void testGetRegisteredProductsListener() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        final Gson gson = new Gson();
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @NonNull
            @Override
            protected Gson getGson() {
                return gson;
            }

            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }
        };
        when(product.getCtn()).thenReturn("HD8967/09");
        when(product.getSerialNumber()).thenReturn("1234");
        RegisteredProductsListener registeredProductsListener = userWithProducts.
                getRegisteredProductsListener(product, listener);
        RegisteredResponse responseMock = mock(RegisteredResponse.class);
        final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        registeredResponseData.setProductSerialNumber("1234");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        registeredResponseData1.setProductSerialNumber("1234");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        registeredResponseData2.setProductSerialNumber("1234");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};
        when(responseMock.getResults()).thenReturn(results);

        RegisteredProduct[] registeredProducts = {new RegisteredProduct("ctn", "serial", null, null, null)};
        when(userWithProductsMock.getRegisteredProductsFromResponse(results, gson)).thenReturn(registeredProducts);
        registeredProductsListener.getRegisteredProducts(Arrays.asList(registeredProducts));
        verify(listener).onProdRegFailed(product);
        verify(userWithProductsMock).getRegisteredProductsFromResponse(results, gson);
        verify(localRegisteredProducts).syncLocalCache(registeredProducts);
    }*/

    public void testGettingRegisteredListener() {
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        userWithProducts.getRegisteredProducts(registeredProductsListener);
        assertEquals(registeredProductsListener, userWithProducts.getRegisteredProductsListener());
    }

    public void testReturnCorrectRequestType() {
        final Product product = new Product("ctn", "serial", null, null, null);
        final RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "serial", null, null, null);
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        userWithProducts.setProductRegistrationListener(prodRegListener);
        userWithProducts.registerProduct(product);
        assertTrue(userWithProducts.getRequestType() == UserWithProducts.PRODUCT_REGISTRATION);

        userWithProducts.getRegisteredProducts(registeredProductsListener);
        assertTrue(userWithProducts.getRequestType() == (UserWithProducts.FETCH_REGISTERED_PRODUCTS));
    }

    public void testValidatingSerialNumber() {
        ProductMetadataResponseData data = mock(ProductMetadataResponseData.class);
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        final ProdRegListener listener = mock(ProdRegListener.class);
        final ProdRegListener listener2 = mock(ProdRegListener.class);

        when(data.getRequiresSerialNumber()).thenReturn("true");
        userWithProducts.validateSerialNumberFromMetadata(data, productMock, listener);
        when(productMock.getSerialNumber()).thenReturn("1234");
        when(data.getSerialNumberFormat()).thenReturn("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        userWithProducts.validateSerialNumberFromMetadata(data, productMock, listener2);
        when(productMock.getSerialNumber()).thenReturn("1344");
        assertTrue(userWithProducts.validateSerialNumberFromMetadata(data, productMock, listener2));
    }

    public void testValidatingPurchaseDate() {
        ProductMetadataResponseData data = mock(ProductMetadataResponseData.class);
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        final ProdRegListener listener = mock(ProdRegListener.class);
        assertFalse(userWithProducts.validatePurchaseDateFromMetadata(data, productMock, listener));

        when(productMock.getPurchaseDate()).thenReturn("2016-03-22");
        when(data.getRequiresDateOfPurchase()).thenReturn("false");
        assertTrue(userWithProducts.validatePurchaseDateFromMetadata(data, productMock, listener));
        verify(productMock, atLeastOnce()).setPurchaseDate(null);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        assertTrue(userWithProducts.validatePurchaseDateFromMetadata(data, productMock, listener));
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
        RegistrationRequest registrationRequest = userWithProducts.getRegistrationRequest(context, productMock);
        assertEquals(registrationRequest.getCatalog(), Catalog.CONSUMER);
        assertEquals(registrationRequest.getSector(), Sector.B2C);
        assertEquals(registrationRequest.getLocale(), "en_GB");
        assertEquals(registrationRequest.getCtn(), ctn);
        assertEquals(registrationRequest.getProductSerialNumber(), serialNumber);
    }

    public void testModelMapping() {
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context));
        userWithProducts.setLocale("en_GB");
        assertEquals(userWithProducts.getLocale(), "en_GB");
    }

    /*public void testIsCtnRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8967/09");
        when(product.getSerialNumber()).thenReturn("1234");
        *//*final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        registeredResponseData.setProductSerialNumber("1234");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        registeredResponseData1.setProductSerialNumber("1234");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        registeredResponseData2.setProductSerialNumber("1234");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};*//*
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(new RegisteredProduct("HD8970/09","1234",null,null,null));
        registeredProducts.add(new RegisteredProduct("HD8968/09","1234",null,null,null));
        registeredProducts.add(new RegisteredProduct("HD8967/09", "1234", null, null, null));
        registeredProducts.add(new RegisteredProduct("HD8969/09", "1234", null, null, null));
        assertTrue(userWithProducts.isCtnRegistered(registeredProducts, product, prodRegListener));
    }
*/
    public void testIsCtnNotRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        when(product.getCtn()).thenReturn("HD8965/09");
        when(product.getSerialNumber()).thenReturn("1234");
        /*final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        registeredResponseData.setProductSerialNumber("1234");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};*/
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(new RegisteredProduct("HD8970/09", "1234", null, null, null));
        registeredProducts.add(new RegisteredProduct("HD8968/09", "1234", null, null, null));
        registeredProducts.add(new RegisteredProduct("HD8967/09", "1234", null, null, null));
        registeredProducts.add(new RegisteredProduct("HD8969/09", "1234", null, null, null));
        assertFalse(userWithProducts.isCtnRegistered(registeredProducts, product, prodRegListener));
    }

    public void testGetPrxResponseListenerForRegisteredProducts() {
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        ResponseListener responseListener = userWithProducts.getPrxResponseListenerForRegisteredProducts(registeredProductsListener);
        RegisteredResponse registeredResponse = mock(RegisteredResponse.class);
        final RegisteredResponseData registeredResponseData = new RegisteredResponseData();
        registeredResponseData.setProductModelNumber("HD8967/09");
        registeredResponseData.setProductSerialNumber("1234");
        final RegisteredResponseData registeredResponseData1 = new RegisteredResponseData();
        registeredResponseData1.setProductModelNumber("HD8968/09");
        final RegisteredResponseData registeredResponseData2 = new RegisteredResponseData();
        registeredResponseData2.setProductModelNumber("HD8969/09");
        RegisteredResponseData[] results = {registeredResponseData, registeredResponseData1, registeredResponseData2};
        when(registeredResponse.getResults()).thenReturn(results);
        responseListener.onResponseSuccess(registeredResponse);
//        verify(registeredProductsListener).getRegisteredProducts(registeredResponse);
        responseListener.onResponseError("test", 10);
    }

    public void testGetPrxResponseListenerForRegisteringProducts() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @Override
            protected ErrorHandler getErrorHandler() {
                return errorHandlerMock;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        ResponseListener responseListener = userWithProducts.getPrxResponseListener(product, prodRegListener);
        RegistrationResponse responseData = mock(RegistrationResponse.class);
        final RegistrationResponseData data = mock(RegistrationResponseData.class);
        when(responseData.getData()).thenReturn(data);

        when(data.getWarrantyEndDate()).thenReturn("2016-03-22");
        responseListener.onResponseSuccess(responseData);
        verify(product).setRegistrationState(RegistrationState.REGISTERED);
        verify(localRegisteredProducts).updateRegisteredProducts(product);
        verify(prodRegListener).onProdRegSuccess(product);
        verify(userWithProductsMock).mapRegistrationResponse(responseData, product);
        responseListener.onResponseError("test", 10);
        verify(errorHandlerMock).handleError(product, 10, prodRegListener);
    }

    public void testMapRegistrationResponse() {
        RegistrationResponse responseData = mock(RegistrationResponse.class);
        final RegistrationResponseData data = mock(RegistrationResponseData.class);
        when(responseData.getData()).thenReturn(data);
        when(data.getWarrantyEndDate()).thenReturn("2016-03-22");
        final RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "serial", null, null, null);
        userWithProducts.mapRegistrationResponse(responseData, registeredProduct);
        assertEquals(registeredProduct.getEndWarrantyDate(), data.getWarrantyEndDate());
    }

    public void testInvokingAccessTokenWhenExpired() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        RegisteredProduct product = new RegisteredProduct("ctn", "serial", null, null, null);
        final User userMock = mock(User.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            protected User getUser() {
                return userMock;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        errorHandlerMock.handleError(product, 403, prodRegListener);
        RefreshLoginSessionHandler refreshLoginSessionHandler = mock(RefreshLoginSessionHandler.class);
        when(userWithProductsMock.getRefreshLoginSessionHandler(product, prodRegListener, context)).thenReturn(refreshLoginSessionHandler);
        userWithProducts.onAccessTokenExpire(product, prodRegListener);
        verify(userMock).refreshLoginSession(refreshLoginSessionHandler, context);
    }

    public void testGetUserRefreshedLoginSession() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        final LocalRegisteredProducts localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProductsMock;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        RefreshLoginSessionHandler refreshLoginSessionHandler = userWithProducts.getRefreshLoginSessionHandler(product, prodRegListener, context);
        refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(50);
        verify(userWithProductsMock).updateLocaleCacheOnError(product, ProdRegError.ACCESS_TOKEN_INVALID, RegistrationState.FAILED);
        verify(localRegisteredProductsMock).updateRegisteredProducts(product);
        verify(prodRegListener).onProdRegFailed(product);
        refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
        verify(userWithProductsMock).retryRequests(context, product, prodRegListener);
    }

    public void testGetMetadataListener() {
        RegisteredProduct productMock = new RegisteredProduct("ctn", "serial", null, null, null);
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
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
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @Override
            protected ErrorHandler getErrorHandler() {
                return errorHandlerMock;
            }
        };
        MetadataListener metadataListener = userWithProducts.getMetadataListener(productMock, prodRegListenerMock);
        ProductMetadataResponse responseDataMock = mock(ProductMetadataResponse.class);
        metadataListener.onMetadataResponse(responseDataMock);
        verify(userWithProductsMock).makeRegistrationRequest(context, productMock, prodRegListenerMock);
        metadataListener.onErrorResponse(ProdRegError.METADATA_FAILED.getDescription(), ProdRegError.METADATA_FAILED.getCode());
        verify(errorHandlerMock).handleError(productMock, ProdRegError.METADATA_FAILED.getCode(), prodRegListenerMock);
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

        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {

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
        userWithProducts.makeRegistrationRequest(context, productMock, prodRegListenerMock);
        assertEquals(productMock.getPurchaseDate(), registrationRequest.getPurchaseDate());
        assertEquals(productMock.getLocale(), registrationRequest.getLocale());
        assertEquals(productMock.getSerialNumber(), registrationRequest.getProductSerialNumber());
        verify(requestManagerMock).executeRequest(registrationRequest, responseListenerMock);
    }

    public void testRetryMethod() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final Product product = new Product("ctn", "serial", null, null, null);
        final RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "serial", null, null, null);
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProductsListener registeredProductsListenerMock = mock(RegisteredProductsListener.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        userWithProducts.setProductRegistrationListener(prodRegListenerMock);
        userWithProducts.registerProduct(product);
        userWithProducts.retryRequests(context, registeredProduct, prodRegListenerMock);
        verify(userWithProductsMock).makeRegistrationRequest(context, registeredProduct, prodRegListenerMock);
        userWithProducts.getRegisteredProducts(registeredProductsListenerMock);
        userWithProducts.retryRequests(context, registeredProduct, prodRegListenerMock);
        verify(userWithProductsMock).getRegisteredProducts(registeredProductsListenerMock);
        userWithProducts.setRequestType(-1);
        userWithProducts.retryRequests(context, registeredProduct, prodRegListenerMock);
        verify(prodRegListenerMock, never()).onProdRegFailed(registeredProduct);
    }

    /*public void testValidateIsUserRegisteredLocally() {
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "serial", "date", null, null);
        registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
        RegisteredProduct registeredProduct1 = new RegisteredProduct("ctn1", "serial1", "date1", null, null);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        when(localRegisteredProducts.getRegisteredProducts()).thenReturn(registeredProducts);
        assertTrue(userWithProducts.IsUserRegisteredLocally(registeredProduct));
        registeredProduct.setRegistrationState(RegistrationState.PENDING);
        assertFalse(userWithProducts.IsUserRegisteredLocally(registeredProduct));
        registeredProducts.remove(registeredProduct);
        assertFalse(userWithProducts.IsUserRegisteredLocally(registeredProduct));
    }*/

    public void testUpdateLocaleCacheOnError() {
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "serial", null, null, null);
        userWithProducts.updateLocaleCacheOnError(registeredProduct, ProdRegError.PRODUCT_ALREADY_REGISTERED, RegistrationState.FAILED);
        assertEquals(registeredProduct.getRegistrationState(), RegistrationState.FAILED);
        assertEquals(registeredProduct.getProdRegError(), ProdRegError.PRODUCT_ALREADY_REGISTERED);
        verify(localRegisteredProducts).updateRegisteredProducts(registeredProduct);
    }

    public void testCachedRegisterProducts() {
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "Serial", "2016-03-22", null, null);
        registeredProduct.setRegistrationState(RegistrationState.PENDING);
        RegisteredProduct registeredProduct1 = new RegisteredProduct("ctn1", "Serial1", "2016-04-22", null, null);
        registeredProduct1.setRegistrationState(RegistrationState.FAILED);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        when(userWithProductsMock.isUserSignedIn(context)).thenReturn(false);
        userWithProducts.registerCachedProducts(registeredProducts, prodRegListener);

        verify(userWithProductsMock).updateLocaleCacheOnError(registeredProduct1, ProdRegError.USER_NOT_SIGNED_IN, RegistrationState.FAILED);
        verify(prodRegListener, Mockito.atLeastOnce()).onProdRegFailed(registeredProduct);
        when(userWithProductsMock.isUserSignedIn(context)).thenReturn(true);
        when(userWithProductsMock.isValidDate("2016-2-12")).thenReturn(false);
        userWithProducts.registerCachedProducts(registeredProducts, prodRegListener);
        verify(userWithProductsMock).updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_DATE, RegistrationState.FAILED);
        verify(prodRegListener, Mockito.atLeastOnce()).onProdRegFailed(registeredProduct);
    }

    /*public void testGetRegisteredProductsListenerOnCtnNotRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final ErrorHandler errorHandlerMock = mock(ErrorHandler.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        ProdRegListener listener = mock(ProdRegListener.class);
        final MetadataListener metadataListener = mock(MetadataListener.class);
        when(product.getCtn()).thenReturn("HD8970/09");
        final Gson gson = new Gson();
        final RegisteredProduct[] registeredProductsMock = {new RegisteredProduct(null, null, null, null, null), new RegisteredProduct(null, null, null, null, null)};
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context)) {
            @NonNull
            @Override
            MetadataListener getMetadataListener(final RegisteredProduct product, final ProdRegListener appListener) {
                return metadataListener;
            }

            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @NonNull
            @Override
            protected Gson getGson() {
                return gson;
            }

            @Override
            protected ErrorHandler getErrorHandler() {
                return errorHandlerMock;
            }
        };
        RegisteredProductsListener registeredProductsListener = userWithProducts.
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
        when(userWithProductsMock.getRegisteredProductsFromResponse(results, gson)).thenReturn(registeredProductsMock);
//        registeredProductsListener.getRegisteredProducts(responseMock);
        verify(localRegisteredProducts).syncLocalCache(registeredProductsMock);
        verify(product).getProductMetadata(context, metadataListener);
//        registeredProductsListener.onErrorResponse(ProdRegError.METADATA_FAILED.getDescription(), ProdRegError.METADATA_FAILED.getCode());
        verify(errorHandlerMock).handleError(product, ProdRegError.METADATA_FAILED.getCode(), listener);
    }*/
}
