package com.example.cdpp.bluelibreferenceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Peripherals. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PeripheralDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PeripheralListActivity extends AppCompatActivity {

    private static final String TAG = "PeripheralListActivity";

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 1;
    private static final long SCAN_TIMEOUT_MS = 10000L;

    private SHNCentral mShnCentral;
    private SHNDeviceScanner mShnDeviceScanner;

    private List<SHNDevice> mAssociatedDevices;
    private List<SHNDevice> mFoundDevices = new ArrayList<SHNDevice>();
    private PeripheralListViewAdapter mPeripheralListViewAdapter;

    private Handler mHandler = new Handler(Looper.myLooper());

    private final SHNDeviceScanner.SHNDeviceScannerListener mDeviceScannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            Log.i(TAG, String.format("Device found: %s", shnDeviceFoundInfo.getDeviceName()));

            mFoundDevices.add(shnDeviceFoundInfo.getShnDevice());
            updateList();
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            Log.i(TAG, "Scan stopped.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.peripheral_list);
        setupRecyclerView(recyclerView);

        // Obtain SHNCentral instance
        mShnCentral = ReferenceApplication.get().getShnCentral();
        mShnDeviceScanner = mShnCentral.getShnDeviceScanner();

        Log.d(TAG, "Acquiring permission");
        acquirePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Initialize peripheral list
        mFoundDevices.clear();
        updateList();

        // Start scanning for Bluetooth peripherals
        mShnDeviceScanner.startScanning(mDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, SCAN_TIMEOUT_MS);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mShnDeviceScanner.stopScanning();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mPeripheralListViewAdapter = new PeripheralListViewAdapter(mFoundDevices);
        recyclerView.setAdapter(mPeripheralListViewAdapter);
    }

    private void updateList() {
        if (mPeripheralListViewAdapter == null) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mPeripheralListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public class PeripheralListViewAdapter
            extends RecyclerView.Adapter<PeripheralListViewAdapter.ViewHolder> {

        private final List<SHNDevice> mItems;

        public PeripheralListViewAdapter(List<SHNDevice> items) {
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.peripheral_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder, position: " + position);

            final SHNDevice device = mItems.get(position);

            holder.mItem = device;
            holder.mNameView.setText(mItems.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReferenceApplication.get().setSelectedDevice(holder.mItem);

                    Context context = v.getContext();
                    Intent intent = new Intent(context, PeripheralDetailActivity.class);
                    intent.putExtra(PeripheralDetailActivity.ARG_ITEM_ID, position);

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public SHNDevice mItem;

            public ViewHolder(View view) {
                super(view);

                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }

    private void acquirePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }
        }
    }
}
