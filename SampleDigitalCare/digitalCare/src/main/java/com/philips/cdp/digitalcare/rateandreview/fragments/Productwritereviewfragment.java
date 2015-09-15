package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * This class is responsible for showing the UI for getting the end user information before submiting the
 * product review in the Philips Page.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 */
public class Productwritereviewfragment extends DigitalCareBaseFragment {

    private static final String TAG = Productwritereviewfragment.class.getSimpleName();
    private DigitalCareFontButton mOkButton = null;
    private RatingBar mRatingBar = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_review_write, container,
                false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_send_button);
        mOkButton.setOnClickListener(this);
        setRatingBarUI();
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    private void setRatingBarUI() {
        mRatingBar = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar);

        LayerDrawable stars = (LayerDrawable) mRatingBar

                .getProgressDrawable();

        stars.getDrawable(2).setColorFilter(Color.parseColor("#528E18"),

                PorterDuff.Mode.SRC_ATOP);

        stars.getDrawable(1).setColorFilter(Color.parseColor("#528E18"),
                PorterDuff.Mode.SRC_ATOP);

        stars.getDrawable(0).setColorFilter(Color.parseColor("#CCD9BE"),

                PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public String getActionbarTitle() {
        return "Write a review";
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == (R.id.your_product_review_send_button))
            Toast.makeText(getActivity(), "Previewed", Toast.LENGTH_SHORT).show();

    }
}
