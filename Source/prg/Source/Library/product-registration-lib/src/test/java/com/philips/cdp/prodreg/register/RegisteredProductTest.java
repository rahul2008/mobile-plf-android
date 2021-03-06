package com.philips.cdp.prodreg.register;


import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prxclient.PrxConstants;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

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
    PrxConstants.Sector mSector;
    PrxConstants.Catalog mCatalog;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        registeredProduct = new RegisteredProduct(mCTN, mSector, mCatalog);
    }

    @Test
    public void testSetRegistrationState() throws Exception {
        registeredProduct.setRegistrationState(RegistrationState.FAILED);
    }

    @Test
    public void testGetEndWarrantyDate() throws Exception {
        registeredProduct.setEndWarrantyDate("29/4/2016");
        assertEquals("29/4/2016", registeredProduct.getEndWarrantyDate());
    }

    @Test
    public void testSetEndWarrantyDate() throws Exception {
        registeredProduct.setEndWarrantyDate("29/4/2016");
    }

    @Test
    public void testGetUserUUid() throws Exception {
        registeredProduct.setUserUUid("ABCD");
        assertEquals("ABCD", registeredProduct.getUserUUid());
    }

    @Test
    public void testGetContractNumber() throws Exception {
        registeredProduct.setContractNumber("900000");
        assertEquals("900000", registeredProduct.getContractNumber());
    }

    @Test
    public void testSetContractNumber() throws Exception {
        registeredProduct.setContractNumber("900000");
    }

    @Test
    public void testIsShouldConsiderUUID() {
        assertFalse(registeredProduct.isShouldConsiderUUID("abc", "abc", true, true));
        assertFalse(registeredProduct.isShouldConsiderUUID("abc", "abc", true, false));
        assertTrue(registeredProduct.isShouldConsiderUUID("abcd", "abc", true, true));
    }

    @Test
    public void testEqual() {
        assertFalse(registeredProduct.equals(registeredProduct.isShouldConsiderUUID("abc", "abc", true, true)));
    }

    @Test
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

    @Test
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