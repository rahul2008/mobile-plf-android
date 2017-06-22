/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.utils;

import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

public class UIDClickableSpanWrapper extends UIDClickableSpan {

    public interface ClickInterceptor {
        boolean interceptClick(CharSequence tag);
    }

    private final ClickableSpan wrappedSpan;
    private ClickInterceptor clickInterceptor;

    public UIDClickableSpanWrapper(ClickableSpan clickableSpan) {
        this(clickableSpan, null);
    }

    public UIDClickableSpanWrapper(ClickableSpan clickableSpan, Runnable clickRunnable) {
        super(clickRunnable);
        this.wrappedSpan = clickableSpan;
    }

    public void setClickInterceptor(ClickInterceptor clickInterceptor) {
        this.clickInterceptor = clickInterceptor;
    }

    @Override
    public void onClick(View widget) {
        super.onClick(widget);
        if (wrappedSpan != null &&
                (clickInterceptor == null || !clickInterceptor.interceptClick(getTag()))) {
            wrappedSpan.onClick(widget);
        }
    }

    @Override
    public CharSequence getTag() {
        if (wrappedSpan instanceof URLSpan) {
            return ((URLSpan) wrappedSpan).getURL();
        }
        return super.getTag();
    }

    public ClickableSpan getWrappedSpan() {
        return wrappedSpan;
    }
}