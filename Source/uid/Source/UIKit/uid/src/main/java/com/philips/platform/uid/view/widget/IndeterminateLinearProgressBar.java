/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.AnimatedTranslateDrawable;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * <p>Provides custom implementation for indeterminate linear progress bar.
 * It uses animator to animate drawables across different end points.</p>
 * <p><b>Current Implementation:</b> The transition drawable is 40% of total width of the progressBar.
 * It uses two sets of moving blocks. Second blocks starts when the first starts moving out of the visible frame.
 * As per DLS specs each block has gradient of 0-100-0, (start-center-end) color. It can be customized with custom attributes(Refer table below for details).
 * Though {@link GradientDrawable} provides way to set it, but the visual effects are not the same.<br>
 * To achieve this effect, two drawables are next to each other and other is mirror of first one(with reverse gradient) of same size.
 * Effectively the drawable and mirror drawable are 20% each(combined 40%) of total progressBar.<br>
 * </p>
 * <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidIndeterminateLinearProgressBGColor</td>
 * <td rowspan="1">Background color for the progressBar </td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidIndeterminateLinearProgressBGAlpha</td>
 * <td rowspan="1">Alpha to be applied on the background color</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidIndeterminateLinearProgressStartColor</td>
 * <td rowspan="1">Applied if the transition drawable is GradientDrawable. Sets the start color for drawable.</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidIndeterminateLinearProgressCenterColor</td>
 * <td rowspan="1">Applied if the transition drawable is GradientDrawable. Sets the center color for drawable.</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidIndeterminateLinearProgressEndColor</td>
 * <td rowspan="1">Applied if the transition drawable is GradientDrawable. Sets the end color for drawable.</td
 * </tr>
 * <tr>
 * <td rowspan="1">uidIndeterminateLinearProgressAnimDuration</td>
 * <td rowspan="1">Sets the animation duration for the transition of indeterminate progressbar.</td
 * </tr>
 * <p>
 * </tbody>
 * <p>
 * </table>
 */
public class IndeterminateLinearProgressBar extends View {
    private static final float TRANSITION_DRAWABLE_WIDTH_RATIO = 0.4F;
    Drawable leadingDrawable;
    Drawable leadingMirrorDrawable;
    Drawable trailingDrawable;
    Drawable trailingMirrorDrawable;
    AnimatedTranslateDrawable leadingAnim;
    AnimatedTranslateDrawable trailingAnim;
    private int transitionDrawableWidth;
    private int transitionExtraWhiteSpace;
    private boolean drawTrailingAnim;
    private int gradientStartColor;
    private int gradientCenterColor;
    private int gradientEndColor;
    private int gradientDuration;

    public IndeterminateLinearProgressBar(@NonNull final Context context) {
        this(context, null);
    }

