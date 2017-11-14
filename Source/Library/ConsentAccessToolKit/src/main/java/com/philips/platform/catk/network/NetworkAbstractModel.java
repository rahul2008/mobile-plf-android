/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.network;

import android.os.Message;

import com.google.gson.JsonArray;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.GetConsentsModel;

public abstract class NetworkAbstractModel {

    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(Message msg);

        int onModelDataError(ConsentNetworkError error);
    }

    public NetworkAbstractModel(DataLoadListener listener) {
        mDataLoadListener = listener;
    }

    public void onResponseSuccess(final Message msg) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataLoadFinished(msg);
        }
    }

    public void onResponseError(final ConsentNetworkError error) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataError(error);
        }
    }

    public abstract GetConsentsModel[] parseResponse(JsonArray response);

    public abstract int getMethod();

    public abstract String requestBody();

    public abstract String getUrl();
}
