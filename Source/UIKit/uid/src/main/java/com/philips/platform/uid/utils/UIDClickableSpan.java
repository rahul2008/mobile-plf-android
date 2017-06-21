/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.utils;

import android.content.res.ColorStateList;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.philips.platform.uid.R;

public class UIDClickableSpan extends ClickableSpan {
    private static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
    private static final int[] STATE_VISITED = {R.attr.uid_state_visited};

    protected Runnable clickRunnable;

    private boolean pressed;
    private boolean visited;
    private int defaultColor;
    private int pressedColor;
    private int visitedColor;

    private CharSequence tag;
    private ColorStateList colorList;
    private boolean shouldDrawUnderline = true;

    public UIDClickableSpan(Runnable clickRunnable) {
        this.clickRunnable = clickRunnable;
    }

    public void setColors(ColorStateList colorStateList) {
        colorList = colorStateList;
        if (colorList != null) {
            defaultColor = colorStateList.getDefaultColor();
            pressedColor = colorStateList.getColorForState(STATE_PRESSED, defaultColor);
            visitedColor = colorStateList.getColorForState(STATE_VISITED, defaultColor);
        }
    }

    public ColorStateList getColors() {
        return colorList;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setTag(CharSequence tag) {
        this.tag = tag;
    }

    public CharSequence getTag() {
        return tag;
    }

    public boolean isVisited() {
        return visited;
    }

    public void shouldDrawUnderline(boolean shouldDrawUnderline) {
        this.shouldDrawUnderline = shouldDrawUnderline;
    }

    @Override
    public void onClick(View widget) {
        setVisited(true);
        if (clickRunnable != null) {
            clickRunnable.run();
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(getLinkColor(ds.linkColor));
        ds.setUnderlineText(shouldDrawUnderline);
    }

    private int getLinkColor(int defColor) {
        int linkColor = defColor;
        if (colorList != null) {
            linkColor = defaultColor;
            if (pressed) {
                linkColor = pressedColor;
            } else if (isVisited()) {
                linkColor = visitedColor;
            }
        }
        return linkColor;
    }
}