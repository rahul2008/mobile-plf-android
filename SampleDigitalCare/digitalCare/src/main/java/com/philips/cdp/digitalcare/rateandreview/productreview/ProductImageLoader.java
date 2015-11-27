/*
package com.philips.cdp.digitalcare.rateandreview.productreview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

*/
/**
 * Adding Product Image Loader here.
 *
 * @author naveen@philips.com
 *//*

public class ProductImageLoader {

    private static final String TAG = ProductImageLoader.class.getSimpleName();
    private String mImageUrl = null;
    private ImageView mImageView = null;
    private Bitmap mBitmap = null;
    private Handler mResponseHandler = null;

    public ProductImageLoader(String url, ImageView imageview) {
        this.mImageUrl = url;
        this.mImageView = imageview;
        mResponseHandler = new Handler(Looper.getMainLooper());
    }

    public void execute() {
        NetworkThread mNetworkThread = new NetworkThread();
        mNetworkThread.setPriority(Thread.MAX_PRIORITY);
        mNetworkThread.start();
    }

    protected void notifyResponseHandler() {
        if (mBitmap != null) {

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                   mImageView.setImageBitmap(mBitmap);
                }
            });

        }
    }

    class NetworkThread extends Thread {

        @Override
        public void run() {
            try {
                URL obj = new URL(mImageUrl);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) obj
                        .openConnection();
                mHttpURLConnection.setRequestMethod("GET");
                InputStream mInputStream = mHttpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(mInputStream);
                mBitmap = bitmap;
            } catch (Exception e) {
                DigiCareLogger.e(
                        TAG,
                        "Failed to fetch Image Data : "
                                + e.getLocalizedMessage());
            } finally {
                notifyResponseHandler();
            }
        }
    }

}
*/
