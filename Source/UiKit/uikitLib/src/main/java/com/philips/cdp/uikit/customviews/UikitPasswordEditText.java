
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;


/**
 * Created by 310213373 on 3/18/2016.
 */

public class UikitPasswordEditText extends AppCompatEditText implements TextWatcher {


    private static final int[] STATE_EMPTY_PASSWORD = {R.attr.uikit_state_emptyPassword};
    private static final int[] STATE_MASKED_PASSWORD = {R.attr.uikit_state_maskedPassword};
    private static final int[] STATE_SHOW_PASSWORD = {R.attr.uikit_state_showPassword};
    final int DRAWABLE_RIGHT = 2;
    int basecolor;
    private boolean isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    Context context;

    public UikitPasswordEditText(final Context cont, AttributeSet attrs) {
        super(cont, attrs);
        context=cont;
        setCompoundDrawablesWithIntrinsicBounds(null, null, getIcon(), null);
        setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.uikit_tab_badge_margin_top));
        setEnabled(true);
        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        basecolor = a.getInt(0, R.attr.uikit_baseColor);
        a.recycle();
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setSelection(getText().length());

        if ((getTransformationMethod()) instanceof PasswordTransformationMethod)
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        else setTransformationMethod(PasswordTransformationMethod.getInstance());
        addTextChangedListener(this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (getRight() - getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        int index = getSelectionEnd();
                        if ((getTransformationMethod()) instanceof PasswordTransformationMethod)
                            setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        else setTransformationMethod(PasswordTransformationMethod.getInstance());

                        cancelLongPress();
                        setSelection(index);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void setOnEditorActionListener(OnEditorActionListener l) {
        super.setOnEditorActionListener(l);
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
        int[] colors = { ContextCompat.getColor(context, R.color.uikit_enricher4), ContextCompat.getColor(context,R.color.uikit_password_icon_color), ContextCompat.getColor(context,R.color.uikit_philips_bright_blue)};

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

        if(s.length() == 0){
            getCompoundDrawables()[DRAWABLE_RIGHT].setState(STATE_EMPTY_PASSWORD);
            if ((getTransformationMethod()) instanceof PasswordTransformationMethod)
            {
                //do nothing
            }
            else setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else if((getTransformationMethod()) instanceof PasswordTransformationMethod) {
            getCompoundDrawables()[DRAWABLE_RIGHT].setState(STATE_MASKED_PASSWORD);
        } else {
            getCompoundDrawables()[DRAWABLE_RIGHT].setState(STATE_SHOW_PASSWORD);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

    }
}

