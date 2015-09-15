package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
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
 * This class is the final greeting class, Shown to the users after the successfull push of the Product review
 *
 * @author naveen@philips.com
 * @since 16/September/2015
 */
public class ProductreviewThankyouFragment extends DigitalCareBaseFragment {


    private static final String TAG = ProductreviewThankyouFragment.class.getSimpleName();
    private DigitalCareFontButton mOkButton = null;
    private RatingBar mRatingBar = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_review_thankyou, container,
                false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_thankyou_button);
        mOkButton.setOnClickListener(this);
    }


    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        return null;
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

        if (v.getId() == (R.id.your_product_review_thankyou_button))
            Toast.makeText(getActivity(), "Pressed ?", Toast.LENGTH_SHORT).show();

    }

}

