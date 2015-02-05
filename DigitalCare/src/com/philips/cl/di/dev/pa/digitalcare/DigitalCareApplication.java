package com.philips.cl.di.dev.pa.digitalcare;

import com.philips.cl.di.dev.pa.digitalcare.util.FragmentObserver;

/*
 *	DigitalCareApplication is Application class for DigitalCare app. 
 *  Here we can maintain the instances at digital care app level.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @creation Date : 5 Dec 2015
 */
public class DigitalCareApplication {

	private static FragmentObserver mFragmentObserver = null;

	public static FragmentObserver getFragmentObserverInstance() {
		if (mFragmentObserver == null) {
			mFragmentObserver = new FragmentObserver();
		}
		return mFragmentObserver;
	}
}
