package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.digitalcare.DigitalCareApplication;
import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;
import com.philips.cl.di.dev.pa.digitalcare.util.DigiCareContants;

/*
 *	SupportHomeFragment is the first screen of Support app.
 *	This class will give all the possible options to navigate
 *	within digital support app. 
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2015
 */
public class SupportHomeFragment extends DigitalCareBaseFragment {

	private RelativeLayout mContactUs = null;
	private RelativeLayout mProductDetails = null;
	private RelativeLayout mFaq = null;
	private RelativeLayout mFindPhilips = null;
	private RelativeLayout mWhatYouThink = null;
	private RelativeLayout mRegisterProduct = null;

	private DigitalCareFontButton mOptionBtnContactUs = null;
	private DigitalCareFontButton mOptionBtnProdDetails = null;
	private DigitalCareFontButton mOptionBtnFaq = null;
	private DigitalCareFontButton mOptionBtnFindPhilips = null;
	private DigitalCareFontButton mOptionBtnThinking = null;
	private DigitalCareFontButton mOptionBtnRegisterProduct = null;

	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;

	private static final String TAG = "SupportHomeFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_support, container,
				false);
		ALog.i(TAG, "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		for (int btnOption : DigitalCareApplication.getFeatureListKeys()) {
			enableOptionButtons(btnOption);
		}
		mOptionParent = (LinearLayout) getActivity().findViewById(
				R.id.optionParent);
		mParams = (android.widget.FrameLayout.LayoutParams) mOptionParent
				.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mOptionParent.setLayoutParams(mParams);
	}

	private void enableOptionButtons(int option) {
		switch (option) {
		case DigiCareContants.OPTION_CONTACT_US:
			mContactUs = (RelativeLayout) getActivity().findViewById(
					R.id.optionContactUs);
			mOptionBtnContactUs = (DigitalCareFontButton) getActivity().findViewById(
					R.id.optionBtnContactUs);
			mContactUs.setVisibility(View.VISIBLE);
			mOptionBtnContactUs.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_PRODUCS_DETAILS:
			mProductDetails = (RelativeLayout) getActivity().findViewById(
					R.id.optionProdDetails);
			mOptionBtnProdDetails = (DigitalCareFontButton) getActivity().findViewById(
					R.id.optionBtnProdDetails);
			mProductDetails.setVisibility(View.VISIBLE);
			mOptionBtnProdDetails.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_FAQ:
			mFaq = (RelativeLayout) getActivity().findViewById(R.id.optionFaq);
			mOptionBtnFaq = (DigitalCareFontButton) getActivity().findViewById(
					R.id.optionBtnFaq);
			mFaq.setVisibility(View.VISIBLE);
			mOptionBtnFaq.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_FIND_PHILIPS_NEARBY:
			mFindPhilips = (RelativeLayout) getActivity().findViewById(
					R.id.optionFindPhilips);
			mOptionBtnFindPhilips = (DigitalCareFontButton) getActivity().findViewById(
					R.id.optionBtnFindPhilips);
			mFindPhilips.setVisibility(View.VISIBLE);
			mOptionBtnFindPhilips.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_WHAT_ARE_YOU_THINKING:
			mWhatYouThink = (RelativeLayout) getActivity().findViewById(
					R.id.optionThinking);
			mOptionBtnThinking = (DigitalCareFontButton) getActivity().findViewById(
					R.id.optionBtnThinking);
			mWhatYouThink.setVisibility(View.VISIBLE);
			mOptionBtnThinking.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_REGISTER_PRODUCT:
			mRegisterProduct = (RelativeLayout) getActivity().findViewById(
					R.id.optionRegProd);
			mOptionBtnRegisterProduct = (DigitalCareFontButton) getActivity()
					.findViewById(R.id.optionBtnRegProd);
			mRegisterProduct.setVisibility(View.VISIBLE);
			mOptionBtnRegisterProduct
					.setOnClickListener(actionBarClickListener);
			break;
		}
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {
		CharSequence actionbarTitle = null;
		int optionSelected = -1;

		public void onClick(View view) {
			Log.i(TAG, "onClickListener view : " + view);
			int id = view.getId();
			if (id == R.id.optionBtnContactUs) {
				actionbarTitle = getResources()
						.getText(R.string.opt_contact_us);
				optionSelected = DigiCareContants.OPTION_CONTACT_US;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
			} else if (id == R.id.optionBtnFaq) {
				actionbarTitle = getResources().getText(R.string.opt_view_faq);
				optionSelected = DigiCareContants.OPTION_FAQ;
			} else if (id == R.id.optionBtnProdDetails) {
				actionbarTitle = getResources().getText(
						R.string.opt_view_product_details);
				optionSelected = DigiCareContants.OPTION_PRODUCS_DETAILS;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
			} else if (id == R.id.optionBtnFindPhilips) {
				actionbarTitle = getResources().getText(
						R.string.opt_find_philips_near_you);
				optionSelected = DigiCareContants.OPTION_FIND_PHILIPS_NEARBY;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
			} else if (id == R.id.optionBtnThinking) {
				actionbarTitle = getResources().getText(
						R.string.opt_what_you_think);
				optionSelected = DigiCareContants.OPTION_WHAT_ARE_YOU_THINKING;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
			} else if (id == R.id.optionBtnRegProd) {
				actionbarTitle = getResources().getText(
						R.string.opt_register_my_product);
				optionSelected = DigiCareContants.OPTION_REGISTER_PRODUCT;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
			} else {
				actionbarTitle = getResources().getText(
						R.string.actionbar_title_support);
			}
			// mAppObserver.setValue(actionbarTitle.toString(), optionSelected);
		}
	};
}
