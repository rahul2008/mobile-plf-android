package com.philips.cl.di.digitalcare.social;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;

import com.philips.cl.di.digitalcare.R;
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
	private AlertDialog mDialog = null;
	private static ProductImageResponseCallback mImageCallback = null;
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
		final String[] items = new String[] {
				mActivity.getResources().getString(R.string.take_photo),
				mActivity.getResources()
						.getString(R.string.choose_from_library),
				mActivity.getResources().getString(R.string.cancel) };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {

					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					mActivity.startActivityForResult(intent,
							DigitalCareContants.IMAGE_CAPTURE);
				} else if (item == 1) {
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					mActivity.startActivityForResult(Intent.createChooser(
							intent, "Complete action using"),
							DigitalCareContants.IMAGE_PICK);
				} else {
					dialog.dismiss();
				}
			}
		});

		mDialog = builder.create();
		mDialog.show();
	}

	public void processProductImage(Intent data, int requestCode) {
		DLog.d(TAG, "onActivity receiving the Intent");

		if (requestCode == DigitalCareContants.IMAGE_PICK) {
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
