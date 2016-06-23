package com.philips.cdp.di.iap.controller;


import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.ProductDetailRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;

public class ProductDetailController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private ProductSearchListener mProductSearchListener;
    private HybrisDelegate mDelegate;
    private StoreSpec mStore;

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
        model.setContext(mContext);
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

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
