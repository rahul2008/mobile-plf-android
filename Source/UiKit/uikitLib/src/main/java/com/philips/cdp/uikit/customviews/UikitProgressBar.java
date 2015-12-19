package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 12/16/2015.
 */
public class UikitProgressBar extends ProgressBar {
    Context mContext;
    public UikitProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        TypedArray ar = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor });
        int baseColor = ar.getInt(0, R.attr.brightColor);
        RotateDrawable d = (RotateDrawable) getResources().getDrawable(R.drawable.uikit_progress_style);
       /* setIndeterminate(false);
      //setProgressDrawableTiled(d);
    //    setProgressDrawable(d);


        setIndeterminate(false);
        //setIndeterminateDrawableTiled(d);//DrawableTiled(d);
        setIndeterminateDrawable(d);*/
        setIndeterminate(true);
        setRotation(0.1f);
        getIndeterminateDrawable().setColorFilter(baseColor, PorterDuff.Mode.SRC_ATOP);
    }

    public UikitProgressBar(Context context) {

        super(context);
        mContext=context;
    }

    public UikitProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        mContext=context;
    }

   /* @Override
    protected void onDraw(Canvas canvas) {
        int centerXOnView = getWidth() / 2;
        int centerYOnView = getHeight() / 2;

        int viewXCenterOnScreen = getLeft() + centerXOnView;
        int viewYCenterOnScreen = getTop() + centerYOnView;

        float threeDpPad = 3f;
        float rad = 70f;

        int leftOffset = (int) (viewXCenterOnScreen - (rad + (threeDpPad * 4)));
        int topOffset = (int) (viewYCenterOnScreen - (rad + (threeDpPad * 3)));
        int rightOffset = (int) (viewXCenterOnScreen + (rad + (threeDpPad * 4)));
        int bottomOffset = (int) (viewYCenterOnScreen + (rad + threeDpPad));

        RectF oval = new RectF(leftOffset, topOffset, rightOffset, bottomOffset);

       *//* int textLength = getText().length();
        if ((textLength % 2) != 0) {
            textLength = textLength + 1;
        }
        this.myArc.addArc(oval, -90 - (textLength * 2), 90 + textLength + 10);

        canvas.drawTextOnPath((String) getText(), this.myArc, 0, 10, this.mPaintText);*//*
        canvas.dr
        invalidate();
    }
*/


}
