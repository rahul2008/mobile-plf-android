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
    private LoadListener mLoadListener;
    private HybrisDelegate mHybrisDelegate;
    private StoreSpec mStore;
    Products mProductData = null;
    ProductCatalogHelper mProductCatalogHelper;
    boolean isPlanA;
    IAPListener notifyCompleteListener;
    final int PAGE_SIZE = 20;
    final int CURRENT_PAGE = 0;

    public void getCompleteProductList(final Context mContext, final IAPListener iapListener, final int currentPage, final int pageSize) {

        final IAPListener listener = new IAPListener() {

            @Override
            public void onGetCartCount(int count) {

            }

            @Override
            public void onGetCompleteProductList(ArrayList<String> productList) {
                if (iapListener != null) {
                    iapListener.onGetCompleteProductList(productList);
                }
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(final int errorCode) {
                if (iapListener != null) {
                    iapListener.onFailure(0);
                }
            }
        };

        IAPListener countListner = new IAPListener() {
//            @Override
//            public void onSuccess(final int count) {
//                getProductCatalog(CURRENT_PAGE, count, listener);
//            }

            @Override
            public void onGetCartCount(int count) {
                getProductCatalog(CURRENT_PAGE, count, listener);
            }

            @Override
            public void onGetCompleteProductList(ArrayList<String> productList) {
                if (iapListener != null) {
                    iapListener.onGetCompleteProductList(productList);
                }
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(final int errorCode) {

            }
        };
        if (CartModelContainer.getInstance().getProductCatalogData() != null && CartModelContainer.getInstance().getProductCatalogData().size() != 0) {
            if (iapListener != null) {
                iapListener.onGetCompleteProductList(getProductCatalogDataFromStoredData());
            }
        } else {
            getCatalogCount(countListner);
        }
    }

    ArrayList<String> getProductCatalogDataFromStoredData() {
        ArrayList<String> catalogDatas = new ArrayList<>();
        HashMap<String, ProductCatalogData> productCatalogDataSaved = CartModelContainer.getInstance().getProductCatalogData();
        for (Map.Entry<String, ProductCatalogData> entry : productCatalogDataSaved.entrySet()) {
            if (entry != null) {
                catalogDatas.add(entry.getValue().getCtnNumber());
            }
        }
        return catalogDatas;
    }

    @Override
    public void getCatalogCount(final IAPListener countListener) {
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
                int totalproduct = products.getPagination().getTotalResults();
                IAPLog.i(IAPLog.LOG, "Total product in Hybris =" + totalproduct);
                // countListener.onGetCompleteProductList(null);
                countListener.onGetCartCount(totalproduct);
            }

            @Override
            public void onError(final Message msg) {
                countListener.onFailure(0);
            }
        });
    }

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

    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, IAPListener listener) {
        this.notifyCompleteListener = listener;
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
        ArrayList<ProductCatalogData> catalogDatas = new ArrayList<>();
        CartModelContainer container = CartModelContainer.getInstance();
        for (String ctn : productList) {
            if (container.isProductCatalogDataPresent(ctn)) {
                catalogDatas.add(container.getProduct(ctn));
            }
        }
        return catalogDatas;
    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (processHybrisRequestForGetProductCatalogData(msg))
            return;

        if (mProductCatalogHelper.processPRXResponse(msg, null, mProductData, notifyCompleteListener))
            return;
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

    public IAPNetworkError createIAPErrorMessage(String errorMessage) {
        VolleyError volleyError = new ServerError();
        IAPNetworkError error = new IAPNetworkError(volleyError, -1, null);
        error.setCustomErrorMessage(errorMessage);
        return error;
    }
}
