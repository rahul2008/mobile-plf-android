package com.philips.cdp.uikit.CustomButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsButton extends ImageButton {

    private static final int SQUARE = 0;
    private static final int CIRCLE = 1;
    private Context context;

    public PhilipsButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        drawView(attrs);
    }

    private void drawView(final AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhilipsView);
        ShapeDrawable normalShapeDrawable = getNormalShapeDrawable(typedArray);
        ShapeDrawable pressedShapeDrawable = getPressedShapeDrawable(typedArray);
        setImageResource(typedArray.getResourceId(R.styleable.PhilipsView_btnImageDrawable, R.drawable.greater));
        typedArray.recycle();
        addStates(normalShapeDrawable, pressedShapeDrawable);
    }

    private ShapeDrawable getShapeDrawable(final TypedArray typedArray) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        Shape shape;
        int shapeValue = typedArray.getInt(R.styleable.PhilipsView_btnShape, SQUARE);
        switch (shapeValue) {
            case SQUARE:
                shape = new RectShape();
                shapeDrawable.setShape(shape);
                break;
            case CIRCLE:
                shape = new OvalShape();
                shapeDrawable.setShape(shape);
                break;
            default:
                shape = new RectShape();
                shapeDrawable.setShape(shape);
                break;
        }
        return shapeDrawable;
    }

    private ShapeDrawable getNormalShapeDrawable(final TypedArray typedArray) {
        ShapeDrawable shapeDrawable = getShapeDrawable(typedArray);
        int color = typedArray.getColor(R.styleable.PhilipsView_btnBgColor, context.getResources().getColor(R.color.philips_dark_blue));
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    private ShapeDrawable getPressedShapeDrawable(final TypedArray typedArray) {
        ShapeDrawable shapeDrawable = getShapeDrawable(typedArray);
        int color = typedArray.getColor(R.styleable.PhilipsView_btnBgColorPressed, context.getResources().getColor(R.color.philips_bright_blue));
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    private StateListDrawable addStates(final ShapeDrawable normal, final ShapeDrawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                pressed);
        states.addState(new int[]{},
                normal);
        setBackgroundDrawable(states);
        return states;
    }
}
