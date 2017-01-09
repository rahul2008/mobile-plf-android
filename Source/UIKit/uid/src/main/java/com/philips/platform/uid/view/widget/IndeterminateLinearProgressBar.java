/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.AnimatedTranslateDrawable;
import com.philips.platform.uid.utils.UIDUtils;

public class IndeterminateLinearProgressBar extends View {
    Drawable leadingDrawable;
    Drawable trailingDrawable;

    private int transitionDrawableWidth;
    private int transitionExtraWhiteSpace;
    private boolean drawTrailingAnim;

    private static final float TRANSITION_DRAWABLE_WIDTH_RATIO = 0.4F;
    private AnimatedTranslateDrawable leadingAnim;
    private AnimatedTranslateDrawable trailingAnim;

    private int transitionStartColor;
    private int transitionCenterColor;
    private int transitionEndColor;
    private int transitionDuration;

    public IndeterminateLinearProgressBar(final Context context) {
        this(context, null);
    }

    public IndeterminateLinearProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidIndeterminateLinearPBStyle);
    }

    public IndeterminateLinearProgressBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttributes(context, attrs, defStyleAttr);
        setTransitionDrawables();
    }

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDIndeterminateLinearProgressBar, defStyleAttr, R.style.UIDIndeterminateLinearProgress);
        final int bgColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressBGColor, Color.WHITE);
        final float bgColorAlpha = obtainStyledAttributes.getFloat(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressBGAlpha, 1.0f);
        setTintedBackground(bgColor, bgColorAlpha);

        transitionStartColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressStartColor, Color.WHITE);
        transitionCenterColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressCenterColor, Integer.MIN_VALUE);
        transitionEndColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressEndColor, Color.WHITE);
        transitionDuration = obtainStyledAttributes.getInt(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressAnimDuration, (int) AnimatedTranslateDrawable.DEFAULT_ANIMATION_DURATION);
        obtainStyledAttributes.recycle();
    }

    private void setTransitionDrawables() {
        leadingDrawable = getLeadingDrawable();
        trailingDrawable = getTrailingDrawable();
        setTransitionColorGradients(leadingDrawable);
        setTransitionColorGradients(trailingDrawable);
    }

    private void setTransitionColorGradients(Drawable drawable) {
        if (drawable instanceof GradientDrawable) {
            int[] colors;
            if (transitionCenterColor != Integer.MIN_VALUE) {
                colors = new int[]{transitionStartColor, transitionCenterColor, transitionEndColor};
            } else {
                colors = new int[]{transitionStartColor, transitionEndColor};
            }
            ((GradientDrawable) leadingDrawable).setColors(colors);
        }
    }

    protected Drawable getTrailingDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).getConstantState().newDrawable();
    }

    protected Drawable getLeadingDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).getConstantState().newDrawable();
    }

    private void setTintedBackground(int color, float alpha) {
        setBackground(new ColorDrawable(UIDUtils.modulateColorAlpha(color, alpha)));
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = Math.max(getMinimumHeight(), getDesiredHeight());
        setMeasuredDimension(ViewCompat.getMeasuredWidthAndState(this), height);
        transitionDrawableWidth = (int) (getMeasuredWidth() * TRANSITION_DRAWABLE_WIDTH_RATIO);
        transitionExtraWhiteSpace = getMeasuredWidth() - transitionDrawableWidth;
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

    protected Rect getTransitionDrawableBoundRect() {
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
        leadingAnim = new AnimatedTranslateDrawable(leadingDrawable, -transitionDrawableWidth, getMeasuredWidth() + transitionExtraWhiteSpace);
        trailingAnim = new AnimatedTranslateDrawable(leadingDrawable, -transitionDrawableWidth, getMeasuredWidth() + transitionExtraWhiteSpace);
        setAnimationProperties(leadingAnim);
        setAnimationProperties(trailingAnim);

        startAnimation();
    }

    private void setAnimationProperties(final AnimatedTranslateDrawable translateDrawable) {
        translateDrawable.setCallback(this);
        translateDrawable.getAnimator().setDuration(transitionDuration);
        translateDrawable.setBounds(getAnimationDrawableRect());
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

    protected int getDesiredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_height);
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