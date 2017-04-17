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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import com.philips.platform.uid.R;

public class NotificationBadge extends AppCompatTextView {

    private final Resources resources;
    private int badgeBackgroundColor;
    private Drawable roundRectDrawable;
    private Drawable circleDrawable;

    public NotificationBadge(Context context) {
        this(context, null);
    }

    public NotificationBadge(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NotificationBadge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        resources = context.getResources();
        badgeBackgroundColor = ContextCompat.getColor(getContext(), R.color.uidColorRed);
        roundRectDrawable = getSquareRoundBackground(isSmallBadge(attrs, getContext()));
        circleDrawable = getCircleBackground(isSmallBadge(attrs, getContext()));

        if (getText().length() > 2)
            setBackgroundDrawable(roundRectDrawable);
        else
            setBackgroundDrawable(circleDrawable);

        setGravity(Gravity.CENTER);
        handleTextChangeListener();
        setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
    }

    private void applyLayoutParams(int width, int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null)
            lp = new ViewGroup.LayoutParams(width, height);

        setMinimumHeight(height);
        setMinimumWidth(width);
        setLayoutParams(lp);
    }


    private boolean isSmallBadge(AttributeSet attrs, Context context) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.notification_badge, 0, 0);
        boolean smallBadge = a.getBoolean(R.styleable.notification_badge_uid_notification_badge_small, false);
        a.recycle();
        return smallBadge;
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

    private void validateView(CharSequence s) {
        if (s.length() > 2) {
            setBackgroundDrawable(roundRectDrawable);
        } else {
            setBackgroundDrawable(circleDrawable);
        }
    }

    @NonNull
    private ShapeDrawable getSquareRoundBackground(boolean smallBadge) {
        int radius;
        if (smallBadge)
            radius = dipToPixels(resources.getInteger(R.integer.uid_notification_badge_view_small_size_radius));
        else
            radius = dipToPixels(resources.getInteger(R.integer.uid_notification_badge_view_default_size_radius));

        float[] outerR = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);

        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(badgeBackgroundColor);
        applyLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int padding = (int) resources.getDimension(R.dimen.uid_notification_badge_square_round_padding);
        setPadding(padding, 0, padding, 0);

        return shapeDrawable;
    }

    @NonNull
    private ShapeDrawable getCircleBackground(boolean smallBadge) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.setPadding(0, 0, 0, 0);
        final Paint paint = shapeDrawable.getPaint();
        paint.setColor(badgeBackgroundColor);
        paint.setAntiAlias(true);
        int defaultWidth, defaultHeight;
        if (smallBadge) {
            defaultWidth = (int) resources.getDimension(R.dimen.uid_notification_badge_small_circle_radius);
            defaultHeight = (int) resources.getDimension(R.dimen.uid_notification_badge_small_circle_radius);

        } else {
            defaultWidth = (int) resources.getDimension(R.dimen.uid_notification_badge_default_radius);
            defaultHeight = (int) resources.getDimension(R.dimen.uid_notification_badge_default_radius);
        }
        applyLayoutParams(defaultWidth, defaultHeight);
        return shapeDrawable;
    }

    private int dipToPixels(int dip) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }

    /**
     * Sets the number to Notification Badge.
     *
     * @param badgeNumber
     */
    public void setErrorMessage(CharSequence badgeNumber) {

        if (badgeNumber != null) {
            if (badgeNumber.equals("0")||badgeNumber.equals("")) {
                setVisibility(INVISIBLE);
            } else {
                if (badgeNumber.length() >= 4) {
                    setVisibility(VISIBLE);
                    setText("9999+");
                } else {
                    setVisibility(VISIBLE);
                    setText(badgeNumber);
                }
            }

        } else {
            setVisibility(INVISIBLE);
        }

    }
}
