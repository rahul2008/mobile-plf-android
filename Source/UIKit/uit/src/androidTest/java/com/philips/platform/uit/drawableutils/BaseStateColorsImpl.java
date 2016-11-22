/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.drawableutils;

import android.graphics.drawable.Drawable;

import com.philips.platform.uit.utils.UITTestUtils;

public abstract class BaseStateColorsImpl implements GradientDrawableUtils.StateColors {

    protected Drawable drawable;
    protected Drawable.ConstantState constantState;

    private static final String CORNER_RADIUS_ARRAY = "mRadiusArray";
    private static final String CORNER_RADIUS = "mRadius";
    private static final String STROKE_WIDTH = "mStrokeWidth";

    private static final String RING_THICKNESS = "mThickness";
    private static final String RING_INNER_RADIUS = "mInnerRadius";
    private static final String STATE_COLORS = "mColors";

    public BaseStateColorsImpl(Drawable drawable) {
        this.drawable = drawable;
        constantState = drawable.getConstantState();
    }

    @Override
    public float[] getCornerRadius() {
        Drawable.ConstantState state = getConstantStateForRadius();
        float[] radiusArray = (float[]) GradientDrawableUtils.getField(state, CORNER_RADIUS_ARRAY);
        //try to find radius if array is null
        if (radiusArray == null) {
            float radius = (float) GradientDrawableUtils.getField(state, CORNER_RADIUS);
            radiusArray = new float[]{radius};
        }
        return radiusArray;
    }

    @Override
    public int getStrokeWidth() {
        Drawable.ConstantState state = getConstantStateForStrokeWidth();
        return (int) GradientDrawableUtils.getField(state, STROKE_WIDTH);
    }

    @Override
    public int getRippleRadius() {
        return 0;
    }

    @Override
    public int getRippleColor(final int attr) {
        return 0;
    }

    protected int getStrokeWidthFromConstantState(Drawable.ConstantState state) {
        return (int) GradientDrawableUtils.getField(state, STROKE_WIDTH);
    }

    @Override
    public int getInnerRadius() {
        return UITTestUtils.getIntegerFieldValueFromReflection(getConstantStateForRadius(), RING_THICKNESS);
    }

    @Override
    public int getRingThickness() {
        return UITTestUtils.getIntegerFieldValueFromReflection(getConstantStateForRadius(), RING_INNER_RADIUS);
    }

    @Override
    public int[] getColors() {
        return UITTestUtils.getIntegerArrayFromReflection(getConstantStateForRadius(), STATE_COLORS);
    }

    protected Drawable.ConstantState getConstantStateForRadius() {
        return constantState;
    }

    protected Drawable.ConstantState getConstantStateForStrokeWidth() {
        return constantState;
    }
}