/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.philips.platform.uid.R;

public class ProgressBarWithLabel extends FrameLayout {

    private enum LabelPosition {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, BOTTOM_CENTER}

    private boolean isLinearProgressBarEnabled = false;
    private boolean isIndeterminateProgressIndicator = false;
    private LabelPosition labelPosition = LabelPosition.TOP_LEFT;
    private ProgressBar progressBar;
    private int textSize;
    private int textColor;
    private String text;
    private int progress;
    private int secondaryProgress;
    private ProgressBar.CircularProgressBarSize circularProgressBarSize;
    private Label label;

    public ProgressBarWithLabel(final Context context) {
        this(context, null);
    }

    public ProgressBarWithLabel(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarWithLabel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyleAttributes(context, attrs);

        int id;
        if (isLinearProgressBarEnabled) {
            id = isIndeterminateProgressIndicator ? R.layout.uid_indeterminate_linear_progress_indicator_with_label : R.layout.uid_linear_progress_indicator_with_label;
            addView(View.inflate(getContext(), id, null));
            initializeLinearProgressBarViews();
        } else {
            id = getCircularProgressBarLayout(circularProgressBarSize);
            addView(View.inflate(getContext(), id, null));
            initializeCircularProgressBarViews();
        }

        setAttributes();
    }

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressIndicatorWithLabel);
        isLinearProgressBarEnabled = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressIndicatorWithLabel_uidIsLinearProgressBar, false);
        isIndeterminateProgressIndicator = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressIndicatorWithLabel_uidIsIndeterminateProgressIndicator, false);
        labelPosition = LabelPosition.values()[obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_uidLabelPosition, 0)];
        text = obtainStyledAttributes.getString(R.styleable.UIDProgressIndicatorWithLabel_android_text);

        textSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.UIDProgressIndicatorWithLabel_android_textSize, -1);
        textColor = obtainStyledAttributes.getColor(R.styleable.UIDProgressIndicatorWithLabel_android_textColor, -1);
        progress = obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_android_progress, 0);
        secondaryProgress = obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_android_secondaryProgress, 0);
        circularProgressBarSize = ProgressBar.CircularProgressBarSize.values()[obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_uidCircularProgressBarSize, 0)];

        obtainStyledAttributes.recycle();
    }

    private void setAttributes() {
        if (textSize != -1) {
            setTextSize(textSize);
        }
        if (textColor != -1) {
            setTextColor(textColor);
        }

        setText(text);
        setProgress(progress);
        setSecondaryProgress(secondaryProgress);
        setLabelVisibility();
    }

    private void initializeLinearProgressBarViews() {
        label = (Label) getRootView().findViewById(getLinearProgressBarLabelID());

        if (!isIndeterminateProgressIndicator) {
            progressBar = (ProgressBar) findViewById(R.id.uid_determinate_linear_progress_indicator);
        }
    }

    private int getLinearProgressBarLabelID() {
        switch (labelPosition) {
            case TOP_RIGHT:
                return R.id.uid_progress_indicator_label_top_right;
            case BOTTOM_LEFT:
                return R.id.uid_progress_indicator_label_bottom_left;
            case BOTTOM_RIGHT:
                return R.id.uid_progress_indicator_label_bottom_right;
        }
        return R.id.uid_progress_indicator_label_top_left;
    }

    private int getCircularProgressBarLabelID() {
        if (labelPosition == LabelPosition.BOTTOM_CENTER) {
            return R.id.uid_progress_indicator_label_bottom_center;
        } else {
            return R.id.uid_progress_indicator_label_center;
        }
    }

    private void initializeCircularProgressBarViews() {
        label = (Label) getRootView().findViewById(getCircularProgressBarLabelID());
        progressBar = (ProgressBar) findViewById(R.id.uid_progress_indicator);
    }

    private void setLabelVisibility() {
        label.setVisibility(VISIBLE);
    }

    private int getCircularProgressBarLayout(ProgressBar.CircularProgressBarSize bar) {
        int id = R.layout.uid_determinate_circular_progress_indicator_with_label_small;
        switch (bar) {
            case SMALL:
                id = isIndeterminateProgressIndicator ? R.layout.uid_indeterminate_circular_progress_indicator_with_label_small : R.layout.uid_determinate_circular_progress_indicator_with_label_small;
                break;
            case MIDDLE:
                id = isIndeterminateProgressIndicator ? R.layout.uid_indeterminate_circular_progress_indicator_with_label_middle : R.layout.uid_determinate_circular_progress_indicator_with_label_middle;
                break;
            case BIG:
                id = isIndeterminateProgressIndicator ? R.layout.uid_indeterminate_circular_progress_indicator_with_label_big : R.layout.uid_determinate_circular_progress_indicator_with_label_big;
                break;
        }
        return id;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SavedState savedState = new ProgressBarWithLabel.SavedState(parcelable);

        savedState.label = label.getText().toString();
        savedState.progress = progress;
        savedState.secondaryProgress = secondaryProgress;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof ProgressBarWithLabel.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final ProgressBarWithLabel.SavedState savedState = (ProgressBarWithLabel.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        setText(savedState.label);

        progressBar.post(new Runnable() {
            @Override
            public void run() {
                setProgress(savedState.progress);
                setSecondaryProgress(savedState.secondaryProgress);
            }
        });
    }

    /**
     * Sets the string value of the text to the selected label
     * @param text value to set
     */
    public void setText(String text) {
        label.setText(text);
    }

    /**
     * Sets the size of the text of the selected label
     * @param size text size in sp
     */
    public void setTextSize(int size) {
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * Sets the text color of the selected label
     * @param textColor color for the label
     */
    public void setTextColor(int textColor) {
        label.setTextColor(textColor);
    }

    /**
     * Sets the progress on the determinate progress bar
     * @param progress progress to be set on progressbar
     */
    public void setProgress(int progress) {
        if (!isIndeterminateProgressIndicator) {
            progressBar.setProgress(progress);
        }
    }

    /**
     * Sets the secondary progress on the determinate progress bar
     * @param progress for the secondary progress
     */
    public void setSecondaryProgress(int progress) {
        if (!isIndeterminateProgressIndicator) {
            progressBar.setSecondaryProgress(progress);
        }
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<ProgressBarWithLabel.SavedState> CREATOR
                = new Parcelable.Creator<ProgressBarWithLabel.SavedState>() {
            public ProgressBarWithLabel.SavedState createFromParcel(Parcel in) {
                return new ProgressBarWithLabel.SavedState(in);
            }

            public ProgressBarWithLabel.SavedState[] newArray(int size) {
                return new ProgressBarWithLabel.SavedState[size];
            }
        };
        String label;
        int progress;
        int secondaryProgress;

        SavedState(final Parcelable superState) {
            super(superState);
        }

        SavedState(final Parcel in) {
            super(in);
            label = in.readString();
            progress = in.readInt();
            secondaryProgress = in.readInt();
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);

            out.writeString(label);
            out.writeInt(progress);
            out.writeInt(secondaryProgress);
        }
    }
}
