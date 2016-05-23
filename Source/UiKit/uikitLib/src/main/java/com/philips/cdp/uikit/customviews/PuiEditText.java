/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * Layout which wraps EditText internally and facilitates to show error.
 * <p/>
 * Use {@link Validator} interface with {@link PuiEditText#setValidator(Validator)} to enable
 * error display.
 * <p/>
 * <p>
 * <H3>Custom Attributes</H3>
 * <b>singleLine:</b> EditText will be restricted to a single line.<br>
 * <b>hintText:</b> EditText hint.<br>
 * <b>errorText:</b> Text to be displyed below the EditText when input validation fails.<br>
 * <b>enabled:</b> Enables the EditText (allows user to click and enter the text).<br>
 * <b>errorTextColor:</b> Do not change unless absolutely necessary. Will be provided by the Theme.
 * Sets the color of the error text shown.<br>
 * <b>errorIcon:</b> Do not change unless absolutely necessary. Will be provided by the Theme. The
 * error icon shown below the EditText.<br>
 * <b>errorBackground:</b> Do not change unless absolutely necessary. Will be provided by the Theme.
 * It's a drawable which is set as the background of the EditText when input validation fails.<br>
 * </p>
 * <p/>
 * Example
 * <br>
 * <pre>
 *     &lt;com.philips.cdp.uikit.customviews.PuiEditText
 *          android:id="@+id/input_field_1"  			    -> Unique id of the PuiEditText to retain value across different config changes
 *          android:layout_width="wrap_content" 			-> Width of the EditText and layout container
 *          android:layout_height="wrap_content" 			-> Height of the layout container
 *          app:errorText="@string/invalid_email_format" 	-> Error text to be shown when validation fails.
 *          app:hintText="@string/enter_text_here" 		    -> EditText hint text.
 *          inputText:enabled="false"                       -> Add this if you want to disable the EditText
 *          inputText:uikit_password_edit_field="true"       ->Add this if you want a password Edit Text with icon and password ( Clicking on password ey image will show and hide password)
 *          app:singleLine="true"/&gt; -> Is EditText restricted to a single line.
 * </pre>
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
    Context context;
    ColorStateList csl;
    int basecolor;
    boolean isPassword;
    int index;

    /**
     * Interface to be registered in case app wants to show error message.<br>
     * Returning true will show the error.
     * <H3>Sample Code</H3>
     * <pre>
     *     puiEditText1 = (PuiEditText) findViewById(R.id.someID);
     *      puiEditText1.setValidator(new PuiEditText.Validator() {
     *       public boolean validate(final String inputToBeValidated) {
     *           return validateEmail(inputToBeValidated);
     *       }
     *   });
     *
     *    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";
     *
     *   private boolean validateEmail(final String email) {
     *       if (email == null) return false;
     *       Pattern pattern = Pattern.compile(EMAIL_PATTERN);
     *       Matcher matcher = pattern.matcher(email);
     *       return matcher.matches();
     *   }
     * </pre>
     */
    public interface Validator {
        /**
         * App gets the callback with the current string in the editbox.
         * <br> App can validate the string and return true to show error.
         *
         * @param inputToBeValidated Current text in edittext.
         * @return true: Show error , false: no effect
         */
        boolean validate(String inputToBeValidated);
    }

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

    public PuiEditText(final Context cont, final AttributeSet attrs) {
        super(cont, attrs);
        LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        context = cont;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputTextField);
        boolean singleLine = a.getBoolean(R.styleable.InputTextField_uikit_singleLine, true);
        String editTextHint = a.getString(R.styleable.InputTextField_uikit_hintText);
        String errorText = a.getString(R.styleable.InputTextField_uikit_errorText);
        boolean enabled = a.getBoolean(R.styleable.InputTextField_uikit_enabled, true);
        errorTextColor = a.getColor(R.styleable.InputTextField_uikit_errorTextColor, getResources().getColor(R.color.uikit_philips_bright_orange));
        errorIcon = VectorDrawable.create(context, R.drawable.uikit_red_error_cross).getConstantState().newDrawable().mutate();;
        errorBackground =a.getDrawable(R.styleable.InputTextField_uikit_errorBackground);
        isPassword = a.getBoolean(R.styleable.InputTextField_uikit_password_edit_field, false);
        a.recycle();
        a = getContext().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        basecolor = a.getInt(0, R.attr.uikit_baseColor);
        setPadding(10, 10, 10, 10);
        a.recycle();
        setSaveEnabled(true);

        if (isPassword) {
            inflater.inflate(R.layout.uikit_input_password_field, this, true);
            initEditText(editTextHint,enabled,false);
        }
        else {
            inflater.inflate(R.layout.uikit_input_text_field, this, true);
            initEditText(editTextHint, enabled,/* editTextWidth,*/ singleLine/*, editTextHeight*/);

        }

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
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getWidth() > 0) {
            editText.setWidth(getWidth());
        }
    }

    /**
     * Restore the state of the EditText and error icon and error message.
     * See {@link SavedState} for details of saved values.
     *
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
                editText.setSelection(savedState.savedText.length());
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
     *
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

    private void initEditText(final String editTextHint, final boolean enabled, /*final int editTextWidth,*/ final boolean singleLine/*, int editTextHeight*/) {
        if(!isPassword)
        editText = (EditText) getChildAt(0);
        else {
            editText = (UikitPasswordEditText) getChildAt(0);
        }
/*        if (editTextWidth > 0) setWidth(editTextWidth);
        if (editTextHeight > 0) setHeight(editTextHeight);*/
        if (focused) editText.requestFocus();
        if(!isPassword) {
            editText.setSingleLine(singleLine);
        }
        editText.setHint(editTextHint);
        editText.setFocusable(enabled);
        editText.setEnabled(enabled);
        editText.setOnFocusChangeListener(onFocusChangeListener);
    }

    /**
     * Use this to set a validator. Dependign upon the return value, the error message is shown.
     *
     * @param validator
     */
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
     * Sets the hint for the edit text.
     *
     * @param hint Hint for the edit text
     */
    public void setHintText(String hint) {
        editText.setHint(hint);
    }

    /**
     * Set the enabled state of this view.
     *
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

    /**
     * Class to save the state of the existing Edittext.
     * We will use this class to restore the state of the Edittext after orientation change.
     * variable savedText Text to be saved when view is destroyed.
     * variable showError Error visibility state of the destroyed Edittext.
     * variable focused Weather the current Edittext was focused when the view was destroyed.
     */
    private static class SavedState extends BaseSavedState {

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

    public void setPassword() {
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, getIcon(), null);
        editText.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.uikit_tab_badge_margin_top));
        editText.setEnabled(true);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if ((editText.getTransformationMethod()) instanceof PasswordTransformationMethod)
            editText.setTransformationMethod(null);
        else editText.setTransformationMethod(new PasswordTransformationMethod());

        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        int index = editText.getSelectionEnd();
                        if ((editText.getTransformationMethod()) instanceof PasswordTransformationMethod)
                            editText.setTransformationMethod(null);

                        else editText.setTransformationMethod(new PasswordTransformationMethod());

                        cancelLongPress();
                        editText.setSelection(index);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private Drawable getIcon() {
        Drawable d = VectorDrawable.create(context, R.drawable.uikit_password_show_icon).getConstantState().newDrawable().mutate();
        return d;
    }

    public void togglePassword()
        {
            if ((editText.getTransformationMethod()) instanceof PasswordTransformationMethod)
                editText.setTransformationMethod(null);

            else editText.setTransformationMethod(new PasswordTransformationMethod());

        }
}
