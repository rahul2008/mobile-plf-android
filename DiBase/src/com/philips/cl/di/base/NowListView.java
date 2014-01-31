package com.philips.cl.di.base;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NowListView extends ListView implements OnGlobalLayoutListener {

	public NowListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//initLayoutObserver();

	}

	public NowListView(Context context) {
		super(context);
		//initLayoutObserver();
	}

	//private void initLayoutObserver() {
	//	setOrientation(LinearLayout.VERTICAL);
	//	getViewTreeObserver().addOnGlobalLayoutListener(this);
	//}

	@Override
	public void onGlobalLayout() {
		getViewTreeObserver().removeGlobalOnLayoutListener(this);

		final int heightPx = getContext().getResources().getDisplayMetrics().heightPixels;

		boolean inversed = false;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);

			int[] location = new int[2];

			child.getLocationOnScreen(location);

			if (location[1] > heightPx) {
				break;
			}

			if (!inversed) {
				child.startAnimation(AnimationUtils.loadAnimation(getContext(),
						R.anim.slide_up_left));
			} else {
				child.startAnimation(AnimationUtils.loadAnimation(getContext(),
						R.anim.slide_up_right));
			}

			inversed = !inversed;
		}

	}

}
