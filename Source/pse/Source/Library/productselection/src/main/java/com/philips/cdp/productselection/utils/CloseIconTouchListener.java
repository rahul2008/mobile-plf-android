package com.philips.cdp.productselection.utils;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public abstract class CloseIconTouchListener implements View.OnTouchListener {

    Drawable drawable;
    private int count = 10;

    public CloseIconTouchListener(TextView view) {
        super();
        final Drawable[] drawables = view.getCompoundDrawables();
        if (drawables != null && drawables.length == 4) {
            this.drawable = drawables[2];
        }
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            final Rect bounds = drawable.getBounds();

            if (x >= (v.getRight() - bounds.width() - 30) && x <= (v.getRight()  + count)
                    && y >= (0 - count) && y <= (v.getHeight() ) + count) {
                return onDrawableTouch(event);
            }
        }
        return false;
    }

    public abstract boolean onDrawableTouch(final MotionEvent event);
}
