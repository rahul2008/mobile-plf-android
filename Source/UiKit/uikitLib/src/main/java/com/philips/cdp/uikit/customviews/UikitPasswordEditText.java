
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;


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
        setCompoundDrawables(null,null,getIcon(context),null);
       /* GradientDrawable bgShape = (GradientDrawable)this.getBackground();
        csl=new ColorStateList(
                new int [] [] {

                        new int [] {android.R.attr.state_focused},
                        new int [] {}
                },
                new int [] {
                        basecolor,
                        Color.GRAY
                });*/


        //setError("Incorrect ", getResources().getDrawable(R.drawable.uikit_action_icon_plus));

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
                        //getCompoundDrawables()[2].setTintList(csl);
                        //getCompoundDrawables()[2].setTint(Color.GRAY);

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
        private Drawable getIcon(Context context) {
            Resources r = getResources();
            float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33, r.getDisplayMetrics());
            float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,32, r
                    .getDisplayMetrics());
            Drawable d = VectorDrawable.create(context, R.drawable.uikit_password_show_icon).mutate();
            d.setBounds(0, 0,100, 70);
            return d;
        }

    @Override
    public boolean isLongClickable() {
        return false;
    }
}

