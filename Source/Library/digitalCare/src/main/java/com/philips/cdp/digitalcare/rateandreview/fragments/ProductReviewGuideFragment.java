/*
package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

*/
/**
 * This class is responsible for showing the guidlines for the Philips Product users before submiting the
 * product review in the Philips Page.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 *//*

public class ProductReviewGuideFragment extends DigitalCareBaseFragment {

    private static final String TAG = ProductReviewGuideFragment.class.getSimpleName();
    private DigitalCareFontButton mOkButton = null;
    private LinearLayout mParentLayout = null;
    private FrameLayout.LayoutParams mLayoutParams = null;
    private ImageView mFirstGuideLineImage, mSecondGuideLineImage, mThirdGuideLineImage, mFourthGuideLineImage = null;
    private DigitalCareFontTextView mFirstGuideText, mSecondGuideText, mThirdGuideText, mFourthGuideText = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.consumercare_fragment_productreview, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mParentLayout = (LinearLayout) getActivity().findViewById(R.id.productreviewguideparent);
        mLayoutParams = (FrameLayout.LayoutParams) mParentLayout
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.fragment_product_review_ok_button);

        mFirstGuideLineImage = (ImageView) getActivity().findViewById(R.id.productreview_first_expandableimage);
        mSecondGuideLineImage = (ImageView) getActivity().findViewById(R.id.productreview_second_expandableimage);
        mThirdGuideLineImage = (ImageView) getActivity().findViewById(R.id.productreview_third_expandableimage);
        mFourthGuideLineImage = (ImageView) getActivity().findViewById(R.id.productreview_fourth_expandableimage);

     */
/*   mFirstGuideText = (DigitalCareFontTextView) getActivity().findViewById(R.id.productreview_first_expandableimage_text);
        mSecondGuideText = (DigitalCareFontTextView) getActivity().findViewById(R.id.productreview_second_expandableimage_text);
        mThirdGuideText = (DigitalCareFontTextView) getActivity().findViewById(R.id.productreview_third_expandableimage_text);
        mFourthGuideText = (DigitalCareFontTextView) getActivity().findViewById(R.id.productreview_fourth_expandableimage_text);*//*


        mOkButton.setOnClickListener(this);

      */
/*  mFirstGuideLineImage.setOnClickListener(this);
        mSecondGuideLineImage.setOnClickListener(this);
        mThirdGuideLineImage.setOnClickListener(this);
        mFourthGuideLineImage.setOnClickListener(this);


        mFirstGuideText.setOnClickListener(this);
        mSecondGuideText.setOnClickListener(this);
        mThirdGuideText.setOnClickListener(this);
        mFourthGuideText.setOnClickListener(this);*//*


        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_REVIEW_GUIDE_LINE,
                    getPreviousName());
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "IllegaleArgumentException : " + e);
        }
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setViewParams(newConfig);
    }

    private void setButtonParams(float density) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                (int) ((getActivity().getResources()
                        .getDimension(R.dimen.support_btn_height))/3 * density), (int) ((getActivity().getResources()
                .getDimension(R.dimen.support_btn_height))/3 * density));


        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);


        mOkButton.setLayoutParams(params);
        mFirstGuideLineImage.setLayoutParams(imageParams);
        mSecondGuideLineImage.setLayoutParams(imageParams);
        mThirdGuideLineImage.setLayoutParams(imageParams);
        mFourthGuideLineImage.setLayoutParams(imageParams);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.bazzarvoice_productreview_writescreen_actionbar_title);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_REVIEW_GUIDE_LINE;
    }

    */
/**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     *//*

    @Override
    public void onClick(View v) {

        if (v.getId() == (R.id.fragment_product_review_ok_button))
            showFragment(new ProductWriteReviewFragment());
    }
}
*/
