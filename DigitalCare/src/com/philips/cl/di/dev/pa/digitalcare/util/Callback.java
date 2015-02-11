package com.philips.cl.di.dev.pa.digitalcare.util;

import android.graphics.Bitmap;

/**
 * @description Callback interface used for attaching and dettaching the product image in contactUs support.
 * @author 310190678
 *@since 11/feb/2015
 */
public interface Callback {

	void onImageReceived(Bitmap image, String Uri);

	void onImageDettach();

}
