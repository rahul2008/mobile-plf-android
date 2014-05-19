package com.philips.cl.di.reg;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.capture.Capture.InvalidApidChangeException;
import com.philips.cl.di.reg.controller.ContinueSocialProviderLogin;
import com.philips.cl.di.reg.controller.ForgotPassword;
import com.philips.cl.di.reg.controller.LoginSocialProvider;
import com.philips.cl.di.reg.controller.LoginTraditional;
import com.philips.cl.di.reg.controller.RefreshLoginSession;
import com.philips.cl.di.reg.controller.RegisterTraditional;
import com.philips.cl.di.reg.controller.ResendVerificationEmail;
import com.philips.cl.di.reg.dao.ConsumerArray;
import com.philips.cl.di.reg.dao.ConsumerInterest;
import com.philips.cl.di.reg.dao.DIUserProfile;
import com.philips.cl.di.reg.errormapping.Error;
import com.philips.cl.di.reg.handlers.ForgotPasswordHandler;
import com.philips.cl.di.reg.handlers.RefreshLoginSessionHandler;
import com.philips.cl.di.reg.handlers.ResendVerificationEmailHandler;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;

public class User {
	private String email, givenName,password,displayName;
	private boolean olderThanAgeLimit, receiveMarketingEmails;
	private Context context;
	private JSONObject consumerInterestObject;
    private	JSONArray consumerInterestArray;

	public User(Context context) {
		this.context = context;
	}

	// For Traditional SignIn
	public void loginUsingTraditional(String emailAddress, String password,
			TraditionalLoginHandler traditionalLoginHandler, Context context) {

		if (emailAddress != null) {
			LoginTraditional loginTraditionalResultHandler = new LoginTraditional(
					traditionalLoginHandler, context, emailAddress, password);
			Jump.performTraditionalSignIn(emailAddress, password,loginTraditionalResultHandler, null);
		} else {
			traditionalLoginHandler.onLoginFailedWithError(Error.INVALID_PARAM.geterrorList());
		}
	}

	// For Social SignIn Using Provider

	public void loginUsingSocialProvider(Activity activity,String providerName, 
			SocialProviderLoginHandler socialLoginHandler,Context getApplicationContext) {
		if (providerName != null) {
			LoginSocialProvider loginSocialResultHandler = new LoginSocialProvider(socialLoginHandler, getApplicationContext);
			Jump.showSignInDialog(activity, providerName,loginSocialResultHandler, null);
		} else {
			socialLoginHandler.onLoginFailedWithError(Error.INVALID_PARAM.geterrorList());
		}
	}

	// For Traditional Registration

	public void registerNewUserUsingTraditional(ArrayList<DIUserProfile> profile,
			TraditionalRegistrationHandler traditionalRegisterHandler,Context context) {

		if (profile != null) {
			
			for (DIUserProfile diUserProfile : profile) {
				email = diUserProfile.getEmail();
				givenName = diUserProfile.getGivenName();
				password = diUserProfile.getPassword();
				olderThanAgeLimit = diUserProfile.getOlderThanAgeLimit();
				receiveMarketingEmails = diUserProfile.getReceiveMarketingEmail();
			}
			JSONObject newUser = new JSONObject();
			try {
				newUser.put("email", email)
				        .put("givenName", givenName)
						.put("password", password)
						.put("olderThanAgeLimit", olderThanAgeLimit)
						.put("receiveMarketingEmail", receiveMarketingEmails);
			} catch (JSONException e) {
				throw new RuntimeException("Unexpected", e);
			}
			RegisterTraditional traditionalRegisterResultHandler = new RegisterTraditional(traditionalRegisterHandler, context);
			Jump.registerNewUser(newUser, null,traditionalRegisterResultHandler);
		} else {
			traditionalRegisterHandler.onRegisterFailedWithFailure(Error.INVALID_PARAM.geterrorList());
		}
	}

	// For Forgot password

	public void forgotPassword(String emailAddress,ForgotPasswordHandler forgotpasswordhandler) {

		if (emailAddress != null) {
			ForgotPassword forgotPasswordHandler = new ForgotPassword(forgotpasswordhandler);
			Jump.performForgotPassword(emailAddress, forgotPasswordHandler);
		} else {
			forgotpasswordhandler.onSendForgotPasswordFailedWithError(Error.INVALID_PARAM.geterrorList());
		}
	}

	// For Refresh login Session
	public void refreshLoginSession(RefreshLoginSessionHandler refreshLoginSessionHandler) {

		if (Jump.getSignedInUser() == null) {
			return;
		}
		RefreshLoginSession refreshLoginhandler = new RefreshLoginSession(refreshLoginSessionHandler);
		Jump.getSignedInUser().refreshAccessToken(refreshLoginhandler);
	}

