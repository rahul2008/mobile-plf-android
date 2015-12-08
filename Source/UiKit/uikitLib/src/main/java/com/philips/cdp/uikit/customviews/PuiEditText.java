package com.philips.cdp.uikit.customviews;

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

import com.philips.cdp.uikit.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiEditText extends RelativeLayout {

    private EditText editText;
    private ImageView errorImage;
    private TextView errorTextView;
    private int errorTextColor;
    private Drawable errorIcon;
    private Drawable errorBackground;
    private Drawable themeDrawable;
    private Validator validator;
    private boolean focused;
    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            focused = hasFocus;
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
        boolean enabled = a.getBoolean(R.styleable.InputTextField_enabled, true);
        errorTextColor = a.getColor(R.styleable.InputTextField_errorTextColor, getResources().getColor(R.color.uikit_philips_bright_orange));
        errorIcon = a.getDrawable(R.styleable.InputTextField_errorIcon);
        errorBackground = a.getDrawable(R.styleable.InputTextField_errorBackground);
        a.recycle();

        setSaveEnabled(true);
        initEditText(editTextHint, enabled, editTextWidth, singleLine, editTextHeight);

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

    /**
     * Restore the state of the EditText and error icon and error message.
     * See {@link SavedState} for details of saved values.
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.setText(savedState.savedText);
                int focused = savedState.focused;
                if (focused == 1) {
                    editText.requestFocus();
                }
            }
        });
        showErrorAndChangeEditTextStyle(View.VISIBLE == savedState.showError);
    }

    /**
     * Save the state of the EditText and error icon and error message.
     * See {@link SavedState} for details of saved values.
     * @return Parcelable with SavedState.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.savedText = editText.getEditableText().toString();
        savedState.showError = errorImage.getVisibility();
        if (focused) {
            savedState.focused = 1;
        } else {
            savedState.focused = -1;
        }
        return savedState;
    }

    /**
     * Returns the Edittext component of the PuiEditText.
     * The developer is free to add constraints on this EditText to fit their needs.
     *
     * @return Edittext
     */
    public EditText getEditText() {
        return editText;
    }

    /**
     * Returns the error text TextView component of the PuiEditText.
     * The developer is free to add constraints on this TextView to fit their needs.
     *
     * @return Edittext
     */
    public TextView getErrorText() {
        return errorTextView;
    }

    /**
     * Returns the error icon ImageView component of the PuiEditText.
     * The developer is free to add constraints on this ImageView to fit their needs.
     *
     * @return Edittext
     */
    public ImageView getErrorIcon() {
        return errorImage;
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

    private void initEditText(final String editTextHint, final boolean enabled, final int editTextWidth, final boolean singleLine, int editTextHeight) {
        editText = (EditText) getChildAt(0);
        if (editTextWidth > 0) setWidth(editTextWidth);
        if (editTextHeight > 0) setHeight(editTextHeight);
        if (focused) editText.requestFocus();
        editText.setSingleLine(singleLine);
        editText.setHint(editTextHint);
        editText.setFocusable(enabled);
        editText.setEnabled(enabled);
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Set the width of the edittext.
     * Can be set through XML by using the inputField:width attribute.
     *
     * @param width
     */
    public void setWidth(int width) {
        editText.setWidth(width);
    }

    /**
     * Set the height of the edit text.
     * Do no modify this value unless absolutely nessecary.
     * The default value of edittext height is 44dp as per Philips Design guidelines.
     *
     * @param height
     */
    public void setHeight(int height) {
        editText.setHeight(height);
    }

    /**
     * Set the enabled state of this view.
     * @param enabled
     */
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

    public interface Validator {
        boolean validate(String inputToBeValidated);
    }

    /**
     * Class to save the state of the existing Edittext.
     * We will use this class to restore the state of the Edittext after orientation change.
     * variable savedText Text to be saved when view is destroyed.
     * variable showError Error visibility state of the destroyed Edittext.
     * variable focused Weather the current Edittext was focused when the view was destroyed.
     */
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
        int focused;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.savedText = in.readString();
            this.showError = in.readInt();
            this.focused = in.readInt();
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.savedText);
            out.writeInt(this.showError);
            out.writeInt(this.focused);
        }
    }
}
