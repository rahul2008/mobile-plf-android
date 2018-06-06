/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.List;

public class GdprFeatureFragment extends DSBaseFragment
        implements DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener {

    private Button btnFetchMomentConsentStatus;
    private Button btnMigrateData;
    private Button btnResetMigrationFlag;
    private TextView flagStateView;
    private TextView momentConsentStatusTextView;


    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnMigrateData) {
                migrateGDPRData();
            } else if (view == btnResetMigrationFlag) {
                DataServicesManager.getInstance().resetGDPRMigrationFlag();
                updateMigrationFlagView();
            } else if (view == btnFetchMomentConsentStatus) {
                updateMomentConsentStatusView();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.gdpr_features, container, false);
        btnMigrateData = fragmentView.findViewById(R.id.migrate_data);
        btnResetMigrationFlag = fragmentView.findViewById(R.id.reset_migration_flag);
        flagStateView = fragmentView.findViewById(R.id.migration_flag_status);
        btnFetchMomentConsentStatus = fragmentView.findViewById(R.id.fetch_moment_consent_status);
        momentConsentStatusTextView = fragmentView.findViewById(R.id.moment_consent_status);
        updateMigrationFlagView();
        updateMomentConsentStatusView();

        btnMigrateData.setOnClickListener(clickListener);
        btnResetMigrationFlag.setOnClickListener(clickListener);
        btnFetchMomentConsentStatus.setOnClickListener(clickListener);

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
        DataServicesManager.getInstance().registerDBChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
        DataServicesManager.getInstance().unRegisterSynchronisationCosmpleteListener();
        SyncScheduler.getInstance().setListener(null);
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


    private void updateMigrationFlagView() {
        flagStateView.setText(checkMigrationFlag() ? "Yes" : "No");
    }

    private boolean checkMigrationFlag() {
        return DataServicesManager.getInstance().isGdprMigrationDone();
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
