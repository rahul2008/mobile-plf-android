package com.philips.cdp.digitalcare.rateandreview.productreview;

/**
 * OnImageUploadComplete.java <br>
 * ReviewSubmissionExample<br>
 * 
 * This is an interface used for passing callback functions for image uploads.
 * 
 * <p>
 * Copyright (c) 2012 BazaarVoice. All rights reserved.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 11 Sep 2015
 */
public interface OnImageUploadComplete {
	
	/**
	 * Called when an image upload has completed
	 */
	public void onFinish();

}