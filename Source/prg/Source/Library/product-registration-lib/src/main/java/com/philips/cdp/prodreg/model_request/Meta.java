package com.philips.cdp.prodreg.model_request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Meta implements Serializable {

	@SerializedName("sendEmail")
	private boolean sendEmail;

	public void setSendEmail(boolean sendEmail){
		this.sendEmail = sendEmail;
	}

	public boolean isSendEmail(){
		return sendEmail;
	}

	@Override
 	public String toString(){
		return 
			"Meta{" + 
			"sendEmail = '" + sendEmail + '\'' + 
			"}";
		}
}