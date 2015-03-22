package com.philips.cl.di.digitalcare.contactus;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.social.facebook.FacebookScreenFragment;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthenticationCallback;
import com.philips.cl.di.digitalcare.social.twitter.TwitterFragment;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 *	ContactUsFragment will help to provide options to contact Philips.
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

	// CDLS related
	private CdlsRequestTask mCdlsRequestTask = null;
	// private static final String mURL =
	// "http://www.philips.com/prx/cdls/B2C/de_DE/CARE/PERSONAL_CARE_GR.querytype.(fallback)";
	private static final String mURL = "http://www.philips.com/prx/cdls/B2C/en_GB/CARE/PERSONAL_CARE_GR.querytype.(fallback)";

	private static final String TAG = ContactUsFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (!Utils.isNetworkConnected(getActivity())) {
			return null;
		}
		/*
		 * CDLS execution
		 */
		mCdlsRequestTask = new CdlsRequestTask(mURL, mCdlsResponseCallback);
		if (!(mCdlsRequestTask.getStatus() == AsyncTask.Status.RUNNING || mCdlsRequestTask
				.getStatus() == AsyncTask.Status.FINISHED)) {
			mCdlsRequestTask.execute();
		}
		View view = inflater.inflate(R.layout.fragment_contact_us, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!Utils.isNetworkConnected(getActivity())) {
			return;
		}
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
		mFirstRowText = (TextView) getActivity()
				.findViewById(R.id.firstRowText);
		mSecondRowText = (TextView) getActivity().findViewById(
				R.id.secondRowText);
		mFacebook.setOnClickListener(this);

		/*
		 * Live chat is configurable parameter. Developer can enable/disable it.
		 */
		if (!getResources().getBoolean(R.bool.live_chat_required)) {
			mChat.setVisibility(View.GONE);
		}
		mChat.setOnClickListener(this);

		mCallPhilips.setOnClickListener(this);
		mTwitter.setOnClickListener(this);
		mEmail.setOnClickListener(this);
		mParams = (FrameLayout.LayoutParams) mConactUsParent.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
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
		showFragment(new TwitterFragment());
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
						mSecondRowText.setText(mCdlsParsedResponse.getPhone()
								.getOpeningHoursSaturday());

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

		private boolean hasEmptyChatContent(
				CdlsResponseModel cdlsResponseModel) {
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
				callPhilips();
			} else if (!Utils.isSimAvailable(getActivity())) {
				Toast.makeText(getActivity(), "Check the SIM",
						Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.socialLoginFacebookBtn
				&& Utils.isNetworkConnected(getActivity())) {
			showFragment(new FacebookScreenFragment());
		} else if (id == R.id.socialLoginTwitterBtn
				&& Utils.isNetworkConnected(getActivity())) {
			mTwitter.setClickable(true);
			TwitterAuthentication mTwitter = TwitterAuthentication
					.getInstance(getActivity());
			mTwitter.initSDK(this);
		} else if (id == R.id.contactUsEmail) {
			sendEmail();
		}
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
		mCallPhilips
				.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
		mCallPhilips.setEnabled(false);
		mChat.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
		mChat.setEnabled(false);
	}

	@Override
	public void setViewParams(Configuration config) {

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
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
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "philipscustomer@philipsSupport.com" });
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
}
