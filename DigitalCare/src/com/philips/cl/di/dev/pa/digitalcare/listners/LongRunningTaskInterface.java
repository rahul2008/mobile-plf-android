package com.philips.cl.di.dev.pa.digitalcare.listners;

/*
 * LongRunningTaskInterface is interface for Aysnc task LongRunningTask class. Once 
 * data has been fetched(response received) from server then this will be invoked programmetically. 
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */

public interface LongRunningTaskInterface {
	void responseReceived(String str);
}
