package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.pa.digitalcare.social.TwitterAuth;
import com.philips.cl.di.dev.pa.digitalcare.social.TwitterConnect;
import com.philips.cl.di.dev.pa.digitalcare.util.FragmentUtility;
import com.philips.cl.di.dev.pa.digitalcare.util.Utils;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com, naveen@philips.com
 * 
 * Creation Date : 19 Jan 2015
 */
public class ContactUsFragment extends BaseFragment implements TwitterAuth,
		FragmentUtility {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private DigitalCareFontButton mFacebook, mTwitter = null;
	private DigitalCareFontButton mChat, mEmail, mCallPhilips = null;
	private TwitterAuth mTwitterAuth = this;

	private static final String TAG = ContactUsFragment.class.getSimpleName();

	// private static FacebookUtility mFacebookUtility = null;
	// private static Bundle mSaveInstanceState = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// mSaveInstanceState = savedInstanceState;
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
		Log.d(TAG, "Twitter Authentication Failed");

	}

	@Override
	public void onTwitterLoginSuccessful() {
		Toast.makeText(getActivity(), "Logged in Successfully",
				Toast.LENGTH_SHORT).show();
		showFragment(new TwitterScreenFragment());
	}

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:" + "9986202179"));
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
