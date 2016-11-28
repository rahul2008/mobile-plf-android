package com.philips.platform.uit.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.philips.platform.uit.R;

public class MaxHeightScrollView extends ScrollView {
    public MaxHeightScrollView(final Context context) {
        super(context);
    }

    public MaxHeightScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightScrollView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaxHeightScrollView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.uid_alert_dialog_content_max_height), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
