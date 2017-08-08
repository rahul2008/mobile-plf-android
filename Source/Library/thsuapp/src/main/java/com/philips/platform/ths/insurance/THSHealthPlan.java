/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.HealthPlan;

import java.util.List;

public class THSHealthPlan {

    private List<HealthPlan> healthPlanList;

    public List<HealthPlan> getHealthPlanList() {
        return healthPlanList;
    }

    public void setHealthPlanList(List<HealthPlan> healthPlanList) {
        this.healthPlanList = healthPlanList;
    }


}
