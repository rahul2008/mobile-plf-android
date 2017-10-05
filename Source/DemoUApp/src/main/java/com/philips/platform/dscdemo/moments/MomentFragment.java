/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.characteristics.CharacteristicsFragment;
import com.philips.platform.dscdemo.consents.ConsentFragment;
import com.philips.platform.dscdemo.database.datatypes.MomentType;
import com.philips.platform.dscdemo.insights.InsightFragment;
import com.philips.platform.dscdemo.utility.UserRegistrationHandler;
import com.philips.platform.dscdemo.settings.SettingsFragment;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MomentFragment extends DSBaseFragment
        implements View.OnClickListener, DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener, SynchronisationCompleteListener {

    public static final String TAG = MomentFragment.class.getSimpleName();
    private Context mContext;

    private DataServicesManager mDataServicesManager;
    private UserRegistrationHandler userRegistrationInterface;
    private User mUser;

    private TextView mTvLatestMoment;
    private TextView mTvConsents;
    private TextView mTvCharacteristics;
    private TextView mTvSettings;
    private TextView mTvInsights;
    private TextView mTvLogout;
    private ImageButton mAddButton;
    private ImageButton mDeleteExpiredMomentsButton;

    private MomentAdapter mAdapter;
    private MomentPresenter mTemperaturePresenter;
    private MomentHelper mTemperatureMomentHelper;
    private AlarmManager alarmManager;
    private ProgressDialog mProgressDialog;

    private ArrayList<? extends Moment> mMomentList = new ArrayList();
    private SharedPreferences mSharedPreferences;
    private Utility mUtility;

    @Override
    public int getActionbarTitleResId() {
        return R.string.moment_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.moment_title);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDataServicesManager = DataServicesManager.getInstance();

        mUser = new User(mContext);
        userRegistrationInterface = new UserRegistrationHandler(mContext, mUser);
        mTemperatureMomentHelper = new MomentHelper();
        alarmManager = (AlarmManager) mContext.getApplicationContext().getSystemService(ALARM_SERVICE);
        mTemperaturePresenter = new MomentPresenter(mContext, MomentType.TEMPERATURE, this);
        mUtility = new Utility();
        mSharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
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
            mTvLatestMoment.setVisibility(View.INVISIBLE);
            mAddButton.setVisibility(View.INVISIBLE);
            mTvConsents.setVisibility(View.INVISIBLE);
            mTvInsights.setVisibility(View.INVISIBLE);
            mTvSettings.setVisibility(View.INVISIBLE);
            mTvCharacteristics.setVisibility(View.INVISIBLE);
            return;
        }

        deleteUserDataIfNewUserLoggedIn();

        mTemperaturePresenter.fetchData(this);

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
        dismissProgressDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        mAdapter = new MomentAdapter(getContext(), mMomentList, mTemperaturePresenter);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAddButton = (ImageButton) view.findViewById(R.id.add);
        mDeleteExpiredMomentsButton = (ImageButton) view.findViewById(R.id.delete_moments);
        mAddButton.setOnClickListener(this);
        mDeleteExpiredMomentsButton.setOnClickListener(this);

        mTvLatestMoment = (TextView) view.findViewById(R.id.tv_last_moment);
        mTvConsents = (TextView) view.findViewById(R.id.tv_set_consents);
        mTvCharacteristics = (TextView) view.findViewById(R.id.tv_set_characteristics);
        mTvSettings = (TextView) view.findViewById(R.id.tv_settings);
        mTvLogout = (TextView) view.findViewById(R.id.tv_logout);
        mTvSettings = (TextView) view.findViewById(R.id.tv_settings);
        mTvInsights = (TextView) view.findViewById(R.id.tv_insights);

        mTvLatestMoment.setOnClickListener(this);
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
    }

    @Override
    public void onClick(final View v) {
        int i = v.getId();
        if (i == R.id.add) {
            mTemperaturePresenter.addOrUpdateMoment(MomentPresenter.ADD, null);
        } else if (i == R.id.delete_moments) {
            mDataServicesManager.clearExpiredMoments(new DeleteExpiredMomentsListener());
        } else if (i == R.id.tv_set_consents) {
            ConsentFragment consentsFragment = new ConsentFragment();
            showFragment(consentsFragment);
        } else if (i == R.id.tv_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            showFragment(settingsFragment);
        } else if (i == R.id.tv_set_characteristics) {
            CharacteristicsFragment characteristicsFragment = new CharacteristicsFragment();
            showFragment(characteristicsFragment);
        } else if (i == R.id.tv_logout) {
            logOut();
        } else if (i == R.id.tv_insights) {
            InsightFragment insightFragment = new InsightFragment();
            showFragment(insightFragment);
        } else if (i == R.id.tv_last_moment) {
            LatestMomentFragment latestMomentFragment = new LatestMomentFragment();
            showFragment(latestMomentFragment);
        }
    }

    @Override
    public void onSuccess(final List<? extends Moment> data) {
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
                    if (mContext != null)
                        Toast.makeText(mContext, "UI update Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (mContext != null)
                        Toast.makeText(mContext, "UI update Failed", Toast.LENGTH_SHORT).show();
                }
                if (mSharedPreferences.getBoolean("isSynced", false)) {
                    dismissProgressDialog();
                }
            }
        });
    }

    String getLastStoredHsdpId() {
        AppInfraInterface gAppInfra = DemoAppManager.getInstance().getAppInfra();
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        String decryptedData = ssInterface.fetchValueForKey("hsdp_id", ssError);
        return decryptedData;
    }

    void storeLastHsdpId() {
        AppInfraInterface gAppInfra = DemoAppManager.getInstance().getAppInfra();
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        ssInterface.storeValueForKey("hsdp_id", mUser.getHsdpUUID(), ssError);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type != SyncType.MOMENT) return;
        mTemperaturePresenter.fetchData(MomentFragment.this);
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
            }
        });
    }

    private void showToastOnUiThread(final String msg) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFetchSuccess(final List<? extends Moment> data) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMomentList = (ArrayList<? extends Moment>) data;
                mAdapter.setData(mMomentList);
                mAdapter.notifyDataSetChanged();

                if (mDataServicesManager.getSyncTypes() != null && mDataServicesManager.getSyncTypes().size() <= 0) {
                    dismissProgressDialog();
                    Toast.makeText(getContext(), "No Sync Types Configured", Toast.LENGTH_LONG).show();
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

    private void replaceFragment(Fragment fragment, String tag) {

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

    public void logOut() {
        User user = new User(getContext());
        if (!user.isUserSignIn()) return;

        user.logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Logout Success", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                });
            }

            @Override
            public void onLogoutFailure(int i, String s) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Logout Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void reloadData() {
        mDataServicesManager.registerDBChangeListener(this);
        mTemperaturePresenter.fetchData(this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MomentFragment.this.getView().invalidate();
            }
        });
    }

    public class DeleteExpiredMomentsListener implements DBRequestListener<Integer> {

        @Override
        public void onSuccess(List<? extends Integer> data) {
            MomentFragment.this.showToastOnUiThread(MomentFragment.this.getActivity().getString(R.string.deleted_expired_moments_count) + data.get(0));
            reloadData();
        }

        @Override
        public void onFailure(Exception exception) {
            MomentFragment.this.showToastOnUiThread(MomentFragment.this.getActivity().getString(R.string.error_deleting_expired_moments));
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.setMessage(getString(R.string.fetching_moments));
            mProgressDialog.show();
        }
    }
}
