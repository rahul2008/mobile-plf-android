/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.text.utils;

import android.content.res.ColorStateList;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.philips.platform.uid.R;

/**
 * Custom implementation of {@link ClickableSpan} to support different color for states.
 */
public class UIDClickableSpan extends ClickableSpan {
    private static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
    private static final int[] STATE_VISITED = {R.attr.uid_state_visited};

    private Runnable clickRunnable;

    private boolean isPressed;
    private boolean isVisited;
    private int defaultColor;
    private int pressedColor;
    private int visitedColor;

    private CharSequence tag;
    private ColorStateList colorList;
    private boolean shouldDrawUnderline = true;

    /**
     * Init the span with custom {@link Runnable}.
     *
     * @param clickRunnable to be called on click
     *                      @since 3.0.0
     */
    public UIDClickableSpan(Runnable clickRunnable) {
        this.clickRunnable = clickRunnable;
    }

    /**
     * Set the colors for the span.<br>
     * Supported states are default color, {@code android.R.attr.state_pressed} and {@code R.attr.uid_state_visited}
     *
     * @param colorStateList for the span
     *                       @since 3.0.0
     */
    public void setColors(ColorStateList colorStateList) {
        colorList = colorStateList;
        if (colorList != null) {
            defaultColor = colorStateList.getDefaultColor();
            pressedColor = colorStateList.getColorForState(STATE_PRESSED, defaultColor);
            visitedColor = colorStateList.getColorForState(STATE_VISITED, defaultColor);
        }
    }

    /**
     * Returns colors applied on span.
     *
     * @return colors applied on span.
     * @since 3.0.0
     */
    public ColorStateList getColors() {
        return colorList;
    }

    /**
     * Changes the state to pressed.
     *
     * @param pressed to mark as pressed.
     *                @since 3.0.0
     */
    public void setPressed(boolean pressed) {
        this.isPressed = pressed;
    }

    /**
     * Changes the state to visited.
     *
     * @param visited to mark as visited.
     *                @since 3.0.0
     */
    public void setVisited(boolean visited) {
        this.isVisited = visited;
    }

    /**
     * Tag can be set on the span for the unique identification.
     *
     * @param tag for the span.
     *            @since 3.0.0
     */
    public void setTag(CharSequence tag) {
        this.tag = tag;
    }

    /**
     * Tag associated with the span.
     *
     * @return tag with the span
     * @since 3.0.0
     */
    public CharSequence getTag() {
        return tag;
    }

    /**
     * Returns visited state of span.
     *
     * @return visited or not.
     * @since 3.0.0
     */
    public boolean isVisited() {
        return isVisited;
    }

    /**
     * Returns pressed state of span.
     *
     * @return pressed or not.
     * @since 3.0.0
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Sets whether to include underline in span, defaults to true.
     *
     * @param shouldDrawUnderline draw underline or not.
     *                            @since 3.0.0
     */
    @SuppressWarnings("unused")
    public void shouldDrawUnderline(boolean shouldDrawUnderline) {
        this.shouldDrawUnderline = shouldDrawUnderline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View widget) {
        setVisited(true);
        if (clickRunnable != null) {
            clickRunnable.run();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(getLinkColor(ds.linkColor));
        ds.setUnderlineText(shouldDrawUnderline);
    }

    private int getLinkColor(int defColor) {
        int linkColor = defColor;
        if (colorList != null) {
            linkColor = defaultColor;
            if (isPressed) {
                linkColor = pressedColor;
            } else if (isVisited()) {
                linkColor = visitedColor;
            }
        }
        return linkColor;
    }
}