package cdp.philips.com.mydemoapp.blob;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.philips.cdp.uikit.customviews.CircularProgressbar;
import com.philips.platform.core.listeners.BlobDownloadRequestListener;
import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.core.listeners.BlobUploadRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.activity.FilePicker;
import static android.app.Activity.RESULT_OK;

public class BlobFragment extends Fragment implements View.OnClickListener {

    Button mBtnUpload;
    private ProgressBar mProgressBar;
    private static final int REQUEST_PICK_FILE = 1;

    private Button Browse,Fetch;
    private File selectedFile;
    private RecyclerView mRecyclerView;
    private ArrayList<BlobMetaData> blobMetaDatas;
    private BlobMetaDataAdapter blobMetaDataAdapter;
    private Button mBtnDownload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.blob_layout, container, false);
        mBtnUpload = (Button) view.findViewById(R.id.upload);
        mBtnDownload = (Button) view.findViewById(R.id.download);
        mProgressBar = (CircularProgressbar) view.findViewById(R.id.settings_progress_bar);
        Fetch=(Button) view.findViewById(R.id.fetch);

        Fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataServicesManager.getInstance().FetchMetaDataForBlobID("14b4f366-2c3a-46f0-a870-658ee3eb7eb0", new BlobRequestListener() {
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
        });

        Browse = (Button) view.findViewById(R.id.browse);

        Browse.setOnClickListener(this);

        mBtnUpload.setOnClickListener(this);

        mBtnDownload.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView=(RecyclerView) view.findViewById(R.id.lv_blob_id) ;
        mRecyclerView.setLayoutManager(layoutManager);
        blobMetaDatas=new ArrayList<>();
        blobMetaDataAdapter = new BlobMetaDataAdapter(getActivity(), blobMetaDatas);
        mRecyclerView.setAdapter(blobMetaDataAdapter);
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

    private void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_PICK_FILE:

                    if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        //filePath.setText(selectedFile.getPath());
                        mBtnUpload.setEnabled(true);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browse:
                if (shouldAskPermissions()) {
                    askPermissions();
                }

                Intent intent = new Intent(getActivity(), FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
                break;
            case R.id.upload:
                setProgressBarVisibility(true);

                DataServicesManager.getInstance().createBlob(selectedFile, getMimeType(selectedFile.getPath()), new BlobUploadRequestListener() {
                    @Override
                    public void onBlobRequestSuccess(String itemId) {
                        setProgressBarVisibility(false);
                        showToast("Blob Request Succes and the itemID = " + itemId);
                        DataServicesManager.getInstance().FetchMetaDataForBlobID(itemId, new BlobRequestListener() {
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
                break;
            case R.id.download:
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
                break;
        }
    }
}
