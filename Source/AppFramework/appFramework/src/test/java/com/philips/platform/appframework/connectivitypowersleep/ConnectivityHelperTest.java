/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentType;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConnectivityHelperTest {

    private ConnectivityHelper connectivityHelper;

    @Mock
    private static DataServicesManager dataServicesManager;

    @Mock
    private Moment moment;

    @Mock
    private MeasurementGroup measurementGroupInside;

    @Mock
    private MeasurementGroup measurementGroup;

    @Before
    public void setUp() {
        connectivityHelper = new ConnectivityHelperMock();
    }

    @Test
    public void calculateDeepSleepScore() throws Exception {
        Assert.assertEquals(95, connectivityHelper.calculateDeepSleepScore(6950712));
    }

    @Test
    public void createMomentTest() {
        when(dataServicesManager.createMoment(any(String.class))).thenReturn(moment);
        when(dataServicesManager.createMeasurementGroup(any(Moment.class))).thenReturn(measurementGroup);
        when(dataServicesManager.createMeasurementGroup(any(MeasurementGroup.class))).thenReturn(measurementGroupInside);
        Moment moment = connectivityHelper.createMoment("87", "87312923", "84723947", new DateTime(), "");
        assertNotNull(moment);
    }

    @Test
    public void getSummaryInfoFromNullMomentTest() {
        assertNull(connectivityHelper.getSummaryInfoFromMoment(null));
    }

    @After
    public void tearDown() {
        connectivityHelper = null;
        dataServicesManager = null;
        moment = null;
        measurementGroupInside = null;
        measurementGroup = null;
    }

    static class ConnectivityHelperMock extends ConnectivityHelper {
        @Override
        protected DataServicesManager getDataServicesManagerInstance() {
            return dataServicesManager;
        }
    }

}