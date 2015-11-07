package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

public class PhilipsBadgeView extends TextView {

    private static final int DEFAULT_BADGE_COLOR = Color.parseColor("#CD202C");
    private int width;
    private int height;

    public PhilipsBadgeView(final Context context) {
        this(context, null);
    }

    public PhilipsBadgeView(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    public PhilipsBadgeView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setGravity(Gravity.CENTER);
        handleTextChangeListener();
        setTextColor(context.getResources().getColor(android.R.color.white));
    }


    private void handleTextChangeListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateView(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to setBackgroundDrawable(): sticking with deprecated API for now
    private void validateView(CharSequence s) {
        if (s.length() > 1) {
            setBackgroundDrawable(getSquareRoundBackground());
        } else {
            setBackgroundDrawable(getCircleBackground());
        }
    }

    private ShapeDrawable getSquareRoundBackground() {
        int r = dipToPixels(15);
        float[] outerR = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        setSquareRoundParams(shapeDrawable);
        shapeDrawable.getPaint().setColor(DEFAULT_BADGE_COLOR);
        return shapeDrawable;
    }

    private void setSquareRoundParams(ShapeDrawable shapeDrawable) {
        width = this.getWidth();
        height = this.getHeight();
        Resources resources = getContext().getResources();
        int defaultWidth = (int) resources.getDimension(R.dimen.uikit_notification_label_square_round_width);
        int defaultHeight = (int) resources.getDimension(R.dimen.uikit_notification_label_square_round_height);

        if (width == height) {
            shapeDrawable.setIntrinsicWidth(defaultWidth);
            shapeDrawable.setIntrinsicHeight(defaultHeight);
        } else {

            if (width != ViewGroup.LayoutParams.WRAP_CONTENT && width != 0)
                shapeDrawable.setIntrinsicWidth(width);
            else
                shapeDrawable.setIntrinsicWidth(defaultWidth);


            if (height != ViewGroup.LayoutParams.WRAP_CONTENT && height != 0)
                shapeDrawable.setIntrinsicHeight(height);
            else
                shapeDrawable.setIntrinsicHeight(defaultHeight);
        }
    }

    private ShapeDrawable getCircleBackground() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(DEFAULT_BADGE_COLOR);
        setCircleBackgroundParams(shapeDrawable);
        return shapeDrawable;
    }

    private void setCircleBackgroundParams(ShapeDrawable shapeDrawable) {
        width = this.getWidth();
        height = this.getHeight();
        Resources resources = getContext().getResources();
        int defaultWidth = (int) resources.getDimension(R.dimen.uikit_notification_label_default_width);
        int defaultHeight = (int) resources.getDimension(R.dimen.uikit_notification_label_default_height);

        if (width != height) {
            shapeDrawable.setIntrinsicWidth(defaultWidth);
            shapeDrawable.setIntrinsicHeight(defaultHeight);
        } else {
            if (width != ViewGroup.LayoutParams.WRAP_CONTENT && width != 0)
                shapeDrawable.setIntrinsicWidth(width);
            else
                shapeDrawable.setIntrinsicWidth((int) resources.getDimension(R.dimen.uikit_notification_label_default_width));

            if (height != ViewGroup.LayoutParams.WRAP_CONTENT && height != 0)
                shapeDrawable.setIntrinsicHeight(height);
            else
                shapeDrawable.setIntrinsicHeight((int) resources.getDimension(R.dimen.uikit_notification_label_default_height));
        }
    }

    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

}
