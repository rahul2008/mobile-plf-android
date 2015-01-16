package com.philips.cl.di.dev.pa.digitalcare.fragment;

import java.util.Observer;

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

import com.philips.cl.di.dev.pa.digitalcare.ConfigurationManager;
import com.philips.cl.di.dev.pa.digitalcare.DigitalCareApplication;
import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;
import com.philips.cl.di.dev.pa.digitalcare.util.DigiCareContants;
import com.philips.cl.di.dev.pa.digitalcare.util.FragmentObserver;

/*
 *	SupportHomeFragment is the first screen of Support app.
 *	This class will give all the possible options to navigate
 *	within digital support app. 
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2014
 */
public class SupportHomeFragment extends BaseFragment {

	private RelativeLayout mContactUs = null;
	private RelativeLayout mProductDetails = null;
	private RelativeLayout mFaq = null;
	private RelativeLayout mFindPhilips = null;
	private RelativeLayout mWhatYouThink = null;
	private RelativeLayout mRegisterProduct = null;

	private FontButton mOptionBtnContactUs = null;
	private FontButton mOptionBtnProdDetails = null;
	private FontButton mOptionBtnFaq = null;
	private FontButton mOptionBtnFindPhilips = null;
	private FontButton mOptionBtnThinking = null;
	private FontButton mOptionBtnRegisterProduct = null;

	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;

	private static final String TAG = "SupportHomeFragment";

	private FragmentObserver mAppObserver = DigitalCareApplication
			.getAppContext().getObserver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_support, container,
				false);
		mAppObserver.addObserver((Observer) getActivity());
		ALog.i(TAG, "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ConfigurationManager configManagerinstance = ConfigurationManager
				.getInstance();
		for (int btnOption : configManagerinstance.getFeatureListKeys()) {
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
			mOptionBtnContactUs = (FontButton) getActivity().findViewById(
					R.id.optionBtnContactUs);
			mContactUs.setVisibility(View.VISIBLE);
			mOptionBtnContactUs.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_PRODUCS_DETAILS:
			mProductDetails = (RelativeLayout) getActivity().findViewById(
					R.id.optionProdDetails);
			mOptionBtnProdDetails = (FontButton) getActivity().findViewById(
					R.id.optionBtnProdDetails);
			mProductDetails.setVisibility(View.VISIBLE);
			mOptionBtnProdDetails.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_FAQ:
			mFaq = (RelativeLayout) getActivity().findViewById(R.id.optionFaq);
			mOptionBtnFaq = (FontButton) getActivity().findViewById(
					R.id.optionBtnFaq);
			mFaq.setVisibility(View.VISIBLE);
			mOptionBtnFaq.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_FIND_PHILIPS_NEARBY:
			mFindPhilips = (RelativeLayout) getActivity().findViewById(
					R.id.optionFindPhilips);
			mOptionBtnFindPhilips = (FontButton) getActivity().findViewById(
					R.id.optionBtnFindPhilips);
			mFindPhilips.setVisibility(View.VISIBLE);
			mOptionBtnFindPhilips.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_WHAT_ARE_YOU_THINKING:
			mWhatYouThink = (RelativeLayout) getActivity().findViewById(
					R.id.optionThinking);
			mOptionBtnThinking = (FontButton) getActivity().findViewById(
					R.id.optionBtnThinking);
			mWhatYouThink.setVisibility(View.VISIBLE);
			mOptionBtnThinking.setOnClickListener(actionBarClickListener);
			break;
		case DigiCareContants.OPTION_REGISTER_PRODUCT:
			mRegisterProduct = (RelativeLayout) getActivity().findViewById(
					R.id.optionRegProd);
			mOptionBtnRegisterProduct = (FontButton) getActivity()
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
			switch (view.getId()) {
			case R.id.optionBtnContactUs:
				actionbarTitle = getResources()
						.getText(R.string.opt_contact_us);
				optionSelected = DigiCareContants.OPTION_CONTACT_US;
				break;
			case R.id.optionBtnFaq:
				actionbarTitle = getResources().getText(R.string.opt_view_faq);
				optionSelected = DigiCareContants.OPTION_FAQ;
				break;
			case R.id.optionBtnProdDetails:
				actionbarTitle = getResources().getText(
						R.string.opt_view_product_details);
				optionSelected = DigiCareContants.OPTION_PRODUCS_DETAILS;
				break;
			case R.id.optionBtnFindPhilips:
				actionbarTitle = getResources().getText(
						R.string.opt_find_philips_near_you);
				optionSelected = DigiCareContants.OPTION_FIND_PHILIPS_NEARBY;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
				break;
			case R.id.optionBtnThinking:
				actionbarTitle = getResources().getText(
						R.string.opt_what_you_think);
				optionSelected = DigiCareContants.OPTION_WHAT_ARE_YOU_THINKING;
				break;
			case R.id.optionBtnRegProd:
				actionbarTitle = getResources().getText(
						R.string.opt_register_my_product);
				optionSelected = DigiCareContants.OPTION_REGISTER_PRODUCT;
				mAppObserver
						.setValue(actionbarTitle.toString(), optionSelected);
				break;
			default:
				actionbarTitle = getResources().getText(
						R.string.actionbar_title_support);
			}
			// mAppObserver.setValue(actionbarTitle.toString(), optionSelected);
		}
	};
}
