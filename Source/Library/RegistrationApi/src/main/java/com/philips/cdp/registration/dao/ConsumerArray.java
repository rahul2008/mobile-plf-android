/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;


import java.util.ArrayList;
import java.util.List;

/**
 * Consumer array for interested items.
 */
public class ConsumerArray {

    /* Consumer interest list */
    private List<ConsumerInterest> mConsumerInterestArray;

    /* Consumer array */
    private static ConsumerArray mConsumerArray = null;

    /**
     * Class Constructor
     */
    public ConsumerArray() {
        mConsumerInterestArray = new ArrayList<>();
    }

    /**
     * {@code getInstance} method to get instance of consumer array
     *
     * @return instance of ConsumerArray
     * {@link ConsumerArray}
     */
    public synchronized static ConsumerArray getInstance() {
        if (mConsumerArray == null) {
            synchronized (ConsumerArray.class) {
                if (mConsumerArray == null) {
                    mConsumerArray = new ConsumerArray();
                }
            }
        }
        return mConsumerArray;
    }

    /**
     * {@code getConsumerArraylist } method to get consumer array list
     * {@link com.philips.cdp.registration.dao.ConsumerInterest}
     *
     * @return
     */
    public List<ConsumerInterest> getConsumerArraylist() {
        return mConsumerInterestArray;
    }

    /**
     * {@code setConsumerArraylist} method to set consumer array list
     * {@link com.philips.cdp.registration.dao.ConsumerInterest}
     *
     * @param listConsumerInterest
     * @return the List of ConsumerInterest
     * @see List
     */
    public void setConsumerArraylist(List<ConsumerInterest> listConsumerInterest) {
        mConsumerInterestArray = listConsumerInterest;
    }

}
