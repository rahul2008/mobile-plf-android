package com.philips.cdp.digitalcare.rateandreview.productreview;


import android.graphics.Bitmap;
import android.util.Log;

import com.bazaarvoice.BazaarEnvironment;
import com.bazaarvoice.types.*;
import com.bazaarvoice.BazaarRequest;
import com.bazaarvoice.OnBazaarResponse;
import com.bazaarvoice.SubmissionMediaParams;
import com.bazaarvoice.SubmissionParams;
import com.bazaarvoice.types.ApiVersion;
import com.philips.cdp.digitalcare.rateandreview.productreview.model.BazaarReviewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * BazaarFunctions.java <br>
 * ReviewSubmissionExample<br>

 * BazaarVoiceWrapper class is used as utility class. This is interface between BazaarVoice library and
 * GUI.

 * This is a suite of functions that leverage the BazaarvoiceSDK. This class
 * consolidates the usage of these functions for easier understanding of how to
 * use the SDK.
 * <p/>
 * Copyright (c) 2012 BazaarVoice. All rights reserved.

 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 11 Sep 2015
 */

public class BazaarVoiceWrapper {

    private static final String TAG = "BazaarFunctions";
    private static final String API_URL = "reviews.apitestcustomer.bazaarvoice.com/bvstaging";
    private static final String API_KEY = "2cpdrhohmgmwfz8vqyo48f52g";
    private static final ApiVersion API_VERSION = ApiVersion.FIVE_FOUR;
    private static final int MIN_IMAGE_DIMENSIONS = 600;

    /**
     * Submits the given review for the given product as a preview. This means
     * that it will not actually be submitted but will be tested against the API
     * and any errors will be reported.
     *
     * @param prodId   the product ID
     * @param review   the full review
     * @param listener the callback function for handling the response
     */
    public static void previewReview(String prodId, BazaarReviewModel review,
                                     OnBazaarResponse listener) {
        reviewAction(prodId, review, listener, false);
    }

    /**
     * Submits the given review for the given product as a submission. This
     * means that it will be entered into the system and be ready for display
     * soon.
     *
     * @param prodId   the product ID
     * @param review   the full review
     * @param listener the callback function for handling the response
     */
    public static void submitReview(String prodId, BazaarReviewModel review,
                                    OnBazaarResponse listener) {
        reviewAction(prodId, review, listener, true);
    }

    /**
     * Builds a review request and sends it off as either a preview or a
     * submission.
     *
     * @param prodId   the product ID
     * @param review   the full review
     * @param listener the callback function for handling the response
     * @param submit   true to submit, false to preview
     */
    private static void reviewAction(String prodId, BazaarReviewModel review,
                                     OnBazaarResponse listener, boolean submit) {
        SubmissionParams params = new SubmissionParams();
        if (submit)
            params.setAction(Action.SUBMIT);
        else
            params.setAction(Action.PREVIEW);

        params.setProductId(prodId);
        params.setRating((int)review.getRating());
        params.setTitle(review.getSummary());
        params.setReviewText(review.getReview());
        params.setUserNickname(review.getNickname());
        params.setUserEmail(review.getEmail());

        if (!review.getEmail().equals("null"))
            params.setUserId(review.getEmail());
        else if (!(review.getNickname().equals("null") || "".equals(review.getNickname().trim())))
            params.setUserId(review.getNickname());
        else
            params.setUserId("Anonymous");

        BazaarRequest submission = new BazaarRequest("clientname", API_KEY, BazaarEnvironment.staging, API_VERSION);
        submission.postSubmission(RequestType.REVIEWS, params, listener);
    }
}
