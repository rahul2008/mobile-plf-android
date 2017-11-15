/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.network;

import com.google.gson.JsonArray;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.Consent;

import java.util.List;

public abstract class NetworkAbstractModel {

    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(List<Consent> consents);

        int onModelDataError(ConsentNetworkError error);
    }

    public NetworkAbstractModel(DataLoadListener listener) {
        mDataLoadListener = listener;
    }

    public void onResponseSuccess(final List<Consent> consents) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataLoadFinished(consents);
        }
    }

    public void onResponseError(final ConsentNetworkError error) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataError(error);
        }
    }


    public abstract List<Consent> parseResponse(JsonArray response);

    public abstract int getMethod();

    public abstract String requestBody();

    public abstract String getUrl();
}
