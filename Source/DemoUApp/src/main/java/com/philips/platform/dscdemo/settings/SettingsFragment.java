/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends DSBaseFragment
        implements DBFetchRequestListner<Settings>, DBRequestListener<Settings>, DBChangeListener, View.OnClickListener {

    private Context mContext;
    private SettingsPresenter mSettingsPresenter;
    private DataServicesManager mDataServicesManager;
    private Spinner mSpinner_Unit, mSpinner_Local;
    private Settings settings;
    private ProgressDialog mProgressDialog;

    @Override
    public int getActionbarTitleResId() {
        return R.string.settings_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.settings_title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_settings, container, false);

        mDataServicesManager = DataServicesManager.getInstance();
        mProgressDialog = new ProgressDialog(mContext);

        Button mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        mSettingsPresenter = new SettingsPresenter(this);
        mSpinner_Unit = (Spinner) rootView.findViewById(R.id.spinner_metrics);
        mSpinner_Local = (Spinner) rootView.findViewById(R.id.spinner_locale);
        ArrayAdapter<CharSequence> adapterMetrics = ArrayAdapter.createFromResource(getActivity(),
                R.array.metrics, android.R.layout.simple_spinner_item);
        adapterMetrics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Unit.setAdapter(adapterMetrics);

        ArrayAdapter<CharSequence> adapterLocale = ArrayAdapter.createFromResource(getActivity(),
                R.array.locals, android.R.layout.simple_spinner_item);
        adapterLocale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Local.setAdapter(adapterLocale);

        fetchSettings();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(final List<? extends Settings> data) {
        refreshUi(data);
    }

    private void refreshUi(final List<? extends Settings> data) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data != null) {
                        settings = data.get(0);
                        if (settings == null) {
                            mDataServicesManager.saveUserSettings(mDataServicesManager.createUserSettings("en_US", "metric"), SettingsFragment.this);
                        } else {
                            updateUi(settings.getUnit(), settings.getLocale());
                        }
                    } else {
                        mDataServicesManager.saveUserSettings(mDataServicesManager.createUserSettings("en_US", "metric"), SettingsFragment.this);
                    }
                    dismissProgressDialog();
                }
            });
        }
    }

    private void updateUi(String unit, String locale) {
        mSpinner_Unit.setSelection(Arrays.asList(getResources().getStringArray(R.array.metrics)).indexOf(unit));
        mSpinner_Local.setSelection(Arrays.asList(getResources().getStringArray(R.array.locals)).indexOf(locale));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    private void refreshOnFailure(final Exception exception) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOK) {
            if (settings == null) {
                settings = mDataServicesManager.createUserSettings(mSpinner_Unit.getSelectedItem().toString(), mSpinner_Local.getSelectedItem().toString());
            } else {
                settings.setUnit(mSpinner_Unit.getSelectedItem().toString());
                settings.setLocale(mSpinner_Local.getSelectedItem().toString());
            }
            mSettingsPresenter.updateSettings(settings);
            getFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else if (i == R.id.btnCancel) {
            getFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataServicesManager.registerDBChangeListener(this);
    }

    public void fetchSettings() {
        showProgressDialog();
        DataServicesManager.getInstance().fetchUserSettings(this);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type != SyncType.SETTINGS) return;
        DataServicesManager.getInstance().fetchUserSettings(this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Exception :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFetchSuccess(final List<? extends Settings> data) {
        refreshUi(data);
    }

    @Override
    public void onFetchFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.setMessage(getString(R.string.fetching_settings));
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.dismiss();
        }
    }
}
