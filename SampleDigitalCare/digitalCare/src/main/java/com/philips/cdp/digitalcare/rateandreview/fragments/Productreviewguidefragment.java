package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * This class is responsible for showing the guidlines for the Philips Product users before submiting the
 * product review in the Philips Page.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 */
public class Productreviewguidefragment extends DigitalCareBaseFragment {

    private static final String TAG = Productreviewguidefragment.class.getSimpleName();
    private DigitalCareFontButton mOkButton = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_productreview, container,
                false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.fragment_product_review_ok_button);
        mOkButton.setOnClickListener(this);
    }


    @Override
    public void setViewParams(Configuration config) {

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

        if (v.getId() == (R.id.fragment_product_review_ok_button))
            showFragment(new Productwritereviewfragment());


    }
}
