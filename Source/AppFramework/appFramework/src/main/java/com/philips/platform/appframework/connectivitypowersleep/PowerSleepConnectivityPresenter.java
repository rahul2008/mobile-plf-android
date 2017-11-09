/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.UIView;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.database.datatypes.MeasurementDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementType;
import com.philips.platform.dscdemo.database.datatypes.MomentDetailType;
import com.philips.platform.dscdemo.database.datatypes.MomentType;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class PowerSleepConnectivityPresenter extends AbstractUIBasePresenter implements ConnectivityPowerSleepContract.UserActionsListener {
    public static final String TAG = PowerSleepConnectivityPresenter.class.getSimpleName();
    private ConnectivityPowerSleepContract.View connectivityViewListener;
    private Context context;
    protected DataServicesManager dataServicesManager;

    public PowerSleepConnectivityPresenter(Context context, final ConnectivityPowerSleepContract.View connectivityViewListener, UIView uiView) {
        super(uiView);
        this.context = context;
        this.connectivityViewListener = connectivityViewListener;
        initDataServiceInterface();
    }

    public void initDataServiceInterface() {
        dataServicesManager = DataServicesManager.getInstance();
    }

    @Override
    public void synchroniseSessionData(final BleReferenceAppliance bleReferenceAppliance) {
        connectivityViewListener.showProgressDialog();
        dataServicesManager.fetchLatestMomentByType(MomentType.SLEEP_SESSION, new DBFetchRequestListner<Moment>() {
            @Override
            public void onFetchSuccess(final List<? extends Moment> list) {
                DateTime latestSessionDateTime;
                if (list.size() > 0) {
                    latestSessionDateTime = list.get(0).getDateTime();
                } else {
                    latestSessionDateTime = new DateTime(Long.MIN_VALUE);
                }
                getSynchronizeSessionsUsecase().execute(bleReferenceAppliance, new SynchronizeSessionsUsecase.Callback() {
                    @Override
                    public void onSynchronizeSucceed(@NonNull SessionsOldestToNewest sleepDataList) {
                        if (connectivityViewListener != null) {
                            connectivityViewListener.hideProgressDialog();
                            connectivityViewListener.showToast("Moment list size::" + sleepDataList.getSortedList().size());
                        }
                        savePowerSleepMomentsData(sleepDataList.getSortedList());
                    }

                    @Override
                    public void onNoNewSessionsAvailable() {
                        connectivityViewListener.showToast("No new sessions available");
                    }

                    @Override
                    public void onError(@NonNull Exception error) {
                        connectivityViewListener.hideProgressDialog();
                        connectivityViewListener.showToast("Error while fetching data:Error:" + error.getMessage());
                    }
                }, latestSessionDateTime);
            }

            @Override
            public void onFetchFailure(final Exception e) {
                connectivityViewListener.showToast("Exception");
            }
        });
    }

    @NonNull
    protected SynchronizeSessionsUsecase getSynchronizeSessionsUsecase() {
        return new SynchronizeSessionsUsecase(new AllSessionsProviderFactory());
    }

    public void savePowerSleepMomentsData(List<Session> sessionList) {
        List<Moment> momentList = new ArrayList<>();
        for (Session session : sessionList) {
            momentList.add(createMoment("", "86", "" + session.getSummary().getDeepSleepTime(), "" + session.getSummary().getTotalSleepTime(), new DateTime(session.getDate().getTime()), ""));
        }
        dataServicesManager.saveMoments(momentList, new DBRequestListener<Moment>() {
            @Override
            public void onSuccess(List<? extends Moment> list) {
                DataServicesManager.getInstance().synchronize();
                connectivityViewListener.showToast("Power sleep data pushed to DB" + list.size());
            }

            @Override
            public void onFailure(Exception e) {
                RALog.e(TAG, "Exception while saving moment in DB" + e.getMessage());
            }
        });
    }

    protected Moment createMoment(String momemtDetail, String dstScore, String deepSleepTime, String sleepTime, DateTime dateTime, String measurementDetail) {
        Moment moment = this.dataServicesManager.createMoment(MomentType.SLEEP_SESSION);
        moment.setDateTime(dateTime);

        MeasurementGroup measurementGroupInside;
        MeasurementGroup measurementGroup;
        Measurement deepSleepTimeMeasurement;
        Measurement sleepTimeMeasurement;
        Measurement dstScoreMeasurement;
        dataServicesManager.createMomentDetail(MomentDetailType.DEVICE_SERIAL, "3249879989", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.CTN, "HX506", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.FW_VERSION, "283749", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.MOMENT_VERSION, "2", moment);
        measurementGroup = dataServicesManager.createMeasurementGroup(moment);
        dataServicesManager.createMeasurementGroupDetail(MeasurementGroupDetailType.REFERENCE_GROUP_ID, measurementDetail, measurementGroup);
        measurementGroupInside = dataServicesManager.createMeasurementGroup(measurementGroup);
        deepSleepTimeMeasurement = dataServicesManager.createMeasurement(MeasurementType.DST, deepSleepTime, MeasurementType.getUnitFromDescription(MeasurementType.DST), measurementGroupInside);
        sleepTimeMeasurement = dataServicesManager.createMeasurement(MeasurementType.TST, sleepTime, MeasurementType.getUnitFromDescription(MeasurementType.TST), measurementGroupInside);
        dstScoreMeasurement = dataServicesManager.createMeasurement(MeasurementType.DST_SCORE, dstScore, "%", measurementGroupInside);
        dataServicesManager.createMeasurementDetail(MeasurementDetailType.LOCATION, measurementDetail, deepSleepTimeMeasurement);
        measurementGroupInside.addMeasurement(deepSleepTimeMeasurement);
        measurementGroupInside.addMeasurement(sleepTimeMeasurement);
        measurementGroupInside.addMeasurement(dstScoreMeasurement);
        measurementGroup.addMeasurementGroup(measurementGroupInside);
        moment.addMeasurementGroup(measurementGroup);
        return moment;
    }

    @Override
    public void onEvent(int componentID) {
        BaseFlowManager targetFlowManager = ((AppFrameworkApplication) context.getApplicationContext()).getTargetFlowManager();

        try {
            BaseState baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.POWER_SLEEP_CONNECTIVITY), AppStates.INSIGHTS);
            if (null != baseState) {
                baseState.navigate(new FragmentLauncher((FragmentActivity) context, R.id.frame_container, (ActionBarListener) ((FragmentActivity) context)));
            }
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
            Toast.makeText(getApplicationContext(), context.getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }
}
