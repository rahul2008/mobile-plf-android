package com.philips.platform.baseapp.screens.dataservices.temperature;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.dataservices.consents.ConsentDialogFragment;
import com.philips.platform.baseapp.screens.dataservices.database.DatabaseHelper;
import com.philips.platform.baseapp.screens.dataservices.database.ORMSavingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.OrmCreator;
import com.philips.platform.baseapp.screens.dataservices.database.OrmDeleting;
import com.philips.platform.baseapp.screens.dataservices.database.OrmDeletingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.OrmFetchingInterfaceImpl;
import com.philips.platform.baseapp.screens.dataservices.database.OrmSaving;
import com.philips.platform.baseapp.screens.dataservices.database.OrmUpdating;
import com.philips.platform.baseapp.screens.dataservices.database.table.BaseAppDateTime;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsent;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurement;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.baseapp.screens.dataservices.listener.DBChangeListener;
import com.philips.platform.baseapp.screens.dataservices.listener.EventHelper;
import com.philips.platform.baseapp.screens.dataservices.reciever.BaseAppBroadcastReceiver;
import com.philips.platform.baseapp.screens.dataservices.registration.UserRegistrationFacadeImpl;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TemperatureTimeLineFragment extends AppFrameworkBaseFragment implements View.OnClickListener, DBChangeListener {
    public static final String TAG = TemperatureTimeLineFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<? extends Moment> mData = new ArrayList();
    AlarmManager alarmManager;
    DataServicesManager mDataServicesManager;
    ImageButton mAddButton;
    TemperaturePresenter mTemperaturePresenter;
    TemperatureMomentHelper mTemperatureMomentHelper;
    private TemperatureTimeLineFragmentcAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private TextView mTvSetCosents;
    private Context mContext;


    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.data_sync_title);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpBackendSynchronizationLoop();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        mAdapter = new TemperatureTimeLineFragmentcAdapter(getContext(), mData, mTemperaturePresenter);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAddButton = (ImageButton) view.findViewById(R.id.add);
        mRecyclerView.setAdapter(mAdapter);
        mAddButton.setOnClickListener(this);
        mTvSetCosents = (TextView) view.findViewById(R.id.tv_set_consents);
        mTvSetCosents.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mTemperaturePresenter.fetchData();
        return view;
    }

    private void init() {
        //Stetho.initializeWithDefaults(getActivity().getApplicationContext());
        mTemperatureMomentHelper = new TemperatureMomentHelper();
        OrmCreator creator = new OrmCreator(new UuidGenerator());
        mDataServicesManager = DataServicesManager.getInstance();
        injectDBInterfacesToCore();
        mDataServicesManager.initialize(mContext, creator, new UserRegistrationFacadeImpl(mContext, new User(mContext)));
        mDataServicesManager.initializeSyncMonitors(null, null);

        alarmManager = (AlarmManager) mContext.getApplicationContext().getSystemService(ALARM_SERVICE);
        EventHelper.getInstance().registerEventNotification(EventHelper.MOMENT, this);
        mTemperaturePresenter = new TemperaturePresenter(mContext, MomentType.TEMPERATURE);

    }

    void injectDBInterfacesToCore() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(mContext, new UuidGenerator());
        try {
            Dao<OrmMoment, Integer> momentDao = databaseHelper.getMomentDao();
            Dao<OrmMomentDetail, Integer> momentDetailDao = databaseHelper.getMomentDetailDao();
            Dao<OrmMeasurement, Integer> measurementDao = databaseHelper.getMeasurementDao();
            Dao<OrmMeasurementDetail, Integer> measurementDetailDao = databaseHelper.getMeasurementDetailDao();
            Dao<OrmSynchronisationData, Integer> synchronisationDataDao = databaseHelper.getSynchronisationDataDao();

            Dao<OrmConsent, Integer> consentDao = databaseHelper.getConsentDao();
            Dao<OrmConsentDetail, Integer> consentDetailsDao = databaseHelper.getConsentDetailsDao();
            Dao<OrmConsentDetailType, Integer> consentDetailTypeDao = databaseHelper.getConsentDetailsTypeDao();


            OrmSaving saving = new OrmSaving(momentDao, momentDetailDao, measurementDao, measurementDetailDao,
                    synchronisationDataDao, consentDao, consentDetailsDao, consentDetailTypeDao);
            OrmUpdating updating = new OrmUpdating(momentDao, momentDetailDao, measurementDao, measurementDetailDao, consentDao, consentDetailsDao, consentDetailTypeDao);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDao, consentDetailsDao, consentDetailTypeDao);
            OrmDeleting deleting = new OrmDeleting(momentDao, momentDetailDao, measurementDao,
                    measurementDetailDao, synchronisationDataDao, consentDao, consentDetailsDao, consentDetailTypeDao);
            BaseAppDateTime uGrowDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, uGrowDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(momentDao, synchronisationDataDao, consentDao, consentDetailsDao, consentDetailTypeDao);

            mDataServicesManager.initializeDBMonitors(ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            mTemperatureMomentHelper.notifyAllFailure(exception);
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
        Intent intent = new Intent(mContext, BaseAppBroadcastReceiver.class);
        intent.setAction(BaseAppBroadcastReceiver.ACTION_USER_DATA_FETCH);
        return PendingIntent.getBroadcast(mContext, 0, intent, 0);
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
        cancelPendingIntent();
        mDataServicesManager.stopCore();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add:
                mTemperaturePresenter.addOrUpdateMoment(TemperaturePresenter.ADD, null);
                break;
            case R.id.tv_set_consents:
                ConsentDialogFragment dFragment = new ConsentDialogFragment();
                dFragment.show(getFragmentManager(), "Dialog");
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
                    if (mContext != null)
                        Toast.makeText(mContext, "UI update Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "http : UI update Failed");
                    if (mContext != null)
                        Toast.makeText(mContext, "UI update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
