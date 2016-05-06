
package com.philips.cdp.registration.dao;

import java.util.ArrayList;
import java.util.List;

public class ConsumerArray {

    private List<ConsumerInterest> mConsumerInterestArray;

    private static ConsumerArray mConsumerArray = null;

    public ConsumerArray() {
        mConsumerInterestArray = new ArrayList<>();
    }

    public static ConsumerArray getInstance() {
        if (mConsumerArray == null) {
            synchronized (ConsumerArray.class) {
                if (mConsumerArray == null) {
                    mConsumerArray = new ConsumerArray();
                }
            }
        }
        return mConsumerArray;
    }

    public List<ConsumerInterest> getConsumerArraylist() {
        return mConsumerInterestArray;
    }

    public void setConsumerArraylist(ArrayList<ConsumerInterest> listConsumerInterest) {
        mConsumerInterestArray = listConsumerInterest;
    }

}
