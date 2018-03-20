/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.philips.platform.uid.R;

/**
 * Layout which wraps {@link com.philips.platform.uid.view.widget.ValidationEditText} for showing error messages.
 * It uses {@link com.philips.platform.uid.view.widget.InputValidationLayout.Validator} for auto hide/show error messages.
 * Current implementation follows below <br>
 * 1. On focus change, if validate() returns false, error message is displayed.<br>
 * 2. On text change, if validate() return true, error message is removed.
 * <p/>
 * <br>
 * <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidTextBoxValidationErrorDrawable</td>
 * <td rowspan="1">Sets error drawable</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidTextBoxValidationErrorText</td>
 * <td rowspan="1">Error message</td>
 * </tr>
 * </tbody>
 * <p>
 * </table>
 * <p/>
 * <p>
 * Sample use can be as follows <br>
 * <pre>
 *              &lt;android:id="@+id/textbox_input_field"
 *                    android:layout_width="match_parent"
 *                    android:layout_height="wrap_content"
 *                    app:uidTextBoxValidationErrorDrawable="@drawable/uid_ic_data_validation"
 *                    app:uidTextBoxValidationErrorText="@string/inline_error_message"/&gt;
 *
 *                           &lt;com.philips.platform.uid.view.widget.ValidationEditText
 *                               android:layout_width="match_parent"
 *                               android:layout_height="wrap_content" /&gt;
 *         </pre>
 * </p>
 */
public class InputValidationLayout extends LinearLayout {

    /**
     * Provides call back with current {@link EditText#getText()} which can be used to validate the input.
     */
    public interface Validator {
        boolean validate(CharSequence msg);
    }
    private Label errorLabel;
    private ImageView errorIcon;
    private ViewGroup errorLayout;
    private ValidationEditText validationEditText;
    private boolean isShowingError;
    private int errorDrawableID;
    private CharSequence errorMessage;
    private Validator validator;
    private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean focussed) {
            if (!focussed && validator != null) {
                boolean result = validator.validate(validationEditText.getText());
                if (!result) {
                    showError();
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (validator != null && validator.validate(charSequence) && isShowingError) {
                hideError();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public InputValidationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, new int[]{R.attr.uidTextBoxValidationErrorDrawable, R.attr.uidTextBoxValidationErrorText});
        errorDrawableID = typedArray.getResourceId(0, -1);
        errorMessage = typedArray.getText(1);
        typedArray.recycle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof ValidationEditText) {
            validationEditText = (ValidationEditText) child;
            validationEditText.setOnFocusChangeListener(focusChangeListener);
            validationEditText.addTextChangedListener(textWatcher);
            ensureErrorLayout();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.isShowingError = isShowingError;
        savedState.errorLabelState = errorLabel.onSaveInstanceState();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        isShowingError = savedState.isShowingError;
        errorLabel.onRestoreInstanceState(savedState.errorLabelState);

        if (isShowingError) {
            showError();
        }
    }

    /**
     * Use this to set a validator. Depending upon the return value, the error message is shown.
     *
     * @param validator instance of the validator
     *                  @since 3.0.0
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Sets error message based on resourceID.
     *
     * @param resID Resource id of the message
     *              @since 3.0.0
     */
    public void setErrorMessage(int resID) {
        if (errorLabel != null) {
            errorLabel.setText(resID);
            errorLabel.setVisibility(VISIBLE);
        }
    }

    /**
     * Sets the error message.
     *
     * @param errorMsg CharSeq of the message
     *                 @since 3.0.0
     */
    public void setErrorMessage(CharSequence errorMsg) {
        if (errorLabel != null) {
            errorLabel.setText(errorMsg);
            errorLabel.setVisibility(VISIBLE);
        }
    }

    /**
     * Sets error drawable.
     *
     * @param drawable Drawable to be set for error
     *                 @since 3.0.0
     */
    @SuppressWarnings("unused")
    public void setErrorDrawable(Drawable drawable) {
        errorIcon.setImageDrawable(drawable);
        errorIcon.setVisibility(VISIBLE);
    }

    /**
     * Sets error drawable from the resourceID
     * @param resID ResId of the Drawable
     *              @since 3.0.0
     */
    @SuppressWarnings("unused")
    public void setErrorDrawable(@DrawableRes int resID) {
        errorIcon.setImageResource(resID);
        errorIcon.setVisibility(VISIBLE);
    }

    /**
     * Call this to display the error. Error text and drawable must be set before calling this api.
     * @since 3.0.0
     */
    public void showError() {
        isShowingError = true;
        validationEditText.setError(true);
        errorLayout.setVisibility(VISIBLE);
    }

    private void ensureErrorLayout() {
        if (errorLayout == null) {
            errorLayout = (ViewGroup) View.inflate(getContext(), R.layout.uid_inline_validation_input, null);
            errorLabel = (Label) errorLayout.findViewById(R.id.uid_inline_validation_text);
            if (!TextUtils.isEmpty(errorMessage)) {
                errorLabel.setText(errorMessage);
                errorLabel.setVisibility(VISIBLE);
            }

            errorIcon = (ImageView) errorLayout.findViewById(R.id.uid_inline_validation_icon);
            if (errorDrawableID != -1) {
                errorIcon.setImageResource(errorDrawableID);
                errorIcon.setVisibility(VISIBLE);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.uid_inline_validation_message_margin_top);
            layoutParams.setMargins(0, topMargin, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;

            addView(errorLayout, layoutParams);
        }
    }

    /**
     * Call to hideError message and error layout.
     * @since 3.0.0
     */
    public void hideError() {
        isShowingError = false;
        validationEditText.setError(false);
        errorLayout.setVisibility(GONE);
    }

    /**
     * Returns current error state.
     *
     * @return true if the error was displaying
     * @since 3.0.0
     */
    public boolean isShowingError() {
        return isShowingError;
    }

    /**
     * Returns error text view.
     *
     * @return errortextView
     * @since 3.0.0
     */
    public Label getErrorLabelView() {
        return errorLabel;
    }

    /**
     * Returns error icon view.
     *
     * @return Imageview containing error icon
     * @since 3.0.0
     */
    public ImageView getErrorIconView() {
        return errorIcon;
    }

    public ViewGroup getErrorLayout() {
        return errorLayout;
    }

    private static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private Parcelable errorLabelState;
        private boolean isShowingError;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            errorLabelState = in.readParcelable(getClass().getClassLoader());
            isShowingError = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(errorLabelState, flags);
            out.writeByte((byte) (this.isShowingError ? 1 : 0));
        }
    }
}