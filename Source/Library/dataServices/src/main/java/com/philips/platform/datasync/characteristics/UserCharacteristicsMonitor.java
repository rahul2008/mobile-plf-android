package com.philips.platform.datasync.characteristics;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.platform.core.datatypes.Characteristic;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.FetchUserCharacteristicsFromBackendEvent;
import com.philips.platform.core.events.UserCharacteristicsToBackendEvent;
import com.philips.platform.core.monitors.EventMonitor;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by indrajitkumar on 1/2/17.
 */

public class UserCharacteristicsMonitor extends EventMonitor {
    private UserCharacteristicsSender userCharacteristicsSender;
    private UserCharacteristicsFetcher userCharacteristicsFetcher;

    @Inject
    public UserCharacteristicsMonitor(@NonNull final UserCharacteristicsSender bookmarkSender, final UserCharacteristicsFetcher bookmarkFetcher) {
        this.userCharacteristicsSender = userCharacteristicsSender;
        this.userCharacteristicsFetcher = userCharacteristicsFetcher;
    }

    public void onEventAsync(UserCharacteristicsToBackendEvent sendBookmarkToBackendEvent) {
        List<String> articleUid = sendBookmarkToBackendEvent.getArticleUid();
        String characteristicsValue = TextUtils.join(",", articleUid);

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(new Characteristic(Characteristic.USER_CHARACTERISTIC_TYPE, characteristicsValue)));
    }

    public void onEventAsync(FetchUserCharacteristicsFromBackendEvent fetchBookmarkFromBackendEvent) {
        userCharacteristicsFetcher.fetchDataSince(null);
    }
}