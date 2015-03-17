package com.philips.cl.di.digitalcare.contactus;

/*
 * CdlsResponseCallback is interface for Aysnc task CdlsRequestAsyncTask class. Once 
 * data has been fetched(response received) from server then this will be invoked programmetically. 
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Dec 2014
 */

public interface CdlsResponseCallback {
	void onCdlsResponseReceived(String response);
}
