package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.DatabaseCharacteristicsUpdateRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public class UserCharacteristicsFetcher extends DataFetcher {
    String accessToken;
    @NonNull
    protected final UCoreAccessProvider accessProvider;
    private GsonConverter gsonConverter;
    DataServicesManager mDataServicesManager;

    @Inject
    public UserCharacteristicsFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                                      @NonNull final Eventing eventing,
                                      @NonNull final GsonConverter gsonConverter) {
        super(uCoreAdapter, eventing);
        this.gsonConverter = gsonConverter;
        mDataServicesManager = DataServicesManager.getInstance();
        this.accessProvider = mDataServicesManager.getUCoreAccessProvider();
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        return fetchCharacteristics();
    }

    @Nullable
    private RetrofitError fetchCharacteristics() {
        try {

            //get the json here ,Parse that Json to charecteristics ,set isSyncronized as true and call saveCharacteristics
            /* Added by pabitra */
            eventing.post(new DatabaseCharacteristicsUpdateRequest(null));//instead of null ,we should get characteristic here
            /* Added by pabitra end */

//            UserCharacteristicsClient userCharacteristicsClient = uCoreAdapter.getAppFrameworkClient(UserCharacteristicsClient.class, accessToken, gsonConverter);
//            final UserCharacteristics characteristics = userCharacteristicsClient.getUserCharacteristics(accessProvider.getUserId(), accessProvider.getUserId(), 9);
//
//            if (characteristics.getCharacteristics().size() > 0) {
//                Characteristic userCharacteristics = characteristics.getCharacteristics().get(0);
//                eventing.post(new SaveUserCharacteristicsEvent(userCharacteristics.getValue()));
//            }
        } catch (RetrofitError retrofitError) {
            return retrofitError;
        }
        return null;
    }
}
