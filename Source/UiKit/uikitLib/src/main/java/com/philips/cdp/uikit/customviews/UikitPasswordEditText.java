
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
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

    int index;
    Context context;
    public UikitPasswordEditText(final Context cont, AttributeSet attrs) {
        super(cont, attrs);
        context=cont;
         setCompoundDrawablesWithIntrinsicBounds(null, null, getIcon(), null);
         setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.uikit_tab_badge_margin_top));
         setEnabled(true);
         setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        if (( getTransformationMethod()) instanceof PasswordTransformationMethod)
             setTransformationMethod(null);
        else  setTransformationMethod(new PasswordTransformationMethod());

         setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= ( getRight() -  getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        int index =  getSelectionEnd();
                        if (( getTransformationMethod()) instanceof PasswordTransformationMethod)
                             setTransformationMethod(null);

                        else  setTransformationMethod(new PasswordTransformationMethod());

                        cancelLongPress();
                         setSelection(index);
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
        if (( getTransformationMethod()) instanceof PasswordTransformationMethod)
             setTransformationMethod(null);

        else  setTransformationMethod(new PasswordTransformationMethod());

    }
    @Override
    public boolean isLongClickable() {
        return false;
    }
}

