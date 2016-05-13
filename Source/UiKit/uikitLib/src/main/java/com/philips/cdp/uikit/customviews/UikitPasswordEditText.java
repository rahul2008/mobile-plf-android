/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
/**
 * Created by 310213373 on 3/18/2016.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * <b></b> UikitPasswordEditTetxt is UI Component providing password masking/unmasking capability</b>
 * <p/>
 *  <b></b> Inline forms can use this component by replacing the editText with UikitPasswordEditText</b>
 *  <p/>
 *  <b></b> This can be used as part of PuiEditText as well</b>
 *  <p/>
 *  <b></b>To use as PuiEditText add the flag inputText:uikit_password_edit_field="true" in the XML file</b>
 *  <p/>
 *  <H3>Sample Code</H3>
 *  <pre>
 *  <com.philips.cdp.uikit.customviews.PuiEditText
         android:layout_height="wrap_content"
         android:id="@+id/password"
         android:hint="password"
         android:layout_centerHorizontal="true"
         inputText:uikit_hintText="Password"
         inputText:uikit_errorText="@string/invalid_pwd_format"
         inputText:uikit_password_edit_field="true"
    </pre>
 />
 */


public class UikitPasswordEditText extends AppCompatEditText implements TextWatcher {


    private static final int[] STATE_EMPTY_PASSWORD = {R.attr.uikit_state_emptyPassword};
    private static final int[] STATE_MASKED_PASSWORD = {R.attr.uikit_state_maskedPassword};
    private static final int[] STATE_SHOW_PASSWORD = {R.attr.uikit_state_showPassword};
    final int DRAWABLE_RIGHT = 2;
    int basecolor,brightColor;
    private boolean isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    Context context;
    private boolean passwordVisible;

    public UikitPasswordEditText(final Context cont, AttributeSet attrs) {
        super(cont, attrs);
        context=cont;
        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor,R.attr.uikit_brightColor});
        basecolor = a.getInt(0, R.attr.uikit_baseColor);
        brightColor = a.getInt(1,R.attr.uikit_brightColor);
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(null, null, getIcon(), null);
        setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.uikit_tab_badge_margin_top));
        setEnabled(true);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        handlePasswordInputVisibility();
        addTextChangedListener(this);
        // Code to disable the long press for copy/paste action mode for password fields
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        setLongClickable(false);
        setTextIsSelectable(false);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (getRight() - getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        togglePasswordIconVisibility();

                        post(new Runnable() {
                            @Override
                            public void run() {
                                setSelection(getText().length());
                            }
                        });

                        return false;

                    }
                }
                return false;
            }
        });

    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused) {
            passwordVisible = false;
            handlePasswordInputVisibility();
        }
    }

    private Drawable getIcon() {

        Drawable d = VectorDrawable.create(context, R.drawable.uikit_password_show_icon).getConstantState().newDrawable().mutate();
        if (isPreLollipop) {
            return wrap(d);
        } else {

            d.setTintList(getColorStateList());
            return d;
        }
    }

    private ColorStateList getColorStateList(){
        int[][] states = {{R.attr.uikit_state_emptyPassword}, {R.attr.uikit_state_maskedPassword},{R.attr.uikit_state_showPassword}};
        int[] colors = { ContextCompat.getColor(context, R.color.uikit_enricher4), ContextCompat.getColor(context,R.color.uikit_password_icon_color), brightColor};

        return new ColorStateList(states, colors);
    }
    private Drawable wrap(Drawable d) {
        if (d == null) return null;

        Drawable wrappedDrawable = DrawableCompat.wrap(d).mutate();
        wrappedDrawable.setBounds(d.getBounds());

        if (wrappedDrawable instanceof DrawableWrapper) {
            ((DrawableWrapper) wrappedDrawable).setCompatTintList(getColorStateList());
            ((DrawableWrapper) wrappedDrawable).setCompatTintMode(PorterDuff.Mode.SRC_ATOP);
        } else {
            wrappedDrawable.setTintList(getColorStateList());
        }
        return wrappedDrawable;
    }

    @Override
    public boolean isLongClickable() {
        return false;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 3);
        if(isEmpty()) {
            mergeDrawableStates(drawableState, STATE_EMPTY_PASSWORD);
        } else if((getTransformationMethod()) instanceof PasswordTransformationMethod) {
            mergeDrawableStates(drawableState, STATE_MASKED_PASSWORD);
        } else {
            mergeDrawableStates(drawableState, STATE_SHOW_PASSWORD);
        }
        return drawableState;
    }

    private boolean isEmpty() {
        boolean result = false;
        try {
            result = TextUtils.isEmpty(getText().toString());
        } catch (Exception e) {
            result = true;
        }
        return result;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    /**
     * Setting the different states for the compound drawable image i.e the eye image
     */
    @Override
    public void afterTextChanged(Editable s) {

        /*if(s.length() == 0){
            getCompoundDrawables()[DRAWABLE_RIGHT].setState(STATE_EMPTY_PASSWORD);
            if ((getTransformationMethod()) instanceof PasswordTransformationMethod)
            {
                //do nothing
            }
            else setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisible = false;
        } else */
        if((getTransformationMethod()) instanceof PasswordTransformationMethod) {
            passwordVisible = false;
            getCompoundDrawables()[DRAWABLE_RIGHT].setState(STATE_MASKED_PASSWORD);
        } else {
            passwordVisible = true;
            getCompoundDrawables()[DRAWABLE_RIGHT].setState(STATE_SHOW_PASSWORD);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

    }



    private void togglePasswordIconVisibility() {
        passwordVisible = !passwordVisible;
        handlePasswordInputVisibility();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, passwordVisible);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        passwordVisible = savedState.isPasswordVisible();
        handlePasswordInputVisibility();
    }

    /**
     * This method is called when restoring the state (e.g. on orientation change)
     */
    private void handlePasswordInputVisibility() {
        if (passwordVisible) {
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    /**
     * Save the state of the password field.
     */
    protected static class SavedState extends BaseSavedState {

        private final boolean mPasswordVisible;

        private SavedState(Parcelable superState, boolean showingIcon) {
            super(superState);
            mPasswordVisible = showingIcon;
        }

        private SavedState(Parcel in) {
            super(in);
            mPasswordVisible = in.readByte() != 0;
        }

        public boolean isPasswordVisible() {
            return mPasswordVisible;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeByte((byte) (mPasswordVisible ? 1 : 0));
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };
    }
}

