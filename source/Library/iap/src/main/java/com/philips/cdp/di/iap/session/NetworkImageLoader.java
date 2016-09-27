package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Custom implementation of Volley Request Queue
 */
public class NetworkImageLoader {

    private static NetworkImageLoader mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LruCache<String, Bitmap> mMemoryCache;

    public NetworkImageLoader(Context context) {
        mRequestQueue = getRequestQueue(context);
        setUpCacheLoader();

    }

    protected void setUpCacheLoader() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    /*private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(200);*/

                    @Override
                    public Bitmap getBitmap(String url) {
                        return mMemoryCache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        //mMemoryCache.put(url, bitmap);
                        addBitmapToMemoryCache(url, bitmap);
                    }

                    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
                        if (getBitmapFromMemCache(key) == null) {
                            mMemoryCache.put(key, bitmap);
                        }
                    }

                    public Bitmap getBitmapFromMemCache(String key) {
                        return mMemoryCache.get(key);
                    }
                });
    }

    public static synchronized NetworkImageLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkImageLoader(context);
        }
        return mInstance;
    }

    protected RequestQueue getRequestQueue(final Context context) {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}