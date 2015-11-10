package com.philips.cdp.uikit.costumviews;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;

import com.philips.cdp.uikit.drawable.VectorDrawable;

import com.philips.cdp.uikit.R;

public class SocialMediaDrawables {





    public Drawable getSocialDrawable(final Context context, int resId, boolean isInverted) {
        ColorFilter selectedFilter;
        TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int selectedColor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        Drawable[] mDrawable = new Drawable[2];
        mDrawable[1] = VectorDrawable.create(context, resId).getConstantState().newDrawable().mutate();
        if (!isInverted) {
            selectedFilter = new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.DST_ATOP);
            array.recycle();
            mDrawable[0] = context.getResources().getDrawable(R.drawable.uikit_social_basecolor);
            mDrawable[0].setColorFilter(selectedFilter);

        } else {
            selectedFilter = new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
            array.recycle();
            mDrawable[0] = ResourcesCompat.getDrawable(context.getResources(), R.drawable.uikit_social_basecolor, null);
            mDrawable[0].setColorFilter(selectedFilter);
            mDrawable[1] = VectorDrawable.create(context, resId).getConstantState().newDrawable().mutate();
            mDrawable[1].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        }
        LayerDrawable mLayerDrawable = new LayerDrawable(mDrawable);
        return mLayerDrawable;

    }
}

/*
if(!isInverted) {


            TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
            int selectedColor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
            selectedFilter = new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.DST_ATOP);
            array.recycle();
            Drawable mDrawable = VectorDrawable.create(context, resId).getConstantState().newDrawable().mutate();

            Drawable[] d = new Drawable[2];
            d[0] = context.getResources().getDrawable(R.drawable.uikit_social_linkedin);
            d[1] = mDrawable;
            d[0].setColorFilter(selectedFilter);
            LayerDrawable l = new LayerDrawable(d);
            return l;
        }

        else
        {

            TypedArray array = context.obtainStyledAttributes(R.styleable.PhilipsUIKit);
            int selectedColor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
            selectedFilter = new PorterDuffColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
            array.recycle();
            Drawable mDrawable = VectorDrawable.create(context, resId).getConstantState().newDrawable().mutate();

            Drawable[] d = new Drawable[2];
            Drawable shape = ResourcesCompat.getDrawable(context.getResources(), R.drawable.uikit_social_linkedin, null);
            shape.setColorFilter(selectedFilter);
            d[0] = shape;
            d[1] = mDrawable;
            d[1].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            LayerDrawable l = new LayerDrawable(d);
            return l;
        }

 */