package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.pa.digitalcare.twitter.TwitterAuth;
import com.philips.cl.di.dev.pa.digitalcare.twitter.TwitterConnect;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2015
 */
public class ContactUsFragment extends BaseFragment implements TwitterAuth {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private DigitalCareFontButton mFacebook, mTwitter = null;
	private DigitalCareFontButton mChat, mCallPhilips = null;
	private TwitterAuth mTwitterAuth = this;

	private static final String TAG = "ContactUsFragment";

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

		mFacebook.setOnClickListener(actionBarClickListener);
		mChat.setOnClickListener(actionBarClickListener);
		mCallPhilips.setOnClickListener(actionBarClickListener);
		mTwitter.setOnClickListener(actionBarClickListener);

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
		Toast.makeText(getActivity(), "Failed To Connect to Twitter",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onTwitterLoginSuccessful() {
		Toast.makeText(getActivity(), "Logged in Successfully",
				Toast.LENGTH_SHORT).show();
		showFragment(new TwitterScreenFragment());
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		public void onClick(View view) {
			int id = view.getId();
			if (id == R.id.contactUsChat) {
				showFragment(new ChatFragment());
			} else if (id == R.id.contactUsCall) {
				callPhilips();
			} else if (id == R.id.socialLoginFacebookBtn) {
				showFragment(new FacebookScreenFragment());
				// mFacebookUtility = new FacebookUtility(getActivity(),
				// mSaveInstanceState);
			} else if (id == R.id.socialLoginTwitterBtn) {
				TwitterConnect mTwitter = TwitterConnect
						.getInstance(getActivity());
				mTwitter.initSDK(mTwitterAuth);
			}
		}
	};

	// /**
	// * onPause of fragment.
	// */
	// public void onPause() {
	// super.onPause();
	// if (mFacebookUtility != null) {
	// mFacebookUtility.onPause();
	// }
	// }
	//
	// /**
	// * onResume of fragment.
	// */
	// public void onDestroy() {
	// super.onDestroy();
	// if (mFacebookUtility != null) {
	// mFacebookUtility.onDestroy();
	// }
	// }
	//
	// /**
	// * onSaveInstanceState fragment.
	// *
	// * @param outState
	// * Bundle Object
	// */
	// public void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	//
	// if (mFacebookUtility != null) {
	// mFacebookUtility.onSaveInstanceState(outState);
	// }
	// }
	//
	// @Override
	// public void onResume() {
	// super.onResume();
	// if (mFacebookUtility != null) {
	// mFacebookUtility.onResume();
	// }
	// }

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:" + "9986202179"));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);

	};

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mConactUsParent.setLayoutParams(mParams);
	}

}
