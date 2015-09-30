package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cdp.digitalcare.rateandreview.productreview.ProductImageLoader;
import com.philips.cdp.digitalcare.rateandreview.productreview.model.BazaarReviewModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.Locale;

/**
 * This class is responsible for showing the UI for getting the end user information before submiting the
 * product review in the Philips Page.
 *
 * @author naveen@philips.com
 * @since 15/September/2015
 */
public class ProductWriteReviewFragment extends DigitalCareBaseFragment {

    private static final String TAG = ProductWriteReviewFragment.class.getSimpleName();
    private static final String PRODUCT_TERMS_DIALOG_URL = "http://%s/content/7543b-%s/termsandconditions.htm";
    private LinearLayout mParentLayout = null;
    private FrameLayout.LayoutParams mLayoutParams = null;
    private DigitalCareFontButton mOkButton, mCancelButton = null;
    private ImageView mProductImage = null;
    private DigitalCareFontTextView mProductTitle = null;
    private DigitalCareFontTextView mProductCtn = null;
    private DigitalCareFontTextView mTermsText = null;
    private RatingBar mRatingBarVerticle, mRatingBarHorizontal = null;
    private Switch mSwitch = null;
    private EditText mSummaryHeaderEditText, mSummaryDescriptionEditText, mNicknameEditText, mEmailEditText = null;
    private DigitalCareFontTextView mSummaryErrorButton = null;
    //    private DigitalCareFontTextView mDescErrorButton = null;
    private DigitalCareFontTextView mNameErrorButton = null;
    private DigitalCareFontTextView mEmailErrorButton = null;
    private RelativeLayout mSummaryVerifiedField = null;
    //    private RelativeLayout mDescVerifiedField = null;
    private RelativeLayout mNameVerifiedField = null;
    private RelativeLayout mEmailVerifiedField = null;

    private ImageView mReviewSummaryIconInvalid = null;
    private ImageView mReviewSummaryIconValid = null;

    private ImageView mReviewNameIconInvalid = null;
    private ImageView mReviewNameIconValid = null;

    private ImageView mReviewEmailIconInvalid = null;
    private ImageView mReviewEmailIconValid = null;

