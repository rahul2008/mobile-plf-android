package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiEditText extends RelativeLayout {

    public interface Validator {
        boolean validate(String inputToBeValidated);
    }

    private static int viewId = 10000001;
    private EditText editText;
    private ImageView errorImage;
    private TextView errorTextView;
    private int errorTextColor;
    private Drawable errorIcon;
    private Drawable errorBackground;
    private Drawable themeDrawable;
    private Validator validator;
    private String textToSave;
    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            if (!hasFocus) {
                showErrorAndChangeEditTextStyle(!(validator == null || validator.validate(editText.getText().toString())));
            } else {
                editText.setTextColor(getResources().getColor(R.color.uikit_philips_very_dark_blue));
            }
        }
    };

    public PuiEditText(final Context context) {
        super(context);
    }

    public PuiEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uikit_input_text_field, this, true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputTextField);
        int editTextWidth = a.getDimensionPixelSize(R.styleable.InputTextField_inputFieldWidth, LayoutParams.WRAP_CONTENT);
        int editTextHeight = a.getDimensionPixelSize(R.styleable.InputTextField_inputFieldHeight, LayoutParams.WRAP_CONTENT);
        boolean singleLine = a.getBoolean(R.styleable.InputTextField_singleLine, true);
        String editTextHint = a.getString(R.styleable.InputTextField_hintText);
        String errorText = a.getString(R.styleable.InputTextField_errorText);
        boolean disabled = a.getBoolean(R.styleable.InputTextField_disabled, false);
        errorTextColor = a.getColor(R.styleable.InputTextField_errorTextColor, getResources().getColor(R.color.uikit_philips_bright_orange));
        errorIcon = a.getDrawable(R.styleable.InputTextField_errorIcon);
        errorBackground = a.getDrawable(R.styleable.InputTextField_errorBackground);
        a.recycle();

        setSaveEnabled(true);
        initEditText(editTextHint, disabled, editTextWidth, singleLine, editTextHeight);

        themeDrawable = editText.getBackground();

        initErrorIcon();

        initErrorMessage(errorText);

        errorImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                setErrorMessageVisibilty(View.GONE);
            }
        });
    }

    public PuiEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        editText.setText(savedState.savedText);
        showErrorAndChangeEditTextStyle(View.VISIBLE == savedState.showError);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.savedText = editText.getEditableText().toString();
        savedState.showError = errorImage.getVisibility();

        return savedState;
    }

    private void initErrorMessage(final String errorText) {
        errorTextView = (TextView) getChildAt(3);
        errorTextView.setText(errorText);
        errorTextView.setTextColor(errorTextColor);
    }

    private void initErrorIcon() {
        errorImage = (ImageView) getChildAt(2);
        errorImage.setImageDrawable(errorIcon);
    }

    private void initEditText(final String editTextHint, final boolean disabled, final int editTextWidth, final boolean singleLine, int editTextHeight) {
        editText = (EditText) getChildAt(0);
        editText.setId(viewId++);
        if (editTextWidth > 0) editText.setWidth(editTextWidth);
        if (editTextHeight > 0) editText.setHeight(editTextHeight);

        editText.setSingleLine(singleLine);
        editText.setHint(editTextHint);
        editText.setFocusable(!disabled);
        editText.setEnabled(!disabled);
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setEditTextEnabled(boolean enabled) {
        editText.setEnabled(enabled);
    }

    private void showErrorAndChangeEditTextStyle(boolean show) {
        if (show) {
            setErrorMessageVisibilty(View.VISIBLE);
            setErrorTextStyle();
        } else {
            setErrorMessageVisibilty(View.GONE);
            editText.setTextColor(getResources().getColor(R.color.uikit_philips_very_dark_blue));
            setBackgroundAsPerAPILevel(themeDrawable);
        }
    }

    private void setErrorTextStyle() {
        editText.setTextColor(errorTextColor);
        setBackgroundAsPerAPILevel(errorBackground);
    }

    private void setErrorMessageVisibilty(int visibility) {
        errorTextView.setVisibility(visibility);
        errorImage.setVisibility((visibility));
    }

    private void setBackgroundAsPerAPILevel(final Drawable backgroundDrawable) {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(backgroundDrawable);
        } else {
            editText.setBackgroundDrawable(backgroundDrawable);
        }
    }

    static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(final Parcel source) {
                        return new SavedState(source);
                    }

                    @Override
                    public SavedState[] newArray(final int size) {
                        return new SavedState[size];
                    }
                };
        String savedText;
        int showError;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.savedText = in.readString();
            this.showError = in.readInt();
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.savedText);
            out.writeInt(this.showError);
        }
    }
}
