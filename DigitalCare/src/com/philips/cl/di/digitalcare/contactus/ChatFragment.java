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
	private LinearLayout mChatButtonParent = null;
	private DigitalCareFontButton mChatNow = null;
	private DigitalCareFontButton mChatCancel = null;
	private LinearLayout.LayoutParams mChatButtonParentParams = null;

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
		mChatButtonParent = (LinearLayout) getActivity().findViewById(
				R.id.chatButtonsParent);
		mChatNow = (DigitalCareFontButton) getActivity().findViewById(
				R.id.chatNow);
		mChatNow.setOnClickListener(this);
		mChatCancel = (DigitalCareFontButton) getActivity().findViewById(
				R.id.chatCancel);
		mChatNow.setOnClickListener(this);
		mChatCancel.setOnClickListener(this);

		mChatButtonParentParams = (LinearLayout.LayoutParams) mChatButtonParent
				.getLayoutParams();
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
			mChatButtonParentParams.leftMargin = mChatButtonParentParams.rightMargin = mLeftRightMarginPort;
		} else {
			mChatButtonParentParams.leftMargin = mChatButtonParentParams.rightMargin = mLeftRightMarginLand;
		}
		mChatButtonParent.setLayoutParams(mChatButtonParentParams);
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
