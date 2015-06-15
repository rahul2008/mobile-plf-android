package com.philips.pins.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
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
        return SHNDataType.TestResult;
    }
}
