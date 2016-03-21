/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.CartDeleteProductRequest;
import com.philips.cdp.di.iap.model.CartUpdateProductQuantityRequest;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartPresenter {
    Context mContext;
    ArrayList<ShoppingCartData> mProductData;
    private LoadListener mLoadListener;
    private HybrisDelegate mHybrisDelegate;
    private Store mStore;
    private FragmentManager mFragmentManager;

    public interface LoadListener {
        void onLoadFinished(ArrayList<ShoppingCartData> data);
    }

    public ShoppingCartPresenter(Context context, LoadListener listener, android.support.v4.app.FragmentManager fragmentManager) {
        mContext = context;
        mProductData = new ArrayList<>();
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
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

    private Store getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    /*//TODO: fix with TAG
    private void addShippingCostRowToTheList() {
        ShoppingCartData summary = new ShoppingCartData();
        mProductData.add(summary);
        mProductData.add(summary);
        mProductData.add(summary);
    }*/

    public void refreshList(ArrayList<ShoppingCartData> data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
    }

    private void sendHybrisRequest(int code, AbstractModel model, RequestListener listener) {
        getHybrisDelegate().sendRequest(code, model, model);
    }

    public void getCurrentCartDetails() {
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(getStore(), null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {

                        if (msg.obj instanceof ArrayList) {
                            mProductData = (ArrayList<ShoppingCartData>) msg.obj;
                            if (mProductData == null || mProductData.size() == 0) {
                                EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRGMENT_REPLACED);
                                Utility.dismissProgressDialog();
                                return;
                            }
                            //addShippingCostRowToTheList();
                            refreshList(mProductData);
                            CartModelContainer.getInstance().setShoppingCartData(mProductData);
                        } else{
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRGMENT_REPLACED);
                            Utility.dismissProgressDialog();
                        }
                        Utility.dismissProgressDialog();
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
                        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
                        NetworkUtility.getInstance().showErrorDialog(mFragmentManager, mContext.getString(R.string.iap_ok),
                                mContext.getString(R.string.iap_network_error), mContext.getString(R.string.iap_check_connection));
                        if(Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                    }
                });
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }

    public void deleteProduct(final ShoppingCartData summary) {
        Map<String, String> query = new HashMap<>();
        query.put(ModelConstants.ENTRY_CODE, String.valueOf(summary.getEntryNumber()));

        CartDeleteProductRequest model = new CartDeleteProductRequest(getStore(), query,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                        getCurrentCartDetails();
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        getCurrentCartDetails();
                    }
                });
        sendHybrisRequest(0, model, model);
    }

    public void updateProductQuantity(final ShoppingCartData data, final int count) {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.PRODUCT_CODE, data.getCtnNumber());
        query.put(ModelConstants.PRODUCT_QUANTITY, String.valueOf(count));
        query.put(ModelConstants.PRODUCT_ENTRYCODE, String.valueOf(data.getEntryNumber()));

        CartUpdateProductQuantityRequest model = new CartUpdateProductQuantityRequest(getStore(),
                query, new AbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(final Message msg) {
                getCurrentCartDetails();
            }

            @Override
            public void onModelDataError(final Message msg) {
                IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
                NetworkUtility.getInstance().showErrorDialog(mFragmentManager, mContext.getString(R.string.iap_ok),
                        mContext.getString(R.string.iap_network_error), mContext.getString(R.string.iap_check_connection));
                Utility.dismissProgressDialog();
            }
        });
        sendHybrisRequest(0, model, model);
    }
}
