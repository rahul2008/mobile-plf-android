package com.philips.platform.datasevices.temperature;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.AppFrameworkBaseFragment;
import com.philips.platform.appframework.R;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasevices.database.DatabaseHelper;
import com.philips.platform.datasevices.database.ORMDeletingInterfaceImpl;
import com.philips.platform.datasevices.database.ORMSavingInterfaceImpl;
import com.philips.platform.datasevices.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.datasevices.database.OrmCreator;
import com.philips.platform.datasevices.database.OrmDeleting;
import com.philips.platform.datasevices.database.OrmFetchingInterfaceImpl;
import com.philips.platform.datasevices.database.OrmSaving;
import com.philips.platform.datasevices.database.OrmUpdating;
import com.philips.platform.datasevices.database.table.BaseAppDateTime;
import com.philips.platform.datasevices.database.table.OrmMeasurement;
import com.philips.platform.datasevices.database.table.OrmMeasurementDetail;
import com.philips.platform.datasevices.database.table.OrmMoment;
import com.philips.platform.datasevices.database.table.OrmMomentDetail;
import com.philips.platform.datasevices.database.table.OrmSynchronisationData;
import com.philips.platform.datasevices.listener.DBChangeListener;
import com.philips.platform.datasevices.listener.EventHelper;
import com.philips.platform.datasevices.reciever.BaseAppBroadcastReceiver;
import com.philips.platform.datasevices.registration.UserRegistrationFacadeImpl;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureTimeLineFragment extends AppFrameworkBaseFragment implements View.OnClickListener, DBChangeListener, SwipeRefreshLayout.OnRefreshListener, TemperatureView {
    public static final String TAG = TemperatureTimeLineFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<? extends Moment> mData = new ArrayList();
    private TemperatureTimeLineFragmentcAdapter mAdapter;
    AlarmManager alarmManager;
    DataServicesManager mDataServicesManager;
    ImageButton mAddButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TemperaturePresenter mTemperaturePresenter;

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.ds_screen_title);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
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
        mAdapter = new TemperatureTimeLineFragmentcAdapter(getContext(), mData, mTemperaturePresenter);
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


    private void init() {
        Stetho.initializeWithDefaults(getActivity().getApplicationContext());
        OrmCreator creator = new OrmCreator(new UuidGenerator());
        mDataServicesManager = DataServicesManager.getInstance();
        injectDBInterfacesToCore();
        mDataServicesManager.initialize(getContext(), creator, new UserRegistrationFacadeImpl(getContext(), new User(getContext())));
        mDataServicesManager.initializeSyncMonitors(null, null);

        alarmManager = (AlarmManager) getContext().getApplicationContext().getSystemService(ALARM_SERVICE);
        EventHelper.getInstance().registerEventNotification(EventHelper.MOMENT, this);
        mTemperaturePresenter = new TemperaturePresenter(this, getContext(), MomentType.TEMPERATURE);
        mTemperaturePresenter.fetchData();
        setUpBackendSynchronizationLoop();
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
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, uGrowDateTime);
            ORMDeletingInterfaceImpl ORMDeletingInterfaceImpl = new ORMDeletingInterfaceImpl(deleting, saving);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao);

            mDataServicesManager.initializeDBMonitors(ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
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
        EventHelper.getInstance().unregisterEventNotification(EventHelper.MOMENT, this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add:
                mTemperaturePresenter.addOrUpdateMoment(TemperaturePresenter.ADD, null);
                break;
        }
    }

    @Override
    public void onSuccess(final ArrayList<? extends Object> data) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "http : UI updated");
                mData = (ArrayList<? extends Moment>) data;
                mAdapter.setData(mData);
                mAdapter.notifyDataSetChanged();
                if (mSwipeRefreshLayout.isRefreshing()) {
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
                dismissRefreshLayout();
            }
        });
    }

    @Override
    public void onRefresh() {
        showRefreshLayout();
        mTemperaturePresenter.startSync();
    }

    private void showRefreshLayout() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    private void dismissRefreshLayout() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void finishActivityAffinity() {
        getActivity().finishAffinity();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (AppFrameworkBaseActivity) getActivity();
    }

    @Override
    public int getContainerId() {
        return R.id.frame_container;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }
}
