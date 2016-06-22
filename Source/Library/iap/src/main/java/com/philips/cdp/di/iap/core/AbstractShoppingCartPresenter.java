/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractShoppingCartPresenter implements ShoppingCartAPI {
    public interface LoadListener<T> {
        void onLoadFinished(ArrayList<T> data);

        void onLoadListenerError(IAPNetworkError error);

        void onRetailerError(IAPNetworkError errorMsg);
    }

    private final static String PHILIPS_STORE_YES = "Y";

    protected FragmentManager mFragmentManager;
    protected Context mContext;
    protected ArrayList<StoreEntity> mStoreEntities;
    protected StoreSpec mStore;
    protected LoadListener mLoadListener;
    protected HybrisDelegate mHybrisDelegate;

    public AbstractShoppingCartPresenter() {
    }

    public AbstractShoppingCartPresenter(final Context context, final LoadListener listener, final FragmentManager fragmentManager) {
        mContext = context;
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
    }

    protected void handleModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
        dismissProgressDialog();
        if (mLoadListener != null) {
            if(msg.obj instanceof IAPNetworkError) {
                mLoadListener.onLoadListenerError((IAPNetworkError) msg.obj);
            }else{
                mLoadListener.onLoadListenerError(createIAPErrorMessage(mContext.getString(R.string.iap_something_went_wrong)));
            }
        }
    }

    @Override
    public void getRetailersInformation(String ctn) {

        Map<String, String> query = new HashMap<>();
        query.put(ModelConstants.PRODUCT_CODE, String.valueOf(ctn));

        GetRetailersInfoRequest model = new GetRetailersInfoRequest(getStore(), query,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(Message msg) {
                        WebResults webResults = null;
                        if (msg.obj instanceof WebResults) {
                            webResults = (WebResults) msg.obj;
                        }
                        if (webResults.getWrbresults().getOnlineStoresForProduct() == null || webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore() == null || webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore().size() == 0) {
                            dismissProgressDialog();
                            if (mLoadListener != null) {
                                mLoadListener.onRetailerError(createIAPErrorMessage(mContext.getString(R.string.iap_no_retailer_message)));
                            }
                            return;
                        }
                        mStoreEntities = (ArrayList<StoreEntity>) webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore();
                       // filterOutPhilipsStore();
                        refreshList(mStoreEntities);

                        dismissProgressDialog();
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        handleModelDataError(msg);
                    }
                });
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }

    private void filterOutPhilipsStore() {
        Iterator<StoreEntity> iterator = mStoreEntities.iterator();
        while (iterator.hasNext()) {
            StoreEntity entity = iterator.next();
            if (PHILIPS_STORE_YES.equalsIgnoreCase(entity.getIsPhilipsStore())) {
                iterator.remove();
            }
        }
    }

    protected void dismissProgressDialog() {
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @SuppressWarnings({"unchecked"})
    public void refreshList(ArrayList data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
    }

    public HybrisDelegate getHybrisDelegate() {
        if (mHybrisDelegate == null) {
            mHybrisDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mHybrisDelegate;
    }

    protected StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    protected void sendHybrisRequest(int code, AbstractModel model, RequestListener listener) {
        getHybrisDelegate().sendRequest(code, model, model);
    }

    public IAPNetworkError createIAPErrorMessage(String errorMessage) {
        VolleyError volleyError = new ServerError();
        IAPNetworkError error = new IAPNetworkError(volleyError, -1, null);
        error.setCustomErrorMessage(errorMessage);
        return error;
    }
}
