package com.philips.cdp.digitalcare.contactus;

/**
 * CdlsEmailModel is bean class for email.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since: 16 Dec 2014
 */
public class CdlsEmailModel {

	private String mLabel = null;
	private String mContentPath = null;

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String mLabel) {
		this.mLabel = mLabel;
	}

	public String getContentPath() {
		return mContentPath;
	}

	public void setContentPath(String mContentPath) {
		if (validateEmailLink(mContentPath))
			this.mContentPath = mContentPath;
	}

	private boolean validateEmailLink(String response) {

		if (response.startsWith("https://"))
			return true;
		else if (!(response.startsWith("@")))
			return true;
		else if (!(response.endsWith("@")))
			return true;
		else if (response.startsWith("http://"))
			return true;

		return false;
	}

}
