package com.philips.cl.di.dev.pa.digitalcare.util;

import java.util.Observable;

/*
 * FragmentObserver class is observer class for fragments. 
 * Fragments level changes wil be updated to Activities/Fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 8 Dec 2014
 */
public class FragmentObserver extends Observable {
	private String mName = null;

	/**
	 * @return the value
	 */
	public String getValue() {
		return mName;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String name) {
		this.mName = name;
		setChanged();
		notifyObservers();
	}
}