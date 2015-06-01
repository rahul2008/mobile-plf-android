package com.philips.cl.di.digitalcare;

import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.Toast;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.contactus.ContactUsFragment;
import com.philips.cl.di.digitalcare.locatephilips.LocatePhilipsFragment;
import com.philips.cl.di.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cl.di.digitalcare.productregistration.ProductRegistrationFragment;
import com.philips.cl.di.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cl.di.digitalcare.util.DLog;
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

	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private String[] mFeatureKeys = null;
	private String[] mFeatureDwawableKey = null;

	private static final String TAG = SupportHomeFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_support, container,
				false);
		DLog.i(TAG, "onCreateView");
		initializeFeaturesSupported();
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

		for (int i = 0; i < mFeatureKeys.length; i++) {
			enableOptionButtons(mFeatureKeys[i], mFeatureDwawableKey[i]);
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

	private void enableOptionButtons(String buttonTitle, String buttonDrawable) {
		String packageName = getActivity().getPackageName();
		int title = getResources().getIdentifier(
				packageName + ":string/" + buttonTitle, null, null);
		int drawable = getResources().getIdentifier(
				packageName + ":drawable/" + buttonDrawable, null, null);
		createButtonLayout(title, drawable);
	}

	/**
	 * Create RelativeLayout at runTime. RelativeLayout will have button and
	 * image together.
	 */
	private void createButtonLayout(int buttonTitle, int resId) {
		float density = getResources().getDisplayMetrics().density;
		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) getResources().getDimension(
						R.dimen.support_btn_height));
		relativeLayout.setLayoutParams(params);

		if (resId == R.drawable.registration) {
			relativeLayout
					.setBackgroundResource(R.drawable.selector_option_prod_reg_button_bg);
		} else {
			relativeLayout
					.setBackgroundResource(R.drawable.selector_option_button_bg);
		}
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
		// img.setContentDescription(buttonTitle);
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

		/*
		 * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
		relativeLayout.setTag(buttonTitle);
		relativeLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		Integer tag = (Integer) view.getTag();

		boolean actionTaken = DigitalCareConfigManager
				.getInstance(DigitalCareConfigManager.getContext())
				.getMainMenuListener().onMainMenuItemClickListener(tag);

		if (actionTaken) {
			return;
		}

		if (tag == R.string.contact_us) {
			if (Utils.isNetworkConnected(getActivity()))
				showFragment(new ContactUsFragment());
		} else if (tag == R.string.view_product_details) {
			showFragment(new ProductDetailsFragment());
		} else if (tag == R.string.find_philips_near_you) {
			if (Utils.isNetworkConnected(getActivity()))
				showFragment(new LocatePhilipsFragment());
		} else if (tag == R.string.view_faq) {

		} else if (tag == R.string.feedback) {
			if (Utils.isNetworkConnected(getActivity()))
				showFragment(new RateThisAppFragment());
		} else if (tag == R.string.registration) {
			showFragment(new ProductRegistrationFragment());
		}
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.actionbar_title_support);
	}

	/*
	 * This method will parse, how many features are available at DigitalCare
	 * level.
	 */
	private void initializeFeaturesSupported() {
		Resources mResources = getActivity().getResources();
		mFeatureKeys = mResources.getStringArray(R.array.main_menu_title);
		mFeatureDwawableKey = mResources
				.getStringArray(R.array.main_menu_resources);
	}

	private Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}
}
