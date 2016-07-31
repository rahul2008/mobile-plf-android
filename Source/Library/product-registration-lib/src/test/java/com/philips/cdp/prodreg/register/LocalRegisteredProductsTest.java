package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.registration.User;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class LocalRegisteredProductsTest extends TestCase {

    private LocalRegisteredProducts localRegisteredProducts;
    private Context context;
    @Mock
    private ProdRegCache prodRegCache;
    private HashSet<RegisteredProduct> registeredProducts = new HashSet<>();
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = mock(Context.class);
        User user = new User(context);
        gson = new Gson();
        addDummyProjects();
        localRegisteredProducts = new LocalRegisteredProducts(context, user) {
            @Override
            protected Set<RegisteredProduct> getUniqueRegisteredProducts() {
                return registeredProducts;
            }

            @NonNull
            @Override
            protected Gson getGSon() {
                return gson;
            }

            @Override
            public ProdRegCache getProdRegCache() {
                return prodRegCache;
            }
        };
    }

    private void addDummyProjects() {
        registeredProducts.add(new RegisteredProduct("ctn", null, null));
        registeredProducts.add(new RegisteredProduct("ctn1", null, null));
        registeredProducts.add(new RegisteredProduct("ctn2", null, null));
    }

    public void testStore() {
        RegisteredProduct registeredProductMock = mock(RegisteredProduct.class);
        when(registeredProductMock.getCtn()).thenReturn("ctn");
        localRegisteredProducts.store(registeredProductMock);
        assertEquals(registeredProducts.size(), 4);
        verify(prodRegCache).storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    public void testUpdateRegisteredProducts() {
        RegisteredProduct registeredProductMock = mock(RegisteredProduct.class);
        when(registeredProductMock.getCtn()).thenReturn("ctn");
        localRegisteredProducts.updateRegisteredProducts(registeredProductMock);
        assertEquals(registeredProducts.size(), 4);
        verify(prodRegCache).storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    public void testSyncLocalCache() {
        RegisteredProduct registeredProductMock = mock(RegisteredProduct.class);
        when(registeredProductMock.getUserUUid()).thenReturn("ABC");
        when(registeredProductMock.getRegistrationState()).thenReturn(RegistrationState.REGISTERED);
        localRegisteredProducts.syncLocalCache(new RegisteredProduct[]{registeredProductMock});
        assertEquals(registeredProducts.size(), 4);
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", null, null);
        localRegisteredProducts.syncLocalCache(new RegisteredProduct[]{registeredProduct});
        assertEquals(registeredProducts.size(), 3);
    }

    public void testGetRegisteredProducts() {
        User userMock = mock(User.class);
        when(userMock.isUserSignIn()).thenReturn(true);
        final RegisteredProduct[] registeredProducts = {new RegisteredProduct(null, null, null), new RegisteredProduct(null, null, null), new RegisteredProduct(null, null, null)};

        localRegisteredProducts = new LocalRegisteredProducts(context, userMock) {
            @Override
            protected RegisteredProduct[] getRegisteredProducts(final Gson gson, final String data) {
                return registeredProducts;
            }
        };
        assertTrue(localRegisteredProducts.getRegisteredProducts().size() == 3);
        when(userMock.isUserSignIn()).thenReturn(false);
        assertTrue(localRegisteredProducts.getRegisteredProducts().size() == 0);
    }

    public void testGettingUniqueRegisteredProducts() {
        User userMock = mock(User.class);
        when(userMock.isUserSignIn()).thenReturn(true);
        final RegisteredProduct[] registeredProducts = {new RegisteredProduct("ctn", null, null), new RegisteredProduct("ctn", null, null), new RegisteredProduct("ctn", null, null)};

        localRegisteredProducts = new LocalRegisteredProducts(context, userMock) {
            @Override
            protected RegisteredProduct[] getRegisteredProducts(final Gson gson, final String data) {
                return registeredProducts;
            }
        };
        assertTrue(localRegisteredProducts.getUniqueRegisteredProducts().size() == 1);
    }

    public void testRemoveProductFromCache() {
        User userMock = mock(User.class);
        RegisteredProduct registeredProductMock = new RegisteredProduct("ctn", null, null);
        when(userMock.isUserSignIn()).thenReturn(true);
        final ProdRegCache prodRegCacheMock = mock(ProdRegCache.class);
        localRegisteredProducts = new LocalRegisteredProducts(context, userMock) {
            @Override
            protected Set<RegisteredProduct> getUniqueRegisteredProducts() {
                return registeredProducts;
            }

            @Override
            protected ProdRegCache getProdRegCache() {
                return prodRegCacheMock;
            }

            @NonNull
            @Override
            protected Gson getGSon() {
                return gson;
            }
        };
        localRegisteredProducts.removeProductFromCache(registeredProductMock);
        assertEquals(registeredProducts.size(), 2);
        verify(prodRegCacheMock).storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }
}
