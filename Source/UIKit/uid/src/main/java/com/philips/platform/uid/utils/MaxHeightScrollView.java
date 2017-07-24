/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import com.philips.platform.uid.R;

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

    public MaxHeightScrollView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) getResources().getDimension(R.dimen.uid_alert_dialog_content_max_height), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
