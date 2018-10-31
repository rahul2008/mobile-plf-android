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
import com.philips.cdp.registration.UserLoginState;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.characteristics.CharacteristicsFragment;
import com.philips.platform.dscdemo.insights.InsightFragment;
import com.philips.platform.dscdemo.settings.SettingsFragment;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MomentFragment extends DSBaseFragment
        implements View.OnClickListener, DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener {

    private static final String CLEAR_CACHE_ERROR_DIALOG_TAG = "CLEAR_CACHE_ERROR_DIALOG_TAG";
    private Context context;

    protected DataServicesManager dataServicesManager;
    private User user;

    private TextView tvAddMomentType;
    private TextView tvLatestMoment;
    private TextView tvCharacteristics;
    private TextView tvSettings;
    private TextView tvInsights;
    private TextView tvMomentByDateRange;
    private TextView tvSyncByDateRange;
    private TextView tvGdprFeatures;
    private TextView tvMomentsCount;
    private ImageButton addButton;

    private MomentAdapter momentAdapter;
    private MomentPresenter momentPresenter;
    private ProgressDialog progressDialog;
    private AlertDialogFragment alertDialog = null;

    private ArrayList<? extends Moment> momentList = new ArrayList();

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
        dataServicesManager = DataServicesManager.getInstance();
        user = new User(context);
        momentPresenter = new MomentPresenter(context, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_data_sync_fragment, container, false);
        momentAdapter = new MomentAdapter(getContext(), momentList, momentPresenter, true);

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        RecyclerView recyclerView = view.findViewById(R.id.timeline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(momentAdapter);

        addButton = view.findViewById(R.id.add);
        addButton.setOnClickListener(this);
        ImageButton clearCacheButton = view.findViewById(R.id.clear_cache);
        clearCacheButton.setOnClickListener(this);
        ImageButton deleteExpiredMomentsButton = view.findViewById(R.id.delete_moments);
        deleteExpiredMomentsButton.setOnClickListener(this);

        tvMomentsCount = view.findViewById(R.id.tv_moments_count);
        tvAddMomentType = view.findViewById(R.id.tv_add_moment_with_type);
        tvLatestMoment = view.findViewById(R.id.tv_last_moment);
        tvMomentByDateRange = view.findViewById(R.id.tv_moment_by_date_range);
        tvCharacteristics = view.findViewById(R.id.tv_set_characteristics);
        tvSettings = view.findViewById(R.id.tv_settings);
        tvSettings = view.findViewById(R.id.tv_settings);
        tvInsights = view.findViewById(R.id.tv_insights);
        tvSyncByDateRange = view.findViewById(R.id.tv_sync_by_date_range);
        tvGdprFeatures = view.findViewById(R.id.tv_gdpr_features);

        tvAddMomentType.setOnClickListener(this);
        tvLatestMoment.setOnClickListener(this);
        tvMomentByDateRange.setOnClickListener(this);
        tvSyncByDateRange.setOnClickListener(this);
        tvGdprFeatures.setOnClickListener(this);
        tvCharacteristics.setOnClickListener(this);
        tvSettings.setOnClickListener(this);
        tvInsights.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (user != null && user.getUserLoginState() != UserLoginState.USER_LOGGED_IN) {
            Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
            tvAddMomentType.setVisibility(View.VISIBLE);
            tvLatestMoment.setVisibility(View.INVISIBLE);
            tvMomentByDateRange.setVisibility(View.INVISIBLE);
            tvSyncByDateRange.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            tvInsights.setVisibility(View.INVISIBLE);
            tvSettings.setVisibility(View.INVISIBLE);
            tvCharacteristics.setVisibility(View.INVISIBLE);
            tvGdprFeatures.setVisibility(View.INVISIBLE);
            return;
        }
        deleteUserDataIfNewUserLoggedIn();


        if (!isOnline()) {
            showToastOnUiThread("Please check your connection");
        }

        showProgressDialog();
        momentPresenter.fetchData(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        dataServicesManager.registerDBChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != alertDialog) {
            alertDialog.dismiss();
        }
    }

    private void deleteUserDataIfNewUserLoggedIn() {
        if (getLastStoredHsdpId() == null) {
            storeLastHsdpId();
            return;
        }

        if (!isSameHsdpId()) {
            DemoAppManager.getInstance().getUserRegistrationHandler().clearUserData(this);
        }
        storeLastHsdpId();
    }

    private boolean isSameHsdpId() {
        return getLastStoredHsdpId().equalsIgnoreCase(user.getHsdpUUID());
    }

    @Override
    public void onPause() {
        dataServicesManager.unRegisterDBChangeListener();
        dismissProgressDialog();
        super.onPause();
    }

    @Override
    public void onClick(final View v) {
        int i = v.getId();
        if (i == R.id.add) {
            momentPresenter.addOrUpdateMoment(MomentPresenter.ADD, null, false);
        } else if (i == R.id.delete_moments) {
            dataServicesManager.clearExpiredMoments(new DeleteExpiredMomentsListener());
        } else if (i == R.id.clear_cache) {
            dataServicesManager.deleteAll(new DeleteAllDataRequestListener());
        } else if (i == R.id.tv_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            showFragment(settingsFragment);
        } else if (i == R.id.tv_set_characteristics) {
            CharacteristicsFragment characteristicsFragment = new CharacteristicsFragment();
            showFragment(characteristicsFragment);
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
            momentPresenter.addOrUpdateMoment(MomentPresenter.ADD, null, true);
        } else if (i == R.id.tv_gdpr_features) {
            GdprFeatureFragment gdprFeatureFragment = new GdprFeatureFragment();
            showFragment(gdprFeatureFragment);
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
        ssInterface.storeValueForKey("hsdp_id", user.getHsdpUUID(), ssError);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type != SyncType.MOMENT) return;
        momentPresenter.fetchData(MomentFragment.this);
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
        reloadData(data);
    }

    @Override
    public void onFetchFailure(Exception exception) {
        onFailureRefresh(exception);
    }

    @Override
    public void onSuccess(final List<? extends Moment> data) {
        momentPresenter.fetchData(this);
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
                    if (context != null)
                        Toast.makeText(context, "UI update Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (context != null)
                        Toast.makeText(context, "UI update Failed", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && !(((Activity) context).isFinishing())) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing() && !(((Activity) context).isFinishing())) {
            progressDialog.setMessage(getString(R.string.fetching_moments));
            progressDialog.show();
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

    private void reloadData(final List<? extends Moment> data) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                momentList = (ArrayList<? extends Moment>) data;
                tvMomentsCount.setText("Moments Count : " + String.valueOf(momentList.size()));

                momentAdapter.setData(momentList);
                momentAdapter.notifyDataSetChanged();

                dismissProgressDialog();
            }
        });
    }

    private void showDialog(String message){
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_ALERT)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, v -> {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                        alertDialog = null;
                    }
                })
                .setDimLayer(DialogConstants.DIM_SUBTLE)
                .setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show(getFragmentManager(), CLEAR_CACHE_ERROR_DIALOG_TAG);

    }

    private class DeleteAllDataRequestListener implements DBRequestListener {
        @Override
        public void onSuccess(List data) {
            MomentFragment.this.showToastOnUiThread(MomentFragment.this.getActivity().getString(R.string.deleting_all_data_success));
            momentPresenter.fetchData(MomentFragment.this);
        }

        @Override
        public void onFailure(Exception exception) {
            showDialog(MomentFragment.this.getActivity().getString(R.string.error_clearing_cache));
        }
    }

    private class DeleteExpiredMomentsListener implements DBRequestListener<Integer> {
        @Override
        public void onSuccess(List<? extends Integer> data) {
            MomentFragment.this.showToastOnUiThread(MomentFragment.this.getActivity().getString(R.string.deleted_expired_moments_count) + data.get(0));
            momentPresenter.fetchData(MomentFragment.this);
        }

        @Override
        public void onFailure(Exception exception) {
            MomentFragment.this.showToastOnUiThread(MomentFragment.this.getActivity().getString(R.string.error_deleting_expired_moments));
        }
    }

    private boolean isOnline() {
        AppInfraInterface appInfra = DemoAppManager.getInstance().getAppInfra();
        return appInfra.getRestClient().isInternetReachable();
    }
}
