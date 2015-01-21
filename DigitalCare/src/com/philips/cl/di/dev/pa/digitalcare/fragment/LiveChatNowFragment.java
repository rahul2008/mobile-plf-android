package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;
import com.philips.cl.di.dev.pa.digitalcare.util.DigiCareContants;

/*
 *	LiveChatNowFragment will help to provide options to start Philips chat.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2014
 */
public class LiveChatNowFragment extends BaseFragment {
	private LinearLayout mChatNowParent = null;
	// private LinearLayout mProdRegParentSecond = null;
	private FrameLayout.LayoutParams mParams = null;
	private FontButton mChat = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_live_chat_now,
				container, false);
		mAppObserver.setValue(
				getActivity().getResources().getString(
						R.string.chat_with_philips), DigiCareContants.OPTION_NOTHING);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mChatNowParent = (LinearLayout) getActivity().findViewById(
				R.id.chatNowParent);
		mChat = (FontButton) getActivity().findViewById(R.id.contactUsChat);
		mChat.setOnClickListener(actionBarClickListener);
		// mProdRegParentSecond = (LinearLayout) getActivity().findViewById(
		// R.id.prodRegParentSecond);
		//
		mParams = (FrameLayout.LayoutParams) mChatNowParent.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		/*
		 * Updating previous screen(contact us) from here only because at
		 * contact_us screen not receiving any call back.
		 */
		mAppObserver.setValue(
				getActivity().getResources().getString(
						R.string.opt_contact_us), DigiCareContants.OPTION_NOTHING);
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
			case R.id.contactUsChat:
				showFragment(new LiveChatNowFragment());
				break;
			}
		};
	};

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mChatNowParent.setLayoutParams(mParams);
		// mProdRegParentSecond.setLayoutParams(mParams);
	}
}
