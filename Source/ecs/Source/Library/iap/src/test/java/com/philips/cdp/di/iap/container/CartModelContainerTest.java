/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.container;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.products.ProductCatalogData;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class CartModelContainerTest extends TestCase {

    @Test
    public void testDeliveryModes() {
        CartModelContainer.getInstance().setDeliveryModes(new ArrayList<DeliveryModes>());
        List<DeliveryModes> deliveryModeList = CartModelContainer.getInstance().getDeliveryModes();
        assertEquals(0, deliveryModeList.size());
    }

    @Test
    public void testOrderNumber() {
        CartModelContainer.getInstance().setOrderNumber("12345");
        assertEquals("12345", CartModelContainer.getInstance().getOrderNumber());
    }

    @Test
    public void testLanguage() {
        CartModelContainer.getInstance().setLanguage("en");
        assertEquals("en", CartModelContainer.getInstance().getLanguage());
    }

    @Test
    public void testShippingAddressFields() {
        AddressFields addressFields = new AddressFields();
        addressFields.setTitleCode("Mr");
        addressFields.setFirstName("Happy");
        addressFields.setLastName("User");
        addressFields.setCountryIsocode("US");
        addressFields.setRegionIsoCode("US-CA");
        addressFields.setLine1("Line1");
        addressFields.setPostalCode("92821");
        addressFields.setTown("California");
        addressFields.setPhone1("+1877-682-8207");
        addressFields.setPhone2("+1877-682-8207");
        CartModelContainer.getInstance().setShippingAddressFields(addressFields);
        assertEquals("US", CartModelContainer.getInstance().getShippingAddressFields().getCountryIsocode());
    }

    @Test
    public void testRegionList() {
        RegionsList regionsList = new RegionsList();
        CartModelContainer.getInstance().setRegionList(regionsList);
        assertNotNull(CartModelContainer.getInstance().getRegionList());
    }

    @Test
    public void testProductAsset() {
        CartModelContainer.getInstance().addProductAsset("HX8071/10", new ArrayList<String>());
        assertTrue(CartModelContainer.getInstance().isPRXAssetPresent("HX8071/10"));
        assertNotNull(CartModelContainer.getInstance().getPRXAssetList());
    }

    @Test
    public void testProductList() {
        CartModelContainer.getInstance().addProduct("HX8331/12", new ProductCatalogData());
        assertNotNull(CartModelContainer.getInstance().getProduct("HX8331/12"));
        assertNull(CartModelContainer.getInstance().getProduct("HX9042/64"));
    }

    @Test
    public void testResetFields(){
        CartModelContainer.getInstance().resetApplicationFields();
    }
}