package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import com.philips.platform.uit.R;

public class Switch extends SwitchCompat {
    public Switch(Context context) {
        super(context);
    }

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable track = getResources().getDrawable(R.drawable.uit_switch_track);
        Log.e("FRANK", "height:" + track.getIntrinsicHeight());
        Log.e("FRANK", "width:" + track.getIntrinsicWidth());
        setTrackDrawable(track);
        setThumbDrawable(getResources().getDrawable(R.drawable.uit_switch_thumb));

        int top = (int) getResources().getDimension(R.dimen.uit_switch_padding_top);
        int bottom = (int) getResources().getDimension(R.dimen.uit_switch_padding_bottom);
        int end = (int) getResources().getDimension(R.dimen.uit_switch_padding_end);
        setPadding(0, top, end, bottom);

        setBackgroundResource(R.color.uit_aqua_level_15);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
