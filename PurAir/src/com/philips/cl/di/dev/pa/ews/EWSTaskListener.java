package com.philips.cl.di.dev.pa.ews;

public interface EWSTaskListener {
	void onTaskCompleted(int responseCode, String response) ;
}
