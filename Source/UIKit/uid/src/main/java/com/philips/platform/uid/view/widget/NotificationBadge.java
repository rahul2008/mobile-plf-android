/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;

import com.philips.platform.uid.R;

public class NotificationBadge extends TextView {

    private final Resources resources;
    private int DEFAULT_BADGE_COLOR;
    private boolean isSmallSize;

    public NotificationBadge(Context context) {
        this(context, (AttributeSet) null, android.R.attr.textViewStyle);
    }

    public NotificationBadge(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NotificationBadge(Context context, View target) {
        this(context, null, android.R.attr.textViewStyle, target, 0);
    }

    public NotificationBadge(Context context, TabWidget target, int index) {
        this(context, null, android.R.attr.textViewStyle, target, index);
    }

    public NotificationBadge(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null, 0);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    public NotificationBadge(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
        super(context, attrs, defStyle);
        validateIsSmallView(attrs, getContext());
        resources = getResources();
        DEFAULT_BADGE_COLOR = resources.getColor(R.color.uidColorRed);

        if (getText().length() > 2)
            setBackgroundDrawable(getSquareRoundBackground());
        else
            setBackgroundDrawable(getCircleBackground());

        setGravity(Gravity.CENTER);
        handleTextChangeListener();
        setTextColor(resources.getColor(android.R.color.white));
    }

    private void applyTo(View target, int width, int height) {
        ViewGroup.LayoutParams lp = target.getLayoutParams();
        if (lp == null)
            lp = new ViewGroup.LayoutParams(width, height);

        target.setMinimumHeight(height);
        target.setMinimumWidth(width);
        target.setLayoutParams(lp);
    }


    private void validateIsSmallView(AttributeSet attrs, Context context) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.notification_badge, 0, 0);
        isSmallSize = a.getBoolean(R.styleable.notification_badge_uid_notification_badge_small, false);
        a.recycle();
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
        if (s.length() > 2) {
            setBackgroundDrawable(getSquareRoundBackground());
        } else {
            setBackgroundDrawable(getCircleBackground());
        }
    }

    @NonNull
    private ShapeDrawable getSquareRoundBackground() {
        int r;
        if (isSmallSize)
            r = dipToPixels(resources.getInteger(R.integer.uid_notification_badge_view_small_size_radius));
        else
            r = dipToPixels(resources.getInteger(R.integer.uid_notification_badge_view_default_size_radius));

        float[] outerR = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = setSquareParams(roundRectShape);
        return shapeDrawable;
    }

    @NonNull
    private ShapeDrawable setSquareParams(RoundRectShape roundRectShape) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(DEFAULT_BADGE_COLOR);
                applyTo(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int padding = (int) resources.getDimension(R.dimen.uid_notification_badge_square_round_padding);
                setPadding(padding
                        , 0, padding, 0);


        return shapeDrawable;
    }

    @NonNull
    private ShapeDrawable getCircleBackground() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.setPadding(0, 0, 0, 0);
        final Paint paint = shapeDrawable.getPaint();
        paint.setColor(DEFAULT_BADGE_COLOR);
        paint.setAntiAlias(true);
        int defaultWidth;
        int defaultHeight;
        if (isSmallSize) {
            defaultWidth = (int) resources.getDimension(R.dimen.uid_notification_badeg_small_circle_radius);
            defaultHeight = (int) resources.getDimension(R.dimen.uid_notification_badeg_small_circle_radius);

        } else {
            defaultWidth = (int) resources.getDimension(R.dimen.uid_notification_badge_default_radius);
            defaultHeight = (int) resources.getDimension(R.dimen.uid_notification_badge_default_radius);
        }
        applyTo(this, defaultWidth, defaultHeight);
        return shapeDrawable;
    }

    private int dipToPixels(int dip) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }

}
