package com.philips.cl.di.digitalcare.contactus;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

/*
 *	ChatFragment will help to provide options to start Philips chat.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2015
 */
public class ChatFragment extends DigitalCareBaseFragment {
	// private LinearLayout mChatNowParent = null;
	private DigitalCareFontButton mChatNow = null;
	// private DigitalCareFontButton mChatNowLand = null;
	private DigitalCareFontButton mChatCancel = null;
	// private DigitalCareFontButton mChatNoThanksLand = null;
	// private ImageView mImageView = null;
//	private LinearLayout.LayoutParams mChatNowParentBottom = null;
//	private LinearLayout mChatNowParentPort = null;
//	private LinearLayout mChatNowParentLand = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);
		mAppObserver.setValue(
				getActivity().getResources().getString(
						R.string.chat_with_philips),
				DigitalCareContants.OPTION_NOTHING);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// mChatNowParent = (LinearLayout) getActivity().findViewById(
		// R.id.chatNowParent);
		mChatNow = (DigitalCareFontButton) getActivity().findViewById(
				R.id.chatNow);
		mChatNow.setOnClickListener(this);
		// mChatNowLand = (DigitalCareFontButton) getActivity().findViewById(
		// R.id.chatNowLand);
		mChatCancel = (DigitalCareFontButton) getActivity().findViewById(
				R.id.chatCancel);
		mChatNow.setOnClickListener(this);
		mChatCancel.setOnClickListener(this);

		// mImageView = (ImageView) getActivity().findViewById(R.id.imageView);

//		mChatNowParentBottom = (LinearLayout.LayoutParams) mChatNowParentPort
//				.getLayoutParams();

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
		mAppObserver
				.setValue(
						getActivity().getResources().getString(
								R.string.opt_contact_us),
						DigitalCareContants.OPTION_NOTHING);
	}

	@Override
	public void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
//			mChatNowParentBottom.leftMargin = mChatNowParentBottom.rightMargin = mLeftRightMarginPort;
		} else {
//			mChatNowParentBottom.leftMargin = mChatNowParentBottom.rightMargin = mLeftRightMarginLand;
		}
//		mChatNowParentPort.setLayoutParams(mChatNowParentBottom);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.chatNow) {
			showFragment(new ChatNowFragment());
		} else if (id == R.id.chatCancel) {
			backstackFragment();
		}
	}
}
