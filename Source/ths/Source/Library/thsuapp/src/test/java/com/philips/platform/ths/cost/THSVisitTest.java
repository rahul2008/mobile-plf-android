/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import com.americanwell.sdk.entity.visit.Visit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class THSVisitTest {

    THSVisit mThsVisit;

    @Mock
    Visit visitMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsVisit = new THSVisit();
    }

    @Test
    public void getVisit() throws Exception {
        mThsVisit.setVisit(visitMock);
        final Visit visit = mThsVisit.getVisit();
        assertNotNull(visit);
        assertThat(visit).isInstanceOf(Visit.class);
    }

    @Test
    public void getInitialVisitCost() throws Exception {
        mThsVisit.setInitialVisitCost(12.99);
        final double initialVisitCost = mThsVisit.getInitialVisitCost();
        assert initialVisitCost == 12.99;
    }

    @Test
    public void getCouponCodeApplied() throws Exception {
        mThsVisit.setCouponCodeApplied("123");
        final String couponCodeApplied = mThsVisit.getCouponCodeApplied();
        assertNotNull(couponCodeApplied);
        assert couponCodeApplied.equalsIgnoreCase("123");
    }
}