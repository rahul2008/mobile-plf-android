package cdp.philips.com.mydemoapp.temperature;

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
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
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
