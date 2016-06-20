package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.prx.PRXDataBuilder;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductCatalogHelper {
    Context mContext;
    ProductCatalogPresenter.LoadListener mLoadListener;
    AbstractModel.DataLoadListener mGetProductCatalogListener;

    public ProductCatalogHelper(Context context, ProductCatalogPresenter.LoadListener listener, AbstractModel.DataLoadListener productlistener) {
        mContext = context;
        mLoadListener = listener;
        mGetProductCatalogListener = productlistener;
    }

    public boolean processPRXResponse(final Message msg, ArrayList<String> planBProductCTNs, Products productData) {
        if (msg.obj instanceof HashMap) {
            HashMap<String, SummaryModel> prxModel = (HashMap<String, SummaryModel>) msg.obj;

            if (checkForEmptyCart(prxModel))
                return true;

            ArrayList<ProductCatalogData> products = mergeResponsesFromHybrisAndPRX(planBProductCTNs, productData, prxModel);
            refreshList(products);

        } else {
            notifyEmptyCartFragment();
        }
        return false;
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

    public void makePrxCall(ArrayList<String> planBProductList, Products productData, boolean isPlanB) {
        ArrayList<String> ctnsToBeRequestedForPRX = new ArrayList<>();
        ArrayList<String> productsToBeShown = new ArrayList<>();
        String ctn;
        if (isPlanB) {
            for (String product : planBProductList) {
                ctn = product;
                productsToBeShown.add(ctn);
                if (!CartModelContainer.getInstance().isPRXDataPresent(ctn)) {
                    ctnsToBeRequestedForPRX.add(ctn);
                }
            }
        } else {
            if (productData != null) {
                final List<ProductsEntity> productsEntities = productData.getProducts();
                for (ProductsEntity entry : productsEntities) {
                    ctn = entry.getCode();
                    productsToBeShown.add(ctn);
                    if (!CartModelContainer.getInstance().isPRXDataPresent(ctn)) {
                        ctnsToBeRequestedForPRX.add(entry.getCode());
                    }
                }
            }
        }
        prxRequest(planBProductList, productData, ctnsToBeRequestedForPRX, productsToBeShown);
    }

    private void prxRequest(ArrayList<String> planBProductList, Products productData, ArrayList<String> ctnsToBeRequestedForPRX, ArrayList<String> productsToBeShown) {
        if (ctnsToBeRequestedForPRX.size() > 0) {
            PRXDataBuilder builder = new PRXDataBuilder(mContext, productsToBeShown,
                    mGetProductCatalogListener);
            builder.preparePRXDataRequest();
        } else {
            HashMap<String, SummaryModel> prxModel = new HashMap<>();
            for (String ctnPresent : productsToBeShown) {
                prxModel.put(ctnPresent, CartModelContainer.getInstance().getProductData(ctnPresent));
            }
            ArrayList<ProductCatalogData> productCatalogDatas = mergeResponsesFromHybrisAndPRX(planBProductList, productData, prxModel);
            refreshList(productCatalogDatas);
        }
    }

    public void refreshList(ArrayList<ProductCatalogData> data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
        /*if(Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();*/
    }

    private ArrayList<ProductCatalogData> mergeHybrisAndPRXPlanB(ArrayList<String> planBProductList, HashMap<String, SummaryModel> prxModel) {
        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXDataObjects();
        ArrayList<ProductCatalogData> products = new ArrayList<>();
        String ctn;
        for (String planBProduct : planBProductList) {
            ctn = planBProduct;
            ProductCatalogData productItem = new ProductCatalogData();
            Data data = null;
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

    private ArrayList<ProductCatalogData> mergeResponsesFromHybrisAndPRX(ArrayList<String> planBProductList, final Products productData, final HashMap<String, SummaryModel> prxModel) {
        if (planBProductList != null) {
            ArrayList<ProductCatalogData> products = mergeHybrisAndPRXPlanB(planBProductList, prxModel);
            return products;
        } else {
            ArrayList<ProductCatalogData> products = mergeHybrisAndPRXPlanA(productData, prxModel);
            return products;
        }
    }

    private ArrayList<ProductCatalogData> mergeHybrisAndPRXPlanA(Products productData, HashMap<String, SummaryModel> prxModel) {
        List<ProductsEntity> entries = productData.getProducts();
        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXDataObjects();
        ArrayList<ProductCatalogData> products = new ArrayList<>();
        String ctn;
        for (ProductsEntity entry : entries) {
            ctn = entry.getCode();
            ProductCatalogData productItem = new ProductCatalogData();
            Data data = null;
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
            fillEntryBaseData(entry, productItem, data);
            products.add(productItem);
        }
        return products;
    }

    private void fillEntryBaseData(final ProductsEntity entry, final ProductCatalogData productItem, final Data data) {
        if (entry.getPrice() == null || entry.getDiscountPrice() == null)
            return;
        productItem.setFormatedPrice(entry.getPrice().getFormattedValue());
        productItem.setPriceValue(String.valueOf(entry.getPrice().getValue()));
        if (entry.getDiscountPrice() != null && entry.getDiscountPrice().getFormattedValue() != null
                && !entry.getDiscountPrice().getFormattedValue().isEmpty()) {
            productItem.setDiscountedPrice(entry.getDiscountPrice().getFormattedValue());
        }
    }
}
