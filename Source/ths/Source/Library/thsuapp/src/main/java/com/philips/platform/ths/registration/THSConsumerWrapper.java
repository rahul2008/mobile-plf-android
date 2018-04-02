/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.insurance.Subscription;

import java.util.List;

public class THSConsumerWrapper implements Parcelable{
    private Consumer consumer;

    public THSConsumerWrapper(){

    }

    protected THSConsumerWrapper(Parcel in) {
        consumer = in.readParcelable(Consumer.class.getClassLoader());
    }

    public static final Creator<THSConsumerWrapper> CREATOR = new Creator<THSConsumerWrapper>() {
        @Override
        public THSConsumerWrapper createFromParcel(Parcel in) {
            return new THSConsumerWrapper(in);
        }

        @Override
        public THSConsumerWrapper[] newArray(int size) {
            return new THSConsumerWrapper[size];
        }
    };

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getGender(){
        return consumer.getGender();
    }

    public String getAge(){
        return consumer.getAge();
    }

    public String getFormularyRestriction(){
        return consumer.getFormularyRestriction();
    }

    public boolean isEligibleForVisit(){
        return consumer.isEligibleForVisit();
    }

    public boolean isEnrolled(){
        return consumer.isEnrolled();
    }

    Subscription getSubscription(){
        return consumer.getSubscription();
    }

    String getPhone(){
        return consumer.getPhone();
    }

    List<Consumer> getDependents(){
        return consumer.getDependents();
    }

    public boolean isDependent(){
        return consumer.isDependent();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(consumer, i);
    }
}
