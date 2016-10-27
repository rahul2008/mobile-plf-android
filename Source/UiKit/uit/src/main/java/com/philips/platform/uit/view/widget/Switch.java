package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import com.philips.platform.uit.R;

public class Switch extends SwitchCompat {

    private Paint pressedThumb = new Paint();
    private boolean pressed = false;
    public Switch(Context context) {
        super(context);
    }

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);

        pressedThumb.setColor(Color.GREEN);

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pressed) {
            Rect bounds = getThumbDrawable().getBounds();

            int leftCenter = bounds.left + ((bounds.right - bounds.left)/2);
            int topCenter = bounds.top + ((bounds.bottom - bounds.top)/2);

            canvas.drawCircle(leftCenter, topCenter, 50, pressedThumb);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if( ev.getAction() == MotionEvent.ACTION_DOWN){
            pressed = true;
            invalidate();
        } else if(ev.getAction() == MotionEvent.ACTION_UP){
            pressed = false;
        }

        return super.onTouchEvent(ev);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
