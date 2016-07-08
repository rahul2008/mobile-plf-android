/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.shoppingcart;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.response.carts.DeliveryAddressEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryModeEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.carts.ProductEntity;
import com.philips.cdp.di.iap.response.carts.StockEntity;

import junit.framework.TestCase;

import org.junit.Test;

import java.lang.reflect.Field;

public class ShoppingCartDataTest  extends TestCase {
    private ShoppingCartData shoppingCartData = new ShoppingCartData();

    @Test
    public void testGetVATValue() {
        shoppingCartData.setVatValue("$13");
        String vatValue = shoppingCartData.getVatValue();
        assertFalse(vatValue.isEmpty());
        assertTrue(vatValue.toString().equalsIgnoreCase("$13"));
        assertFalse(vatValue.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalPriceFormatedPrice() {
        shoppingCartData.setTotalPriceFormatedPrice("$500");
        String totalPriceFormatted = shoppingCartData.getTotalPriceFormatedPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.toString().equalsIgnoreCase("$500"));
        assertFalse(totalPriceFormatted.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetFormatedPrice() {
        shoppingCartData.setFormatedPrice("$200");
        String priceFormatted = shoppingCartData.getFormatedPrice();
        assertFalse(priceFormatted.isEmpty());
        assertTrue(priceFormatted.toString().equalsIgnoreCase("$200"));
        assertFalse(priceFormatted.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalPriceWithTaxFormatted() {
        shoppingCartData.setTotalPriceWithTaxFormatedPrice("$400");
        String totalPriceWithTaxFormatedPrice = shoppingCartData.getTotalPriceWithTaxFormatedPrice();
        assertFalse(totalPriceWithTaxFormatedPrice.isEmpty());
        assertTrue(totalPriceWithTaxFormatedPrice.toString().equalsIgnoreCase("$400"));
        assertFalse(totalPriceWithTaxFormatedPrice.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalItems() {
        shoppingCartData.setTotalItems(3);
        int totalItems = shoppingCartData.getTotalItems();
        assertFalse(totalItems == 0);
        assertTrue(totalItems == 3);
        assertFalse(totalItems == 6);
    }

    @Test
    public void testGetQuantity() {
        shoppingCartData.setQuantity(4);
        int totalItems = shoppingCartData.getQuantity();
        assertTrue(totalItems == 4);
        assertFalse(totalItems == 6);
    }

    @Test
    public void testGetDeliveryItemsQuantity() {
        shoppingCartData.setDeliveryItemsQuantity(4);
        int deliveryItemsQuantity = shoppingCartData.getQuantity();
        assertEquals(deliveryItemsQuantity,0);
        assertFalse(deliveryItemsQuantity == 6);
    }

    @Test
    public void testGetImageURL() {
        shoppingCartData.setImageUrl("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png");
        String imageUrl = shoppingCartData.getImageURL();
        assertFalse(imageUrl.isEmpty());
        assertFalse(imageUrl == null);
        assertTrue(imageUrl.toString().equalsIgnoreCase("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png"));
        assertFalse(imageUrl.toString().equalsIgnoreCase("google.com"));
        assertTrue(imageUrl.toString().contains("png"));
        assertTrue(imageUrl.toString().contains("images.philips.com"));
    }

    @Test
    public void testGetProductTitle() {
        shoppingCartData.setProductTitle("DiamondClean");
        String productTitle = shoppingCartData.getProductTitle();
        assertFalse(productTitle.isEmpty());
        assertFalse(productTitle == null);
        assertTrue(productTitle.toString().equalsIgnoreCase("DiamondClean"));
        assertFalse(productTitle.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetCartNumber() {
        shoppingCartData.setCartNumber("14000107129");
        String cartNumber = shoppingCartData.getCartNumber();
        assertFalse(cartNumber.isEmpty());
        assertFalse(cartNumber == null);
        assertTrue(cartNumber.toString().equalsIgnoreCase("14000107129"));
        assertFalse(cartNumber.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetDeliveryMode() {
        DeliveryModeEntity deliveryMode = shoppingCartData.getDeliveryMode();
        assert(deliveryMode == null);
    }

    @Test
    public void testGetDeliveryAddressEntitiy() {
        DeliveryAddressEntity deliveryAddressEntity = shoppingCartData.getDeliveryAddressEntity();
        assert(deliveryAddressEntity == null);
    }

    @Test
    public void testGetMarkettingTextHeader() {
        shoppingCartData.setMarketingTextHeader("Hello");
        String markettingTextHeader = shoppingCartData.getMarketingTextHeader();
        assertFalse(markettingTextHeader.isEmpty());
        assertFalse(markettingTextHeader == null);
        assertTrue(markettingTextHeader.toString().equalsIgnoreCase("Hello"));
        assertFalse(markettingTextHeader.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetValuePrice() {
        shoppingCartData.setValuePrice("$567");
        String valuePrice = shoppingCartData.getValuePrice();
        assertFalse(valuePrice.isEmpty());
        assertFalse(valuePrice == null);
        assertTrue(valuePrice.toString().equalsIgnoreCase("$567"));
        assertFalse(valuePrice.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetCategory() {
        shoppingCartData.setCategory("Tuscany");
        String category = shoppingCartData.getCategory();
        assertFalse(category.isEmpty());
        assertFalse(category == null);
        assertTrue(category.toString().equalsIgnoreCase("Tuscany"));
        assertFalse(category.toString().equalsIgnoreCase("googles.com"));
    }

    @Test
    public void testIsVatInclusive() {
        shoppingCartData.setVatInclusive(true);
        boolean isVatInclusive = shoppingCartData.isVatInclusive();
        assert(isVatInclusive);
    }

    /*@Test
    public void testSetStockLevel() throws NoSuchFieldException, IllegalAccessException {
        //mEntry.getProduct().getStock().getStockLevel();
        EntriesEntity entriesEntityObject = new EntriesEntity();
        ProductEntity product = new ProductEntity();
        StockEntity stock = new StockEntity();

        int stockLevel1 = stock.getStockLevel();

        ShoppingCartData shoppingCartData = new ShoppingCartData(, null);
        Field entriesEntity = shoppingCartData.getClass().getDeclaredField("mEntry");


        shoppingCartData.setStockLevel(3);
        int stockLevel = shoppingCartData.getStockLevel();
        assertTrue(stockLevel == 3);
        assertFalse(stockLevel == 2);
    }*/

    /*@Test
    public void testSetEntryNumber() {
        shoppingCartData.setEntryNumber(3);
        int entryNumber = shoppingCartData.getStockLevel();
        assertTrue(entryNumber == 3);
        assertFalse(entryNumber == 2);
    }*/

    @Test
    public void testtoString() {
        assert(shoppingCartData.toString() instanceof String);
    }

}
