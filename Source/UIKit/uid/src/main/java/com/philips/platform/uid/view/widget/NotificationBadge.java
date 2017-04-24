/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uid.R;

public class NotificationBadge extends AppCompatTextView {

    private static int badgeBackgroundColor;
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
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotificationBadgeStyle, defStyle, R.style.NotificationBadge);
        setTextResourceID(context, this, attrs);
        roundRectDrawable = getSquareRoundBackground(isSmallBadge(typedArray), badgeBackgroundColor);
        circleDrawable = getCircleBackground(isSmallBadge(typedArray), badgeBackgroundColor);
        if (getText().length() > 2)
            setBackgroundDrawable(roundRectDrawable);
        else
            setBackgroundDrawable(circleDrawable);
        handleTextChangeListener();
        typedArray.recycle();
    }

    private void applyLayoutParams(int width, int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null)
            lp = new ViewGroup.LayoutParams(width, height);

        setMinimumHeight(height);
        setMinimumWidth(width);
        setLayoutParams(lp);
    }

    private boolean isSmallBadge(TypedArray typedArray) {
        boolean smallBadge = typedArray.getBoolean(R.styleable.NotificationBadgeStyle_uidNotificationBadgeSmall, false);
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

    private void validateView(CharSequence count) {
        if (count.length() > 2) {
            setBackgroundDrawable(roundRectDrawable);
        } else {
            setBackgroundDrawable(circleDrawable);
        }
        changeBadgeVisisbility(count);
    }

    @NonNull
    private ShapeDrawable getSquareRoundBackground(boolean smallBadge, int badgeBackgroundColor) {
        int radius;
        if (smallBadge)
            radius = dipToPixels(getContext().getResources().getInteger(R.integer.uid_notification_badge_view_small_size_radius));
        else
            radius = dipToPixels(getContext().getResources().getInteger(R.integer.uid_notification_badge_view_default_size_radius));

        float[] outerR = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(badgeBackgroundColor);
        applyLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return shapeDrawable;
    }

    @NonNull
    private ShapeDrawable getCircleBackground(boolean smallBadge, int badgeBackgroundColor) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(badgeBackgroundColor);
        int defaultWidth, defaultHeight;
        if (smallBadge) {
            defaultWidth = (int) getContext().getResources().getDimension(R.dimen.uid_notification_badge_small_circle_radius);
            defaultHeight = (int) getContext().getResources().getDimension(R.dimen.uid_notification_badge_small_circle_radius);
        } else {
            defaultWidth = (int) getContext().getResources().getDimension(R.dimen.uid_notification_badge_default_radius);
            defaultHeight = (int) getContext().getResources().getDimension(R.dimen.uid_notification_badge_default_radius);
        }
        applyLayoutParams(defaultWidth, defaultHeight);
        return shapeDrawable;
    }

    private int dipToPixels(int dip) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
        return (int) px;
    }

    private void changeBadgeVisisbility(CharSequence count) {
        if (TextUtils.isEmpty(count)) {
            setVisibility(INVISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
    }
    private void setTextResourceID(Context context, View view, AttributeSet attrs) {
        if (view instanceof TextView) {
            TypedArray textArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidNotificationBadgeDefaultTextColor,R.attr.uidNotificationBadgeDefaultBackgroundColor});
            int resourceId = textArray.getResourceId(0, -1);
            if (resourceId != 0) {
                setTextColor(ContextCompat.getColor(getContext(), resourceId));
            }
            resourceId = textArray.getResourceId(1, -1);
            if (resourceId != -1) {
                badgeBackgroundColor=ContextCompat.getColor(getContext(), resourceId);
            }

        }
    }
}
