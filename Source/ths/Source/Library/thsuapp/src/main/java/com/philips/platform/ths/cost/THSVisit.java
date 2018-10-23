/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import com.americanwell.sdk.entity.visit.Visit;

public class THSVisit {


    private Visit visit;
    double initialVisitCost;
    String couponCodeApplied;

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public double getInitialVisitCost() {
        return initialVisitCost;
    }

    public void setInitialVisitCost(double initialVisitCost) {
        this.initialVisitCost = initialVisitCost;
    }
    public String getCouponCodeApplied() {
        return couponCodeApplied;
    }

    public void setCouponCodeApplied(String couponCodeApplied) {
        this.couponCodeApplied = couponCodeApplied;
    }
}
