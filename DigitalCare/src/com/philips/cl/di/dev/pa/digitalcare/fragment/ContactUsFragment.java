package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.bean.CdlsBean;
import com.philips.cl.di.dev.pa.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.pa.digitalcare.listners.FragmentUtilityInterface;
import com.philips.cl.di.dev.pa.digitalcare.listners.LongRunningTaskInterface;
import com.philips.cl.di.dev.pa.digitalcare.social.TwitterAuth;
import com.philips.cl.di.dev.pa.digitalcare.social.TwitterConnect;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;
import com.philips.cl.di.dev.pa.digitalcare.util.LongRunningTask;
import com.philips.cl.di.dev.pa.digitalcare.util.ParserController;
import com.philips.cl.di.dev.pa.digitalcare.util.Utils;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2015
 */
public class ContactUsFragment extends DigitalCareBaseFragment implements
		TwitterAuth, FragmentUtilityInterface {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private DigitalCareFontButton mFacebook = null;
	private DigitalCareFontButton mTwitter = null;
	private DigitalCareFontButton mChat = null;
	private DigitalCareFontButton mEmail = null;
	private DigitalCareFontButton mCallPhilips = null;
	private TwitterAuth mTwitterAuth = this;
	private ParserController mParserController = null;
	private CdlsBean cdlsBean = null;
	private TextView mFirstRowText = null;
	private TextView mSecondRowText = null;
	private TextView mContactUsOpeningHours = null;
	private String mCdlsResponse = null;

	// CDLS related
	private LongRunningTask mLongRunningTask = null;
//	private static final String mURL = "http://www.philips.com/prx/cdls/B2C/de_DE/CARE/PERSONAL_CARE_GR.querytype.(fallback)";
	private static final String mURL = "http://www.philips.com/prx/cdls/B2C/en_GB/CARE/PERSONAL_CARE_GR.querytype.(fallback)";

	private static final String TAG = ContactUsFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (!Utils.isConnected(getActivity())) {
			return null;
		}
		/*
		 * CDLS execution
		 */
		mLongRunningTask = new LongRunningTask(mURL, mLongRunningInterface);
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
		if (!Utils.isConnected(getActivity())) {
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
		ALog.d(TAG, "Twitter Authentication Failed");

	}

	@Override
	public void onTwitterLoginSuccessful() {
		Toast.makeText(getActivity(), "Logged in Successfully",
				Toast.LENGTH_SHORT).show();
		showFragment(new TwitterScreenFragment());
	}

	private LongRunningTaskInterface mLongRunningInterface = new LongRunningTaskInterface() {

		@Override
		public void responseReceived(String response) {
			ALog.i(TAG, "response : " + response);
			if (response != null) {
				mCdlsResponse = response;
				mParserController = ParserController
						.getParserControllInstance(getActivity());
				mParserController.extractCdlsValues(response);
				cdlsBean = mParserController.getCdlsBean();
				if (cdlsBean != null) {
					if (cdlsBean.getSuccess()) {
						enableBottomText();
						mCallPhilips.setText(getResources().getString(
								R.string.call)
								+ " " + cdlsBean.getPhone().getPhoneNumber());
						mFirstRowText.setText(cdlsBean.getPhone()
								.getOpeningHoursWeekdays());
						mSecondRowText.setText(cdlsBean.getPhone()
								.getOpeningHoursSaturday());
						
						if (emptyChat(cdlsBean)) {
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
					|| cdlsBean.getChat().getContent()
							.equalsIgnoreCase("");
		}
	};

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:"
				+ cdlsBean.getPhone().getPhoneNumber()));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);
	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.contactUsChat && Utils.isConnected(getActivity())) {
			if (mCdlsResponse == null) {
				Toast.makeText(getActivity(), "No server response",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (cdlsBean != null && !cdlsBean.getSuccess()) {
				Toast.makeText(getActivity(),
						cdlsBean.getError().getErrorMessage(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			showFragment(new ChatFragment());
		} else if (id == R.id.contactUsCall) {
			if (mCdlsResponse == null) {
				Toast.makeText(getActivity(), "No server response",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (cdlsBean != null && !cdlsBean.getSuccess()) {
				Toast.makeText(getActivity(),
						cdlsBean.getError().getErrorMessage(),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (Utils.isSimAvailable(getActivity())) {
				callPhilips();
			} else if (!Utils.isSimAvailable(getActivity())) {
				Toast.makeText(getActivity(), "Check the SIM",
						Toast.LENGTH_SHORT).show();
			}
		} else if (id == R.id.socialLoginFacebookBtn
				&& Utils.isConnected(getActivity())) {
			//showFragment(new FacebookScreenFragment());
			showFragment(new FacebookWeb());
		} else if (id == R.id.socialLoginTwitterBtn
				&& Utils.isConnected(getActivity())) {
			TwitterConnect mTwitter = TwitterConnect.getInstance(getActivity());
			mTwitter.initSDK(mTwitterAuth);
		} else if (id == R.id.contactUsEmail) {
			Utils.sendEmail(getActivity());
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
}
