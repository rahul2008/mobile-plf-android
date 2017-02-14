package com.philips.platform.baseapp.screens.dataservices.utility;

import com.philips.platform.baseapp.screens.dataservices.database.OrmTypeChecking;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sangamesh on 18/01/17.
 */

public class NotifyDBRequestListener {


    public void notifySuccess(List<? extends Object> ormObjectList, DBRequestListener dbRequestListener) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess((ArrayList<? extends Object>) ormObjectList);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess();
        }else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG,"CallBack not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(null);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess();
        }else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener,Settings settings) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(settings);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess();
        }else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, ArrayList<OrmConsentDetail> ormConsents) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormConsents);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess();
        }else {
            //Callback not registerd
            DSLog.i(DataServicesManager.TAG,"Callback Not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, OrmMoment ormMoment) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormMoment);
        } else if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess();
        } else {
            //No Callback registered
            DSLog.i(DataServicesManager.TAG, "No callback registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, List<OrmConsentDetail> ormConsents) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormConsents);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DBChangeListener dbChangeListener=DataServicesManager.getInstance().getDbChangeListener();
            dbChangeListener.dBChangeSuccess();
        }else {
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifyFailure(Exception e, DBRequestListener dbRequestListener) {
        if(dbRequestListener!=null) {
            dbRequestListener.onFailure(e);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().dBChangeFailed(e);
        }else {
            //Callback No registered
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifyOrmTypeCheckingFailure(DBRequestListener dbRequestListener, OrmTypeChecking.OrmTypeException e, String msg) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        } else if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().dBChangeFailed(e);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, msg);
        }
    }
}
