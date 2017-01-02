/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.drawable;

import android.animation.TimeInterpolator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

public class AnimatedTranslateDrawable extends Drawable implements Runnable {
    private Drawable drawable;
    private float stepIncrement = 0.01f;
    private boolean animationRunning;
    private TimeInterpolator linearInterpolator = new LinearInterpolator();
    private float elapsedFraction;
    public AnimatedTranslateDrawable(final Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(final Canvas canvas) {
        int saveCount = canvas.save();
        stepIncrement += 10;
        canvas.translate(stepIncrement, 0);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
        start();
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
        if (stepIncrement > 1000) stepIncrement = 0.01f;
        invalidateSelf();
        nextFrame();
    }

    @Override
    public void setBounds(final Rect bounds) {
        drawable.setBounds(bounds);
    }

    /**
     * Should be called to start the animation
     */
    public void start() {
        if (!animationRunning) {
            animationRunning = true;
            elapsedFraction = linearInterpolator.getInterpolation(0);
            nextFrame();
        }
    }

    /**
     * Stops the animation (translate animation).
     */
    public void stop() {
        animationRunning = false;
        unscheduleSelf(this);
    }

    /**
     * Provides information about current running information.
     * @return Whether the animation is active.
     */
    public boolean isAnimationRunning() {
        return animationRunning;
    }

    private void nextFrame() {
        unscheduleSelf(this);
        scheduleSelf(this, SystemClock.uptimeMillis() + (long) 2);
    }
}
