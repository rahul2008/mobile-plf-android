package com.cdp.horizontal.prx;

public class PRXDataBuilder {

	private String mServerInfo = null;
	private String mSectorCode = null;
	private String mLanguageCode = null;
	private String mCountryCode = null;
	private String mCatalogCode = null;
	private String mCtnCode = null;

	public void setServerInfo(String serverinfo) {
		this.mServerInfo = serverinfo;
	}

	public void setSectorCode(String sectorCode) {
		this.mSectorCode = sectorCode;
	}

	public void setCTN(String ctn) {
		this.mCtnCode = ctn;
	}

	public void setLanguageCode(String languageCode) {
		this.mLanguageCode = languageCode;
	}

	public void setCountryCode(String countryCode) {
		this.mCountryCode = countryCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.mCatalogCode = catalogCode;
	}

	public String getmServerInfo() {
		return mServerInfo;
	}

	public String getmSectorCode() {
		return mSectorCode;
	}

	public String getmLanguageCode() {
		return mLanguageCode;
	}

	public String getmCountryCode() {
		return mCountryCode;
	}

	public String getmCatalogCode() {
		return mCatalogCode;
	}

	public String getmCtnCode() {
		return mCtnCode;
	}

}
