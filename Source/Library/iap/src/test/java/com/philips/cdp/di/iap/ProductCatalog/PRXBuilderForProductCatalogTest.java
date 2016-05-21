package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;

import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class PRXBuilderForProductCatalogTest {

    @Mock
    private Context context;
    @Mock
    private PRXBuilderForProductCatalog mPrxBuilderForProductCatalog;
    @Mock
    private ProductSummaryRequest mProductSummaryBuilder;
    @Mock
    private ProductsEntity mProductsEntity;


    @Before
    public void setUP() {
        mPrxBuilderForProductCatalog = Mockito.mock(PRXBuilderForProductCatalog.class);
    }
//final ProductsEntity entry, final String code, final ProductSummaryBuilder productSummaryBuilder
    @Test
    public void executeRequest(){
        mPrxBuilderForProductCatalog.executeRequest(mProductsEntity,"",mProductSummaryBuilder);
        verify(mPrxBuilderForProductCatalog).executeRequest(mProductsEntity,"",mProductSummaryBuilder);
    }

}