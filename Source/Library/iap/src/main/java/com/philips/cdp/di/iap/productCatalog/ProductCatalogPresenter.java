/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ProductCatalogHelper;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.response.products.ProductsEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ProductCatalogPresenter implements ProductCatalogAPI, AbstractModel.DataLoadListener {

    private Context mContext;
    private LoadListener mLoadListener;
    private HybrisDelegate mHybrisDelegate;
    private StoreSpec mStore;
    private FragmentManager mFragmentManager;
    Products mProductData = null;
    ProductCatalogHelper mProductCatalogHelper;
    boolean isPlanA;

    public void getCompleteProductList(Context mContext, final IAPHandlerProductListListener iapListener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        GetProductCatalogRequest model = new GetProductCatalogRequest(getStore(), null, this);
        model.setContext(mContext);
        delegate.sendRequest(0, model, new RequestListener() {

            @Override
            public void onSuccess(Message msg) {
                if (iapListener != null) {
                    ArrayList<String> productCTNs = getProductCTNs(msg);
                    IAPLog.d(IAPLog.LOG, "getCompleteProductList -- ProductCatelogPresenter " + productCTNs.toString());
                    iapListener.onSuccess(productCTNs);
                }
            }

            @Override
            public void onError(Message msg) {
                if (iapListener != null) {
                    iapListener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    private ArrayList<String> getProductCTNs(Message msg) {
        Products products = (Products) msg.obj;
        ArrayList<String> productCTNs = new ArrayList<String>();
        for (ProductsEntity entry : products.getProducts()) {
            String ctn = entry.getCode();
            productCTNs.add(ctn);
        }
        return productCTNs;
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }

    public interface LoadListener {
        void onLoadFinished(ArrayList<ProductCatalogData> data);

        void onLoadError(IAPNetworkError error);
    }

    public ProductCatalogPresenter() {
    }

    public ProductCatalogPresenter(Context context, LoadListener listener, FragmentManager fragmentManager, boolean isPlanA) {
        mContext = context;
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
        mProductCatalogHelper = new ProductCatalogHelper(mContext, mLoadListener, this);
        this.isPlanA = isPlanA;
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
    public boolean getProductCatalog() {
        GetProductCatalogRequest model = new GetProductCatalogRequest(getStore(), null, this);
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
        return true;
    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (processHybrisRequestForGetProductCatalogData(msg))
            return;

        if (mProductCatalogHelper.processPRXResponse(msg, IAPHandler.mProductCTNs, mProductData))
            return;
//        if (IAPHandler.mProductCTNs == null && mProductCatalogHelper.processPRXResponse(msg, mProductData))
//            return;
//        if (IAPHandler.mProductCTNs != null && mProductCatalogHelper.processPRXResponse(msg, IAPHandler.mProductCTNs))
//            return;
        /*if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();*/

    }

    public boolean processHybrisRequestForGetProductCatalogData(final Message msg) {
        if (msg.obj instanceof Products) {
            mProductData = (Products) msg.obj;
            if (mProductData != null) {
                mProductCatalogHelper.makePrxCall(IAPHandler.mProductCTNs, mProductData, false);
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
