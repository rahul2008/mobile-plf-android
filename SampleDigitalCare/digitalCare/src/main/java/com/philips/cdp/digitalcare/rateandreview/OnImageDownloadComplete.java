package com.philips.cdp.digitalcare.rateandreview;

import android.graphics.Bitmap;

/**
 * OnImageDownloadComplete.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is an interface used for passing callback functions for image downloads.
 * 
 * <p>
 * Copyright (c) 2012 BazaarVoice. All rights reserved.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 11 Sep 2015
 */
public interface OnImageDownloadComplete {

	/**
	 * Called when an image download has completed
	 * 
	 * @param image the downloaded image
	 */
	public void onFinish(Bitmap image);

}
