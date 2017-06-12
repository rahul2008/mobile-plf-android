/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDClickableSpan;
import com.philips.platform.uid.utils.UIDClickableSpanWrapper;
import com.philips.platform.uid.utils.UIDLocaleHelper;

public class Label extends AppCompatTextView {
    private UIDClickableSpan[] pressedLinks;
    private UIDClickableSpanWrapper.ClickInterceptor externalClickInterceptor;
    private UIDClickableSpanWrapper.ClickInterceptor clickInterceptor = new UIDClickableSpanWrapper.ClickInterceptor() {
        @Override
        public boolean interceptClick(CharSequence tag) {
            return (externalClickInterceptor != null && externalClickInterceptor.interceptClick(tag));
        }
    };

    public Label(final Context context) {
        this(context, null);
    }

    public Label(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidLabelStyle);
    }

    public Label(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
    }

    private void processAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.UIDLabel, defStyleAttr, R.style.UIDLabel);
        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
        attrsArray.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(decorateSpans(text), type);
    }

    private CharSequence decorateSpans(CharSequence text) {

        if (text instanceof Spannable) {
            int spanStart;
            int spanEnd;
            Spannable string = (Spannable) text;
            ClickableSpan[] clickableSpans = string.getSpans(0, text.length(), ClickableSpan.class);
            for (ClickableSpan span : clickableSpans) {
                spanStart = string.getSpanStart(span);
                spanEnd = string.getSpanEnd(span);
                ColorStateList linkColors = null;
                if(span instanceof UIDClickableSpan) {
                    linkColors = ((UIDClickableSpan) span).getColors();
                }
                linkColors = linkColors != null? linkColors: getLinkTextColors();
                if (spanStart >= 0 && spanEnd >= 0) {
                    string.removeSpan(span);
                    UIDClickableSpanWrapper urlSpanWrapper = new UIDClickableSpanWrapper(span);
                    urlSpanWrapper.setColors(linkColors);
                    urlSpanWrapper.setClickInterceptor(clickInterceptor);
                    string.setSpan(urlSpanWrapper, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            return string;
        }

        if (text instanceof Spanned) {
            int spanStart;
            int spanEnd;
            SpannableString string = SpannableString.valueOf(text);
            URLSpan[] urlSpans = string.getSpans(0, text.length(), URLSpan.class);
            for (URLSpan span : urlSpans) {
                string.removeSpan(span);
                spanStart = ((Spanned) text).getSpanStart(span);
                spanEnd = ((Spanned) text).getSpanEnd(span);
                UIDClickableSpanWrapper urlSpanWrapper = new UIDClickableSpanWrapper(span);
                urlSpanWrapper.setColors(getLinkTextColors());
                urlSpanWrapper.setClickInterceptor(clickInterceptor);
                string.setSpan(urlSpanWrapper, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return string;
        }
        return text;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        updateSpans(event);
        return super.onTouchEvent(event);
    }

    private void updateSpans(MotionEvent event) {
        if (!(getText() instanceof Spannable))
            return;
        int action = event.getAction();
        Spannable sequence = (Spannable) getText();
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();

            x += getScrollX();
            y += getScrollY();

            Layout layout = getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            pressedLinks = sequence.getSpans(off, off, UIDClickableSpan.class);

            if (pressedLinks.length != 0) {
                if (action == MotionEvent.ACTION_DOWN) {
                    pressedLinks[0].setPressed(true);
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (pressedLinks != null && pressedLinks.length != 0) {
                pressedLinks[0].setPressed(false);
                pressedLinks = null;
            }
        }
    }

    public void setSpanClickInterceptor(UIDClickableSpanWrapper.ClickInterceptor externalClickInterceptor) {
        this.externalClickInterceptor = externalClickInterceptor;
    }
}