/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.philips.platform.uid.R;
import com.philips.platform.uid.text.utils.UIDClickableSpan;
import com.philips.platform.uid.text.utils.UIDClickableSpanWrapper;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDLocaleHelper;

import java.util.ArrayList;

/**
 * Custom implementation of {@link AppCompatTextView} to support Hyperlinks and other DLS specs.
 * <br> Default values:TextSize: 16sp, lineSpacingExtra: 6dp, lineSpacingMultiplier: 1
 * font: centralesansbook
 * <p>
 * <h1>HyperLinks Guidelines</h1>
 * To apply custom hyperlink colors use {@link #setHyperLinkColors(int)}. <br>
 * 1. HyperLink can be provided in the HTML tags in strings and can be directly used in android:text in layouts.
 * In this case don't use android:autoLink feature, as this removes the embedded urls.<br>
 * 2. Use {@link UIDClickableSpan} to apply custom action not supported in HTML like having in-app actions.
 * <p>
 * <h2>Handling callback</h2>
 * {@link UIDClickableSpan} provides it's own callback mechanism. Refer {@link UIDClickableSpan} for further details.<br>
 * For embedded hyperlinks, use {@link Label#setSpanClickInterceptor(UIDClickableSpanWrapper.ClickInterceptor)} to intercept calls.
 * </p>
 */
public class Label extends AppCompatTextView {

    ColorStateList drawableTintList;

    private UIDClickableSpan[] pressedLinks;
    private ColorStateList linkColors;
    private UIDClickableSpanWrapper.ClickInterceptor externalClickInterceptor;
    private UIDClickableSpanWrapper.ClickInterceptor clickInterceptor = new UIDClickableSpanWrapper.ClickInterceptor() {
        @Override
        public boolean interceptClick(CharSequence tag) {
            return (externalClickInterceptor != null && externalClickInterceptor.interceptClick(tag));
        }
    };

    /**
     * {@inheritDoc}
     */
    public Label(final Context context) {
        this(context, null);
    }

