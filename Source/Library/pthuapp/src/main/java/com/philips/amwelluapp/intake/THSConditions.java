package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.health.Condition;

import java.util.List;

public class THSConditions {
    List<Condition> conditions;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
}
