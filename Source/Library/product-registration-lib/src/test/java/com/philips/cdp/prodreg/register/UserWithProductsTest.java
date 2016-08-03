package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponse;
import com.philips.cdp.prodreg.model.registerproduct.RegistrationResponseData;
import com.philips.cdp.prodreg.prxrequest.RegistrationRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import junit.framework.TestCase;

import org.junit.Rule;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class UserWithProductsTest extends TestCase {

    UserWithProducts userWithProducts;
    private Context context;
    private LocalRegisteredProducts localRegisteredProducts;
    private UserWithProducts userWithProductsMock;
    private ErrorHandler errorHandlerMock;
    private ProdRegListener prodRegListener;
    private User userMock;

    @Rule

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = mock(Context.class);
        userWithProductsMock = mock(UserWithProducts.class);
        userMock = mock(User.class);
        localRegisteredProducts = mock(LocalRegisteredProducts.class);
        prodRegListener = mock(ProdRegListener.class);
        errorHandlerMock = mock(ErrorHandler.class);
        when(userMock.isUserSignIn()).thenReturn(true);
        userWithProducts = new UserWithProducts(context, new User(context), prodRegListener) {
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

            @NonNull
            @Override
            protected User getUser() {
                return userMock;
            }
        };
    }

    public void testIsUserSignedIn() {
        final User userMock = mock(User.class);
        when(userMock.isUserSignIn()).thenReturn(true);
        when(userMock.getEmailVerificationStatus()).thenReturn(true);
        UserWithProducts userWithProducts = new UserWithProducts(context, userMock, prodRegListener);
        assertTrue(userWithProducts.isUserSignedIn(context));
        when(userMock.isUserSignIn()).thenReturn(false);
        when(userMock.getEmailVerificationStatus()).thenReturn(true);
        assertFalse(userWithProducts.isUserSignedIn(context));
    }

    public void testSetUUID() {
        final User userMock = mock(User.class);
        when(userMock.isUserSignIn()).thenReturn(true);
        when(userMock.getEmailVerificationStatus()).thenReturn(true);
        when(userMock.getJanrainUUID()).thenReturn("Janrain_id");
        UserWithProducts userWithProducts = new UserWithProducts(context, userMock, prodRegListener) {
            @NonNull
            @Override
            protected User getUser() {
                return userMock;
            }
        };
        userWithProducts.setUuid();
        assertEquals(userWithProducts.getUuid(), "Janrain_id");
    }

    public void testRegisterProductWhenNotSignedIn() {
        UserWithProducts userWithProducts = new UserWithProducts(context, userMock, prodRegListener) {
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
        Product productMock = new Product("", Sector.B2C, Catalog.CONSUMER);
        userWithProducts.registerProduct(productMock);
        assertEquals(userWithProducts.getRequestType(), (UserWithProducts.PRODUCT_REGISTRATION));
    }

    public void testRegisterProductOnUserAlreadyRegistered() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final RegisteredProductsListener prodRegListenerMock = mock(RegisteredProductsListener.class);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        Set<RegisteredProduct> registeredProductSet = new HashSet<>();
        registeredProducts.add(new RegisteredProduct(null, null, null));
        registeredProducts.add(new RegisteredProduct(null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn", null, null));
        registeredProductSet.add(new RegisteredProduct("ctn1", null, null));
        when(localRegisteredProducts.getRegisteredProducts()).thenReturn(registeredProducts);
        when(localRegisteredProducts.getUniqueRegisteredProducts()).thenReturn(registeredProductSet);
        final UserWithProducts userWithProducts = new UserWithProducts(context, userMock, prodRegListener) {
            @Override
            protected boolean isUserSignedIn(final Context context) {
                return true;
            }

            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            RegisteredProductsListener getRegisteredProductsListener(final RegisteredProduct product) {
                return prodRegListenerMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }
        };

        final Product product = new Product("ctn", null, null);
        RegisteredProduct registeredProduct = mock(RegisteredProduct.class);
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        when(registeredProduct.getRegisteredProductIfExists(localRegisteredProducts)).thenReturn(registeredProduct);
        when(registeredProduct.getRegistrationState()).thenReturn(RegistrationState.REGISTERED);
        userWithProducts.registerProduct(product);
        verify(userWithProductsMock).createDummyRegisteredProduct(product);
        verify(registeredProduct).getRegisteredProductIfExists(localRegisteredProducts);
        verify(prodRegListener).onProdRegFailed(registeredProduct, userWithProductsMock);
        testMapProductToRegisteredProduct(product);
        testThrowExceptionWhenListenerNull(userWithProducts, product);
    }

    public void testRegisterProductOnValidInput() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final RegisteredProductsListener prodRegListenerMock = mock(RegisteredProductsListener.class);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        Set<RegisteredProduct> registeredProductSet = new HashSet<>();
        registeredProducts.add(new RegisteredProduct(null, null, null));
        registeredProducts.add(new RegisteredProduct(null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn", null, null));
        registeredProductSet.add(new RegisteredProduct("ctn1", null, null));
        when(localRegisteredProducts.getRegisteredProducts()).thenReturn(registeredProducts);
        when(localRegisteredProducts.getUniqueRegisteredProducts()).thenReturn(registeredProductSet);
        final UserWithProducts userWithProducts = new UserWithProducts(context, userMock, prodRegListener) {
            @Override
            protected boolean isUserSignedIn(final Context context) {
                return true;
            }

            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }

            @NonNull
            @Override
            RegisteredProductsListener getRegisteredProductsListener(final RegisteredProduct product) {
                return prodRegListenerMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @Override
            public String getUuid() {
                return "1234";
            }
        };

        final Product product = new Product("ctn", null, null);
        RegisteredProduct registeredProduct = mock(RegisteredProduct.class);
        when(userWithProductsMock.isUserSignedIn(context)).thenReturn(true);
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        when(registeredProduct.getRegisteredProductIfExists(localRegisteredProducts)).thenReturn(registeredProduct);
        when(registeredProduct.getRegistrationState()).thenReturn(RegistrationState.PENDING);
        when(registeredProduct.getUserUUid()).thenReturn("1234");
        userWithProducts.registerProduct(product);
        verify(userWithProductsMock).updateLocaleCache(registeredProduct, registeredProduct.getProdRegError(), RegistrationState.REGISTERING);
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

    public void
    testGettingRegisteredListener() {
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        userWithProducts.getRegisteredProducts(registeredProductsListener);
        assertEquals(registeredProductsListener, userWithProducts.getRegisteredProductsListener());
    }

    public void testReturnCorrectRequestType() {
        final Product product = new Product("ctn", null, null);
        final RegisteredProduct registeredProduct = new RegisteredProduct("ctn", null, null);
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        userWithProducts.registerProduct(product);
        assertTrue(userWithProducts.getRequestType() == UserWithProducts.PRODUCT_REGISTRATION);
        when(userMock.isUserSignIn()).thenReturn(false);
        userWithProducts.getRegisteredProducts(registeredProductsListener);
        assertTrue(userWithProducts.getRequestType() != (UserWithProducts.FETCH_REGISTERED_PRODUCTS));
    }

    public void testValidatingSerialNumber() {
        ProductMetadataResponseData data = mock(ProductMetadataResponseData.class);
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        when(data.getRequiresSerialNumber()).thenReturn("true");
        userWithProducts.isValidSerialNumber(data, productMock);
        when(productMock.getSerialNumber()).thenReturn("1234");
        when(data.getSerialNumberFormat()).thenReturn("^[1]{1}[3-9]{1}[0-5]{1}[0-9]{1}$");
        userWithProducts.isValidSerialNumber(data, productMock);
        when(productMock.getSerialNumber()).thenReturn("1344");
        assertTrue(userWithProducts.isValidSerialNumber(data, productMock));
    }

    public void testValidatingPurchaseDate() {
        ProductMetadataResponseData data = mock(ProductMetadataResponseData.class);
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        when(productMock.getPurchaseDate()).thenReturn("2016-03-22");
        when(data.getRequiresDateOfPurchase()).thenReturn("false");
        assertTrue(userWithProducts.isValidPurchaseDate("2016-03-22"));
        when(data.getRequiresDateOfPurchase()).thenReturn("true");
        assertTrue(userWithProducts.isValidPurchaseDate("2016-03-22"));
    }

    public void testRegistrationRequestTest() {
        RegisteredProduct productMock = mock(RegisteredProduct.class);
        final String ctn = "HC5410/83";
        when(productMock.getCtn()).thenReturn(ctn);
        final String serialNumber = "1344";
        final String purchaseDate = "2016-04-15";
        when(productMock.getSerialNumber()).thenReturn(serialNumber);
        when(productMock.getSector()).thenReturn(Sector.B2C);
        when(productMock.getCatalog()).thenReturn(Catalog.CONSUMER);
        when(productMock.getSerialNumber()).thenReturn(serialNumber);
        when(productMock.getPurchaseDate()).thenReturn(purchaseDate);
        when(productMock.getLocale()).thenReturn("en_GB");
        RegistrationRequest registrationRequest = userWithProducts.getRegistrationRequest(context, productMock);
        assertEquals(registrationRequest.getCatalog(), Catalog.CONSUMER);
        assertEquals(registrationRequest.getSector(), Sector.B2C);
        assertEquals(registrationRequest.getCtn(), ctn);
        assertEquals(registrationRequest.getProductSerialNumber(), serialNumber);
        assertEquals(productMock.getPurchaseDate(), registrationRequest.getPurchaseDate());
    }

    public void testIsCtnRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        when(product.getCtn()).thenReturn("HD8967/09");
        when(product.getSerialNumber()).thenReturn("1234");
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        final RegisteredProduct registeredProduct = new RegisteredProduct("HD8970/09", null, null);
        registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
        registeredProduct.setSerialNumber("1234");
        final RegisteredProduct registeredProduct1 = new RegisteredProduct("HD8969/09", null, null);
        registeredProduct1.setRegistrationState(RegistrationState.REGISTERED);
        registeredProduct1.setSerialNumber("1234");
        final RegisteredProduct registeredProduct2 = new RegisteredProduct("HD8968/09", null, null);
        registeredProduct2.setRegistrationState(RegistrationState.REGISTERED);
        registeredProduct2.setSerialNumber("1234");
        final RegisteredProduct registeredProduct3 = new RegisteredProduct("HD8967/09", null, null);
        registeredProduct3.setRegistrationState(RegistrationState.REGISTERED);
        registeredProduct3.setSerialNumber("1234");
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        registeredProducts.add(registeredProduct2);
        registeredProducts.add(registeredProduct3);
        assertTrue(userWithProducts.isCtnRegistered(registeredProducts, product));
    }

    public void testIsCtnNotRegistered() {
        RegisteredProduct product = mock(RegisteredProduct.class);
        when(product.getCtn()).thenReturn("HD8965/09");
        when(product.getSerialNumber()).thenReturn("1234");
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        final RegisteredProduct registeredProduct = new RegisteredProduct("HD8970/09", null, null);
        registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
        final RegisteredProduct registeredProduct1 = new RegisteredProduct("HD8969/09", null, null);
        registeredProduct1.setRegistrationState(RegistrationState.REGISTERED);
        final RegisteredProduct registeredProduct2 = new RegisteredProduct("HD8968/09", null, null);
        registeredProduct2.setRegistrationState(RegistrationState.REGISTERED);
        final RegisteredProduct registeredProduct3 = new RegisteredProduct("HD8967/09", null, null);
        registeredProduct3.setRegistrationState(RegistrationState.REGISTERED);
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        registeredProducts.add(registeredProduct2);
        registeredProducts.add(registeredProduct3);
        assertFalse(userWithProducts.isCtnRegistered(registeredProducts, product));
    }

    public void testGetPrxResponseListenerForRegisteringProducts() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, userMock, prodRegListener) {
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
        ResponseListener responseListener = userWithProducts.getPrxResponseListener(product);
        RegistrationResponse responseData = mock(RegistrationResponse.class);
        final RegistrationResponseData data = mock(RegistrationResponseData.class);
        when(responseData.getData()).thenReturn(data);

        when(data.getWarrantyEndDate()).thenReturn("2016-03-22");
        userWithProducts.setCurrentRegisteredProduct(product);
        responseListener.onResponseSuccess(responseData);
        verify(product).setRegistrationState(RegistrationState.REGISTERED);
        verify(localRegisteredProducts).updateRegisteredProducts(product);
        verify(prodRegListener).onProdRegSuccess(product, userWithProductsMock);
        verify(userWithProductsMock).mapRegistrationResponse(responseData, product);
        responseListener.onResponseError(new PrxError("test", 10));
        verify(errorHandlerMock).handleError(userWithProductsMock, product, 10);
    }

    public void testMapRegistrationResponse() {
        RegistrationResponse responseData = mock(RegistrationResponse.class);
        final RegistrationResponseData data = mock(RegistrationResponseData.class);
        when(responseData.getData()).thenReturn(data);
        when(data.getWarrantyEndDate()).thenReturn("2016-03-22");
        final RegisteredProduct registeredProduct = new RegisteredProduct("ctn", null, null);
        userWithProducts.mapRegistrationResponse(responseData, registeredProduct);
        assertEquals(registeredProduct.getEndWarrantyDate(), data.getWarrantyEndDate());
    }

    public void testInvokingAccessTokenWhenExpired() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);

        RegisteredProduct product = new RegisteredProduct("ctn", null, null);
        final User userMock = mock(User.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener) {
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
        errorHandlerMock.handleError(userWithProductsMock, product, 403);
        RefreshLoginSessionHandler refreshLoginSessionHandler = mock(RefreshLoginSessionHandler.class);
        when(userWithProductsMock.getRefreshLoginSessionHandler(product, context)).thenReturn(refreshLoginSessionHandler);
        userWithProducts.onAccessTokenExpire(product);
        verify(userMock).refreshLoginSession(refreshLoginSessionHandler);
    }

    public void testGetUserRefreshedLoginSession() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        RegisteredProduct product = mock(RegisteredProduct.class);
        final LocalRegisteredProducts localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener) {
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
        userWithProducts.setRequestType(UserWithProducts.PRODUCT_REGISTRATION);
        RefreshLoginSessionHandler refreshLoginSessionHandler = userWithProducts.getRefreshLoginSessionHandler(product, context);
        refreshLoginSessionHandler.onRefreshLoginSessionFailedWithError(50);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.ACCESS_TOKEN_INVALID, RegistrationState.FAILED);
        verify(localRegisteredProductsMock).updateRegisteredProducts(product);
        refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
        verify(userWithProductsMock).retryRequests(context, product);
    }

    public void testRegistrationRequest() {
        final RegistrationRequest registrationRequest = new RegistrationRequest(null, null, null);
        final RegisteredProduct productMock = mock(RegisteredProduct.class);
        final RequestManager requestManagerMock = mock(RequestManager.class);
        final ResponseListener responseListenerMock = mock(ResponseListener.class);
        final String locale = "en_GB";
        when(productMock.getLocale()).thenReturn(locale);
        final String purchase_Date = "2016-03-22";
        when(productMock.getPurchaseDate()).thenReturn(purchase_Date);
        final String serialNumber = "1344";
        when(productMock.getSerialNumber()).thenReturn(serialNumber);

        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener) {

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
            ResponseListener getPrxResponseListener(final RegisteredProduct product) {
                return responseListenerMock;
            }

            @NonNull
            @Override
            UserWithProducts getUserProduct() {
                return userWithProductsMock;
            }
        };
        userWithProducts.makeRegistrationRequest(context, productMock);
        verify(requestManagerMock).executeRequest(registrationRequest, responseListenerMock);
    }

    public void testRetryMethod() {
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final Product product = new Product("ctn", null, null);
        final RegisteredProduct registeredProduct = new RegisteredProduct("ctn", null, null);
        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProductsListener registeredProductsListenerMock = mock(RegisteredProductsListener.class);
        UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener) {
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
            protected User getUser() {
                return userMock;
            }
        };
        when(userWithProductsMock.createDummyRegisteredProduct(product)).thenReturn(registeredProduct);
        userWithProducts.registerProduct(product);
        userWithProducts.retryRequests(context, registeredProduct);
        verify(userWithProductsMock).makeRegistrationRequest(context, registeredProduct);
        userWithProducts.getRegisteredProducts(registeredProductsListenerMock);
        userWithProducts.retryRequests(context, registeredProduct);
        userWithProducts.setRequestType(-1);
        userWithProducts.retryRequests(context, registeredProduct);
        verify(prodRegListenerMock, never()).onProdRegFailed(registeredProduct, userWithProductsMock);
    }

    public void testUpdateLocaleCacheOnError() {
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", null, null);
        userWithProducts.updateLocaleCache(registeredProduct, ProdRegError.PRODUCT_ALREADY_REGISTERED, RegistrationState.FAILED);
        assertEquals(registeredProduct.getRegistrationState(), RegistrationState.FAILED);
        assertEquals(registeredProduct.getProdRegError(), ProdRegError.PRODUCT_ALREADY_REGISTERED);
        verify(localRegisteredProducts).updateRegisteredProducts(registeredProduct);
    }

    @SuppressWarnings("deprecation")
    public void testCachedRegisterProducts() {
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", null, null);
        registeredProduct.setRegistrationState(RegistrationState.PENDING);
        registeredProduct.setSerialNumber("Serial");
        registeredProduct.setPurchaseDate("2098-03-22");
        RegisteredProduct registeredProduct1 = new RegisteredProduct("ctn1", null, null);
        registeredProduct1.setRegistrationState(RegistrationState.FAILED);
        registeredProduct1.setSerialNumber("Serial1");
        registeredProduct1.setPurchaseDate("2098-04-22");
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        when(userWithProductsMock.isUserSignedIn(context)).thenReturn(false);
        userWithProducts.setCurrentRegisteredProduct(registeredProduct);
        userWithProducts.registerCachedProducts(registeredProducts);

        verify(userWithProductsMock).updateLocaleCache(registeredProduct1, ProdRegError.USER_NOT_SIGNED_IN, RegistrationState.FAILED);
        verify(prodRegListener, Mockito.atLeastOnce()).onProdRegFailed(registeredProduct, userWithProductsMock);
        when(userWithProductsMock.isUserSignedIn(context)).thenReturn(true);
    }

    public void testGetRegisteredProductsListener() {
        RegisteredProduct registeredProductMock = mock(RegisteredProduct.class);
        when(registeredProductMock.getCtn()).thenReturn("ctn");
        when(registeredProductMock.getSerialNumber()).thenReturn("serial");
        when(registeredProductMock.getRegistrationState()).thenReturn(RegistrationState.REGISTERED);
        RegisteredProductsListener registeredProductsListener = userWithProducts.getRegisteredProductsListener(registeredProductMock);
        final ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(registeredProductMock);
        userWithProducts.setCurrentRegisteredProduct(registeredProductMock);
        registeredProductsListener.getRegisteredProducts(registeredProducts, 0);
        verify(prodRegListener).onProdRegFailed(registeredProductMock, userWithProductsMock);
    }

    public void testGetMetadataListener() {
        RegisteredProduct productMock = new RegisteredProduct("ctn", null, null);
        final UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        final UserWithProducts userWithProducts = new UserWithProducts(context, new User(context), prodRegListener) {
            @Override
            protected boolean isValidSerialNumber(final ProductMetadataResponseData data, final RegisteredProduct product) {
                return true;
            }

            @Override
            protected boolean isValidPurchaseDate(String date) {
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
        MetadataListener metadataListener = userWithProducts.getMetadataListener(productMock);
        ProductMetadataResponse responseDataMock = mock(ProductMetadataResponse.class);
        metadataListener.onMetadataResponse(responseDataMock);
        verify(userWithProductsMock).makeRegistrationRequest(context, productMock);
        metadataListener.onErrorResponse("test", 8);
        verify(errorHandlerMock).handleError(userWithProductsMock, productMock, 8);
    }
}
