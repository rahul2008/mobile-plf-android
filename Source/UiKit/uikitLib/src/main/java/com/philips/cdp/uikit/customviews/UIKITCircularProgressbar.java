package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 12/15/2015.
 */
public class UIKITCircularProgressbar extends ProgressBar {
    Context mContext;
    boolean grayProgress;
    boolean whiteProgress;
    boolean smallProgress;
    int baseColor;
    public UIKITCircularProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UIKITCircularProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray ar = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor});
        baseColor = ar.getInt(0, R.attr.brightColor);
        ar.recycle();
        final TypedArray a =  mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.UIKitProgressBarCircular, 0, 0);
        grayProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikitgrayprogress, false);
        whiteProgress = a.getBoolean(R.styleable.UIKitProgressBarCircular_uikittransparentprogress, false);
        smallProgress=a.getBoolean(R.styleable.UIKitProgressBarCircular_uikitcircularprogresssmall,false);
        a.recycle();
        setProgressDrawable(cicularProgress());
        setRotation((-getProgress() / 100f * 360f) - 90f);


    }

    private LayerDrawable cicularProgress() {
        LayerDrawable circularbar;
        if(smallProgress){
            circularbar = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.uikit_circular_progress_small);
        }
        else {
            circularbar = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.uikit_circular_progress);
        }
        circularbar.getConstantState().newDrawable().mutate();
        Drawable progressbar = (Drawable) circularbar.findDrawableByLayerId(android.R.id.progress);
        Drawable background = (Drawable) circularbar.findDrawableByLayerId(android.R.id.background);
        ColorFilter BaseColorProgressFilter = new PorterDuffColorFilter(baseColor, PorterDuff.Mode.SRC_ATOP);
        ColorFilter White = new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.uikit_white) , PorterDuff.Mode.SRC_ATOP);
        ColorFilter Enricher6 = new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.uikit_enricher6) , PorterDuff.Mode.SRC_ATOP);
        if(whiteProgress) {
            progressbar.setColorFilter(BaseColorProgressFilter);
            background.setColorFilter(White);
        }else {

            progressbar.setColorFilter(BaseColorProgressFilter);
            background.setColorFilter(Enricher6);
        }
        return circularbar;
    }

    public UIKITCircularProgressbar(Context context) {
        super(context);
    }
}
