package com.philips.cdp.di.iap.model;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.session.RequestListener;

import java.util.Map;

public abstract class AbstractModel implements RequestListener {

    final protected StoreSpec store;
    protected Map<String, String> params;
    Context mContext;
    protected DataLoadListener mDataloadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(Message msg);
        void onModelDataError(Message msg);
    }

    public AbstractModel(StoreSpec store, Map<String, String> query) {
        this(store, query, null);
    }

    public AbstractModel(StoreSpec store, Map<String, String> query, DataLoadListener listener) {
        this.store = store;
        this.params = query;
        mDataloadListener = listener;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onSuccess(final Message msg) {
        onPostSuccess(msg);
    }

    @Override
    public void onError(final Message msg) {
        onPostError(msg);
    }

    protected void onPostSuccess(Message msg) {
        if (mDataloadListener != null) {
            mDataloadListener.onModelDataLoadFinished(msg);
        }
    }

    protected void onPostError(Message msg) {
        if (mDataloadListener != null) {
            mDataloadListener.onModelDataError(msg);
        }
    }

    public StoreSpec getStore() {
        return store;
    }

    public abstract Object parseResponse(Object response);

    public abstract int getMethod();

    public abstract Map<String, String> requestBody();

    public abstract String getUrl();
}