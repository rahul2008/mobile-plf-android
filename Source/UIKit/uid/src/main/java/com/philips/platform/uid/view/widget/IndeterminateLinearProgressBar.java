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
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.AnimatedTranslateDrawable;

public class IndeterminateLinearProgressBar extends View {
    private int suggestedMinHeight;

    private Drawable leadingDrawable;
    private Drawable trailingDrawable;

    private int transitionDrawableWidth;

    private static final float TRANSITION_DRAWABLE_WIDTH_RATIO = 0.4F;
    private AnimatedTranslateDrawable anim;

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
        trailingDrawable = ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).mutate();
    }

    private void setTintedBackground(final Context context, final AttributeSet attrs) {
        setBackground(new ColorDrawable(Color.MAGENTA));
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = Math.max(getMinimumWidth(), suggestedMinHeight);
        transitionDrawableWidth = (int) (getMeasuredWidth() * TRANSITION_DRAWABLE_WIDTH_RATIO);
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
        anim = new AnimatedTranslateDrawable(leadingDrawable, -transitionDrawableWidth, getMeasuredWidth()+ transitionDrawableWidth);
        anim.start();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        leadingDrawable.draw(canvas);
        anim.draw(canvas);
    }
}