package com.philips.cl.di.digitalcare.customview;

import com.philips.cl.di.digitalcare.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author naveen@philips.com
 * @description Network Notification view used during Connection not available
 *              in application component wise announcement.
 * @Since Apr 7, 2015
 */
public class NetworkAlertView {

	public void showNetworkAlert(final Activity mContext) {

		RelativeLayout mParent = new RelativeLayout(mContext);

		RelativeLayout mToastContainer = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams mContainerParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 40);
		mContainerParams.setMargins(10, 0, 10, 10);
		mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mToastContainer.setBackgroundColor(Color.RED);

		TextView mTextView = new TextView(mContext);
		RelativeLayout.LayoutParams mTextViewParams = new RelativeLayout.LayoutParams(
				-2, -2);
		mTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mTextView.setTextColor(Color.WHITE);
		mTextView.setText("Connection Not Available");
		Typeface mTypeFace = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/centralesans-book.otf");
		mTextView.setTypeface(mTypeFace, Typeface.BOLD);
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
		mAnim.setDuration(2000l);
		mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		return mAnim;
	}

}
