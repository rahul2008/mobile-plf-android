package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Condition;

import java.util.List;

public class THSConditionsList {
    List<Condition> conditions;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
}
