/**
 *
 * @author naveen@philips.com
 * @description This is Tablet Menu View used in Social Screen's to pick the
 *              Product/User opted photo's to send the locale specific Twitter
 *              Support center.
 * @Since March 15, 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.customview;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.util.DigiCareLogger;


public class ProductImageSelectorView {

	private final String TAG = ProductImageSelectorView.class.getSimpleName();
	private Activity mContext = null;
	int mOrientation = 0;
	private int mWidth = 0;
	private int mHeight = 0;

	private final int CANCEL_BUTTON = 1;
	private final int TRANSPARENT_BUTTON = 2;
	private final int LIBRARY_BUTTON = 3;
	private final int CAMERA_BUTTON = 4;
	private final int DIVIDER_VIEW = 5;

	/**
	 * 
	 * @param {@link Activity}
	 */
	public ProductImageSelectorView(Activity c) {
		mContext = c;
		mOrientation = mContext.getResources().getConfiguration().orientation;
		setDialogDimension();
	}

	/**
	 * Get the Cancel Button Menu ID.
	 */
	public int getCancelButtonID() {
		return CANCEL_BUTTON;
	}

	/**
	 * Get the Library Button Menu ID.
	 */
	public int getLIbraryButtonID() {
		return LIBRARY_BUTTON;
	}

	/**
	 * Get the Camera option Button View ID.
	 * 
	 */
	public int getCameraButtonID() {
		return CAMERA_BUTTON;
	}

	/**
	 * <p>
	 * Menu View used in the Image Picker menu of the Tablet View.
	 * </p>
	 * 
	 * @return {@link LinearGradient}
	 */
	public LinearLayout getTabletProductImageMenuView() {

		LinearLayout mPopupWindowContainer = new LinearLayout(mContext);
		LinearLayout.LayoutParams mPopwindowContainer = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindowContainer.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams mButtonParams = new LinearLayout.LayoutParams(
				-1, -2);
		DigitalCareFontButton mCameraButton = new DigitalCareFontButton(
				mContext);
		DigitalCareFontButton mGalleryButton = new DigitalCareFontButton(
				mContext);
		DigitalCareFontButton mCancelButton = new DigitalCareFontButton(
				mContext);
		mCameraButton.setId(CAMERA_BUTTON);
		mGalleryButton.setId(LIBRARY_BUTTON);
		mCancelButton.setId(CANCEL_BUTTON);
		mCancelButton.setText(mContext.getResources()
				.getString(R.string.cancel));
		mGalleryButton.setText(mContext.getResources().getString(
				R.string.choose_from_library));
		mCameraButton.setText(mContext.getResources().getString(
				R.string.take_photo));
		setColor(mCameraButton);
		setColor(mGalleryButton);
		setColor(mCancelButton);
		mPopupWindowContainer.setLayoutParams(mPopwindowContainer);
		mCameraButton.setLayoutParams(mButtonParams);
		mGalleryButton.setLayoutParams(mButtonParams);
		mCancelButton.setLayoutParams(mButtonParams);

		mPopupWindowContainer.addView(mCameraButton);
		mPopupWindowContainer.addView(mGalleryButton);
		mPopupWindowContainer.addView(mCancelButton);
		return mPopupWindowContainer;

	}

	/**
	 * Custom Image Picker Menu in Social Support Buttons. And this View is only
	 * for the Phone Menu Button(Separate type view for Tablet Menu).
	 * 
	 * @return {@link RelativeLayout}
	 */
	public RelativeLayout getPhoneProductMenuView() {
		int mCalcHeight = mHeight / 100;
		int mButtonHeight = 0;
		int mTrasparentViewHeight = 0;
		int mDividerHeight = 0;

		if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {

			mButtonHeight = mCalcHeight * 7;
			mTrasparentViewHeight = mCalcHeight * 1;
			// int mButtonTextSize = mCalcHeight * 4;
			mDividerHeight = mTrasparentViewHeight / 3;
		} else {
			mButtonHeight = mCalcHeight * 15;
			mTrasparentViewHeight = mCalcHeight * 2;
			mDividerHeight = mTrasparentViewHeight / 3;
		}

		RelativeLayout mRelativeLayout = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams mRelativeLayoutParams = new RelativeLayout.LayoutParams(
				mWidth, mHeight);
		mRelativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mRelativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mRelativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mRelativeLayoutParams.bottomMargin = mCalcHeight * 60;
		mRelativeLayout.setLayoutParams(mRelativeLayoutParams);

		DigitalCareFontButton mCancelButton = new DigitalCareFontButton(
				mContext);
		RelativeLayout.LayoutParams mCancelButtonParams = new RelativeLayout.LayoutParams(
				-1, mButtonHeight);
		mCancelButtonParams.setMargins(mTrasparentViewHeight, 0,
				mTrasparentViewHeight, 0);
		mCancelButton.setText(mContext.getResources()
				.getString(R.string.cancel));
		mCancelButton.setId(CANCEL_BUTTON);
		setTypeFace(mCancelButton);
		// setTextSize(mCancelButton, mButtonTextSize);

		setPhoneMenuBackground(mCancelButton, mCalcHeight, CANCEL_BUTTON);
		mCancelButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mCancelButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mCancelButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mCancelButtonParams.bottomMargin = mCalcHeight * 4;
		mCancelButton.setLayoutParams(mCancelButtonParams);

		RelativeLayout mTransparentView = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams mTransparentViewParams = new RelativeLayout.LayoutParams(
				-1, mTrasparentViewHeight);
		mTransparentView.setId(TRANSPARENT_BUTTON);
		mTransparentViewParams.addRule(RelativeLayout.ABOVE, CANCEL_BUTTON);
		mTransparentViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mTransparentViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mTransparentView.setLayoutParams(mTransparentViewParams);

		DigitalCareFontButton mLibraryButton = new DigitalCareFontButton(
				mContext);
		RelativeLayout.LayoutParams mLibraryButtonParams = new RelativeLayout.LayoutParams(
				-1, mButtonHeight);
		mLibraryButtonParams.setMargins(mTrasparentViewHeight, 0,
				mTrasparentViewHeight, 0);
		mLibraryButton.setId(LIBRARY_BUTTON);
		mLibraryButton.setText(mContext.getResources().getString(
				R.string.choose_from_library));
		setTypeFace(mLibraryButton);
		// setTextSize(mLibraryButton, mButtonTextSize);
		setPhoneMenuBackground(mLibraryButton, mCalcHeight, LIBRARY_BUTTON);
		mLibraryButtonParams.addRule(RelativeLayout.ABOVE, TRANSPARENT_BUTTON);
		mLibraryButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mLibraryButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mLibraryButton.setLayoutParams(mLibraryButtonParams);

		RelativeLayout mDivider = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams mDividerViewParams = new RelativeLayout.LayoutParams(
				-1, mDividerHeight);
		mDividerViewParams.setMargins(mTrasparentViewHeight, 0,
				mTrasparentViewHeight, 0);
		mDivider.setId(DIVIDER_VIEW);
		mDivider.setBackgroundColor(Color.WHITE);

		mDividerViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mDividerViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mDividerViewParams.addRule(RelativeLayout.ABOVE, LIBRARY_BUTTON);
		mDivider.setLayoutParams(mDividerViewParams);

		DigitalCareFontButton mCameraButton = new DigitalCareFontButton(
				mContext);
		setTypeFace(mCameraButton);
		RelativeLayout.LayoutParams mCameraButtonParams = new RelativeLayout.LayoutParams(
				-1, mButtonHeight);
		mCameraButtonParams.setMargins(mTrasparentViewHeight, 0,
				mTrasparentViewHeight, 0);
		mCameraButton.setId(CAMERA_BUTTON);
		setPhoneMenuBackground(mCameraButton, mCalcHeight, CAMERA_BUTTON);
		mCameraButton.setText(mContext.getResources().getString(
				R.string.take_photo));
		// setTextSize(mCameraButton, mButtonTextSize);
		mCameraButtonParams.addRule(RelativeLayout.ABOVE, DIVIDER_VIEW);
		mCameraButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mCameraButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mCameraButton.setLayoutParams(mCameraButtonParams);

		mRelativeLayout.addView(mCancelButton);
		mRelativeLayout.addView(mTransparentView);
		mRelativeLayout.addView(mLibraryButton);
		mRelativeLayout.addView(mDivider);
		mRelativeLayout.addView(mCameraButton);

		return mRelativeLayout;

	}

	/**
	 * Caluculate the Screen Dimenstion of the View wrt to orientation.
	 */
	private void setDialogDimension() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		mWidth = mDisplayMetrics.widthPixels;
		mHeight = mDisplayMetrics.heightPixels;
		DigiCareLogger.d(TAG, "Weight : " + mWidth + " & Height is " + mHeight);
	}

	/**
	 * Setting "centralesans-book" type face to Button.
	 * 
	 * @param button
	 */
	private void setTypeFace(DigitalCareFontButton button) {
		//TODO: This is giving jenkins crash.
//		Typeface mTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//				"fonts/centralesans-book.otf");
//		button.setTypeface(mTypeFace);
		setColor(button);
	}

	/**
	 * Setting Button Background Color. This Backbground color is configurable
	 * parameter.
	 * 
	 * @param button
	 */
	@SuppressWarnings("deprecation")
	private void setColor(DigitalCareFontButton button) {
		// button.setTextColor(Color.parseColor("#3BB9FF"));
		GradientDrawable mBackground = new GradientDrawable();
		mBackground.setColor(mContext.getResources().getColor(
				R.color.blue));
		button.setBackgroundDrawable(mBackground);
		button.setTextColor(mContext.getResources().getColor(R.color.button_background));
	}

	/**
	 * Setting the Button Layout Parameters to Button as per the design.
	 * 
	 * @param button
	 * @param buttonHeight
	 * @param ID
	 */
	@SuppressWarnings("deprecation")
	private void setPhoneMenuBackground(DigitalCareFontButton button,
			int buttonHeight, int ID) {
		GradientDrawable mBackground = new GradientDrawable();
		mBackground.setColor(mContext.getResources().getColor(
				R.color.blue));

		switch (ID) {
		case CANCEL_BUTTON:
			mBackground.setCornerRadius(buttonHeight);

			break;
		case CAMERA_BUTTON:
			mBackground.setCornerRadii(new float[] { buttonHeight,
					buttonHeight, buttonHeight, buttonHeight, 0, 0, 0, 0 });
			break;
		case LIBRARY_BUTTON:
			mBackground.setCornerRadii(new float[] { 0, 0, 0, 0, buttonHeight,
					buttonHeight, buttonHeight, buttonHeight });
			break;

		default:
			break;
		}

		button.setBackgroundDrawable(mBackground);
	}

}
