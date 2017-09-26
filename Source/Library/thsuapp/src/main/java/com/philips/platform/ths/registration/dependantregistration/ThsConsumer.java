/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import com.americanwell.sdk.entity.State;
import com.philips.cdp.registration.ui.utils.Gender;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

public class ThsConsumer {
    private Date dob;
    private String firstname;
    private String lastname;
    private Gender gender;
    private State state;
    private String hsdpUUID;
    private String hsdoToken;
    private String email;
    private List<ThsConsumer> dependents;

    private ByteArrayInputStream profilePic;

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getHsdoToken() {
        return hsdoToken;
    }

    public void setHsdoToken(String hsdoToken) {
        this.hsdoToken = hsdoToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ThsConsumer> getDependents() {
        return dependents;
    }

    public void setDependents(List<ThsConsumer> dependents) {
        this.dependents = dependents;
    }

    public ByteArrayInputStream getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ByteArrayInputStream profilePic) {
        this.profilePic = profilePic;
    }
}
