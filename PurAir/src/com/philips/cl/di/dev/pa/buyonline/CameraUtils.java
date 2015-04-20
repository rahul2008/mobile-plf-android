package com.philips.cl.di.dev.pa.buyonline;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;

public class CameraUtils {
	
	private static void getImageByAlbum(Activity activity) {
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		if (android.os.Build.VERSION.SDK_INT < 19) {
			intent.setAction(Intent.ACTION_GET_CONTENT);
		} else {
			intent=new Intent(Intent.ACTION_PICK,
			         android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			
		}
		PackageManager pm = activity.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
		if (null == activities || activities.size() == 0) {
			AppUtils.createBuilder(activity).setMessage(activity.getString(
					R.string.gallery_app_not_install)).setPositiveButton(activity.getString(R.string.know), null);
		} else {
			activity.startActivityForResult(intent, 1);
		}
	}

	@TargetApi(19)
	public static String getFilePathFromContentUri(Activity activity, Uri contentUri) {
		String filePath = "";
		if (activity != null && contentUri != null) {
			try {
				if (android.os.Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(activity, contentUri)) {
					String wholeID = DocumentsContract.getDocumentId(contentUri);
					String id = wholeID.split(":")[1];
					String[] column = { MediaStore.Images.Media.DATA };
					String sel = MediaStore.Images.Media._ID + "=?";
					Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							column, sel, new String[] { id }, null);
					int columnIndex = cursor.getColumnIndex(column[0]);
					if (cursor.moveToFirst()) {
						filePath = cursor.getString(columnIndex);
					}
					cursor.close();
				} else {
					String[] projection = { MediaStore.Images.Media.DATA };
					Cursor cursor = activity.getContentResolver().query(contentUri, projection, null, null, null);
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					filePath = cursor.getString(column_index);
				}
				return filePath;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (NoClassDefFoundError e) {
			}
		}
		return filePath;
	}

	private static void getHeadImgByTake(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(AppConstants.TMP_OUTPUT_JPG)));
		PackageManager pm = activity.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
		if (null == activities || activities.size() == 0) {
			AppUtils.createBuilder(activity).setMessage(activity.getString(
					R.string.camera_app_not_install)).setPositiveButton(activity.getString(R.string.know), null);
		} else {
			activity.startActivityForResult(intent, 2);
		}
	}

	
	public static void startPhotoZoom(Activity activity, Uri uri,int w,int h,Uri outputFile) {
		Intent intent = new Intent();
		if (null == uri && activity == null) {
			return;
		}
		if (uri.getScheme().startsWith("content")) {
			String filePath = getFilePathFromContentUri(activity, uri);
			if (filePath == null || "".equals(filePath)) {
				return;
			}
			intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}
		File file = new File(AppConstants.TMP_OUTPUT_CORP_JPG);
		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		intent.setAction("com.android.camera.action.CROP");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", w);
		intent.putExtra("aspectY", h);
		intent.putExtra("outputX", w);
		intent.putExtra("outputY", h);
		intent.putExtra("scale", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile); 
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, 3);
	}
	
	public static AlertDialog.Builder getPic(final Activity activity){
		if (activity == null) return null;
		AlertDialog.Builder builder =
		 new AlertDialog.Builder(activity).setItems(new String[]{activity.getString(
				 R.string.gallery),activity.getString(R.string.camera)},
				 new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					getImageByAlbum(activity);
					break;
				case 1:
					getHeadImgByTake(activity);
					break;
				default:
					break;
				}
			}
		});
		builder.setPositiveButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		return builder;
	}
	
}
