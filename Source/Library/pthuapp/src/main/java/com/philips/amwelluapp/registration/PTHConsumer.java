package com.philips.amwelluapp.registration;

import android.os.Parcel;
import android.os.Parcelable;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.insurance.Subscription;

import java.util.List;

public class PTHConsumer implements Parcelable{
    Consumer consumer;

    public PTHConsumer(){

    }

    protected PTHConsumer(Parcel in) {
        consumer = in.readParcelable(Consumer.class.getClassLoader());
    }

    public static final Creator<PTHConsumer> CREATOR = new Creator<PTHConsumer>() {
        @Override
        public PTHConsumer createFromParcel(Parcel in) {
            return new PTHConsumer(in);
        }

        @Override
        public PTHConsumer[] newArray(int size) {
            return new PTHConsumer[size];
        }
    };

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public Gender getGender(){
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
