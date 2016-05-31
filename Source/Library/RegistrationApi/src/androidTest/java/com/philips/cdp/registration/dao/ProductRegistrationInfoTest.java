/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310202337 on 11/26/2015.
 */
public class ProductRegistrationInfoTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {
    ProductRegistrationInfo productRegistrationInfo = new ProductRegistrationInfo();

    public ProductRegistrationInfoTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());


    }

    public void testContractNumberGetter(){

        productRegistrationInfo.setContractNumber("TestContract");
        assertEquals("TestContract", productRegistrationInfo.getContractNumber());


    }

    public void testDeviceIDGetter(){
        productRegistrationInfo.setDeviceID("TestDevice");
        assertEquals("TestDevice", productRegistrationInfo.getDeviceID());

    }

    public void test_getDeviceName(){
        productRegistrationInfo.setDeviceName("TestDevice");
        assertEquals("TestDevice", productRegistrationInfo.getDeviceName());
    }

    public void test_getExtendedwarranty(){
        productRegistrationInfo.setExtendedWarranty("TestExtendedWarranty");
        assertEquals("TestExtendedWarranty", productRegistrationInfo.getExtendedwarranty());

    }

    public void test_getIsGenerations(){
        productRegistrationInfo.setIsGenerations("TestGenerations");
        assertEquals("TestGenerations", productRegistrationInfo.getIsGenerations());
    }

    public void test_getIsPrimaryUser(){
        productRegistrationInfo.setIsPrimaryUser("TestYes");
        assertEquals("TestYes",productRegistrationInfo.getIsPrimaryUser());
    }

    public void test_getLastSolicitDate(){
        productRegistrationInfo.setLastSolicitDate("TestDate");
        assertEquals("TestDate", productRegistrationInfo.getLastSolicitDate());
    }

    public void test_getProductCatalogLocaleID(){
        productRegistrationInfo.setProductCatalogLocaleID("TestCatalogLocaleID");
        assertEquals("TestCatalogLocaleID", productRegistrationInfo.getProductCatalogLocaleID());
    }

    public void test_getProductID(){
        productRegistrationInfo.setProductID("TestProductID");
        assertEquals("TestProductID", productRegistrationInfo.getProductID());

    }

    public void test_getProductModelNumber(){
        productRegistrationInfo.setProductModelNumber("TestProductModelNumber");
        assertEquals("TestProductModelNumber", productRegistrationInfo.getProductModelNumber());
    }

    public void test_getProductRegistrationID(){
        productRegistrationInfo.setProductRegistrationID("TestProductRegistrationID");
        assertEquals("TestProductRegistrationID",productRegistrationInfo.getProductRegistrationID());
    }

    public void test_getProductSerialNumber(){
        productRegistrationInfo.setProductSerialNumber("TestSerialNumber");
        assertEquals("TestSerialNumber", productRegistrationInfo.getProductSerialNumber());
    }

    public void test_getPurchaseDate(){
        productRegistrationInfo.setPurchaseDate("TestDate");
        assertEquals("TestDate", productRegistrationInfo.getPurchaseDate());
    }

    public void test_getPurchasePlace(){
        productRegistrationInfo.setPurchasePlace("TestPlace");
        assertEquals("TestPlace", productRegistrationInfo.getPurchasePlace());
    }

    public void test_getRegistrationChannel(){
        productRegistrationInfo.setRegistrationChannel("TestChannel");
        assertEquals("TestChannel", productRegistrationInfo.getRegistrationChannel());
    }

    public void test_getRegistrationDate(){
        productRegistrationInfo.setRegistrationDate("TestRegistrationDate");
        assertEquals("TestRegistrationDate", productRegistrationInfo.getRegistrationDate());
    }

    public void test_getSlashWinCompetition(){
        productRegistrationInfo.setSlashWinCompetition("TestSlashWinCompetition");
        assertEquals("TestSlashWinCompetition", productRegistrationInfo.getSlashWinCompetition());
    }

    public void test_getUserUUID(){
        productRegistrationInfo.setUserUUID("TestUUID");
        assertEquals("TestUUID",productRegistrationInfo.getUserUUID());
    }

    public void test_getWarrantyInMonths(){
        productRegistrationInfo.setWarrantyInMonths("12");
        assertEquals("12",productRegistrationInfo.getWarrantyInMonths());
    }

    public void test_getCtn(){
        productRegistrationInfo.setCtn("Test123");
        assertEquals("Test123",productRegistrationInfo.getCtn());
    }

    public void test_getSector(){

        productRegistrationInfo.setSector(Sector.B2B_HC);
        assertEquals(Sector.B2B_HC.name(),productRegistrationInfo.getSector().name());
    }

    public void test_getCatalog(){

        productRegistrationInfo.setCatalog(Catalog.CARE);
        assertEquals(Catalog.CARE.name(), productRegistrationInfo.getCatalog().name());
    }





}
