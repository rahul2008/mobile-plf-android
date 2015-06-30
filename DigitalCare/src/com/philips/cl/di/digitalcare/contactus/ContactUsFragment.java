package com.philips.cl.di.digitalcare.contactus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.philips.cl.di.digitalcare.ConsumerProductInfo;
import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.RequestData;
import com.philips.cl.di.digitalcare.ResponseCallback;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.social.facebook.FacebookWebFragment;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthenticationCallback;
import com.philips.cl.di.digitalcare.social.twitter.TwitterSupportFragment;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;
import com.philips.cl.di.digitalcare.util.Utils;

/*import com.philips.cl.di.digitalcare.social.facebook.FacebookAuthenticate;
 import com.philips.cl.di.digitalcare.social.facebook.FacebookHelper;
 import com.philips.cl.di.digitalcare.social.facebook.FacebookScreenFragment;
 import com.facebook.Session;
 import com.facebook.SessionState;*/

/**
 * ContactUsFragment will help to provide options to contact Philips.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 19 Jan 2015
 */
public class ContactUsFragment extends DigitalCareBaseFragment implements
		TwitterAuthenticationCallback, OnClickListener, ResponseCallback {
	private LinearLayout mContactUsParent = null;
	private LinearLayout mSocialProviderParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private DigitalCareFontButton mChat = null;
	private DigitalCareFontButton mEmail = null;
	private DigitalCareFontButton mCallPhilips = null;
	private CdlsResponseModel mCdlsParsedResponse = null;
	private TextView mFirstRowText = null;
	private TextView mContactUsOpeningHours = null;
	private String mCdlsResponseStr = null;
	private View mView = null;
	private Handler mTwitterProgresshandler = null;
	private ProgressDialog mPostProgress = null;
	private ProgressDialog mDialog = null;
	private Configuration config = null;
	private View mSocialDivider = null;

	private static final String CDLS_URL_PORT = "http://www.philips.com/prx/cdls/%s/%s/%s/%s.querytype.(fallback)";
	private static final String TAG = ContactUsFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DigiCareLogger.i(TAG, "ContactUsFragment : onCreate");
		// mTwitterProgresshandler = new Handler();
		// if (isConnectionAvailable())
		// requestCDLSData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DigiCareLogger.i(TAG, "ContactUsFragment : onCreateView: mView - "
				+ mView);
		// if (mView == null) {
		// mView = inflater.inflate(R.layout.fragment_contact_us, container,
		// false);
		// }

		try {
			if (mView != null) {
				((ViewGroup) mView.getParent()).removeView(mView);
			}
			mView = inflater.inflate(R.layout.fragment_contact_us, container,
					false);

		} catch (InflateException e) {
		}

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DigiCareLogger.i(TAG,
				"ContactUsFragment : onActivityCreated : mConactUsParent == "
						+ mContactUsParent);
		// if (mContactUsParent == null) {
		mTwitterProgresshandler = new Handler();
		if (isConnectionAvailable())
			requestCDLSData();

		mContactUsParent = (LinearLayout) getActivity().findViewById(
				R.id.contactUsParent);
		mChat = (DigitalCareFontButton) getActivity().findViewById(
				R.id.contactUsChat);
		mCallPhilips = (DigitalCareFontButton) getActivity().findViewById(
				R.id.contactUsCall);
		mEmail = (DigitalCareFontButton) getActivity().findViewById(
				R.id.contactUsEmail);
		mContactUsOpeningHours = (TextView) getActivity().findViewById(
				R.id.contactUsOpeningHours);
		mFirstRowText = (TextView) getActivity()
				.findViewById(R.id.firstRowText);
		mSocialProviderParent = (LinearLayout) getActivity().findViewById(
				R.id.contactUsSocialParent);
		mSocialDivider = (View) getActivity().findViewById(R.id.socialDivider);
		// mFacebook.setOnClickListener(this);

		createSocialProviderMenu();

		/*
		 * Live chat is configurable parameter. Developer can enable/disable it.
		 */
		if (!getResources().getBoolean(R.bool.live_chat_required)) {
			mChat.setVisibility(View.GONE);
		}
		mChat.setOnClickListener(this);
		mChat.setTransformationMethod(null);
		mCallPhilips.setOnClickListener(this);
		mCallPhilips.setTransformationMethod(null);
		mEmail.setOnClickListener(this);
		mEmail.setTransformationMethod(null);
		mParams = (FrameLayout.LayoutParams) mContactUsParent.getLayoutParams();

		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACT_US,
				getPreviousName());
		// }
		config = getResources().getConfiguration();
	}

	private void createSocialProviderMenu() {
		Resources mResources = getActivity().getResources();
		String[] mSocialProviderKeys = mResources
				.getStringArray(R.array.social_service_provider_menu_title);
		String[] mSocialProviderDrawableKey = mResources
				.getStringArray(R.array.social_service_provider_menu_resources);

		if (mSocialProviderKeys.length == 0) {
			mSocialProviderParent.setVisibility(View.GONE);
			mSocialDivider.setVisibility(View.GONE);
		} else {
			for (int i = 0; i < mSocialProviderKeys.length; i++) {
				createButtonLayout(mSocialProviderKeys[i],
						mSocialProviderDrawableKey[i]);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setViewParams(config);
	}

	/*
	 * Forming CDLS url. This url will be different for US and other countries.
	 */
	protected String formCdlsURL() {
		ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
				.getInstance().getConsumerProductInfo();
		return getCdlsUrl(consumerProductInfo.getSector(),
				DigitalCareConfigManager.getInstance().getLocale().toString(),
				consumerProductInfo.getCatalog(),
				consumerProductInfo.getSubCategory());
	}

	protected String getCdlsUrl(String sector, String locale, String catalog,
			String subcategory) {
		return String.format(CDLS_URL_PORT, sector, locale, catalog,
				subcategory);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void onTwitterLoginFailed() {
		DigiCareLogger.d(TAG, "Twitter Authentication Failed");
	}

	@Override
	public void onTwitterLoginSuccessful() {
		showFragment(new TwitterSupportFragment());
	}

	protected void requestCDLSData() {
		DigiCareLogger.d(TAG, "CDLS Request Thread is started");
		startProgressDialog();
		new RequestData(formCdlsURL(), this).execute();
	}

	@Override
	public void onResponseReceived(String response) {
		closeProgressDialog();
		DigiCareLogger.i(TAG, "response : " + response);
		if (response != null && isAdded()) {
			mCdlsResponseStr = response;
			parseCDLSResponse(response);
		} else {
			fadeoutButtons();
		}
	}

	private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
		@Override
		public void onCdlsParsingComplete(final CdlsResponseModel response) {
			if (response != null) {
				mCdlsParsedResponse = response;
				updateUi();
			} else {
				fadeoutButtons();
			}
		}
	};

	protected void parseCDLSResponse(String response) {
		DigiCareLogger.d(TAG, "Parsing CDLS Response");
		CdlsResponseParser cdlsResponseParser = new CdlsResponseParser(
				mParsingCompletedCallback);
		cdlsResponseParser.parseCdlsResponse(response);
	}

	protected void updateUi() {
		DigiCareLogger.d(TAG, "Updating Contact Information");

		if (mCdlsParsedResponse.getSuccess()
				|| mCdlsParsedResponse.getError() != null) {
			CdlsPhoneModel phoneModel = mCdlsParsedResponse.getPhone();

			if (phoneModel != null) {
				enableBottomText();
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(phoneModel.getOpeningHoursWeekdays())
						.append(phoneModel.getOpeningHoursSaturday())
						.append(phoneModel.getOpeningHoursSunday())
						.append(phoneModel.getOptionalData1())
						.append(phoneModel.getOptionalData2());
				enableBottomText();
				mCallPhilips
						.setText(getResources().getString(R.string.call_number)
								+ " "
								+ mCdlsParsedResponse.getPhone()
										.getPhoneNumber());
				mFirstRowText.setText(stringBuilder);
			}

			if (hasEmptyChatContent(mCdlsParsedResponse)) {
				mChat.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
				mChat.setEnabled(false);
			}
		} else {
			fadeoutButtons();
		}

	}

	protected boolean hasEmptyChatContent(CdlsResponseModel cdlsResponseModel) {
		return cdlsResponseModel.getChat() == null
				|| cdlsResponseModel.getChat().getContent() == null
				|| cdlsResponseModel.getChat().getContent()
						.equalsIgnoreCase("");
	}

	protected void closeProgressDialog() {
		DigiCareLogger.v(TAG, "Progress Dialog Cancelled");

		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog.cancel();
			mDialog = null;
		}
	}

	protected void startProgressDialog() {
		DigiCareLogger.v(TAG, "Progress Dialog Started");
		if (mDialog == null)
			mDialog = new ProgressDialog(getActivity());
		mDialog.setMessage(getActivity().getResources().getString(
				R.string.loading));
		mDialog.setCancelable(false);
		if (!(getActivity().isFinishing())) {
			mDialog.show();
		}
	}

	protected void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:"
				+ mCdlsParsedResponse.getPhone().getPhoneNumber()));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		String tag = (String) view.getTag();

		boolean actionTaken = false;

		if (tag != null) {
			actionTaken = DigitalCareConfigManager.getInstance()
					.getSocialProviderListener()
					.onSocialProviderItemClicked(tag.toString());
		}
		if (actionTaken) {
			return;
		}

		if (id == R.id.contactUsChat && isConnectionAvailable()) {
			if (mCdlsResponseStr == null) {
				showAlert(mCdlsParsedResponse.getError().getErrorMessage());
				return;
			} else if (mCdlsParsedResponse != null
					&& !mCdlsParsedResponse.getSuccess()) {
				showAlert(mCdlsParsedResponse.getError().getErrorMessage());
				return;
			}
			tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CHAT);
			showFragment(new ChatFragment());
		} else if (id == R.id.contactUsCall) {
			if (mCdlsResponseStr == null) {
				showAlert(mCdlsParsedResponse.getError().getErrorMessage());
				return;
			} else if (mCdlsParsedResponse != null
					&& !mCdlsParsedResponse.getSuccess()) {
				showAlert(mCdlsParsedResponse.getError().getErrorMessage());
				return;
			} else if (Utils.isSimAvailable(getActivity())) {
				tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CALL);
				callPhilips();
			} else if (!Utils.isSimAvailable(getActivity())) {
				showAlert(getString(R.string.check_sim));
			}
		} else if (tag != null
				&& tag.equalsIgnoreCase(getStringKey(R.string.facebook))
				&& isConnectionAvailable()) {

			launchFacebookFeature();

			/*
			 * Session mFacebookSession = Session.getActiveSession();
			 * 
			 * DigiCareLogger.d(TAG, "Session - getSession from Facebook SDK " +
			 * mFacebookSession); if (mFacebookSession == null) {
			 * DigiCareLogger.d(TAG,
			 * "Session is null so Starting FacebookSession");
			 * startFacebookSession(); } else if ((mFacebookSession != null) &&
			 * (mFacebookSession.getState() ==
			 * SessionState.CLOSED_LOGIN_FAILED)) { DigiCareLogger.d(TAG,
			 * "Session is state is CLOSED_LOGIN_FAILED" +
			 * " so Starting Facebook Session"); startFacebookSession(); } else
			 * if ((mFacebookSession != null) && (mFacebookSession.getState() ==
			 * SessionState.OPENED)) { DigiCareLogger.d(TAG,
			 * "Session - getSession from Facebook SDK is not NULL  : " +
			 * mFacebookSession); showFragment(new FacebookScreenFragment());
			 * DigiCareLogger.d(TAG, "Session is not null");
			 * 
			 * }
			 */
		} else if (tag != null
				&& tag.equalsIgnoreCase(getStringKey(R.string.twitter))
				&& isConnectionAvailable()) {
			// mTwitter.setClickable(false);
			TwitterAuthentication mTwitter = TwitterAuthentication
					.getInstance(getActivity());
			mTwitter.initSDK(this);
			mPostProgress = new ProgressDialog(getActivity());
			mPostProgress.setMessage(getActivity().getResources().getString(
					R.string.loading));
			mPostProgress.setCancelable(false);
			if (!(getActivity().isFinishing()))
				mPostProgress.show();
			mTwitterProgresshandler.postDelayed(mTwitteroAuthRunnable, 10000l);

		} else if (id == R.id.contactUsEmail) {
			tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_EMAIL);
			sendEmail();
		}
	}

	private void launchFacebookFeature() {

		try {
			getActivity().getApplicationContext().getPackageManager()
					.getPackageInfo("com.facebook.katana", 0);
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"
					+ getActivity().getResources().getString(
							R.string.facebook_product_pageID))));
			DigiCareLogger.v(TAG, "Launced Installed Facebook Application");
		} catch (Exception e) {
			DigiCareLogger.v(TAG, "Launching Facebook Webpage");
			showFragment(new FacebookWebFragment());
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

	private Runnable mTwitteroAuthRunnable = new Runnable() {

		@Override
		public void run() {
			if (mPostProgress != null && mPostProgress.isShowing()) {
				mPostProgress.dismiss();
				mPostProgress = null;
			}
		}
	};

	private void tagServiceRequest(String serviceChannel) {
		AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_SERVICE_REQUEST,
				AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, serviceChannel);
	}

	private void tagTechnicalError() {
		AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_SET_ERROR,
				AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
				AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_RESPONSE_CDLS);
	}

	/*
	 * If CDLS response received then enable to bottom text.
	 */
	protected void enableBottomText() {
		mContactUsOpeningHours.setVisibility(View.VISIBLE);
		mFirstRowText.setVisibility(View.VISIBLE);
	}

	/*
	 * If feature is not available then fade it out.
	 */
	protected void fadeoutButtons() {
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
		mContactUsParent.setLayoutParams(mParams);
	}

	/*
	 * TODO: Sending message is been implemented through gmail. So this is
	 * temperory.
	 * 
	 * Wouter is working on In-App messaging.
	 */
	protected void sendEmail() {
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

	private Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	private String getStringKey(int resId) {
		return getResources().getResourceEntryName(resId);
	}

	/**
	 * Create RelativeLayout at runTime. RelativeLayout will have button and
	 * image together.
	 */
	@SuppressLint("NewApi")
	private void createButtonLayout(String buttonTitle, String buttonDrawable) {
		String packageName = getActivity().getPackageName();
		int title = getResources().getIdentifier(
				packageName + ":string/" + buttonTitle, null, null);
		int drawable = getResources().getIdentifier(
				packageName + ":drawable/" + buttonDrawable, null, null);
		float density = getResources().getDisplayMetrics().density;

		RelativeLayout relativeLayout = createRelativeLayout(buttonTitle);
		Button button = createButton(density, title, drawable);
		relativeLayout.addView(button);
		setButtonParams(button);
		mSocialProviderParent.addView(relativeLayout);
		setRelativeLayoutParams(relativeLayout, density);
		/*
		 * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
		relativeLayout.setTag(buttonTitle);
		relativeLayout.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private RelativeLayout createRelativeLayout(String buttonTitle) {
		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) getActivity().getResources()
						.getDimension(R.dimen.support_btn_height));
		relativeLayout.setLayoutParams(params);
		relativeLayout
				.setBackground(getDrawable(R.drawable.prod_reg_social_border_btn));

		return relativeLayout;
	}

	private void setRelativeLayoutParams(RelativeLayout relativeLayout,
			float density) {
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
				.getLayoutParams();
		param.topMargin = (int) (15 * density);
		relativeLayout.setLayoutParams(param);
	}

	@SuppressLint("NewApi")
	private Button createButton(float density, int title, int resId) {
		Button button = new Button(getActivity(), null, R.style.fontButton);
		button.setGravity(Gravity.START | Gravity.CENTER);
		button.setPadding((int) (80 * density), 0, 0, 0);
		button.setTextAppearance(getActivity(), R.style.fontButton);
		button.setText(title);
		button.setBackground(getDrawable(resId));
		return button;
	}

	private void setButtonParams(Button button) {
		RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
				.getLayoutParams();
		buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		button.setLayoutParams(buttonParams);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.contact_us);
	}

	@Override
	public String setPreviousPageName() {
		return AnalyticsConstants.PAGE_CONTACT_US;
	}

	/*
	 * protected void startFacebookSession() { FacebookHelper mHelper =
	 * FacebookHelper.getInstance(getActivity());
	 * mHelper.openFacebookSession(new FacebookAuthenticate() {
	 * 
	 * @Override public void onSuccess() { showFragment(new
	 * FacebookScreenFragment()); } }); }
	 */
}
