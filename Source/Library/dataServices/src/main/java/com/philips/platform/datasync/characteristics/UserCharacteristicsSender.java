package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristic;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.DatabaseCharacteristicsUpdateRequest;
import com.philips.platform.core.events.SendUserCharacteristicsToBackendResponseEvent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by indrajitkumar on 1/2/17.
 */
public class UserCharacteristicsSender implements DataSender<Characteristic> {

    private static final int API_VERSION = 9;
    private final UCoreAdapter uCoreAdapter;
    private GsonConverter gsonConverter;
    private Eventing eventing;
    DataServicesManager mDataServicesManager;
    @NonNull
    private final UCoreAccessProvider accessProvider;

    @Inject
    public UserCharacteristicsSender(UCoreAdapter uCoreAdapter, GsonConverter gsonConverter, Eventing eventing) {
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        this.eventing = eventing;
        mDataServicesManager = DataServicesManager.getInstance();
        this.accessProvider = mDataServicesManager.getUCoreAccessProvider();
    }

    @Override
    public boolean sendDataToBackend(@NonNull List<? extends Characteristic> dataToSend) {
        if (dataToSend == null) {
            return false;
        }
        try {
            UserCharacteristicsClient uGrowClient = uCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class, accessProvider.getAccessToken(), gsonConverter);
            Characteristic characteristics = dataToSend.get(0);
            UserCharacteristics bookmarkCharacteristics = new UserCharacteristics(Collections.singletonList(characteristics));
           // Response response = uGrowClient.putUserCharacteristics(accessProvider.getUserId(), accessProvider.getUserId(), bookmarkCharacteristics, API_VERSION);
            /*if (isResponseSuccess(response)) {
                postOk(characteristics);
            }*/
        } catch (RetrofitError retrofitError) {
            //resetTokenOnUnAuthorizedError(retrofitError);
            postError(retrofitError);
        }

        return false;
    }

    private void postError(final RetrofitError retrofitError) {
        eventing.post(new BackendResponse(1, retrofitError));
    }

    private void postOk(final Characteristic characteristics) {
        //eventing.post(new SendUserCharacteristicsToBackendResponseEvent(characteristics.getValue()));
        //set characterestics as syncronized .
        eventing.post(new DatabaseCharacteristicsUpdateRequest(null));//instead of null ,we should get characteristic here
    }

    @Override
    public Class<? extends Characteristic> getClassForSyncData() {
        return Characteristic.class;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

}
