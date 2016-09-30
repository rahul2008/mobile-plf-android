/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

import android.content.Context;
import android.os.Message;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ProductCatalogHelper;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.localematch.PILLocaleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductCatalogPresenter implements ProductCatalogAPI, AbstractModel.DataLoadListener {

    private Context mContext;

    private HybrisDelegate mHybrisDelegate;
    private StoreSpec mStore;

    Products mProductData = null;
    ProductCatalogHelper mProductCatalogHelper;

    private LoadListener mLoadListener;
    IAPListener mIAPListener;

    final int PAGE_SIZE = 20;
    final int CURRENT_PAGE = 0;

    public interface LoadListener {
        void onLoadFinished(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity);

        void onLoadError(IAPNetworkError error);
    }

    public ProductCatalogPresenter() {
    }

    public ProductCatalogPresenter(Context context, LoadListener listener) {
        mContext = context;
        mLoadListener = listener;
        mProductCatalogHelper = new ProductCatalogHelper(mContext, mLoadListener, this);
    }

    public void getCompleteProductList(final IAPListener iapListener) {
        PILLocaleManager localeManager = new PILLocaleManager(mContext);
        String currentCountryCode = localeManager.getCountryCode();
        String savedCountry = Utility.getCountryFromPreferenceForKey(mContext, IAPConstant.IAP_COUNTRY_KEY);
        completeProductList(iapListener, currentCountryCode, savedCountry);
    }

    public void completeProductList(IAPListener iapListener, String currentCountryCode, String savedCountry) {
        if (currentCountryCode.equalsIgnoreCase(savedCountry)
                && CartModelContainer.getInstance().getProductList() != null
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
    public void getCatalogCount(final IAPListener iapListener) {
        final AbstractModel.DataLoadListener listener = this;
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(CURRENT_PAGE));
        query.put(ModelConstants.PAGE_SIZE, String.valueOf(PAGE_SIZE));

        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);

        GetProductCatalogRequest model = new GetProductCatalogRequest(getStore(), query, listener);
        model.setContext(mContext);
        delegate.sendRequest(0, model, new RequestListener() {

            @Override
            public void onSuccess(final Message msg) {
                Products products = (Products) msg.obj;
                int totalProduct = products.getPagination().getTotalResults();
                if (totalProduct == 0) {
                    iapListener.onSuccess();
                    iapListener.onGetCompleteProductList(null);
                } else
                    getProductCatalog(CURRENT_PAGE, totalProduct, iapListener);

            }

            @Override
            public void onError(final Message msg) {
                iapListener.onFailure(0);
            }
        });
    }

    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, IAPListener listener) {
        this.mIAPListener = listener;

        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(currentPage));
        query.put(ModelConstants.PAGE_SIZE, String.valueOf(pageSize));

        GetProductCatalogRequest model = new GetProductCatalogRequest(getStore(), query, this);
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
        return true;
    }

    @Override
    public void getCategorizedProductList(ArrayList<String> productList) {
        if (CartModelContainer.getInstance().getProductList() != null) {
            if (mLoadListener != null) {
                mLoadListener.onLoadFinished(getCategorisedProductCatalog(productList), null);
            }
        }
    }

    public ArrayList<ProductCatalogData> getCategorisedProductCatalog(ArrayList<String> productList) {
        ArrayList<ProductCatalogData> catalogList = new ArrayList<>();
        CartModelContainer container = CartModelContainer.getInstance();
        for (String ctn : productList) {
            if (container.isProductCatalogDataPresent(ctn)) {
                catalogList.add(container.getProduct(ctn));
            }
        }
        return catalogList;
    }

    public boolean processHybrisRequestForGetProductCatalogData(final Message msg) {
        if (msg.obj instanceof Products) {
            mProductData = (Products) msg.obj;
            if (mProductData != null) {
                mProductCatalogHelper.makePrxCall(null, mProductData);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (msg.obj instanceof Products) {
            noProductInStore(msg);
        }
        if (processHybrisRequestForGetProductCatalogData(msg))
            return;

        if (mProductCatalogHelper.processPRXResponse(msg, null, mProductData, mIAPListener))
            return;
    }

    private void noProductInStore(Message msg) {
        if (mLoadListener != null && ((Products) msg.obj).getPagination().getTotalResults() < 1) {
            mLoadListener.onLoadError(createIAPErrorMessage
                    (mContext.getString(R.string.iap_no_product_available)));
        }
    }

    @Override
    public void onModelDataError(final Message msg) {
        if (mLoadListener != null) {
            if (msg.obj instanceof IAPNetworkError)
                mLoadListener.onLoadError((IAPNetworkError) msg.obj);
            else {
                mLoadListener.onLoadError(createIAPErrorMessage
                        (mContext.getString(R.string.iap_no_product_available)));
            }
        }

        if (Utility.isProgressDialogShowing()) {
            Utility.dismissProgressDialog();
        }
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

    private StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    private void sendHybrisRequest(int code, AbstractModel model, RequestListener listener) {
        getHybrisDelegate().sendRequest(code, model, model);
    }

    public IAPNetworkError createIAPErrorMessage(String errorMessage) {
        VolleyError volleyError = new ServerError();
        IAPNetworkError error = new IAPNetworkError(volleyError, -1, null);
        error.setCustomErrorMessage(errorMessage);
        return error;
    }
}
