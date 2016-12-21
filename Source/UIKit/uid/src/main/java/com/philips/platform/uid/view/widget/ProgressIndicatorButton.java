package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class ProgressIndicatorButton extends LinearLayout {

    private Button button;
    private ProgressBar progressIndicatorButtonProgressBar;
    private TextView progressIndicatorButtonTextView;

    public ProgressIndicatorButton(final Context context) {
        this(context, null);
    }

    public ProgressIndicatorButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicatorButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDProgressIndicatorButton, defStyleAttr, 0);
        boolean isIndeterminateProgressIndicator = typedArray.getBoolean(R.styleable.UIDProgressIndicatorButton_uidIsIndeterminateProgressIndicator, true);

        inflateLayout(isIndeterminateProgressIndicator);

        int buttonDrawableId = typedArray.getResourceId(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonButtonDrawable, -1);
        int progressButtonBackgroundDrawableId = typedArray.getResourceId(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgressBackground, R.drawable.uid_progress_indicator_button_background);
        int progress = typedArray.getInt(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgress, 0);
        String buttonText = typedArray.getString(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonButtonText);
        String progressText = typedArray.getString(R.styleable.UIDProgressIndicatorButton_uidProgressIndicatorButtonProgressText);
        typedArray.recycle();

        setButtonDrawable(buttonDrawableId);
        setButtonText(buttonText);
        button.setOnClickListener(buttonOnClickListener);

        setProgressIndicatorButtonProgress(progress);
        setProgressIndicatorButtonText(progressText);

        setBackground(ContextCompat.getDrawable(context, progressButtonBackgroundDrawableId));
    }

    private void inflateLayout(final boolean isIndeterminateProgressIndicator) {
        View layout;
        if (isIndeterminateProgressIndicator) {
            layout = View.inflate(getContext(), R.layout.uid_progress_indicator_button_indeterminate, null);
        } else {
            layout = View.inflate(getContext(), R.layout.uid_progress_indicator_button_determinate, null);
        }

        button = (Button) layout.findViewById(R.id.uid_progress_indicator_button_button);
        progressIndicatorButtonProgressBar = (ProgressBar) layout.findViewById(R.id.uid_progress_indicator_button_progress_bar);
        progressIndicatorButtonTextView = (TextView) layout.findViewById(R.id.uid_progress_indicator_button_text);
        addView(layout);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        button.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button.getMeasuredWidth(), button.getMeasuredHeight());
    }

    private OnClickListener buttonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            showProgressIndicatorButton();
        }
    };

    public void showProgressIndicatorButton() {
        button.setVisibility(View.GONE);
        progressIndicatorButtonProgressBar.setVisibility(View.VISIBLE);

        if (progressIndicatorButtonTextView.getText().length() > 0) {
            progressIndicatorButtonTextView.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) progressIndicatorButtonProgressBar.getLayoutParams();
            params.rightMargin = params.leftMargin;
            progressIndicatorButtonProgressBar.setLayoutParams(params);
        }
    }

    public void setProgressIndicatorButtonProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            progressIndicatorButtonProgressBar.setProgress(progress);
        }
    }

    public void setProgressIndicatorButtonText(String text) {
        if (!TextUtils.isEmpty(text)) {
            progressIndicatorButtonTextView.setText(text);
        }
    }

    public void setButtonText(String text) {
        if (!TextUtils.isEmpty(text)) {
            button.setText(text);
        }
    }

    public void setButtonDrawable(int drawableId) {
        if (drawableId != -1) {
            setButtonDrawable(ContextCompat.getDrawable(getContext(), drawableId));
        }
    }

    public void setButtonDrawable(Drawable drawable) {
        if (drawable != null) {
            button.setImageDrawable(drawable);
        }
    }
}

