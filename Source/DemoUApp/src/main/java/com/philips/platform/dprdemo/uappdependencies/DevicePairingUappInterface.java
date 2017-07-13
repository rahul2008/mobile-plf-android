/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.uappdependencies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.philips.platform.dprdemo.utils.SyncScheduler;
import com.philips.platform.dprdemo.database.DatabaseHelper;
import com.philips.platform.dprdemo.database.ORMSavingInterfaceImpl;
import com.philips.platform.dprdemo.database.ORMUpdatingInterfaceImpl;
import com.philips.platform.dprdemo.database.OrmCreator;
import com.philips.platform.dprdemo.database.OrmDeleting;
import com.philips.platform.dprdemo.database.OrmDeletingInterfaceImpl;
import com.philips.platform.dprdemo.database.OrmFetchingInterfaceImpl;
import com.philips.platform.dprdemo.database.OrmSaving;
import com.philips.platform.dprdemo.database.OrmUpdating;
import com.philips.platform.dprdemo.database.table.BaseAppDateTime;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;
import com.philips.platform.dprdemo.database.table.OrmDCSync;
import com.philips.platform.dprdemo.error.ErrorHandlerInterfaceImpl;
import com.philips.platform.dprdemo.registration.UserRegistrationInterfaceImpl;
import com.philips.platform.dprdemo.ui.PairingFragment;
import com.philips.platform.dprdemo.ui.DevicePairingLaunchActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DevicePairingUappInterface implements UappInterface {
    private Context mContext;
    private DataServicesManager mDataServicesManager;
    private DatabaseHelper mDatabaseHelper;

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        mContext = uappSettings.getContext();
        initPreRequisite(uappSettings.getContext(), uappDependencies.getAppInfra());
    }

    private void initPreRequisite(Context context, AppInfraInterface appInfra) {
        mContext = context;
        mDatabaseHelper = DatabaseHelper.getInstance(context, new UuidGenerator());
        appInfra.getLogging().createInstanceForComponent("DevicePairing", "DevicePairing");
        init();
    }

    private void init() {
        mDataServicesManager = DataServicesManager.getInstance();

        OrmCreator creator = new OrmCreator(new UuidGenerator());
        UserRegistrationInterface userRegistrationInterface = new UserRegistrationInterfaceImpl(mContext, new User(mContext));
        ErrorHandlerInterfaceImpl errorHandlerInterface = new ErrorHandlerInterfaceImpl();

        Set<String> fetchers = new HashSet<>(2);
        fetchers.add(SyncType.MOMENT.getDescription());
        fetchers.add(SyncType.CONSENT.getDescription());
        mDataServicesManager.configureSyncDataType(fetchers);

        DataServicesManager.getInstance().initializeDataServices(mContext, creator, userRegistrationInterface, errorHandlerInterface);
        injectDBInterfacesToCore();
        DataServicesManager.getInstance().initializeSyncMonitors(mContext, null, null);

        User user = new User(mContext);
        if (user.isUserSignIn()) {
            SyncScheduler.getInstance().scheduleSync();
        }
    }

    private void injectDBInterfacesToCore() {
        try {
            Dao<OrmConsentDetail, Integer> consentDetailsDao = mDatabaseHelper.getConsentDetailsDao();
            Dao<OrmDCSync, Integer> dcSyncDao = mDatabaseHelper.getDCSyncDao();

            OrmSaving saving = new OrmSaving(consentDetailsDao, dcSyncDao);
            OrmUpdating updating = new OrmUpdating(consentDetailsDao, dcSyncDao);
            OrmFetchingInterfaceImpl fetching = new OrmFetchingInterfaceImpl(consentDetailsDao, dcSyncDao);
            OrmDeleting deleting = new OrmDeleting(consentDetailsDao, dcSyncDao);

            BaseAppDateTime baseAppDateTime = new BaseAppDateTime();
            ORMSavingInterfaceImpl ORMSavingInterfaceImpl = new ORMSavingInterfaceImpl(saving, updating, fetching, deleting, baseAppDateTime);
            OrmDeletingInterfaceImpl ORMDeletingInterfaceImpl = new OrmDeletingInterfaceImpl(deleting, saving, fetching);
            ORMUpdatingInterfaceImpl dbInterfaceOrmUpdatingInterface = new ORMUpdatingInterfaceImpl(saving, updating, fetching, deleting);
            OrmFetchingInterfaceImpl dbInterfaceOrmFetchingInterface = new OrmFetchingInterfaceImpl(consentDetailsDao, dcSyncDao);

            mDataServicesManager.initializeDatabaseMonitor(mContext, ORMDeletingInterfaceImpl, dbInterfaceOrmFetchingInterface, ORMSavingInterfaceImpl, dbInterfaceOrmUpdatingInterface);
        } catch (SQLException exception) {
            Toast.makeText(mContext, "db injection failed to dataservices", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity();
        } else {
            launchAsFragment(uiLauncher);
        }
    }

    private void launchAsActivity() {
        Intent intent = new Intent(mContext, DevicePairingLaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void launchAsFragment(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();

        PairingFragment pairingFragment = new PairingFragment();
        fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), pairingFragment, PairingFragment.TAG);
        fragmentTransaction.addToBackStack(PairingFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
