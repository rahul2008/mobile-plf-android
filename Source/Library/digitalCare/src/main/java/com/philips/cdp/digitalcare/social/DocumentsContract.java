/**
 * Image Capturing Class for the Twitter & Facebook product data picker Logic.
 *
 * @author naveen@philips.com
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.social;

import java.util.List;

import android.net.Uri;
import android.util.Log;

public class DocumentsContract {
	private static final String DOCUMENT_URIS = "com.android.providers.media.documents "
			+ "com.android.externalstorage.documents "
			+ "com.android.providers.downloads.documents "
			+ "com.android.providers.media.documents";

	private static final String PATH_DOCUMENT = "document";
	private static final String TAG = DocumentsContract.class.getSimpleName();

	public static String getDocumentId(Uri documentUri) {
		final List<String> paths = documentUri.getPathSegments();
		if (paths.size() < 2) {
			throw new IllegalArgumentException("Not a document: " + documentUri);
		}

		if (!PATH_DOCUMENT.equals(paths.get(0))) {
			throw new IllegalArgumentException("Not a document: " + documentUri);
		}
		return paths.get(1);
	}

	public static boolean isDocumentUri(Uri uri) {
		final List<String> paths = uri.getPathSegments();
		Log.d(TAG, "paths[" + paths + "]");
		if (paths.size() < 2) {
			return false;
		}
		if (!PATH_DOCUMENT.equals(paths.get(0))) {
			return false;
		}
		return DOCUMENT_URIS.contains(uri.getAuthority());
	}
}