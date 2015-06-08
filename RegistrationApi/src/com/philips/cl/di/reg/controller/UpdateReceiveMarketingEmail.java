
package com.philips.cl.di.reg.controller;

import org.json.JSONException;

import android.content.Context;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cl.di.reg.handlers.UpdateReceiveMarketingEmailHandler;

public class UpdateReceiveMarketingEmail implements CaptureApiRequestCallback {

	private UpdateReceiveMarketingEmailHandler mUpdateReceiveMarketingEmailHandler;

	private Context mContext;

	private boolean mReceiveMarketingEmail;

	private String USER_RECEIVE_MARKETING_EMAIL = "receiveMarketingEmail";

	public UpdateReceiveMarketingEmail(
	        UpdateReceiveMarketingEmailHandler updateReceiveMarketingEmailHandler, Context context,
	        boolean receiveMarketingEmail) {
		mUpdateReceiveMarketingEmailHandler = updateReceiveMarketingEmailHandler;
		mContext = context;
		mReceiveMarketingEmail = receiveMarketingEmail;
	}

	public void onSuccess() {
		CaptureRecord capture = CaptureRecord.loadFromDisk(mContext);

		try {
			capture.put(USER_RECEIVE_MARKETING_EMAIL, mReceiveMarketingEmail);
			capture.saveToDisk(mContext);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mUpdateReceiveMarketingEmailHandler.onUpdateReceiveMarketingEmailSuccess();

	}

	public void onFailure(CaptureApiError error) {
		mUpdateReceiveMarketingEmailHandler
		        .onUpdateReceiveMarketingEmailFailedWithError(error.code);
	}
}
