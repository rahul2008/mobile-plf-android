/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
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

public class UserCharacteristicsSender implements DataSender<Characteristics> {

    private static final int API_VERSION = 9;

    private final UCoreAdapter mUCoreAdapter;

    private GsonConverter mGsonConverter;
    private Eventing mEventing;
    private UserCharacteristicsConverter mUserCharacteristicsConverter;
    private DataServicesManager mDataServicesManager;

    @NonNull
    private final UCoreAccessProvider accessProvider;

    @Inject
    public UserCharacteristicsSender(UCoreAdapter uCoreAdapter, GsonConverter gsonConverter, Eventing eventing, UserCharacteristicsConverter userCharacteristicsConverter) {
        this.mUCoreAdapter = uCoreAdapter;
        this.mGsonConverter = gsonConverter;
        this.mEventing = eventing;
        this.mUserCharacteristicsConverter = userCharacteristicsConverter;

        mDataServicesManager = DataServicesManager.getInstance();
        this.accessProvider = mDataServicesManager.getUCoreAccessProvider();
    }

    @Override
    public boolean sendDataToBackend(@NonNull List<? extends Characteristics> userCharacteristicsListToSend) {
        if (!accessProvider.isLoggedIn() && userCharacteristicsListToSend == null
                && userCharacteristicsListToSend.size() > 0) {
            return false;
        }

        List<Characteristics> userCharacteristicsList = new ArrayList<>();
        for (Characteristics characteristics : userCharacteristicsListToSend) {
            userCharacteristicsList.add(characteristics);
        }
        return sendUserCharacteristics(userCharacteristicsList);
    }

    private boolean sendUserCharacteristics(List<Characteristics> userCharacteristicsList) {
        try {
            UserCharacteristicsClient uClient =
                    mUCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class,
                            accessProvider.getAccessToken(), mGsonConverter);

            Response response =
                    uClient.createOrUpdateUserCharacteristics(accessProvider.getUserId(), accessProvider.getUserId(), mUserCharacteristicsConverter.convertToUCoreUserCharacteristics(userCharacteristicsList), API_VERSION);

            if (isResponseSuccess(response)) {
                postOk();
            }
        } catch (RetrofitError retrofitError) {
            postError(retrofitError);
        }

        return false;
    }

    private void postError(final RetrofitError retrofitError) {
        mEventing.post(new BackendResponse(1, retrofitError));
    }

    private void postOk() {
        mEventing.post(new UserCharacteristicsSaveRequest(null));
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
