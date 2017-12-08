/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataSteps extends SHNData {
    private final int steps;

    public SHNDataSteps(int steps) {

        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.Steps;
    }

    @Override
    public String toString() {
        return "Steps: " + getSteps();
    }
}
