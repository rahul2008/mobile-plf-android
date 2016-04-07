package com.philips.cdp.prodreg.backend;

import android.content.Context;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.registration.dao.DIUserProfile;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CacheHandlerTest extends MockitoTestCase {

    CacheHandler cacheHandler;
    Context mContext;
    Product product;
    DIUserProfile diUserProfile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        cacheHandler = new CacheHandler(mContext);
        product = mock(Product.class);
        diUserProfile=mock(DIUserProfile.class);


    }

    public void testCacheProductsToRegister() throws Exception {
        Product product = mock(Product.class);
        DIUserProfile diUserProfile=mock(DIUserProfile.class);
        cacheHandler.cacheProductsToRegister(product, diUserProfile);
         }

     public void  testgetInternalCacheForProductToRegister() throws Exception{
         DIUserProfile diUserProfilemock =mock(DIUserProfile.class);
         Product productmock=mock(Product.class);
         File file=mock(File.class);
         final String uuid = "1322";
         final  String mSeraialNumber="12345";
         final String ctn = "HC5410/83";
         when(diUserProfilemock.getJanrainUUID()).thenReturn(uuid);
         when(productmock.getSerialNumber()).thenReturn(mSeraialNumber);
         when(productmock.getCtn()).thenReturn(ctn);
       //  file= cacheHandler.getSerialNumber(product) + cacheHandler.getUUID(diUserProfilemock) + product.setSerialNumber(ctn);
         /*//assertEquals(path,);
      //   1322_NO_SERIALnull



         cacheHandler.getInternalCacheForProductToRegister(diUserProfile, product);
*/
     }
    public  void testGetUUID(){
        DIUserProfile diUserProfilemock =mock(DIUserProfile.class);
        final String uuid = "1322";
        when(diUserProfilemock.getJanrainUUID()).thenReturn(uuid);
        String userProfile = cacheHandler.getUUID(diUserProfilemock);
        assertEquals(userProfile, ("1322_"));
        //Failed case
        final String noUUid="nouuid";
        when(diUserProfilemock.getJanrainUUID()).thenReturn(noUUid);
        assertEquals(cacheHandler.getUUID(diUserProfile), ("nouuid/"));
    }

    public  void testGetSerialNumber(){
        Product productmock=mock(Product.class);
        final  String mSeraialNumber="12345";
        when(productmock.getSerialNumber()).thenReturn(mSeraialNumber);
        String seraialno = cacheHandler.getSerialNumber(productmock);
        assertEquals(seraialno,mSeraialNumber);
        //Failed case
        final String noSerialnumber="NO_SERIAL";
        when(product.getSerialNumber()).thenReturn(noSerialnumber);
        String noSerial = cacheHandler.getSerialNumber(product);
        assertEquals(noSerial,noSerialnumber);
    }


}