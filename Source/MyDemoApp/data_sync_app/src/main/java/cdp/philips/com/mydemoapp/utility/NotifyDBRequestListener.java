package cdp.philips.com.mydemoapp.utility;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.database.OrmTypeChecking;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;

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

    public void notifySuccess(DBRequestListener dbRequestListener, ArrayList<OrmConsent> ormConsents) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormConsents.get(0));
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

    public void notifySuccess(DBRequestListener dbRequestListener, OrmConsent ormConsent) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormConsent);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DBChangeListener dbChangeListener=DataServicesManager.getInstance().getDbChangeListener();
            dbChangeListener.dBChangeSuccess();
        }else {
            //Callback Not registered
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
