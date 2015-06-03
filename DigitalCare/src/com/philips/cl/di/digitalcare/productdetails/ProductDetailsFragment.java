package com.philips.cl.di.digitalcare.productdetails;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * ProductDetailsFragment will help to show product details.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Jan 2015
 */
public class ProductDetailsFragment extends DigitalCareBaseFragment implements
		OnClickListener {

	private static String TAG = ProductDetailsFragment.class.getSimpleName();
	private String[] mFeatureKeys = null;

	private RelativeLayout mFirstContainer = null;
	private LinearLayout.LayoutParams mFirstContainerParams = null;
	private LinearLayout.LayoutParams mSecondContainerParams = null;
	private LinearLayout mProdButtonsParent = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DLog.d(TAG, "onCreateView");
		View mView = inflater.inflate(R.layout.fragment_view_product,
				container, false);
		initializeFeaturesSupported();
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		DLog.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		mFirstContainer = (RelativeLayout) getActivity().findViewById(
				R.id.toplayout);
		mProdButtonsParent = (LinearLayout) getActivity().findViewById(
				R.id.prodbuttonsParent);

		mFirstContainerParams = (LinearLayout.LayoutParams) mFirstContainer
				.getLayoutParams();
		mSecondContainerParams = (LinearLayout.LayoutParams) mProdButtonsParent
				.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);

		for (int i = 0; i < mFeatureKeys.length; i++) {
			enableOptionButtons(mFeatureKeys[i]);
		}
		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_PRODCUT_DETAILS);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		// if (isTablet()) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginPort;
			mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
		} else {
			mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginLand;
			mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginLand;
		}
		mFirstContainer.setLayoutParams(mFirstContainerParams);
		mProdButtonsParent.setLayoutParams(mSecondContainerParams);
	}

	private void enableOptionButtons(String buttonTitle) {
		String packageName = getActivity().getPackageName();
		int title = getResources().getIdentifier(
				packageName + ":string/" + buttonTitle, null, null);
		createButtonLayout(title);
	}

	/**
	 * Create RelativeLayout at runTime. RelativeLayout will have button and
	 * image together.
	 */
	private void createButtonLayout(int buttonTitle) {
		float density = getResources().getDisplayMetrics().density;
		RelativeLayout relativeLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) getActivity().getResources()
						.getDimension(R.dimen.support_btn_height));
		relativeLayout.setLayoutParams(params);
		relativeLayout
				.setBackgroundResource(R.drawable.selector_option_prod_reg_button_bg);
		Button fontButton = new Button(getActivity(), null, R.style.fontButton);
		fontButton.setGravity(Gravity.START | Gravity.CENTER);
		fontButton.setPadding((int) 0, 0, 0, 0);
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

		mProdButtonsParent.addView(relativeLayout);

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
				.getProductMenuListener().onProductMenuItemClickListener(tag);

		if (actionTaken) {
			return;
		}

		if (tag == R.string.product_open_manual) {
			Toast.makeText(getActivity(), AnalyticsConstants.PAGE_HOME,
					Toast.LENGTH_SHORT).show();
		} else if (tag == R.string.product_download_manual) {
			Toast.makeText(getActivity(), AnalyticsConstants.PAGE_HOME,
					Toast.LENGTH_SHORT).show();
		} else if (tag == R.string.product_information) {
			Toast.makeText(getActivity(), AnalyticsConstants.PAGE_HOME,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.product_info);
	}

	/*
	 * This method will parse, how many features are available at DigitalCare
	 * level.
	 */
	private void initializeFeaturesSupported() {
		Resources mResources = getActivity().getResources();
		mFeatureKeys = mResources.getStringArray(R.array.product_menu_title);
	}
}
