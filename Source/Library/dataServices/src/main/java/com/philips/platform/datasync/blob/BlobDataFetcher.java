package com.philips.platform.datasync.blob;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.listeners.BlobDownloadRequestListener;
import com.philips.platform.core.listeners.BlobUploadRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.squareup.okhttp.ResponseBody;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;


public class BlobDataFetcher extends DataFetcher{

    @Inject
    Eventing eventing;

    @Inject
    UCoreAccessProvider accessProvider;

    @Inject
    GsonConverter gsonConverter;

    @Inject
    public BlobDataFetcher(@NonNull UCoreAdapter uCoreAdapter) {
        super(uCoreAdapter);
        DataServicesManager.getInstance().getAppComponant().injectblobDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        return null;
    }

    @Nullable
    public void fetchBlobData(@Nullable String itemId, BlobDownloadRequestListener blobDownloadRequestListener) {
        if (isUserInvalid()) {
            return;
        }
        try {

            BlobClient client = uCoreAdapter.getAppFrameworkClient(BlobClient.class, accessProvider.getAccessToken(), gsonConverter);
            Response downloadResponse = null;
            if (client != null) {
                downloadResponse = client.downloadBlob(itemId);
            }
            InputStream in = downloadResponse.getBody().in();
            blobDownloadRequestListener.onBlobDownloadRequestSuccess(in);
        } catch (RetrofitError ex) {
            DSLog.e(DSLog.LOG, "RetrofitError: " + ex.getMessage() + ex);
            eventing.post(new BackendDataRequestFailed(ex));
            onError(ex);
            blobDownloadRequestListener.onBlobRequestFailure(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean isUserInvalid() {
        final String accessToken = accessProvider.getAccessToken();
        return !accessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }
}
