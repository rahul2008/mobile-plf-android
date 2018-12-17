/*
 * @author : Ritesh.jha@philips.com
 * @since: 16 Dec 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.models;


/**
 * CdlsBean is bean class for all CDLS related objects.
 */
public class CdlsResponseModel {
    private Boolean mSuccess = null;
    private CdlsEmailModel mEmail = null;
    private CdlsChatModel mChat = null;
    private CdlsPhoneModel mPhone = null;
    private CdlsErrorModel mError = null;

    public CdlsResponseModel(final boolean success, final CdlsPhoneModel phone, final CdlsChatModel chat,
                             final CdlsEmailModel email, final CdlsErrorModel error) {
        mSuccess = success;
        mPhone = phone;
        mChat = chat;
        mEmail = email;
        mError = error;
    }

    public boolean getSuccess() {
        return mSuccess;
    }

    public CdlsErrorModel getError() {
        return mError;
    }

    public CdlsEmailModel getEmail() {
        return mEmail;
    }

    public CdlsChatModel getChat() {
        return mChat;
    }

    public CdlsPhoneModel getPhone() {
        return mPhone;
    }
}
