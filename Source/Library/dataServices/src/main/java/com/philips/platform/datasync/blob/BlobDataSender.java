package com.philips.platform.datasync.blob;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.SavingBlobMetaDataRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.BlobDataCreater;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

public class BlobDataSender extends DataSender {

    @Inject
    BlobDataSender() {
        DataServicesManager.getInstance().getAppComponant().injectBlobSender(this);
    }

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @Inject
    UCoreAdapter uCoreAdapter;

    @Inject
    Eventing eventing;

    @Inject
    BaseAppDataCreator dataCreator;


    @Inject
    MomentGsonConverter gsonConverter;

    @Override
    public boolean sendDataToBackend(@NonNull List dataToSend) {
        if (dataToSend == null && dataToSend.size() != 0) return false;
        DSLog.i(DSLog.LOG, "sendDataToBackend DataSender sendDataToBackend data = " + dataToSend.toString());
        if (!uCoreAccessProvider.isLoggedIn()) {
            return false;
        }

        for (Object data : dataToSend)
            uploadBlob((BlobData) data);

        return true;

    }

    @Override
    public Class getClassForSyncData() {
        return null;
    }


    private void uploadBlob(final BlobData blobData) {
        uCoreAdapter.setBlobData(blobData);
        BlobClient service = uCoreAdapter.getAppFrameworkClient(BlobClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
        TypedFile typedFile = new TypedFile(blobData.getType(), blobData.getFile());

        try {
            UcoreBlobResponse response = service.uploadBlob(typedFile);

            if(response == null){
                blobData.getBlobRequestListener().onBlobRequestFailure(new Exception("Server returned null response"));
            }else {
                BlobMetaData blobMetaData = ((BlobDataCreater)dataCreator).createBlobMetaData();
                blobMetaData.setBlobId(response.getItemId());
                eventing.post(new SavingBlobMetaDataRequest(blobMetaData));

                blobData.getBlobRequestListener().onBlobRequestSuccess(response.getItemId());
            }
        }catch (RetrofitError error){
            error.printStackTrace();
            blobData.getBlobRequestListener().onBlobRequestFailure(error);
        }

    }
}
