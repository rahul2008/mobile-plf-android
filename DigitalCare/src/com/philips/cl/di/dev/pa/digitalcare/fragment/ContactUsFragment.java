package com.philips.cl.di.dev.pa.digitalcare.fragment;

import org.json.JSONException;
import org.json.JSONObject;

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
	private CdlsBean cdlsBean = null;
	private TextView mFirstRowText = null;
	private TextView mSecondRowText = null;

	// CDLS related
	private LongRunningTask mLongRunningTask = null;
	private static final String mURL = "http://www.philips.com/prx/cdls/B2C/en_GB/CARE/PERSONAL_CARE_GR.querytype.(fallback)";

	private static final String TAG = ContactUsFragment.class.getSimpleName();

	// private static FacebookUtility mFacebookUtility = null;
	// private static Bundle mSaveInstanceState = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// mSaveInstanceState = savedInstanceState;
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
		mFirstRowText = (TextView) getActivity()
				.findViewById(R.id.firstRowText);
		mSecondRowText = (TextView) getActivity().findViewById(
				R.id.secondRowText);

		mFacebook.setOnClickListener(this);
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
				// mEditText.setText(response);
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);
					boolean successValue = jsonObject.optBoolean("success");

					ALog.i(TAG, "response : " + response);

					cdlsBean = ParserController.extractCdlsValues(successValue,
							jsonObject);

					mCallPhilips.setText(getResources()
							.getString(R.string.call)
							+ " "
							+ cdlsBean.getPhone().getPhoneNumber());

					mFirstRowText.setText(cdlsBean.getPhone()
							.getOpeningHoursWeekdays());
					mSecondRowText.setText(cdlsBean.getPhone()
							.getOpeningHoursSaturday());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:" + cdlsBean.getPhone().getPhoneNumber()));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);

	};

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.contactUsChat) {
			if (Utils.isConnected(getActivity()))
				showFragment(new ChatFragment());
		} else if (id == R.id.contactUsCall) {
			callPhilips();
		} else if (id == R.id.socialLoginFacebookBtn) {
			if (Utils.isConnected(getActivity()))
				showFragment(new FacebookScreenFragment());
		} else if (id == R.id.socialLoginTwitterBtn) {
			if (Utils.isConnected(getActivity())) {
				TwitterConnect mTwitter = TwitterConnect
						.getInstance(getActivity());
				mTwitter.initSDK(mTwitterAuth);
			}
		} else if (id == R.id.contactUsEmail) {
			if (Utils.isConnected(getActivity()))
				Utils.sendEmail(getActivity());
		}
	}

	@Override
	public void setViewParams(Configuration config) {

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mConactUsParent.setLayoutParams(mParams);

		if (!Utils.isSimAvailable(getActivity()))
			mCallPhilips.setVisibility(View.GONE);
	}
}
