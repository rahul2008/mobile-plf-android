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
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.activity.FilePicker;

public class BlobPresenter {

    private Activity activity;
    private Button mBtnUpload;
    private ProgressBar mProgressBar;
    private final DBFetchRequestListner<BlobMetaData> dbFetchRequestListner;

    BlobPresenter(Activity activity, ProgressBar progressBar, Button upload, DBFetchRequestListner<BlobMetaData> dbFetchRequestListner){
        this.activity = activity;
        this.mProgressBar = progressBar;
        this.mBtnUpload = upload;

        this.dbFetchRequestListner = dbFetchRequestListner;
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
            public void onBlobRequestSuccess(final String itemId) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnUpload.setEnabled(false);
                        setProgressBarVisibility(false);
                        showToast("Blob Request Succes and the itemID = " + itemId);
                    }
                });
                fetchMetaDataForBlobID(itemId.trim());
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

    void download(String blobId) {
        DataServicesManager.getInstance().fetchBlob(blobId, new BlobDownloadRequestListener() {

            @Override
            public void onBlobDownloadRequestSuccess(final InputStream file,final String mime) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Blob Download Request Success");
                    }
                });

            }

            @Override
            public void onBlobRequestFailure(Exception exception) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Blob Request Failed");
                    }
                });

            }
        });
    }

    void fetchMetaDataForBlobID(String blobID){
        DataServicesManager.getInstance().fetchMetaDataForBlobID(blobID, new BlobRequestListener() {
            @Override
            public void onBlobRequestSuccess(String itemID) {

            }

            @Override
            public void onBlobRequestFailure(Exception exception) {

            }

            @Override
            public void onFetchMetaDataSuccess(BlobMetaData uCoreFetchMetaData) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });

            }
        });
    }

    void updateUI() {
        DataServicesManager.getInstance().fetchAllMetaData(dbFetchRequestListner);
    }


}
