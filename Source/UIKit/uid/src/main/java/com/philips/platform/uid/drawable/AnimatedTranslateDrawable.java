/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

public class AnimatedTranslateDrawable extends Drawable implements Runnable {
    private Drawable drawable;
    private float stepIncrement = 1.5f;

    public AnimatedTranslateDrawable(final Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(final Canvas canvas) {
        int saveCount = canvas.save();
        float stepSize = 20 * stepIncrement;

        canvas.translate(stepSize, stepSize);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public void setAlpha(final int alpha) {
        drawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(final ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }

    @Override
    public void run() {
        if (stepIncrement > 100) stepIncrement = 0;
        invalidateSelf();
        nextFrame();
    }

    private void nextFrame() {
        unscheduleSelf(this);
        scheduleSelf(this, SystemClock.uptimeMillis() + (long) 0.2);
    }
}
