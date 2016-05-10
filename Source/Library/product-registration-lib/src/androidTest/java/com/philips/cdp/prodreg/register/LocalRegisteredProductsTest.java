package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.registration.User;

import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalRegisteredProductsTest extends MockitoTestCase {

    private LocalRegisteredProducts localRegisteredProducts;
    private Context context;
    @Mock
    private LocalSharedPreference localSharedPreference;
    private RegisteredProduct mockRegisteredProduct;
    private String data;
    private RegisteredProduct registeredProduct;
    private HashSet<RegisteredProduct> registeredProducts = new HashSet<>();
    private Gson gson;
    private RegisteredProduct[] registeredProducttest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
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
            public LocalSharedPreference getLocalSharedPreference() {
                return localSharedPreference;
            }
        };
        mockRegisteredProduct = mock(RegisteredProduct.class);
        data = localSharedPreference.getData("prod_reg_key");
        registeredProduct = new RegisteredProduct("HC5410/83", Sector.B2C, Catalog.CONSUMER);
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
        verify(localSharedPreference).storeData(LocalRegisteredProducts.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    public void testUpdateRegisteredProducts() {
        RegisteredProduct registeredProductMock = mock(RegisteredProduct.class);
        when(registeredProductMock.getCtn()).thenReturn("ctn");
        localRegisteredProducts.updateRegisteredProducts(registeredProductMock);
        assertEquals(registeredProducts.size(), 4);
        verify(localSharedPreference).storeData(LocalRegisteredProducts.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    public void testSyncLocalCache() {
        RegisteredProduct registeredProductMock = mock(RegisteredProduct.class);
        when(registeredProductMock.getUserUUid()).thenReturn("ABC");
        when(registeredProductMock.getRegistrationState()).thenReturn(RegistrationState.REGISTERED);
        localRegisteredProducts.syncLocalCache(new RegisteredProduct[]{registeredProductMock});
        assertEquals(registeredProducts.size(), 4);
    }

    public void testGetRegisteredProducts() {
        User userMock = mock(User.class);
        RegisteredProduct registeredProductMOck = mock(RegisteredProduct.class);
        assertEquals(3, localRegisteredProducts.getUniqueRegisteredProducts().size());
        assertEquals(0, localRegisteredProducts.getRegisteredProducts().size());
        when(registeredProductMOck.getUserUUid()).thenReturn("12345");
        when(userMock.getJanrainUUID()).thenReturn("12345");
        // verify(registeredProductMOck.getUserUUid().equalsIgnoreCase(userMock.getJanrainUUID()));
        assertTrue(registeredProductMOck.getUserUUid().equalsIgnoreCase(userMock.getJanrainUUID()));
    }
}
