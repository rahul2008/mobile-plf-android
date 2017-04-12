package cdp.philips.com.mydemoapp.blob;

import android.content.Intent;
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
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.activity.FilePicker;

import static android.app.Activity.RESULT_OK;

public class BlobFragment extends Fragment implements View.OnClickListener,DBFetchRequestListner<BlobMetaData> ,DBChangeListener{

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

        blobMetaDatas = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lv_blob_id);
        mRecyclerView.setLayoutManager(layoutManager);

        mBlobPresenter = new BlobPresenter(getActivity(),mProgressBar,mBtnUpload, this);


        blobMetaDataAdapter = new BlobMetaDataAdapter(getActivity(), blobMetaDatas, mBlobPresenter);
        mRecyclerView.setAdapter(blobMetaDataAdapter);
        mBlobPresenter.updateUI();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DataServicesManager.getInstance().registerDBChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
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
            case R.id.fetch:
                mBlobPresenter.fetchMetaDataForBlobID("14b4f366-2c3a-46f0-a870-658ee3eb7eb0");
                break;
        }
    }

    void browse() {
        Intent intent = new Intent(getActivity(), FilePicker.class);
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    public void onFetchSuccess(List<? extends BlobMetaData> data) {
     setBlobMetaDataList(data);
    }

    @Override
    public void onFetchFailure(Exception exception) {

    }

    void setBlobMetaDataList(final List<? extends BlobMetaData> blobMetaDataList){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                blobMetaDataAdapter.setData((ArrayList<? extends BlobMetaData>) blobMetaDataList);
                blobMetaDataAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        mBlobPresenter.updateUI();
    }

    @Override
    public void dBChangeFailed(Exception e) {

    }
}
