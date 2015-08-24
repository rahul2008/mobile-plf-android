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
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.rateandreview.parser.ProductPageListener;
import com.philips.cdp.digitalcare.rateandreview.parser.ProductPageParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * RateThisAppFragment class is used to rate the app and review the product.
 *
 * @author: naveen@philips.com
 * @since: Jan 11, 2015
 */
public class RateThisAppFragment extends DigitalCareBaseFragment implements ProductPageListener {

    private static final String PRODUCT_REVIEW_URL = "http://%s%s/%s";
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_tellus, container,
                false);
        new ProductPageParser(this).execute();
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

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        mDividerView = (View) getActivity().findViewById(R.id.divider);
        mRatePlayStoreBtn.setOnClickListener(this);
        mRatePhilipsBtn.setTransformationMethod(null);
        mRatePlayStoreBtn.setTransformationMethod(null);
        mRatePhilipsBtn.setOnClickListener(this);

        mLayoutParams = (FrameLayout.LayoutParams) mLayoutParent
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        hideProductReviewView();

        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);

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

    /**
     * Product Review URL Page
     *
     * @return
     */
    protected Uri getUri() {
        String language = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getLanguage().toLowerCase();

        String country = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getCountry().toUpperCase();
        String countryFallBack = LocaleMatchHandler.getPRXUrl(language + "_" + country);
        DigiCareLogger.v(TAG, "Country FallBack Url : " + countryFallBack);
        DigiCareLogger.v(TAG, "Country Specific Review&Rewards Url : " + getActivity().getResources().getString(R.string.reviewandrewards));

        return Uri.parse(String.format(PRODUCT_REVIEW_URL, countryFallBack,
                mProductReviewPage, getActivity().getResources().getString(R.string.reviewandrewards)));
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
        Uri productPageUrl = getUri();
        DigiCareLogger.d(TAG, productPageUrl.toString());
        i.setData(productPageUrl);
        tagExitLisk(productPageUrl.toString());
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActionBarMenuIcon != null && mActionBarArrow != null)
            if (mActionBarMenuIcon.getVisibility() == View.VISIBLE)
                enableActionBarLeftArrow();
    }

    private void tagExitLisk(String url) {
        AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK,
                AnalyticsConstants.ACTION_KEY_EXIT_LINK, url);
    }

    private void enableActionBarLeftArrow() {
        mActionBarMenuIcon.setVisibility(View.GONE);
        mActionBarArrow.setVisibility(View.VISIBLE);
        mActionBarArrow.bringToFront();
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

    private void setButtonParams(float density) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);

        mRatePlayStoreBtn.setLayoutParams(params);
        mRatePhilipsBtn.setLayoutParams(params);
    }

    @Override
    public void onPRXProductPageReceived(String productlink) {
        if (productlink == null)
            hideProductReviewView();
        else {
            mProductReviewPage = productlink;
            showProductReviewView();
        }
    }


    /*protected String getLocalizedReviewUrl(String countryUrl) {

        String reviewAwards = null;

        switch (countryUrl) {
            case "www.philips.co.in":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.usa":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.fr":
                reviewAwards = "r/écompenses";
                break;
            case "www.philips.de":
                reviewAwards = "testberichte";
                break;
            case "www.philips.co.kr":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.nl":
                reviewAwards = "reviewenawards";
                break;
            case "www.philips.com.br":
                reviewAwards = "premios-e-reviews";
                break;
            case "www.philips.ru":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.com.tw":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.it":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.com.cn":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.pl":
                reviewAwards = "recenzje-i-nagrody";
                break;
            case "www.philips.es":
                reviewAwards = "valoracionesyresenas";
                break;
            case "www.philips.com.hk":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.dk":
                reviewAwards = "priser-og-anmeldelser";
                break;
            case "www.philips.fi":
                reviewAwards = "palkinnot-ja-arvostelut";
                break;
            case "www.philips.no":
                reviewAwards = "priser-og-anmelselser";
                break;
            case "www.philips.se":
                reviewAwards = "recensioner-och-utmarkelser";
                break;
            case "www.philips.co.uk":
                reviewAwards = "reviewandawards";
                break;
            default:
                reviewAwards = "reviewandawards";
                break;
        }
        return reviewAwards;
    }*/
}
