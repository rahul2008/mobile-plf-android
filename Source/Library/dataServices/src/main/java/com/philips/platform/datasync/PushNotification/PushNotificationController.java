package com.philips.platform.datasync.PushNotification;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.PushNotificationResponse;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class PushNotificationController {
    public static final String TAG = "PushNotificationController";

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @Inject
    Eventing eventing;

    @NonNull
    private final UCoreAdapter mUCoreAdapter;

    @NonNull
    private final GsonConverter mGsonConverter;

    private PushNotificationClient mPushNotificationClient;

    @Inject
    public PushNotificationController(@NonNull UCoreAdapter uCoreAdapter,
                                      @NonNull GsonConverter gsonConverter) {
        mUCoreAdapter = uCoreAdapter;
        mGsonConverter = gsonConverter;

    }

    public boolean registerPushNotification(UCorePushNotification uCorePushNotification) {
        if (isUserInvalid()) {
            postError(1, RetrofitError.unexpectedError("", new IllegalStateException("Please login")));
            return false;
        }

        mPushNotificationClient = mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);
        try {
            Response response = mPushNotificationClient.registerDeviceToken(uCoreAccessProvider.getUserId(),
                    uCoreAccessProvider.getUserId(), 13, uCorePushNotification);
            eventing.post(new PushNotificationResponse(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    public boolean unRegisterPushNotification(String appVariant, String token) {
        if (isUserInvalid()) {
            postError(1, RetrofitError.unexpectedError("", new IllegalStateException("Please login")));
            return false;
        }

        mPushNotificationClient = mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);
        try {
            Response response = mPushNotificationClient.unRegisterDeviceToken(uCoreAccessProvider.getUserId(),
                    uCoreAccessProvider.getUserId(), 13, appVariant, token);
            eventing.post(new PushNotificationResponse(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    public boolean isUserInvalid() {
        if (uCoreAccessProvider != null) {
            String accessToken = uCoreAccessProvider.getAccessToken();
            return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
        }
        return false;
    }

    void postError(int referenceId, final RetrofitError error) {
        eventing.post(new BackendResponse(referenceId, error));
    }
}
