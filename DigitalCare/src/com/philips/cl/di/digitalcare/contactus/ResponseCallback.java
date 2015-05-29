package com.philips.cl.di.digitalcare.contactus;

/**
 * ResponseCallback is interface for Aysnc task RequestThread class. Once 
 * data has been fetched(response received) from server then this will be invoked programmetically. 
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Dec 2014
 */

public interface ResponseCallback {
	void onResponseReceived(String response);
}
