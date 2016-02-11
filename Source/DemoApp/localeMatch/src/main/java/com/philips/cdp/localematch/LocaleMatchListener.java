package com.philips.cdp.localematch;

import com.philips.cdp.localematch.enums.LocaleMatchError;

/**
 * @author Deepthi Shivakumar
 * 
 */
public interface LocaleMatchListener {

	public void onLocaleMatchRefreshed(String locale);

	public void onErrorOccurredForLocaleMatch(LocaleMatchError error);

}
