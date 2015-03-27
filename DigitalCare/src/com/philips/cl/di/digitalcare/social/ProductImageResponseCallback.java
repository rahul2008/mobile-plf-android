package com.philips.cl.di.digitalcare.social;

import android.graphics.Bitmap;

/**
 * ProductImageInteface interface used for attaching and dettaching the product
 * image in contactUs support.
 * 
 * @author 310190678
 * @since 11/feb/2015
 */
public interface ProductImageResponseCallback {

	void onImageReceived(Bitmap image, String Uri);

	void onImageDettach();

}
