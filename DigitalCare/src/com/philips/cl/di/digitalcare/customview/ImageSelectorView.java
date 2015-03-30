package com.philips.cl.di.digitalcare.customview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.util.DLog;

public class ImageSelectorView {

	private final String TAG = ImageSelectorView.class.getSimpleName();
	private Activity mContext = null;
	private int mWidth = 0;
	private int mHeight = 0;

	private final int CANCEL_BUTTON = 1;
	private final int TRANSPARENT_BUTTON = 2;
	private final int LIBRARY_BUTTON = 3;
	private final int CAMERA_BUTTON = 4;
	private final int DIVIDER_VIEW = 5;

	public ImageSelectorView(Activity c) {
		mContext = c;
		setDialogDimension();
	}

	public int getCancelButtonID() {
		return CANCEL_BUTTON;
	}

	public int getLIbraryButtonID() {
		return LIBRARY_BUTTON;
	}

	public int getCameraButtonID() {
		return CAMERA_BUTTON;
	}

	public RelativeLayout getPortraitView() {
		int mCalcHeight = mHeight / 100;
		int mButtonHeight = mCalcHeight * 7;
		int mTrasparentViewHeight = mCalcHeight * 1;
//		int mButtonTextSize = mCalcHeight * 4;
		int mDividerHeight = mTrasparentViewHeight / 3;

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
				mTrasparentViewHeight, 0 );
		mCancelButton.setText(mContext.getResources()
				.getString(R.string.cancel));
		mCancelButton.setId(CANCEL_BUTTON);
		setTypeFace(mCancelButton);
		//setTextSize(mCancelButton, mButtonTextSize);

		setBackground(mCancelButton, mCalcHeight, CANCEL_BUTTON);
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
	//	setTextSize(mLibraryButton, mButtonTextSize);
		setBackground(mLibraryButton, mCalcHeight, LIBRARY_BUTTON);
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
		setBackground(mCameraButton, mCalcHeight, CAMERA_BUTTON);
		mCameraButton.setText(mContext.getResources().getString(
				R.string.take_photo));
	//	setTextSize(mCameraButton, mButtonTextSize);
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

	private void setDialogDimension() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		mWidth = mDisplayMetrics.widthPixels;
		mHeight = mDisplayMetrics.heightPixels;
		DLog.d(TAG, "Weight : " + mWidth + " & Height is " + mHeight);
	}

	private void setTypeFace(DigitalCareFontButton button) {
		Typeface mTypeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/centralesans-book.otf");
		button.setTypeface(mTypeFace);
		setColor(button);
	}

	private void setColor(DigitalCareFontButton button) {
		button.setTextColor(Color.parseColor("#3BB9FF"));
	}

	/*private void setTextSize(DigitalCareFontButton button, float size) {
		button.setTextSize(android.R.style.TextAppearance_DeviceDefault_Small);
	}*/

	@SuppressWarnings("deprecation")
	private void setBackground(DigitalCareFontButton button, int buttonHeight,
			int ID) {
		GradientDrawable mBackground = new GradientDrawable();
		mBackground.setColor(mContext.getResources().getColor(
				R.color.activity_bg));

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
