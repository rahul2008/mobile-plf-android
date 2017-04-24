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


/**
 * Notification Badge  is a Custom view component which supports to show Badge Count as displayed below</b>

 * You can use Notification Badge follow the below steps</b><br>
 *
 * 1 . It uses {@link com.philips.platform.uid.view.widget.NotificationBadge} for Notification Badge.
 *
 * 2 . Use Styles as per your requirement as shown below</b>
 *
 *            a).Use Default Notification style = "@style/NotificationBadge" to support medium size Badge View
 *            b).Use Small Notification style = "@style/NotificationBadge.Small" to support small size Badge View
 * <p/>
 * <br>
 * <p>The Following attributes Define in Default and Small Notification Badge.</p>
 * <table border="2" width="80%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>Default Notification </th> <th>Small Notification</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">Default Radius 24dp</td>
 * <td rowspan="1">Small Radius 20dp</td>
 * </tr>
 * <tr>
 * <td rowspan="1">Text Size for default 12sp</td>
 * <td rowspan="1">Text size for small 10sp</td>
 * </tr>
 * <tr>
 * <tr>
 * <td rowspan="1">Margin for button or View from top and right should be 12 dp </td>
 * <td rowspan="1">Margin for button or View from top and right should be 10 dp </td>
 * </tr>
 * <tr>
 * <td rowspan="1">for both left and right padding 4dp</td>
 * </tr>
 * </tbody>
 * <p>
 * </table>
 * <p/>
 * <p>
 Sample use can be as follows <br>
 * <pre>
 *                          &lt;com.philips.platform.uid.view.widget.NotificationBadg
 *                                  android:layout_width="wrap_content"
 *                                   android:layout_height="wrap_content"
 *                                   style="@style/NotificationBadge"  /&gt;
 *         </pre>
 * </p>
 */

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
        badgeBackgroundResourceID(context, this, attrs);
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

    private void badgeBackgroundResourceID(Context context, View view, AttributeSet attrs) {
        if (view instanceof TextView) {
            TypedArray textArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidNotificationBadgeDefaultBackgroundColor});
            int resourceId = textArray.getResourceId(0, -1);
            if (resourceId != -1) {
                badgeBackgroundColor=ContextCompat.getColor(getContext(), resourceId);
            }

        }
    }
}
