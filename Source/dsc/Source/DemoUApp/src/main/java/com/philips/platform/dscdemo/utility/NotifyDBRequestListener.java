package com.philips.platform.dscdemo.utility;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.database.OrmTypeChecking;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmMoment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotifyDBRequestListener {

    public void notifySuccess(List<? extends Object> ormObjectList, DBRequestListener dbRequestListener, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormObjectList);
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(null);
        }
    }

    public void notifyPrepareForDeletion(DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(null);
        }
    }

    public <T> void notifySuccess(DBRequestListener<T> dbRequestListener, T... returnValue) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(Arrays.asList(returnValue));
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, Settings settings, SyncType type) {
        if (dbRequestListener != null) {
            List list = new ArrayList();
            list.add(settings);
            dbRequestListener.onSuccess(list);
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, ArrayList<OrmConsentDetail> ormConsents, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormConsents);
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, OrmMoment ormMoment, SyncType type) {
        if (dbRequestListener != null) {
            List list = new ArrayList();
            list.add(ormMoment);
            dbRequestListener.onSuccess(list);
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, List<OrmConsentDetail> ormConsents, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormConsents);
        }
    }

    public void notifyFailure(Exception e, DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        }
    }

    public void notifyOrmTypeCheckingFailure(DBRequestListener dbRequestListener, OrmTypeChecking.OrmTypeException e, String msg) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        }
    }

    public void notifyConsentFetchSuccess(DBFetchRequestListner dbFetchRequestListner, ArrayList<OrmConsentDetail> ormConsents) {
        if (dbFetchRequestListner != null) {
            dbFetchRequestListner.onFetchSuccess(ormConsents);
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
        }
    }
}
