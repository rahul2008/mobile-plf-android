package com.philips.cdp.prodreg.backend;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.model.RegistrationState;

import org.mockito.Mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProductTest extends MockitoTestCase {

    RegisteredProduct registeredProduct;
    @Mock
    String mCTN, mSerialNumber, mPurchdate;
    @Mock
    Sector mSector;
    @Mock
    Catalog mCatalog;
   /* @Mock
    final String parentUuid, final String currentUuid, final boolean parentState, final boolean currentState
*/

    @Override
    protected void setUp() throws Exception {
        registeredProduct = new RegisteredProduct(mCTN, mSerialNumber, mPurchdate, mSector, mCatalog);
    }

    public void testSetRegistrationState() throws Exception {
        registeredProduct.setRegistrationState(RegistrationState.FAILED);
    }

    public void testGetRegistrationState() throws Exception {
        registeredProduct.setRegistrationState(RegistrationState.FAILED);
        assertEquals("FAILED", registeredProduct.getRegistrationState());
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

    public void testSetUserUUid() throws Exception {
        assertEquals("ABCD", registeredProduct.getUserUUid());
    }

    public void testGetProdRegError() throws Exception {
        registeredProduct.setProdRegError(ProdRegError.INVALID_CTN);
        assertEquals("INVALID_CTN", registeredProduct.getProdRegError());
    }

    public void testSetProdRegError() throws Exception {
        assertEquals("INVALID_CTN", registeredProduct.getProdRegError());
    }

    public void testGetContractNumber() throws Exception {
        registeredProduct.setContractNumber("900000");
        assertEquals("900000", registeredProduct.getContractNumber());
    }

    public void testSetContractNumber() throws Exception {
        registeredProduct.setContractNumber("900000");
    }

  /*  public void testIsShouldConsiderUUID(){
        registeredProduct.isShouldConsiderUUID()
    }*/
}