    /**
     * {@inheritDoc}
     */
    public Label(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uidLabelStyle);
    }

    /**
     * {@inheritDoc}
     */
    public Label(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
        setHyperLinkColors(R.color.uid_link_selector);
    }

    private void processAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.UIDLabel, defStyleAttr, R.style.UIDLabel);
        UIDLocaleHelper.setTextFromResourceID(context, this, attrs);
        setDrawableTintColor(context, attrsArray);
        attrsArray.recycle();
    }

    private void setDrawableTintColor(Context context, TypedArray typedArray) {
        if (typedArray.hasValue(R.styleable.UIDLabel_uidDrawableTint)) {
            int resourceId = typedArray.getResourceId(R.styleable.UIDLabel_uidDrawableTint, -1);
            if (resourceId == -1) {
                drawableTintList = ColorStateList.valueOf(typedArray.getColor(R.styleable.UIDLabel_uidDrawableTint, 0));
            } else {
                drawableTintList = ThemeUtils.buildColorStateList(context, resourceId);
            }
            Drawable[] compoundDrawables = getCompoundDrawables();
            for (Drawable drawable : compoundDrawables) {
                if (drawable != null) {
                    drawable.setTintList(drawableTintList);
                }
            }
        }
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(wrapCompoundDrawableTint(left), wrapCompoundDrawableTint(top),
                wrapCompoundDrawableTint(right), wrapCompoundDrawableTint(bottom));
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(wrapCompoundDrawableTint(start), wrapCompoundDrawableTint(top),
                wrapCompoundDrawableTint(end), (bottom));
    }

    private Drawable wrapCompoundDrawableTint(Drawable drawable) {
        if (drawable != null && drawableTintList != null) {
            drawable.setTintList(drawableTintList);
        }
        return drawable;
    }

    /**
     * Set the link colors. Calling this will change all the colors of the hyperlinks present.
     * If different colors are reburied per span, consider using {@link UIDClickableSpan}
     *
     * @param resID Color selector resource id
     */
    public void setHyperLinkColors(@ColorRes int resID) {
        linkColors = ThemeUtils.buildColorStateList(getContext(), resID);
        if (getText() instanceof Spanned) {
            Spanned text = (Spanned) getText();
            UIDClickableSpan[] spans = text.getSpans(0, text.length(), UIDClickableSpan.class);
            for (UIDClickableSpan span : spans) {
                span.setColors(linkColors);
            }
        }
    }

    /**
     * Returns colors applied on links.
     *
     * @return colors applied on links
     */
    public ColorStateList getHyperLinkColors() {
        return linkColors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        ensureHyperLinkColors();
        super.setText(decorateLinks(text), type);
    }

    private void ensureHyperLinkColors() {
        if (linkColors == null) {
            setHyperLinkColors(R.color.uid_link_selector);
        }
    }

    private CharSequence decorateLinks(CharSequence text) {
        CharSequence string = decorateSpannableString(text);
        if (string != null) {
            setMovementMethod(LinkMovementMethod.getInstance());
            return string;
        }

        string = decorateSpannedString(text);

        if (string != null) {
            setMovementMethod(LinkMovementMethod.getInstance());
            return string;
        }

        return text;
    }

    /**
     * We don't update the input string unless it contains valid urls.
     *
     * @param text text of the label
     * @return null if doesn't contain URL/ spans.
     */
    @Nullable
    private CharSequence decorateSpannableString(CharSequence text) {
        Spannable string = null;
        if (text instanceof Spannable) {
            int spanStart;
            int spanEnd;
            ClickableSpan[] clickableSpans = ((Spannable) text).getSpans(0, text.length(), ClickableSpan.class);
            if (clickableSpans.length > 0) {
                string = (Spannable) text;
                ColorStateList linkColors = getHyperLinkColors();
                for (ClickableSpan span : clickableSpans) {
                    if (span instanceof UIDClickableSpan) {
                        ColorStateList spanLinkColors = ((UIDClickableSpan) span).getColors();
                        if (spanLinkColors == null) {
                            ((UIDClickableSpan) span).setColors(linkColors);
                        }
                    } else {
                        spanStart = string.getSpanStart(span);
                        spanEnd = string.getSpanEnd(span);
                        if (spanStart >= 0 && spanEnd >= 0) {
                            string.removeSpan(span);
                            UIDClickableSpanWrapper urlSpanWrapper = new UIDClickableSpanWrapper(span);
                            urlSpanWrapper.setColors(linkColors);
                            urlSpanWrapper.setClickInterceptor(clickInterceptor);
                            string.setSpan(urlSpanWrapper, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        return string;
    }

    /**
     * We don't update the input string unless it contains valid urls.
     *
     * @param text text of the label
     * @return null if doesn't contain URL spans.
     */
    @Nullable
    private CharSequence decorateSpannedString(CharSequence text) {
        SpannableString string = null;
        if (text instanceof Spanned) {
            int spanStart;
            int spanEnd;
            URLSpan[] urlSpans = ((Spanned) text).getSpans(0, text.length(), URLSpan.class);
            if (urlSpans.length > 0) {
                string = SpannableString.valueOf(text);
                for (URLSpan span : urlSpans) {
                    string.removeSpan(span);
                    spanStart = ((Spanned) text).getSpanStart(span);
                    spanEnd = ((Spanned) text).getSpanEnd(span);
                    UIDClickableSpanWrapper urlSpanWrapper = new UIDClickableSpanWrapper(span);
                    urlSpanWrapper.setColors(getHyperLinkColors());
                    urlSpanWrapper.setClickInterceptor(clickInterceptor);
                    string.setSpan(urlSpanWrapper, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return string;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URLSpan[] getUrls() {
        if (getText() instanceof Spanned) {
            Spanned text = (Spanned) getText();
            UIDClickableSpanWrapper[] spans = text.getSpans(0, text.length(), UIDClickableSpanWrapper.class);
            if (spans.length > 0) {
                ArrayList<URLSpan> urlSpanList = new ArrayList<>();
                for (UIDClickableSpanWrapper span : spans) {
                    if (span.getWrappedSpan() instanceof URLSpan) {
                        urlSpanList.add((URLSpan) span.getWrappedSpan());
                    }
                }
                return urlSpanList.toArray(new URLSpan[0]);
            }
        }
        return super.getUrls();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (!isPressed() && hasPressedLinks()) {
            resetPressedLinks();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        updateSpans(event);

        boolean result = super.onTouchEvent(event);

        removeLinkSelectionBackground(event);

        return result;
    }

    //Must be called before super.onTouch to properly set the colors for pressed to normal
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
                pressedLinks[pressedLinks.length - 1].setPressed(true);
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (hasPressedLinks()) {
                resetPressedLinks();
            }
        }
    }

    private void resetPressedLinks() {
        pressedLinks[pressedLinks.length - 1].setPressed(false);
        pressedLinks = null;
        invalidate();
    }

    private boolean hasPressedLinks() {
        return pressedLinks != null && pressedLinks.length != 0;
    }

    //We need to call this after super.onTouch() to clear the press background color for links
    private void removeLinkSelectionBackground(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN && pressedLinks != null && pressedLinks.length != 0) {
            pressedLinks[pressedLinks.length - 1].setPressed(true);
            Selection.removeSelection((Spannable) getText());
        }
    }

    /**
     * Notifies the listener with link pressed. The same listener is used for all the links.
     * Be sure to check for null before unwrapping values.
     *
     * @param externalClickInterceptor callback for URL spans
     */
    public void setSpanClickInterceptor(UIDClickableSpanWrapper.ClickInterceptor externalClickInterceptor) {
        this.externalClickInterceptor = externalClickInterceptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(((SavedState) state).getSuperState());
        if (savedState.spanVisitedArray != null && getText() != null) {
            UIDClickableSpan[] spans = ((Spannable) getText()).getSpans(0, getText().length(), UIDClickableSpan.class);
            int index = 0;
            for (UIDClickableSpan span : spans) {
                span.setVisited(savedState.spanVisitedArray[index++]);
            }
        }
    }

    private static class SavedState extends BaseSavedState {
        int linksCount;
        boolean[] spanVisitedArray;

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private SavedState(Parcelable superState, CharSequence text) {
            super(superState);
            if (text instanceof Spannable) {
                UIDClickableSpan[] spans = ((Spannable) text).getSpans(0, text.length(), UIDClickableSpan.class);
                if (spans.length > 0) {
                    linksCount = spans.length;
                    spanVisitedArray = new boolean[linksCount];
                    for (int i = 0; i < spans.length; i++) {
                        spanVisitedArray[i] = spans[i].isVisited();
                    }
                }
            }
        }

        private SavedState(Parcel in) {
            super(in);
            linksCount = in.readInt();
            if (linksCount > 0) {
                spanVisitedArray = new boolean[linksCount];
                in.readBooleanArray(spanVisitedArray);
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(linksCount);
            if (spanVisitedArray != null) {
                out.writeBooleanArray(spanVisitedArray);
            }
        }
    }
}