package cdp.philips.com.mydemoapp.temperature;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.DataSyncApplication;
import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.characteristics.CharacteristicsDialogFragment;
import cdp.philips.com.mydemoapp.consents.ConsentDialogFragment;
import cdp.philips.com.mydemoapp.database.datatypes.MomentType;
import cdp.philips.com.mydemoapp.reciever.BaseAppBroadcastReceiver;
import cdp.philips.com.mydemoapp.registration.UserRegistrationInterfaceImpl;
import cdp.philips.com.mydemoapp.settings.SettingsFragment;
import cdp.philips.com.mydemoapp.utility.Utility;

import static android.content.Context.ALARM_SERVICE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TemperatureTimeLineFragment extends Fragment implements View.OnClickListener, DBRequestListener, DBChangeListener, SynchronisationCompleteListener{
    public static final String TAG = TemperatureTimeLineFragment.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<? extends Moment> mData = new ArrayList();
    private TemperatureTimeLineFragmentcAdapter mAdapter;
    AlarmManager alarmManager;
    DataServicesManager mDataServicesManager;
    ImageButton mAddButton;
    TemperaturePresenter mTemperaturePresenter;
    TemperatureMomentHelper mTemperatureMomentHelper;
    private Context mContext;
    SharedPreferences mSharedPreferences;
    ProgressDialog mProgressBar;
    UserRegistrationInterfaceImpl userRegistrationInterface;
    User mUser;
    Utility mUtility;

    TextView mTvConsents, mTvCharacteristics , mTvSettings;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataServicesManager = DataServicesManager.getInstance();
        mUser = new User(mContext);
        userRegistrationInterface = new UserRegistrationInterfaceImpl(mContext, mUser);
        mTemperatureMomentHelper = new TemperatureMomentHelper();
        alarmManager = (AlarmManager) mContext.getApplicationContext().getSystemService(ALARM_SERVICE);
        //EventHelper.getInstance().registerEventNotification(EventHelper.MOMENT, this);
        mTemperaturePresenter = new TemperaturePresenter(mContext, MomentType.TEMPERATURE, this);
        mUtility = new Utility();
        mSharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setCancelable(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDataServicesManager.registerDBChangeListener(this);
        mDataServicesManager.registerSynchronisationCompleteListener(this);

        if (mUser != null && !mUser.isUserSignIn()) {
            Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
            mAddButton.setVisibility(View.INVISIBLE);
            mTvConsents.setVisibility(View.INVISIBLE);
            return;
        }

        deleteUserDataIfNewUserLoggedIn();

        mTemperaturePresenter.fetchData(this);

        //Reseting the sync Flags
        /*mDataServicesManager.setPullComplete(true);
        mDataServicesManager.setPushComplete(true);*/

        setUpBackendSynchronizationLoop();

        if (!mUtility.isOnline(getContext())) {
            Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mSharedPreferences.getBoolean("isSynced", false)) {
            showProgressDialog();
        }
    }

    private void deleteUserDataIfNewUserLoggedIn() {
        if (getLastStoredEmail() == null) {
            storeLastEmail();
            return;
        }

        if (!isSameEmail()) {
            userRegistrationInterface.clearUserData(this);
        }
        storeLastEmail();
    }

    private boolean isSameEmail() {
        if (getLastStoredEmail().equalsIgnoreCase(mUser.getEmail()))
            return true;
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
        mDataServicesManager.unRegisterSynchronisationCosmpleteListener();
        cancelPendingIntent();
        //mDataServicesManager.stopCore();
        dismissProgressDialog();
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
        mTvConsents = (TextView) view.findViewById(R.id.tv_set_consents);
        mTvCharacteristics = (TextView) view.findViewById(R.id.tv_set_characteristics);
        mTvSettings= (TextView) view.findViewById(R.id.tv_settings);

        mTvConsents.setOnClickListener(this);
        mTvCharacteristics.setOnClickListener(this);
        mTvSettings.setOnClickListener(this);
        return view;
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
        // EventHelper.getInstance().unregisterEventNotification(EventHelper.MOMENT, this);
        //mDataServicesManager.releaseDataServicesInstances();
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
            case R.id.tv_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.show(getFragmentManager(), "settings");

                break;

            case R.id.tv_set_characteristics:

                CharacteristicsDialogFragment characteristicsDialogFragment = new CharacteristicsDialogFragment();
                characteristicsDialogFragment.show(getFragmentManager(), "Character");

                break;
        }
    }

    @Override
    public void onSuccess(final ArrayList<? extends Object> data) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DSLog.i(TAG, "http : UI updated");
                mData = (ArrayList<? extends Moment>) data;
                mAdapter.setData(mData);
                mAdapter.notifyDataSetChanged();

                if (mSharedPreferences.getBoolean("isSynced", false)) {
                    dismissProgressDialog();
                }
            }
        });

    }

    @Override
    public void onSuccess(final Object data) {
        mTemperaturePresenter.fetchData(this);
    }

    @Override
    public void onFailure(final Exception exception) {
        onFailureRefresh(exception);
    }

    private void onFailureRefresh(final Exception e) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e != null && e.getMessage() != null) {
                    DSLog.i(TAG, "http : UI update Failed" + e.getMessage());
                    if (mContext != null)
                        Toast.makeText(mContext, "UI update Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    DSLog.i(TAG, "http : UI update Failed");
                    if (mContext != null)
                        Toast.makeText(mContext, "UI update Failed", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressBar != null && !mProgressBar.isShowing()) {
            mProgressBar.setMessage("Loading Please wait!!!");
            mProgressBar.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressBar != null && mProgressBar.isShowing()) {
            mProgressBar.dismiss();
        }
    }

    String getLastStoredEmail() {
        AppInfraInterface gAppInfra = ((DataSyncApplication) getContext().getApplicationContext()).gAppInfra;
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        String decryptedData = ssInterface.fetchValueForKey("last_email", ssError);
        return decryptedData;
    }

    void storeLastEmail() {
        AppInfraInterface gAppInfra = ((DataSyncApplication) getContext().getApplicationContext()).gAppInfra;
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        ssInterface.storeValueForKey("last_email", mUser.getEmail(), ssError);
    }

    @Override
    public void dBChangeSuccess() {
        DSLog.i(DSLog.LOG, "DB OnSuccess");
        mTemperaturePresenter.fetchData(this);

        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mTemperaturePresenter.fetchData(TemperatureTimeLineFragment.this);
            }
        });


    }

    @Override
    public void dBChangeFailed(final Exception e) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity(), "Exception :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSyncComplete() {

    }

    @Override
    public void onSyncFailed(final Exception exception) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "Exception :" + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
