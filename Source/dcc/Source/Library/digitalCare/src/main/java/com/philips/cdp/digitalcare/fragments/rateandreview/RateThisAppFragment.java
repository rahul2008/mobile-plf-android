/**
 * RateThisAppFragment class is used to rate the app and review the product.
 *
 * @author: naveen@philips.com
 * @since: Jan 11, 2015
 * <p>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.fragments.rateandreview;

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

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.fragments.rateandreview.fragments.ProductReviewFragment;
import com.philips.cdp.digitalcare.fragments.rateandreview.fragments.RateThisAppFragmentContract;
import com.philips.cdp.digitalcare.fragments.rateandreview.fragments.RateThisAppFragmentPresenter;

public class RateThisAppFragment extends DigitalCareBaseFragment implements RateThisAppFragmentContract {
    private static String TAG = RateThisAppFragment.class.getSimpleName();
    private final String APPRATER_PLAYSTORE_BROWSER_BASEURL = "https://play.google.com/store/apps/details?id=";
    private final String APPRATER_PLAYSTORE_APP_BASEURL = "market://details?id=";
    private Button mRatePlayStoreBtn = null;
    private Button mRatePhilipsBtn = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private Uri mStoreUri = null;
    private RateThisAppFragmentPresenter rateThisAppFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rateThisAppFragmentPresenter = new RateThisAppFragmentPresenter(this);
        View mView = inflater.inflate(R.layout.consumercare_fragment_tellus, container,false);
        intiView(mView);
        mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL+ getContext().getPackageName());
        return mView;
    }

    private void intiView(View view) {
        mRatePlayStoreBtn = (Button) view.findViewById(
                R.id.tellus_PlayStoreReviewButton);
        mRatePhilipsBtn = (Button) view.findViewById(
                R.id.tellus_PhilipsReviewButton);

        mActionBarMenuIcon = (ImageView) view.findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) view.findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRatePlayStoreBtn.setOnClickListener(this);
        mRatePhilipsBtn.setTransformationMethod(null);
        mRatePlayStoreBtn.setTransformationMethod(null);
        mRatePhilipsBtn.setOnClickListener(this);
        rateThisAppFragmentPresenter.handleProductData();
        rateThisAppFragmentPresenter.validateContryChina();
    }

    @Override
    public void hidePlayStoreBtn() {
        mRatePlayStoreBtn.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setViewParams(config);
    }

    private void rateThisApp() {
        mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL
                +getContext().getPackageName());

        Uri uri = Uri.parse(APPRATER_PLAYSTORE_APP_BASEURL+getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, mStoreUri));
        }
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(isConnectionAvailable()) {
            if (id == R.id.tellus_PhilipsReviewButton) {
                rateProductReview();
            } else if (id == R.id.tellus_PlayStoreReviewButton) {
                rateThisApp();
            }
        }
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.dcc_tellUs_header);
        return title;
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_RATE_THIS_APP;
    }

    @Override
    public void onPRXProductPageReceived(ViewProductDetailsModel data) {
        mRatePhilipsBtn.setVisibility(data.getProductInfoLink() ==null? View.GONE: View.VISIBLE);
    }
}
