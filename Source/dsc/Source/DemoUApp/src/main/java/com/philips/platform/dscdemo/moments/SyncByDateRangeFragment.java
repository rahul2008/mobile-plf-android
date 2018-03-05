/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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

import org.joda.time.DateTime;

import java.text.ParseException;
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

    private Date mStartDate;
    private DateTime jodaStartDate;
    private Date mEndDate;
    private DateTime jodaEndDate;
    private EditText mMomentStartDateEt;
    private EditText mMomentEndDateEt;
    private Calendar myCalendar;
    private TextView tvSyncStatus;
    private Button btnMigrateData;
    private Button btnResetMigrationFlag;
    private TextView flagStateView;

    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                updateStartDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                updateEndDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnCompleteSync) {
                updateTextView(getString(R.string.sync_inProgress));
                DataServicesManager.getInstance().synchronize();
            } else if (view == mMomentStartDateEt) {
                new DatePickerDialog(mContext, startDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else if (view == mMomentEndDateEt) {
                new DatePickerDialog(mContext, endDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } else if (view == btnStartSyncByDateRange) {
                fetchSyncByDateRange();
            } else if (view == btnDeleteSyncedData) {
                deleteSyncedData();
            } else if (view == btnMigrateData) {
                migrateGDPRData();
            } else if (view == btnResetMigrationFlag) {
                boolean status = checkMigrationFlag();
                flagStateView.setText(status ? "Yes" : "No");
            }
        }
    };

    private boolean checkMigrationFlag() {
        return DataServicesManager.getInstance().isGdprMigrationDone();
    }

    private View fragmentView;

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

        ToggleButton mEnableDisableSync = fragmentView.findViewById(R.id.toggleButton);

        if (!SyncScheduler.getInstance().isSyncEnabled()) {
            mEnableDisableSync.setChecked(false);
        }

        mEnableDisableSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SyncScheduler.getInstance().setSyncEnable(true);
                    SyncScheduler.getInstance().scheduleSync();
                } else {
                    SyncScheduler.getInstance().setSyncEnable(false);
                    SyncScheduler.getInstance().stopSync();
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
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        SyncScheduler.getInstance().setListener(this);
        DataServicesManager.getInstance().registerDBChangeListener(this);
        DataServicesManager.getInstance().registerSynchronisationCompleteListener(this);
    }

    private void fetchSyncByDateRange() {
        jodaStartDate = (mStartDate == null) ? new DateTime(0) : new DateTime(mStartDate);
        jodaEndDate = (mEndDate == null) ? new DateTime() : new DateTime(mEndDate);
        if (jodaStartDate != null && jodaEndDate != null) {
            updateTextView(getString(R.string.sync_inProgress));
        }
        mMomentPresenter.fetchSyncByDateRange(jodaStartDate, jodaEndDate, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    private void updateStartDate() throws ParseException {
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String dateAsString = sdf.format(myCalendar.getTime());
        mMomentStartDateEt.setText(dateAsString);
        mStartDate = sdf.parse(dateAsString);
    }


    private void updateEndDate() throws ParseException {
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String dateAsString = sdf.format(myCalendar.getTime());
        mMomentEndDateEt.setText(dateAsString);
        mEndDate = sdf.parse(dateAsString);
    }

    private void updateTextView(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSyncStatus.setText(text);
            }
        });
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
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(), "GDPR migration failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
