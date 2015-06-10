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

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.contactus.ContactUsFragment;
import com.philips.cl.di.digitalcare.locatephilips.LocatePhilipsFragment;
import com.philips.cl.di.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cl.di.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cl.di.digitalcare.util.DLog;

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

		createMainMenu();

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

	/**
	 * Create RelativeLayout at runTime. RelativeLayout will have button and
	 * image together.
	 */
	private void createButtonLayout(String buttonTitle, String buttonDrawable) {
		float density = getResources().getDisplayMetrics().density;
		String packageName = getActivity().getPackageName();
		int title = getResources().getIdentifier(
				packageName + ":string/" + buttonTitle, null, null);
		int drawable = getResources().getIdentifier(
				packageName + ":drawable/" + buttonDrawable, null, null);

		RelativeLayout relativeLayout = createRelativeLayout(buttonTitle);

		Button button = createButton(density, title);
		relativeLayout.addView(button);
		setButtonParams(button);
		ImageView imageView = createImageView(density, drawable);
		relativeLayout.addView(imageView);
		setImageParams(imageView, density);
		mOptionParent.addView(relativeLayout);
		setRelativeLayoutParams(relativeLayout, density);

		/*
		 * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
		relativeLayout.setTag(buttonTitle);
		relativeLayout.setOnClickListener(this);
	}

	private RelativeLayout createRelativeLayout(String buttonTitle) {
		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) getActivity().getResources()
						.getDimension(R.dimen.support_btn_height));
		relativeLayout.setLayoutParams(params);

		if (buttonTitle.equalsIgnoreCase(getStringKey(R.string.registration))) {
			relativeLayout
					.setBackgroundResource(R.drawable.selector_option_prod_reg_button_bg);
		} else {
			relativeLayout
					.setBackgroundResource(R.drawable.selector_option_button_bg);
		}

		return relativeLayout;
	}

	private void setRelativeLayoutParams(RelativeLayout relativeLayout,
			float density) {
		LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
				.getLayoutParams();
		param.topMargin = (int) (15 * density);
		relativeLayout.setLayoutParams(param);
	}

	private void setImageParams(ImageView imageView, float density) {
		LayoutParams imageViewParams = (LayoutParams) imageView
				.getLayoutParams();
		imageViewParams.height = (int) (35 * density);
		imageViewParams.width = (int) (35 * density);
		imageViewParams.topMargin = imageViewParams.bottomMargin = imageViewParams.rightMargin = (int) (10 * density);
		imageViewParams.leftMargin = (int) (19 * density);
		imageView.setLayoutParams(imageViewParams);
	}

	private void setButtonParams(Button button) {
		RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
				.getLayoutParams();
		buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		button.setLayoutParams(buttonParams);
	}

	private ImageView createImageView(float density, int drawable) {
		ImageView imageView = new ImageView(getActivity(), null,
				R.style.supportHomeImageButton);
		imageView.setPadding(0, 0, 0, 0);
		imageView.setImageDrawable(getDrawable(drawable));

		return imageView;
	}

	private Button createButton(float density, int title) {
		Button button = new Button(getActivity(), null, R.style.fontButton);
		button.setGravity(Gravity.START | Gravity.CENTER);
		button.setPadding((int) (80 * density), 0, 0, 0);
		button.setTextAppearance(getActivity(), R.style.fontButton);
		button.setText(title);
		return button;
	}

	@Override
	public void onClick(View view) {

		String tag = (String) view.getTag();

		boolean actionTaken = DigitalCareConfigManager
				.getInstance(DigitalCareConfigManager.getContext())
				.getMainMenuListener()
				.onMainMenuItemClickListener(tag.toString());

		if (actionTaken) {
			return;
		}

		if (tag.equalsIgnoreCase(getStringKey(R.string.contact_us))) {
			if (isConnectionAvailable())
				showFragment(new ContactUsFragment());
		} else if (tag
				.equalsIgnoreCase(getStringKey(R.string.view_product_details))) {
			showFragment(new ProductDetailsFragment());
		} else if (tag
				.equalsIgnoreCase(getStringKey(R.string.find_philips_near_you))) {
			if (isConnectionAvailable())
				showFragment(new LocatePhilipsFragment());
		} else if (tag.equalsIgnoreCase(getStringKey(R.string.view_faq))) {

		} else if (tag.equalsIgnoreCase(getStringKey(R.string.feedback))) {
			if (isConnectionAvailable())
				showFragment(new RateThisAppFragment());
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
	private void createMainMenu() {
		Resources mResources = getActivity().getResources();
		String[] menuTitleKeys = mResources
				.getStringArray(R.array.main_menu_title);
		String[] menuDrawableKeys = mResources
				.getStringArray(R.array.main_menu_resources);
		for (int i = 0; i < menuTitleKeys.length; i++) {
			createButtonLayout(menuTitleKeys[i], menuDrawableKeys[i]);
		}
	}

	private Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	private String getStringKey(int resId) {
		return getResources().getResourceEntryName(resId);
	}
}
