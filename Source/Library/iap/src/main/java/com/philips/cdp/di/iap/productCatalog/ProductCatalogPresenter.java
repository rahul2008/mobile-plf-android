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
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;

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

    public ProductCatalogPresenter() {
    }

    public ProductCatalogPresenter(Context context, LoadListener listener) {
        mContext = context;
        mLoadListener = listener;
        mProductCatalogHelper = new ProductCatalogHelper(mContext, mLoadListener, this);
    }

    public void getCompleteProductList(final IAPListener iapListener) {
        if (CartModelContainer.getInstance().getProductCatalogData() != null &&
                CartModelContainer.getInstance().getProductCatalogData().size() != 0) {
            iapListener.onGetCompleteProductList(getProductCatalogDataFromStoredData());
        } else {
            getCatalogCount(iapListener);
        }
    }

    ArrayList<String> getProductCatalogDataFromStoredData() {
        ArrayList<String> catalogList = new ArrayList<>();
        HashMap<String, ProductCatalogData> productCatalogDataSaved = CartModelContainer.getInstance().getProductCatalogData();
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
    public void getProductCategorizedProduct(ArrayList<String> productList) {
        if (CartModelContainer.getInstance().getProductCatalogData() != null) {
            if (mLoadListener != null) {
                mLoadListener.onLoadFinished(getCatalogItems(productList), null);
            }
        } else {
            ProductCatalogPresenter presenter = new ProductCatalogPresenter();
            presenter.getProductCategorizedProduct(productList);
        }
    }

    private ArrayList<ProductCatalogData> getCatalogItems(ArrayList<String> productList) {
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
            if (((Products) msg.obj).getPagination().getTotalResults() < 1) {
                mLoadListener.onLoadError(createIAPErrorMessage(mContext.getString(R.string.iap_no_product_available)));
            }
        }
        if (processHybrisRequestForGetProductCatalogData(msg))
            return;

        if (mProductCatalogHelper.processPRXResponse(msg, null, mProductData, mIAPListener))
            return;
    }

    @Override
    public void onModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());

        if (mLoadListener != null) {
            if (msg.obj instanceof IAPNetworkError)
                mLoadListener.onLoadError((IAPNetworkError) msg.obj);
            else {
                mLoadListener.onLoadError(createIAPErrorMessage(mContext.getString(R.string.iap_no_product_available)));
            }
        }
        if (Utility.isProgressDialogShowing()) {
            Utility.dismissProgressDialog();
        }
    }

    public interface LoadListener {
        void onLoadFinished(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity);

        void onLoadError(IAPNetworkError error);
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
