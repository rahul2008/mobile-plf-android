package com.philips.cl.di.localematch;

import com.philips.cl.di.localematch.enums.LocaleMatchError;

/**
 * @author Deepthi Shivakumar
 * 
 */
public interface LocaleMatchListener {

	public void onLocaleMatchRefreshed(String locale);

	public void onErrorOccurredForLocaleMatch(LocaleMatchError error);

}
