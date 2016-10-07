package cdp.philips.com.mydemoapp.datasync.temperature;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.events.ListSaveResponse;
import com.philips.platform.core.events.LoadMomentsResponse;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import cdp.philips.com.mydemoapp.DataSyncApplicationClass;
import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.datasync.trackers.TemperatureTracker;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureTimeLineFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = TemperatureTimeLineFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<? extends Moment> mData = new ArrayList();
    private TemperatureTimeLineFragmentcAdapter mAdapter ;

// 	private int mMomentItemSelectedPosition = 0;

    @Inject
    Eventing eventing;

    @Inject
    DataPullSynchronise mDataPullSynchronise;

    @Inject
    DataPushSynchronise mDataPushSynchronise;

    Button mAddButton;
    TemperatureTracker mTemperatureMoment;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        mAdapter = new TemperatureTimeLineFragmentcAdapter(getContext(), eventing,mData);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAddButton = (Button) view.findViewById(R.id.add);
        mRecyclerView.setAdapter(mAdapter);
        mAddButton.setOnClickListener(this);
        return view;
    }

    private void addMoment() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.af_datasync_create_moment_pop_up);
        dialog.setTitle("Create A Moment");

        //final EditText phase = (EditText) dialog.findViewById(R.id.phase_detail);
        final String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        //phase.setText(mydate);
        final EditText temperature = (EditText) dialog.findViewById(R.id.temperature_detail);
        final EditText location = (EditText) dialog.findViewById(R.id.location_detail);
        final Button dialogButton = (Button) dialog.findViewById(R.id.save);
        dialogButton.setEnabled(false);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createAndSaveMoment(mydate, temperature.getText().toString(),
                        location.getText().toString()   );
                dialog.dismiss();
            }
        });

        temperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                setSaveButtonState(temperature, location, dialogButton);
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
                setSaveButtonState(temperature, location, dialogButton);
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        dialog.show();
    }

    private void setSaveButtonState(final EditText temperature, final EditText location, final Button dialogButton) {
        if(!TextUtils.isEmpty(temperature.getText().toString()) && !TextUtils.isEmpty(location.getText().toString())){
            dialogButton.setEnabled(true);
        }else{
            dialogButton.setEnabled(false);
        }
    }

    private void createAndSaveMoment(final String phaseInput, final String temperatureInput, final String locationInput) {
        mTemperatureMoment.createMoment(phaseInput, temperatureInput, locationInput);
        mTemperatureMoment.saveRequest();
    }



    private void init() {
        ((DataSyncApplicationClass) getActivity().getApplication()).getAppComponent().injectFragment(this);
        eventing.register(this);
        mTemperatureMoment = new TemperatureTracker(getContext(), MomentType.TEMPERATURE);
        mTemperatureMoment.fetchData();
        SynchronisationMonitor monitor = new SynchronisationMonitor(mDataPullSynchronise,mDataPushSynchronise);
        monitor.start(eventing);
        eventing.post(new ReadDataFromBackendRequest(null));
    }

    public void onEventMainThread(LoadMomentsResponse event) {
        mData = (ArrayList<? extends Moment>) event.getList();
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(ReadDataFromBackendResponse event) {
        Toast.makeText(getContext(),"got update",Toast.LENGTH_SHORT).show();
        mTemperatureMoment.fetchData();
    }

    public void onEventBackgroundThread(ListSaveResponse event) {
    //    boolean savedAllData = event.isSavedAllData();
        if(event.isSavedAllData()){
            mTemperatureMoment.fetchData();
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add:
                addMoment();
                break;
        }
    }
}
