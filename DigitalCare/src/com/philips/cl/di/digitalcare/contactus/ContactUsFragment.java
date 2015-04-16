package com.philips.cl.di.digitalcare.contactus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.social.facebook.FacebookAuthenticate;
import com.philips.cl.di.digitalcare.social.facebook.FacebookHelper;
import com.philips.cl.di.digitalcare.social.facebook.FacebookScreenFragment;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthenticationCallback;
import com.philips.cl.di.digitalcare.social.twitter.TwitterSupportFragment;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 * ContactUsFragment will help to provide options to contact Philips.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 19 Jan 2015
 */
public class ContactUsFragment extends DigitalCareBaseFragment implements
		TwitterAuthenticationCallback, OnClickListener {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private DigitalCareFontButton mFacebook = null;
	private DigitalCareFontButton mTwitter = null;
	private DigitalCareFontButton mChat = null;
	private DigitalCareFontButton mEmail = null;
	private DigitalCareFontButton mCallPhilips = null;
	private CdlsResponseParser mCdlsResponseParser = null;
	private CdlsResponseModel mCdlsParsedResponse = null;
	private TextView mFirstRowText = null;
	private TextView mSecondRowText = null;
	private TextView mContactUsOpeningHours = null;
	private String mCdlsResponseStr = null;
	private View mView = null;
	private Handler mTwitterProgresshandler = null;
	private ProgressDialog mPostProgress = null;
	Configuration config = null;

	// CDLS related
	private CdlsRequestTask mCdlsRequestTask = null;
	private static final String CDLS_BASE_URL_PREFIX = "http://www.philips.com/prx/cdls/B2C/";
	private static final String CDLS_BASE_URL_POSTFIX = ".querytype.(fallback)";

	private static final String TAG = ContactUsFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DLog.i(TAG, "ContactUsFragment : onCreate");
		mCdlsRequestTask = new CdlsRequestTask(getActivity(), formCdlsURL(),
				mCdlsResponseCallback);
		mTwitterProgresshandler = new Handler();
		if (!(mCdlsRequestTask.getStatus() == AsyncTask.Status.RUNNING || mCdlsRequestTask
				.getStatus() == AsyncTask.Status.FINISHED)) {
			mCdlsRequestTask.execute();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DLog.i(TAG, "ContactUsFragment : onCreateView: mView - " + mView);
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_contact_us, container,
					false);
		}
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DLog.i(TAG,
				"ContactUsFragment : onActivityCreated : mConactUsParent == "
						+ mConactUsParent);
		if (mConactUsParent == null) {
			mConactUsParent = (LinearLayout) getActivity().findViewById(
					R.id.contactUsParent);
			mChat = (DigitalCareFontButton) getActivity().findViewById(
					R.id.contactUsChat);
			mFacebook = (DigitalCareFontButton) getActivity().findViewById(
					R.id.socialLoginFacebookBtn);
			mTwitter = (DigitalCareFontButton) getActivity().findViewById(
					R.id.socialLoginTwitterBtn);
			mCallPhilips = (DigitalCareFontButton) getActivity().findViewById(
					R.id.contactUsCall);
			mEmail = (DigitalCareFontButton) getActivity().findViewById(
					R.id.contactUsEmail);
			mContactUsOpeningHours = (TextView) getActivity().findViewById(
					R.id.contactUsOpeningHours);
			mFirstRowText = (TextView) getActivity().findViewById(
					R.id.firstRowText);
			mSecondRowText = (TextView) getActivity().findViewById(
					R.id.secondRowText);
			mFacebook.setOnClickListener(this);

			/*
			 * Live chat is configurable parameter. Developer can enable/disable
			 * it.
			 */
			if (!getResources().getBoolean(R.bool.live_chat_required)) {
				mChat.setVisibility(View.GONE);
			}
			mChat.setOnClickListener(this);
			mChat.setTransformationMethod(null);
			mFacebook.setTransformationMethod(null);
			mCallPhilips.setOnClickListener(this);
			mCallPhilips.setTransformationMethod(null);
			mTwitter.setOnClickListener(this);
			mTwitter.setTransformationMethod(null);
			mEmail.setOnClickListener(this);
			mEmail.setTransformationMethod(null);
			mParams = (FrameLayout.LayoutParams) mConactUsParent
					.getLayoutParams();

			AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACT_US);
		}

		if (!Utils.isNetworkConnected(getActivity())) {
			return;
		}
		config = getResources().getConfiguration();
		
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		setViewParams(config);
	}

	/*
	 * Forming CDLS url. This url will be different for US and other countries.
	 */
	private String formCdlsURL() {
		return CDLS_BASE_URL_PREFIX + DigitalCareConfigManager.getLocale()
				+ DigitalCareConfigManager.getCdlsPrimarySubCategory()
				+ CDLS_BASE_URL_POSTFIX;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void onTwitterLoginFailed() {
		DLog.d(TAG, "Twitter Authentication Failed");
		mTwitter.setClickable(true);
	}

	@Override
	public void onTwitterLoginSuccessful() {
		mTwitter.setClickable(true);
		Toast.makeText(getActivity(), "Logged in Successfully",
				Toast.LENGTH_SHORT).show();
		showFragment(new TwitterSupportFragment());
	}

	private CdlsResponseCallback mCdlsResponseCallback = new CdlsResponseCallback() {

		@Override
		public void onCdlsResponseReceived(String response) {
			DLog.i(TAG, "response : " + response);
			if (response != null && isAdded()) {
				mCdlsResponseStr = response;
				mCdlsResponseParser = CdlsResponseParser
						.getParserControllInstance(getActivity());
				mCdlsResponseParser.processCdlsResponse(response);
				mCdlsParsedResponse = mCdlsResponseParser.getCdlsBean();
				if (mCdlsParsedResponse != null) {
					if (mCdlsParsedResponse.getSuccess()) {
						enableBottomText();
						mCallPhilips.setText(getResources().getString(
								R.string.call_number)
								+ " "
								+ mCdlsParsedResponse.getPhone()
										.getPhoneNumber());
						mFirstRowText.setText(mCdlsParsedResponse.getPhone()
								.getOpeningHoursWeekdays());
						if (Utils.isEmpty(mCdlsParsedResponse.getPhone()
								.getOpeningHoursSaturday())) {
							mSecondRowText.setText(mCdlsParsedResponse
									.getPhone().getOpeningHoursSunday());
						} else {
							mSecondRowText.setText(mCdlsParsedResponse
									.getPhone().getOpeningHoursSaturday());
						}
						if (hasEmptyChatContent(mCdlsParsedResponse)) {
							mChat.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
							mChat.setEnabled(false);
						}

					} else {
						fadeoutButtons();
					}
				} else {
					fadeoutButtons();
				}
			} else {
				fadeoutButtons();
			}
		}

		private boolean hasEmptyChatContent(CdlsResponseModel cdlsResponseModel) {
			return cdlsResponseModel.getChat() == null
					|| cdlsResponseModel.getChat().getContent() == null
					|| cdlsResponseModel.getChat().getContent()
							.equalsIgnoreCase("");
		}
	};

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:"
				+ mCdlsParsedResponse.getPhone().getPhoneNumber()));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.contactUsChat && Utils.isNetworkConnected(getActivity())) {
			if (mCdlsResponseStr == null) {
				Toast.makeText(getActivity(), "No server response",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (mCdlsParsedResponse != null
					&& !mCdlsParsedResponse.getSuccess()) {
				Toast.makeText(getActivity(),
						mCdlsParsedResponse.getError().getErrorMessage(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			tagServiceRequest(AnalyticsConstants.SERVICE_CHANNEL_CHAT);
			showFragment(new ChatFragment());
		} else if (id == R.id.contactUsCall) {
			if (mCdlsResponseStr == null) {
				Toast.makeText(getActivity(), "No server response",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (mCdlsParsedResponse != null
					&& !mCdlsParsedResponse.getSuccess()) {
				Toast.makeText(getActivity(),
						mCdlsParsedResponse.getError().getErrorMessage(),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (Utils.isSimAvailable(getActivity())) {
				tagServiceRequest(AnalyticsConstants.SERVICE_CHANNEL_CALL);
				callPhilips();
			} else if (!Utils.isSimAvailable(getActivity())) {
				Toast.makeText(getActivity(), "Check the SIM",
						Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.socialLoginFacebookBtn
				&& Utils.isNetworkConnected(getActivity())) {

			Session mFacebookSession = Session.getActiveSession();

			DLog.d(TAG, "Session - getSession from Facebook SDK "
					+ mFacebookSession);
			if (mFacebookSession == null) {
				DLog.d(TAG, "Session is null so Starting FacebookSession");
				startFacebookSession();
			} else if ((mFacebookSession != null)
					&& (mFacebookSession.getState() == SessionState.CLOSED_LOGIN_FAILED)) {
				DLog.d(TAG, "Session is state is CLOSED_LOGIN_FAILED"
						+ " so Starting Facebook Session");
				startFacebookSession();
			} else if ((mFacebookSession != null)
					&& (mFacebookSession.getState() == SessionState.OPENED)) {
				DLog.d(TAG,
						"Session - getSession from Facebook SDK is not NULL  : "
								+ mFacebookSession);
				showFragment(new FacebookScreenFragment());
				DLog.d(TAG, "Session is not null");

			}
			// showFragment(new FacebookScreenFragment());
			// mFacebook.setLoginBehavior(SessionLoginBehavior.SSO_ONLY);
			// mFacebook.setReadPermissions(Arrays.asList("email",
			// "user_birthday", "user_hometown", "user_location"));
			// FacebookHelper mHelper = new FacebookHelper(getActivity());

		} else if (id == R.id.socialLoginTwitterBtn
				&& Utils.isNetworkConnected(getActivity())) {
			mTwitter.setClickable(false);
			TwitterAuthentication mTwitter = TwitterAuthentication
					.getInstance(getActivity());
			mTwitter.initSDK(this);
			mPostProgress = new ProgressDialog(getActivity());
			mPostProgress.setMessage("Loading...");
			mPostProgress.setCancelable(false);
			if (!(getActivity().isFinishing()))
				mPostProgress.show();
			mTwitterProgresshandler.postDelayed(mTwitteroAuthRunnable, 10000l);
			
		} else if (id == R.id.contactUsEmail) {
			tagServiceRequest(AnalyticsConstants.SERVICE_CHANNEL_EMAIL);
			sendEmail();
		}
	}

	@Override
	public void onPause() {
		if (mPostProgress != null && mPostProgress.isShowing()) {
			mPostProgress.dismiss();
			mPostProgress = null;
		}
		super.onPause();
	}
	
	
	Runnable mTwitteroAuthRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mPostProgress != null && mPostProgress.isShowing()) {
				mPostProgress.dismiss();
				mPostProgress = null;
			}
		}
	}; 
	
	private void tagServiceRequest(String serviceChannel) {
		AnalyticsTracker.trackAction(
				AnalyticsConstants.ACTION_KEY_SERVICE_REQUEST,
				AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, serviceChannel);
	}

	private void tagTechnicalError() {
		AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_KEY_SET_ERROR,
				AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
				AnalyticsConstants.TECHNICAL_ERROR_RESPONSE_CDLS);
	}

	/*
	 * If CDLS response received then enable to bottom text.
	 */
	private void enableBottomText() {
		mContactUsOpeningHours.setVisibility(View.VISIBLE);
		mFirstRowText.setVisibility(View.VISIBLE);
		mSecondRowText.setVisibility(View.VISIBLE);
	}

	/*
	 * If feature is not available then fade it out.
	 */
	private void fadeoutButtons() {
		tagTechnicalError();
		if (mCallPhilips != null) {
			mCallPhilips
					.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
			mCallPhilips.setEnabled(false);
		}
		if (mChat != null) {
			mChat.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
			mChat.setEnabled(false);
		}
	}

	@Override
	public void setViewParams(Configuration config) {

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// if (mLeftRightMarginPort != 0)
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			// if (mLeftRightMarginLand != 0)
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mConactUsParent.setLayoutParams(mParams);
	}

	/*
	 * TODO: Sending message is been implemented through gmail. So this is
	 * temperory.
	 * 
	 * Wouter is working on In-App messaging.
	 */
	private void sendEmail() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getResources()
				.getString(R.string.support_email_id) });
		intent.putExtra(Intent.EXTRA_SUBJECT,
				"My AirFryer HD9220/20 is gone case");
		intent.putExtra(
				Intent.EXTRA_TEXT,
				"Hi Team\n My Airfryer is not at all cooking actually. It is leaving ultimate smoke."
						+ " Please do let me know how i can correct my favourate Philips Machine!! ");
		intent.setPackage("com.google.android.gm");
		getActivity().startActivity(intent);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.opt_contact_us);
	}

	private void startFacebookSession() {
		FacebookHelper mHelper = FacebookHelper.getInstance(getActivity());
		mHelper.openFacebookSession(new FacebookAuthenticate() {
			@Override
			public void onSuccess() {
				showFragment(new FacebookScreenFragment());
			}
		});
	}
}
