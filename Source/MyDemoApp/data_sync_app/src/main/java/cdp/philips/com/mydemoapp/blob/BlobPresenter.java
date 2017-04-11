package cdp.philips.com.mydemoapp.blob;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.philips.platform.core.listeners.BlobDownloadRequestListener;
import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.core.listeners.BlobUploadRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.io.File;
import java.io.InputStream;

import cdp.philips.com.mydemoapp.activity.FilePicker;

public class BlobPresenter {

    private Activity activity;
    private Button mBtnUpload;
    private ProgressBar mProgressBar;

    BlobPresenter(Activity activity, ProgressBar progressBar, Button upload){
        this.activity = activity;
        this.mProgressBar = progressBar;
        this.mBtnUpload = upload;
    }

    void upload(File selectedFile) {
        setProgressBarVisibility(true);
        final String mimeType = getMimeType(selectedFile.getPath());

        if(mimeType == null){
            showToast("Mime  Type invalid - choose another file");
            setProgressBarVisibility(false);
            return;
        }

        DataServicesManager.getInstance().createBlob(selectedFile, mimeType, new BlobUploadRequestListener() {
            @Override
            public void onBlobRequestSuccess(String itemId) {
                setProgressBarVisibility(false);
                showToast("Blob Request Succes and the itemID = " + itemId);
                DataServicesManager.getInstance().fetchMetaDataForBlobID(itemId, new BlobRequestListener() {
                    @Override
                    public void onBlobRequestSuccess(String itemID) {

                    }

                    @Override
                    public void onBlobRequestFailure(Exception exception) {

                    }

                    @Override
                    public void onFetchMetaDataSuccess(BlobMetaData uCoreFetchMetaData) {

                    }
                });
                mBtnUpload.setEnabled(false);
            }

            @Override
            public void onBlobRequestFailure(Exception exception) {
                setProgressBarVisibility(false);
                showToast("Blob Request Failed");
                mBtnUpload.setEnabled(false);
            }
        });
    }

    public void setProgressBarVisibility(final boolean isVisible) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isVisible) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }

    private void showToast(final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void download() {
        DataServicesManager.getInstance().fetchBlob("14b4f366-2c3a-46f0-a870-658ee3eb7eb0", new BlobDownloadRequestListener() {

            @Override
            public void onBlobDownloadRequestSuccess(InputStream file) {
                showToast("Blob Download Request Success");
            }

            @Override
            public void onBlobRequestFailure(Exception exception) {
                showToast("Blob Request Failed");
            }
        });
    }

    void fetchMetaDataForBlobID(){
        DataServicesManager.getInstance().fetchMetaDataForBlobID("14b4f366-2c3a-46f0-a870-658ee3eb7eb0", new BlobRequestListener() {
            @Override
            public void onBlobRequestSuccess(String itemID) {

            }

            @Override
            public void onBlobRequestFailure(Exception exception) {

            }

            @Override
            public void onFetchMetaDataSuccess(BlobMetaData uCoreFetchMetaData) {

            }
        });
    }
}
