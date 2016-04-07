
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.Resources;
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

    int index;
    public UikitPasswordEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);

        setCompoundDrawables(null,null,getIcon(context),null);
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

