/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.productCatalog;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ProductCatalogPresenter {

    private Context mContext;
    private ArrayList<ProductCatalogData> mProductData;
    private LoadListener mLoadListener;
    private HybrisDelegate mHybrisDelegate;
    private Store mStore;
    private FragmentManager mFragmentManager;

    public interface LoadListener {
        void onLoadFinished(ArrayList<ProductCatalogData> data);
    }


    public ProductCatalogPresenter(Context context, LoadListener listener, FragmentManager fragmentManager){
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

    public void refreshList(ArrayList<ProductCatalogData> data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
    }

    private void sendHybrisRequest(int code, AbstractModel model, RequestListener listener) {
        getHybrisDelegate().sendRequest(code, model, model);
    }


    public void getProductCatalog() {
        GetProductCatalogRequest model = new GetProductCatalogRequest(getStore(), null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(Message msg) {

                        if (msg.obj instanceof Products) {
                            Products productCatalog = (Products) msg.obj;
                            if (productCatalog.getProducts() == null) {
                                msg = Message.obtain(msg);
                            } else {
                                PRXBuilderForProductCatalog builder = new PRXBuilderForProductCatalog(mContext, productCatalog,
                                        this);
                                builder.build();
                                return;
                            }
                        }

                        if (msg.obj instanceof ArrayList) {
                            mProductData = (ArrayList<ProductCatalogData>) msg.obj;
                            if (mProductData == null || mProductData.size() == 0) {
                                EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                                if (Utility.isProgressDialogShowing()) {
                                    Utility.dismissProgressDialog();
                                }
                                return;
                            }
                            //addShippingCostRowToTheList();
                            refreshList(mProductData);
                        } else{
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                            Utility.dismissProgressDialog();
                        }
                        if(Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
                        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
                        NetworkUtility.getInstance().showErrorMessage(msg,mFragmentManager,mContext);
                        if(Utility.isProgressDialogShowing()) {
                            Utility.dismissProgressDialog();
                        }
                    }
                });
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }


}
