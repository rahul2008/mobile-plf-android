package com.philips.cl.di.dev.pa.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.IBackgroundDrawable;
import com.philips.cl.di.dev.pa.util.Utils;

public class ShareBaseActivity extends Activity implements IBackgroundDrawable{
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void setBackgroundDrawable(ViewGroup view, Drawable drawable) {
		if (view == null || drawable == null) return;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}
	
	@Override
	public void setBackground(ViewGroup view, int resourceId, int color, float height) {
		if (view != null) {
			Bitmap src = BitmapFactory.decodeResource(getResources(), resourceId);
			Bitmap shadow = Utils.getShadow(src.getHeight(), src.getWidth(), color, height);
	
			Drawable drawable = new BitmapDrawable(getResources(), shadow);
			
			setBackgroundDrawable(view, drawable);
		}
	}
}
