package cdp.philips.com.mydemoapp.temperature;

import android.util.Log;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.ArrayList;
import java.util.List;

import cdp.philips.com.mydemoapp.database.OrmTypeChecking;
import cdp.philips.com.mydemoapp.database.datatypes.MomentDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureMomentHelper {

    String getTemperature(Moment moment) {
        try {

            ArrayList<? extends MeasurementGroup> measurementGroupParent = new ArrayList<>(moment.getMeasurementGroups());
            ArrayList<? extends MeasurementGroup> measurementGroupChild = new ArrayList<>(measurementGroupParent.get(0).getMeasurementGroups());
            ArrayList<? extends Measurement> measurements = new ArrayList<>(measurementGroupChild.get(0).getMeasurements());

            // ArrayList<? extends Measurement> measurements = new ArrayList<>(moment.getMeasurements());
            return measurements.get(0).getValue();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "default";
        } catch (IndexOutOfBoundsException e) {
            return "default";
        }catch (Exception e){
            return "default";
        }
        //return -1;
    }

    String getTime(Moment moment) {
        try {
            ArrayList<? extends MomentDetail> momentDetails = new ArrayList<>(moment.getMomentDetails());
            for (MomentDetail detail : momentDetails) {
                if (detail.getType().equalsIgnoreCase(MomentDetailType.PHASE))
                    return detail.getValue();
            }
            return "default";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "default";
        } catch (IndexOutOfBoundsException e) {
            return "default";
        }catch (Exception e){
            return "default";
        }
    }

    public void notifySuccess(ArrayList<? extends Object> ormMoments, DBRequestListener dbRequestListener) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess((ArrayList<? extends Object>) ormMoments);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().onSuccess(ormMoments);
        }else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG,"CallBack not registered");
        }
    }


    public void notifySuccess(DBRequestListener dbRequestListener) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(null);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().onSuccess(null);
        }else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, ArrayList<OrmConsent> ormConsents) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormConsents.get(0));
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().onSuccess(ormConsents.get(0));
        }else {
            //Callback not registerd
            DSLog.i(DataServicesManager.TAG,"Callback Not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, List<OrmSettings> ormSettingsList) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormSettingsList);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().onSuccess(ormSettingsList);
        }else {
            //Callback not registerd
            DSLog.i(DataServicesManager.TAG,"Callback Not registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, OrmMoment ormMoment) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(ormMoment);
        } else if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().onSuccess(ormMoment);
        } else {
            //No Callback registered
            DSLog.i(DataServicesManager.TAG, "No callback registered");
        }
    }

    public void notifySuccess(DBRequestListener dbRequestListener, OrmConsent ormConsent) {
        if(dbRequestListener!=null) {
            dbRequestListener.onSuccess(ormConsent);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            dbRequestListener=DataServicesManager.getInstance().getDbChangeListener();
            dbRequestListener.onSuccess(ormConsent);
        }else {
            //Callback Not registered
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifyFailure(Exception e, DBRequestListener dbRequestListener) {
        if(dbRequestListener!=null) {
            dbRequestListener.onFailure(e);
        }else if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().onFailure(e);
        }else {
            //Callback No registered
            DSLog.i(DataServicesManager.TAG,"Callback not registered");
        }
    }

    public void notifyOrmTypeCheckingFailure(DBRequestListener dbRequestListener, OrmTypeChecking.OrmTypeException e, String msg) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        } else if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().onFailure(e);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, msg);
        }
    }

    String getNotes(Moment moment) {
        try {

            ArrayList<? extends MeasurementGroup> measurementGroupParent = new ArrayList<>(moment.getMeasurementGroups());
            ArrayList<? extends MeasurementGroup> measurementGroupChild = new ArrayList<>(measurementGroupParent.get(0).getMeasurementGroups());
            ArrayList<? extends Measurement> measurements = new ArrayList<>(measurementGroupChild.get(0).getMeasurements());

            // ArrayList<? extends Measurement> measurements = new ArrayList<>(moment.getMeasurements());
            //return measurements.get(0).getValue();

            // ArrayList<? extends Measurement> measurements = new ArrayList<>(moment.getMeasurements());
            Measurement measurement = measurements.get(0);
            ArrayList<? extends MeasurementDetail> measurementDetails = new ArrayList<>(measurement.getMeasurementDetails());
            return measurementDetails.get(0).getValue();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "default";
        } catch (IndexOutOfBoundsException e) {
            return "default";
        }catch (Exception e){
            return "default";
        }
        //return null;
    }

}
