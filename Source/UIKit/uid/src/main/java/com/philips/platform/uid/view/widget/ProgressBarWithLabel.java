package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.UIDUtils;

public class ProgressBarWithLabel extends FrameLayout {

    public enum LabelPosition {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, BOTTOM_CENTER}

    private boolean isLinearProgressBarEnabled = false;
    private boolean isIndeterminateProgressIndicator = false;
    private LabelPosition labelPosition = LabelPosition.TOP_LEFT;
    private Label labelTopLeft;
    private Label labelTopRight;
    private Label labelBottomLeft;
    private Label labelBottomRight;
    private ProgressBar progressBar;
    private int textSize;
    private int textColor;
    private float textAlpha;
    private String text;
    private int progress;
    private int secondaryProgress;
    private IndeterminateLinearProgressBar indeterminateLinearProgressBar;
    private Label labelCenter;
    private Label labelBottomCenter;
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

        obtainStyleAttributes(context, attrs, defStyleAttr);

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

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressIndicatorWithLabel);
        isLinearProgressBarEnabled = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressIndicatorWithLabel_uidIsLinearProgressBar, false);
        isIndeterminateProgressIndicator = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressIndicatorWithLabel_uidIsIndeterminateProgressIndicator, false);
        labelPosition = LabelPosition.values()[obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_uidLabelPosition, 0)];
        text = obtainStyledAttributes.getString(R.styleable.UIDProgressIndicatorWithLabel_android_text);

        textSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.UIDProgressIndicatorWithLabel_android_textSize, -1);
        textColor = obtainStyledAttributes.getColor(R.styleable.UIDProgressIndicatorWithLabel_android_textColor, -1);
        textAlpha = obtainStyledAttributes.getFloat(R.styleable.UIDProgressIndicatorWithLabel_uidLabelTextAlpha, -1);
        progress = obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_android_progress, 0);
        secondaryProgress = obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_android_secondaryProgress, 0);
        circularProgressBarSize = ProgressBar.CircularProgressBarSize.values()[obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_uidCircularProgressBarSize, 0)];

        obtainStyledAttributes.recycle();
    }

    private void setAttributes() {
        if (textSize != -1) {
            setTextSize(textSize);
        }

        if (textColor != -1 && textAlpha != -1) {
            setTextColor(UIDUtils.modulateColorAlpha(textColor, textAlpha));
        }

        setText(text);
        setProgress(progress);
        setSecondaryProgress(secondaryProgress);
        setLabelVisibility();
    }

    private void initializeLinearProgressBarViews() {
        label = (Label) getRootView().findViewById(getLinearProgressBarLabelID());

        if (isIndeterminateProgressIndicator) {
            indeterminateLinearProgressBar = (IndeterminateLinearProgressBar) findViewById(R.id.uid_progress_indicator);
        } else {
            progressBar = (ProgressBar) findViewById(R.id.uid_progress_indicator);
        }
    }

    private int getLinearProgressBarLabelID() {
        switch (labelPosition) {
            case TOP_RIGHT:
                return R.id.uid_progress_indicator_label_top_right;
            case BOTTOM_LEFT:
                return  R.id.uid_progress_indicator_label_bottom_left;
            case BOTTOM_RIGHT:
                return R.id.uid_progress_indicator_label_bottom_right;
        }
        return R.id.uid_progress_indicator_label_top_left;
    }

    private int getCircularProgressBarLabelID() {
        switch (labelPosition) {
            case BOTTOM_CENTER:
                return R.id.uid_progress_indicator_label_bottom_center;
        }
        return R.id.uid_progress_indicator_label_center;
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

    public void setText(String text) {
        label.setText(text);
    }

    public void setTextSize(int size) {
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTextColor(int textColor) {
        label.setTextColor(textColor);
    }

    public void setProgress(int progress) {
        if (!isIndeterminateProgressIndicator) {
            progressBar.setProgress(progress);
        }
    }

    public void setSecondaryProgress(int progress) {
        if (!isIndeterminateProgressIndicator) {
            progressBar.setSecondaryProgress(progress);
        }
    }
}
