package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bazaarvoice.OnBazaarResponse;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.rateandreview.productreview.BazaarVoiceWrapper;
import com.philips.cdp.digitalcare.rateandreview.productreview.model.BazaarReviewModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * This class is responsible for showing the UI for showing the preview of the end user information.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 */

public class ProductReviewPreviewFragment extends DigitalCareBaseFragment {


    private static final String TAG = ProductReviewPreviewFragment.class.getSimpleName();
    private DigitalCareFontButton mOkButton, mCancelButton = null;
    private RatingBar mRatingBar = null;
    private DigitalCareFontTextView mReviewSummaryHeader = null;
    private DigitalCareFontTextView mReviewDescription = null;
    private DigitalCareFontTextView mNickName = null;
    private DigitalCareFontTextView mEmail = null;
    private ProgressDialog mProgressDialog = null;
    private BazaarReviewModel mBazaarReviewModel = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_review_your, container,
                false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        // This Bundle value will contain BazaarReviewModel values, entered in previous screen.
        Bundle bundle = getArguments();
        mBazaarReviewModel= (BazaarReviewModel) bundle.getSerializable("productReviewModel");

        mProgressDialog = new ProgressDialog(getActivity());
        mRatingBar = (RatingBar) getActivity().findViewById(R.id.your_product_review_rating_ratingbar);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_preview_send_button);
        mCancelButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_preview_cancel_button);
        mReviewSummaryHeader = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review_summary_header_text_value);
        mReviewDescription = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review__header_text_value);
        mNickName = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review_nickname_header_text_value);
        mEmail = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review_email_header_text_value);
        mOkButton.setOnClickListener(this);

        mRatingBar.setRating((float)mBazaarReviewModel.getRating());
        mRatingBar.setEnabled(false);
        mReviewSummaryHeader.setText(mBazaarReviewModel.getSummary());
        mReviewDescription.setText(mBazaarReviewModel.getReview());
        mNickName.setText(mBazaarReviewModel.getNickname());
        mEmail.setText(mBazaarReviewModel.getEmail());
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

        String productId = "1000001";
        mBazaarReviewModel.setUserId("test1");

        if (v.getId() == (R.id.your_product_review_preview_send_button))

            //set to preview for easier testing, intention here is to submit
            BazaarVoiceWrapper.previewReview(productId, mBazaarReviewModel,
                    new OnBazaarResponse() {

                        @Override
                        public void onException(String message,
                                                Throwable exception) {
                            Log.e(TAG,
                                    "Error = "
                                            + message
                                            + "\n"
                                            + Log.getStackTraceString(exception));
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onResponse(String url, JSONObject json) {
                            Log.i(TAG, "Response = \n" + json);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
//
//                            try {
//                                if (json.getBoolean("HasErrors")) {
//                                    displayErrorMessage(json);
//                                    mProgressDialog.dismiss();
//                                } else {
//                                    Intent intent = new Intent(
//                                            getActivity(),
//                                            RatingPreviewActivity.class);
//                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                    displayImage.compress(
//                                            Bitmap.CompressFormat.PNG, 100,
//                                            stream);
//                                    byte[] byteArray = stream.toByteArray();
//                                    intent.putExtra("displayImage", byteArray);
//                                    intent.putExtra("reviewTitle",
//                                            reviewModel.getTitle());
//                                    intent.putExtra("reviewText",
//                                            reviewModel.getReviewText());
//                                    intent.putExtra("reviewNickname",
//                                            reviewModel.getNickname());
//                                    intent.putExtra("reviewRating",
//                                            reviewModel.getRating());
//                                    mProgressDialog.dismiss();
//                                    startActivity(intent);
//                                }
//                            } catch (JSONException exception) {
//                                Log.e(TAG, Log.getStackTraceString(exception));
//                            }

                        }

                    });
        mProgressDialog.setMessage("Submitting Review...");
        mProgressDialog.show();
        showFragment(new ProductReviewThankyouFragment());

    }

    private void setRatingBarUI() {
        mRatingBar = (RatingBar) getActivity().findViewById(R.id.your_product_review_rating_ratingbar);

        LayerDrawable stars = (LayerDrawable) mRatingBar
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#528E18"),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#528E18"),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#CCD9BE"),
                PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Grabs the first field error and displays it in a toast. If no form errors
     * occurred, displays a general error.
     *
     * @param json the response to the BazaarRequest
     */
    protected void displayErrorMessage(final JSONObject json) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    JSONObject formErrors = json.getJSONObject("FormErrors");
                    JSONArray errorNames = formErrors
                            .getJSONArray("FieldErrorsOrder");
                    JSONObject fieldErrors = formErrors
                            .getJSONObject("FieldErrors");
                    if (!errorNames.optString(0).equals("")) {
                        String name = errorNames.getString(0);
                        JSONObject error = fieldErrors.getJSONObject(name);
                        String message = error.getString("Message");
                        Toast.makeText(getActivity(), message,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),
                                "An error has occurred", Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (JSONException exception) {
                    Log.e(TAG, Log.getStackTraceString(exception));
                }
            }

        });
    }
}
