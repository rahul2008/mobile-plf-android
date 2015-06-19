
package com.philips.cl.di.reg.coppa;

import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.Jump.CaptureApiResultHandler;
import com.philips.cl.di.reg.User;

public class CoppaExtension implements CoppaExtensionHandler {

	@Override
	public CoppaStatus getCoppaEmailConsentStatus() {
		return getCoppaStatusForConsent(CoppaConfiguration.getConsent());
	}

	@Override
	public void fetchCoppaEmailConsentStatus(final Context context,
	        final FetchCoppaEmailConsentStatusHandler handler) {

		if (Jump.getSignedInUser() == null) {
			handler.didCoppaStatusFectchingFailedWIthError(null);
			return;
		}
		Jump.fetchCaptureUserFromServer(new CaptureApiResultHandler() {

			@Override
			public void onSuccess(JSONObject response) {
				Jump.saveToDisk(context);
				User user = new User(context);
				user.buildCoppaConfiguration();
				handler.didCoppaStatusFetchingSucess(getCoppaEmailConsentStatus());
			}

			@Override
			public void onFailure(CaptureAPIError failureParam) {
				handler.didCoppaStatusFectchingFailedWIthError(failureParam);
			}
		});

	}

	@Override
	public void resendCoppaEmailConsentForUserEmail(String email) {

	}

	private CoppaStatus getCoppaStatusForConsent(Consent consent) {
		if (consent == null) {
			return null;
		}
		CoppaStatus coppaStatus = null;
		if (null != consent.getGiven()) {
			if (!consent.getGiven().equalsIgnoreCase(null)
			        && Boolean.parseBoolean(consent.getGiven())) {
				coppaStatus = CoppaStatus.kDICOPPAConsentGiven;
				if (null != consent.getConfirmationGiven()) {
					if (!consent.getConfirmationGiven().equalsIgnoreCase("null")) {
						if (Boolean.parseBoolean(consent.getConfirmationGiven())) {
							coppaStatus = CoppaStatus.kDICOPPAConfirmationGiven;
						} else {
							coppaStatus = CoppaStatus.kDICOPPAConfirmationNotGiven;
						}
					}
				} else if (null != consent.getConfirmationCommunicationSentAt()
				        && consent.getConfirmationCommunicationSentAt().length() > 0) {
					coppaStatus = coppaStatus.kDICOPPAConfirmationPending;
				}
			} else {
				coppaStatus = CoppaStatus.kDICOPPAConsentNotGiven;
			}
		} else {
			coppaStatus = CoppaStatus.kDICOPPAConsentPending;
		}
		return coppaStatus;
	}

}
