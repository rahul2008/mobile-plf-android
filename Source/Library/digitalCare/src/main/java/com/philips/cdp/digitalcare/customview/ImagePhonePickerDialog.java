/**
 *
 * @author naveen@philips.com
 * @description Image picker View Custom AlertView used for Phone(Not tablet) to
 *              select the user defined image/Product image to in Social Support
 *              Screens.
 * @Since March 20, 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;


public class ImagePhonePickerDialog extends Dialog implements
		View.OnClickListener {

	private final String TAG = ImagePhonePickerDialog.class.getSimpleName();
	private Activity mContext = null;

	private int mWidth = 0;
	private int mHeight = 0;

	private DigitalCareFontButton mCameraImagePick;
	private DigitalCareFontButton mLibraryImagePick;
	private DigitalCareFontButton mCancelDialog;

	private final int CANCEL_BUTTON = 1;
	private final int LIBRARY_BUTTON = 3;
	private final int CAMERA_BUTTON = 4;

	public ImagePhonePickerDialog(Activity context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		DigiCareLogger.i(TAG, "onCreate");
		setDialogDimension();
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		ProductImageSelectorView mViewObject = new ProductImageSelectorView(
				mContext);
		View mView = mViewObject.getPhoneProductMenuView();
		setContentView(mView);
		getWindow().setLayout(mWidth, (mHeight / 100) * 99);
		mCameraImagePick = (DigitalCareFontButton) mView
				.findViewById(mViewObject.getCameraButtonID());
		mLibraryImagePick = (DigitalCareFontButton) mView
				.findViewById(mViewObject.getLIbraryButtonID());
		mCancelDialog = (DigitalCareFontButton) mView.findViewById(mViewObject
				.getCancelButtonID());

		mCameraImagePick.setOnClickListener(this);
		mCameraImagePick.setTransformationMethod(null);
		mLibraryImagePick.setOnClickListener(this);
		mLibraryImagePick.setTransformationMethod(null);
		mCancelDialog.setOnClickListener(this);
		mCancelDialog.setTransformationMethod(null);

	}

	private void setDialogDimension() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		mWidth = mDisplayMetrics.widthPixels;
		mHeight = mDisplayMetrics.heightPixels;
		DigiCareLogger.d(TAG, "Weight : " + mWidth + " & Height is " + mHeight);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case CANCEL_BUTTON:
			DigiCareLogger.d("IMAGE", "Clicked on Cancel Button");
			dismiss();

			break;
		case LIBRARY_BUTTON:
			DigiCareLogger.d("IMAGE", "Clicked on Library Button");

		Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			mContext.startActivityForResult(
					Intent.createChooser(intent, null),
					DigitalCareConstants.IMAGE_PICK);
			dismiss();
			break;
		case CAMERA_BUTTON:
			DigiCareLogger.d("IMAGE", "Clicekd on Camera Button");

			Intent mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mContext.startActivityForResult(mCameraIntent,
					DigitalCareConstants.IMAGE_CAPTURE);
			dismiss();
			break;
		default:
			break;
		}
	}

}
