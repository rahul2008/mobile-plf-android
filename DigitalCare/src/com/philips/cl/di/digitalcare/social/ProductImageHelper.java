package com.philips.cl.di.digitalcare.social;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.adobe.mobile.Analytics;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

/**
 * @description This Class will fetch the ProductImage from Camera & Gallery and
 *              sends to faceboook & Twitter with the callback interface.
 * @author naveen@philips.com
 * @since 11/Feb/2015
 */

public class ProductImageHelper {
	private static String TAG = ProductImageHelper.class.getSimpleName();
	private static ProductImageHelper mProductImage = null;
	private static ProductImageResponseCallback mImageCallback = null;
	private ImagePickerDialog mDialog = null;
	private static Activity mActivity = null;

	private ProductImageHelper() {
	}

	public static ProductImageHelper getInstance(Activity activity,
			ProductImageResponseCallback callback) {
		if (mProductImage == null)
			mProductImage = new ProductImageHelper();

		mActivity = activity;
		mImageCallback = callback;

		return mProductImage;
	}

	public static ProductImageHelper getInstance() {
		return mProductImage;
	}

	public void pickImage() {
		mDialog = new ImagePickerDialog(mActivity);
		mDialog.show();
	}

	public void resetDialog() {
		if (mDialog.isShowing() && !(mActivity.isFinishing())) {
			DLog.d(TAG, "Dialog is resetted");
			mDialog.dismiss();
			pickImage();
		}
	}

	public void processProductImage(Intent data, int requestCode) {
		DLog.d(TAG, "onActivity receiving the Intent");

		if (requestCode == DigitalCareContants.IMAGE_PICK) {
			AnalyticsTracker.trackAction(
					AnalyticsConstants.ACTION_KEY_RECEIPT_PHOTO,
					AnalyticsConstants.ACTION_KEY_APP_ID,
					Analytics.getTrackingIdentifier());
			DLog.d(TAG, "Prod Image Received from Gallery");
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Uri selectedImage = data.getData();

			Cursor cursor = mActivity.getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			DLog.d(TAG, "Gallery Image Path : " + picturePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
			mImageCallback.onImageReceived(bitmap, picturePath);
			cursor.close();
		}

		if (requestCode == DigitalCareContants.IMAGE_CAPTURE) {
			AnalyticsTracker.trackAction(
					AnalyticsConstants.ACTION_KEY_RECEIPT_PHOTO,
					AnalyticsConstants.ACTION_KEY_APP_ID,
					Analytics.getTrackingIdentifier());
			DLog.d(TAG, "Product Image receiving from Camera");

			File f = new File(mActivity.getCacheDir(), "DC_IMAGE");
			try {
				f.createNewFile();

				// Convert bitmap to byte array
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
				byte[] bitmapdata = bos.toByteArray();

				FileOutputStream fos = new FileOutputStream(f);
				fos.write(bitmapdata);
				fos.flush();
				fos.close();

				mImageCallback.onImageReceived(bitmap, f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
