package com.philips.cl.di.digitalcare.social;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.philips.cl.di.digitalcare.customview.DigitalCareProductImageSelectorView;
import com.philips.cl.di.digitalcare.customview.TabletPopupWindow;
import com.philips.cl.di.digitalcare.customview.TabletPopupWindow.AlignMode;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

public class ImageTabletPick implements OnClickListener {

	private final String TAG = ImageTabletPick.class.getSimpleName();
	private Activity mContext = null;
	private final int CANCEL_BUTTON = 1;
	private final int LIBRARY_BUTTON = 3;
	private final int CAMERA_BUTTON = 4;

	private View mCameraImagePick;
	private View mLibraryImagePick;
	private View mCancelDialog;

	TabletPopupWindow mPointerAlert = null;

	public ImageTabletPick(Activity activity) {
		mContext = activity;
		getPointerAlert();
	}

	public TabletPopupWindow getPointerAlert() {
		mPointerAlert = new TabletPopupWindow(mContext, 400);
		DigitalCareProductImageSelectorView mImageSelectorView = new DigitalCareProductImageSelectorView(
				mContext);
		View mView = mImageSelectorView.getTabletProductImageMenuView();
		mPointerAlert.setContentView(mView);
		mPointerAlert.setBackgroundDrawable(new ColorDrawable(Color.RED));
		mCameraImagePick = mView.findViewById(mImageSelectorView
				.getCameraButtonID());
		mLibraryImagePick = mView.findViewById(mImageSelectorView
				.getLIbraryButtonID());
		mCancelDialog = mView.findViewById(mImageSelectorView
				.getCancelButtonID());
		setListenersToMenuOptions();
		return mPointerAlert;
	}

	private void setListenersToMenuOptions() {
		mCameraImagePick.setOnClickListener(this);
		mLibraryImagePick.setOnClickListener(this);
		mCancelDialog.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case CANCEL_BUTTON:
			DLog.d("IMAGE", "Cancel Button");

			break;
		case LIBRARY_BUTTON:
			DLog.d("IMAGE", "Library Button");

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			mContext.startActivityForResult(
					Intent.createChooser(intent, "Complete action using"),
					DigitalCareContants.IMAGE_PICK);
			break;
		case CAMERA_BUTTON:
			DLog.d("IMAGE", "Camera Button");

			Intent mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mContext.startActivityForResult(mCameraIntent,
					DigitalCareContants.IMAGE_CAPTURE);
			break;
		default:
			break;
		}
	}

}
