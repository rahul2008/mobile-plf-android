package com.ecs.demouapp.ui.controller;


import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.ProductDetailRequest;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;

import java.util.HashMap;

public class ProductDetailController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private ProductSearchListener mProductSearchListener;
    private HybrisDelegate mDelegate;
    private StoreListener mStore;

    public interface ProductSearchListener {
        void onGetProductDetail(Message msg);
    }

    public ProductDetailController(Context context, ProductSearchListener listener) {
        mContext = context;
        mProductSearchListener = listener;
    }

    public void getProductDetail(String ctnNumber) {
        HashMap<String, String> query = new HashMap<>();
        String formattedCtnNumber = ctnNumber.replace('/', '_');
        query.put(ModelConstants.PRODUCT_CODE, formattedCtnNumber);
        ProductDetailRequest model = new ProductDetailRequest(getStore(), query, this);
        getHybrisDelegate().sendRequest(RequestCode.SEARCH_PRODUCT, model, model);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        sendListener(msg);

    }

    @Override
    public void onModelDataError(Message msg) {
        sendListener(msg);
    }

    private void sendListener(Message msg) {
        if (null == mProductSearchListener) return;

        int requestCode = msg.what;

        switch (requestCode) {
            case RequestCode.SEARCH_PRODUCT:
                mProductSearchListener.onGetProductDetail(msg);
                break;
        }
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    public void setStore(StoreListener store) {
        mStore = store;
    }

    StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
