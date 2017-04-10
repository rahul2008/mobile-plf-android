package com.philips.platform.datasync.blob;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;

import org.joda.time.DateTime;

import javax.inject.Inject;

import retrofit.RetrofitError;


public class BlobDataFetcher extends DataFetcher{

    @Inject
    public BlobDataFetcher(@NonNull UCoreAdapter uCoreAdapter) {
        super(uCoreAdapter);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        return null;
    }
}
