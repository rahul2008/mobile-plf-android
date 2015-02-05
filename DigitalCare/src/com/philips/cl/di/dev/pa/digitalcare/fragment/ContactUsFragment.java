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

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2015
 */
public class ContactUsFragment extends BaseFragment {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private FontButton mFacebook = null;
	private FontButton mChat = null;
	private FontButton mCallPhilips = null;

	private static final String TAG = "ContactUsFragment";

//	private static FacebookUtility mFacebookUtility = null;
//	private static Bundle mSaveInstanceState = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		mSaveInstanceState = savedInstanceState;
		View view = inflater.inflate(R.layout.fragment_contact_us, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mConactUsParent = (LinearLayout) getActivity().findViewById(
				R.id.contactUsParent);
		mChat = (FontButton) getActivity().findViewById(R.id.contactUsChat);
		mFacebook = (FontButton) getActivity().findViewById(
				R.id.socialLoginFacebookBtn);
		mCallPhilips = (FontButton) getActivity().findViewById(
				R.id.contactUsCall);

		mFacebook.setOnClickListener(actionBarClickListener);
		mChat.setOnClickListener(actionBarClickListener);
		mCallPhilips.setOnClickListener(actionBarClickListener);

		mParams = (FrameLayout.LayoutParams) mConactUsParent.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

//	public void onActivityResultFragment(Activity activity, int requestCode,
//			int resultCode, Intent data) {
//		if (mFacebookUtility != null) {
//			mFacebookUtility.onActivityResultFragment(activity, requestCode,
//					resultCode, data);
//		}
//	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
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
//				 mFacebookUtility = new FacebookUtility(getActivity(),
//				 mSaveInstanceState);
			}
		}
	};

//	/**
//	 * onPause of fragment.
//	 */
//	public void onPause() {
//		super.onPause();
//		if (mFacebookUtility != null) {
//			mFacebookUtility.onPause();
//		}
//	}
//
//	/**
//	 * onResume of fragment.
//	 */
//	public void onDestroy() {
//		super.onDestroy();
//		if (mFacebookUtility != null) {
//			mFacebookUtility.onDestroy();
//		}
//	}
//
//	/**
//	 * onSaveInstanceState fragment.
//	 * 
//	 * @param outState
//	 *            Bundle Object
//	 */
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//
//		if (mFacebookUtility != null) {
//			mFacebookUtility.onSaveInstanceState(outState);
//		}
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (mFacebookUtility != null) {
//			mFacebookUtility.onResume();
//		}
//	}

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
