/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.utils.UIDUtils;

public class ProgressBarButton extends LinearLayout {

    private Button button;
    private ProgressBar progressBar;
    private TextView progressTextView;

    private boolean isProgressDisplaying;
    private OnClickListener clickListener;
    private GestureDetectorCompat gestureDetector;
    private Drawable progressBackgroundDrawable;

    public ProgressBarButton(@NonNull final Context context) {
        this(context, null);
    }

    public ProgressBarButton(@NonNull final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarButton(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        gestureDetector = new GestureDetectorCompat(context, new TapDetector());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressIndicatorButton, defStyleAttr, 0);
        boolean isIndeterminateProgressIndicator = typedArray.getBoolean(R.styleable.UIDProgressIndicatorButton_uidIsIndeterminateProgressIndicator, false);
        inflateLayout(isIndeterminateProgressIndicator);

        initializeViews();

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Context themedContext = UIDContextWrapper.getThemedContext(context, theme);

        initializeElements(themedContext, typedArray);
        typedArray.recycle();

    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        clickListener = listener;
        super.setOnClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onInterceptTouchEvent(@NonNull final MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return isProgressDisplaying || super.onInterceptTouchEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        button.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button.getMeasuredWidth(), button.getMeasuredHeight());
        setMinimumHeight(getMeasuredHeight());
        setMinimumWidth(getMeasuredWidth());
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);

        savedState.buttonText = getText();
        savedState.progressText = getProgressText();
        savedState.progress = getProgress();
        savedState.buttonVisibility = button.getVisibility();

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setText(savedState.buttonText);
        setProgressText(savedState.progressText);

        progressBar.post(new Runnable() {
            @Override
            public void run() {
                setProgress(savedState.progress);
            }
        });

