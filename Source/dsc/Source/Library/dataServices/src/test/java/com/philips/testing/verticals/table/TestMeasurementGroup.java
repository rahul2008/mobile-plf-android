package com.philips.testing.verticals.table;

import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 310218660 on 11/17/2016.
 */

public class TestMeasurementGroup implements MeasurementGroup, Serializable {
    private static final long serialVersionUID = 11L;

    private int id;

    List<TestMeasurement> testMeasurements = new ArrayList<>();

    private TestMoment testMoment;

    private TestMeasurementGroup testMeasurementGroup;

    List<TestMeasurementGroupDetail> testMeasurementGroupDetails = new ArrayList<>();

    List<TestMeasurementGroup> testMeasurementGroups = new ArrayList<>();

    public TestMeasurementGroup(TestMeasurementGroup testMeasurementGroup) {
        this.testMeasurementGroup = testMeasurementGroup;
        this.id = -1;
    }

    public TestMeasurementGroup(TestMoment testMoment) {
        this.testMoment = testMoment;
        this.id = -1;
    }

    @Override
    public Collection<? extends TestMeasurement> getMeasurements() {
        return testMeasurements;
    }

    @Override
    public Collection<? extends TestMeasurementGroup> getMeasurementGroups() {
        return testMeasurementGroups;
    }

    @Override
    public void addMeasurement(Measurement measurement) {
        testMeasurements.add((TestMeasurement) measurement);
    }

    @Override
    public void addMeasurementGroup(MeasurementGroup measurementGroup) {
        testMeasurementGroups.add((TestMeasurementGroup) measurementGroup);
    }

    @Override
    public Collection<? extends TestMeasurementGroupDetail> getMeasurementGroupDetails() {
        return testMeasurementGroupDetails;
    }

    @Override
    public void addMeasurementGroupDetail(final MeasurementGroupDetail measurementGroupDetail) {
        testMeasurementGroupDetails.add((TestMeasurementGroupDetail) measurementGroupDetail);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setMeasurementGroups(Collection<? extends MeasurementGroup> groups) {

    }
}
