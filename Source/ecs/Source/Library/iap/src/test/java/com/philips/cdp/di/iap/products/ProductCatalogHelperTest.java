/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.products.DiscountPriceEntity;
import com.philips.cdp.di.iap.response.products.PriceEntity;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.response.products.StockEntity;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogHelperTest {
    @Mock
    private SummaryModel mockSummaryModel;

    @Mock
    private Data mockData;

    @Mock
    private Message mockMessage;

    @Mock
    private PriceEntity mockPriceEntity;

    @Mock
    private IAPListener mockIapListener;

    @Mock
    private DiscountPriceEntity mockDiscountPriceEntity;

    @Mock
    private ProductCatalogPresenter.ProductCatalogListener mockProductCatalogListener;

    @Mock
    private AbstractModel.DataLoadListener mockDataLoadListener;

    @Mock
    private Products mockProducts;

    @Mock
    private ProductsEntity mockProductEntity;

    @Mock
    private StockEntity mockStockentity;

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private LoggingInterface loggingMock;

    private ProductCatalogHelper productCatalogHelper;

    private Context mContext;

    private HashMap<String, SummaryModel> mockHashMap = new HashMap<>();

    private List<ProductsEntity> productsEntityList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mContext = getInstrumentation().getContext();

        when(loggingMock.createInstanceForComponent(any(), any())).thenReturn(loggingMock);
        when(appInfraMock.getLogging()).thenReturn(loggingMock);
        CartModelContainer.getInstance().setAppInfraInstance(appInfraMock);

        productCatalogHelper = new ProductCatalogHelper(mContext, mockProductCatalogListener, mockDataLoadListener);

        when(mockProductEntity.getCode()).thenReturn("HX8332/11");
        when(mockDiscountPriceEntity.getFormattedValue()).thenReturn("12.23");
        when(mockPriceEntity.getFormattedValue()).thenReturn("23.3");
        when(mockProductEntity.getPrice()).thenReturn(mockPriceEntity);
        when(mockStockentity.getStockLevelStatus()).thenReturn("OutofStock");
        when(mockProductEntity.getStock()).thenReturn(mockStockentity);
        when(mockProductEntity.getDiscountPrice()).thenReturn(mockDiscountPriceEntity);

        productsEntityList.add(mockProductEntity);
        when(mockProducts.getProducts()).thenReturn(productsEntityList);
        when(mockData.getCtn()).thenReturn("HX8332/11");
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

    @Test
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
        when(mockSummaryModel.getData()).thenReturn(mockData);
        mockHashMap.put("HX8332/11", mockSummaryModel);
        mockMessage.obj = mockHashMap;
        productCatalogHelper.processPRXResponse(mockMessage, mockProducts, mockIapListener);
    }

    @Test
    public void test_processPRXResponseWithDifferentHashMapData() throws Exception {
        when(mockSummaryModel.getData()).thenReturn(mockData);
        mockHashMap.put("HX8332/12", mockSummaryModel);
        mockMessage.obj = mockHashMap;
        productCatalogHelper.processPRXResponse(mockMessage, mockProducts, mockIapListener);
    }
}