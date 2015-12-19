package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 12/15/2015.
 */
public class UikitCustomSpinner extends ProgressBar {
    Context mContext;

    public UikitCustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UikitCustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        TypedArray ar = mContext.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor });
        int baseColor = ar.getInt(0, R.attr.brightColor);
        LayerDrawable d = (LayerDrawable) getResources().getDrawable(R.drawable.uikit_spike_progress);
//        setIndeterminate(false);
        //setProgressDrawableTiled(d);
        setProgressDrawable(d);
     //   getProgressDrawable().setBounds(100,200,100,300);


//        //setIndeterminateDrawableTiled(d);//DrawableTiled(d);
     // setIndeterminateDrawable(d);
        getProgressDrawable().setColorFilter(baseColor, PorterDuff.Mode.MULTIPLY);

        this.setRotation((-getProgress() / 100f * 360f) - 90f);




    }

    public UikitCustomSpinner(Context context) {
        super(context);
    }
}
