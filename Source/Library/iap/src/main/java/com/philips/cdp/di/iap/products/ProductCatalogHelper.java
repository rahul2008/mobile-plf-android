/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.prx.PRXSummaryExecutor;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCatalogHelper {
    Context mContext;
    ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener;
    AbstractModel.DataLoadListener mGetProductCatalogListener;

    public ProductCatalogHelper(Context context, ProductCatalogPresenter.ProductCatalogListener productCatalogListener,
                                AbstractModel.DataLoadListener productListener) {
        mContext = context;
        mProductCatalogListener = productCatalogListener;
        mGetProductCatalogListener = productListener;
    }

    public void sendPRXRequest(ArrayList<String> planBProductList, Products productData) {
        ArrayList<String> productsToBeShown = new ArrayList<>();
        String ctn;

        if (planBProductList != null && productData == null) {
            for (String product : planBProductList) {
                ctn = product;
                productsToBeShown.add(ctn);
            }
        } else {
            if (productData != null) {
                final List<ProductsEntity> productsEntities = productData.getProducts();
                if (productsEntities != null)
                    for (ProductsEntity entry : productsEntities) {
                        ctn = entry.getCode();
                        productsToBeShown.add(ctn);
                    }
            }
        }

        PRXSummaryExecutor builder = new PRXSummaryExecutor(mContext, productsToBeShown,
                mGetProductCatalogListener);
        builder.preparePRXDataRequest();
    }

    @SuppressWarnings("unchecked")
    public boolean processPRXResponse(final Message msg, ArrayList<String> planBProductCTNs,
                                      Products productData, IAPListener listener) {
        if (msg.obj instanceof HashMap) {
            HashMap<String, SummaryModel> prxModel = (HashMap<String, SummaryModel>) msg.obj;

            if (checkForEmptyCart(prxModel))
                return true;

            ArrayList<ProductCatalogData> products = mergeResponsesFromHybrisAndPRX(planBProductCTNs, productData, prxModel);
            PaginationEntity pagination = null;
            if (productData != null && products.size() != 0)
                pagination = productData.getPagination();
            refreshList(products, pagination, listener);

        } else {
            notifyEmptyCartFragment();
        }
        return false;
    }

    private ArrayList<ProductCatalogData> mergeResponsesFromHybrisAndPRX(ArrayList<String> planBProductList,
                                                                         final Products productData,
                                                                         final HashMap<String, SummaryModel> prxModel) {
        if (planBProductList != null) {
            return mergeHybrisAndPRXPlanB(planBProductList, prxModel);
        } else {
            return mergeHybrisAndPRXPlanA(productData, prxModel);
        }
    }

    private ArrayList<ProductCatalogData> mergeHybrisAndPRXPlanB(ArrayList<String> planBProductList,
                                                                 HashMap<String, SummaryModel> prxModel) {
        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXSummaryList();
        ArrayList<ProductCatalogData> products = new ArrayList<>();
        String ctn;
        for (String planBProduct : planBProductList) {
            ctn = planBProduct;
            ProductCatalogData productItem = new ProductCatalogData();
            Data data;
            if (prxModel.containsKey(ctn)) {
                data = prxModel.get(ctn).getData();
            } else if (list.containsKey(ctn)) {
                data = list.get(ctn).getData();
            } else {
                continue;
            }
            productItem.setImageUrl(data.getImageURL());
            productItem.setProductTitle(data.getProductTitle());
            productItem.setCtnNumber(ctn);
            productItem.setMarketingTextHeader(data.getMarketingTextHeader());
            products.add(productItem);
        }
        return products;
    }

    private ArrayList<ProductCatalogData> mergeHybrisAndPRXPlanA(Products productData,
                                                                 HashMap<String, SummaryModel> prxModel) {
        List<ProductsEntity> entries = productData.getProducts();
        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXSummaryList();
        ArrayList<ProductCatalogData> products = new ArrayList<>();
        String ctn;
        if (entries != null)
            for (ProductsEntity entry : entries) {
                ctn = entry.getCode();
                ProductCatalogData productItem = new ProductCatalogData();
                Data data;
                if (prxModel.containsKey(ctn)) {
                    data = prxModel.get(ctn).getData();
                } else if (list.containsKey(ctn)) {
                    data = list.get(ctn).getData();
                } else {
                    continue;
                }
                productItem.setImageUrl(data.getImageURL());
                productItem.setProductTitle(data.getProductTitle());
                productItem.setCtnNumber(ctn);
                productItem.setMarketingTextHeader(data.getMarketingTextHeader());
                fillEntryBaseData(entry, productItem);
                products.add(productItem);
            }
        return products;
    }

    private void fillEntryBaseData(final ProductsEntity entry, final ProductCatalogData productItem) {
        if (entry.getPrice() == null || entry.getDiscountPrice() == null)
            return;
        productItem.setFormattedPrice(entry.getPrice().getFormattedValue());
        productItem.setPriceValue(String.valueOf(entry.getPrice().getValue()));
        if (entry.getDiscountPrice() != null && entry.getDiscountPrice().getFormattedValue() != null
                && !entry.getDiscountPrice().getFormattedValue().isEmpty()) {
            productItem.setDiscountedPrice(entry.getDiscountPrice().getFormattedValue());
        }
    }

    private void notifyEmptyCartFragment() {
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    private boolean checkForEmptyCart(final HashMap<String, SummaryModel> prxModel) {
        if (prxModel == null || prxModel.size() == 0) {
            notifyEmptyCartFragment();
            return true;
        }
        return false;
    }

    public void refreshList(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity, IAPListener listener) {
        if (mProductCatalogListener != null) {
            mProductCatalogListener.onLoadFinished(data, paginationEntity);
        }
        if (listener != null) {
            listener.onGetCompleteProductList(getProductCTNs(data));
        }
        storeData(data);
    }

    private ArrayList<String> getProductCTNs(final ArrayList<ProductCatalogData> data) {
        ArrayList<String> ctns = new ArrayList<>();
        for (ProductCatalogData entry : data) {
            ctns.add(entry.getCtnNumber());
        }
        return ctns;
    }

    private void storeData(final ArrayList<ProductCatalogData> data) {
        CartModelContainer container = CartModelContainer.getInstance();

        String currentCountry = container.getCountry();
        String CTN;
        for (ProductCatalogData entry : data) {
            CTN = entry.getCtnNumber();
            if (currentCountry.equalsIgnoreCase(Utility.getCountryFromPreferenceForKey(mContext, IAPConstant.IAP_COUNTRY_KEY))) {
                if (!container.isProductCatalogDataPresent(CTN)) {
                    container.addProduct(CTN, entry);
                }
            } else {
                CartModelContainer.getInstance().clearCategorisedProductList();
            }
        }
        Utility.addCountryInPreference(mContext, IAPConstant.IAP_COUNTRY_KEY, container.getCountry());
    }
}
