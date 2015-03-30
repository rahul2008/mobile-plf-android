package com.philips.cl.di.digitalcare.social;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.philips.cl.di.digitalcare.customview.ImageSelectorView;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

public class ImagePickerDialog extends Dialog implements View.OnClickListener {

	private final String TAG = ImagePickerDialog.class.getSimpleName();
	private Activity mContext = null;

	private int mWidth = 0;
	private int mHeight = 0;

	private View mCameraImagePick;
	private View mLibraryImagePick;
	private View mCancelDialog;

	private final int CANCEL_BUTTON = 1;
	private final int LIBRARY_BUTTON = 3;
	private final int CAMERA_BUTTON = 4;

	public ImagePickerDialog(Activity context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DLog.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setDialogDimension();
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		ImageSelectorView mViewObject = new ImageSelectorView(mContext);
		View mView = mViewObject.getPortraitView();
		setContentView(mView);
		getWindow().setLayout(mWidth, (mHeight / 100) * 99);
		mCameraImagePick = mView.findViewById(mViewObject.getCameraButtonID());
		mLibraryImagePick = mView
				.findViewById(mViewObject.getLIbraryButtonID());
		mCancelDialog = mView.findViewById(mViewObject.getCancelButtonID());

		mCameraImagePick.setOnClickListener(this);
		mLibraryImagePick.setOnClickListener(this);
		mCancelDialog.setOnClickListener(this);
	}

	private void setDialogDimension() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		mWidth = mDisplayMetrics.widthPixels;
		mHeight = mDisplayMetrics.heightPixels;
		DLog.d(TAG, "Weight : " + mWidth + " & Height is " + mHeight);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case CANCEL_BUTTON:
			DLog.d("IMAGE", "Cancel Button");
			dismiss();

			break;
		case LIBRARY_BUTTON:
			DLog.d("IMAGE", "LIbrary Button");

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			mContext.startActivityForResult(
					Intent.createChooser(intent, "Complete action using"),
					DigitalCareContants.IMAGE_PICK);
			dismiss();
			break;
		case CAMERA_BUTTON:
			DLog.d("IMAGE", "Camera Button");

			Intent mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mContext.startActivityForResult(mCameraIntent,
					DigitalCareContants.IMAGE_CAPTURE);
			dismiss();
			break;
		default:
			break;
		}
	}

}
