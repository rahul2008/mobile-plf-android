/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.shoppingcart;

import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.response.carts.DeliveryAddressEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryModeEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ShoppingCartDataTest extends TestCase {
    private ShoppingCartData shoppingCartData = new ShoppingCartData();

    @Test
    public void testGetVATValue() {
        shoppingCartData.setVatValue("$13");
        String vatValue = shoppingCartData.getVatValue();
        assertFalse(vatValue.isEmpty());
        assertTrue(vatValue.equalsIgnoreCase("$13"));
        assertFalse(vatValue.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalPriceFormattedPrice() {
        shoppingCartData.setFormattedTotalPrice("$500");
        String totalPriceFormatted = shoppingCartData.getFormattedTotalPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.equalsIgnoreCase("$500"));
        assertFalse(totalPriceFormatted.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetFormattedPrice() {
        shoppingCartData.setFormattedPrice("$200");
        String priceFormatted = shoppingCartData.getFormattedPrice();
        assertFalse(priceFormatted.isEmpty());
        assertTrue(priceFormatted.equalsIgnoreCase("$200"));
        assertFalse(priceFormatted.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalPriceWithTaxFormatted() {
        shoppingCartData.setFormattedTotalPriceWithTax("$400");
        String totalPriceWithTaxFormattedPrice = shoppingCartData.getFormattedTotalPriceWithTax();
        assertFalse(totalPriceWithTaxFormattedPrice.isEmpty());
        assertTrue(totalPriceWithTaxFormattedPrice.equalsIgnoreCase("$400"));
        assertFalse(totalPriceWithTaxFormattedPrice.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalItems() {
        shoppingCartData.setTotalItems(3);
        int totalItems = shoppingCartData.getTotalItems();
        assertFalse(totalItems == 0);
        assertTrue(totalItems == 3);
    }

    @Test
    public void testGetQuantity() {
        shoppingCartData.setQuantity(4);
        int totalItems = shoppingCartData.getQuantity();
        assertTrue(totalItems == 4);
    }

    @Test
    public void testGetDeliveryItemsQuantity() {
        shoppingCartData.setDeliveryItemsQuantity(4);
        int deliveryItemsQuantity = shoppingCartData.getQuantity();
        assertEquals(deliveryItemsQuantity, 0);
        assertFalse(deliveryItemsQuantity == 6);
    }

    @Test
    public void testGetImageURL() {
        shoppingCartData.setImageUrl("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png");
        String imageUrl = shoppingCartData.getImageURL();
        assertFalse(imageUrl.isEmpty());
        assertTrue(imageUrl.equalsIgnoreCase("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png"));
        assertFalse(imageUrl.equalsIgnoreCase("google.com"));
        assertTrue(imageUrl.contains("png"));
        assertTrue(imageUrl.contains("images.philips.com"));
    }

    @Test
    public void testGetProductTitle() {
        shoppingCartData.setProductTitle("DiamondClean");
        String productTitle = shoppingCartData.getProductTitle();
        assertFalse(productTitle.isEmpty());
        assertTrue(productTitle.equalsIgnoreCase("DiamondClean"));
        assertFalse(productTitle.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetDeliveryMode() {
        DeliveryModeEntity deliveryMode = shoppingCartData.getDeliveryMode();
        assertFalse(deliveryMode != null);
    }

    @Test
    public void testGetDeliveryAddressEntity() {
        DeliveryAddressEntity deliveryAddressEntity = shoppingCartData.getDeliveryAddressEntity();
        assertFalse(deliveryAddressEntity != null);
    }

    @Test
    public void testGetMarketingTextHeader() {
        shoppingCartData.setMarketingTextHeader("Hello");
        String marketingTextHeader = shoppingCartData.getMarketingTextHeader();
        assertFalse(marketingTextHeader.isEmpty());
        assertTrue(marketingTextHeader.equalsIgnoreCase("Hello"));
        assertFalse(marketingTextHeader.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetValuePrice() {
        shoppingCartData.setValuePrice("$567");
        String valuePrice = shoppingCartData.getValuePrice();
        assertFalse(valuePrice.isEmpty());
        assertTrue(valuePrice.equalsIgnoreCase("$567"));
        assertFalse(valuePrice.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetCategory() {
        shoppingCartData.setCategory("Tuscany");
        String category = shoppingCartData.getCategory();
        assertFalse(category.isEmpty());
        assertTrue(category.equalsIgnoreCase("Tuscany"));
        assertFalse(category.equalsIgnoreCase("googles.com"));
    }

    @Test
    public void testIsVatInclusive() {
        shoppingCartData.setVatInclusive(true);
        boolean isVatInclusive = shoppingCartData.isVatInclusive();
        assertTrue(isVatInclusive);
    }

    @Test
    public void testVatActualValue() {
        shoppingCartData.setVatActualValue("12");
        assertEquals(shoppingCartData.getVatActualValue(), "12");
    }

    @Test
    public void testDeliveryItemsQuantity(){
        shoppingCartData.setDeliveryItemsQuantity(2);
        assertEquals(shoppingCartData.getDeliveryItemsQuantity(), 2);
    }

    @Test
    public void testEntryNumber(){
        ShoppingCartData mShoppingData = new ShoppingCartData(new EntriesEntity(), null);
        mShoppingData.setEntryNumber(2);
        mShoppingData.getEntryNumber();
    }

    @Test(expected = NullPointerException.class)
    public void testSetStockLevel(){
        ShoppingCartData mShoppingData = new ShoppingCartData(new EntriesEntity(), null);
        mShoppingData.setStockLevel(2);
        mShoppingData.getStockLevel();
    }

    @Test
    public void testToString(){
        shoppingCartData.toString();
    }
}
