/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class UserCharacteristicsSender extends DataSender {
    @Inject
    UCoreAdapter mUCoreAdapter;

    @NonNull
    private GsonConverter mGsonConverter;

    @Inject
    Eventing mEventing;

    @Inject
    UserCharacteristicsConverter mUserCharacteristicsConverter;

    @Inject
    UCoreAccessProvider mUCoreAccessProvider;

    @Inject
    public UserCharacteristicsSender(@NonNull final UserCharacteristicsConverter userCharacteristicsConverter,
                                     @NonNull final GsonConverter gsonConverter) {
        DataServicesManager.getInstance().getAppComponent().injectUserCharacteristicsSender(this);
        this.mUserCharacteristicsConverter = userCharacteristicsConverter;
        this.mGsonConverter = gsonConverter;
    }

    @Override
    public boolean sendDataToBackend(@NonNull List userCharacteristicsListToSend) {
        if (!mUCoreAccessProvider.isLoggedIn() || userCharacteristicsListToSend == null) {
            return false;
        }

        List<Characteristics> userUserCharacteristicsList = new ArrayList<>();
        for (Object characteristics : userCharacteristicsListToSend) {
            userUserCharacteristicsList.add((Characteristics) characteristics);
        }
        
        return sendUserCharacteristics(userUserCharacteristicsList);
    }

    private boolean sendUserCharacteristics(List<Characteristics> userUserCharacteristicsList) {

        if (userUserCharacteristicsList == null) return false;
        try {
            UserCharacteristicsClient uClient =
                    mUCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class,
                            mUCoreAccessProvider.getAccessToken(), mGsonConverter);
            if (uClient == null) return false;
            Response response = uClient.createOrUpdateUserCharacteristics(mUCoreAccessProvider.getUserId(),
                    mUCoreAccessProvider.getUserId(),
                    mUserCharacteristicsConverter.convertToUCoreUserCharacteristics(userUserCharacteristicsList),
                    UCoreAdapter.API_VERSION);

            if (isResponseSuccess(response)) {
                mEventing.post(new SyncBitUpdateRequest(SyncType.CHARACTERISTICS, true));
                return true;
            }
        } catch (RetrofitError retrofitError) {
            onError(retrofitError);
            postError(retrofitError);
        }
        return false;
    }

    private void postError(final RetrofitError retrofitError) {
        mEventing.post(new BackendResponse(1, retrofitError));
    }

    @Override
    public Class<? extends Characteristics> getClassForSyncData() {
        return Characteristics.class;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }
}
