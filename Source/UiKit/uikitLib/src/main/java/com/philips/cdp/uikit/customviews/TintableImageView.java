package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.philips.cdp.uikit.R;

/**
 * Created by 310213373 on 12/2/2015.
 */
public class TintableImageView extends ImageView {
    private ColorStateList tint;
    private boolean useInvertedTheme = true;
    Context mContext;
    int baseColor;

    public TintableImageView(Context context) {
        super(context);
        mContext = context;
        initBaseColor();
    }

    //this is the constructor that causes the exception
    public TintableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBaseColor();
        init(context, attrs, 0);
    }

    public TintableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
        mContext = context;
    }

    //here, obtainStyledAttributes was asking for an array
    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{R.styleable.TintableImageView_tint}, defStyle, 0);
        tint = a.getColorStateList(R.styleable.TintableImageView_tint);
        a.recycle();
        setColorFilter(baseColor);
        //  setColorFilter(tint);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (useInvertedTheme) {
            int[] state = getDrawableState();
            boolean isSelected = false;
            for (int i : state) {
                if (i == android.R.attr.state_pressed) {
                    isSelected = true;
                    break;
                }
            }

            if (isSelected) {
                clearColorFilter();
            } else {
                setColorFilter(baseColor);
            }
        }
    }

    private void initBaseColor() {
        TypedArray array = mContext.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        baseColor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        array.recycle();
    }

    public void setUseInvertedTheme(boolean useInvertedTheme) {
        this.useInvertedTheme = useInvertedTheme;
    }

}
