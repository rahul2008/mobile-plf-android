package com.philips.cl.di.dev.pa.digitalcare.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.philips.cl.di.dev.pa.digitalcare.listners.ProductImageInteface;

/**
 * @description This Class will fetch the ProductImage from Camera & Gallery and sends to faceboook & Twitter with 
 * the callback interface. 
 * @author naveen@philips.com
 * @since 11/Feb/2015
 */

public class ProductImage {

	private static String TAG = ProductImage.class.getSimpleName();
	private static ProductImage mObject = null;
	private AlertDialog dialog = null;
	private static ProductImageInteface mImageCallback = null;
	private static Activity mActivity = null;
	private Uri mImageUri = null;

	private ProductImage() {
	}

	public static ProductImage getInstance(Activity activity, ProductImageInteface callback) {
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
					String fileName = "Philips Product.jpg";
					ContentValues values = new ContentValues();
					values.put(MediaStore.Images.Media.TITLE, fileName);
					mImageUri = ((ContextWrapper) mActivity)
							.getContentResolver()
							.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									values);
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
					mActivity.startActivityForResult(cameraIntent,
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
		ALog.d(TAG, "onActivity receiving the Intent");

		if (requestCode == DigiCareContants.IMAGE_PICK) {
			ALog.d(TAG, "Prod Image Received from Gallery");
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Uri selectedImage = data.getData();

			Cursor cursor = mActivity.getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			ALog.d(TAG, "Gallery Image Path : " + picturePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
			mImageCallback.onImageReceived(bitmap, picturePath);
			cursor.close();
		}

		if (requestCode == DigiCareContants.IMAGE_CAPTURE) {
			ALog.d(TAG, "Product Image receiving from Camera");
			Bitmap photo = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			String[] projection = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = mActivity.managedQuery(mImageUri, projection, null,
					null, null);
			int column_index_data = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index_data);
			photo = BitmapFactory.decodeFile(path, options);
			Log.d(TAG, "image Path : " + path);
			mImageCallback.onImageReceived(photo, path);
			if (cursor != null)
				cursor.close();
		}
	}

}
