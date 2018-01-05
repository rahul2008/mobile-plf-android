/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.drawable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

public class AnimatedTranslateDrawable extends Drawable {
    private Drawable drawable;
    private Drawable mirrorDrawable;
    private float translateValue;

    private Animator animator;

    public AnimatedTranslateDrawable(final Drawable drawable, final Drawable mirrorDrawable, final float startX, final float endX) {
        this.drawable = drawable;
        this.mirrorDrawable = mirrorDrawable;
        createDefaultAnimation(startX, endX);
    }

    private void createDefaultAnimation(final float startX, final float endX) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "translate", startX, endX);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        animator = anim;
    }

    @Override
    public void draw(@NonNull final Canvas canvas) {
        int saveCount = canvas.save();
        canvas.translate(translateValue, 0);
        drawable.draw(canvas);
        if (mirrorDrawable != null) {
            mirrorDrawable.draw(canvas);
        }
        canvas.restoreToCount(saveCount);
    }

    /**
     * Sets the alpha on enclosing drawables (drawable and mirror drawable)
     * @param alpha to be applied on drawables.
     *              @since 3.0.0
     */
    @Override
    public void setAlpha(final int alpha) {
        drawable.setAlpha(alpha);
        if (mirrorDrawable != null) {
            mirrorDrawable.setAlpha(alpha);
        }
    }

    /**
     * Sets the ColorFilter on enclosing drawables (drawable and mirror drawable)
     * @param colorFilter to be applied on drawables.
     *                    @since 3.0.0
     */
    @Override
    public void setColorFilter(final ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
        if (mirrorDrawable != null) {
            mirrorDrawable.setColorFilter(colorFilter);
        }
    }

    /**
     * Returns the opacity of enclosed drawable.
     * @return opacity of enclosed drawable.
     * @since 3.0.0
     */
    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }

    /**
     * Function to return translated value for translation calculated by animator.
     * @return Interpolated translate value.
     * @since 3.0.0
     */
    public float getTranslate() {
        return translateValue;
    }

    /**
     * Function used by animator to calculate values by interpolator
     * @param translateX set by the animation framework.
     *                   @since 3.0.0
     */
    public void setTranslate(float translateX) {
        translateValue = translateX;
        invalidateSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBounds(@NonNull final Rect bounds) {
        super.setBounds(bounds);
    }

    /**
     * Should be called to start the animation
     * @since 3.0.0
     */
    public void start() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    /**
     * Should be called to resume the animation
     * @since 3.0.0
     */
    public void resume() {
        if (animator.isPaused()) {
            animator.resume();
        } else {
            animator.start();
        }
    }

    /**
     * Should be called to pause the animation
     * @since 3.0.0
     */
    public void pause() {
        if (animator.isRunning()) {
            animator.pause();
        }
    }

    /**
     * Ends the animation (translate animation).
     * @since 3.0.0
     */
    public void end() {
        if (animator != null && (animator.isRunning() || animator.isPaused())) {
            animator.end();
        }
    }

    /**
     * Returns animator bind to this drawable.
     * @return Animator bind to this drawable.
     * @since 3.0.0
     */
    public Animator getAnimator() {
        return animator;
    }
}
