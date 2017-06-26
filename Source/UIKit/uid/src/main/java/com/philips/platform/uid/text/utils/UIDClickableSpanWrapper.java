/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.text.utils;

import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;

/**
 * Class which wraps another {@link ClickableSpan} to perform actions on clickable span and provide DLS specific color mappings.
 * {@link com.philips.platform.uid.view.widget.Label} internally uses this class to wrap all the {@link URLSpan} for coloring.
 */
public class UIDClickableSpanWrapper extends UIDClickableSpan {

    /**
     * Callback for receiving the click event for the clickable span.
     * It's additionally provides the tag(if set). For embedded url in {@link com.philips.platform.uid.view.widget.Label}
     * it's called with the url. Tag can be null if not set.
     */
    public interface ClickInterceptor {
        /**
         * @param tag tag attached with wrapper.URL in case of {@link URLSpan}
         * @return {@code Boolean.TRUE} to indicate the call has been handled. In this case, the wrapped click action will not be called.
         * If {@code Boolean.FALSE} is returned, wrapped span's click is clicked.
         */
        boolean interceptClick(CharSequence tag);
    }

    private final ClickableSpan wrappedSpan;
    private ClickInterceptor clickInterceptor;

    /**
     * Initializes the wrapper with another {@link ClickableSpan} with no callback.
     *
     * @param clickableSpan clickablespan
     */
    public UIDClickableSpanWrapper(ClickableSpan clickableSpan) {
        this(clickableSpan, null);
    }

    /**
     * Initializes the wrapper with another {@link ClickableSpan} with custom Runnable.
     *
     * @param clickableSpan clickablespan
     */
    public UIDClickableSpanWrapper(ClickableSpan clickableSpan, Runnable clickRunnable) {
        super(clickRunnable);
        this.wrappedSpan = clickableSpan;
    }

    public void setClickInterceptor(ClickInterceptor clickInterceptor) {
        this.clickInterceptor = clickInterceptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View widget) {
        super.onClick(widget);
        if (wrappedSpan != null &&
                (clickInterceptor == null || !clickInterceptor.interceptClick(getTag()))) {
            wrappedSpan.onClick(widget);
        }
    }

    /**
     * Returns the tag associated with span. Return's url if the wrapped span is a {@link URLSpan}.
     *
     * @return tag, which can be null
     */
    @Override
    public CharSequence getTag() {
        if (wrappedSpan instanceof URLSpan) {
            return ((URLSpan) wrappedSpan).getURL();
        }
        return super.getTag();
    }

    /**
     * Returns wrapped span.
     *
     * @return wrapped span
     */
    public ClickableSpan getWrappedSpan() {
        return wrappedSpan;
    }
}