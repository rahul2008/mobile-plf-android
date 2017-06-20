package cdp.philips.com.utility;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.database.OrmTypeChecking;
import cdp.philips.com.database.table.OrmConsentDetail;
import cdp.philips.com.database.table.OrmInsight;
import cdp.philips.com.database.table.OrmMoment;


public class NotifyDBRequestListener {


    public void notifySuccess(List<? extends Object> ormObjectList, DBRequestListener dbRequestListener, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess((ArrayList<? extends Object>) ormObjectList);
        } else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(null);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifyPrepareForDeletion(DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(null);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, Settings settings, SyncType type) {
        if (dbRequestListener != null) {
            List list = new ArrayList();
            list.add(settings);
            dbRequestListener.onSuccess(list);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, ArrayList<OrmConsentDetail> ormConsents, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormConsents);
        } else {
            //Callback not registerd
            DSLog.i(DataServicesManager.TAG, "Callback Not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, OrmMoment ormMoment, SyncType type) {
        if (dbRequestListener != null) {
            List list = new ArrayList();
            list.add(ormMoment);
            dbRequestListener.onSuccess(list);
        } else {
            //No Callback registered
            DSLog.i(DataServicesManager.TAG, "No callback registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, List<OrmConsentDetail> ormConsents, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormConsents);
        } else {
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifyFailure(Exception e, DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        } else {
            //Callback No registered
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifyOrmTypeCheckingFailure(DBRequestListener dbRequestListener, OrmTypeChecking.OrmTypeException e, String msg) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, msg);
        }
    }

    public void notifyConsentFetchSuccess(DBFetchRequestListner dbFetchRequestListner, ArrayList<OrmConsentDetail> ormConsents) {
        if (dbFetchRequestListner != null) {
            dbFetchRequestListner.onFetchSuccess(ormConsents);
        } else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }

    public void notifyDBChange(SyncType type) {
        if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess(type);
        }
    }

    public void notifyMomentsSaveSuccess(List<Moment> moments, DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(moments);
        }
    }

    public void notifyMomentFetchSuccess(List<OrmMoment> ormMoments, DBFetchRequestListner dbFetchRequestListner) {
        if (dbFetchRequestListner != null) {
            dbFetchRequestListner.onFetchSuccess(ormMoments);
        } else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }

    public void notifyInsightSaveSuccess(List<Insight> insights, DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(insights);
        }
    }

    public void notifyInsightFetchSuccess(List<OrmInsight> ormInsights, DBFetchRequestListner dbFetchRequestListner) {
        if (dbFetchRequestListner != null) {
            dbFetchRequestListner.onFetchSuccess(ormInsights);
        } else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }
}
