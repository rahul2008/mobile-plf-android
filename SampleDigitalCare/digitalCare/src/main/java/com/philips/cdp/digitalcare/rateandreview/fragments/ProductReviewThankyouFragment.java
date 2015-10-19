package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * This class is the final greeting class, Shown to the users after the successfull push of the Product review
 *
 * @author naveen@philips.com
 * @since 16/September/2015
 */
public class ProductReviewThankyouFragment extends DigitalCareBaseFragment {


    private static final String TAG = ProductReviewThankyouFragment.class.getSimpleName();
    private Button mOkButton = null;
    private LinearLayout mParentLayout = null;
    private LinearLayout.LayoutParams mLayoutParams = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_review_thankyou, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_thankyou_button);
        mParentLayout = (LinearLayout) getActivity().findViewById(R.id.product_review_thankyou_Parent_container_one);
        mLayoutParams = (LinearLayout.LayoutParams) mParentLayout
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();

        mOkButton.setOnClickListener(this);

        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_REVIEW_THANKS,
                    getPreviousName());
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "IllegaleArgumentException : " + e);
        }
    }


    private void setButtonParams(float density) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);

        mOkButton.setLayoutParams(params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setViewParams(newConfig);
    }


    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginPort;
        } else {
            mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginLand;
        }
        mParentLayout.setLayoutParams(mLayoutParams);

    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.bazzarvoice_productreview_thankyou_actionbar_title);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_REVIEW_THANKS;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == (R.id.your_product_review_thankyou_button))
            backstackToSupportFragment();

    }

}

