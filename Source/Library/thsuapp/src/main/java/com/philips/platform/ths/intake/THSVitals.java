package com.philips.platform.ths.intake;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.visit.Vitals;

public class THSVitals {

    Vitals vitals;

    public Vitals getVitals() {
        return vitals;
    }

    public void setVitals(Vitals vitals) {
        this.vitals = vitals;
    }

    Integer getSystolic(){
        return vitals.getSystolic();
    }

    Integer getDiastolic(){
        return vitals.getDiastolic();
    }

    Double getTemperature(){
        return vitals.getTemperature();
    }

    Integer getWeight(){
        return vitals.getWeight();
    }

    void setSystolic(@NonNull Integer systolic){
        vitals.setSystolic(systolic);
    }

    void setDiastolic(@NonNull Integer diastolic){
        vitals.setDiastolic(diastolic);
    }

    void setTemperature(@NonNull Double temperature){
        vitals.setTemperature(temperature);
    }

    void setWeight(@NonNull Integer weight){
        vitals.setWeight(weight);
    }

    boolean isEmpty(){
        return vitals.isEmpty();
    }
}
