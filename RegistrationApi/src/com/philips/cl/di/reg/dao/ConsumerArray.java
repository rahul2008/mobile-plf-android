package com.philips.cl.di.reg.dao;

import java.util.ArrayList;

public class ConsumerArray {

	private static ArrayList<ConsumerInterest> consumerInterestArray = new ArrayList<ConsumerInterest>();
	static ConsumerArray consumerArray = new ConsumerArray();
	
	public ConsumerArray() {
        
	}

	public static ConsumerArray getInstance() {
		return consumerArray;
	}
	
	public ArrayList<ConsumerInterest> getConsumerArraylist() {
		return consumerInterestArray;
	}
	
	public void setConsumerArraylist(ConsumerInterest consumerInterest) {
		consumerInterestArray.add(consumerInterest);
	}

}
