package com.philips.cdp.horizontal;

public class RequestManager {

	private static RequestManager mRequestManager = null;

	private String mServerInfo = "http://www.philips.com";
	private String mSectorCode = null;
	private String mLanguageCode = null;
	private String mCountryCode = null;
	private String mCatalogCode = null;
	private String mCtnCode = null;
	
	
	/**
	 * It is the entrypoint to the Java/Android PRX Component. </br>
	 * It is the singlotong class, So make sure to set the method values to fetch the accurate data from
	 * the Philips PRX IT System.
	 * @return
	 */
	public static RequestManager getInstance() {
		if (mRequestManager == null)
			mRequestManager = new RequestManager();
		return mRequestManager;
	}
	
	
	/**
	 * Set the <b>SERVER_INFO</b> of the Philips portal if you have data to get very accurate data, 
	 * If you don't have the default value will be "http://www.philips.com" (This data will also give the
	 * accurate data).
	 * @param serverinfo
	 */
	public void setServerInfo(String serverinfo)
	{
		this.mServerInfo = serverinfo;
	}
	
	/**
	 * Pass the Sector Code which gets from the LocaleMatch SDK.
	 * @param sectorCode
	 */
	public void setSectorCode(String sectorCode )
	{
		this.mSectorCode = sectorCode;
	}
	
	/**
	 * Pass the CTN code of the product for which you need to the information from the PRX IT System.
	 * @param ctn
	 */
	public void setCTN(String ctn )
	{
		this.mCtnCode = ctn;
	}
	
	/**
	 * Pass the language code. 
	 * @param languageCode
	 */
	public void setLanguageCode(String languageCode)
	{
		this.mLanguageCode = languageCode;
	}
	
	/**
	 * Pass the Country code.
	 * @param countryCode
	 */
	public void setCountryCode(String countryCode)
	{
		this.mCountryCode = countryCode;
	}
	
	
	/**
	 * Pass the catalogCode to fetch the data.
	 * @param catalogCode
	 */
	public void setCatalogCode(String catalogCode)
	{
		this.mCatalogCode = catalogCode;
	}
}
