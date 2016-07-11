package com.philips.cdp.di.iap.ProductCatalog;

import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class ProductCatalogDataTest extends TestCase{

    private ProductCatalogData productCatalogData = new ProductCatalogData();

    @Test
    public void testGetTotalItems() {
        productCatalogData.setTotalItems(3);
        int totalItems = productCatalogData.getTotalItems();
        assertEquals(3,totalItems);
        assertFalse(totalItems==0);
        assertFalse(totalItems==5);
    }

    @Test
    public void testGetTotalPriceFormattedPrice() {
       productCatalogData.setTotalPriceFormatedPrice("$1000");
        String totalPriceFormatted = productCatalogData.getTotalPriceFormatedPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.toString().equalsIgnoreCase("$1000"));
        assertFalse(totalPriceFormatted.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetFormattedPrice() {
        productCatalogData.setFormatedPrice("$1000");
        String totalPriceFormatted = productCatalogData.getFormatedPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.toString().equalsIgnoreCase("$1000"));
        assertFalse(totalPriceFormatted.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalPriceWithTaxFormattedPrice() {
        productCatalogData.setTotalPriceWithTaxFormatedPrice("$1000");
        String totalPriceFormatted = productCatalogData.getTotalPriceWithTaxFormatedPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.toString().equalsIgnoreCase("$1000"));
        assertFalse(totalPriceFormatted.toString().equalsIgnoreCase("$1345"));
    }

    @Test
    public void testImageURL() {
        productCatalogData.setImageUrl("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png");
        String imageUrl = productCatalogData.getImageURL();
        assertFalse(imageUrl.isEmpty());
        assertFalse(imageUrl == null);
        assertTrue(imageUrl.toString().equalsIgnoreCase("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png"));
        assertFalse(imageUrl.toString().equalsIgnoreCase("google.com"));
        assertTrue(imageUrl.toString().contains("png"));
        assertTrue(imageUrl.toString().contains("images.philips.com"));
    }

    @Test
    public void testProductTitle() {
        productCatalogData.setProductTitle("DiamondClean");
        String productTitle = productCatalogData.getProductTitle();
        assertFalse(productTitle.isEmpty());
        assertFalse(productTitle == null);
        assertTrue(productTitle.toString().equalsIgnoreCase("DiamondClean"));
        assertFalse(productTitle.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testCTNNumber() {
        productCatalogData.setCtnNumber("HX4567/22");
        String ctn = productCatalogData.getCtnNumber();
        assertFalse(ctn.isEmpty());
        assertFalse(ctn == null);
        assertTrue(ctn.toString().equalsIgnoreCase("HX4567/22"));
        assertFalse(ctn.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testMarkettingTextHeader() {
        productCatalogData.setMarketingTextHeader("Market");
        String marketingTextHeader = productCatalogData.getMarketingTextHeader();
        assertFalse(marketingTextHeader.isEmpty());
        assertFalse(marketingTextHeader == null);
        assertTrue(marketingTextHeader.toString().equalsIgnoreCase("Market"));
        assertFalse(marketingTextHeader.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testPriceValue() {
        productCatalogData.setPriceValue("$100");
        String priceValue = productCatalogData.getPriceValue();
        assertFalse(priceValue.isEmpty());
        assertFalse(priceValue == null);
        assertTrue(priceValue.toString().equalsIgnoreCase("$100"));
        assertFalse(priceValue.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testDiscountedPrice() {
        productCatalogData.setDiscountedPrice("$100");
        String discoutedPrice = productCatalogData.getDiscountedPrice();
        assertFalse(discoutedPrice.isEmpty());
        assertFalse(discoutedPrice == null);
        assertTrue(discoutedPrice.toString().equalsIgnoreCase("$100"));
        assertFalse(discoutedPrice.toString().equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetStockLevel() {
        productCatalogData.setStockLevel(4);
    }
}
