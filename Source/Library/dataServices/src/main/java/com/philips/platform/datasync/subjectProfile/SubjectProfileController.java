package com.philips.platform.datasync.subjectProfile;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.GetSubjectProfileListResponseEvent;
import com.philips.platform.core.events.GetSubjectProfileResponseEvent;
import com.philips.platform.core.events.SubjectProfileErrorResponseEvent;
import com.philips.platform.core.events.SubjectProfileResponseEvent;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.core.utils.DataServicesErrorConstants;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class SubjectProfileController {
    public static final String TAG = "SubjectProfileController";

    @Inject
    UCoreAccessProvider mUCoreAccessProvider;

    @Inject
    Eventing mEventing;

    @NonNull
    private final UCoreAdapter mUCoreAdapter;

    @NonNull
    private final GsonConverter mGsonConverter;

    private SubjectProfileClient mSubjectProfileClient;

    @Inject
    public SubjectProfileController(@NonNull UCoreAdapter uCoreAdapter,
                                    @NonNull GsonConverter gsonConverter) {
        mUCoreAdapter = uCoreAdapter;
        mGsonConverter = gsonConverter;
    }

    public boolean createSubjectProfile(UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest) {
        if (isUserInvalid()) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mSubjectProfileClient = mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class,
                mUCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mSubjectProfileClient == null) return false;

        try {
            Response response = mSubjectProfileClient.createSubjectProfile(mUCoreAccessProvider.getUserId(),
                    uCoreCreateSubjectProfileRequest);
            mEventing.post(new SubjectProfileResponseEvent(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }

        return false;
    }

    public boolean getSubjectProfileList() {
        if (isUserInvalid()) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mSubjectProfileClient = mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class,
                mUCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mSubjectProfileClient == null) return false;

        try {
            UCoreSubjectProfileList uCoreSubjectProfileList = mSubjectProfileClient.getSubjectProfiles(mUCoreAccessProvider.getUserId());
            mEventing.post(new GetSubjectProfileListResponseEvent(uCoreSubjectProfileList));
        } catch (RetrofitError error) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }

        return false;
    }

    public boolean getSubjectProfile(String subjectID) {
        if (isUserInvalid()) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mSubjectProfileClient = mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class,
                mUCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mSubjectProfileClient == null) return false;

        try {
            UCoreSubjectProfile uCoreSubjectProfile = mSubjectProfileClient.getSubjectProfile(mUCoreAccessProvider.getUserId(), subjectID);
            mEventing.post(new GetSubjectProfileResponseEvent(uCoreSubjectProfile));
        } catch (RetrofitError error) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }

        return false;
    }

    public boolean deleteSubjectProfile(String subjectID) {
        if (isUserInvalid()) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(DataServicesErrorConstants.DS_INVALID_USER_ERROR_CODE,
                    DataServicesErrorConstants.DS_INVALID_USER_ERROR_MESSAGE)));
            return false;
        }

        mSubjectProfileClient = mUCoreAdapter.getAppFrameworkClient(SubjectProfileClient.class,
                mUCoreAccessProvider.getAccessToken(), mGsonConverter);

        if (mSubjectProfileClient == null) return false;

        try {
            Response response = mSubjectProfileClient.deleteSubjectProfile(mUCoreAccessProvider.getUserId(), subjectID);
            mEventing.post(new SubjectProfileResponseEvent(isResponseSuccess(response)));
        } catch (RetrofitError error) {
            mEventing.post(new SubjectProfileErrorResponseEvent(createDataServicesError(error.getResponse().getStatus(), error.getMessage())));
        }

        return false;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    public boolean isUserInvalid() {
        if (mUCoreAccessProvider != null) {
            String accessToken = mUCoreAccessProvider.getAccessToken();
            return !mUCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
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
