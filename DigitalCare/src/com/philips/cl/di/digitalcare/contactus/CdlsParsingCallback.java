package com.philips.cl.di.digitalcare.contactus;


/**
 * CdlsParsingCallback is interface, used when cdls parsing is completed.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 10 June 2015
 */

public interface CdlsParsingCallback {
	void onParsingDone(CdlsResponseModel response);
}
