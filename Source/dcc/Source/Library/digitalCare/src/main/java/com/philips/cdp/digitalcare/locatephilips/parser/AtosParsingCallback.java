package com.philips.cdp.digitalcare.locatephilips.parser;


import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;

/**
 * AtosParsingCallback is interface, used when parseing is completed.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 10 June 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

public interface AtosParsingCallback {
	void onAtosParsingComplete(AtosResponseModel response);
}
