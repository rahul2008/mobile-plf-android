package com.philips.cdp.di.iap.model;

import android.os.Message;

import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.StoreListener;

import java.util.Map;

public abstract class AbstractModel implements RequestListener {
//    Context mContext;

    final protected StoreListener store;
    protected Map<String, String> params;
    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(Message msg);
        void onModelDataError(Message msg);
    }

    public AbstractModel(StoreListener store, Map<String, String> query) {
        this(store, query, null);
    }

    public AbstractModel(StoreListener store, Map<String, String> query, DataLoadListener listener) {
        this.store = store;
        this.params = query;
        mDataLoadListener = listener;
    }

    /*public void setContext(Context context) {
        mContext = context;
    }*/

    @Override
    public void onSuccess(final Message msg) {
        onPostSuccess(msg);
    }

    @Override
    public void onError(final Message msg) {
        onPostError(msg);
    }

    protected void onPostSuccess(Message msg) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataLoadFinished(msg);
        }
    }

    protected void onPostError(Message msg) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataError(msg);
        }
    }

    public StoreListener getStore() {
        return store;
    }

    public abstract Object parseResponse(Object response);

    public abstract int getMethod();

    public abstract Map<String, String> requestBody();

    public abstract String getUrl();
}