        if (savedState.buttonVisibility == View.GONE) {
            showProgressIndicator();
        }
    }

    private void initializeElements(final Context themedContext, final TypedArray typedArray) {
        int buttonDrawableId = typedArray.getResourceId(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonDrawable, -1);
        setDrawable(buttonDrawableId);

        int progressButtonBackgroundDrawableId = typedArray.getResourceId(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgressBackground, R.drawable.uid_progress_indicator_button_background);
        progressBackgroundDrawable = UIDUtils.setTintOnDrawable(ContextCompat.getDrawable(themedContext, progressButtonBackgroundDrawableId), R.color.uid_progress_indicator_button_background_selector, themedContext);

        int progress = typedArray.getInt(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgress, 0);
        setProgress(progress);

        CharSequence buttonText = typedArray.getText(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonText);
        setText(buttonText);

        CharSequence progressText = typedArray.getText(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgressText);
        setProgressText(progressText);
    }

    private void inflateLayout(final boolean isIndeterminateProgressIndicator) {
        final int layoutId = isIndeterminateProgressIndicator ? R.layout.uid_progress_indicator_button_indeterminate : R.layout.uid_progress_indicator_button_determinate;

        addView(View.inflate(getContext(), layoutId, null));
    }

    private void initializeViews() {
        button = (Button) findViewById(R.id.uid_progress_indicator_button_button);
        progressBar = (ProgressBar) findViewById(R.id.uid_progress_indicator_button_progress_bar);
        progressTextView = (TextView) findViewById(R.id.uid_progress_indicator_button_text);
    }

    private void setVisibilityOfProgressButtonElements(boolean visible) {
        isProgressDisplaying = visible;
        if (visible) {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            setBackground(progressBackgroundDrawable);

            setProgressBarAndProgressTextVisibility();
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            setBackground(null);
        }
    }

    private void setProgressBarAndProgressTextVisibility() {
        final boolean hasProgressText = !TextUtils.isEmpty(progressTextView.getText());
        progressTextView.setVisibility(hasProgressText ? View.VISIBLE : GONE);

        MarginLayoutParams params = (MarginLayoutParams) progressBar.getLayoutParams();
        params.rightMargin = (!hasProgressText) ? params.leftMargin : 0;
        progressBar.setLayoutParams(params);
    }

    /**
     * Show progress indicator on the button
     */
    public void showProgressIndicator() {
        setVisibilityOfProgressButtonElements(true);
    }

    /**
     * Hide progress indicator on the button
     */
    public void hideProgressIndicator() {
        setVisibilityOfProgressButtonElements(false);
    }

    /**
     * Get the progress of the indicator on the button
     *
     * @return value between 0 and 100
     */
    public int getProgress() {
        return progressBar.getProgress();
    }

    /**
     * Set the progress of the indicator on the button
     *
     * @param progress value between 0 - 100
     */
    public void setProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            progressBar.setProgress(progress);
        }
    }

    /**
     * Set the progress text of the button
     *
     * @param resId on the text that will be shown on the button
     */
    @SuppressWarnings("unused")
    public void setProgressText(@StringRes int resId) {
        setProgressText(getContext().getString(resId));
    }

    /**
     * Get the progress text of the button
     *
     * @return progress text of the button
     */
    public String getProgressText() {
        return progressTextView.getText().toString();
    }

    /**
     * Set the progress text of the button
     *
     * @param text that will shown on the button
     */
    public void setProgressText(CharSequence text) {
        progressTextView.setText(text);
        setVisibilityOfProgressButtonElements(isProgressDisplaying);
    }

    /**
     * Set the text of the button
     *
     * @param resId on the text that will be shown on the button
     */
    public void setText(@StringRes int resId) {
        setText(getContext().getString(resId));
    }

    /**
     * Set the button enabled state
     *
     * @param enabled true if it should be enabled, false otherwise
     */
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    /**
     * Get the text of the button
     *
     * @return text of the button
     */
    public String getText() {
        return button.getText().toString();
    }

    /**
     * Set the text of the button
     *
     * @param text that will be shown on the button
     */
    public void setText(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            button.setText(text);
        }
    }

    /**
     * Set the drawable on the button
     *
     * @param drawableId provided drawableId should be a non vector drawable resource id
     */
    public void setDrawable(@DrawableRes int drawableId) {
        if (drawableId != -1) {
            button.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
        }
    }

    /**
     * Set the drawable on the button
     *
     * @param drawable provided drawable will be used to set on the button
     */
    public void setDrawable(Drawable drawable) {
        button.setImageDrawable(drawable);
    }

    /**
     * Get the Button instance
     *
     * @return Button instance
     */
    public Button getButton() {
        return button;
    }

    /**
     * Get the ProgressBar instance
     *
     * @return ProgressBar instance
     */
    @SuppressWarnings("unused")
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * Get the progress TextView
     *
     * @return progress TextView
     */
    @SuppressWarnings("unused")
    public TextView getProgressTextView() {
        return progressTextView;
    }

    static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<ProgressBarButton.SavedState> CREATOR
                = new Parcelable.Creator<ProgressBarButton.SavedState>() {
            public ProgressBarButton.SavedState createFromParcel(Parcel in) {
                return new ProgressBarButton.SavedState(in);
            }

            public ProgressBarButton.SavedState[] newArray(int size) {
                return new ProgressBarButton.SavedState[size];
            }
        };
        String buttonText;
        String progressText;
        int progress;
        int buttonVisibility;

        SavedState(final Parcelable superState) {
            super(superState);
        }

        SavedState(final Parcel in) {
            super(in);
            buttonText = in.readString();
            progressText = in.readString();
            progress = in.readInt();
            buttonVisibility = in.readInt();
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);

            out.writeString(buttonText);
            out.writeString(progressText);
            out.writeInt(progress);
            out.writeInt(buttonVisibility);
        }
    }

    private class TapDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(final MotionEvent e) {
            if (clickListener != null && button.isEnabled()) {
                clickListener.onClick(ProgressBarButton.this);
            }
            return super.onSingleTapConfirmed(e);
        }
    }
}

