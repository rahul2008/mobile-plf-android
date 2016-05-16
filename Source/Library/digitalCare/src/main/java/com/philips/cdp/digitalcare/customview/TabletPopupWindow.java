/**
 *
 * @author naveen@philips.com
 * @description PopupWindow Menu used in Social Support Screens to select the
 *              product image/user defined image from Camera and device Gallery.
 * @Since March 22, 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.philips.cdp.digitalcare.util.DigiCareLogger;


public class TabletPopupWindow extends PopupWindow {

	private LinearLayout mContainer;
	private ImageView mAnchorImage;
	private FrameLayout mContent;
	private int mMarginScreen;
	private AlignMode mAlignMode = AlignMode.CENTER_FIX;
	private final String TAG = TabletPopupWindow.class.getSimpleName();

	public TabletPopupWindow(Context context, int width) {
		this(context, width, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	public TabletPopupWindow(Context context, int width, int height) {
		super(width, height);
		if (width < 0) {
			throw new RuntimeException(
					"You must specify the window width explicitly(do not use WRAP_CONTENT or MATCH_PARENT)!!!");
		}
		mContainer = new LinearLayout(context);
		mContainer.setOrientation(LinearLayout.VERTICAL);
		mAnchorImage = new ImageView(context);
		mContent = new FrameLayout(context);
		setBackgroundDrawable(new ColorDrawable());
		setOutsideTouchable(false);
		setFocusable(true);
	}

	public AlignMode getAlignMode() {
		return mAlignMode;
	}

	public void setAlignMode(AlignMode mAlignMode) {
		this.mAlignMode = mAlignMode;
	}

	public AlignMode getOffsetMode() {
		return mAlignMode;
	}

	public void setOffsetMode(AlignMode mAlignMode) {
		this.mAlignMode = mAlignMode;
	}

	public int getMarginScreen() {
		return mMarginScreen;
	}

	public void setMarginScreen(int marginScreen) {
		this.mMarginScreen = marginScreen;
	}

	public void setPointerImageDrawable(Drawable d) {
		mAnchorImage.setImageDrawable(d);
	}

	public void setPointerImageRes(int res) {
		mAnchorImage.setImageResource(res);
	}

	public void setPointerImageBitmap(Bitmap bitmap) {
		mAnchorImage.setImageBitmap(bitmap);
	}

	@Override
	public void setContentView(View contentView) {
		if (contentView != null) {
			mContainer.removeAllViews();
			mContainer.addView(mAnchorImage,
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			mContainer.addView(mContent, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			mContent.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			super.setContentView(mContainer);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBackgroundDrawable(Drawable background) {
		// noinspection deprecation
		mContent.setBackgroundDrawable(background);
		super.setBackgroundDrawable(new ColorDrawable());
	}

	public void showAsPointer(View anchor) {
		showAsPointer(anchor, 0, 0);
	}

	public void showAsPointer(View anchor, int yoff) {
		showAsPointer(anchor, 0, yoff);
	}

	public void showAsPointer(View anchor, int xoff, int yoff) {
		final Rect displayFrame = new Rect();
		anchor.getWindowVisibleDisplayFrame(displayFrame);
		// final int displayFrameWidth = displayFrame.right - displayFrame.left;
		int[] loc = new int[2];
		anchor.getLocationInWindow(loc);// get anchor location
		/*
		 * if (mAlignMode == AlignMode.CENTER_FIX) { float offCenterRate =
		 * (displayFrame.centerX() - loc[0]) / (float) displayFrameWidth; xoff =
		 * (int) ((anchor.getWidth() - getWidth()) / 2 + offCenterRate
		 * getWidth() / 2); } else
		 */if (mAlignMode == AlignMode.CENTER_FIX) {
			xoff = (anchor.getWidth() - getWidth()) / 2;
			DigiCareLogger.i(TAG, "XOff : " + xoff);
		}
		/*
		 * int left = loc[0] + xoff; int right = left + getWidth(); // reset x
		 * offset to display the window fully in the screen if (right >
		 * displayFrameWidth - mMarginScreen) { xoff = (displayFrameWidth -
		 * mMarginScreen - getWidth()) - loc[0]; } if (left < displayFrame.left
		 * + mMarginScreen) { xoff = displayFrame.left + mMarginScreen - loc[0];
		 * }
		 */
		computePointerLocation(anchor, xoff);
		DigiCareLogger.i(TAG, "Compute Location Anchor : " + anchor
				+ " & xOff : " + xoff);
		super.showAsDropDown(anchor, xoff, yoff);
	}

	private void computePointerLocation(View anchor, int xoff) {
		int aw = anchor.getWidth();
		int dw = mAnchorImage.getDrawable().getIntrinsicWidth();
		mAnchorImage.setPadding((aw - dw) / 2 - xoff, 0, 0, 0);
	}

	public static enum AlignMode {
		/**
		 * default align mode,align the left|bottom of the anchor view
		 */
		DEFAULT,
		/**
		 * PopupWindowMain align center of the anchor view
		 */
		CENTER_FIX,
		/**
		 * according to the location of the anchor view in the display window,
		 * auto offset the popup window to display center.
		 */
		AUTO_OFFSET
	}

}