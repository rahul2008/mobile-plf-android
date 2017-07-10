package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Medication;

import java.util.List;

/**
 * Created by philips on 6/29/17.
 */

public class THSMedication {
    List<Medication> medicationList;
    public List<Medication> getMedicationList() {
        return medicationList;
    }

    public void setMedicationList(List<Medication> medicationList) {
        this.medicationList = medicationList;
    }


}