    private ImageView mSummaryArrow = null;
    private final TextWatcher mTextWatcherSummary = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() > 0) {
                mReviewSummaryIconInvalid.setVisibility(View.VISIBLE);
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("testing", "onTextChanged  s : " + s.toString() + s.length());
            Log.i("testing", "onTextChanged   count : " + count);

            if (s.length() > 0) {
                mReviewSummaryIconInvalid.setVisibility(View.GONE);
                mReviewSummaryIconValid.setVisibility(View.VISIBLE);
                mSummaryErrorButton.setVisibility(View.GONE);
                mSummaryArrow.setVisibility(View.GONE);
            } else {
                mReviewSummaryIconInvalid.setVisibility(View.VISIBLE);
                mReviewSummaryIconValid.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
            Log.i("testing", "afterTextChanged   s editable  : " + s.toString());
            if (s.length() > 0) {
                mReviewSummaryIconInvalid.setVisibility(View.GONE);
                mReviewSummaryIconValid.setVisibility(View.VISIBLE);
                mSummaryErrorButton.setVisibility(View.GONE);
                mSummaryArrow.setVisibility(View.GONE);
            } else {
                mReviewSummaryIconInvalid.setVisibility(View.VISIBLE);
                mReviewSummaryIconValid.setVisibility(View.GONE);
            }
        }
    };
    private ImageView mNameArrow = null;
    private final TextWatcher mTextWatcherName = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() > 0) {
                mReviewNameIconInvalid.setVisibility(View.VISIBLE);
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("testing", "onTextChanged  s : " + s.toString() + s.length());
            Log.i("testing", "onTextChanged   count : " + count);

            if (s.length() > 0) {
                mReviewNameIconInvalid.setVisibility(View.GONE);
                mReviewNameIconValid.setVisibility(View.VISIBLE);
                mNameErrorButton.setVisibility(View.GONE);
                mNameArrow.setVisibility(View.GONE);
            } else {
                mReviewNameIconInvalid.setVisibility(View.VISIBLE);
                mReviewNameIconValid.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
            Log.i("testing", "afterTextChanged   s editable  : " + s.toString());
            if (s.length() > 0) {
                mReviewNameIconInvalid.setVisibility(View.GONE);
                mReviewNameIconValid.setVisibility(View.VISIBLE);
                mNameErrorButton.setVisibility(View.GONE);
                mNameArrow.setVisibility(View.GONE);
            } else {
                mReviewNameIconInvalid.setVisibility(View.VISIBLE);
                mReviewNameIconValid.setVisibility(View.GONE);
            }
        }
    };
    private ImageView mEmailArrow = null;
    private final TextWatcher mTextWatcherEmail = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() > 0) {
                mReviewEmailIconInvalid.setVisibility(View.VISIBLE);
            }
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("testing", "onTextChanged  s : " + s.toString() + s.length());
            Log.i("testing", "onTextChanged   count : " + count);

            if (s.length() > 0) {
                mReviewEmailIconInvalid.setVisibility(View.GONE);
                mReviewEmailIconValid.setVisibility(View.VISIBLE);
                mEmailErrorButton.setVisibility(View.GONE);
                mEmailArrow.setVisibility(View.GONE);
            } else {
                mReviewEmailIconInvalid.setVisibility(View.VISIBLE);
                mReviewEmailIconValid.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
            Log.i("testing", "afterTextChanged   s editable  : " + s.toString());
            if (s.length() > 0) {
                mReviewEmailIconInvalid.setVisibility(View.GONE);
                mReviewEmailIconValid.setVisibility(View.VISIBLE);
                mEmailErrorButton.setVisibility(View.GONE);
                mEmailArrow.setVisibility(View.GONE);
            } else {
                mReviewEmailIconInvalid.setVisibility(View.VISIBLE);
                mReviewEmailIconValid.setVisibility(View.GONE);
            }
        }
    };
    private DigitalCareFontTextView mDescTextCount = null;
    private final TextWatcher mTextWatcherDesc = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDescTextCount.setText(count + "");
        }

        public void afterTextChanged(Editable s) {
        }
    };

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
        mParentLayout = (LinearLayout) getActivity().findViewById(R.id.write_product_review_include_container);
        mLayoutParams = (FrameLayout.LayoutParams) mParentLayout
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        mOkButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_send_button);
        mCancelButton = (DigitalCareFontButton) getActivity().findViewById(R.id.your_product_review_cancel_button);
        mSummaryErrorButton = (DigitalCareFontTextView) getActivity().findViewById(R.id.tv_summary);
//        mDescErrorButton = (DigitalCareFontTextView) getActivity().findViewById(R.id.tv_desc);
        mNameErrorButton = (DigitalCareFontTextView) getActivity().findViewById(R.id.tv_name);
        mEmailErrorButton = (DigitalCareFontTextView) getActivity().findViewById(R.id.tv_email);

        mDescTextCount = (DigitalCareFontTextView) getActivity().findViewById(R.id.textcount_count);

        mSummaryVerifiedField = (RelativeLayout) getActivity().findViewById(R.id.summary_verified_field);
