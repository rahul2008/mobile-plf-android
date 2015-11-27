/*
package com.philips.cdp.digitalcare.rateandreview.productreview.model;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

*/
/**
 * BazaarReviewModel.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is a very basic class used to represent a review in the Bazaarvoice
 * system for the use of this example app.
 * 
 * <p>
 * <b>Note: This does not include all functionality of the Bazaarvoice
 * system.</b>
 * 
 * <p>
 * Copyright (c) 2012 BazaarVoice. All rights reserved.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 11 Sep 2015
 *//*

public class BazaarReviewModel implements Serializable{
	private float rating;
	private String title;
	private String authorId;
	private String userNickname;
	private String dateString;
	private String reviewText;
	private String imageUrl;
	private Bitmap imageBitmap;
    private String mEmail;
    private String mSummary;
    private String mReview;
    private String mUserId;
    private boolean mTermAndConditionAccepted = false;

    */
/**
	 * Sets all variables to 0, null, or "null"
	 *//*

	public BazaarReviewModel() {
		rating = 0;
		title = "null";
		authorId = "null";
		userNickname = "null";
		dateString = "null";
		reviewText = "null";
		imageUrl = "null";
		imageBitmap = null;
	}

	*/
/**
	 * Parses the json response of a review query and builds the structure of
	 * the object.
	 * 
	 * @param json
	 *            the response of a review query
	 * @throws NumberFormatException
	 *             if the rating is not formatted as an <code>int</code> (this
	 *             should never occur)
	 * @throws org.json.JSONException
	 *             if there is a missing field in the json response
	 *//*

	public BazaarReviewModel(JSONObject json) throws NumberFormatException,
			JSONException {
		String ratingText = json.getString("Rating");
		if (!"null".equals(ratingText))
			rating = Integer.parseInt(ratingText);
		else
			rating = 0;

		title = json.getString("Title");
		authorId = json.optString("AuthorId");
		userNickname = json.optString("UserNickname");
		dateString = formatDateString(json.getString("SubmissionTime"));
		reviewText = json.getString("ReviewText");

		JSONArray photos = json.optJSONArray("Photos");
		if (photos != null && photos.length() != 0) {
			Log.i("Photo", photos.toString());
			JSONObject photo = photos.getJSONObject(0);
			JSONObject sizes = photo.getJSONObject("Sizes");
			JSONObject thumb = sizes.getJSONObject("thumbnail");
			imageUrl = thumb.getString("Url");
		} else {
			imageUrl = "";
		}
		imageBitmap = null;
	}

	*/
/**
	 * Formats the date string from a json response into a readable format.
	 * 
	 * <p>
	 * Assumes "yyyy-mm-ddThh:mm:ss.000-06:00" format
	 * 
	 * @param timestamp
	 *            the timestamp string from the json response
	 * @return the formatted string
	 *//*

	private String formatDateString(String timestamp) {
		String year = timestamp.substring(0, 4);
		int monthNum = Integer.parseInt(timestamp.substring(5, 7));
		String day = timestamp.substring(8, 10);
		String month = "";

		switch (monthNum) {
		case 1:
			month = "January";
			break;
		case 2:
			month = "February";
			break;
		case 3:
			month = "March";
			break;
		case 4:
			month = "April";
			break;
		case 5:
			month = "May";
			break;
		case 6:
			month = "June";
			break;
		case 7:
			month = "July";
			break;
		case 8:
			month = "August";
			break;
		case 9:
			month = "September";
			break;
		case 10:
			month = "October";
			break;
		case 11:
			month = "November";
			break;
		case 12:
			month = "December";
			break;
		}

		return month + " " + day + ", " + year;

	}


	*/
/**
	 * @return the rating
	 *//*

	public float getRating() {
		return rating;
	}

	*/
/**
	 * @param rating
	 *            the rating to set
	 *//*

	public void setRating(float rating) {
		this.rating = rating;
	}

	*/
/**
	 * @return the title
	 *//*

	public String getTitle() {
		return title;
	}

	*/
/**
	 * @param title
	 *            the title to set
	 *//*

	public void setTitle(String title) {
		this.title = title;
	}

    public String getReview() {
        return mReview;
    }

    */
/**
     * @param review
     *            the title to set
     *//*

    public void setReview(String review) {
        mReview = review;
    }

    public String getSummary() {
        return mSummary;
    }

    */
/**
     * @param summary
     *            the title to set
     *//*

    public void setSummary(String summary) {
        mSummary = summary;
    }

    */
/**
     * @param email
     *            the title to set
     *//*

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

	*/
/**
	 * @return the authorId
	 *//*

	public String getAuthorId() {
		return authorId;
	}

	*/
/**
	 * @param authorId
	 *            the authorId to set
	 *//*

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	*/
/**
	 * @return the dateString
	 *//*

	public String getDateString() {
		return dateString;
	}

	*/
/**
	 * @param dateString
	 *            the dateString to set
	 *//*

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	*/
/**
	 * @return the reviewText
	 *//*

	public String getReviewText() {
		return reviewText;
	}

	*/
/**
	 * @param reviewText
	 *            the reviewText to set
	 *//*

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

    */
/**
     * @return the mUserId
     *//*

    public String getUserId() {
        return mUserId;
    }

    */
/**
     * @param userID
     *            the reviewText to set
     *//*

    public void setUserId(String userID) {
        mUserId = userID;
    }

    */
/**
	 * @return the imageBitmap
	 *//*

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	*/
/**
	 * @param imageBitmap
	 *            the imageBitmap to set
	 *//*

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	*/
/**
	 * @return the userNickname
	 *//*

	public String getNickname() {
		return userNickname;
	}

	*/
/**
	 * @param userNickname
	 *            the userNickname to set
	 *//*

	public void setNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	*/
/**
	 * Gets the correct name for display.
	 * 
	 * @return nickname if it exists, else the author ID
	 *//*

	public String getDisplayName() {
		if (userNickname == "")
			return authorId;
		else
			return userNickname;
	}

    public boolean getTermsAndConditionStatus(){
        return mTermAndConditionAccepted;
    }

    public void setTermsAndConditionAccepted(boolean termAndCond){
        mTermAndConditionAccepted = termAndCond;
    }

}
*/
