package com.philips.cdp.prodreg.register;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.constants.RegistrationState;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegisteredProductTest extends TestCase {

    RegisteredProduct registeredProduct;
    String mCTN;
    Sector mSector;
    Catalog mCatalog;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        registeredProduct = new RegisteredProduct(mCTN, mSector, mCatalog);
    }

    public void testSetRegistrationState() throws Exception {
        registeredProduct.setRegistrationState(RegistrationState.FAILED);
    }

    public void testGetEndWarrantyDate() throws Exception {
        registeredProduct.setEndWarrantyDate("29/4/2016");
        assertEquals("29/4/2016", registeredProduct.getEndWarrantyDate());
    }

    public void testSetEndWarrantyDate() throws Exception {
        registeredProduct.setEndWarrantyDate("29/4/2016");
    }

    public void testGetUserUUid() throws Exception {
        registeredProduct.setUserUUid("ABCD");
        assertEquals("ABCD", registeredProduct.getUserUUid());
    }

    public void testGetContractNumber() throws Exception {
        registeredProduct.setContractNumber("900000");
        assertEquals("900000", registeredProduct.getContractNumber());
    }

    public void testSetContractNumber() throws Exception {
        registeredProduct.setContractNumber("900000");
    }

    public void testIsShouldConsiderUUID() {
        assertFalse(registeredProduct.isShouldConsiderUUID("abc", "abc", true, true));
        assertFalse(registeredProduct.isShouldConsiderUUID("abc", "abc", true, false));
        assertTrue(registeredProduct.isShouldConsiderUUID("abcd", "abc", true, true));
    }

    public void testEqual() {
        assertFalse(registeredProduct.equals(registeredProduct.isShouldConsiderUUID("abc", "abc", true, true)));
    }

    public void testIsProductAlreadyRegistered() {
        RegisteredProduct registeredProduct = new RegisteredProduct("abcd", null, null);
        RegisteredProduct registeredProduct1 = new RegisteredProduct("abcdef", null, null);
        LocalRegisteredProducts localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        List<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(new RegisteredProduct("abc", null, null));
        final RegisteredProduct abcd = new RegisteredProduct("abcd", null, null);
        abcd.setRegistrationState(RegistrationState.REGISTERED);
        registeredProducts.add(abcd);
        registeredProducts.add(new RegisteredProduct("abcde", null, null));
        when(localRegisteredProductsMock.getRegisteredProducts()).thenReturn(registeredProducts);
        assertTrue(registeredProduct.isProductAlreadyRegistered(localRegisteredProductsMock));
        assertFalse(registeredProduct1.isProductAlreadyRegistered(localRegisteredProductsMock));
    }

    public void testGetRegisteredProductIfExists() {
        RegisteredProduct registeredProduct = new RegisteredProduct("abcd", null, null);
        RegisteredProduct registeredProduct1 = new RegisteredProduct("abcdef", null, null);
        LocalRegisteredProducts localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        List<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(new RegisteredProduct("abc", null, null));
        final RegisteredProduct abcd = new RegisteredProduct("abcd", null, null);
        abcd.setRegistrationState(RegistrationState.REGISTERED);
        registeredProducts.add(abcd);
        registeredProducts.add(new RegisteredProduct("abcde", null, null));
        when(localRegisteredProductsMock.getRegisteredProducts()).thenReturn(registeredProducts);
        assertTrue(registeredProduct.getRegisteredProductIfExists(localRegisteredProductsMock) != null);
        verify(localRegisteredProductsMock).updateRegisteredProducts(registeredProduct);
        assertFalse(registeredProduct1.getRegisteredProductIfExists(localRegisteredProductsMock) != null);
    }
}