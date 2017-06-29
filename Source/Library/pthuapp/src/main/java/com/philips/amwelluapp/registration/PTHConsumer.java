package com.philips.amwelluapp.registration;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.Gender;
import com.americanwell.sdk.entity.insurance.Subscription;

import java.io.Serializable;
import java.util.List;

public class PTHConsumer implements Serializable{
    Consumer consumer;

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
}
