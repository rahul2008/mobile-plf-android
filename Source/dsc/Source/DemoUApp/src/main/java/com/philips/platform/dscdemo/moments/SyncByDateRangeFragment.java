/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SyncByDateRangeFragment extends DSBaseFragment
        implements DBFetchRequestListner<Moment>,
        DBRequestListener<Moment>, DBChangeListener, SynchronisationCompleteListener, SyncScheduler.UpdateSyncStatus {

    private Context mContext;
    private MomentPresenter mMomentPresenter;
    private Button btnCompleteSync;
    private Button btnStartSyncByDateRange;
    private Button btnDeleteSyncedData;
    private Button btnFetchMomentConsentStatus;
    private Date mStartDate;
    private Date mEndDate;
    private EditText mMomentStartDateEt;
    private EditText mMomentEndDateEt;
    private Calendar myCalendar;
    private TextView tvSyncStatus;
    private Button btnMigrateData;
    private Button btnResetMigrationFlag;
    private TextView flagStateView;
    private TextView momentConsentStatusTextView;
    private View fragmentView;
    private ToggleButton mEnableDisableSync;
    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
    private EditText mLastSyncDateEt;
    private Button btnSetLastSyncTimestamp;
    private DateTime mLastSyncDateTime;

    final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(year, monthOfYear, dayOfMonth);
            mStartDate = myCalendar.getTime();
            updateEditTextView(mMomentStartDateEt, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        }
    };

    final DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(year, monthOfYear, dayOfMonth);
            mEndDate = myCalendar.getTime();
            updateEditTextView(mMomentEndDateEt, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        }
    };

    final DatePickerDialog.OnDateSetListener lastSyncDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(year, monthOfYear, dayOfMonth);
            mLastSyncDateTime = new DateTime(myCalendar.getTime());
            updateEditTextView(mLastSyncDateEt, "yyyy-MM-dd");
            btnSetLastSyncTimestamp.setEnabled(true);
        }
    };

    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnCompleteSync) {
                updateTextView(getString(R.string.sync_inProgress));
                DataServicesManager.getInstance().synchronize();
            } else if (view == mMomentStartDateEt) {
                new DatePickerDialog(mContext, startDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else if (view == mMomentEndDateEt) {
                new DatePickerDialog(mContext, endDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else if (view == btnStartSyncByDateRange) {
                fetchSyncByDateRange();
            } else if (view == btnDeleteSyncedData) {
                deleteSyncedData();
            } else if (view == btnMigrateData) {
                migrateGDPRData();
            } else if (view == btnResetMigrationFlag) {
                DataServicesManager.getInstance().resetGDPRMigrationFlag();
                updateMigrationFlagView();
            } else if (view == btnFetchMomentConsentStatus) {
                updateMomentConsentStatusView();
            } else if (view == btnSetLastSyncTimestamp) {
                resetLastSyncTime();
            } else if (view == mLastSyncDateEt) {
                DatePickerDialog lastSyncDatePickerDialog = new DatePickerDialog(mContext, lastSyncDateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                lastSyncDatePickerDialog.getDatePicker().setMaxDate(DateTime.now().getMillis());
                lastSyncDatePickerDialog.show();
            }
        }
    };

    private void resetLastSyncTime() {
        mMomentPresenter.resetLastSyncTimestampTo(mLastSyncDateTime);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMomentPresenter = new MomentPresenter(mContext, this);
        myCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.momentsync_by_daterange, container, false);
        btnCompleteSync = fragmentView.findViewById(R.id.btn_completeSync);
        mMomentStartDateEt = fragmentView.findViewById(R.id.et_moment_startDate);
        mMomentEndDateEt = fragmentView.findViewById(R.id.et_moment_endDate);
        btnStartSyncByDateRange = fragmentView.findViewById(R.id.btn_startSyncBy_dateRange);
        tvSyncStatus = fragmentView.findViewById(R.id.tvSyncStatus);
        btnDeleteSyncedData = fragmentView.findViewById(R.id.mya_delete_synced_data);
        btnMigrateData = fragmentView.findViewById(R.id.migrate_data);
        btnResetMigrationFlag = fragmentView.findViewById(R.id.reset_migration_flag);
        flagStateView = fragmentView.findViewById(R.id.migration_flag_status);
        btnFetchMomentConsentStatus = fragmentView.findViewById(R.id.fetch_moment_consent_status);
        momentConsentStatusTextView = fragmentView.findViewById(R.id.moment_consent_status);
        mLastSyncDateEt = fragmentView.findViewById(R.id.et_last_sync_Date);
        btnSetLastSyncTimestamp = fragmentView.findViewById(R.id.btn_setSyncTimestamp);
        updateMigrationFlagView();
        updateMomentConsentStatusView();
        mEnableDisableSync = fragmentView.findViewById(R.id.toggleButton);
        loadPreferences();

        if (!SyncScheduler.getInstance().isSyncEnabled()) {
            mEnableDisableSync.setChecked(false);
        }

        mEnableDisableSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SyncScheduler.getInstance().setSyncEnable(true);
                    SyncScheduler.getInstance().scheduleSync();
                    savePreferences();
                } else {
                    SyncScheduler.getInstance().setSyncEnable(false);
                    SyncScheduler.getInstance().stopSync();
                    savePreferences();
                }
            }
        });

        if (SyncScheduler.getInstance().getSyncStatus()) {
            updateTextView(getString(R.string.sync_inProgress));
        } else {
            updateTextView(getString(R.string.sync_stopped));
        }

        btnCompleteSync.setOnClickListener(clickListener);
        mMomentStartDateEt.setOnClickListener(clickListener);
        mMomentEndDateEt.setOnClickListener(clickListener);
        btnStartSyncByDateRange.setOnClickListener(clickListener);
        btnDeleteSyncedData.setOnClickListener(clickListener);
        btnMigrateData.setOnClickListener(clickListener);
        btnResetMigrationFlag.setOnClickListener(clickListener);
        btnFetchMomentConsentStatus.setOnClickListener(clickListener);
        btnSetLastSyncTimestamp.setOnClickListener(clickListener);
        mLastSyncDateEt.setOnClickListener(clickListener);

        return fragmentView;
    }

    private void updateMomentConsentStatusView() {
        momentConsentStatusTextView.setText("fetching...");
        JustInTimeConsentDependencies.appInfra.getConsentManager().fetchConsentTypeState("moment", new FetchConsentCallback() {
            @Override
            public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                momentConsentStatusTextView.setText(consentDefinitionStatus.getConsentState().toString());
            }

            @Override
            public void onGetConsentFailed(ConsentError error) {
                momentConsentStatusTextView.setText("Error fetching...");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        SyncScheduler.getInstance().setListener(this);
        DataServicesManager.getInstance().registerDBChangeListener(this);
        DataServicesManager.getInstance().registerSynchronisationCompleteListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        savePreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        savePreferences();
    }

    @Override
    public void onSyncComplete() {
        updateTextView(getString(R.string.sync_completed));
    }

    @Override
    public void onSyncFailed(Exception exception) {
        if (exception.getClass().getSimpleName().equalsIgnoreCase("SyncException")) {
            Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            updateTextView(getString(R.string.sync_failed));
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
        DataServicesManager.getInstance().unRegisterSynchronisationCosmpleteListener();
        SyncScheduler.getInstance().setListener(null);
    }

    @Override
    public void onSyncStatusChanged(boolean isRunning) {
        if (!(((Activity) mContext).isFinishing())) {
            if (isRunning) {
                updateTextView(getString(R.string.sync_inProgress));
            } else {
                updateTextView(getString(R.string.sync_stopped));
            }
        }
    }

    @Override
    public void onFetchSuccess(List<? extends Moment> data) {

    }

    @Override
    public void onFetchFailure(Exception exception) {
    }

    @Override
    public void onSuccess(List<? extends Moment> data) {

    }

    @Override
    public void onFailure(Exception exception) {
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
    }

    @Override
    public void dBChangeFailed(Exception e) {
    }

    @Override
    public int getActionbarTitleResId() {
        return 0;
    }

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    private void fetchSyncByDateRange() {
        DateTime jodaStartDate = (mStartDate == null) ? new DateTime(0) : new DateTime(mStartDate);
        DateTime jodaEndDate = (mEndDate == null) ? new DateTime() : new DateTime(mEndDate);
        updateTextView(getString(R.string.sync_inProgress));
        mMomentPresenter.fetchSyncByDateRange(jodaStartDate, jodaEndDate, this);
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = getActivity().getPreferences(mContext.MODE_PRIVATE);
        boolean  syncState = sharedPreferences.getBoolean("syncState", true );
        mEnableDisableSync.setChecked(syncState);
    }

    private void savePreferences(){
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("syncState", mEnableDisableSync.isChecked());
        editor.commit();
    }


    private void updateMigrationFlagView() {
        flagStateView.setText(checkMigrationFlag() ? "Yes" : "No");
    }

    private boolean checkMigrationFlag() {
        return DataServicesManager.getInstance().isGdprMigrationDone();
    }

    private void updateEditTextView(EditText editTextView, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String dateAsString = sdf.format(myCalendar.getTime());
        editTextView.setText(dateAsString);
    }

    private void updateTextView(final String text) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvSyncStatus.setText(text);
                }
            });
        }
    }

    private void deleteSyncedData() {
        DataServicesManager.getInstance().deleteSyncedMoments(new DBRequestListener<Moment>() {
            @Override
            public void onSuccess(List<? extends Moment> data) {
                this.showSnackbar(R.string.dsc_delete_synced_moments_success);
            }

            @Override
            public void onFailure(Exception exception) {
                this.showSnackbar(R.string.dsc_delete_synced_moments_failed);
            }

            private void showSnackbar(int msg) {
                Snackbar.make(fragmentView, msg, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void migrateGDPRData() {
        DataServicesManager.getInstance().migrateGDPR(new DBRequestListener<Object>() {
            @Override
            public void onSuccess(List<?> data) {
                Toast.makeText(getContext(), "GDPR migration completed", Toast.LENGTH_LONG).show();
                updateMigrationFlagView();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "GDPR migration failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
