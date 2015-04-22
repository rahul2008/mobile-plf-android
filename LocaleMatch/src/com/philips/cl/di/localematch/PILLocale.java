/**
 * 
 */
package com.philips.cl.di.localematch;

/**
 * @author Deepthi Shivakumar
 *
 */
public class PILLocale {

	private String mCountryCode;
	private String mLanguageCode;
	private String mLocaleCode;
	
	
	public void setCountryCode(String countrycode){
		mCountryCode = countrycode;
	}
	
	public void setLanguageCode(String languageCode){
		mLanguageCode = languageCode;
	}
	
	public void setLocaleCode(String localeCode){
		mLocaleCode = localeCode;
	}
	
	public String getCountrycode(){
		return mCountryCode;
	}
	
	public String getLanguageCode(){
		return mLanguageCode;
	}
	
	public String getLocaleCode(){
		return mLocaleCode;
	}
	
}
