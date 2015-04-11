package com.philips.cl.di.digitalcare;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.contactus.ContactUsFragment;
import com.philips.cl.di.digitalcare.locatephilips.LocatePhilipsFragment;
import com.philips.cl.di.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cl.di.digitalcare.productregistration.ProductRegistrationFragment;
import com.philips.cl.di.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 * SupportHomeFragment is the first screen of Support app. This class will give
 * all the possible options to navigate within digital support app.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @creation Date : 5 Dec 2014
 */

public class SupportHomeFragment extends DigitalCareBaseFragment {

	private RelativeLayout mContactUsLayout = null;
	private RelativeLayout mProdDetailsLayout = null;
	private RelativeLayout mFaqLayout = null;
	private RelativeLayout mPhilipsNearByLayout = null;
	private RelativeLayout mRateThisAppLayout = null;
	private RelativeLayout mProductRegistrationLayout = null;

	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;

	private static final String TAG = SupportHomeFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_support, container,
				false);
		DLog.i(TAG, "onCreateView");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mOptionParent = (LinearLayout) getActivity().findViewById(
				R.id.optionParent);
		mParams = (FrameLayout.LayoutParams) mOptionParent.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);

		for (int btnOption : DigitalCareConfigManager.getFeatureListKeys()) {
			enableOptionButtons(btnOption);
		}

		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_HOME);
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

			mContactUsLayout = createButtonLayout(
					getString(R.string.opt_contact_us),
					R.drawable.support_btn_contact_us);
			break;
		case DigitalCareContants.OPTION_PRODUCS_DETAILS:
			mProdDetailsLayout = createButtonLayout(
					getString(R.string.opt_view_product_details),
					R.drawable.support_btn_product_info);
			break;
		case DigitalCareContants.OPTION_FAQ:
			mFaqLayout = createButtonLayout(getString(R.string.opt_view_faq),
					R.drawable.support_btn_read_faq);
			break;
		case DigitalCareContants.OPTION_FIND_PHILIPS_NEARBY:
			mPhilipsNearByLayout = createButtonLayout(
					getString(R.string.opt_find_philips_near_you),
					R.drawable.support_btn_find_philips);
			break;
		case DigitalCareContants.OPTION_WHAT_ARE_YOU_THINKING:
			mRateThisAppLayout = createButtonLayout(
					getString(R.string.opt_what_you_think),
					R.drawable.support_btn_tell_us);
			break;
		case DigitalCareContants.OPTION_REGISTER_PRODUCT:
			mProductRegistrationLayout = createButtonLayout(
					getString(R.string.opt_register_my_product),
					R.drawable.support_btn_register_product);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		if (view == mContactUsLayout) {
			if (Utils.isNetworkConnected(getActivity()))
				showFragment(new ContactUsFragment());
		} else if (view == mProdDetailsLayout) {
			showFragment(new ProductDetailsFragment());
		} else if (view == mPhilipsNearByLayout) {
			if (Utils.isNetworkConnected(getActivity()))
				showFragment(new LocatePhilipsFragment());
		} else if (view == mFaqLayout) {

		} else if (view == mRateThisAppLayout) {
			if (Utils.isNetworkConnected(getActivity()))
				showFragment(new RateThisAppFragment());
		} else if (view == mProductRegistrationLayout) {
			showFragment(new ProductRegistrationFragment());
		}
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.actionbar_title_support);
	}

	/**
	 * Create RelativeLayout at runTime. RelativeLayout will have button and
	 * image together.
	 */
	private RelativeLayout createButtonLayout(String buttonTitle, int resId) {
		float density = getResources().getDisplayMetrics().density;
		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		relativeLayout.setLayoutParams(params);
		relativeLayout
				.setBackgroundResource(R.drawable.selector_option_button_bg);

		Button fontButton = new Button(getActivity(), null, R.style.fontButton);
		fontButton.setGravity(Gravity.START | Gravity.CENTER);
		fontButton.setPadding((int) (80 * density), 0, 0, 0);
		fontButton.setTextAppearance(getActivity(), R.style.fontButton);

		fontButton.setText(buttonTitle);
		relativeLayout.addView(fontButton);

		RelativeLayout.LayoutParams buttonParams = (LayoutParams) fontButton
				.getLayoutParams();
		buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);

		fontButton.setLayoutParams(buttonParams);

		ImageView img = new ImageView(getActivity(), null,
				R.style.supportHomeImageButton);
		img.setPadding(0, 0, 0, 0);
		img.setContentDescription(buttonTitle);
		img.setImageDrawable(getDrawable(resId));
		relativeLayout.addView(img);

		LayoutParams imgParams = (LayoutParams) img.getLayoutParams();
		imgParams.height = (int) (35 * density);
		imgParams.width = (int) (35 * density);
		imgParams.topMargin = imgParams.bottomMargin = imgParams.rightMargin = (int) (10 * density);
		imgParams.leftMargin = (int) (19 * density);
		img.setLayoutParams(imgParams);

		mOptionParent.addView(relativeLayout);

		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
				.getLayoutParams();
		param.topMargin = (int) (15 * density);
		relativeLayout.setLayoutParams(param);
		relativeLayout.setOnClickListener(this);
		return relativeLayout;
	}

	private Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}
}
