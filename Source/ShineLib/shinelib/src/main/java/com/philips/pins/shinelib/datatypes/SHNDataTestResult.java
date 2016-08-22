/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

public class SHNDataTestResult extends SHNData {
    private final long testId;
    private final int expected;
    private final int actual;

    public SHNDataTestResult(long testId, int expected, int actual) {
        this.testId = testId;
        this.expected = expected;
        this.actual = actual;
    }

    public long getTestId() {
        return testId;
    }

    public int getExpected() {
        return expected;
    }

    public int getActual() {
        return actual;
    }

    @Override
    public SHNDataType getSHNDataType() {
        return SHNDataType.TestResultMoonshine;
    }

    @Override
    public String toString() {
        return "TestResult testId: " + getTestId() + " expected: " + getExpected() + " actual: " + getActual();
    }
}
