package com.philips.cl.di.dev.pa;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

public interface IBackgroundDrawable {
	void setBackgroundDrawable(ViewGroup view, Drawable drawable);
	void setBackground(ViewGroup view, int resourceId, int color, float height);
}
