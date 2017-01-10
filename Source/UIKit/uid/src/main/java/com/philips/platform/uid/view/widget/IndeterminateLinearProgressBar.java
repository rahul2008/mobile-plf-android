/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.AnimatedTranslateDrawable;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * <p></p>Provides custom implementation for indeterminate linear progress bar.
 * It uses animator to animate drawables across different end points.</p>
 * <p>
 * <p><b>Current Implementation:</b> The transition drawable is 40% of total widht of the progressBar.
 * It uses two sets of moving blocks. Second blocks stars when the first starts moving out of the visible frame.
 * As per DLS specs each block has gradient of 0-100-0, (start-center-end) color. It can be customized with custom attributes(Refer table below for details).
 * Though {@link GradientDrawable} provides way to set it, but the visual effects are not the same.<br>
 * To achieve this effect, two drawables are next to each other and other is mirror of first one(with reverse gradient) of same size.
 * Effectively the drawable and mirror drawable are 20% each(combined 40%) of total progressBar.<br>
 * <p>
 * With this effect animation runs for 2 * width. Currently the default time is 900ms to cover the visible area, which effectively translates to 2* 900 ms as
 * total distance covered is 2* width.
 * <p>
 * <p>
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
    Drawable leadingDrawable;
    Drawable leadingMirrorDrawable;
    Drawable trailingDrawable;
    Drawable trailingMirrorDrawable;
    AnimatedTranslateDrawable leadingAnim;
    AnimatedTranslateDrawable trailingAnim;

    private int transitionDrawableWidth;
    private int transitionExtraWhiteSpace;
    private boolean drawTrailingAnim;

    private static final float TRANSITION_DRAWABLE_WIDTH_RATIO = 0.4F;

    private int gradientStartColor;
    private int gradientCenterColor;
    private int gradientEndColor;
    private int gradientDuration;

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
     */
    protected Drawable getTrailingDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).getConstantState().newDrawable();
    }

    /**
     * Override this provide custom trailing mirror drawable.
     *
     * @return The drawable used as the trailing mirror drawable.
     */
    protected Drawable getTrailingMirrorDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition_mirror).getConstantState().newDrawable();
    }

    /**
     * Override this provide custom leading drawable.
     *
     * @return The drawable used as the leading drawable.
     */
    protected Drawable getLeadingDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition).getConstantState().newDrawable();
    }

    /**
     * Override this provide custom leading mirror drawable.
     *
     * @return The drawable used as the leading mirror drawable.
     */
    protected Drawable getLeadingMirrorDrawable() {
        return ContextCompat.getDrawable(getContext(), R.drawable.uid_progess_bar_linear_transition_mirror).getConstantState().newDrawable();
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
     */
    protected int getDesiredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.uid_progress_bar_height);
    }

    /**
     * Sets the bounds for the progressDrawable.
     *
     * @return Returns {@link Rect} which will be set as bounds for transition drawable.
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
     */
    protected Animator getTrailingAnimator() {
        if (trailingAnim != null) {
            return trailingAnim.getAnimator();
        }
        return null;
    }

    private void createAnimationSet() {
        endAnimation();
        leadingAnim = new AnimatedTranslateDrawable(leadingDrawable, leadingMirrorDrawable, getLeadingAnimationStartPos(), getLeadingAnimationEndPos());
        trailingAnim = new AnimatedTranslateDrawable(leadingDrawable, trailingMirrorDrawable, getTrailingAnimationStartOffset(), getTrailingAnimationEndOffset());
        setAnimationProperties(leadingAnim);
        setAnimationProperties(trailingAnim);

        startAnimation();
    }

    /**
     * Start offset for leading animation
     *
     * @return Offset to be applied as animation start point.
     */
    protected int getLeadingAnimationStartPos() {
        return -transitionDrawableWidth;
    }

    /**
     * End offset for leading animation
     *
     * @return Offset to be applied as animation end point.
     */
    protected int getLeadingAnimationEndPos() {
        return getMeasuredWidth() + transitionExtraWhiteSpace;
    }

    /**
     * Start offset for trailing animation
     *
     * @return Offset to be applied as animation start point.
     */
    protected int getTrailingAnimationStartOffset() {
        return -transitionDrawableWidth;
    }

    /**
     * End offset for trailing animation
     *
     * @return Offset to be applied as animation end point.
     */
    protected int getTrailingAnimationEndOffset() {
        return getMeasuredWidth() + transitionExtraWhiteSpace;
    }

    private void setAnimationProperties(final AnimatedTranslateDrawable translateDrawable) {
        translateDrawable.setCallback(this);
        translateDrawable.getAnimator().setDuration(gradientDuration * getAnimationTravelRatio());
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

    /**
     * Can be overriden if the animator properties are changed after animation start.
     * Call {@link #endAnimation()} before calling {@link #startAnimation()}
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
     * Call {@link #endAnimation()} before calling {@link #startAnimation()}
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
    private void pauseAnimation() {
        leadingAnim.pause();
        trailingAnim.pause();
    }

    private int getAnimationTravelRatio() {
        return (getLeadingAnimationEndPos() - getLeadingAnimationStartPos()) / getMeasuredWidth();
    }

    @Override
    protected boolean verifyDrawable(final Drawable who) {
        return who == leadingAnim || who == trailingAnim || super.verifyDrawable(who);
    }
}