/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ProductCatalog;

import com.philips.cdp.di.iap.products.ProductCatalogData;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


@RunWith(RobolectricTestRunner.class)
public class ProductCatalogDataTest extends TestCase {

    private ProductCatalogData productCatalogData = new ProductCatalogData();

    @Test
    public void testGetTotalItems() {
        productCatalogData.setTotalItems(3);
        int totalItems = productCatalogData.getTotalItems();
        assertEquals(3, totalItems);
    }

    @Test
    public void testGetTotalPriceFormattedPrice() {
        productCatalogData.setFormattedTotalPrice("$1000");
        String totalPriceFormatted = productCatalogData.getFormattedTotalPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.equalsIgnoreCase("$1000"));
        assertFalse(totalPriceFormatted.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetFormattedPrice() {
        productCatalogData.setFormattedPrice("$1000");
        String totalPriceFormatted = productCatalogData.getFormattedPrice();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.equalsIgnoreCase("$1000"));
        assertFalse(totalPriceFormatted.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testGetTotalPriceWithTaxFormattedPrice() {
        productCatalogData.setFormattedTotalPriceWithTax("$1000");
        String totalPriceFormatted = productCatalogData.getFormattedTotalPriceWithTax();
        assertFalse(totalPriceFormatted.isEmpty());
        assertTrue(totalPriceFormatted.equalsIgnoreCase("$1000"));
        assertFalse(totalPriceFormatted.equalsIgnoreCase("$1345"));
    }

    @Test
    public void testImageURL() {
        productCatalogData.setImageUrl("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png");
        String imageUrl = productCatalogData.getImageURL();
        assertFalse(imageUrl.isEmpty());
        assertTrue(imageUrl.equalsIgnoreCase("https://images.philips.com/is/image/PhilipsConsumer/ALR002-AWR-en_US-001.png"));
        assertFalse(imageUrl.equalsIgnoreCase("google.com"));
        assertTrue(imageUrl.contains("png"));
        assertTrue(imageUrl.contains("images.philips.com"));
    }

    @Test
    public void testProductTitle() {
        productCatalogData.setProductTitle("DiamondClean");
        String productTitle = productCatalogData.getProductTitle();
        assertFalse(productTitle.isEmpty());
        assertTrue(productTitle.equalsIgnoreCase("DiamondClean"));
        assertFalse(productTitle.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testCTNNumber() {
        productCatalogData.setCtnNumber("HX4567/22");
        String ctn = productCatalogData.getCtnNumber();
        assertFalse(ctn.isEmpty());
        assertTrue(ctn.equalsIgnoreCase("HX4567/22"));
        assertFalse(ctn.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testMarketingTextHeader() {
        productCatalogData.setMarketingTextHeader("Market");
        String marketingTextHeader = productCatalogData.getMarketingTextHeader();
        assertFalse(marketingTextHeader.isEmpty());
        assertTrue(marketingTextHeader.equalsIgnoreCase("Market"));
        assertFalse(marketingTextHeader.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testPriceValue() {
        productCatalogData.setPriceValue("$100");
        String priceValue = productCatalogData.getPriceValue();
        assertFalse(priceValue.isEmpty());
        assertTrue(priceValue.equalsIgnoreCase("$100"));
        assertFalse(priceValue.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testDiscountedPrice() {
        productCatalogData.setDiscountedPrice("$100");
        String discountedPrice = productCatalogData.getDiscountedPrice();
        assertFalse(discountedPrice.isEmpty());
        assertTrue(discountedPrice.equalsIgnoreCase("$100"));
        assertFalse(discountedPrice.equalsIgnoreCase("google.com"));
    }

    @Test
    public void testGetStockLevel() {
        productCatalogData.setStockLevelStatus("inStock");
        assertEquals("inStock", productCatalogData.getStockLevelStatus());
    }
}
