package com.philips.cl.di.digitalcare.locatephilips;


/**
 * ParsingCompletedCallback is interface, used when parseing is completed.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 10 June 2015
 */

public interface ParsingCompletedCallback {
	void onParsingDone(AtosResponseModel response);
}
