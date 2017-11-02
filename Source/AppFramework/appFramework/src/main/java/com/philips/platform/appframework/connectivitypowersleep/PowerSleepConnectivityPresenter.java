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
    private DataServicesManager dataServicesManager;

    public PowerSleepConnectivityPresenter(Context context, final ConnectivityPowerSleepContract.View connectivityViewListener, UIView uiView) {
        super(uiView);
        this.context = context;
        this.connectivityViewListener = connectivityViewListener;
        this.dataServicesManager=DataServicesManager.getInstance();
    }

    @Override
    public void removeSessionPortListener(BleReferenceAppliance appliance) {
        if (appliance != null) {
            appliance.getSessionDataPort().removePortListener(diCommPortListener);
        }
    }

//    @Override
//    public void savePowerSleepMomentsData(List<SessionDataPortProperties> sessionDataPortPropertiesList) {
//        List<Moment> momentList = new ArrayList<>();
//        for (SessionDataPortProperties sessionDataPortProperties : sessionDataPortPropertiesList) {
//            momentList.add(createMoment("",sessionDataPortProperties.getDeepSleepTime().toString(), sessionDataPortProperties.getTotalSleepTime().toString(),new DateTime(sessionDataPortProperties.getEpochTime()),""));
//        }
//        saveMomentToDB(momentList);
//    }

    @Override
    public void synchroniseSessionData(final BleReferenceAppliance bleReferenceAppliance) {
        connectivityViewListener.showProgressDialog();
//        DataServicesManager.getInstance().fetchLatestMomentByType(MomentType.SLEEP_SESSION, new DBFetchRequestListner<Moment>() {
//            @Override
//            public void onFetchSuccess(final List<? extends Moment> list) {
//
//            }
//
//            @Override
//            public void onFetchFailure(final Exception e) {
//
//            }
//        });
        DateTime latestSessionDateTime = new DateTime(Long.MIN_VALUE);
//        DateTime latestSessionDateTime=DateTime.list.get(0).getDateTime();
        new SynchronizeSessionsUsecase(new AllSessionsProviderFactory()).execute(bleReferenceAppliance, new SynchronizeSessionsUsecase.Callback() {
            @Override
            public void onSynchronizeSucceed(@NonNull SessionsOldestToNewest sleepDataList) {
                if(connectivityViewListener!=null) {
                    connectivityViewListener.hideProgressDialog();
                    connectivityViewListener.showToast("Moment list size::"+sleepDataList.getSortedList().size());
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

    public void savePowerSleepMomentsData(List<Session> sessionList) {
        List<Moment> momentList = new ArrayList<>();
        for (Session session : sessionList) {
            momentList.add(createMoment("",""+session.getSummary().getDeepSleepTime(), ""+session.getSummary().getTotalSleepTime(),new DateTime(session.getDate().getTime()),""));
        }
        DataServicesManager.getInstance().saveMoments(momentList, new DBRequestListener<Moment>() {
            @Override
            public void onSuccess(List<? extends Moment> list) {
                DataServicesManager.getInstance().synchronize();
                connectivityViewListener.showToast("Power sleep data pushed to DB"+list.size());
//                refreshUi();
            }

            @Override
            public void onFailure(Exception e) {
                RALog.e(TAG,"Exception while saving moment in DB"+e.getMessage());
            }
        });
//        saveMomentToDB(momentList);
    }

    private void saveMomentToDB(List<Moment> moments) {
        DataServicesManager.getInstance().saveMoments(moments, new DBRequestListener<Moment>() {
            @Override
            public void onSuccess(List<? extends Moment> list) {
                DataServicesManager.getInstance().synchronize();
                refreshUi();
            }

            @Override
            public void onFailure(Exception e) {
                RALog.e(TAG,"Exception while saving moment in DB"+e.getMessage());
            }
        });
    }

    protected Moment createMoment(String momemtDetail, String deepSleepTime,String sleepTime,DateTime dateTime,String measurementDetail) {
        Moment moment = this.dataServicesManager.createMoment(MomentType.SLEEP_SESSION);
        moment.setDateTime(dateTime);

        MeasurementGroup measurementGroupInside;
        MeasurementGroup measurementGroup;
        Measurement deepSleepTimeMeasurement;
        Measurement sleepTimeMeasurement;
        dataServicesManager.createMomentDetail(MomentDetailType.DEVICE_SERIAL, "3249879989", moment);
        dataServicesManager.createMomentDetail(MomentDetailType.CTN,"HX506",moment);
        dataServicesManager.createMomentDetail(MomentDetailType.FW_VERSION,"283749",moment);
        dataServicesManager.createMomentDetail(MomentDetailType.MOMENT_VERSION,"2",moment);
        measurementGroup = dataServicesManager.createMeasurementGroup(moment);
        dataServicesManager.createMeasurementGroupDetail(MeasurementGroupDetailType.REFERENCE_GROUP_ID, measurementDetail,measurementGroup);
        measurementGroupInside = dataServicesManager.createMeasurementGroup(measurementGroup);
        deepSleepTimeMeasurement = dataServicesManager.createMeasurement(MeasurementType.DST, deepSleepTime, "milliseconds", measurementGroupInside);
        sleepTimeMeasurement = dataServicesManager.createMeasurement(MeasurementType.TST, sleepTime, "milliseconds", measurementGroupInside);
        dataServicesManager.createMeasurementDetail(MeasurementDetailType.LOCATION, measurementDetail, deepSleepTimeMeasurement);
        measurementGroupInside.addMeasurement(deepSleepTimeMeasurement);
        measurementGroupInside.addMeasurement(sleepTimeMeasurement);
        measurementGroup.addMeasurementGroup(measurementGroupInside);
        moment.addMeasurementGroup(measurementGroup);
        return moment;
    }

    private void refreshUi() {
        DataServicesManager.getInstance().fetchLatestMomentByType(MomentType.SLEEP_SESSION, new DBFetchRequestListner<Moment>() {

                    @Override
                    public void onFetchSuccess(final List<? extends Moment> list) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Size of the moment type sleep" + list.size(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFetchFailure(Exception e) {
                        RALog.d(TAG,"Erro while fetching moment with type sleep"+e.getMessage());
                    }
                });
    }

    @Override
    public void setUpApplicance(@NonNull BleReferenceAppliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create bleReferenceAppliance for provided NetworkNode.");
        }

//        appliance.getSessionDataPort().addPortListener(diCommPortListener);
    }

    DICommPortListener diCommPortListener = new DICommPortListener<SessionDataPort>() {
        @Override
        public void onPortUpdate(SessionDataPort diCommPort) {
            connectivityViewListener.updateSessionData(((SessionDataPortProperties)diCommPort.getPortProperties()).getTotalSleepTime(), ((SessionDataPortProperties)diCommPort.getPortProperties()).getNumberOfInterruptions(), ((SessionDataPortProperties)diCommPort.getPortProperties()).getDeepSleepTime(),((SessionDataPortProperties)diCommPort.getPortProperties()).getEpochTime());

        }

        @Override
        public void onPortError(SessionDataPort diCommPort, Error error, @Nullable String s) {
            connectivityViewListener.showError(error, s);
        }
    };

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
