package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.rateandreview.productreview.model.BazaarReviewModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * This class is responsible for showing the UI for getting the end user information before submiting the
 * product review in the Philips Page.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 */
public class ProductWriteReviewFragment extends DigitalCareBaseFragment {

    private static final String TAG = ProductWriteReviewFragment.class.getSimpleName();
    private DigitalCareFontButton mOkButton, mCancelButton = null;
    private ImageView mProductImage = null;
    private DigitalCareFontTextView mProductTitle = null;
    private DigitalCareFontTextView mProductCtn = null;
    private RatingBar mRatingBar = null;
    private Switch mSwitch = null;
    private EditText mSummaryHeaderEditText, mSummaryDescriptionEditText, mNicknameEditText, mEmailEditText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_review_write, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_send_button);
        mCancelButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_cancel_button);
        mProductImage = (ImageView) getActivity().findViewById(R.id.review_write_rate_productimage);
        mProductTitle = (DigitalCareFontTextView) getActivity().findViewById(R.id.review_write_rate_name);
        mProductCtn = (DigitalCareFontTextView) getActivity().findViewById(R.id.review_write_rate_variant);
        mRatingBar = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar);
        mSwitch = (Switch) getActivity().findViewById(R.id.review_write_rate_product_terms_switch);
        mSummaryDescriptionEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_header_description);
        mSummaryHeaderEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_header_summary);
        mNicknameEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_nickname_header_value);
        mEmailEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_email_header_value);


        mOkButton.setOnClickListener(this);
        setRatingBarUI();
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    private void setRatingBarUI() {
        mRatingBar = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar);
        mRatingBar.setNumStars(5);
        mRatingBar.setMax(5);
        mRatingBar.setStepSize(1f);
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
            submitReview();
    }


    /**
     * Does some client-side validation before calling the necessary
     * BazaarFunctions function to submit a review (only previews to facilitate
     * easier testing). When the response comes in, it launches the next
     * activity.
     * <p/>
     * If the photo has not uploaded yet, we put off submitting and show an
     * "Uploading Photo..." dialog.
     */
    protected void submitReview() {
        if (mRatingBar.getRating() == 0) {
            Toast.makeText(getActivity(),
                    "You must give a rating between 1 and 5.",
                    Toast.LENGTH_SHORT).show();
        } else if (mSummaryHeaderEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "You must enter a summary.",
                    Toast.LENGTH_SHORT).show();
        } else if (mSummaryDescriptionEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "You must enter a description.",
                    Toast.LENGTH_SHORT).show();
        } else if (mNicknameEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "You must enter a nick name.",
                    Toast.LENGTH_SHORT).show();
        } else if (mEmailEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "You must enter a email.",
                    Toast.LENGTH_SHORT).show();
        } else {

            BazaarReviewModel reviewModel = new BazaarReviewModel();
            reviewModel.setRating((float) mRatingBar.getRating());
            reviewModel.setSummary(mSummaryHeaderEditText.getText().toString());
            reviewModel.setReview(mSummaryDescriptionEditText.getText().toString());
            reviewModel.setNickname(mNicknameEditText.getText().toString());
            reviewModel.setEmail(mEmailEditText.getText().toString());

            ProductReviewPreviewFragment productReviewPreviewFragment = new ProductReviewPreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("productReviewModel", reviewModel);

            productReviewPreviewFragment.setArguments(bundle);
            showFragment(productReviewPreviewFragment);
        }
    }
}
