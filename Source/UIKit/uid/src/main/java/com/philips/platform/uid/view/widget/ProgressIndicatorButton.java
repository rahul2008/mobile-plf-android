package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.uid.R;

public class ProgressIndicatorButton extends LinearLayout {

    private Button button;
    private ProgressBar progressBar;
    private TextView textView;

    public ProgressIndicatorButton(final Context context) {
        this(context, null);
    }

    public ProgressIndicatorButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicatorButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View layout = View.inflate(getContext(), R.layout.uid_progress_indicator_button, null);
        button = (Button) layout.findViewById(R.id.uid_progress_indicator_button_button);
        progressBar = (ProgressBar) layout.findViewById(R.id.uid_progress_indicator_button_progress_bar);
        textView = (TextView) layout.findViewById(R.id.uid_progress_indicator_button_text);
        addView(layout);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        button.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(button.getMeasuredWidth(), button.getMeasuredHeight());
    }
}
