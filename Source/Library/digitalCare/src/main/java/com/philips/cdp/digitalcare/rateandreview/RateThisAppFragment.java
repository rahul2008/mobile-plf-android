/**
 * RateThisAppFragment class is used to rate the app and review the product.
 *
 * @author: naveen@philips.com
 * @since: Jan 11, 2015
 * <p>
 * Copyright (c) 2016 Philips. All rights reserved.
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
import android.widget.ImageView;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.rateandreview.fragments.ProductReviewFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;

public class RateThisAppFragment extends DigitalCareBaseFragment {
    private static String TAG = RateThisAppFragment.class.getSimpleName();
    private final String APPRATER_PLAYSTORE_BROWSER_BASEURL = "http://play.google.com/store/apps/details?id=";
    private final String APPRATER_PLAYSTORE_APP_BASEURL = "market://details?id=";
    private Button mRatePlayStoreBtn = null;
    private Button mRatePhilipsBtn = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private Uri mStoreUri = null;
    private Uri mTagUrl = null;
    private ViewProductDetailsModel mProductData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "Launching RateThisAppFragment Screen");
        View mView = inflater.inflate(R.layout.consumercare_fragment_tellus, container,
                false);
        mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL
                + DigitalCareConfigManager.getInstance().getContext()
                .getPackageName());
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRatePlayStoreBtn = (Button) getActivity().findViewById(
                R.id.tellus_PlayStoreReviewButton);
        mRatePhilipsBtn = (Button) getActivity().findViewById(
                R.id.tellus_PhilipsReviewButton);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        mRatePlayStoreBtn.setOnClickListener(this);
        mRatePhilipsBtn.setTransformationMethod(null);
        mRatePlayStoreBtn.setTransformationMethod(null);
        mRatePhilipsBtn.setOnClickListener(this);

        mProductData = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        if (mProductData != null)
            onPRXProductPageReceived(mProductData);

        if(Utils.isCountryChina())
        {
            mRatePlayStoreBtn.setVisibility(View.GONE);
        }

        /*AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_RATE_THIS_APP,
                getPreviousName());*/
        DigitalCareConfigManager.getInstance().getTaggingInterface().
                trackPageWithInfo(AnalyticsConstants.PAGE_RATE_THIS_APP,
                        getPreviousName(), getPreviousName());
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
        DigiCareLogger.i(TAG, "PlayStore Application Link : " + uri);
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
       /* AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK,
                AnalyticsConstants.ACTION_KEY_EXIT_LINK, url);*/
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                (AnalyticsConstants.ACTION_EXIT_LINK,
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

    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.feedback);
        DigiCareLogger.i(TAG, "Rate This App Fragment Screen title : " + title);
        return title;
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_RATE_THIS_APP;
    }

    public void onPRXProductPageReceived(ViewProductDetailsModel data) {
        mRatePhilipsBtn.setVisibility(data.getProductInfoLink() ==null? View.GONE: View.VISIBLE);
    }
}
