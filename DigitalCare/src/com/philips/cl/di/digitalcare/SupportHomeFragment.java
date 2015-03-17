package com.philips.cl.di.digitalcare;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.digitalcare.contactus.ContactUsFragment;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.locatephilips.LocatePhilipsFragment;
import com.philips.cl.di.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cl.di.digitalcare.productregistration.ProductRegistrationFragment;
import com.philips.cl.di.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

/**
 * SupportHomeFragment is the first screen of Support app. This class will give
 * all the possible options to navigate within digital support app.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @creation Date : 5 Dec 2014
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
		Log.i(TAG, "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		for (int btnOption : DigitalCareApplication.getInstance(getActivity())
				.getFeatureListKeys()) {
			enableOptionButtons(btnOption);
		}
		mOptionParent = (LinearLayout) getActivity().findViewById(
				R.id.optionParent);
		mParams = (FrameLayout.LayoutParams) mOptionParent.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	public void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mOptionParent.setLayoutParams(mParams);
	}

	private void enableOptionButtons(int option) {
		switch (option) {
		case DigitalCareContants.OPTION_CONTACT_US:
			mContactUs = (RelativeLayout) getActivity().findViewById(
					R.id.optionContactUs);
			mOptionBtnContactUs = (DigitalCareFontButton) getActivity()
					.findViewById(R.id.optionBtnContactUs);
			mContactUs.setVisibility(View.VISIBLE);
			mOptionBtnContactUs.setOnClickListener(this);
			break;
		case DigitalCareContants.OPTION_PRODUCS_DETAILS:
			mProductDetails = (RelativeLayout) getActivity().findViewById(
					R.id.optionProdDetails);
			mOptionBtnProdDetails = (DigitalCareFontButton) getActivity()
					.findViewById(R.id.optionBtnProdDetails);
			mProductDetails.setVisibility(View.VISIBLE);
			mOptionBtnProdDetails.setOnClickListener(this);
			break;
		case DigitalCareContants.OPTION_FAQ:
			mFaq = (RelativeLayout) getActivity().findViewById(R.id.optionFaq);
			mOptionBtnFaq = (DigitalCareFontButton) getActivity().findViewById(
					R.id.optionBtnFaq);
			mFaq.setVisibility(View.VISIBLE);
			mOptionBtnFaq.setOnClickListener(this);
			break;
		case DigitalCareContants.OPTION_FIND_PHILIPS_NEARBY:
			mFindPhilips = (RelativeLayout) getActivity().findViewById(
					R.id.optionFindPhilips);
			mOptionBtnFindPhilips = (DigitalCareFontButton) getActivity()
					.findViewById(R.id.optionBtnFindPhilips);
			mFindPhilips.setVisibility(View.VISIBLE);
			mOptionBtnFindPhilips.setOnClickListener(this);
			break;
		case DigitalCareContants.OPTION_WHAT_ARE_YOU_THINKING:
			mWhatYouThink = (RelativeLayout) getActivity().findViewById(
					R.id.optionThinking);
			mOptionBtnThinking = (DigitalCareFontButton) getActivity()
					.findViewById(R.id.optionBtnThinking);
			mWhatYouThink.setVisibility(View.VISIBLE);
			mOptionBtnThinking.setOnClickListener(this);
			break;
		case DigitalCareContants.OPTION_REGISTER_PRODUCT:
			mRegisterProduct = (RelativeLayout) getActivity().findViewById(
					R.id.optionRegProd);
			mOptionBtnRegisterProduct = (DigitalCareFontButton) getActivity()
					.findViewById(R.id.optionBtnRegProd);
			mRegisterProduct.setVisibility(View.VISIBLE);
			mOptionBtnRegisterProduct.setOnClickListener(this);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		Log.i(TAG, "onClickListener view : " + view);
		int id = view.getId();
		if (id == R.id.optionBtnContactUs) {
			showFragment(new ContactUsFragment());
		} else if (id == R.id.optionBtnFaq) {
		} else if (id == R.id.optionBtnProdDetails) {
			showFragment(new ProductDetailsFragment());
		} else if (id == R.id.optionBtnFindPhilips) {
			showFragment(new LocatePhilipsFragment());
		} else if (id == R.id.optionBtnThinking) {
			showFragment(new RateThisAppFragment());
		} else if (id == R.id.optionBtnRegProd) {
			showFragment(new ProductRegistrationFragment());
		}
	}

	@Override
	public String getActionbarTitle() {
		return (getResources().getString(R.string.actionbar_title_support));
	}
}
