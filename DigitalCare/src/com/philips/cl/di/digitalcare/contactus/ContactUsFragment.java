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
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuth;
import com.philips.cl.di.digitalcare.social.twitter.TwitterConnect;
import com.philips.cl.di.digitalcare.social.twitter.TwitterScreenFragment;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.Utils;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2015
 */
public class ContactUsFragment extends DigitalCareBaseFragment implements
		TwitterAuth, OnClickListener {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private DigitalCareFontButton mFacebook = null;
	private DigitalCareFontButton mTwitter = null;
	private DigitalCareFontButton mChat = null;
	private DigitalCareFontButton mEmail = null;
	private DigitalCareFontButton mCallPhilips = null;
	private CdlsResponseParserHelper mParserController = null;
	private CdlsBean mCdlsBean = null;
	private TextView mFirstRowText = null;
	private TextView mSecondRowText = null;
	private TextView mContactUsOpeningHours = null;
	private String mCdlsResponse = null;

	// CDLS related
	private CdlsRequestAsyncTask mLongRunningTask = null;
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
		mLongRunningTask = new CdlsRequestAsyncTask(mURL, mLongRunningInterface);
		if (!(mLongRunningTask.getStatus() == AsyncTask.Status.RUNNING || mLongRunningTask
				.getStatus() == AsyncTask.Status.FINISHED)) {
			mLongRunningTask.execute();
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
		mChat = (DigitalCareFontButton) getActivity().findViewById(R.id.contactUsChat);
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
		showFragment(new TwitterScreenFragment());
	}

	private CdlsResponseCallback mLongRunningInterface = new CdlsResponseCallback() {

		@Override
		public void onResponseReceived(String response) {
			DLog.i(TAG, "response : " + response);
			if (response != null) {
				mCdlsResponse = response;
				mParserController = CdlsResponseParserHelper
						.getParserControllInstance(getActivity());
				mParserController.extractCdlsValues(response);
				mCdlsBean = mParserController.getCdlsBean();
				if (mCdlsBean != null) {
					if (mCdlsBean.getSuccess()) {
						enableBottomText();
						mCallPhilips.setText(getResources().getString(
								R.string.call)
								+ " " + mCdlsBean.getPhone().getPhoneNumber());
						mFirstRowText.setText(mCdlsBean.getPhone()
								.getOpeningHoursWeekdays());
						mSecondRowText.setText(mCdlsBean.getPhone()
								.getOpeningHoursSaturday());

						if (emptyChat(mCdlsBean)) {
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

		private boolean emptyChat(CdlsBean cdlsBean) {
			return cdlsBean.getChat() == null
					|| cdlsBean.getChat().getContent() == null
					|| cdlsBean.getChat().getContent().equalsIgnoreCase("");
		}
	};

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:"
				+ mCdlsBean.getPhone().getPhoneNumber()));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.contactUsChat && Utils.isNetworkConnected(getActivity())) {
			if (mCdlsResponse == null) {
				Toast.makeText(getActivity(), "No server response",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (mCdlsBean != null && !mCdlsBean.getSuccess()) {
				Toast.makeText(getActivity(),
						mCdlsBean.getError().getErrorMessage(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			showFragment(new ChatFragment());
		} else if (id == R.id.contactUsCall) {
			if (mCdlsResponse == null) {
				Toast.makeText(getActivity(), "No server response",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (mCdlsBean != null && !mCdlsBean.getSuccess()) {
				Toast.makeText(getActivity(),
						mCdlsBean.getError().getErrorMessage(),
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
//			getActivity().startActivity(
//					new Intent(Intent.ACTION_VIEW, Uri
//							.parse("https://m.facebook.com/PhilipsIndia?")));

		} else if (id == R.id.socialLoginTwitterBtn
				&& Utils.isNetworkConnected(getActivity())) {
			mTwitter.setClickable(false);
			TwitterConnect mTwitter = TwitterConnect.getInstance(getActivity());
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
}