//        mDescVerifiedField = (RelativeLayout) getActivity().findViewById(R.id.desc_verified_field);
        mNameVerifiedField = (RelativeLayout) getActivity().findViewById(R.id.name_verified_field);
        mEmailVerifiedField = (RelativeLayout) getActivity().findViewById(R.id.email_verified_field);

        mProductImage = (ImageView) getActivity().findViewById(R.id.review_write_rate_productimage);
        mProductTitle = (DigitalCareFontTextView) getActivity().findViewById(R.id.review_write_rate_name);
        mProductCtn = (DigitalCareFontTextView) getActivity().findViewById(R.id.review_write_rate_variant);
        mTermsText = (DigitalCareFontTextView) getActivity().findViewById(R.id.review_write_rate_product_terms_termstext);
        mRatingBarVerticle = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar);
        mRatingBarHorizontal = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar_horizontal);
        mSwitch = (Switch) getActivity().findViewById(R.id.review_write_rate_product_terms_switch);
        mSummaryDescriptionEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_header_description);
        mSummaryHeaderEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_header_summary);
        mNicknameEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_nickname_header_value);
        mEmailEditText = (EditText) getActivity().findViewById(R.id.review_write_rate_product_email_header_value);

        mReviewSummaryIconInvalid = (ImageView) getActivity().findViewById(R.id.reviewSummaryIconInvalid);
        mReviewSummaryIconValid = (ImageView) getActivity().findViewById(R.id.reviewSummaryIconValid);

        mReviewNameIconInvalid = (ImageView) getActivity().findViewById(R.id.reviewNameIconInvalid);
        mReviewNameIconValid = (ImageView) getActivity().findViewById(R.id.reviewNameIconValid);

        mReviewEmailIconInvalid = (ImageView) getActivity().findViewById(R.id.reviewEmailIconInvalid);
        mReviewEmailIconValid = (ImageView) getActivity().findViewById(R.id.reviewEmailIconValid);

        mSummaryArrow = (ImageView) getActivity().findViewById(R.id.iv_up_arrow_summary);
        mNameArrow = (ImageView) getActivity().findViewById(R.id.iv_up_arrow_name);
        mEmailArrow = (ImageView) getActivity().findViewById(R.id.iv_up_arrow_email);

        mReviewSummaryIconInvalid.setOnClickListener(this);
        mReviewEmailIconInvalid.setOnClickListener(this);
        mReviewNameIconInvalid.setOnClickListener(this);

        mSummaryHeaderEditText.addTextChangedListener(mTextWatcherSummary);
        mNicknameEditText.addTextChangedListener(mTextWatcherName);
        mEmailEditText.addTextChangedListener(mTextWatcherEmail);
        mSummaryDescriptionEditText.addTextChangedListener(mTextWatcherDesc);

        mProductTitle.setText(RateThisAppFragment.mProductReviewProductName);
        mProductCtn.setText(RateThisAppFragment.mProductReviewProductCtn);
        if (RateThisAppFragment.mProductReviewProductImage != null) {
            new ProductImageLoader(RateThisAppFragment.mProductReviewProductImage, mProductImage).execute();

        }

        mOkButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mTermsText.setOnClickListener(this);
        setRatingBarUI();

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_REVIEW_WRITING,
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
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);
        params.weight = 1;
        param.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);

        RelativeLayout.LayoutParams paramErrorLabel = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        RelativeLayout.LayoutParams paramIcon = (RelativeLayout.LayoutParams) mReviewSummaryIconInvalid.getLayoutParams();
        paramIcon.topMargin = (int) (getActivity().getResources().getDimension(R.dimen.marginTopButton) +
                (getActivity().getResources().getDimension(R.dimen.icon_top_margin_right)));
        paramIcon.width = (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density);
        paramIcon.height = (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density);

        paramErrorLabel.topMargin = (int) (getActivity().getResources().getDimension(R.dimen.marginTopButton) *
                getActivity().getResources().getDimension(R.dimen.err_edit_fields_margin_top));

        RelativeLayout.LayoutParams verifiedFieldParams = (RelativeLayout.LayoutParams) mSummaryVerifiedField.getLayoutParams();
        verifiedFieldParams.height = (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density);

        mSummaryVerifiedField.setLayoutParams(verifiedFieldParams);
//        mDescVerifiedField.setLayoutParams(verifiedFieldParams);
        mNameVerifiedField.setLayoutParams(verifiedFieldParams);
        mEmailVerifiedField.setLayoutParams(verifiedFieldParams);


        mReviewSummaryIconInvalid.setLayoutParams(paramIcon);
        mReviewSummaryIconValid.setLayoutParams(paramIcon);

        mReviewNameIconInvalid.setLayoutParams(paramIcon);
        mReviewNameIconValid.setLayoutParams(paramIcon);

        mReviewEmailIconInvalid.setLayoutParams(paramIcon);
        mReviewEmailIconValid.setLayoutParams(paramIcon);

        mCancelButton.setLayoutParams(params);
        mOkButton.setLayoutParams(params);

        mSummaryHeaderEditText.setLayoutParams(param);
        mNicknameEditText.setLayoutParams(param);
        mEmailEditText.setLayoutParams(param);

        mSummaryErrorButton.setLayoutParams(paramErrorLabel);
