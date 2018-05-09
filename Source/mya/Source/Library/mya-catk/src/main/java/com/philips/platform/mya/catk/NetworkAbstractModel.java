/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.google.gson.JsonArray;
import com.philips.platform.mya.catk.dto.GetConsentDto;
import com.philips.platform.mya.catk.error.ConsentNetworkError;

import java.util.List;

abstract class NetworkAbstractModel {

    protected DataLoadListener mDataLoadListener;

    public interface DataLoadListener {
        void onModelDataLoadFinished(List<GetConsentDto> consents);

        void onModelDataError(ConsentNetworkError error);
    }

    public NetworkAbstractModel(DataLoadListener listener) {
        mDataLoadListener = listener;
    }

    public void onResponseSuccess(final List<GetConsentDto> consents) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataLoadFinished(consents);
        }
    }

    public void onResponseError(final ConsentNetworkError error) {
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataError(error);
        }
    }


    public abstract List<GetConsentDto> parseResponse(JsonArray response);

    public abstract int getMethod();

    public abstract String requestBody();

    public abstract String getUrl();
}
