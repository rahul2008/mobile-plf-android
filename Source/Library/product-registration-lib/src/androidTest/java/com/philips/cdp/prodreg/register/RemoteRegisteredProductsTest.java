package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.test.InstrumentationTestCase;

import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponse;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponseData;
import com.philips.cdp.prodreg.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;

import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RemoteRegisteredProductsTest extends InstrumentationTestCase {

    RemoteRegisteredProducts remoteRegisteredProducts;
    @Mock
    RegisteredProduct registeredProduct;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        remoteRegisteredProducts = new RemoteRegisteredProducts();
        context = getInstrumentation().getContext();
    }

    public void testGetPrxResponseListenerForRegisteredProducts() {
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        ResponseListener responseListener = remoteRegisteredProducts.getPrxResponseListenerForRegisteredProducts(userWithProductsMock, localRegisteredProducts, registeredProductsListener);
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
        final long value = System.currentTimeMillis();
        when(userWithProductsMock.getTimeStamp()).thenReturn(value);
        responseListener.onResponseSuccess(registeredResponse);
        verify(registeredProductsListener, Mockito.atLeastOnce()).getRegisteredProducts(localRegisteredProducts.getRegisteredProducts(), value);
        responseListener.onResponseError(new PrxError("test", 10));
        verify(registeredProductsListener, Mockito.atLeastOnce()).getRegisteredProducts(localRegisteredProducts.getRegisteredProducts(), 0);
        responseListener.onResponseError(new PrxError("test", ProdRegError.ACCESS_TOKEN_INVALID.getCode()));
        verify(userWithProductsMock).onAccessTokenExpire(null);
    }

    public void testRegisterMethod() {
        final ResponseListener responseListenerMock = mock(ResponseListener.class);
        final RequestManager requestManager = mock(RequestManager.class);
        final RegisteredProductsRequest registeredProductsRequest = mock(RegisteredProductsRequest.class);
        RemoteRegisteredProducts remoteRegisteredProducts = new RemoteRegisteredProducts() {
            @NonNull
            @Override
            ResponseListener getPrxResponseListenerForRegisteredProducts(final UserWithProducts userWithProducts, final LocalRegisteredProducts localRegisteredProducts, final RegisteredProductsListener registeredProductsListener) {
                return responseListenerMock;
            }

            @NonNull
            @Override
            protected RequestManager getRequestManager(final Context context) {
                return requestManager;
            }

            @NonNull
            @Override
            protected RegisteredProductsRequest getRegisteredProductsRequest(final User user) {
                return registeredProductsRequest;
            }
        };
        User user = mock(User.class);
        UserWithProducts userWithProducts = mock(UserWithProducts.class);
        RegisteredProductsListener registeredProductsListener = mock(RegisteredProductsListener.class);
        remoteRegisteredProducts.getRegisteredProducts(context, userWithProducts, user, registeredProductsListener);
        verify(requestManager).executeRequest(registeredProductsRequest, responseListenerMock);
    }

    public void testGetRegisteredProductsRequest() {
        User user = mock(User.class);
        when(user.getAccessToken()).thenReturn("access_token");
        RegisteredProductsRequest registeredProductsRequest = remoteRegisteredProducts.getRegisteredProductsRequest(user);
        assertEquals(registeredProductsRequest.getAccessToken(), "access_token");
    }
}
