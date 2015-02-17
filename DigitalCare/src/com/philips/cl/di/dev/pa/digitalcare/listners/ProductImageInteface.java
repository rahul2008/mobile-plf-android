package com.philips.cl.di.dev.pa.digitalcare.listners;

import android.graphics.Bitmap;

/**
 * @description ProductImageInteface interface used for attaching and dettaching the product image in contactUs support.
 * @author 310190678
 *@since 11/feb/2015
 */
public interface ProductImageInteface {

	void onImageReceived(Bitmap image, String Uri);

	void onImageDettach();

}
