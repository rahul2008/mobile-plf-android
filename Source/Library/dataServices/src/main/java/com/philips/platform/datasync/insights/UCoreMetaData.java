package com.philips.platform.datasync.insights;

/**
 * Created by philips on 3/7/17.
 */

public class UCoreMetaData {
    private int avg;
    private int min;
    private int max;

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAvg() {
        return avg;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
