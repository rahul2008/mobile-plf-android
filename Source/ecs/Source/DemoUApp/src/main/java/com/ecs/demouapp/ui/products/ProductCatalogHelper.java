/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.products;

import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.eventhelper.EventHelper;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.prx.PRXSummaryListExecutor;
import com.ecs.demouapp.ui.utils.ECSConstant;
import com.philips.cdp.di.ecs.model.products.PaginationEntity;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.summary.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCatalogHelper {
    private Context mContext;
    private ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener;
    private AbstractModel.DataLoadListener mGetProductCatalogListener;

    public ProductCatalogHelper(Context context, ProductCatalogPresenter.ProductCatalogListener productCatalogListener,
                                AbstractModel.DataLoadListener productListener) {
        mContext = context;
        mProductCatalogListener = productCatalogListener;
        mGetProductCatalogListener = productListener;
    }

    public void sendPRXRequest(Products productData) {
        ArrayList<String> productsToBeShown = new ArrayList<>();
        String ctn;

        if (productData != null) {
            final List<Product> productsEntities = productData.getProducts();
            if (productsEntities != null)
                for (Product entry : productsEntities) {
                    ctn = entry.getCode();
                    productsToBeShown.add(ctn);
                }
        }
        PRXSummaryListExecutor builder = new PRXSummaryListExecutor(mContext, productsToBeShown,
                mGetProductCatalogListener);

        builder.preparePRXDataRequest();
    }

    @SuppressWarnings("unchecked")
    public boolean processPRXResponse(Products productData, ECSListener listener) {


            ArrayList<ProductCatalogData> products = mergeHybrisAndPRX(productData);
            PaginationEntity pagination = null;
            if (productData != null && products.size() != 0)
                pagination = productData.getPagination();
            refreshList(products, pagination, listener);

        return false;
    }

    private ArrayList<ProductCatalogData> mergeHybrisAndPRX(Products productData) {
        List<Product> entries = productData.getProducts();
        ArrayList<ProductCatalogData> products = new ArrayList<>();
        String ctn;
        if (entries != null)
            for (Product entry : entries) {
                ctn = entry.getCode();
                ProductCatalogData productItem = new ProductCatalogData();

                productItem.setProduct(entry);

                Data data = entry.getSummary();

                if(data!=null) {
                    productItem.setImageUrl(data.getImageURL());
                    productItem.setProductTitle(data.getProductTitle());
                    productItem.setCtnNumber(ctn);
                    productItem.setMarketingTextHeader(data.getMarketingTextHeader());
                }
                fillEntryBaseData(entry, productItem);
                products.add(productItem);
            }
        return products;
    }

    private void fillEntryBaseData(final Product entry, final ProductCatalogData productItem) {
        if (entry.getPrice() == null || entry.getDiscountPrice() == null)
            return;
        productItem.setFormattedPrice(entry.getPrice().getFormattedValue());
        productItem.setPriceValue(String.valueOf(entry.getPrice().getValue()));
        if (entry.getDiscountPrice() != null && entry.getDiscountPrice().getFormattedValue() != null
                && !entry.getDiscountPrice().getFormattedValue().isEmpty()) {
            productItem.setDiscountedPrice(entry.getDiscountPrice().getFormattedValue());
            productItem.setStockLevelStatus(entry.getStock().getStockLevelStatus());
            productItem.setStockLevel(entry.getStock().getStockLevel());
        }
    }

    private void notifyEmptyCartFragment() {
        EventHelper.getInstance().notifyEventOccurred(ECSConstant.EMPTY_CART_FRAGMENT_REPLACED);
    }

    private boolean checkForEmptyCart(final HashMap<String, Data> prxModel) {
        if (prxModel == null || prxModel.size() == 0) {
            notifyEmptyCartFragment();
            return true;
        }
        return false;
    }

    public void refreshList(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity, ECSListener listener) {
        storeData(data);
        if (mProductCatalogListener != null) {
            mProductCatalogListener.onLoadFinished(data, paginationEntity);
        }
        if (listener != null) {
            listener.onGetCompleteProductList(getProductCTNs(data));
        }
    }

    protected ArrayList<String> getProductCTNs(final ArrayList<ProductCatalogData> data) {
        ArrayList<String> productCodes = new ArrayList<>();
        for (ProductCatalogData entry : data) {
            productCodes.add(entry.getCtnNumber());
        }
        return productCodes;
    }

    private void storeData(final ArrayList<ProductCatalogData> data) {
        CartModelContainer container = CartModelContainer.getInstance();
        if (data == null) return;

        String CTN;
        for (ProductCatalogData entry : data) {
            CTN = entry.getCtnNumber();
                if (CTN != null ) {
                    if (!container.isProductCatalogDataPresent(CTN)) {
                        container.addProduct(CTN, entry);
                    }
                } else {
                    CartModelContainer.getInstance().clearCategorisedProductList();
                }
        }
    }
}
