/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.util.List;

public class UIDTestUtils {
    public final static int UI_LOAD_WAIT_TIME = 750;
    public final static int UI_LOAD_WAIT_TIME_EXTRA = 1000;

    public static int getAttributeColor(Context context, int attribute) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{attribute});
        int color = Color.MAGENTA;
        if (typedArray != null) {
            color = typedArray.getColor(0, Color.WHITE);
            typedArray.recycle();
        }
        return color;
    }

    public static float getAttributeAlpha(Context context, int attribute) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{attribute});
        float alpha = Color.MAGENTA;
        if (typedArray != null) {
            alpha = typedArray.getFloat(0, 0f);
            typedArray.recycle();
        }
        return alpha;
    }

    @SuppressWarnings("ResourceType")
    public static int getColorWithAlphaFromAttrs(Context context, int colorAttr, int alphaAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{colorAttr, alphaAttr});
        if (typedArray != null) {
            int color = typedArray.getColor(0, Color.WHITE);
            float alpha = typedArray.getFloat(1, 0f);
            typedArray.recycle();
            return modulateColorAlpha(color, alpha);
        }
        throw new RuntimeException("the typed array doesn't contain color and alpha attr");
    }

    public static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(Color.alpha(color) * alphaMod));
    }

    public static Drawable getDrawableWithReflection(Object object, String funcName) {
        Drawable drawable = null;
        try {
            drawable = (Drawable) MethodUtils.invokeMethod(object, funcName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return drawable;
    }

    public static ColorStateList getColorStateListWithReflection(Object object, String funcName) {
        ColorStateList colorList;
        try {
            colorList = (ColorStateList) MethodUtils.invokeMethod(object, funcName);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return colorList;
    }

    public static int[] getColorsWithReflection(Object object) {
        int[] colorList;
        try {
            colorList = (int[]) MethodUtils.invokeMethod(object, "getColors");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return colorList;
    }

    public static int getMaxRippleRadius(RippleDrawable ripple) {
        int radius = 0;
        try {
            radius = (int) MethodUtils.invokeMethod(ripple, "getRadius");
        } catch (Exception e) {
            try {
                radius = (int) MethodUtils.invokeMethod(ripple, "getMaxRadius");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return radius;
    }

    public static ColorStateList getRippleColor(Drawable.ConstantState cs) {
        try {
            Field colorField = cs.getClass().getDeclaredField("mColor");
            colorField.setAccessible(true);
            return (ColorStateList) colorField.get(cs);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Avoid using until really necessary to call it.
     * We can't avoid waiting in few situations, where we need actual view must be drawn before we start asserting.
     *
     * @param object
     * @param milliSecs
     */
    public static void waitFor(Object object, int milliSecs) {
        Thread thread = Thread.currentThread();
        synchronized (object) {
            try {
                object.wait(milliSecs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Drawable extractClipDrawable(Drawable drawable) {
        if (drawable instanceof ClipDrawable) {
            if (Build.VERSION.SDK_INT >= 23) {
                return ((ClipDrawable) drawable).getDrawable();
            }
        }
        return drawable;
    }

    public static Drawable extractGradientFromRotateDrawable(Drawable drawable) {
        Drawable d = DrawableCompat.unwrap(drawable);
        if (d instanceof RotateDrawable) {
            return ((RotateDrawable) d).getDrawable();
        }
        return drawable;
    }

    //We need this for wrapperDrawables, specially for Lollipop
    public static Drawable getWrappedClipDrawableFromReflection(Drawable drawable) {
        if (!(DrawableCompat.unwrap(drawable) instanceof ClipDrawable))
            return drawable;
        try {
            Drawable.ConstantState constantState = DrawableCompat.unwrap(drawable).getConstantState();
            return (Drawable) FieldUtils.readDeclaredField(constantState, "mDrawable", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static float getFloatFieldValueFromReflection(Object object, String field) {
        try {
            return (float) FieldUtils.readField(object, field, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIntFieldValueFromReflection(Object object, String field) {
        try {
            return (int) FieldUtils.readField(object, field, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int[] getIntegerArrayFromReflection(Object object, String field) {
        try {
            return (int[]) FieldUtils.readField(object, field, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new int[]{0};
    }

    public static float getFloatValueFromDimen(final Context activity, final int dimenResource) {
        TypedValue typedValue = new TypedValue();
        activity.getResources().getValue(dimenResource, typedValue, true);
        return typedValue.getFloat();
    }

    public static List<RecyclerView.ItemDecoration> getItemsDecoration(final RecyclerView recyclerView) {
        try {
            final List<RecyclerView.ItemDecoration> mItemDecorations = (List<RecyclerView.ItemDecoration>) FieldUtils.readField(recyclerView, "mItemDecorations", true);
            return mItemDecorations;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}