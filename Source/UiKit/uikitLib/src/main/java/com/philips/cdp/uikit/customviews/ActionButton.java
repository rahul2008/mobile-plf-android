package com.philips.cdp.uikit.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionButton extends ImageButton {

    private static final int SQUARE = 0;
    private static final int CIRCLE = 1;

    public ActionButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.actionButtonStyleRef);
    }

    public ActionButton(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context, attrs, defStyle);
    }

    private void initializeView(final Context context, final AttributeSet attrs, final int defStyle) {
        Resources resources = context.getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ActionButton, defStyle,
                R.style.Philips_ActionButton);

        int resID = getResID(typedArray);
        if (resID > 0)
            setImageDrawable(VectorDrawable.create(context, resID));

        addStates(getNormalStateDrawable(typedArray, resources), getPressedStateDrawable(typedArray, resources));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setShadowing(resources, typedArray);

        typedArray.recycle();

        setScaleType(ScaleType.CENTER);
    }

    private void setShadowing(Resources resources, TypedArray typedArray) {
        float shadowValue = 0;

        if (typedArray.getBoolean(R.styleable.ActionButton_actionButtonShadow, false))
            shadowValue = resources.getDimension(R.dimen.uikit_action_button_shadow_radius);

        setShadow(shadowValue);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setShadow(float value) {
        setElevation(value);
    }

    private int getResID(final TypedArray typedArray) {
        String resPath = typedArray.getString(R.styleable.ActionButton_actionButtonImageDrawable);
        if (resPath != null)
            return VectorDrawableImageView.getResourceID(resPath.substring(resPath.lastIndexOf("/") + 1, resPath.lastIndexOf(".")), R.drawable.class);
        else
            return 0;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private GradientDrawable getShapeDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable;
        int shapeValue = typedArray.getInt(R.styleable.ActionButton_actionButtonShape, SQUARE);
        switch (shapeValue) {
            case SQUARE:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_square);
                break;
            case CIRCLE:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_circle);
                break;
            default:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_square);
                break;
        }
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to resources.getColor(): sticking with deprecated API for now
    private GradientDrawable getNormalStateDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray, resources);
        int color = typedArray.getColor(R.styleable.ActionButton_actionButtonBgColor, resources.getColor(R.color.uikit_philips_bright_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to resources.getColor(): sticking with deprecated API for now
    private GradientDrawable getPressedStateDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray, resources);
        int color = typedArray.getColor(R.styleable.ActionButton_actionButtonBgColorPressed, resources.getColor(R.color.uikit_philips_dark_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void addStates(final GradientDrawable normal, final GradientDrawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                pressed);
        states.addState(new int[]{},
                normal);
        setBackgroundDrawable(states);
    }
}