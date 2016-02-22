
package com.philips.cdp.registration.coppa;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.Jump.CaptureApiResultHandler;
import com.philips.cdp.registration.HttpClient;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoppaExtension implements CoppaExtensionHandler {


//	pending stuffs
//	-writtig sendcoppamail flow after login
//	-testing coppa end to end
//	-fix if any issues


    private final String NULL = "null";

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


    public void triggerSendCoppaMailAfterLogin(final String email) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String url = "https://" + Jump.getCaptureDomain() + "/widget/verify_email.jsonp";
                HttpClient client = new HttpClient();
                //String response = client.postData(url,getCoppaMailParams(email), null);
                String response = client.callPost(url, getCoppaMailParameters(email), null);
                RLog.i("triggerSendCoppaMailAfterLogin", "triggerSendCoppaMailAfterLogin response : " + response);

            }
        }).start();
    }


    @Override
    public void resendCoppaEmailConsentForUserEmail(final String email,
                                                    final ResendCoppaEmailConsentHandler resendCoppaEmailConsentHandler) {
        final boolean isConsent = getCosentGivenStatus();
        String MAIL_TYPE_CONSENT = "consent";
        String MAIL_TYPE_CONFIRMATION = "confirmation";
        final String accessToken = Jump.getSignedInUser() != null ? Jump.getSignedInUser()
                .getAccessToken() : null;

        Log.i("COPPA", "Returninge of doInBackground :Coppa acces token" + accessToken);
        final String mailType = isConsent ? MAIL_TYPE_CONFIRMATION : MAIL_TYPE_CONSENT;
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpClient client = new HttpClient();
                /*String response = client.postData(RegistrationHelper.getInstance()
				        .getRegistrationSettings().getResendConsentUrl(),
				        geResendMailParams(mailType, email), accessToken);*/

                String response = client.callPost(UserRegistrationInitializer.getInstance().getRegistrationSettings().getResendConsentUrl(),
                        getResendMailParameters(mailType, email), accessToken);

                Log.i("COPPA", "Resend Response" + response);

                if (response == null) {
                    notifyCoppaResendFailed(0, null, resendCoppaEmailConsentHandler);
                } else {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(response);
                        parseResendResponse(jsonObject, isConsent, resendCoppaEmailConsentHandler);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        notifyCoppaResendFailed(0, null, resendCoppaEmailConsentHandler);
                    }
                }
            }
        }).start();
    }

    private void parseResendResponse(JSONObject jsonObject, boolean isConsent,
                                     ResendCoppaEmailConsentHandler resendCoppaEmailConsentHandler) {
        String kErrorMessage = "errormessage";
        String kError = "ERROR";
        String kMessage = "message";
        String kSuccess = "success";
        String kErrorCode = "errorcode";
        try {
            if (!jsonObject.isNull(kSuccess)) {
                if (jsonObject.getBoolean(kSuccess)) {
                    resendCoppaEmailConsentHandler.didResendCoppaEmailConsentSucess();
                } else {
                    notifyCoppaResendFailed(0, jsonObject.getString(kMessage),
                            resendCoppaEmailConsentHandler);
                }
            } else {
                if (!jsonObject.isNull(kError)) {
                    JSONObject jsonObject2 = jsonObject.getJSONObject(kError);
                    int errorCode = 0;
                    String errorDesc = null;
                    if (!jsonObject2.isNull(kErrorCode)) {
                        errorCode = Integer.parseInt(jsonObject2.getString(kErrorCode));
                    }

                    if (!jsonObject2.isNull(kErrorMessage)) {
                        errorDesc = jsonObject2.getString(kErrorMessage);
                    }
                    notifyCoppaResendFailed(errorCode, errorDesc, resendCoppaEmailConsentHandler);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            notifyCoppaResendFailed(0, null, resendCoppaEmailConsentHandler);
        }
    }

    private void notifyCoppaResendFailed(int errorCode, String errorDesc,
                                         ResendCoppaEmailConsentHandler resendCoppaEmailConsentHandler) {
        CoppaResendError coppaResendError = new CoppaResendError();
        coppaResendError.setErrorCode(errorCode);
        coppaResendError.setErrorDesc(errorDesc);
        resendCoppaEmailConsentHandler.didResendCoppaEmailConsentFailedWithError(coppaResendError);
    }

	/*private List<NameValuePair> geResendMailParams(String mailtype, String email) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("locale", "en_US"));
		params.add(new BasicNameValuePair("campaignId", RegistrationConfiguration.getInstance()
		        .getPilConfiguration().getCampaignID()));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("mailType", mailtype));
		params.add(new BasicNameValuePair("redirect_uri", RegistrationHelper.getInstance()
		        .getRegistrationSettings().getRegisterCoppaActivationUrl()));
		params.add(new BasicNameValuePair("flow_name", RegistrationHelper.getInstance()
		        .getRegistrationSettings().getFlowName()));
		params.add(new BasicNameValuePair("form", "resendConsentForm"));
		params.add(new BasicNameValuePair("emailFieldName", "traditionalSignIn_emailAddress"));

		params.add(new BasicNameValuePair("flow_version", Jump.getCaptureFlowVersion()));
		params.add(new BasicNameValuePair("response_type", "token"));
		return params;
	}*/

    private List<Pair<String, String>> getResendMailParameters(String mailtype, String email) {
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair("locale", getResendLocaleParam()));
        Log.e("COPPA", "Returninge of doInBackground :campaign id" + RegistrationConfiguration.getInstance()
                .getPilConfiguration().getCampaignID());
        params.add(new Pair("campaignId", RegistrationConfiguration.getInstance()
                .getPilConfiguration().getCampaignID()));
        params.add(new Pair("email", email));
        params.add(new Pair("mailType", mailtype));
        params.add(new Pair("redirect_uri", UserRegistrationInitializer.getInstance().getRegistrationSettings().getRegisterCoppaActivationUrl()));
        params.add(new Pair("flow_name",
                UserRegistrationInitializer.getInstance().getRegistrationSettings().getFlowName()));
        params.add(new Pair("form", "resendConsentForm"));
        params.add(new Pair("emailFieldName", "traditionalSignIn_emailAddress"));
        params.add(new Pair("flow_version", Jump.getCaptureFlowVersion()));
        params.add(new Pair("response_type", "token"));

        return params;

    }

    private String getResendLocaleParam() {
        if (Jump.getCaptureLocale().length() > 2 && Jump.getCaptureLocale().contains("-")) {
            return Jump.getCaptureLocale().replace("-", "_");
        }
        return "en_US";
    }

    private List<Pair> getCoppaMailParams(String email) {
        List<Pair> params = new ArrayList<Pair>();
        params.add(new Pair("access_token", Jump.getAccessToken()));
        params.add(new Pair("capture_screen", "signIn"));
        params.add(new Pair("capture_transactionId", getTransactionID()));
        params.add(new Pair("client_id", Jump.getCaptureClientId()));
        params.add(new Pair("locale", Jump.getCaptureLocale()));
        params.add(new Pair("response_type", "token"));
        params.add(new Pair("redirect_uri",UserRegistrationInitializer.getInstance().getRegistrationSettings().getRegisterCoppaActivationUrl()));
        params.add(new Pair("flow", UserRegistrationInitializer.getInstance().getRegistrationSettings().getFlowName()));
        params.add(new Pair("form", "socialSignInConsentCheckForm"));
        params.add(new Pair("traditionalSignIn_emailAddress", email));
        params.add(new Pair("flow_version", Jump.getCaptureFlowVersion()));
        return params;
    }

    private List<Pair<String, String>> getCoppaMailParameters(String email) {
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair("access_token", Jump.getAccessToken()));

        params.add(new Pair("capture_screen", "signIn"));
        params.add(new Pair("capture_transactionId", getTransactionID()));
        params.add(new Pair("client_id", Jump.getCaptureClientId()));
        params.add(new Pair("locale", Jump.getCaptureLocale()));
        params.add(new Pair("response_type", "token"));
        params.add(new Pair("redirect_uri",UserRegistrationInitializer.getInstance().getRegistrationSettings().getRegisterCoppaActivationUrl()));
        params.add(new Pair("flow",UserRegistrationInitializer.getInstance().getRegistrationSettings().getFlowName()));
        params.add(new Pair("form", "socialSignInConsentCheckForm"));
        params.add(new Pair("traditionalSignIn_emailAddress", email));
        params.add(new Pair("flow_version", Jump.getCaptureFlowVersion()));
        return params;
    }


    private String getTransactionID() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }


    private boolean getCosentGivenStatus() {
        if (CoppaConfiguration.getConsent() != null
                && CoppaConfiguration.getConsent().getGiven() != null
                && !CoppaConfiguration.getConsent().getGiven().equalsIgnoreCase(NULL)) {
            return Boolean.parseBoolean(CoppaConfiguration.getConsent().getGiven());
        }
        return false;
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
                if (null != consent.getConfirmationGiven() && !consent.getConfirmationGiven().equalsIgnoreCase(NULL)) {
                    if (Boolean.parseBoolean(consent.getConfirmationGiven())) {
                        coppaStatus = CoppaStatus.kDICOPPAConfirmationGiven;
                    } else {
                        coppaStatus = CoppaStatus.kDICOPPAConfirmationNotGiven;
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