	// For Resend verification email

	public void resendVerificationMail(String emailAddress,ResendVerificationEmailHandler resendVerificationEmail) {

		if (emailAddress != null) {
			ResendVerificationEmail resendVerificationEmailHandler = new ResendVerificationEmail(resendVerificationEmail);
			Jump.resendEmailVerification(emailAddress,resendVerificationEmailHandler);
		} else {
			resendVerificationEmail.onResendVerificationEmailFailedWithError(Error.INVALID_PARAM.geterrorList());
		}
	}

	public void MergeToTraditionalAccount(String emailAddress, String password,
			String mergeToken, TraditionalLoginHandler traditionalLoginHandler,Context context) {

		if (emailAddress != null) {
			LoginTraditional loginTraditionalResultHandler = new LoginTraditional(traditionalLoginHandler, context, emailAddress, password);
			Jump.performTraditionalSignIn(emailAddress, password,loginTraditionalResultHandler, mergeToken);

		} else {
			traditionalLoginHandler.onLoginFailedWithError(Error.INVALID_PARAM.geterrorList());
		}
	}

	// For Two Step registration
	public void ContinueSocialProviderLogin(ArrayList<DIUserProfile> profile,
			SocialProviderLoginHandler socialProviderLoginHandler,
			String socialRegistrationToken, Context context) {

		if (profile != null) {
			for (DIUserProfile diUserProfile : profile) {
				email = diUserProfile.getEmail();
				givenName = diUserProfile.getGivenName();
				password = diUserProfile.getPassword();
				displayName = diUserProfile.getDisplayName();
				olderThanAgeLimit = diUserProfile.getOlderThanAgeLimit();
				receiveMarketingEmails = diUserProfile.getReceiveMarketingEmail();
			}
			JSONObject newUser = new JSONObject();
			try {
				newUser.put("email", email).put("givenName", givenName)
						.put("password", password)
						.put("displayName", displayName)
						.put("olderThanAgeLimit", olderThanAgeLimit)
						.put("receiveMarketingEmail", receiveMarketingEmails);

			} catch (JSONException e) {
				throw new RuntimeException("Unexpected", e);
			}

			ContinueSocialProviderLogin continueSocialProviderLogin = new ContinueSocialProviderLogin(
					socialProviderLoginHandler, context);
			Jump.registerNewUser(newUser, socialRegistrationToken,continueSocialProviderLogin);
		} else {
			socialProviderLoginHandler
					.onContinueSocialProviderLoginFailure(Error.INVALID_PARAM
							.geterrorList());
		}
	}

	// For getting values from Captured and Saved Json object
	public DIUserProfile getUserInstance(Context mContext) {
		DIUserProfile diUserProfile = new DIUserProfile();
		CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);

		if (captured == null)
			return null;
		try {

			JSONObject mObject = new JSONObject(captured.toString());
			diUserProfile.setEmail(mObject.getString("email"));
			diUserProfile.setGivenName(mObject.getString("givenName"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return diUserProfile;
	}
	
	// For updating consumer interests
	public void ReplaceConsumerInterests(Context mContext,
			ConsumerArray consumerArray) {

		CaptureRecord captured = CaptureRecord.loadFromDisk(mContext);
		consumerInterestArray = new JSONArray();
		ConsumerArray consumer = ConsumerArray.getInstance();

		if (consumer != null) {
			for (ConsumerInterest diConsumerInterest : consumer
					.getConsumerArraylist()) {
				try {

					consumerInterestObject = new JSONObject();
					consumerInterestObject.put("campaignName",diConsumerInterest.getCampaignName());
					consumerInterestObject.put("subjectArea",diConsumerInterest.getSubjectArea());
					consumerInterestObject.put("topicCommunicationKey",diConsumerInterest.getTopicCommunicationKey());
					consumerInterestObject.put("topicValue",diConsumerInterest.getTopicValue());

				} catch (JSONException e) {

					e.printStackTrace();
				}
				consumerInterestArray.put(consumerInterestObject);
			}
		}

		if (captured != null) {
			try {
				captured.put("consumerInterests", consumerInterestArray);

				try {
					captured.synchronize(new Capture.CaptureApiRequestCallback() {
						@Override
						public void onSuccess() {

						}

						@Override
						public void onFailure(CaptureApiError e) {

						}
					});

				} catch (InvalidApidChangeException e) {

					e.printStackTrace();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	// For Log out

	public void logout(Activity activity) {
		Jump.signOutCaptureUser(activity);
		CaptureRecord.deleteFromDisk(context);
	}
}
