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

    public ProgressBarWithLabel(final Context context) {
        this(context, null);
    }

    public ProgressBarWithLabel(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarWithLabel(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyleAttributes(context, attrs, defStyleAttr);

        int id = isIndeterminateProgressIndicator ? R.layout.uid_indeterminate_linear_progress_indicator_with_label : R.layout.uid_linear_progress_indicator_with_label;
        addView(View.inflate(getContext(), id, null));

        initializeViews();
        setAttributes();
    }

    private void obtainStyleAttributes(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressIndicatorWithLabel);
        isLinearProgressBarEnabled = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressIndicatorWithLabel_uidLinearProgressBar, false);
        isIndeterminateProgressIndicator = obtainStyledAttributes.getBoolean(R.styleable.UIDProgressIndicatorWithLabel_uidIsIndeterminateProgressIndicator, false);
        labelPosition = LabelPosition.values()[obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_uidLabelPosition, 0)];
        text = obtainStyledAttributes.getString(R.styleable.UIDProgressIndicatorWithLabel_android_text);

        textSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.UIDProgressIndicatorWithLabel_android_textSize, -1);
        textColor = obtainStyledAttributes.getColor(R.styleable.UIDProgressIndicatorWithLabel_android_textColor, -1);
        textAlpha = obtainStyledAttributes.getFloat(R.styleable.UIDProgressIndicatorWithLabel_uidLabelTextAlpha, -1);
        progress = obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_android_progress, 0);
        secondaryProgress = obtainStyledAttributes.getInt(R.styleable.UIDProgressIndicatorWithLabel_android_secondaryProgress, 0);

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
        setLabelVisibility(labelPosition);
    }

    private void initializeViews() {
        labelTopLeft = (Label) getRootView().findViewById(R.id.uid_progress_indicator_label_top_left);
        labelTopRight = (Label) getRootView().findViewById(R.id.uid_progress_indicator_label_top_right);
        labelBottomLeft = (Label) getRootView().findViewById(R.id.uid_progress_indicator_label_bottom_left);
        labelBottomRight = (Label) getRootView().findViewById(R.id.uid_progress_indicator_label_bottom_right);

        if (isIndeterminateProgressIndicator) {
            indeterminateLinearProgressBar = (IndeterminateLinearProgressBar) findViewById(R.id.uid_progress_indicator);
        } else {
            progressBar = (ProgressBar) findViewById(R.id.uid_progress_indicator);
        }
    }

    private void setLabelVisibility(LabelPosition position) {
        labelTopLeft.setVisibility(position == LabelPosition.TOP_LEFT ? VISIBLE : GONE);
        labelTopRight.setVisibility(position == LabelPosition.TOP_RIGHT ? VISIBLE : GONE);
        labelBottomLeft.setVisibility(position == LabelPosition.BOTTOM_LEFT ? VISIBLE : GONE);
        labelBottomRight.setVisibility(position == LabelPosition.BOTTOM_RIGHT ? VISIBLE : GONE);
    }

    public void setText(String text) {
        labelTopLeft.setText(text);
        labelTopRight.setText(text);
        labelBottomLeft.setText(text);
        labelBottomRight.setText(text);
    }

    public void setTextSize(int size) {
        labelTopLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        labelTopRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        labelBottomLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        labelBottomRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTextColor(int textColor) {
        labelTopLeft.setTextColor(textColor);
        labelTopRight.setTextColor(textColor);
        labelBottomLeft.setTextColor(textColor);
        labelBottomRight.setTextColor(textColor);
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
