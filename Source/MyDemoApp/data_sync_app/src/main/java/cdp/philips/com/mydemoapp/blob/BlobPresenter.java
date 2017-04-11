package cdp.philips.com.mydemoapp.blob;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit.mime.MimeUtil;


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
            showToast("Mime  Type invalid - choose another file",null);
            setProgressBarVisibility(false);
            return;
        }

        DataServicesManager.getInstance().createBlob(selectedFile, mimeType, new BlobUploadRequestListener() {
            @Override
            public void onBlobRequestSuccess(final String itemId) {
                setUploadButtonState();
                setProgressBarVisibility(false);
                showToast("Blob Request Succes and the itemID = " + itemId, null);
                fetchMetaDataForBlobID(itemId.trim());
            }

            @Override
            public void onBlobRequestFailure(Exception exception) {
                setProgressBarVisibility(false);
                showToast("Blob Request Failed",exception);
                setUploadButtonState();
            }
        });
    }

    private void setUploadButtonState() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

    private void showToast(final String message, final Exception exception) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(exception!=null && exception.getMessage()!=null) {
                    Toast.makeText(activity, message + " and Error -> " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void download(BlobMetaData blobMetaData) {
        setProgressBarVisibility(true);
        DataServicesManager.getInstance().fetchBlobWithMetaData(blobMetaData, new BlobDownloadRequestListener() {

            @Override
            public void onBlobDownloadRequestSuccess(InputStream file,final String mime) {
                showToast("Blob Download Request Success",null);
                copyInputStreamToFile(file, MimeTypeMap.getSingleton().getExtensionFromMimeType(mime));
                setProgressBarVisibility(false);
            }

            @Override
            public void onBlobRequestFailure(Exception exception) {
                showToast("Blob Request Failed",exception);
                setProgressBarVisibility(false);
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
                updateUI();
            }
        });
    }

    void updateUI() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataServicesManager.getInstance().fetchAllMetaData(dbFetchRequestListner);
            }
        });
    }

    private void copyInputStreamToFile(InputStream inputStream, String extension) {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            File targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"targetFile."+extension);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            showToast("Stored the file successfully",null);
        } catch (IOException e) {
            showToast("File could not be stored",e);
        }
    }

    private String getAppDirectory(){
        PackageManager m = activity.getPackageManager();
        String s = activity.getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        s = p.applicationInfo.dataDir;
        return s;
    }
}
