package com.philips.cl.di.dev.pa.digitalcare.util;

import java.util.Observable;

/*
 * FragmentObserver class is observer class for fragments. 
 * Fragments level changes wil be updated to Activities/Fragments.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 8 Dec 2015
 */
public class FragmentObserver extends Observable {
	private String mActionbaTitle = null;
	private int mOptionSelected = -1;

	/**
	 * @return the value
	 */
	public String getActionbarTitle() {
		return mActionbaTitle;
	}

	/**
	 * @return the option selected.
	 */
	public int getOptionSelected() {
		return mOptionSelected;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String name, int optionSelected) {
		this.mActionbaTitle = name;
		this.mOptionSelected = optionSelected;
		setChanged();
		notifyObservers();
	}
}