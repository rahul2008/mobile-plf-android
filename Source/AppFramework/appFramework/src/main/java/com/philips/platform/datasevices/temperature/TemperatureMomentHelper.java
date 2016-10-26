package com.philips.platform.datasevices.temperature;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TemperatureMomentHelper {

    double getTemperature(Moment moment){
        try {
            ArrayList<? extends Measurement> measurements = new ArrayList<>(moment.getMeasurements());
            return measurements.get(0).getValue();
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0.0;
        } catch (IndexOutOfBoundsException e) {
            return 0.0;
        }
    }

    String getTime(Moment moment){
        try {
            ArrayList<? extends MomentDetail> momentDetails = new ArrayList<>(moment.getMomentDetails());
            for(MomentDetail detail : momentDetails){
                if(detail.getType() == MomentDetailType.PHASE)
                    return detail.getValue();
            }
            return "default";
        }catch (ArrayIndexOutOfBoundsException e){
            return "default";
        }catch (IndexOutOfBoundsException e){
            return "default";
        }
    }

    String getNotes(Moment moment){
        try {
            ArrayList<? extends Measurement> measurements = new ArrayList<>(moment.getMeasurements());
            Measurement measurement = measurements.get(0);
            ArrayList<? extends MeasurementDetail> measurementDetails = new ArrayList<>(measurement.getMeasurementDetails());
            return measurementDetails.get(0).getValue();
        }catch (ArrayIndexOutOfBoundsException e){
            return "default";
        }catch (IndexOutOfBoundsException e){
            return "default";
        }
    }

    Moment updateMoment(Moment moment,String phase, String temperature, String notes){
        ArrayList<? extends MomentDetail> momentDetails = new ArrayList<>(moment.getMomentDetails());
        MomentDetail momentDetail = momentDetails.get(1);
        momentDetail.setValue(phase);

        ArrayList<? extends Measurement> measurements = new ArrayList<>(moment.getMeasurements());
        Measurement measurement = measurements.get(0);
        measurement.setValue(Double.parseDouble(temperature));

        ArrayList<? extends MeasurementDetail> measurementDetails = new ArrayList<>(measurement.getMeasurementDetails());
        MeasurementDetail measurementDetail = measurementDetails.get(0);
        measurementDetail.setValue(notes);

        return moment;
    }
}
