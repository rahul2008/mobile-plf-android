/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.visit.Vitals;

public class THSVitals {

    private Vitals vitals;

    public Vitals getVitals() {
        return vitals;
    }

    public void setVitals(Vitals vitals) {
        this.vitals = vitals;
    }

    public Integer getSystolic() {
        return vitals.getSystolic();
    }

    public Integer getDiastolic() {
        return vitals.getDiastolic();
    }

    public Double getTemperature() {
        return vitals.getTemperature();
    }

    public Integer getWeight() {
        return vitals.getWeight();
    }

    public void setSystolic(@NonNull Integer systolic) {
        vitals.setSystolic(systolic);
    }

    public void setDiastolic(@NonNull Integer diastolic) {
        vitals.setDiastolic(diastolic);
    }

    public void setTemperature(@NonNull Double temperature) {
        vitals.setTemperature(temperature);
    }

    public void setWeight(@NonNull Integer weight) {
        vitals.setWeight(weight);
    }

    public boolean isEmpty() {
        return vitals.isEmpty();
    }
}
