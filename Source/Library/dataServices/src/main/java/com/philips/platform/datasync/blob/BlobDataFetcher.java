package com.philips.platform.datasync.blob;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.events.FetchMetaDataRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.mime.TypedFile;


public class BlobDataFetcher extends DataFetcher{

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @NonNull
    UCoreAdapter uCoreAdapter;

    @Inject
    public BlobDataFetcher(@NonNull UCoreAdapter uCoreAdapter, UCoreAccessProvider uCoreAccessProvider, MomentGsonConverter gsonConverter) {
        super(uCoreAdapter);
        this.uCoreAccessProvider = uCoreAccessProvider;
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        DataServicesManager.getInstance().getAppComponant().injectBlobDataFetcher(this);
    }

    @Inject
    MomentGsonConverter gsonConverter;


    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        return null;
    }

    public void fetchBlobMetaData(FetchMetaDataRequest fetchMetaDataRequest) {

        try{

        BlobClient service = uCoreAdapter.getAppFrameworkClient(BlobClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
        UcoreBlobMetaData ucoreBlobMetaData = service.fetchMetaData(fetchMetaDataRequest.getBlobID());

        if(ucoreBlobMetaData == null){
            fetchMetaDataRequest.getBlobRequestListener().onBlobRequestFailure(new Exception("Server returned null response"));
        }else {
            fetchMetaDataRequest.getBlobRequestListener().onFetchMetaDataSuccess(ucoreBlobMetaData);
        }
    }catch (RetrofitError error){
        error.printStackTrace();
            fetchMetaDataRequest.getBlobRequestListener().onBlobRequestFailure(error);
    }

    }

    public void fetchBlob(){

    }
}
