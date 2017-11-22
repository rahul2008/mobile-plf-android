/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.cdp.registration.ui.utils.Gender;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class THSConsumerTest {
    THSConsumer thsConsumer;

    @Mock
    Consumer consumerMock;

    @Mock
    Date dateMock;

    @Mock
    ByteArrayInputStream byteArrayInputStreamMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsConsumer = new THSConsumer();
    }

    @After
    public void tearDown() throws Exception {
        thsConsumer = null;
    }

    @Test
    public void getDob() throws Exception {
        thsConsumer.setDob(dateMock);
        final Date dob = thsConsumer.getDob();
        assertNotNull(dob);
        assertThat(dob).isInstanceOf(Date.class);
    }

    @Test
    public void getFirstName() throws Exception {
        thsConsumer.setFirstName("Spoorti");
        final String firstName = thsConsumer.getFirstName();
        assertNotNull(firstName);
        assertThat(firstName).isInstanceOf(String.class);
    }

    @Test
    public void getLastName() throws Exception {
        thsConsumer.setLastName("Hallur");
        final String lastName = thsConsumer.getLastName();
        assertNotNull(lastName);
        assertThat(lastName).isInstanceOf(String.class);
    }

    @Test
    public void getGender() throws Exception {
        thsConsumer.setGender(Gender.FEMALE);
        final Gender gender = thsConsumer.getGender();
        assertNotNull(gender);
        assertThat(gender).isInstanceOf(Gender.class);
    }

    @Test
    public void getState() throws Exception {
        State state = new State() {
            @Override
            public String getCode() {
                return null;
            }

            @Override
            public boolean isLegalResidence() {
                return false;
            }

            @Nullable
            @Override
            public Country getCountry() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {

            }
        };
        thsConsumer.setState(state);
        final State state1 = thsConsumer.getState();
        assertNotNull(state1);
        assertThat(state1).isInstanceOf(State.class);
    }


    @Test
    public void getHsdpUUID() throws Exception {
        thsConsumer.setHsdpUUID("1234");
        final String hsdpUUID = thsConsumer.getHsdpUUID();
        assertNotNull(hsdpUUID);
        assertThat(hsdpUUID).isInstanceOf(String.class);
    }

    @Test
    public void getHsdoToken() throws Exception {
        thsConsumer.setHsdpToken("1234");
        final String hsdoToken = thsConsumer.getHsdpToken();
        assertNotNull(hsdoToken);
        assertThat(hsdoToken).isInstanceOf(String.class);
    }

    @Test
    public void getEmail() throws Exception {
        thsConsumer.setEmail("spoorti@g.com");
        final String email = thsConsumer.getEmail();
        assertNotNull(email);
        assertThat(email).isInstanceOf(String.class);
    }

    @Test
    public void getDependents() throws Exception {
        List<THSConsumer> dependent = new ArrayList<>();
        THSConsumer thsConsumer1= new THSConsumer();
        dependent.add(thsConsumer1);
        thsConsumer.setDependents(dependent);
        final List<THSConsumer> dependents = thsConsumer.getDependents();
        assertNotNull(dependents);
        assertThat(dependents).isInstanceOf(List.class);
    }

    @Test
    public void getProfilePic() throws Exception {
        thsConsumer.setProfilePic(byteArrayInputStreamMock);
        final ByteArrayInputStream profilePic = thsConsumer.getProfilePic();
        assertNotNull(profilePic);
        assertThat(profilePic).isInstanceOf(ByteArrayInputStream.class);
    }

    @Test
    public void getBloodPressureSystolic() throws Exception {
        thsConsumer.setBloodPressureSystolic("90");
        final String bloodPressureSystolic = thsConsumer.getBloodPressureSystolic();
        assertNotNull(bloodPressureSystolic);
        assertThat(bloodPressureSystolic).isInstanceOf(String.class);
    }

    @Test
    public void getBloodPressureDiastolic() throws Exception {
        thsConsumer.setBloodPressureDiastolic("120");
        final String bloodPressureDiastolic = thsConsumer.getBloodPressureDiastolic();
        assertNotNull(bloodPressureDiastolic);
        assertThat(bloodPressureDiastolic).isInstanceOf(String.class);
    }

    @Test
    public void getTemperature() throws Exception {
        thsConsumer.setTemperature(90);
        final double temperature = thsConsumer.getTemperature();
        assertNotNull(temperature);
        assertThat(temperature).isInstanceOf(Double.class);
    }

    @Test
    public void getWeight() throws Exception {
        thsConsumer.setWeight(78);
        final double weight = thsConsumer.getWeight();
        assertNotNull(weight);
        assertThat(weight).isInstanceOf(Double.class);
    }

    @Test
    public void getConsumer() throws Exception {
        thsConsumer.setConsumer(consumerMock);
        final Consumer consumer = thsConsumer.getConsumer();
        assertNotNull(consumer);
        assertThat(consumer).isInstanceOf(Consumer.class);
    }

    @Test
    public void isDependent() throws Exception {
        thsConsumer.setDependent(true);
        boolean dependent = thsConsumer.isDependent();
        assertNotNull(dependent);
        assertThat(dependent).isInstanceOf(Boolean.class);
        assert dependent == true;
    }

}