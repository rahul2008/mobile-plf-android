package cdp.philips.com.mydemoapp.blob;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.philips.cdp.uikit.customviews.CircularProgressbar;
import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.UcoreBlobMetaData;

import java.io.File;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.activity.FilePicker;

import static android.app.Activity.RESULT_OK;
import static cdp.philips.com.mydemoapp.R.drawable.file;

public class BlobFragment extends Fragment {

    Button mBtnUpload;
    private ProgressBar mProgressBar;
    private static final int REQUEST_PICK_FILE = 1;

    private Button Browse;
    private File selectedFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.blob_layout, container, false);
        mBtnUpload = (Button) view.findViewById(R.id.upload);
        mProgressBar = (CircularProgressbar) view.findViewById(R.id.settings_progress_bar);

        Browse = (Button) view.findViewById(R.id.browse);
        Browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shouldAskPermissions()) {
                    askPermissions();
                }

                Intent intent = new Intent(getActivity(), FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setProgressBarVisibility(true);

                //File file = new File("/storage/3038-3435/619.jpg");
                DataServicesManager.getInstance().createBlob(selectedFile, getMimeType(selectedFile.getPath()), new BlobRequestListener() {
                    @Override
                    public void onBlobRequestSuccess(String itemId) {
                        setProgressBarVisibility(false);
                        showToast("Blob Request Succes and the itemID = " + itemId);
                    }

                    @Override
                    public void onBlobRequestFailure(Exception exception) {
                        setProgressBarVisibility(false);
                        showToast("Blob Request Failed");
                    }

                    @Override
                    public void onFetchMetaDataSuccess(UcoreBlobMetaData uCoreFetchMetaData) {
                        setProgressBarVisibility(false);
                        showToast("Blob Meta Data Request Succes");
                    }
                });
            }
        });
        return view;
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    public void setProgressBarVisibility(final boolean isVisible) {

        getActivity().runOnUiThread(new Runnable() {
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

    private void showToast(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            switch(requestCode) {

                case REQUEST_PICK_FILE:

                    if(data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        //filePath.setText(selectedFile.getPath());
                        mBtnUpload.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }


}
