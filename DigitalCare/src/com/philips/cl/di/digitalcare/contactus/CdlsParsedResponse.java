package com.philips.cl.di.digitalcare.contactus;

/*
 *	CdlsBean is bean class for all CDLS related objects.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since: 16 Dec 2014
 */

public class CdlsParsedResponse {
	private Boolean mSuccess = null;
	private CdlsEmail mEmail = null;
	private CdlsChat mChat = null;
	private CdlsPhone mPhone = null;
	private CdlsError mError = null;

	public CdlsParsedResponse(boolean success, CdlsPhone phone, CdlsChat chat,
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
