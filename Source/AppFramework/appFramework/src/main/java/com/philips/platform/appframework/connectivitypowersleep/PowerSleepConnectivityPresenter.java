/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
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
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
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

    private ConnectivityHelper connectivityHelper;

    public PowerSleepConnectivityPresenter(ConnectivityHelper connectivityHelper,Context context, final ConnectivityPowerSleepContract.View connectivityViewListener, UIView uiView) {
        super(uiView);
        this.context = context;
        this.connectivityViewListener = connectivityViewListener;
        this.connectivityHelper=connectivityHelper;

    }

    public void initDataServiceInterface(DataServicesManager dataServicesManager) {
        this.dataServicesManager = dataServicesManager;
    }

    @Override
    public void synchronizeSessionData(final RefAppBleReferenceAppliance bleReferenceAppliance) {
        connectivityViewListener.showProgressDialog();
        dataServicesManager.fetchLatestMomentByType(MomentType.SLEEP_SESSION,new DBFetchRequestListner<Moment>() {
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
                        }
                        if(sleepDataList.size()>0) {
                            savePowerSleepMomentsData(sleepDataList.getSortedList());
                            RALog.d(Constants.POWER_SLEEP_CONNECTIVITY_TAG,"Moment list size::" + sleepDataList.getSortedList().size());
                        }else{
                            onNoNewSessionsAvailable();
                            RALog.d(Constants.POWER_SLEEP_CONNECTIVITY_TAG,"No New session available");
                        }
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
                RALog.d(Constants.POWER_SLEEP_CONNECTIVITY_TAG,"Error while fetching latest moment with type sleepSession");
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
            momentList.add(connectivityHelper.createMoment(String.valueOf(connectivityHelper.calculateDeepSleepScore(session.getSummary().getDeepSleepTime())), String.valueOf(session.getSummary().getDeepSleepTime()),String.valueOf(session.getSummary().getTotalSleepTime()), new DateTime(session.getDate().getTime()), ""));
        }
        dataServicesManager.saveMoments(momentList, new DBRequestListener<Moment>() {
            @Override
            public void onSuccess(List<? extends Moment> list) {
                fetchLatestSessionInfo();
                dataServicesManager.synchronize();
                RALog.d(TAG,"Power sleep data pushed to DB" + list.size());
            }

            @Override
            public void onFailure(Exception e) {
                RALog.e(TAG, "Exception while saving moment in DB" + e.getMessage());
            }
        });
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

    public void fetchLatestSessionInfo(){
        dataServicesManager.fetchLatestMomentByType(MomentType.SLEEP_SESSION,new DBFetchRequestListner<Moment>() {
            @Override
            public void onFetchSuccess(final List<? extends Moment> list) {
                        if(list!=null && list.size()>0) {
                            Moment moment=list.get(0);
                            Summary summary=connectivityHelper.getSummaryInfoFromMoment(moment);
                            connectivityViewListener.updateScreenWithLatestSessionInfo(summary);
                        }else{
                            RALog.d(Constants.POWER_SLEEP_CONNECTIVITY_TAG,"sessions data not available in db");
                        }

            }

            @Override
            public void onFetchFailure(final Exception e) {
                connectivityViewListener.showToast("Error while fetching data from power sleep device. Error::"+e.getMessage());
            }
        });
    }


}
