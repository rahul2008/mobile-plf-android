/**
 * RateThisAppFragment class is used to rate the app and review the product.
 *
 * @author: naveen@philips.com
 * @since: Jan 11, 2015
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.rateandreview;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.rateandreview.fragments.ProductReviewFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/*import com.philips.cdp.digitalcare.rateandreview.fragments.ProductReviewGuideFragment;
import com.philips.cdp.digitalcare.rateandreview.productreview.BazaarVoiceWrapper;*/


public class RateThisAppFragment extends DigitalCareBaseFragment {


    public static String mProductReviewProductImage = null;
    public static String mProductReviewProductName = null;
    public static String mProductReviewProductCtn = null;
    private static String TAG = RateThisAppFragment.class.getSimpleName();
    private final String APPRATER_PLAYSTORE_BROWSER_BASEURL = "http://play.google.com/store/apps/details?id=";
    private final String APPRATER_PLAYSTORE_APP_BASEURL = "market://details?id=";
    private String mProductReviewPage = null;
    private Button mRatePlayStoreBtn = null;
    private Button mRatePhilipsBtn = null;
    private LinearLayout mLayoutParent = null;
    private LinearLayout mProductReviewView = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private View mDividerView = null;
    private FrameLayout.LayoutParams mLayoutParams = null;
    private Uri mStoreUri = null;
    private Uri mTagUrl = null;
   // private boolean mBazaarVoiceReviewRequired = false;
    //  private BazaarVoiceWrapper mBazaarVoiceWrapper;
    private ViewProductDetailsModel mProductData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.consumercare_fragment_tellus, container,
                false);
        mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL
                + DigitalCareConfigManager.getInstance().getContext()
                .getPackageName());
     //   mBazaarVoiceReviewRequired = DigitalCareConfigManager.getInstance().isBazaarVoiceRequired();
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        mRatePlayStoreBtn = (DigitalCareFontButton) getActivity().findViewById(
                R.id.tellus_PlayStoreReviewButton);
        mRatePhilipsBtn = (DigitalCareFontButton) getActivity().findViewById(
                R.id.tellus_PhilipsReviewButton);
        mLayoutParent = (LinearLayout) getActivity().findViewById(
                R.id.parentLayout);
        mProductReviewView = (LinearLayout) getActivity().findViewById(
                R.id.secondParent);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        mDividerView = getActivity().findViewById(R.id.divider);
        mRatePlayStoreBtn.setOnClickListener(this);
        mRatePhilipsBtn.setTransformationMethod(null);
        mRatePlayStoreBtn.setTransformationMethod(null);
        mRatePhilipsBtn.setOnClickListener(this);

        mLayoutParams = (FrameLayout.LayoutParams) mLayoutParent
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);

        mProductData = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        if (mProductData != null)
            onPRXProductPageReceived(mProductData);


        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_RATE_THIS_APP,
                getPreviousName());
    }


    protected void hideProductReviewView() {
        mProductReviewView.setVisibility(View.GONE);
        mDividerView.setVisibility(View.GONE);
    }

    protected void showProductReviewView() {
        mProductReviewView.setVisibility(View.VISIBLE);
        mDividerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    private void rateThisApp() {

        mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL
                + DigitalCareConfigManager.getInstance().getContext()
                .getPackageName());

        Uri uri = Uri.parse(APPRATER_PLAYSTORE_APP_BASEURL
                + DigitalCareConfigManager.getInstance().getContext()
                .getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        mTagUrl = uri;
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            mTagUrl = mStoreUri;
            startActivity(new Intent(Intent.ACTION_VIEW, mStoreUri));
        }

        tagExitLisk(mTagUrl.toString());
    }

    private void rateProductReview() {
        if (isConnectionAvailable())
            showFragment(new ProductReviewFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    private void tagExitLisk(String url) {
        AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK,
                AnalyticsConstants.ACTION_KEY_EXIT_LINK, url);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tellus_PhilipsReviewButton) {
            if (isConnectionAvailable())
                rateProductReview();

        } else if (id == R.id.tellus_PlayStoreReviewButton) {
            if (isConnectionAvailable())
                rateThisApp();
        }
    }

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginPort;
        } else {
            mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginLand;
        }
        mLayoutParent.setLayoutParams(mLayoutParams);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.feedback);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_RATE_THIS_APP;
    }

    private void setButtonParams(float density) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);

        mRatePlayStoreBtn.setLayoutParams(params);
        mRatePhilipsBtn.setLayoutParams(params);
    }


    public void onPRXProductPageReceived(ViewProductDetailsModel data) {

        String productlink = data.getProductInfoLink();
        if (productlink != null /*&& mBazaarVoiceReviewRequired*/) {
            DigiCareLogger.d(TAG, "Show product review()");
            showProductReviewView();
        } else {
            DigiCareLogger.d(TAG, "Hide product review()");
            hideProductReviewView();
        }
    }
}
