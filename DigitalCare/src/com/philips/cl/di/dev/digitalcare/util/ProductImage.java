package com.philips.cl.di.dev.digitalcare.util;

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
import android.util.Log;
import android.widget.ArrayAdapter;

import com.philips.cl.di.dev.digitalcare.listners.ProductImageInteface;

/**
 * @description This Class will fetch the ProductImage from Camera & Gallery and
 *              sends to faceboook & Twitter with the callback interface.
 * @author naveen@philips.com
 * @since 11/Feb/2015
 */

public class ProductImage {
	private static String TAG = ProductImage.class.getSimpleName();
	private static ProductImage mObject = null;
	private AlertDialog dialog = null;
	private static ProductImageInteface mImageCallback = null;
	private static Activity mActivity = null;

	private ProductImage() {
	}

	public static ProductImage getInstance(Activity activity,
			ProductImageInteface callback) {
		if (mObject == null)
			mObject = new ProductImage();

		mActivity = activity;
		mImageCallback = callback;

		return mObject;
	}

	public static ProductImage getInstance() {
		return mObject;
	}

	public void pickImage() {
		final String[] items = new String[] { "Take Photo",
				"Choose from Library", "Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {

					Intent intent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					mActivity.startActivityForResult(intent,
							DigiCareContants.IMAGE_CAPTURE);
				} else if (item == 1) {
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					mActivity.startActivityForResult(Intent.createChooser(
							intent, "Complete action using"),
							DigiCareContants.IMAGE_PICK);
				} else {
					dialog.dismiss();
				}
			}
		});

		dialog = builder.create();
		dialog.show();
	}

	public void onActivityResult(Intent data, int requestCode) {
		Log.d(TAG, "onActivity receiving the Intent");

		if (requestCode == DigiCareContants.IMAGE_PICK) {
			Log.d(TAG, "Prod Image Received from Gallery");
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Uri selectedImage = data.getData();

			Cursor cursor = mActivity.getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			Log.d(TAG, "Gallery Image Path : " + picturePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
			mImageCallback.onImageReceived(bitmap, picturePath);
			cursor.close();
		}

		if (requestCode == DigiCareContants.IMAGE_CAPTURE) {
			Log.d(TAG, "Product Image receiving from Camera");

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
