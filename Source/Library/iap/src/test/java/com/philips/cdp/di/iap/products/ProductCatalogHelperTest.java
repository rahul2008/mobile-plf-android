/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.products.DiscountPriceEntity;
import com.philips.cdp.di.iap.response.products.PriceEntity;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.response.products.StockEntity;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogHelperTest {
    private ProductCatalogHelper productCatalogHelper;

    private HashMap<String, SummaryModel> mockHashMap = new HashMap<>();
    @Mock
    SummaryModel mockSummaryModel;
    @Mock
    Data mockData;
    @Mock
    Message mockMessage;
    @Mock
    PriceEntity mockPriceEntity;
    @Mock
    IAPListener mockIapListener;
    @Mock
    DiscountPriceEntity mockDiscountPriceEntity;
    @Mock
    Context mContext;
    @Mock
    ProductCatalogPresenter.ProductCatalogListener mockProductCatalogListener;
    @Mock
    AbstractModel.DataLoadListener mockDataLoadListener;

    List<ProductsEntity> productsEntityList = new ArrayList<>();
    @Mock
    Products mockProducts;
    @Mock
    private ProductsEntity mockProductEntity;
    @Mock
    private StockEntity mockStockentity;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        productCatalogHelper = new ProductCatalogHelper(mContext, mockProductCatalogListener, mockDataLoadListener);
        Mockito.when(mockProductEntity.getCode()).thenReturn("HX8332/11");
        Mockito.when(mockDiscountPriceEntity.getFormattedValue()).thenReturn("12.23");
        Mockito.when(mockPriceEntity.getFormattedValue()).thenReturn("23.3");
        Mockito.when(mockProductEntity.getPrice()).thenReturn(mockPriceEntity);
        Mockito.when(mockStockentity.getStockLevelStatus()).thenReturn("OutofStock");
        Mockito.when(mockProductEntity.getStock()).thenReturn(mockStockentity);
        Mockito.when(mockProductEntity.getDiscountPrice()).thenReturn(mockDiscountPriceEntity);
        productsEntityList.add(mockProductEntity);
        Mockito.when(mockProducts.getProducts()).thenReturn(productsEntityList);
        Mockito.when(mockData.getCtn()).thenReturn("HX8332/11");
    }

    @Test
    public void testProductCtn() {
        ArrayList<ProductCatalogData> data = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductCatalogData catalogData = new ProductCatalogData();
            if (i == 0)
                catalogData.setCtnNumber("HX8071/10");
            if (i == 1)
                catalogData.setCtnNumber("HX8331/11");
            data.add(i, catalogData);
        }

        assertEquals(2, new ProductCatalogHelper(mContext, mockProductCatalogListener, mockDataLoadListener).getProductCTNs(data).size());
    }

    @Test(expected = NullPointerException.class)
    public void test_sendPRXRequest() throws Exception {
        productCatalogHelper.sendPRXRequest(mockProducts);

    }

    @Test
    public void test_processPRXResponse() throws Exception {
        mockMessage.obj = HashMap.class;
        productCatalogHelper.processPRXResponse(mockMessage, mockProducts, mockIapListener);
    }

    @Test
    public void test_processPRXResponseWithExactHashMap() throws Exception {
        mockMessage.obj = mockHashMap;
        productCatalogHelper.processPRXResponse(mockMessage, mockProducts, mockIapListener);
    }

    @Test
    public void test_processPRXResponseWithExactHashMapData() throws Exception {
        Mockito.when(mockSummaryModel.getData()).thenReturn(mockData);
        mockHashMap.put("HX8332/11", mockSummaryModel);
        mockMessage.obj = mockHashMap;
        productCatalogHelper.processPRXResponse(mockMessage, mockProducts, mockIapListener);
    }

    @Test
    public void test_processPRXResponseWithDifferentHashMapData() throws Exception {
        Mockito.when(mockSummaryModel.getData()).thenReturn(mockData);
        mockHashMap.put("HX8332/12", mockSummaryModel);
        mockMessage.obj = mockHashMap;
        productCatalogHelper.processPRXResponse(mockMessage, mockProducts, mockIapListener);
    }
}