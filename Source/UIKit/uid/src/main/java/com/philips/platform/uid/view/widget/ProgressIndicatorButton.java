package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class ProgressIndicatorButton extends LinearLayout {

    private Button button;
    private ProgressBar progressBar;
    private TextView progressTextView;

    private boolean isProgressDisplaying;
    private OnClickListener clickListener;
    private GestureDetectorCompat gestureDetector;
    private Drawable progressBackgroundDrawable;
    private View childLayout;

    public ProgressIndicatorButton(final Context context) {
        this(context, null);
    }

    public ProgressIndicatorButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicatorButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        gestureDetector = new GestureDetectorCompat(context, new TapDetector());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressIndicatorButton, defStyleAttr, 0);
        boolean isIndeterminateProgressIndicator = typedArray.getBoolean(R.styleable.UIDProgressIndicatorButton_uidIsIndeterminateProgressIndicator, false);
        inflateLayout(isIndeterminateProgressIndicator);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        initializeElements(context, typedArray, theme);

        typedArray.recycle();

        setClickable(true);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        clickListener = l;
        super.setOnClickListener(l);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return isProgressDisplaying || super.onInterceptTouchEvent(event);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        button.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button.getMeasuredWidth(), button.getMeasuredHeight());
        childLayout.setMinimumHeight(getMeasuredHeight());
        childLayout.setMinimumWidth(getMeasuredWidth());
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

    private void initializeElements(final Context context, final TypedArray typedArray, Resources.Theme theme) {
        int buttonDrawableId = typedArray.getResourceId(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonDrawable, -1);
        setDrawable(buttonDrawableId);

        int progressButtonBackgroundDrawableId = typedArray.getResourceId(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgressBackground, R.drawable.uid_progress_indicator_button_background);
        progressBackgroundDrawable = setTintOnDrawable(ContextCompat.getDrawable(context, progressButtonBackgroundDrawableId), R.color.uid_progress_indicator_button_background_selector, theme);

        int progress = typedArray.getInt(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgress, 0);
        setProgress(progress);

        String buttonText = typedArray.getString(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonText);
        setText(buttonText);

        String progressText = typedArray.getString(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgressText);
        setProgressText(progressText);
    }

    private void inflateLayout(final boolean isIndeterminateProgressIndicator) {
        if (isIndeterminateProgressIndicator) {
            childLayout = View.inflate(getContext(), R.layout.uid_progress_indicator_button_indeterminate, null);
        } else {
            childLayout = View.inflate(getContext(), R.layout.uid_progress_indicator_button_determinate, null);
        }

        button = (Button) childLayout.findViewById(R.id.uid_progress_indicator_button_button);
        progressBar = (ProgressBar) childLayout.findViewById(R.id.uid_progress_indicator_button_progress_bar);
        progressTextView = (TextView) childLayout.findViewById(R.id.uid_progress_indicator_button_text);
        addView(childLayout);
    }

    private Drawable setTintOnDrawable(Drawable drawable, int tintId, Resources.Theme theme) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(getResources(), theme, tintId);
        Drawable compatDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(compatDrawable, colorStateList);
        return compatDrawable;
    }

    private void setVisibilityOfProgressButtonElements(boolean visible) {
        isProgressDisplaying = visible;
        if (visible) {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            setBackground(progressBackgroundDrawable);

            if (!TextUtils.isEmpty(progressTextView.getText())) {
                progressTextView.setVisibility(View.VISIBLE);
            } else {
                progressTextView.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) progressBar.getLayoutParams();
                params.rightMargin = params.leftMargin;
                progressBar.setLayoutParams(params);
            }
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            setBackground(null);
        }
    }

    private class TapDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(final MotionEvent e) {
            if (clickListener != null && button.isEnabled()) {
                clickListener.onClick(ProgressIndicatorButton.this);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    public void showProgressIndicator() {
        setVisibilityOfProgressButtonElements(true);
    }

    public void hideProgressIndicator() {
        setVisibilityOfProgressButtonElements(false);
    }

    public void setProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            progressBar.setProgress(progress);
        }
    }

    public int getProgress() {
        return progressBar.getProgress();
    }

    public void setProgressText(String text) {
        if (!TextUtils.isEmpty(text)) {
            progressTextView.setText(text);
        }
    }

    public String getProgressText() {
        return progressTextView.getText().toString();
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            button.setText(text);
        }
    }

    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    public String getText() {
        return button.getText().toString();
    }

    public void setDrawable(int drawableId) {
        if (drawableId != -1) {
            button.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
        }
    }

    public void setDrawable(Drawable drawable) {
        button.setImageDrawable(drawable);
    }

    public Button getButton() {
        return button;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getProgressTextView() {
        return progressTextView;
    }

    public static class SavedState extends BaseSavedState {
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

        public static final Parcelable.Creator<ProgressIndicatorButton.SavedState> CREATOR
                = new Parcelable.Creator<ProgressIndicatorButton.SavedState>() {
            public ProgressIndicatorButton.SavedState createFromParcel(Parcel in) {
                return new ProgressIndicatorButton.SavedState(in);
            }

            public ProgressIndicatorButton.SavedState[] newArray(int size) {
                return new ProgressIndicatorButton.SavedState[size];
            }
        };
    }
}

