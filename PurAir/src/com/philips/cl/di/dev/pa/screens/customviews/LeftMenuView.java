package com.philips.cl.di.dev.pa.screens.customviews;

import com.philips.cl.di.dev.pa.constants.AppConstants;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * The Class LeftMenuView.
 */
public class LeftMenuView extends ListView {

	/** The width. */
	int width;

	/**
	 * Instantiates a new left menu view.
	 *
	 * @param context the context
	 */
	public LeftMenuView(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new left menu view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public LeftMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/* (non-Javadoc)
	 * @see android.widget.AbsListView#onAttachedToWindow()
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		// Animate here
		ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationX",
				-width, 0f);
		translate.setDuration(AppConstants.DURATION);
		translate.start();
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
