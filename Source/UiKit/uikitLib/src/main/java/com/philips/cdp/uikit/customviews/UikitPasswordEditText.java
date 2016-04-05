package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 3/18/2016.
 */
public class UikitPasswordEditText extends AppCompatEditText {
    int basecolor ,errorTextColor;
    Drawable errorIcon, errorBackground;

    ColorStateList csl;
    int index;
    public UikitPasswordEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        basecolor=a.getInt(0, R.attr.uikit_baseColor);
        setPadding(10, 10, 10, 10);
        a.recycle();

        a = context.obtainStyledAttributes(attrs, R.styleable.InputTextField);
//        int editTextWidth = a.getDimensionPixelSize(R.styleable.InputTextField_inputFieldWidth, LayoutParams.WRAP_CONTENT);
//        int editTextHeight = a.getDimensionPixelSize(R.styleable.InputTextField_inputFieldHeight, LayoutParams.WRAP_CONTENT);
        errorTextColor = a.getColor(R.styleable.InputTextField_uikit_errorTextColor, getResources().getColor(R.color.uikit_philips_bright_orange));
        errorIcon = a.getDrawable(R.styleable.InputTextField_uikit_errorIcon);
        errorBackground = a.getDrawable(R.styleable.InputTextField_uikit_errorBackground);
        a.recycle();
        setBackgroundResource((R.drawable.uikit_password_background));
        GradientDrawable bgShape = (GradientDrawable)this.getBackground();
        csl=new ColorStateList(
                new int [] [] {

                        new int [] {android.R.attr.state_focused},
                        new int [] {}
                },
                new int [] {
                        basecolor,
                        Color.GRAY
                });

        bgShape.setStroke(3, csl);
        setError("Incorrect ", getResources().getDrawable(R.drawable.uikit_action_icon_plus));

       // setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (getRight() - getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        index=getSelectionEnd();
                        getCompoundDrawables()[2].setTintList(csl);

                        if ((getTransformationMethod()) instanceof PasswordTransformationMethod)

                            setTransformationMethod(null);

                        else setTransformationMethod(new PasswordTransformationMethod());

                        // Toast.makeText(context, " clicked ", Toast.LENGTH_LONG).show();
                        cancelLongPress();
                        setSelection(index);
                        return false;
                    }
                }
                return false;
            }
        });

    }
//    @Override
//    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
//        super.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.bottom_right, 0);
//    }
//
//    @Override
//    public void setCompoundDrawables(Drawable left, Drawable top,
//                                     Drawable right, Drawable bottom) {
//
//        super.setCompoundDrawables(null, null, getResources().getDrawable(R.drawable.uikit_apple), null);
//    }


    @Override
    public boolean isLongClickable() {
        return false;
    }
}
