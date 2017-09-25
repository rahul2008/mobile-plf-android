/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.philips.platform.uid.utils.BackgroundOutlineProvider;


/**
 * Implementation for a customized Notification Badge</b>
 * <br>To change background color, assign app:backgroundTint="?attr/uidNotificationBadgeAccentBackgroundColor" for accent or any other custom color.
 * <br>To change text color, assign app:textColor="?attr/uidNotificationBadgeAccentTextColor" for accent or any other custom color.
 * <p>
 * You can use Notification Badge follow the below steps</b><br>
 * <p>
 * 1 . It uses {@link com.philips.platform.uid.view.widget.NotificationBadge} for Notification Badge.
 * <p>
 * 2 . Use Styles as per your requirement as shown below</b>
 * <p>
 * a) Use Default Notification style = "@style/NotificationBadge" to support medium size Badge View
 * <p>
 * b) Use Small Notification style = "@style/NotificationBadge.Small" to support small size Badge View
 * <p/>
 * <p>
 * Template for Default Notification Badge <br>
 * <pre>
 *        &lt;FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *            android:layout_width="wrap_content"
 *            android:layout_height="wrap_content"/&gt;
 *
 *            &lt;View
 *                android:layout_width="wrap_content"
 *                android:layout_height="wrap_content"
 *                android:layout_marginRight="@dimen/uid_notificationbadge_default_margin"
 *                android:layout_marginTop="@dimen/uid_notificationbadge_default_margin"
 *                android:stateListAnimator="@null"
 *                android:gravity="center|center_horizontal"/&gt;
 *
 *            &lt;com.philips.platform.uid.view.widget.NotificationBadge
 *                android:layout_width="wrap_content"
 *                android:layout_height="wrap_content"
 *                style="@style/NotificationBadge"&gt;
 *
 *      &lt;FrameLayout/&gt;
 *   </pre>
 * </p>
 * <p>
 * <p>
 * Template for Small Notification Badge <br>
 * <pre>
 *        &lt;FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *            android:layout_width="wrap_content"
 *            android:layout_height="wrap_content"/&gt;
 *
 *            &lt;View
 *               android:layout_width="wrap_content"
 *               android:layout_height="wrap_content"
 *               android:layout_margin="@dimen/uid_notificationbadge_small_margin"
 *               android:stateListAnimator="@null"
 *               android:gravity="center|center_horizontal""/&gt;
 *
 *            &lt;com.philips.platform.uid.view.widget.NotificationBadge
 *                android:layout_width="wrap_content"
 *                android:layout_height="wrap_content"
 *                style="@style/NotificationBadge.small"&gt;
 *      &lt;FrameLayout/&gt;
 *   </pre>
 * </p>
 */


public class NotificationBadge extends AppCompatTextView {

    public NotificationBadge(Context context) {
        this(context, null);
    }

    public NotificationBadge(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public NotificationBadge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTextChangeListener();
        setOutlineProvider(new BackgroundOutlineProvider());
    }

    private void setTextChangeListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    setVisibility(INVISIBLE);
                } else {
                    setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
