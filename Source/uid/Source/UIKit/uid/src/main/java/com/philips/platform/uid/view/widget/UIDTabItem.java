/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.*;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * UID TabItem
 * <p>
 * <P> UID TabItem is custom TabItem as per DLS design.
 * <p>
 * <P> Use TabItem in your xml layout file as well as in code similar to TabItem.
 * <p>
 *
 */

public class UIDTabItem extends FrameLayout {
    AppCompatImageView tabIconView;
    Label tabLabel;
    NotificationBadge tabBadge;

    public UIDTabItem(@NonNull Context context) {
        this(context, false);
    }

    public UIDTabItem(Context context, boolean isIconWithTitleLayout) {
        super(context, null, R.attr.uidBottomTabItemStyle);
        addView(View.inflate(context, isIconWithTitleLayout ? R.layout.uid_tabbar_with_title_layout : R.layout.uid_tabbar_icon_only_layout, null));
        processAttributes(context, null, R.attr.uidBottomTabItemStyle, false);
    }

    public UIDTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidBottomTabItemStyle);
    }

    public UIDTabItem(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr, true);
    }

    public UIDTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void processAttributes(@NonNull Context context, AttributeSet attrs, int defStyleAttr, boolean isFromXml) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UIDTabItem, defStyleAttr, R.style.UIDTabItemStyle);
        if (isFromXml) {
            addView(View.inflate(context, a.hasValue(R.styleable.UIDTabItem_android_text) ? R.layout.uid_tabbar_with_title_layout : R.layout.uid_tabbar_icon_only_layout, null));
        }
        processIconAttributes(context, a);
        processTextAttributes(context, a);
        processBadgeAttributes(a);
        a.recycle();
        invalidate();
    }

    private void processIconAttributes(Context context, TypedArray array) {
        tabIconView = findViewById(R.id.uid_tab_icon);
        if (tabIconView == null) {
            return;
        }
        if (array.hasValue(R.styleable.UIDTabItem_android_src)) {
            setIcon(array.getDrawable(R.styleable.UIDTabItem_android_src));
        }
        if (array.hasValue(R.styleable.UIDTabItem_android_tint)) {
            int resourceId = array.getResourceId(R.styleable.UIDTabItem_android_tint, 0);
            setIconColor(ThemeUtils.buildColorStateList(context, resourceId));
        }

        if (array.hasValue(R.styleable.UIDTabItem_android_tintMode)) {
            PorterDuff.Mode mode = UIDUtils.parseTintMode(array.getIndex(R.styleable.UIDTabItem_android_tintMode), PorterDuff.Mode.SRC_IN);
            setIconColorTintMode(mode);
        }
    }

    private void processTextAttributes(Context context, TypedArray array) {
        tabLabel = findViewById(R.id.uid_tab_label);
        if (tabLabel == null) {
            return;
        }
        if (array.hasValue(R.styleable.UIDTabItem_android_text)) {
            setTitle(array.getText(R.styleable.UIDTabItem_android_text));
        }
        if (array.hasValue(R.styleable.UIDTabItem_android_textColor)) {
            setTitleColor(ThemeUtils.buildColorStateList(context, array.getResourceId(R.styleable.UIDTabItem_android_textColor, R.color.uid_tab_text_selector)));
        }
    }

    private void processBadgeAttributes(TypedArray array) {
        tabBadge = findViewById(R.id.uid_tab_notification_badge);
        if (tabBadge == null) {
            return;
        }
        if (array.hasValue(R.styleable.UIDTabItem_uidTabItemNotificationBadgeCount)) {
            setBadgeCount(array.getText(R.styleable.UIDTabItem_uidTabItemNotificationBadgeCount));
        }
    }

    public void setIcon(@DrawableRes int srcId) {
        tabIconView = findViewById(R.id.uid_tab_icon);
        if (tabIconView == null) {
            return;
        }
        tabIconView.setImageDrawable(getContext().getDrawable(srcId));
        tabIconView.setVisibility(View.VISIBLE);
    }

    public void setIcon(Drawable drawable) {
        tabIconView = findViewById(R.id.uid_tab_icon);
        if (tabIconView == null) {
            return;
        }
        tabIconView.setImageDrawable(drawable);
        tabIconView.setVisibility(View.VISIBLE);
    }

    public void setIconColor(ColorStateList colorList) {
        tabIconView = findViewById(R.id.uid_tab_icon);
        if (tabIconView == null) {
            return;
        }
        tabIconView.setImageTintList(colorList);
    }

    public void setIconColorTintMode(PorterDuff.Mode mode) {
        tabIconView = findViewById(R.id.uid_tab_icon);
        if (tabIconView == null) {
            return;
        }
        tabIconView.setImageTintMode(mode);
    }

    public void setTitle(CharSequence text) {
        tabLabel = findViewById(R.id.uid_tab_label);
        if (tabLabel == null) {
            return;
        }
        tabLabel.setText(text);
        tabLabel.setVisibility(View.VISIBLE);
    }

    public void setTitle(@StringRes int resId) {
        tabLabel = findViewById(R.id.uid_tab_label);
        if (tabLabel == null) {
            return;
        }
        tabLabel.setText(getContext().getString(resId));
        tabLabel.setVisibility(View.VISIBLE);
    }

    public void setTitleColor(ColorStateList colorList) {
        tabLabel = findViewById(R.id.uid_tab_label);
        if (tabLabel == null) {
            return;
        }
        tabLabel.setTextColor(colorList);
    }

    @SuppressWarnings("unused")
    public void setTitleColor(@ColorInt int color) {
        tabLabel = findViewById(R.id.uid_tab_label);
        if (tabLabel == null) {
            return;
        }
        tabLabel.setTextColor(color);
    }

    public void setBadgeCount(CharSequence count) {
        tabBadge = findViewById(R.id.uid_tab_notification_badge);
        if (tabBadge == null) {
            return;
        }
        tabBadge.setText(count);
        tabBadge.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("unused")
    public void setBadgeCount(@StringRes int resId) {
        tabBadge = findViewById(R.id.uid_tab_notification_badge);
        if (tabBadge == null) {
            return;
        }
        tabBadge.setText(getContext().getString(resId));
        tabBadge.setVisibility(View.VISIBLE);
    }
}