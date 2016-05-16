/**
 * @author naveen@philips.com
 * @description This Class will fetch the ProductImage from Camera & Gallery and
 * sends to faceboook & Twitter with the callback interface.
 * @since 11/Feb/2015
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.social;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.ImagePhonePickerDialog;
import com.philips.cdp.digitalcare.customview.TabletPopupWindow;
import com.philips.cdp.digitalcare.customview.TabletPopupWindow.AlignMode;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



@SuppressLint("NewApi")
public class ProductImageHelper {
    private final static int KITKAT = 19;
    private static String TAG = ProductImageHelper.class.getSimpleName();
    private static ProductImageHelper mProductImage = null;
    private static ProductImageResponseCallback mImageCallback = null;
    private static Activity mActivity = null;
    private static View mProductImageView = null;
    TabletPopupWindow mPopupMenu = null;
    private ImagePhonePickerDialog mDialog = null;

    private ProductImageHelper() {
    }

    public static ProductImageHelper getInstance(Activity activity,
                                                 ProductImageResponseCallback callback, View view) {
        if (mProductImage == null)
            mProductImage = new ProductImageHelper();

        mActivity = activity;
        mImageCallback = callback;
        mProductImageView = view;

        return mProductImage;
    }

    public static ProductImageHelper getInstance() {
        return mProductImage;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                long mId = Long.parseLong(id);
                Uri mUri = Uri.parse("content://downloads/public_downloads");
                final Uri contentUri = ContentUris.withAppendedId(mUri, mId);
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public void pickImage() {
        if (isTablet(mActivity) && (mProductImageView != null)) {
            DigiCareLogger.d(TAG, "It is Tablet");
            ImageTabletPick mImageTabletPick = new ImageTabletPick(mActivity);
            mPopupMenu = mImageTabletPick.getPointerAlert();
            setBackgroundColorToPointerView(mPopupMenu);
            mPopupMenu.showAsPointer(mProductImageView);
            mPopupMenu.setAlignMode(AlignMode.CENTER_FIX);
        } else {
            DigiCareLogger.d(TAG, "It is Mobile Phone");
            mDialog = new ImagePhonePickerDialog(mActivity);
            mDialog.show();
        }

    }

    private void setBackgroundColorToPointerView(TabletPopupWindow mPointerView) {
        Drawable mPointerImage = mActivity.getResources().getDrawable(
                android.R.drawable.arrow_up_float);
    /*	mPointerImage.setColorFilter(
				mActivity.getResources().getColor(
						R.color.activity_background_color), Mode.LIGHTEN);*/
        mPointerImage.setColorFilter(
                mActivity.getResources().getColor(
                        R.color.blue), Mode.LIGHTEN);
        mPointerView.setPointerImageDrawable(mPointerImage);
    }

    public void resetDialog() {

        if (mActivity != null & mProductImage != null) {

            if (isTablet(mActivity) && (mProductImageView != null)) {
                if (mPopupMenu.isShowing() && !(mActivity.isFinishing())) {
                    mPopupMenu.dismiss();
                    pickImage();
                }
            } else {
                if (mDialog.isShowing() && !(mActivity.isFinishing())) {
                    DigiCareLogger.d(TAG, "Dialog is resetted");
                    mDialog.dismiss();
                    pickImage();
                }
            }
        }
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void processProductImage(Intent data, int requestCode) {
        DigiCareLogger.d(TAG, "onActivity receiving the Intent");
        DigiCareLogger.d(TAG, "Android Version is : " + Build.VERSION.SDK_INT);

        if (requestCode == DigitalCareConstants.IMAGE_PICK) {
            DigiCareLogger.d(TAG, "Image Picked From Gallery  with Data : " + data);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Uri selectedImage = data.getData();

            if (Build.VERSION.SDK_INT >= KITKAT) {
                DigiCareLogger.d(TAG, "Android Version in 19+");

                DigiCareLogger.d(TAG, "PATH : " + getPath(mActivity, selectedImage));
                InputStream input;
                Bitmap bitmap;
                String picturePath = getPath(mActivity, selectedImage);
                try {
                    input = mActivity.getContentResolver().openInputStream(
                            selectedImage);
                    bitmap = BitmapFactory.decodeStream(input);
                    mImageCallback.onImageReceived(bitmap, picturePath);
                } catch (FileNotFoundException e1) {

                }
            } else {
                DigiCareLogger.d(TAG, "Android Version is 18 below");

                Cursor cursor = mActivity.getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                DigiCareLogger.d(TAG, "Gallery Image Path : " + picturePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
                mImageCallback.onImageReceived(bitmap, picturePath);
                cursor.close();
            }
        } else if (requestCode == DigitalCareConstants.IMAGE_CAPTURE) {
            DigiCareLogger.d(TAG, "Product Image receiving from Camera");

            File f = new File(mActivity.getCacheDir(), "DC_IMAGE");
            try {
                f.createNewFile();
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
