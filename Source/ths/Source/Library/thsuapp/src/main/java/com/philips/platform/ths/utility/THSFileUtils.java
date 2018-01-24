/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.FileAttachment;
import com.americanwell.sdk.entity.UploadAttachment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;

import static com.philips.platform.ths.utility.THSConstants.THS_VIEW_VISIT_REPORT;

/**
 * Utilities for File handling
 */
public class THSFileUtils {


    /**
     * Download the given {@link FileAttachment} into an actual File object
     *
     * @param context
     * @param fileAttachment
     * @return
     * @throws IOException
     */
    public File downloadAttachment(final Context context, final FileAttachment fileAttachment) throws IOException {
        final MediaType mediaType = MediaType.parse(fileAttachment.getType());
        String fileName = fileAttachment.getName();
        final String subDir = context.getString(context.getApplicationInfo().labelRes);
        final File path = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                + File.separator
                + subDir);
        path.mkdirs();
        if (!fileName.contains(".")) {
            fileName += "." + mediaType.subtype();
        }
        File file = new File(path, fileName);
        // if file with name already exists... rename with (1), (2), etc...
        int index = 1;
        final int dotIndex = fileName.lastIndexOf(".");
        final String fileNamePrefix = fileName.substring(0, dotIndex);
        final String fileNameExtension = fileName.substring(dotIndex + 1, fileName.length());
        while (file.exists()) {
            fileName = fileNamePrefix + " (" + index++ + ")." + fileNameExtension;
            file = new File(path, fileName);
        }

        if (file.exists()) {
            return file;
        } else {
            FileOutputStream fos = new FileOutputStream(file);
            final OutputStream stream = new BufferedOutputStream(fos);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = fileAttachment.getInputStream().read(buffer)) != -1) {
                stream.write(buffer, 0, len);
            }
            stream.close();
            return file;
        }


/*        Intent intent =
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);*/


    }

    /**
     * Get a new instance of an UploadAttachment for a given URI
     *
     * @param context
     * @param awsdk
     * @param uri
     * @return
     * @throws FileNotFoundException
     */
    public UploadAttachment getUploadAttachment(final Context context, final AWSDK awsdk, final Uri uri) throws FileNotFoundException {
        final InputStream inputStream = context.getContentResolver().openInputStream(uri);
        final String type = context.getContentResolver().getType(uri);
        final UploadAttachment uploadAttachment = awsdk.getNewUploadAttachment();
        uploadAttachment.setInputStream(inputStream);
        uploadAttachment.setType(type);
        uploadAttachment.setName(getFileName(context, uri));
        return uploadAttachment;
    }

    /**
     * return the filename for a given URI
     *
     * @param context
     * @param uri
     * @return
     */
    public String getFileName(final Context context, final Uri uri) {
        String result = null;
        if (uri.getScheme().equals("file")) {
            return uri.getLastPathSegment();
        }
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * download and open the given FileAttachment
     * will open in the default viewer for the type
     *
     * @param context
     * @param fileAttachment
     */
    public void openAttachment(final Context context, final FileAttachment fileAttachment) {
        try {
            // Note: sample code: this code will download the attachment every time it's clicked on
            // in a production app, you'd probably want to keep track of downloaded files and reopen
            // rather than redownload
            final File attachment = downloadAttachment(context, fileAttachment);
            final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            final Uri uri =  FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.philips.platform.ths", attachment);

            String type = "*/*";
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            if (mimeTypeMap.hasExtension(fileExtension)) {
                type = mimeTypeMap.getMimeTypeFromExtension(fileExtension);
            }

            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(intent);
                THSTagUtils.doTrackPageWithInfo(THS_VIEW_VISIT_REPORT,null,null);
            } catch (ActivityNotFoundException e) {
            }
        } catch (IOException ioe) {
        }
    }

}
