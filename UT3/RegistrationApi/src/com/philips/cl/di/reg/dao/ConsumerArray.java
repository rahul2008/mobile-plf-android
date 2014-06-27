package com.philips.cl.di.reg.dao;

import java.util.ArrayList;

public class ConsumerArray {

	private ArrayList<ConsumerInterest> consumerInterestArray ;
	static ConsumerArray consumerArray =null ;
	
	public ConsumerArray() {
		consumerInterestArray = new ArrayList<ConsumerInterest>();
	}

	public static ConsumerArray getInstance() {
		if(consumerArray==null)
		{
			consumerArray = new ConsumerArray();
		}
		return consumerArray;
	}
	
	public ArrayList<ConsumerInterest> getConsumerArraylist() {
		return consumerInterestArray;
	}
	
	public void setConsumerArraylist(ArrayList<ConsumerInterest> alconsumerInterest) {
		consumerInterestArray = alconsumerInterest;
	}

}
