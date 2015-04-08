package com.philips.cl.di.digitalcare.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.digitalcare.R;

/**
 * 
 * @author naveen@philips.com
 * @description Network Notification view used during Connection not available
 *              in application component wise announcement.
 * @Since Apr 7, 2015
 */
@SuppressLint("NewApi")
public class NetworkAlertView {

	public void showNetworkAlert(final Activity mContext) {

		String mAlertbackGroundHeight = mContext.getResources().getString(
				R.string.NetworkAlertView_height);

		RelativeLayout mParent = new RelativeLayout(mContext);

		RelativeLayout mToastContainer = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams mContainerParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				Integer.parseInt(mAlertbackGroundHeight));
		mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		GradientDrawable mBackground = new GradientDrawable();
		mBackground.setColor(mContext.getResources().getColor(
				R.color.NetworkAlertView_background_color));
		mBackground.setCornerRadii(new float[] { 10, 10, 10, 10, 0, 0, 0, 0 });
		mToastContainer.setBackground(mBackground);

		TextView mTextView = new TextView(mContext);
		RelativeLayout.LayoutParams mTextViewParams = new RelativeLayout.LayoutParams(
				-2, -2);
		mTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mTextView.setTextColor(mContext.getResources().getColor(
				R.color.NetworkAlertView_text_color));
		mTextView.setText(mContext.getResources().getString(
				R.string.NetworkAlertView_text));
		//TODO: This is giving jenkins crash.
//		Typeface mTypeFace = Typeface.createFromAsset(mContext.getAssets(),
//				"fonts/centralesans-book.otf");
		mTextView.setTypeface(null, Typeface.BOLD);
		mTextView.setLayoutParams(mTextViewParams);

		mToastContainer.setLayoutParams(mContainerParams);
		mToastContainer.addView(mTextView);
		mParent.addView(mToastContainer);

		mParent.setAnimation(setAnimation());

		mContext.addContentView(mParent, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private Animation setAnimation() {
		TranslateAnimation mAnim = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE,
				0.0f, TranslateAnimation.ABSOLUTE, 0.0f,
				TranslateAnimation.ABSOLUTE, 100);
		mAnim.setFillAfter(true);
		mAnim.setDuration(4000l);
		mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		return mAnim;
	}

}
