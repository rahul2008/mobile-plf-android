package com.philips.cdp.di.iap.prx;

import java.util.Comparator;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PriorityCompare{
    public String asset = "";
    public int priorityStatus = 0;

    public PriorityCompare(String asset, int priorityStatus)
    {
        this.asset = asset;
        this.priorityStatus = priorityStatus;
    }
}
