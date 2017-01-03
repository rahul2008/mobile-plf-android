/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.drawable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class AnimatedTranslateDrawable extends Drawable {
    private static final int DEFAULT_ANIMATION_DURATION = 900; //900ms

    private Drawable drawable;
    private float translateValue;

    private ObjectAnimator animator;

    public AnimatedTranslateDrawable(final Drawable drawable, float startX, float endX) {
        this.drawable = drawable;
        createDefaultAnimation(startX, endX);
    }

    private void createDefaultAnimation(final float startX, final float endX) {
        animator = ObjectAnimator.ofFloat(this, "translate", startX, endX);
        setRepeatMode(ValueAnimator.RESTART);
        setRepeatCount(ValueAnimator.INFINITE);
        setDuration(DEFAULT_ANIMATION_DURATION);
    }

    @Override
    public void draw(final Canvas canvas) {
        int saveCount = canvas.save();
        canvas.translate(translateValue, 0);
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

    public float getTranslate() {
        return translateValue;
    }

    public void setTranslate(float translateX) {
        translateValue = translateX;
        invalidateSelf();
    }

    @Override
    public void setBounds(final Rect bounds) {
        drawable.setBounds(bounds);
    }

    /**
     * Should be called to start the animation
     */
    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    /**
     * Ends the animation (translate animation).
     */
    public void end() {
        if (animator != null && animator.isRunning()) {
            animator.end();
        }
    }

    public void setDuration(long duration) {
        animator.setDuration(duration);
    }

    public void setRepeatCount(int repeatCount) {
        animator.setRepeatCount(repeatCount);
    }

    public void setRepeatMode(int repeatMode) {
        animator.setRepeatMode(repeatMode);
    }

    public Animator getAnimator() {
        return animator;
    }
}
