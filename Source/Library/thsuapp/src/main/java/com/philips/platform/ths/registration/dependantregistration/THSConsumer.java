/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.cdp.registration.ui.utils.Gender;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

public class THSConsumer {
    private Date dob;
    private String firstName;
    private String lastName;
    private Gender gender;
    private State state;
    private String hsdpUUID;
    private String hsdpToken;
    private String email;
    private String bloodPressureSystolic;
    private String bloodPressureDiastolic;
    private double temperature;
    private int weight;
    private List<THSConsumer> dependents;
    private Consumer consumer;

    private boolean isDependent = false;

    private ByteArrayInputStream profilePic;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getHsdpUUID() {
        return hsdpUUID;
    }

    public void setHsdpUUID(String hsdpUUID) {
        this.hsdpUUID = hsdpUUID;
    }

    public String getHsdpToken() {
        return hsdpToken;
    }

    public void setHsdpToken(String hsdpToken) {
        this.hsdpToken = hsdpToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<THSConsumer> getDependents() {
        return dependents;
    }

    public void setDependents(List<THSConsumer> dependents) {
        this.dependents = dependents;
    }

    public ByteArrayInputStream getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ByteArrayInputStream profilePic) {
        this.profilePic = profilePic;
    }

    public String getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }

    public void setBloodPressureSystolic(String bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }

    public String getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }

    public void setBloodPressureDiastolic(String bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }


    public boolean isDependent() {
        return isDependent;
    }

    public void setDependent(boolean dependent) {
        isDependent = dependent;
    }

}
