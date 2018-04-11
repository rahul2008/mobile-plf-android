/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.os.Parcel;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.insurance.Subscription;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class THSConsumerWrapperTest {

    THSConsumerWrapper mThsConsumerWrapper;

    @Mock
    Consumer consumerMock;

    @Mock
    Parcel parcelMock;

    @Mock
    Subscription subscriptionMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsConsumerWrapper = new THSConsumerWrapper();
        mThsConsumerWrapper.setConsumer(consumerMock);
    }

    @Test
    public void getConsumer() throws Exception {
        mThsConsumerWrapper.setConsumer(consumerMock);
        final Consumer consumer = mThsConsumerWrapper.getConsumer();
        assertNotNull(consumer);
        assertThat(consumer).isInstanceOf(Consumer.class);
    }

    @Test
    public void getGender() throws Exception {
        String gender =  mThsConsumerWrapper.getGender();
        assertNull(gender);
    }

    @Test
    public void getAge() throws Exception {
        when(consumerMock.getAge()).thenReturn("24");
        final String age = mThsConsumerWrapper.getAge();
        assertNotNull(age);
        assertThat(age).isInstanceOf(String.class);
        assert age.equalsIgnoreCase("24");
    }

    @Test
    public void getFormularyRestriction() throws Exception {
        when(consumerMock.getFormularyRestriction()).thenReturn("Spoorti");
        final String formularyRestriction = mThsConsumerWrapper.getFormularyRestriction();
        assertNotNull(formularyRestriction);
        assertThat(formularyRestriction).isInstanceOf(String.class);
        assert formularyRestriction.equalsIgnoreCase("Spoorti");
    }

    @Test
    public void isEligibleForVisit() throws Exception {
        when(consumerMock.isEligibleForVisit()).thenReturn(true);
        final boolean eligibleForVisit = mThsConsumerWrapper.isEligibleForVisit();
        assertNotNull(eligibleForVisit);
        assertThat(eligibleForVisit).isInstanceOf(Boolean.class);
        assert eligibleForVisit == true;
    }

    @Test
    public void isEnrolled() throws Exception {
        when(consumerMock.isEnrolled()).thenReturn(true);
        final boolean enrolled = mThsConsumerWrapper.isEnrolled();
        assertNotNull(enrolled);
        assertThat(enrolled).isInstanceOf(Boolean.class);
        assert enrolled == true;
    }

    @Test
    public void getSubscription() throws Exception {
        when(consumerMock.getSubscription()).thenReturn(subscriptionMock);
        final Subscription subscription = mThsConsumerWrapper.getSubscription();
        assertNotNull(subscription);
        assertThat(subscription).isInstanceOf(Subscription.class);
    }

    @Test
    public void getPhone() throws Exception {
        when(consumerMock.getPhone()).thenReturn("123456789");
        final String phone = mThsConsumerWrapper.getPhone();
        assertNotNull(phone);
        assertThat(phone).isInstanceOf(String.class);
        assert phone.equalsIgnoreCase("123456789");
    }

    @Test
    public void getDependents() throws Exception {
        List list = new ArrayList();
        when(consumerMock.getDependents()).thenReturn(list);
        final List<Consumer> dependents = mThsConsumerWrapper.getDependents();
        assertNotNull(dependents);
        assertThat(dependents).isInstanceOf(List.class);
    }

    @Test
    public void isDependent() throws Exception {
        when(consumerMock.isDependent()).thenReturn(true);
        final boolean dependent = mThsConsumerWrapper.isDependent();
        assertNotNull(dependent);
        assertThat(dependent).isInstanceOf(Boolean.class);
        assert dependent == true;
    }

    @Test
    public void describeContents() throws Exception {
        when(consumerMock.describeContents()).thenReturn(1);
        final int i = mThsConsumerWrapper.describeContents();
    }

    @Test
    public void writeToParcel() throws Exception {
        mThsConsumerWrapper = new THSConsumerWrapper(parcelMock);
        mThsConsumerWrapper.writeToParcel(parcelMock,-1);
    }

}