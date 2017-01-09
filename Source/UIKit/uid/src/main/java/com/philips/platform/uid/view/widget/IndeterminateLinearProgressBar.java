/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.AnimatedTranslateDrawable;

public class IndeterminateLinearProgressBar extends View {
    private Drawable leadingDrawable;
    private Drawable trailingDrawable;

    private int transitionDrawableWidth;
    private boolean drawTrailingAnim;

    private static final float TRANSITION_DRAWABLE_WIDTH_RATIO = 0.4F;
    private AnimatedTranslateDrawable leadingAnim;
    private AnimatedTranslateDrawable trailingAnim;

    public IndeterminateLinearProgressBar(final Context context) {
        this(context, null);
    }

    public IndeterminateLinearProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidIndeterminateLinearPBStyle);
    }

    public IndeterminateLinearProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTintedBackground(context, attrs);
        setAnimationDrawables();
    }

    private void setAnimationDrawables() {
        leadingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).mutate();
        trailingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition_pink).mutate();
    }

    private void setTintedBackground(final Context context, final AttributeSet attrs) {
        setBackground(new ColorDrawable(Color.WHITE));
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(ViewCompat.getMeasuredWidthAndState(this), 15);
        transitionDrawableWidth = (int) (getMeasuredWidth() * TRANSITION_DRAWABLE_WIDTH_RATIO);
        setTransitionDrawablesBounds();
        createAnimationSet();
    }

    private void setTransitionDrawablesBounds() {
        if (leadingDrawable != null) {
            leadingDrawable.setBounds(getTransitionDrawableBoundRect());
        }
        if (trailingDrawable != null) {
            trailingDrawable.setBounds(getTransitionDrawableBoundRect());
        }
    }

    private Rect getTransitionDrawableBoundRect() {
        int width = transitionDrawableWidth;
        int height = getMeasuredHeight();
        return new Rect(0, 0, width, height);
    }

    private Rect getAnimationDrawableRect() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        return new Rect(0, 0, width, height);
    }

    private void createAnimationSet() {
        leadingAnim = new AnimatedTranslateDrawable(leadingDrawable, -transitionDrawableWidth, getMeasuredWidth());
        leadingAnim.setCallback(this);
        leadingAnim.setBounds(getAnimationDrawableRect());

        trailingAnim = new AnimatedTranslateDrawable(trailingDrawable, -(/*getMeasuredWidth()+*/ transitionDrawableWidth), getMeasuredWidth());
        trailingAnim.setCallback(this);
        trailingAnim.setBounds(getAnimationDrawableRect());

        startAnimation();
    }

    @Override
    public void setVisibility(final int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
            if (visibility == GONE || visibility == INVISIBLE) {
                endAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onWindowVisibilityChanged(final int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            pauseAnimation();
        } else {
            resumeAnimation();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        endAnimation();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        leadingAnim.draw(canvas);
        if (drawTrailingAnim) {
            trailingAnim.draw(canvas);
        }
    }

    @Override
    public void onScreenStateChanged(final int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == SCREEN_STATE_OFF) {
            pauseAnimation();
        } else {
            resumeAnimation();
        }
    }

    @Override
    protected boolean verifyDrawable(final Drawable who) {
        return who == leadingAnim || who == trailingAnim || super.verifyDrawable(who);
    }

    private void startAnimation() {
        if (leadingAnim != null && !leadingAnim.getAnimator().isRunning()) {
            leadingAnim.start();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawTrailingAnim = true;
                    if (trailingAnim != null && !trailingAnim.getAnimator().isRunning()) {
                        trailingAnim.start();
                    }
                }
            }, leadingAnim.getAnimator().getDuration() / 2);
        }
    }

    private void resumeAnimation() {
        if (leadingAnim != null) {
            leadingAnim.resume();
        }
        if (trailingAnim != null) {

            trailingAnim.resume();
        }
    }

    private void endAnimation() {
        drawTrailingAnim = false;
        leadingAnim.end();
        trailingAnim.end();
    }

    private void pauseAnimation() {
        leadingAnim.pause();
        trailingAnim.pause();
    }
}