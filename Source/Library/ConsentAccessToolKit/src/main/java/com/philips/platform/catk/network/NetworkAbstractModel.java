package com.philips.platform.catk.network;

import android.os.Message;

import com.google.gson.JsonArray;
import com.philips.platform.catk.listener.RequestListener;
import com.philips.platform.catk.model.GetConsentsModel;

import java.util.Map;

/**
 * Created by philips on 10/18/17.
 */

public abstract class NetworkAbstractModel implements RequestListener {

    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(Message msg);
        int onModelDataError(Message msg);
    }

    public NetworkAbstractModel(DataLoadListener listener) {
        mDataLoadListener = listener;
    }

    @Override
    public void onResponseSuccess(final Message msg) {
        onPostSuccess(msg);
    }

    @Override
    public void onResponseError(final Message msg) {
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

    public abstract GetConsentsModel[] parseResponse(JsonArray response);

    public abstract int getMethod();

    public abstract Map<String, String> requestHeader();

    public abstract String requestBody();

    public abstract String getUrl();

}
