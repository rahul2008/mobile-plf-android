package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.HealthPlan;

import java.util.List;

/**
 * Created by philips on 7/12/17.
 */

public class THSHealthPlan {

    List<HealthPlan> healthPlanList;
    public List<HealthPlan> getHealthPlanList() {
        return healthPlanList;
    }

    public void setHealthPlanList(List<HealthPlan> healthPlanList) {
        this.healthPlanList = healthPlanList;
    }


}
