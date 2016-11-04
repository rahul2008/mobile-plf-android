package cdp.philips.com.mydemoapp.temperature;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.facebook.stetho.Stetho;
import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;

import java.sql.SQLException;
import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.DatabaseHelper;
import cdp.philips.com.mydemoapp.database.ORMDeletingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.ORMSavingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.ORMUpdatingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmCreator;
import cdp.philips.com.mydemoapp.database.OrmDeleting;
import cdp.philips.com.mydemoapp.database.OrmFetchingInterfaceImpl;
import cdp.philips.com.mydemoapp.database.OrmSaving;
import cdp.philips.com.mydemoapp.database.OrmUpdating;
import cdp.philips.com.mydemoapp.database.table.BaseAppDateTime;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.listener.DBChangeListener;
import cdp.philips.com.mydemoapp.listener.EventHelper;
import cdp.philips.com.mydemoapp.reciever.BaseAppBroadcastReceiver;
import cdp.philips.com.mydemoapp.registration.UserRegistrationFacadeImpl;

import static android.content.Context.ALARM_SERVICE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureTimeLineFragment extends Fragment implements View.OnClickListener, DBChangeListener{
    public static final String TAG = TemperatureTimeLineFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<? extends Moment> mData = new ArrayList();
    private TemperatureTimeLineFragmentcAdapter mAdapter ;
    AlarmManager alarmManager;
    DataServicesManager mDataServicesManager;
    ImageButton mAddButton;
    TemperaturePresenter mTemperaturePresenter;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpBackendSynchronizationLoop();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelPendingIntent();
        mDataServicesManager.stopCore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        mAdapter = new TemperatureTimeLineFragmentcAdapter(getContext(),mData, mTemperaturePresenter);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAddButton = (ImageButton) view.findViewById(R.id.add);
        mRecyclerView.setAdapter(mAdapter);
        mAddButton.setOnClickListener(this);
        return view;
    }

    private void init() {
        Stetho.initializeWithDefaults(getActivity().getApplicationContext());
        OrmCreator creator = new OrmCreator(new UuidGenerator());
        mDataServicesManager = DataServicesManager.getInstance();
        injectDBInterfacesToCore();
        mDataServicesManager.initialize(getContext(), creator, new UserRegistrationFacadeImpl(getContext(), new User(getContext())));
        mDataServicesManager.initializeSyncMonitors(null,null);

        alarmManager = (AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE);
        EventHelper.getInstance().registerEventNotification(EventHelper.MOMENT, this);
        mTemperaturePresenter = new TemperaturePresenter(getContext(), MomentType.TEMPERATURE);
        mTemperaturePresenter.fetchData();
    }

    void injectDBInterfacesToCore() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(getContext(), new UuidGenerator());
        try {
            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = databaseHelper.getSynchronisationDataDao();


            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao);
            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao);
            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving,updating,fetching,deleting,uGrowDateTime);
            ORMDeletingInterfaceImpl ORMDeletingInterfaceImpl = new ORMDeletingInterfaceImpl(deleting,saving);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving,updating,fetching,deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao,synchronisationDataDao);

            mDataServicesManager.initializeDBMonitors(ORMDeletingInterfaceImpl,dbInterfaceOrmFetchingInterface,ORMSavingInterfaceImpl,dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            throw new IllegalStateException("Can not instantiate database");
        }
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
                mTemperaturePresenter.addOrUpdateMoment(TemperaturePresenter.ADD,null);
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
            }
        });
    }
}
