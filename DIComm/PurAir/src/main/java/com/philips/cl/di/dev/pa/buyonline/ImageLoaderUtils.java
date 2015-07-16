package com.philips.cl.di.dev.pa.buyonline;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderUtils {

    public static String LOG_TAG = "ImageLoaderUtils";

    public static void initImageLoader(Context context, DisplayImageOptions defaultDisplayImageOptions, File cacheDir, boolean debug) {
        initImageLoader(context, defaultDisplayImageOptions, cacheDir, new Md5FileNameGenerator(), debug);
    }

    public static void initImageLoader(Context context, DisplayImageOptions defaultDisplayImageOptions, File cacheDir, FileNameGenerator fileNameGenerator, boolean debug) {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration.Builder configBuidler = new ImageLoaderConfiguration.Builder(context);
            configBuidler.threadPriority(Thread.NORM_PRIORITY);
            configBuidler.denyCacheImageMultipleSizesInMemory();
            configBuidler.tasksProcessingOrder(QueueProcessingType.LIFO);
            configBuidler.defaultDisplayImageOptions(defaultDisplayImageOptions);
            if (cacheDir != null && cacheDir.exists()) {
                configBuidler.discCache(new UnlimitedDiscCache(cacheDir, fileNameGenerator));
            } else {
                configBuidler.discCacheFileNameGenerator(fileNameGenerator);
            }
            if (debug) {
                configBuidler.writeDebugLogs();
            }

             configBuidler.threadPoolSize(6);

            ImageLoader.getInstance().init(configBuidler.build());
        }
    }

    public static DisplayImageOptions getDisplayImageOptions(boolean cacheInMemory, int ic_loading, int ic_empty, int ic_error, int roundPixels, int durationMillis) {
        DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
        optionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
        optionsBuilder.cacheOnDisc(true);
        optionsBuilder.cacheInMemory(cacheInMemory);
        optionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
        if (ic_loading > 0) {
            optionsBuilder.showImageOnLoading(ic_loading);
        }
        if (ic_empty > 0) {
            optionsBuilder.showImageForEmptyUri(ic_empty);
        }
        if (ic_error > 0) {
            optionsBuilder.showImageOnFail(ic_error);
        }
        if (roundPixels > 0) {
            optionsBuilder.displayer(new RoundedBitmapDisplayer(roundPixels));
        }
        if (durationMillis > 0) {
            optionsBuilder.displayer(new FadeInBitmapDisplayer(durationMillis));
        }
        optionsBuilder.resetViewBeforeLoading(false);
        return optionsBuilder.build();
    }

    @SuppressWarnings("deprecation")
	public static Drawable getCacheDrawable(String imgUrl, DiscCacheAware discCacheAware) {
        try {
            File file = DiscCacheUtil.findInCache(imgUrl, discCacheAware);
            Drawable drawable = null;
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (null != bitmap) {
                    drawable = new BitmapDrawable(bitmap);
                    return drawable;
                }
            }
        } catch (Exception e) {}
        return null;
    }


    public static void destory() {
        ImageLoader.getInstance().destroy();
    }

    public static void clearDiscCache() {
        ImageLoader.getInstance().clearDiscCache();
    }


}