    public IndeterminateLinearProgressBar(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, R.attr.uidIndeterminateLinearPBStyle);
    }

    public IndeterminateLinearProgressBar(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttributes(context, attrs, defStyleAttr);
        setTransitionDrawables();
    }

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDIndeterminateLinearProgressBar, defStyleAttr, R.style.UIDIndeterminateLinearProgress);
        final int bgColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressBGColor, Color.WHITE);
        setTintedBackground(bgColor);

        gradientStartColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressStartColor, Color.WHITE);
        gradientCenterColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressCenterColor, Integer.MIN_VALUE);
        gradientEndColor = obtainStyledAttributes.getColor(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressEndColor, Color.WHITE);
        gradientDuration = obtainStyledAttributes.getInt(R.styleable.UIDIndeterminateLinearProgressBar_uidIndeterminateLinearProgressAnimDuration, 0);
        obtainStyledAttributes.recycle();
    }

    private void setTransitionDrawables() {
        leadingDrawable = getLeadingDrawable();
        leadingMirrorDrawable = getLeadingMirrorDrawable();
        trailingDrawable = getTrailingDrawable();
        trailingMirrorDrawable = getTrailingMirrorDrawable();
        setTransitionColorGradients(leadingDrawable);
        setTransitionColorMirrorGradients(leadingMirrorDrawable);
        setTransitionColorGradients(trailingDrawable);
        setTransitionColorMirrorGradients(trailingMirrorDrawable);
    }

    private void setTransitionColorGradients(Drawable drawable) {
        if (drawable instanceof GradientDrawable) {
            int[] colors = new int[]{gradientStartColor, gradientCenterColor};
            ((GradientDrawable) drawable).setColors(colors);
        }
    }

    private void setTransitionColorMirrorGradients(Drawable drawable) {
        if (drawable instanceof GradientDrawable) {
            int[] colors = new int[]{gradientCenterColor, gradientEndColor};
            ((GradientDrawable) drawable).setColors(colors);
        }
    }

    /**
     * Override this provide custom trailing drawable.
     *
     * @return The drawable used as the trailing drawable.
     * @since 3.0.0
     */
    @Nullable
    protected Drawable getTrailingDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).getConstantState().newDrawable();
    }

    /**
     * Override this to provide custom trailing mirror drawable.
     *
     * @return The drawable used as the trailing mirror drawable.
     * @since 3.0.0
     */
    @Nullable
    protected Drawable getTrailingMirrorDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition_mirror).getConstantState().newDrawable();
    }

    /**
     * Override this to provide custom leading drawable.
     *
     * @return The drawable used as the leading drawable.
     * @since 3.0.0
     */
    @Nullable
    protected Drawable getLeadingDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).getConstantState().newDrawable();
    }

    /**
     * Override this to provide custom leading mirror drawable.
     *
     * @return The drawable used as the leading mirror drawable.
     * @since 3.0.0
     */
    @Nullable
    protected Drawable getLeadingMirrorDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition_mirror).getConstantState().newDrawable();
    }

    private void setTintedBackground(int color) {
        setBackground(new ColorDrawable(color));
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

    private void createAnimationSet() {
        endAnimation();

        boolean isRTL = UIDUtils.isLayoutRTL(this);
        int startPos =  isRTL ? getLeadingAnimationEndPos(): getLeadingAnimationStartPos();
        int endPos =  isRTL ? getLeadingAnimationStartPos(): getLeadingAnimationEndPos();
        leadingAnim = new AnimatedTranslateDrawable(leadingDrawable, leadingMirrorDrawable, startPos, endPos);

        int startOffset =  isRTL ? getTrailingAnimationEndOffset(): getTrailingAnimationStartOffset();
        int endOffset =  isRTL ? getTrailingAnimationStartOffset(): getTrailingAnimationEndOffset();
        trailingAnim = new AnimatedTranslateDrawable(leadingDrawable, trailingMirrorDrawable, startOffset, endOffset);
        setAnimationProperties(leadingAnim);
        setAnimationProperties(trailingAnim);

        startAnimation();
    }

    private void setTransitionDrawablesBounds() {
        if (leadingDrawable != null) {
            leadingDrawable.setBounds(getTransitionDrawableBoundRect());
        }

        if (leadingMirrorDrawable != null) {
            leadingMirrorDrawable.setBounds(getTransitionMirrorDrawableBoundRect());
        }

        if (trailingDrawable != null) {
            trailingDrawable.setBounds(getTransitionDrawableBoundRect());
        }

        if (trailingMirrorDrawable != null) {
            trailingMirrorDrawable.setBounds(getTransitionMirrorDrawableBoundRect());
        }
    }

    /**
     * Set the height for the progress bar.
     *
     * @return Height that will be set to the progressbar
     * @since 3.0.0
     */
    protected int getDesiredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_height);
    }

    /**
     * Sets the bounds for the progressDrawable.
     *
     * @return Returns {@link Rect} which will be set as bounds for transition drawable.
     * @since 3.0.0
     */
    protected Rect getTransitionDrawableBoundRect() {
        int width = transitionDrawableWidth / 2;
        int height = getMeasuredHeight();
        return new Rect(0, 0, width, height);
    }

    /**
     * Sets the bounds for the progressDrawable. Set this to empty rect if no mirror drawable is required.
     *
     * @return Returns {@link Rect} which will be set as bounds for transition mirror drawable.
     * @since 3.0.0
     */
    protected Rect getTransitionMirrorDrawableBoundRect() {
        int width = transitionDrawableWidth / 2;
        int height = getMeasuredHeight();
        return new Rect(width, 0, 2 * width, height);
    }

    private Rect getAnimationDrawableRect() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        return new Rect(0, 0, width, height);
    }

    /**
     * Retrieves animator associated with leading animation. Properties can be modified for animator.
     *
     * @return Animator linked to leadingAnim.
     * @since 3.0.0
     */
    protected Animator getLeadingAnimator() {
        if (leadingAnim != null) {
            return leadingAnim.getAnimator();
        }
        return null;
    }

    /**
     * Retrieves animator associated with trailing animation. Properties can be modified for animator.
     *
     * @return Animator linked to trailingAnim.
     * @since 3.0.0
     */
    protected Animator getTrailingAnimator() {
        if (trailingAnim != null) {
            return trailingAnim.getAnimator();
        }
        return null;
    }

    /**
     * Start offset for leading animation
     *
     * @return Offset to be applied as animation start point.
     * @since 3.0.0
     */
    protected int getLeadingAnimationStartPos() {
        return -transitionDrawableWidth;
    }

    /**
     * End offset for leading animation
     *
     * @return Offset to be applied as animation end point.
     * @since 3.0.0
     */
    protected int getLeadingAnimationEndPos() {
        return getMeasuredWidth() + transitionExtraWhiteSpace;
    }

    /**
     * Start offset for trailing animation
     *
     * @return Offset to be applied as animation start point.
     * @since 3.0.0
     */
    protected int getTrailingAnimationStartOffset() {
        return -transitionDrawableWidth;
    }

    /**
     * End offset for trailing animation
     *
     * @return Offset to be applied as animation end point.
     * @since 3.0.0
     */
    protected int getTrailingAnimationEndOffset() {
        return getMeasuredWidth() + transitionExtraWhiteSpace;
    }

    private void setAnimationProperties(final AnimatedTranslateDrawable translateDrawable) {
        translateDrawable.setCallback(this);
        translateDrawable.getAnimator().setDuration(gradientDuration * getAnimationTravelRatio());
        translateDrawable.setBounds(getAnimationDrawableRect());
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onWindowVisibilityChanged(final int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            pauseAnimation();
        } else {
            resumeAnimation();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDetachedFromWindow() {
        endAnimation();
        super.onDetachedFromWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        leadingAnim.draw(canvas);
        if (drawTrailingAnim) {
            trailingAnim.draw(canvas);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onScreenStateChanged(final int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == SCREEN_STATE_OFF) {
            pauseAnimation();
        } else {
            resumeAnimation();
        }
    }

    /**
     * Can be overriden if the animator properties are changed after animation start.
     * Call {@link #endAnimation()} before calling start.
     */
    protected void startAnimation() {
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
            }, gradientDuration);
        }
    }

    /**
     * Resumes the animation if its paused otherwise animation is started.
     */
    protected void resumeAnimation() {
        if (leadingAnim != null) {
            leadingAnim.resume();
        }
        if (trailingAnim != null) {
            trailingAnim.resume();
        }
    }

    /**
     * Can be overriden if the animator properties are changed after animation start.
     * Call end before calling {@link #startAnimation()}
     */
    protected void endAnimation() {
        drawTrailingAnim = false;
        if (leadingAnim != null) {
            leadingAnim.end();
        }
        if (trailingAnim != null) {
            trailingAnim.end();
        }
    }

    /**
     * Pauses the animation if its running, else it has no effect.
     */
    protected void pauseAnimation() {
        if (leadingAnim != null) {
            leadingAnim.pause();
        }
        if (trailingAnim != null) {
            trailingAnim.pause();
        }
    }

    private int getAnimationTravelRatio() {
        return (getLeadingAnimationEndPos() - getLeadingAnimationStartPos()) / getMeasuredWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verifyDrawable(@NonNull final Drawable who) {
        return who == leadingAnim || who == trailingAnim || super.verifyDrawable(who);
    }
}