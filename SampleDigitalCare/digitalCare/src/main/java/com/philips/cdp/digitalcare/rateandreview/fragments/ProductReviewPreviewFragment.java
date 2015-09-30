package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bazaarvoice.OnBazaarResponse;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.rateandreview.productreview.BazaarVoiceWrapper;
import com.philips.cdp.digitalcare.rateandreview.productreview.model.BazaarReviewModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for showing the UI for showing the preview of the end user information.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 */

public class ProductReviewPreviewFragment extends DigitalCareBaseFragment {


    private static final String TAG = ProductReviewPreviewFragment.class.getSimpleName();
    private LinearLayout mParentLayout, mParentLayout1, mParentLayout2 = null;
    private LinearLayout.LayoutParams mLayoutParams, mLayoutParams1, mLayoutParams2 = null;
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
        View view = inflater.inflate(R.layout.fragment_review_your, container,
                false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        // This Bundle value will contain BazaarReviewModel values, entered in previous screen.
        Bundle bundle = getArguments();
        mBazaarReviewModel = (BazaarReviewModel) bundle.getSerializable("productReviewModel");
        mParentLayout = (LinearLayout) getActivity().findViewById(R.id.product_preview_container_three);
        mLayoutParams = (LinearLayout.LayoutParams) mParentLayout
                .getLayoutParams();
        mParentLayout1 = (LinearLayout) getActivity().findViewById(R.id.product_preview_container_one);
        mLayoutParams1 = (LinearLayout.LayoutParams) mParentLayout1
                .getLayoutParams();
        mParentLayout2 = (LinearLayout) getActivity().findViewById(R.id.product_preview_container_two);
        mLayoutParams2 = (LinearLayout.LayoutParams) mParentLayout2
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        mProgressDialog = new ProgressDialog(getActivity());
        mRatingBar = (RatingBar) getActivity().findViewById(R.id.your_product_review_rating_ratingbar);
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_preview_send_button);
        mCancelButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_preview_cancel_button);
        mReviewSummaryHeader = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review_summary_header_text_value);
        mReviewDescription = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review__header_text_value);
        mNickName = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review_nickname_header_text_value);
        mEmail = (DigitalCareFontTextView) getActivity().findViewById(R.id.your_product_review_email_header_text_value);
        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        mRatingBar.setRating((float) mBazaarReviewModel.getRating());
        mRatingBar.setEnabled(false);
        setRatingBarUI();
        mReviewSummaryHeader.setText(mBazaarReviewModel.getSummary());
        mReviewDescription.setText(mBazaarReviewModel.getReview());
        mNickName.setText(mBazaarReviewModel.getNickname());
        mEmail.setText(mBazaarReviewModel.getEmail());

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_REVIEW__PREVIEW,
                    getPreviousName());
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "IllegaleArgumentException : " + e);
        }
        setViewParams(config);
        float density = getResources().getDisplayMetrics().density;
        setButtonParams(density);
    }

    private void setButtonParams(float density) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);
        params.weight = 1;

        mCancelButton.setLayoutParams(params);
        params.leftMargin = (int) ((getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density) / 3);
        mOkButton.setLayoutParams(params);


    }

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginPort;
            mLayoutParams1.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginPort;
            mLayoutParams2.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginPort;
        } else {
            mLayoutParams.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginLand;
            mLayoutParams1.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginLand;
            mLayoutParams2.leftMargin = mLayoutParams.rightMargin = mLeftRightMarginLand;
        }
        mParentLayout.setLayoutParams(mLayoutParams);
        mParentLayout1.setLayoutParams(mLayoutParams);
        mParentLayout2.setLayoutParams(mLayoutParams);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setViewParams(newConfig);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.bazzarvoice_productreview_preview_actionbar_title);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_REVIEW__PREVIEW;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == (R.id.your_product_review_preview_cancel_button)) {
            backstackFragment();
            return;
        } else if (v.getId() == (R.id.your_product_review_preview_send_button)) {
            String productId = DigitalCareConfigManager.getInstance().getConsumerProductInfo().getCtn();

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
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onResponse(String url, JSONObject json) {
                            Log.i(TAG, "Response = \n" + json);
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }

                            try {
                                if (json.getBoolean("HasErrors")) {
                                    displayErrorMessage(json);
                                    backstackFragment();
                                } else {

                                    /* TODO: Name and email ID can have legal terms associated while tagging.
                                            As per joost suggestion, we are commenting two attributes.*/

                                    Map<String, Object> contextData = new HashMap<String, Object>();
                                    contextData.put(AnalyticsConstants.ACTION_KEY_REVIEWER_STAR_RATING, mBazaarReviewModel.getRating());
//                                    contextData.put(AnalyticsConstants.ACTION_KEY_REVIEWER_NAME, mBazaarReviewModel.getNickname());
                                    contextData.put(AnalyticsConstants.ACTION_KEY_REVIEWER_SUMMARY, mBazaarReviewModel.getSummary());
//                                    contextData.put(AnalyticsConstants.ACTION_KEY_REVIEWER_EMAIL, mBazaarReviewModel.getEmail());
                                    AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_LOCATE_PHILIPS_SEND_DATA, contextData);

                                    showFragment(new ProductReviewThankyouFragment());
                                }
                            } catch (JSONException exception) {
                                Log.e(TAG, Log.getStackTraceString(exception));
                            }
                        }
                    });
            mProgressDialog.setMessage("Submitting Review...");
            mProgressDialog.show();
        }
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
                    String name = errorNames.getString(0);
                    JSONObject error = fieldErrors.getJSONObject(name);
                    String code = error.getString("Code");
                    String message = error.getString("Message");
                    tagTechnicalError(code);

                    if (!errorNames.optString(0).equals("")) {
                        Toast.makeText(getActivity(), message,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),
                                "An error has occurred" + message, Toast.LENGTH_LONG)
                                .show();
                        Toast.makeText(getActivity(), code,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException exception) {
                    Log.e(TAG, Log.getStackTraceString(exception));
                }
            }

        });
    }

    private void tagTechnicalError(String error) {
        AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_SET_ERROR, AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
                error);
    }
}
