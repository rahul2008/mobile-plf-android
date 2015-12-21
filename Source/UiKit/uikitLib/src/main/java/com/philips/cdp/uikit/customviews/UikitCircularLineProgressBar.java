package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * Created by 310213373 on 12/15/2015.
 */
public class UikitCircularLineProgressBar extends ProgressBar {
    Context mContext;
    boolean grayProgress;
    boolean whiteProgress;
    boolean smallProgress;
    int baseColor;
    public UikitCircularLineProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UikitCircularLineProgressBar(Context context, AttributeSet attrs) {
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
        ColorFilter BaseColorProgressFilter = new PorterDuffColorFilter(baseColor, PorterDuff.Mode.SRC_ATOP);
        AnimationDrawable ad=(AnimationDrawable)ContextCompat.getDrawable(mContext, R.drawable.uikit_circular_line_progress);

        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner1)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner8)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner7)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner6)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner5)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner4)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner3)),500);
        ad.addFrame((VectorDrawable.create(mContext, R.drawable.progressbar_spinner2)),500);
       // animationDrawable.addFrame();
        setIndeterminateDrawable(ad);
      //  setRotation((-getProgress() / 100f * 360f) - 90f);
        ad.start();


    }

    /*private LayerDrawable cicularProgress() {
        LayerDrawable circularbar;
        if(smallProgress){
            circularbar = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.uikit_circular_line_progress);
        }
        else {
            circularbar = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.uikit_circular_line_progress);
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
    }*/

    public UikitCircularLineProgressBar(Context context) {
        super(context);
    }
}
