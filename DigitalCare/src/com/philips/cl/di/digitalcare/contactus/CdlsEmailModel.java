package com.philips.cl.di.digitalcare.contactus;
/**
 *	CdlsEmailModel is bean class for email.
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
		this.mContentPath = mContentPath;
	}

}
