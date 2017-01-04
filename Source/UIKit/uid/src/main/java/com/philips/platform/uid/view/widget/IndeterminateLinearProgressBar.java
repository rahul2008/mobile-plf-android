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
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.AnimatedTranslateDrawable;

public class IndeterminateLinearProgressBar extends View {
    private String TAG = IndeterminateLinearProgressBar.class.getName();
    private int suggestedMinHeight;

    private Drawable leadingDrawable;
    private Drawable trailingDrawable;

    private int transitionDrawableWidth;
    private int transitionWhiteSpaceWidth;
    private boolean drawTrailingAnim;

    private static final float TRANSITION_DRAWABLE_WIDTH_RATIO = 0.4F;
    private AnimatedTranslateDrawable leadingAnim;
    private AnimatedTranslateDrawable trailingAnim;

    public IndeterminateLinearProgressBar(final Context context) {
        super(context, null);
    }

    public IndeterminateLinearProgressBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        suggestedMinHeight = context.getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_height);
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
        int height = Math.max(getMinimumWidth(), suggestedMinHeight);
        transitionDrawableWidth = (int) (getMeasuredWidth() * TRANSITION_DRAWABLE_WIDTH_RATIO);
        transitionWhiteSpaceWidth = getMeasuredWidth() - transitionDrawableWidth;
        setMeasuredDimension(getMeasuredWidthAndState(), height);
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

    private void createAnimationSet() {
        leadingAnim = new AnimatedTranslateDrawable(leadingDrawable, -transitionDrawableWidth, 2* getMeasuredWidth());
        leadingAnim.setCallback(this);
        leadingAnim.setBounds(getTransitionDrawableBoundRect());

        trailingAnim = new AnimatedTranslateDrawable(trailingDrawable, -(getMeasuredWidth()+ transitionDrawableWidth), getMeasuredWidth());
        trailingAnim.setCallback(this);
        trailingAnim.setBounds(getTransitionDrawableBoundRect());

        startAnimation();
    }

    private void startAnimation() {
        if (leadingAnim != null && !leadingAnim.getAnimator().isRunning()) {
            leadingAnim.start();
        }
        drawTrailingAnim = true;
        if (trailingAnim != null && !trailingAnim.getAnimator().isRunning()) {
            trailingAnim.start();
        }
       /* postDelayed(new Runnable() {
            @Override
            public void run() {
                drawTrailingAnim = true;
                if (trailingAnim != null && !trailingAnim.getAnimator().isRunning()) {
                    trailingAnim.start();
                }
            }
        }, 100 * 50);*/
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
        Log.d(TAG, "onWindowVisibilityChanged: " + visibility + " isshown:" + isShown() + " iswindowvi:" + getWindowVisibility());

        if (visibility == GONE || visibility == INVISIBLE) {
            endAnimation();
        } else {
            startAnimation();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        endAnimation();
        super.onDetachedFromWindow();
    }

    private void endAnimation() {
        drawTrailingAnim = false;
        leadingAnim.end();
        trailingAnim.end();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        Log.d("amit", "onDraw: ILP");
        super.onDraw(canvas);
        leadingAnim.draw(canvas);
        if (drawTrailingAnim) {
            trailingAnim.draw(canvas);
        }
    }

    @Override
    protected boolean verifyDrawable(final Drawable who) {
        return who == leadingAnim || who == trailingAnim || super.verifyDrawable(who);
    }
}