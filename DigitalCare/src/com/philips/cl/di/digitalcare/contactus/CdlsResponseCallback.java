package com.philips.cl.di.digitalcare.contactus;

/*
 * CdlsResponseCallback is interface for Aysnc task CdlsRequestAsyncTask class. Once 
 * data has been fetched(response received) from server then this will be invoked programmetically. 
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */

public interface CdlsResponseCallback {
	void onResponseReceived(String response);
}
