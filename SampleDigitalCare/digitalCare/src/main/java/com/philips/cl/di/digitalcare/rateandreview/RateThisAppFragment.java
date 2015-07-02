package com.philips.cl.di.digitalcare.rateandreview;

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
import android.widget.LinearLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;

/**
 * RateThisAppFragment class is used to rate the app and review the product.
 *
 * @author: naveen@philips.com
 * @since: Jan 11, 2015
 */
public class RateThisAppFragment extends DigitalCareBaseFragment {

    private static final String PRODUCT_REVIEW_URL = "http://www.philips.co.uk%s/reviewandawards";
    private static String TAG = RateThisAppFragment.class.getSimpleName();
    ;
    private final String APPRATER_PLAYSTORE_BROWSER_BASEURL = "http://play.google.com/store/apps/details?id=";
    private final String APPRATER_PLAYSTORE_APP_BASEURL = "market://details?id=";
    private Button mRatePlayStoreBtn = null;
    private Button mRatePhilipsBtn = null;
    private LinearLayout mLayoutParent = null;
    private LinearLayout mProductReviewView = null;
    private View mDividerView = null;
    private FrameLayout.LayoutParams mLayoutParams = null;
    private Uri mStoreUri = null;
    private Uri mTagUrl = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_tellus, container,
                false);
        mStoreUri = Uri.parse(APPRATER_PLAYSTORE_BROWSER_BASEURL
                + DigitalCareConfigManager.getInstance().getContext()
                .getPackageName());
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
        mDividerView = (View) getActivity().findViewById(R.id.divider);
        mRatePlayStoreBtn.setOnClickListener(this);
        mRatePhilipsBtn.setTransformationMethod(null);
        mRatePlayStoreBtn.setTransformationMethod(null);
        mRatePhilipsBtn.setOnClickListener(this);

        mLayoutParams = (FrameLayout.LayoutParams) mLayoutParent
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        if (null == getProductReviewPRXUrl())
            hideProductReviewView();

        setViewParams(config);
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_RATE_THIS_APP,
                getPreviousName());
    }

    protected String getProductReviewPRXUrl() {
        return DigitalCareConfigManager.getInstance().getConsumerProductInfo()
                .getProductReviewUrl();
    }

    protected void hideProductReviewView() {
        mProductReviewView.setVisibility(View.GONE);
        mDividerView.setVisibility(View.GONE);
    }

    protected Uri getUri() {
        return Uri.parse(String.format(PRODUCT_REVIEW_URL,
                getProductReviewPRXUrl()));
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
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(getUri());
        startActivity(i);
    }

    private void tagExitLisk(String url) {
        AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK,
                AnalyticsConstants.ACTION_KEY_EXIT_LINK, url);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tellus_PhilipsReviewButton) {
            tagExitLisk(getUri().toString());
            if (isConnectionAvailable())
                rateProductReview();
        } else if (id == R.id.tellus_PlayStoreReviewButton) {
            if (isConnectionAvailable())
                rateThisApp();
        }
    }

    ;

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
}