//        mDescErrorButton.setLayoutParams(paramErrorDescLabel);
        mNameErrorButton.setLayoutParams(paramErrorLabel);
        mEmailErrorButton.setLayoutParams(paramErrorLabel);

    /*   mRatingBarHorizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.
                LayoutParams.WRAP_CONTENT));*/
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

    private void setRatingBarUI() {
        mRatingBarVerticle = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar);
        mRatingBarVerticle.setNumStars(5);
        mRatingBarVerticle.setMax(5);
        mRatingBarVerticle.setStepSize(1f);
        setRatingBarLayers(mRatingBarVerticle);
        /*LayerDrawable stars = (LayerDrawable) mRatingBarVerticle
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#528E18"),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#528E18"),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.parseColor("#CCD9BE"),
                PorterDuff.Mode.SRC_ATOP);*/

        mRatingBarHorizontal = (RatingBar) getActivity().findViewById(R.id.review_write_rate_product_ratingBar);
        mRatingBarHorizontal.setNumStars(5);
        mRatingBarHorizontal.setMax(5);
        mRatingBarHorizontal.setStepSize(1f);
        //     mRatingBarHorizontal.set
        setRatingBarLayers(mRatingBarHorizontal);


    }


    protected void setRatingBarLayers(RatingBar ratingbar) {
        LayerDrawable stars = (LayerDrawable) ratingbar
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
        return getResources().getString(R.string.bazzarvoice_productreview_writescreen_actionbar_title);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_REVIEW_WRITING;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reviewSummaryIconInvalid) {
            mSummaryErrorButton.setVisibility(View.VISIBLE);
            mSummaryArrow.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.reviewNameIconInvalid) {
            mNameErrorButton.setVisibility(View.VISIBLE);
            mNameArrow.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.reviewEmailIconInvalid) {
            mEmailErrorButton.setVisibility(View.VISIBLE);
            mEmailArrow.setVisibility(View.VISIBLE);
        } else if (v.getId() == (R.id.your_product_review_send_button)) {
            submitReview();
        } else if (v.getId() == R.id.your_product_review_cancel_button) {
            backstackFragment();
        } else if (v.getId() == (R.id.review_write_rate_product_terms_termstext)) {
            if (isConnectionAvailable())
                showEULAAlert(getTermsAndConditionsPage().toString());
        }
    }

    protected Uri getTermsAndConditionsPage() {

        Locale info = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack();
        String locale = info.toString();
        String countryFallbackUrl = LocaleMatchHandler.getPRXUrl(locale);
        String termsAndConditionsUrl = countryFallbackUrl.replace("www.", "brand-reviews.");

        return Uri.parse(String.format(PRODUCT_TERMS_DIALOG_URL, termsAndConditionsUrl, locale));
    }


    public String getCtn() {
        return DigitalCareConfigManager.getInstance().getConsumerProductInfo().getCtn();
    }

    public String getProductTitle() {
        return DigitalCareConfigManager.getInstance().getConsumerProductInfo().getProductTitle();
    }

    public String getNickNameValue(EditText editTextView) {
        return editTextView.getText().toString();
    }

    public String getEmailValue(EditText editTextView) {
        return editTextView.getText().toString();
    }

    public String getReviewSummaryValue(EditText editTextView) {
        return editTextView.getText().toString();
    }

    public String getReviewDescriptionValue(EditText editTextView) {
        return editTextView.getText().toString();
    }

    public float getRatingValue(RatingBar ratingBarView) {
        return ratingBarView.getRating();
    }

    public boolean getLegalTermValue(Switch switchView) {
        return switchView.isChecked();
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
    private void submitReview() {
        if (getRatingValue(mRatingBarVerticle) == 0) {
            Toast.makeText(getActivity(),
                    "You must give a rating between 1 and 5.",
                    Toast.LENGTH_SHORT).show();
        } else if (getReviewSummaryValue(mSummaryHeaderEditText).equals("")) {
            Toast.makeText(getActivity(), "You must enter a summary.",
                    Toast.LENGTH_SHORT).show();
        } else if (getReviewDescriptionValue(mSummaryDescriptionEditText).equals("")) {
            Toast.makeText(getActivity(), "You must enter a description.",
                    Toast.LENGTH_SHORT).show();
        } else if (getNickNameValue(mNicknameEditText).equals("")) {
            Toast.makeText(getActivity(), "You must enter a nick name.",
                    Toast.LENGTH_SHORT).show();
        } else if (getEmailValue(mEmailEditText).equals("")) {
            Toast.makeText(getActivity(), "You must enter a email.",
                    Toast.LENGTH_SHORT).show();
        } else if (!getLegalTermValue(mSwitch)) {
            Toast.makeText(getActivity(), "You must agree the term and conditions.",
                    Toast.LENGTH_SHORT).show();
        } else {

            BazaarReviewModel reviewModel = new BazaarReviewModel();
            reviewModel.setRating((float) mRatingBarVerticle.getRating());
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
