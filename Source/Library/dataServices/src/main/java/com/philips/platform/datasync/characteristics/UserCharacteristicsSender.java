/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
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

public class UserCharacteristicsSender implements DataSender<UserCharacteristics> {

    private static final int API_VERSION = 9;

    @Inject
    UCoreAdapter mUCoreAdapter;

    @NonNull
    GsonConverter mGsonConverter;
    @Inject
    Eventing mEventing;
    @Inject
    UserCharacteristicsConverter mUserCharacteristicsConverter;

    @Inject
    UCoreAccessProvider mUCoreAccessProvider;

    @Inject
    public UserCharacteristicsSender(@NonNull final UserCharacteristicsConverter userCharacteristicsConverter,
                                     @NonNull final GsonConverter gsonConverter) {
        DataServicesManager.getInstance().mAppComponent.injectUserCharacteristicsSender(this);
        this.mUserCharacteristicsConverter = userCharacteristicsConverter;
        this.mGsonConverter = gsonConverter;
    }

    @Override
    public boolean sendDataToBackend(@NonNull List<? extends UserCharacteristics> userCharacteristicsListToSend) {
        if (!mUCoreAccessProvider.isLoggedIn() && userCharacteristicsListToSend.size() > 0) {
            return false;
        }

        List<UserCharacteristics> userUserCharacteristicsList = new ArrayList<>();
        for (UserCharacteristics userCharacteristics : userCharacteristicsListToSend) {
            userUserCharacteristicsList.add(userCharacteristics);
        }
        //TODO:Spoorti - send only if not synced
        //While sending UC ,we used to fetch UC which are not synced ,So no need to check here
        return sendUserCharacteristics(userUserCharacteristicsList);
    }

    private boolean sendUserCharacteristics(List<UserCharacteristics> userUserCharacteristicsList) {

        if (userUserCharacteristicsList == null || userUserCharacteristicsList.size() == 0 ) return false;
        try {
            UserCharacteristicsClient uClient =
                    mUCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class,
                            mUCoreAccessProvider.getAccessToken(), mGsonConverter);

            Response response = uClient.createOrUpdateUserCharacteristics(mUCoreAccessProvider.getUserId(),
                            mUCoreAccessProvider.getUserId(),
                            mUserCharacteristicsConverter.convertToUCoreUserCharacteristics(userUserCharacteristicsList),
                            API_VERSION);

            if (isResponseSuccess(response)) {
                postOk(userUserCharacteristicsList.get(0));
                return true;
            }
        } catch (RetrofitError retrofitError) {
            postError(retrofitError);
        }

        return false;
    }

    private void postError(final RetrofitError retrofitError) {
        mEventing.post(new BackendResponse(1, retrofitError));
    }

    private void postOk(UserCharacteristics characteristic) {
        characteristic.setSynchronized(true);
        DSLog.d(DSLog.LOG, "Inder = Inside UC Sender postOk " + characteristic.getCharacteristicsDetails());
        //TODO: SPoorti - As of now I see that the below event is posted to saving monitor, genrally it could go to UpdatingMonitor.
        // We can get it one's verified with Ajay
        mEventing.post(new UserCharacteristicsSaveRequest(characteristic, null));
    }

    @Override
    public Class<? extends UserCharacteristics> getClassForSyncData() {
        return UserCharacteristics.class;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }
}
