package com.philips.cl.di.dev.digitalcare.bean;

/*
 *	CdlsBean is bean class for all CDLS related objects.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */

public class CdlsBean {

	private Boolean mSuccess = null;
	private CdlsEmail mEmail = null;
	private CdlsChat mChat = null;
	private CdlsPhone mPhone = null;
	private CdlsError mError = null;

	public CdlsBean(boolean success, CdlsPhone phone, CdlsChat chat,
			CdlsEmail email, CdlsError error) {
		mSuccess = success;
		mPhone = phone;
		mChat = chat;
		mEmail = email;
		mError = error;
	}

	public boolean getSuccess() {
		return mSuccess;
	}

	public CdlsError getError() {
		return mError;
	}

	public CdlsEmail getEmail() {
		return mEmail;
	}

	public CdlsChat getChat() {
		return mChat;
	}

	public CdlsPhone getPhone() {
		return mPhone;
	}
}
