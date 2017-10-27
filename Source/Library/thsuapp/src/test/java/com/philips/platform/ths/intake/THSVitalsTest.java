package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.visit.Vitals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSVitalsTest {

    THSVitals thsVitals;

    @Mock
    Vitals vitalsMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsVitals = new THSVitals();
        thsVitals.setVitals(vitalsMock);
    }

    @Test
    public void getVitals() throws Exception {
        Vitals vitals = thsVitals.getVitals();
        assertNotNull(vitals);
        assertThat(vitals).isInstanceOf(Vitals.class);
    }

    @Test
    public void getSystolic() throws Exception {
        when(vitalsMock.getSystolic()).thenReturn(10);
        Integer systolic = thsVitals.getSystolic();
        assert systolic == 10;
    }

    @Test
    public void getDiastolic() throws Exception {
        when(vitalsMock.getDiastolic()).thenReturn(10);
        Integer diastolic = thsVitals.getDiastolic();
        assert diastolic == 10;
    }

    @Test
    public void getTemperature() throws Exception {
        when(vitalsMock.getTemperature()).thenReturn(30.0);
        Double temperature = thsVitals.getTemperature();
        assert temperature == 30.0;
    }

    @Test
    public void getWeight() throws Exception {
        when(vitalsMock.getWeight()).thenReturn(200);
        Integer weight = thsVitals.getWeight();
        assert weight == 200;
    }

    @Test
    public void setSystolic() throws Exception {
        thsVitals.setSystolic(10);
        verify(vitalsMock).setSystolic(10);
    }

    @Test
    public void setDiastolic() throws Exception {
        thsVitals.setDiastolic(10);
        verify(vitalsMock).setDiastolic(10);
    }

    @Test
    public void setTemperature() throws Exception {
        thsVitals.setTemperature(30.0);
        verify(vitalsMock).setTemperature(30.0);
    }

    @Test
    public void setWeight() throws Exception {
        thsVitals.setWeight(200);
        verify(vitalsMock).setWeight(200);
    }

    @Test
    public void isEmpty() throws Exception {
        when(vitalsMock.isEmpty()).thenReturn(true);
        boolean empty = thsVitals.isEmpty();
        assertTrue(empty);
    }

}