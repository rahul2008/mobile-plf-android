/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public abstract class AbstractShoppingCartPresenter implements ShoppingCartAPI {
    protected Context mContext;
    protected ArrayList<StoreEntity> mStoreEntities;
    protected StoreListener mStore;
    protected ShoppingCartListener mLoadListener;
    protected HybrisDelegate mHybrisDelegate;

    public interface ShoppingCartListener<T> {
        void onLoadFinished(ArrayList<T> data);

        void onLoadError(Message msg);

        void onRetailerError(IAPNetworkError errorMsg);
    }

    public AbstractShoppingCartPresenter() {
    }

    public AbstractShoppingCartPresenter(final Context context, final ShoppingCartListener listener) {
        mContext = context;
        mLoadListener = listener;
    }

    protected void handleModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
        dismissProgressDialog();
        if (mLoadListener != null) {
            mLoadListener.onLoadError(msg);
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
                        if(webResults==null){
                            mLoadListener.onRetailerError(NetworkUtility.getInstance().createIAPErrorMessage(mContext.getString(R.string.iap_no_retailer_message)));
                            return;
                        }
                        if (webResults.getWrbresults().getOnlineStoresForProduct() == null || webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore() == null || webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore().size() == 0) {
                            dismissProgressDialog();
                            if (mLoadListener != null) {
                                mLoadListener.onRetailerError(NetworkUtility.getInstance().createIAPErrorMessage(mContext.getString(R.string.iap_no_retailer_message)));
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
        getHybrisDelegate().sendRequest(0, model, model);
    }

  /*  private void filterOutPhilipsStore() {
        Iterator<StoreEntity> iterator = mStoreEntities.iterator();
        while (iterator.hasNext()) {
            StoreEntity entity = iterator.next();
            if (PHILIPS_STORE_YES.equalsIgnoreCase(entity.getIsPhilipsStore())) {
                iterator.remove();
            }
        }
    }*/

    protected void dismissProgressDialog() {
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @SuppressWarnings({"unchecked"})
    public void refreshList(ArrayList data) {
        if (mLoadListener == null) {
            return;
        }
        mLoadListener.onLoadFinished(data);
    }

    public HybrisDelegate getHybrisDelegate() {
        if (mHybrisDelegate == null) {
            mHybrisDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mHybrisDelegate;
    }

    protected StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

}
