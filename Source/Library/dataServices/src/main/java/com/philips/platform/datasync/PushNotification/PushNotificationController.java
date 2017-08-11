package com.philips.platform.datasync.PushNotification;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.PushNotificationErrorResponse;
import com.philips.platform.core.events.PushNotificationResponse;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.core.utils.DataServicesErrorConstants;
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
            eventing.post(new PushNotificationErrorResponse(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mPushNotificationClient = mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);
        if(mPushNotificationClient==null) return false;
        try {
            Response response = mPushNotificationClient.registerDeviceToken(uCoreAccessProvider.getUserId(),
                    uCoreAccessProvider.getUserId(), 13, uCorePushNotification);
            eventing.post(new PushNotificationResponse(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            eventing.post(new PushNotificationErrorResponse(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }
        return false;
    }

    public boolean unRegisterPushNotification(String appVariant, String token) {
        if (isUserInvalid()) {
            eventing.post(new PushNotificationErrorResponse(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mPushNotificationClient = mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);
        if(mPushNotificationClient==null) return false;
        try {
            Response response = mPushNotificationClient.unRegisterDeviceToken(uCoreAccessProvider.getUserId(),
                    uCoreAccessProvider.getUserId(), 13, appVariant, token);
            eventing.post(new PushNotificationResponse(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            eventing.post(new PushNotificationErrorResponse(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
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

    protected DataServicesError createDataServicesError(int errorCode, String errorMessage) {
        DataServicesError dataServicesError = new DataServicesError();
        dataServicesError.setErrorCode(errorCode);
        dataServicesError.setErrorMessage(errorMessage);
        return dataServicesError;
    }
}
