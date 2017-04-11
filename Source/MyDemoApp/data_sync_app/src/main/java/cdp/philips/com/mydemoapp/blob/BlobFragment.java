package cdp.philips.com.mydemoapp.blob;

import android.annotation.TargetApi;
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
import android.widget.Button;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.customviews.CircularProgressbar;
import com.philips.platform.core.listeners.BlobRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.io.File;
import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.activity.FilePicker;

import static android.app.Activity.RESULT_OK;

public class BlobFragment extends Fragment implements View.OnClickListener {

    Button mBtnUpload;
    private ProgressBar mProgressBar;


    private Button Browse, Fetch;
    private File selectedFile;
    private RecyclerView mRecyclerView;
    private ArrayList<BlobMetaData> blobMetaDatas;
    private BlobMetaDataAdapter blobMetaDataAdapter;
    private Button mBtnDownload;
    private BlobPresenter mBlobPresenter;
    private static final int REQUEST_PICK_FILE = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.blob_layout, container, false);
        mBtnUpload = (Button) view.findViewById(R.id.upload);
        mBtnDownload = (Button) view.findViewById(R.id.download);
        mProgressBar = (CircularProgressbar) view.findViewById(R.id.settings_progress_bar);
        Fetch = (Button) view.findViewById(R.id.fetch);
        Browse = (Button) view.findViewById(R.id.browse);

        Browse.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
        mBtnDownload.setOnClickListener(this);
        Fetch.setOnClickListener(this);

        mBlobPresenter = new BlobPresenter(getActivity(),mProgressBar,mBtnUpload);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lv_blob_id);
        mRecyclerView.setLayoutManager(layoutManager);
        blobMetaDatas = new ArrayList<>();
        blobMetaDataAdapter = new BlobMetaDataAdapter(getActivity(), blobMetaDatas);
        mRecyclerView.setAdapter(blobMetaDataAdapter);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_PICK_FILE:

                    if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        mBtnUpload.setEnabled(true);
                    }
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browse:
                browse();
                break;
            case R.id.upload:
                mBlobPresenter.upload(selectedFile);
                break;
            case R.id.download:
                mBlobPresenter.download();
                break;
            case R.id.fetch:
                mBlobPresenter.fetchMetaDataForBlobID();
                break;
        }
    }

    void browse() {
        Intent intent = new Intent(getActivity(), FilePicker.class);
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }
}
