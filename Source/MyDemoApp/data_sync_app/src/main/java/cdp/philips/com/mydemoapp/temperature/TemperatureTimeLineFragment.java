package cdp.philips.com.mydemoapp.temperature;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.DataSyncApplication;
import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;
import cdp.philips.com.mydemoapp.reciever.BaseAppBroadcastReceiver;
import retrofit.RetrofitError;

import static android.content.Context.ALARM_SERVICE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureTimeLineFragment extends Fragment implements View.OnClickListener, DBChangeListener, SwipeRefreshLayout.OnRefreshListener{
    public static final String TAG = TemperatureTimeLineFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<? extends Moment> mData = new ArrayList();
    private TemperatureTimeLineFragmentcAdapter mAdapter ;
    AlarmManager alarmManager;

    ImageButton mAddButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TemperaturePresenter mTemperaturePresenter;
    private ProgressDialog mProgressDialog = null;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelPendingIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        mAdapter = new TemperatureTimeLineFragmentcAdapter(getContext(),mData);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAddButton = (ImageButton) view.findViewById(R.id.add);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView.setAdapter(mAdapter);
        mAddButton.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void addMoment() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle("Create A Moment");
        final EditText temperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        final EditText location = (EditText) dialog.findViewById(R.id.location_detail);
        final EditText phase = (EditText) dialog.findViewById(R.id.phase_detail);
        final Button dialogButton = (Button) dialog.findViewById(R.id.save);
        dialogButton.setEnabled(false);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createAndSaveMoment(phase.getText().toString(), temperature.getText().toString(),
                        location.getText().toString()   );
                dialog.dismiss();
            }
        });

        phase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        temperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if(phase.getText().toString()!=null && !phase.getText().toString().isEmpty() && temperature.getText().toString()!=null && !temperature.getText().toString().isEmpty() && location.getText().toString()!=null && !location.getText().toString().isEmpty()){
                    dialogButton.setEnabled(true);
                }else{
                    dialogButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        dialog.show();
    }

    private void createAndSaveMoment(final String phaseInput, final String temperatureInput, final String locationInput) {
        mTemperaturePresenter.createMoment(phaseInput, temperatureInput, locationInput);
        mTemperaturePresenter.saveRequest();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE);
        ((DataSyncApplication) getActivity().getApplication()).getAppComponent().injectFragment(this);
        EventHelper.getInstance().registerEventNotification(EventHelper.MOMENT,this);
        mTemperaturePresenter = new TemperaturePresenter(getContext(), MomentType.TEMPERATURE);
         mTemperaturePresenter.fetchData();
        setUpBackendSynchronizationLoop();
        //mTemperaturePresenter.startSync();
    }

    private void setUpBackendSynchronizationLoop() {
        PendingIntent dataSyncIntent = getPendingIntent();

        // Start the first time after 5 seconds
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 5 * 1000;

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, BaseAppBroadcastReceiver.DATA_FETCH_FREQUENCY, dataSyncIntent);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), BaseAppBroadcastReceiver.class);
        intent.setAction(BaseAppBroadcastReceiver.ACTION_USER_DATA_FETCH);
        return PendingIntent.getBroadcast(getContext(), 0, intent, 0);
    }

    public void cancelPendingIntent() {
        PendingIntent dataSyncIntent = getPendingIntent();
        dataSyncIntent.cancel();
        alarmManager.cancel(dataSyncIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHelper.getInstance().unregisterEventNotification(EventHelper.MOMENT,this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add:
                addMoment();
                break;
        }
    }

    @Override
    public void onSuccess(final ArrayList<? extends Object> data) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"http : UI updated");
                mData = (ArrayList<? extends Moment>) data;
                mAdapter.setData(mData);
                mAdapter.notifyDataSetChanged();
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public void onSuccess(final Object data) {
        mTemperaturePresenter.fetchData();
    }

    @Override
    public void onFailure(final Exception exception) {
        onFailureRefresh(exception);
    }

    private void onFailureRefresh(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e != null && e.getMessage() != null) {
                    Log.i(TAG, "http : UI update Failed" + e.getMessage());
                    if (getContext() != null)
                        Toast.makeText(getContext(), "UI update Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "http : UI update Failed");
                    if (getContext() != null)
                        Toast.makeText(getContext(), "UI update Failed", Toast.LENGTH_SHORT).show();
                }
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if(!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        mTemperaturePresenter.startSync();
    }
}
