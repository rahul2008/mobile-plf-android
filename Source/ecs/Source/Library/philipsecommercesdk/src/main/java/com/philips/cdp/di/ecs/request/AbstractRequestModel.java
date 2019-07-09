package com.philips.cdp.di.ecs.request;

import android.os.Message;



import java.util.Map;

public abstract class AbstractRequestModel  {

    protected Map<String, String> params;
    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(Message msg);
        void onModelDataError(Message msg);
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

    public abstract Object parseResponse(Object response);

    public abstract int getMethod();

    public abstract Map<String, String> requestBody();

    public abstract String getUrl();
}