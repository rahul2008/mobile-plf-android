
package com.philips.cl.di.reg.dao;

import java.util.ArrayList;

public class ConsumerArray {

	private ArrayList<ConsumerInterest> mConsumerInterestArray;

	private static ConsumerArray mConsumerArray = null;

	public ConsumerArray() {
		mConsumerInterestArray = new ArrayList<ConsumerInterest>();
	}

	public static ConsumerArray getInstance() {
		if (mConsumerArray == null) {
			mConsumerArray = new ConsumerArray();
		}
		return mConsumerArray;
	}

	public ArrayList<ConsumerInterest> getConsumerArraylist() {
		return mConsumerInterestArray;
	}

	public void setConsumerArraylist(ArrayList<ConsumerInterest> listConsumerInterest) {
		mConsumerInterestArray = listConsumerInterest;
	}

}
