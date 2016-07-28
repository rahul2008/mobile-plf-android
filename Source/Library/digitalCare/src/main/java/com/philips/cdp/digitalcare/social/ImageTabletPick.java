package com.philips.cdp.digitalcare.social;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.customview.ProductImageSelectorView;
import com.philips.cdp.digitalcare.customview.TabletPopupWindow;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;

/**
 * 
 * @author naveen@philips.com
 * @description Custom PopupWindow View Parameters & callback listeners.
 * @Since March 26, 2015
 */
public class ImageTabletPick implements OnClickListener {

	private final String TAG = ImageTabletPick.class.getSimpleName();
	private Activity mContext = null;
	private final int CANCEL_BUTTON = 1;
	private final int LIBRARY_BUTTON = 3;
	private final int CAMERA_BUTTON = 4;

	private DigitalCareFontButton mCameraImagePick;
	private DigitalCareFontButton mLibraryImagePick;
	private DigitalCareFontButton mCancelDialog;

	TabletPopupWindow mPointerAlert = null;

	public ImageTabletPick(Activity activity) {
		/*DigiCareLogger.i(TAG, "ImageTabletPick");*/
		mContext = activity;
		getPointerAlert();
	}

	public TabletPopupWindow getPointerAlert() {
		mPointerAlert = new TabletPopupWindow(mContext, 400);
		ProductImageSelectorView mImageSelectorView = new ProductImageSelectorView(
				mContext);
		View mView = mImageSelectorView.getTabletProductImageMenuView();
		mPointerAlert.setContentView(mView);
		mPointerAlert.setBackgroundDrawable(new ColorDrawable());
		mCameraImagePick = (DigitalCareFontButton) mView
				.findViewById(mImageSelectorView.getCameraButtonID());
		mLibraryImagePick = (DigitalCareFontButton) mView
				.findViewById(mImageSelectorView.getLIbraryButtonID());
		mCancelDialog = (DigitalCareFontButton) mView
				.findViewById(mImageSelectorView.getCancelButtonID());
		setListenersToMenuOptions();
		return mPointerAlert;
	}

	private void setListenersToMenuOptions() {
		mCameraImagePick.setOnClickListener(this);
		mCameraImagePick.setTransformationMethod(null);
		mLibraryImagePick.setOnClickListener(this);
		mLibraryImagePick.setTransformationMethod(null);
		mCancelDialog.setOnClickListener(this);
		mCancelDialog.setTransformationMethod(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case CANCEL_BUTTON:
			DigiCareLogger.d("IMAGE", "Cancel Button");
			mPointerAlert.dismiss();

			break;
		case LIBRARY_BUTTON:
			DigiCareLogger.d("IMAGE", "Library Button");

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			mContext.startActivityForResult(
					Intent.createChooser(intent, null),
					DigitalCareConstants.IMAGE_PICK);
			mPointerAlert.dismiss();
			break;
		case CAMERA_BUTTON:
			DigiCareLogger.d("IMAGE", "Camera Button");

			Intent mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mContext.startActivityForResult(mCameraIntent,
					DigitalCareConstants.IMAGE_CAPTURE);
			mPointerAlert.dismiss();
			break;
		default:
			break;
		}
	}

}
