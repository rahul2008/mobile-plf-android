/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractShoppingCartPresenter implements ShoppingCartAPI {

    public interface LoadListener<T> {
        void onLoadFinished(ArrayList<T> data);
    }

    protected FragmentManager mFragmentManager;
    protected Context mContext;
    protected ArrayList<StoreEntity> mStoreEntities;
    protected StoreSpec mStore;
    protected LoadListener mLoadListener;
    protected HybrisDelegate mHybrisDelegate;
    protected ArrayList<ShoppingCartData> mProductData;

    public AbstractShoppingCartPresenter() {}

    public AbstractShoppingCartPresenter(final Context context, final LoadListener listener, final FragmentManager fragmentManager) {
        mContext = context;
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
    }

    protected void handleModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
        NetworkUtility.getInstance().showErrorMessage(msg, mFragmentManager, mContext);
        dismissProgressDialog();
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
                            NetworkUtility.getInstance().showErrorDialog(mFragmentManager, mContext.getString(R.string.iap_ok), "No Retailers for this product", "No Retailers for this product");
                            Utility.dismissProgressDialog();
                            return;
                        }
                        mStoreEntities = (ArrayList<StoreEntity>) webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore();
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

    protected void dismissProgressDialog() {
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    protected void launchShoppingCart() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, new ShoppingCartFragment());
        transaction.addToBackStack(ShoppingCartFragment.TAG);
        transaction.commitAllowingStateLoss();
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
}
