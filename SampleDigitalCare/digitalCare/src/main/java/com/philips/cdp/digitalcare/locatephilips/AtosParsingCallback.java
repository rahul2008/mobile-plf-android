package com.philips.cdp.digitalcare.locatephilips;


/**
 * AtosParsingCallback is interface, used when parseing is completed.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 10 June 2015
 */

public interface AtosParsingCallback {
	void onAtosParsingComplete(AtosResponseModel response);
}
