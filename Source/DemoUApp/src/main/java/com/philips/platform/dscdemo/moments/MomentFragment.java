/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.characteristics.CharacteristicsFragment;
import com.philips.platform.dscdemo.consents.ConsentFragment;
import com.philips.platform.dscdemo.insights.InsightFragment;
import com.philips.platform.dscdemo.settings.SettingsFragment;
import com.philips.platform.dscdemo.utility.UserRegistrationHandler;
import com.philips.platform.dscdemo.utility.Utility;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MomentFragment extends DSBaseFragment
        implements View.OnClickListener, DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener {

    private Context mContext;

    private DataServicesManager mDataServicesManager;
    private UserRegistrationHandler userRegistrationInterface;
    private User mUser;

    private TextView mTvAddMomentType;
    private TextView mTvLatestMoment;
    private TextView mTvConsents;
    private TextView mTvCharacteristics;
    private TextView mTvSettings;
    private TextView mTvInsights;
    private TextView mTvMomentByDateRange;
    private TextView mTvSyncByDateRange;
    private TextView mTvMomentsCount;
    private ImageButton mAddButton;

    private MomentAdapter mMomentAdapter;
    private MomentPresenter mMomentPresenter;
    private ProgressDialog mProgressDialog;

    private ArrayList<? extends Moment> mMomentList = new ArrayList();
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
        mMomentPresenter = new MomentPresenter(mContext, this);
        mUtility = new Utility();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        mMomentAdapter = new MomentAdapter(getContext(), mMomentList, mMomentPresenter, true);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setCancelable(false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mMomentAdapter);

        mAddButton = (ImageButton) view.findViewById(R.id.add);
        ImageButton mDeleteExpiredMomentsButton = (ImageButton) view.findViewById(R.id.delete_moments);
        mAddButton.setOnClickListener(this);
        mDeleteExpiredMomentsButton.setOnClickListener(this);

        mTvMomentsCount = (TextView) view.findViewById(R.id.tv_moments_count);
        mTvAddMomentType = (TextView) view.findViewById(R.id.tv_add_moment_with_type);
        mTvLatestMoment = (TextView) view.findViewById(R.id.tv_last_moment);
        mTvMomentByDateRange = (TextView) view.findViewById(R.id.tv_moment_by_date_range);
        mTvConsents = (TextView) view.findViewById(R.id.tv_set_consents);
        mTvCharacteristics = (TextView) view.findViewById(R.id.tv_set_characteristics);
        mTvSettings = (TextView) view.findViewById(R.id.tv_settings);
        mTvSettings = (TextView) view.findViewById(R.id.tv_settings);
        mTvInsights = (TextView) view.findViewById(R.id.tv_insights);
        mTvSyncByDateRange = (TextView) view.findViewById(R.id.tv_sync_by_date_range);
        TextView mTvLogout = (TextView) view.findViewById(R.id.tv_logout);

        mTvAddMomentType.setOnClickListener(this);
        mTvLatestMoment.setOnClickListener(this);
        mTvMomentByDateRange.setOnClickListener(this);
        mTvSyncByDateRange.setOnClickListener(this);
        mTvConsents.setOnClickListener(this);
        mTvCharacteristics.setOnClickListener(this);
        mTvSettings.setOnClickListener(this);
        mTvInsights.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
        return view;
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

        if (mUser != null && !mUser.isUserSignIn()) {
            Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
            mTvAddMomentType.setVisibility(View.VISIBLE);
            mTvLatestMoment.setVisibility(View.INVISIBLE);
            mTvMomentByDateRange.setVisibility(View.INVISIBLE);
            mTvSyncByDateRange.setVisibility(View.INVISIBLE);
            mAddButton.setVisibility(View.INVISIBLE);
            mTvConsents.setVisibility(View.INVISIBLE);
            mTvInsights.setVisibility(View.INVISIBLE);
            mTvSettings.setVisibility(View.INVISIBLE);
            mTvCharacteristics.setVisibility(View.INVISIBLE);
            return;
        }

        deleteUserDataIfNewUserLoggedIn();

        if (!mUtility.isOnline(getContext())) {
            showToastOnUiThread("Please check your connection");
            return;
        }

        showProgressDialog();
        mMomentPresenter.fetchData(this);
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
        return getLastStoredHsdpId().equalsIgnoreCase(mUser.getHsdpUUID());
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(final View v) {
        int i = v.getId();
        if (i == R.id.add) {
            mMomentPresenter.addOrUpdateMoment(MomentPresenter.ADD, null, false);
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
        } else if (i == R.id.tv_moment_by_date_range) {
            MomentByDateRangeFragment momentByDateRangeFragment = new MomentByDateRangeFragment();
            showFragment(momentByDateRangeFragment);
        } else if (i == R.id.tv_sync_by_date_range) {
            SyncByDateRangeFragment syncByDateRangeFragment = new SyncByDateRangeFragment();
            showFragment(syncByDateRangeFragment);
        } else if (i == R.id.tv_add_moment_with_type) {
            mMomentPresenter.addOrUpdateMoment(MomentPresenter.ADD, null, true);
        }
    }

    String getLastStoredHsdpId() {
        AppInfraInterface gAppInfra = DemoAppManager.getInstance().getAppInfra();
        SecureStorageInterface ssInterface = gAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        return ssInterface.fetchValueForKey("hsdp_id", ssError);
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
        mMomentPresenter.fetchData(MomentFragment.this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {
        showToastOnUiThread("Exception :" + e.getMessage());

        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
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
                mTvMomentsCount.setText("Moments Count : " + String.valueOf(mMomentList.size()));

                mMomentAdapter.setData(mMomentList);
                mMomentAdapter.notifyDataSetChanged();

                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onFetchFailure(Exception exception) {
        onFailureRefresh(exception);
    }

    @Override
    public void onSuccess(final List<? extends Moment> data) {
        mMomentPresenter.fetchData(this);
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
                dismissProgressDialog();
            }
        });
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


    private void showToastOnUiThread(final String msg) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reloadData() {
        mDataServicesManager.registerDBChangeListener(this);
        mMomentPresenter.fetchData(this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MomentFragment.this.getView().invalidate();
            }
        });
    }

    private class DeleteExpiredMomentsListener implements DBRequestListener<Integer> {
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
}
