package com.philips.plataform.mya.model.network;

import android.os.Message;

import com.google.gson.JsonArray;
import com.philips.plataform.mya.model.listener.RequestListener;
import com.philips.plataform.mya.model.response.ConsentModel;

import java.util.Map;

/**
 * Created by philips on 10/18/17.
 */

public abstract class NetworkAbstractModel implements RequestListener {

    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(Message msg);
        void onModelDataError(Message msg);
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

    public abstract ConsentModel[] parseResponse(JsonArray response);

    public abstract int getMethod();

    public abstract Map<String, String> requestHeader();

    public abstract Map<String, String> requestBody();

    public abstract String getUrl();

}
