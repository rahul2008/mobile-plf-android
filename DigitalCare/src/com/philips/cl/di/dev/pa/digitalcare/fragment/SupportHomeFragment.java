package com.philips.cl.di.dev.pa.digitalcare.fragment;

import java.util.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.digitalcare.DigitalCareApplication;
import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;
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

	private FragmentObserver mAppObserver = DigitalCareApplication
			.getAppContext().getObserver();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_support, container,
				false);
		mAppObserver.addObserver((Observer) getActivity());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int[] keys = getResources().getIntArray(R.array.options_available);
		for (int btnOption : keys) {
			enableOptionButtons(btnOption);
		}
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

		public void onClick(View view) {
			Log.i("testing", "onClickListener view : " + view);
			switch (view.getId()) {
			case R.id.optionBtnContactUs:
				actionbarTitle = getResources()
						.getText(R.string.opt_contact_us);
				break;
			case R.id.optionBtnFaq:
				actionbarTitle = getResources().getText(R.string.opt_view_faq);
				break;
			case R.id.optionBtnProdDetails:
				actionbarTitle = getResources().getText(
						R.string.opt_view_product_details);
				break;
			case R.id.optionBtnFindPhilips:
				actionbarTitle = getResources().getText(
						R.string.opt_find_philips_near_you);
				break;
			case R.id.optionBtnThinking:
				actionbarTitle = getResources().getText(
						R.string.opt_what_you_think);
				break;
			case R.id.optionBtnRegProd:
				actionbarTitle = getResources().getText(
						R.string.opt_register_my_product);
				break;
			default:
				actionbarTitle = getResources().getText(
						R.string.actionbar_title_support);
			}
			mAppObserver.setValue(actionbarTitle.toString());
		}
	};
}
