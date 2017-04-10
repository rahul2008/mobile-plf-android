package com.philips.platform.datasync.blob;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.CreateBlobRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by philips on 4/5/17.
 */

public class BlobMonitor extends EventMonitor {

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @NonNull
    private final BlobDataSender blobDataSender;

    @NonNull
    private final BlobDataFetcher blobDataFetcher;


    @Inject
    public BlobMonitor(@NonNull BlobDataSender blobDataSender, @NonNull BlobDataFetcher blobDataFetcher) {
        this.blobDataSender = blobDataSender;
        this.blobDataFetcher = blobDataFetcher;
        DataServicesManager.getInstance().getAppComponant().injectBlobMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(CreateBlobRequest event) {
        BlobData data = new BlobData();
        data.setType(event.getType());
        data.setFile(event.getInputStream());
        data.setBlobRequestListener(event.getBlobRequestListener());

        List list = new ArrayList();
        list.add(data);

        blobDataSender.sendDataToBackend(list);
    }
}
