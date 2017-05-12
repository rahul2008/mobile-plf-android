package cdp.philips.com.mydemoapp.temperature;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.DataSyncApplication;
import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.characteristics.CharacteristicsDialogFragment;
import cdp.philips.com.mydemoapp.consents.ConsentDialogFragment;
import cdp.philips.com.mydemoapp.database.datatypes.MomentType;
import cdp.philips.com.mydemoapp.insights.InsightFragment;
import cdp.philips.com.mydemoapp.registration.UserRegistrationInterfaceImpl;
import cdp.philips.com.mydemoapp.settings.SettingsFragment;
import cdp.philips.com.mydemoapp.utility.Utility;

import static android.content.Context.ALARM_SERVICE;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TemperatureTimeLineFragment extends Fragment implements View.OnClickListener, DBFetchRequestListner<Moment>,DBRequestListener<Moment>, DBChangeListener, SynchronisationCompleteListener{
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

    TextView mTvConsents, mTvCharacteristics , mTvSettings ,mTvLogout ,mTvInsights;



    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        Log.d("pabitra","onStart");
        mDataServicesManager.registerDBChangeListener(this);
        mDataServicesManager.registerSynchronisationCompleteListener(this);

        if (mUser != null && !mUser.isUserSignIn()) {
            Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
            mAddButton.setVisibility(View.INVISIBLE);
            mTvConsents.setVisibility(View.INVISIBLE);
            mTvInsights.setVisibility(View.INVISIBLE);
            mTvSettings.setVisibility(View.INVISIBLE);
            mTvCharacteristics.setVisibility(View.INVISIBLE);
            return;
        }

        deleteUserDataIfNewUserLoggedIn();

        mTemperaturePresenter.fetchData(this);

        //Reseting the sync Flags
        /*mDataServicesManager.setPullComplete(true);
        mDataServicesManager.setPushComplete(true);*/

        //setUpBackendSynchronizationLoop();

        if (!mUtility.isOnline(getContext())) {
            showToastOnUiThread("Please check your connection");
            return;
        }

        if (!mSharedPreferences.getBoolean("isSynced", false)) {
            showProgressDialog();
        }
    }

    private void deleteUserDataIfNewUserLoggedIn() {
        if (getLastStoredHsdpId() == null) {
            storeLastHsdpId();
            return;
        }

        if (!isSameHsdpId()) {
            userRegistrationInterface.clearUserData(this);
        }
        storeLastHsdpId();
    }

   /* private boolean isSameEmail() {
        if (getLastStoredEmail().equalsIgnoreCase(mUser.getEmail()))
            return true;
        return false;
    }*/

    private boolean isSameHsdpId() {
        if (getLastStoredHsdpId().equalsIgnoreCase(mUser.getHsdpUUID()))
            return true;
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
        mDataServicesManager.unRegisterSynchronisationCosmpleteListener();
        //cancelPendingIntent();
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
        mTvLogout= (TextView) view.findViewById(R.id.tv_logout);
        mTvSettings = (TextView) view.findViewById(R.id.tv_settings);
        mTvInsights = (TextView)view.findViewById(R.id.tv_insights);

        mTvConsents.setOnClickListener(this);
        mTvCharacteristics.setOnClickListener(this);
        mTvSettings.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
        mTvInsights.setOnClickListener(this);
        return view;
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
                replaceFragment(dFragment,"consents");

                break;
            case R.id.tv_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                replaceFragment(settingsFragment,"settings");

                break;

            case R.id.tv_set_characteristics:

                CharacteristicsDialogFragment characteristicsDialogFragment = new CharacteristicsDialogFragment();
                replaceFragment(characteristicsDialogFragment,"Character");
                break;

            case R.id.tv_logout:

                boolean isLogout= ((DataSyncApplication) getContext().getApplicationContext()).getUserRegImple().logout();
                if(isLogout)getActivity().finish();

                break;
            case R.id.tv_insights:
                InsightFragment insightFragment = new InsightFragment();

                replaceFragment(insightFragment,"insights");
        }
    }

    @Override
    public void onSuccess(final List<? extends Moment> data) {
        DSLog.i(DSLog.LOG, "on Success Temperature");
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
                if (mSharedPreferences.getBoolean("isSynced", false)) {
                    dismissProgressDialog();
                }
               // dismissProgressDialog();
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

   /* String getLastStoredEmail() {
        AppInfraInterface gAppInfra = ((DataSyncApplication) getContext().getApplicationContext()).gAppInfra;
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        String decryptedData = ssInterface.fetchValueForKey("last_email", ssError);
        return decryptedData;
    }*/

    String getLastStoredHsdpId() {
        AppInfraInterface gAppInfra = ((DataSyncApplication) getContext().getApplicationContext()).gAppInfra;
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        String decryptedData = ssInterface.fetchValueForKey("hsdp_id", ssError);
        return decryptedData;
    }

    /*void storeLastEmail() {
        AppInfraInterface gAppInfra = ((DataSyncApplication) getContext().getApplicationContext()).gAppInfra;
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        ssInterface.storeValueForKey("last_email", mUser.getEmail(), ssError);
    }*/

    void storeLastHsdpId() {
        AppInfraInterface gAppInfra = ((DataSyncApplication) getContext().getApplicationContext()).gAppInfra;
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        ssInterface.storeValueForKey("hsdp_id", mUser.getHsdpUUID(), ssError);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        DSLog.i(DSLog.LOG, "In Temperature TimeLine Fragment DB OnSuccess");
        if(type!=SyncType.MOMENT)return;

        DSLog.i(DSLog.LOG, "In Temperature TimeLine Fragment DB OnSuccess Moment request");
        mTemperaturePresenter.fetchData(TemperatureTimeLineFragment.this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {
        showToastOnUiThread("Exception :" + e.getMessage());

        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSharedPreferences.getBoolean("isSynced", false)) {
                    dismissProgressDialog();
                }

            }
        });
    }

    @Override
    public void onSyncComplete() {
        DSLog.i(TAG, "Sync completed");
        dismissProgressDialog();
    }

    @Override
    public void onSyncFailed(final Exception exception) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSharedPreferences.getBoolean("isSynced", false)) {
                    dismissProgressDialog();
                }
                //dismissProgressDialog();
               // Toast.makeText(getActivity(), "Exception :" + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showToastOnUiThread(final String msg){

        if(getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFetchSuccess(final List<? extends Moment> data) {
        DSLog.i(DSLog.LOG,"On Sucess ArrayList TemperatureTimeLineFragment");
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DSLog.i(DSLog.LOG, "http TEmperature TimeLine : UI updated");
                mData = (ArrayList<? extends Moment>) data;
                mAdapter.setData(mData);
                mAdapter.notifyDataSetChanged();

                if (mDataServicesManager.getSyncTypes()!=null && mDataServicesManager.getSyncTypes().size()<=0) {
                    dismissProgressDialog();
                    Toast.makeText(getContext(),"No Sync Types Configured",Toast.LENGTH_LONG).show();
                    return;
                }

                if (mSharedPreferences.getBoolean("isSynced", false)) {
                    dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void onFetchFailure(Exception exception) {
        onFailureRefresh(exception);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      //  menu.clear();
        //inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /*case R.id.menu_consent:
                Toast.makeText(getApplicationContext(), "speaking....", Toast.LENGTH_LONG).show();
                return false;*/

            default:


                break;
        }

        return false;
    }


    private void replaceFragment(Fragment fragment,String tag){

        int containerId = R.id.frame_container_user_reg;
        try {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
