package com.philips.cdp.prodreg.backend;

import android.content.Context;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.registration.dao.DIUserProfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

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

    public void testCacheForProductToRegister(){
        DIUserProfile diUserProfilemock =mock(DIUserProfile.class);
        Product productmock=mock(Product.class);
        final String basePath="/productstoregister";
        final String uuid = "1322";
        final String ctn = "HC541083";
        final  String mSeraialNumber="12345";
        when(diUserProfilemock.getJanrainUUID()).thenReturn(uuid);
        when(productmock.getSerialNumber()).thenReturn(mSeraialNumber);
        when(productmock.getCtn()).thenReturn(ctn);
        cacheHandler.getInternalCacheForProductToRegister(diUserProfilemock, productmock);
        File file = new File(basePath + productmock.getCtn() + productmock.getSerialNumber());
        cacheHandler.createFileIfNotCreated(file);
        File file1= new File(basePath + ctn + mSeraialNumber);
        assertEquals(file, file1);
    }
    public  void testCacheProductsToRegister(){
        DIUserProfile diUserProfilemock =mock(DIUserProfile.class);
        Product productmock=mock(Product.class);
        final String basePath="/productstoregister";
        final String uuid = "1322";
        final String ctn = "HC541083";
        final  String mSeraialNumber="12345";
        when(diUserProfilemock.getJanrainUUID()).thenReturn(uuid);
        when(productmock.getSerialNumber()).thenReturn(mSeraialNumber);
        when(productmock.getCtn()).thenReturn(ctn);
        cacheHandler.cacheObject(productmock, cacheHandler.getInternalCacheForProductToRegister(diUserProfilemock, productmock));
        File file = new File(basePath + productmock.getCtn() + productmock.getSerialNumber());
        cacheHandler.createFileIfNotCreated(file);

        try {
            ObjectOutput objectOutput=new ObjectOutputStream(new FileOutputStream(file));
            objectOutput.writeObject(productmock);
            objectOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


     public void  testCreateFolder(){
         DIUserProfile diUserProfilemock =mock(DIUserProfile.class);
         final String uuid = "1322";
         final String basePath="/productstoregister";
         when(diUserProfilemock.getJanrainUUID()).thenReturn(uuid);
         String  path = cacheHandler.createFolder(basePath + cacheHandler.getUUID(diUserProfilemock));
         assertEquals(path,basePath+uuid+"_");
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