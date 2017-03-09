package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;

public class TextInputLayout extends LinearLayout {

    private Label validationText;
    private ValidationEditText validationEditText;
    private ImageView validationIcon;

    public TextInputLayout(Context context) {
        super(context);
        initialize(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uid_inline_validation_input, this, true);

        validationEditText = (ValidationEditText) findViewById(R.id.uid_inline_validation_edittext);
        validationText = (Label) findViewById(R.id.uid_inline_validation_text);
        validationIcon = (ImageView) findViewById(R.id.uid_inline_validation_icon);
    }

}
