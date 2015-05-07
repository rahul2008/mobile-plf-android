package com.pins.philips.shinelib.datatypes;

/**
 * Created by 310188215 on 07/05/15.
 */
public class SHNLogItemDataTestResult implements SHNLogItemData {
    private final long testId;
    private final int expected;
    private final int actual;

    public SHNLogItemDataTestResult(long testId, int expected, int actual) {
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
}
