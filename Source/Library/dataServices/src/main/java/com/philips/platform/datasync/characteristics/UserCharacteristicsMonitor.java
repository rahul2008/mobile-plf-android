package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristic;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.CharacteristicsBackendGetRequest;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.events.DatabaseCharacteristicsUpdateRequest;
import com.philips.platform.core.events.FetchUserCharacteristicsFromBackendEvent;
import com.philips.platform.core.events.UserCharacteristicsToBackendEvent;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.consent.UCoreConsentDetail;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public class UserCharacteristicsMonitor extends EventMonitor {

    private final DataServicesManager mDataServicesManager;
    private final UCoreAccessProvider accessProvider;
    private UserCharacteristicsSender userCharacteristicsSender;
    private UserCharacteristicsFetcher userCharacteristicsFetcher;

    private final UCoreAdapter uCoreAdapter;
    private final  GsonConverter gsonConverter;
    private final  Eventing eventing;

    private static final int API_VERSION = 9;

    @Inject
    public UserCharacteristicsMonitor(UCoreAdapter uCoreAdapter, GsonConverter gsonConverter, Eventing eventing) {
        this.uCoreAdapter=uCoreAdapter;
        this.gsonConverter=gsonConverter;
        this.eventing=eventing;
        mDataServicesManager = DataServicesManager.getInstance();
        this.accessProvider = mDataServicesManager.getUCoreAccessProvider();
    }

    public void onEventAsync(CharacteristicsBackendSaveRequest characteristicsBackendSaveRequest) {

       /* for(CharacteristicsDetail characteristicsDetail:characteristicsBackendSaveRequest.getCharacteristic().getCharacteristicsDetails()){
            Characteristic characteristic = new Characteristic(characteristicsDetail.getType(), characteristicsDetail.getValue());
            sendDataToBackend(Collections.singletonList(characteristic));
        }*/

        sendDataToBackend(characteristicsBackendSaveRequest.getCharacteristic());

    }

    public void onEventAsync(CharacteristicsBackendGetRequest characteristicsBackendGetRequest) {
        userCharacteristicsFetcher.fetchDataSince(null);
    }

    public boolean sendDataToBackend(Characteristics characteristics) {
        if (characteristics.getCharacteristicsDetails() == null) {
            return false;
        }
        try {
            UserCharacteristicsClient uGrowClient = uCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class, accessProvider.getAccessToken(), gsonConverter);
            //Characteristic characteristics = dataToSend.get(0);
            //UserCharacteristics bookmarkCharacteristics = new UserCharacteristics(Collections.singletonList(characteristics));
            List<UCoreCharacteristicsDetail> uCoreCharacteristicsDetails = convertToUCoreCharacteristicsDetails(characteristics.getCharacteristicsDetails());
            Response response = uGrowClient.putUserCharacteristics(accessProvider.getUserId(), accessProvider.getUserId(), uCoreCharacteristicsDetails, API_VERSION);
            if (isResponseSuccess(response)) {
                postOk(characteristics);
            }
        } catch (RetrofitError retrofitError) {
            //resetTokenOnUnAuthorizedError(retrofitError);
            postError(retrofitError);
        }

        return false;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    private void postError(final RetrofitError retrofitError) {
        eventing.post(new BackendResponse(1, retrofitError));
    }

    private void postOk(final Characteristics characteristics) {
        //eventing.post(new SendUserCharacteristicsToBackendResponseEvent(characteristics.getValue()));
        //set characterestics as syncronized .
        characteristics.setBackEndSynchronized(true);
        eventing.post(new DatabaseCharacteristicsUpdateRequest(characteristics));//instead of null ,we should get characteristic here
    }


    public List<UCoreCharacteristicsDetail> convertToUCoreCharacteristicsDetails(@NonNull final Collection<? extends CharacteristicsDetail> characteristicsDetails) {
        List<UCoreCharacteristicsDetail> uCoreCharacteristicsDetails = new ArrayList<>();
        for (CharacteristicsDetail characteristicsDetail : characteristicsDetails) {

                UCoreCharacteristicsDetail uCoreCharacteristicsDetail = new UCoreCharacteristicsDetail(characteristicsDetail.getType(), characteristicsDetail.getValue());
            uCoreCharacteristicsDetails.add(uCoreCharacteristicsDetail);

        }
        return uCoreCharacteristicsDetails;
    }


}