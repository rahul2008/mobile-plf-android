package com.philips.platform.datasync.devicePairing;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.DevicePairingErrorResponseEvent;
import com.philips.platform.core.events.DevicePairingResponseEvent;
import com.philips.platform.core.events.GetPairedDevicesResponseEvent;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.core.utils.DataServicesErrorConstants;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class DevicePairingController {

    public static final String TAG = "DevicePairingController";

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @Inject
    Eventing mEventing;

    @NonNull
    private final UCoreAdapter mUCoreAdapter;

    @NonNull
    private final GsonConverter mGsonConverter;

    private DevicePairingClient mDevicePairingClient;

    @Inject
    public DevicePairingController(@NonNull UCoreAdapter uCoreAdapter,
                                   @NonNull GsonConverter gsonConverter) {
        mUCoreAdapter = uCoreAdapter;
        mGsonConverter = gsonConverter;
    }

    public boolean pairDevices(UCoreDevicePair uCoreDevicePair) {
        if (isUserInvalid()) {
            mEventing.post(new DevicePairingErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mDevicePairingClient = mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mDevicePairingClient == null) return false;

        try {
            Response response = mDevicePairingClient.pairDevice(uCoreAccessProvider.getUserId(), 12,
                    uCoreAccessProvider.getUserId(), uCoreDevicePair);
            mEventing.post(new DevicePairingResponseEvent(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            mEventing.post(new DevicePairingErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }

        return false;
    }

    public boolean unPairDevice(String deviceID) {
        if (isUserInvalid()) {
            mEventing.post(new DevicePairingErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mDevicePairingClient = mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mDevicePairingClient == null) return false;

        try {
            Response response = mDevicePairingClient.unPairDevice(uCoreAccessProvider.getUserId(), 12,
                    uCoreAccessProvider.getUserId(), deviceID);
            mEventing.post(new DevicePairingResponseEvent(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            mEventing.post(new DevicePairingErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }
        return false;
    }

    public boolean getPairedDevices() {
        if (isUserInvalid()) {
            mEventing.post(new DevicePairingErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mDevicePairingClient = mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class,
                uCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mDevicePairingClient == null) return false;

        try {
            List<String> pairedDevices = mDevicePairingClient.getPairedDevices(uCoreAccessProvider.getUserId(), 12, uCoreAccessProvider.getUserId());
            mEventing.post(new GetPairedDevicesResponseEvent(pairedDevices));
        } catch (RetrofitError error) {
            mEventing.post(new DevicePairingErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
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
