/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.products;

import android.content.Context;
import android.os.Message;

import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.GetProductCatalogRequest;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.session.RequestListener;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.ecs.demouapp.ui.utils.NetworkUtility;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.PaginationEntity;
import com.philips.cdp.di.ecs.model.products.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductCatalogPresenter implements ProductCatalogAPI, AbstractModel.DataLoadListener {

    private Context mContext;

    private HybrisDelegate mHybrisDelegate;
    private StoreListener mStore;

    private Products mProducts;
    private ProductCatalogHelper mProductCatalogHelper;

    protected ProductCatalogListener mProductCatalogListener;
    ECSListener mIAPListener;

    private final int PAGE_SIZE = 20;
    private final int CURRENT_PAGE = 0;

    public interface ProductCatalogListener {
        void onLoadFinished(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity);

        void onLoadError(IAPNetworkError error);
    }

    public ProductCatalogPresenter(Context context, ProductCatalogListener productCatalogListener) {
        mContext = context;
        mProductCatalogListener = productCatalogListener;
        mProductCatalogHelper = new ProductCatalogHelper(mContext, mProductCatalogListener, this);
    }

    public void getCompleteProductList(final ECSListener iapListener) {
        completeProductList(iapListener);
    }

    public void completeProductList(ECSListener iapListener) {
        if (
                CartModelContainer.getInstance().getProductList() != null
                        && CartModelContainer.getInstance().getProductList().size() != 0) {
            iapListener.onGetCompleteProductList(getProductCatalogDataFromStoredData());
        } else {
            CartModelContainer.getInstance().clearCategorisedProductList();
            getCatalogCount(iapListener);
        }
    }

    ArrayList<String> getProductCatalogDataFromStoredData() {
        ArrayList<String> catalogList = new ArrayList<>();
        HashMap<String, ProductCatalogData> productCatalogDataSaved =
                CartModelContainer.getInstance().getProductList();
        for (Map.Entry<String, ProductCatalogData> entry : productCatalogDataSaved.entrySet()) {
            if (entry != null) {
                catalogList.add(entry.getValue().getCtnNumber());
            }
        }
        return catalogList;
    }

    @Override
    public void getCatalogCount(final ECSListener iapListener) {


        ECSUtility.getInstance().getEcsServices().getProductList(CURRENT_PAGE, PAGE_SIZE, new ECSCallback<Products, Exception>() {
            @Override
            public void onResponse(Products products) {

                int totalProduct = products.getPagination().getTotalResults();
                if (totalProduct == 0) {
                    iapListener.onSuccess();
                    iapListener.onGetCompleteProductList(null);
                } else
                    getProductCatalog(CURRENT_PAGE, totalProduct, iapListener);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                iapListener.onFailure(0);
            }
        });

    }


    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, ECSListener listener) {
        this.mIAPListener = listener;

        ECSUtility.getInstance().getEcsServices().getProductList(currentPage, pageSize, new ECSCallback<com.philips.cdp.di.ecs.model.products.Products, Exception>() {
            @Override
            public void onResponse(com.philips.cdp.di.ecs.model.products.Products result) {

                mProducts = result;

                if (mProducts.getPagination().getTotalResults() < 1) {
                    trackNoProductFoundInPRX();
                    mProductCatalogListener.onLoadError(NetworkUtility.getInstance().createIAPErrorMessage
                            ("", mContext.getString(R.string.iap_no_product_available)));
                    return;
                }

                mProductCatalogHelper.processPRXResponse(mProducts,listener);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        });
        return true;
    }

    @Override
    public void getCategorizedProductList(ArrayList<String> ctnList) {
        if (CartModelContainer.getInstance().getProductList() != null) {
            ArrayList<ProductCatalogData> productCatalogList = new ArrayList<>();
            CartModelContainer container = CartModelContainer.getInstance();
            for (String ctn : ctnList) {
                if (container.isProductCatalogDataPresent(ctn)) {
                    productCatalogList.add(container.getProduct(ctn));
                }
            }
            mProductCatalogListener.onLoadFinished(productCatalogList, null);
        }
    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (msg.obj instanceof Products) {
            mProducts = (Products) msg.obj;

            if (mProducts.getPagination().getTotalResults() < 1) {
                trackNoProductFoundInPRX();
                mProductCatalogListener.onLoadError(NetworkUtility.getInstance().createIAPErrorMessage
                        ("", mContext.getString(R.string.iap_no_product_available)));
            } else {
                mProductCatalogHelper.sendPRXRequest(mProducts);
            }
        } else if (msg.obj instanceof HashMap) {
           // mProductCatalogHelper.processPRXResponse(msg, mProducts, mIAPListener);
        }
    }

    @Override
    public void onModelDataError(final Message msg) {
        if(mProductCatalogListener== null) return;
        if (msg.obj instanceof IAPNetworkError)
            mProductCatalogListener.onLoadError((IAPNetworkError) msg.obj);
        else {
            trackNoProductFoundInPRX();
            mProductCatalogListener.onLoadError(NetworkUtility.getInstance().createIAPErrorMessage
                    ("", mContext.getString(R.string.iap_no_product_available)));
        }
    }

    private void trackNoProductFoundInPRX() {
        ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                ECSAnalyticsConstant.ERROR, ECSAnalyticsConstant.PRX + ECSAnalyticsConstant.NO_PRODUCT_FOUND);
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mHybrisDelegate = delegate;
    }

    public HybrisDelegate getHybrisDelegate() {
        if (mHybrisDelegate == null) {
            mHybrisDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mHybrisDelegate;
    }

    private StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    public void setStore(StoreListener store) {
        mStore = store;
    }
}
