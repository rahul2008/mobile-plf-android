/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.philips.platform.uid.R;


/**
 * Implementation for a customized Notification Badge</b>
 * <p>
 * You can use Notification Badge follow the below steps</b><br>
 * <p>
 * 1 . It uses {@link com.philips.platform.uid.view.widget.NotificationBadge} for Notification Badge.
 * <p>
 * 2 . Use Styles as per your requirement as shown below</b>
 * <p>
 * a) Use Default Notification style = "@style/NotificationBadge" to support medium size Badge View
 * b) Use Small Notification style = "@style/NotificationBadge.Small" to support small size Badge View
 * <p/>
 * <p>
 *  Template for Default Notification Badge <br>
 *    <pre>
 *        &lt;RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"/&gt;
 *         &lt;RelativeLayout
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"/&gt;
 *         &lt;com.philips.platform.uid.view.widget.Button
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         android:layout_marginRight="@dimen/uid_notification_badge_default_view_margin_top_right"
 *         android:layout_marginTop="@dimen/uid_notification_badge_default_view_margin_top_right"
 *         android:gravity="center|center_horizontal"
 *         android:text="@string/e_mails"/&gt;
 *         &lt;RelativeLayout/&gt;
 *         &lt;com.philips.platform.uid.view.widget.NotificationBadge
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         style="@style/NotificationBadge"
 *         android:gravity="center|center_horizontal"
 *         android:layout_alignRight="@id/relative_layout"
 *         android:visibility="invisible"/&gt;
 *       &lt;RelativeLayout/&gt;
 *     </pre>
 * </p>
 *
 * <p>
 *  Template for Small Notification Badge <br>
 *    <pre>
 *        &lt;RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"/&gt;
 *         &lt;RelativeLayout
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"/&gt;
 *         &lt;com.philips.platform.uid.view.widget.Button
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         android:layout_marginRight="@dimen/uid_notification_badge_default_view_margin_top_right"
 *         android:layout_marginTop="@dimen/uid_notification_badge_default_view_margin_top_right"
 *         android:gravity="center|center_horizontal"
 *         android:text="@string/e_mails"/&gt;
 *         &lt;RelativeLayout/&gt;
 *         &lt;com.philips.platform.uid.view.widget.NotificationBadge.Small
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         style="@style/NotificationBadge"
 *         android:gravity="center|center_horizontal"
 *         android:layout_alignRight="@id/relative_layout"
 *         android:visibility="invisible"/&gt;
 *       &lt;RelativeLayout/&gt;
 *     </pre>
 * </p>
 */


public class NotificationBadge extends AppCompatTextView {

    private static int badgeBackgroundColor;

    public NotificationBadge(Context context) {
        this(context, null);
    }

    public NotificationBadge(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NotificationBadge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotificationBadgeStyle, defStyle, R.style.NotificationBadge);
        setBackgroundColor(context, attrs);
        isSmallBadge(typedArray);
        setTextChangeListener();
        typedArray.recycle();
    }

    private void setBackgroundColor(Context context, AttributeSet attrs) {
        TypedArray textArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidNotificationBadgeDefaultBackgroundColor});
        int resourceId = textArray.getResourceId(0, -1);
        if (resourceId != -1) {
            badgeBackgroundColor = ContextCompat.getColor(getContext(), resourceId);
        }
    }

    private boolean isSmallBadge(TypedArray typedArray) {
        boolean smallBadge = typedArray.getBoolean(R.styleable.NotificationBadgeStyle_uidNotificationBadgeSmall, false);
        return smallBadge;
    }

    private void setTextChangeListener() {
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
        if (TextUtils.isEmpty(count)) {
            setVisibility(INVISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
    }

}
