/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uid.utils;

import android.content.Context;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.view.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public final class UIDUtils {

    private UIDUtils() {
    }

    public static boolean isMinLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    //Prior to 6.0.1 version, radius is automatically decided as per view bounds.
    //Call hidden api to set radius
    public static void setRippleMaxRadius(Drawable drawable, int radius) {
        try {
            Method setMaxRadius = drawable.getClass().getDeclaredMethod("setMaxRadius", Integer.TYPE);
            setMaxRadius.setAccessible(true);
            setMaxRadius.invoke(drawable, radius);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void animateAlpha(final View view, float toAlpha, int duration, final Runnable endAction) {
        ViewPropertyAnimator animator = view.animate().alpha(toAlpha).setDuration(duration);
        if (endAction != null) {
            animator.setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    endAction.run();
                }
            });
        }
        animator.start();
    }

    public static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(Color.alpha(color) * alphaMod));
    }

    public static Drawable setTintOnDrawable(Drawable drawable, int tintId, Resources.Theme theme, Context context) {
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(context.getResources(), theme, tintId);
        Drawable compatDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(compatDrawable, colorStateList);
        return compatDrawable;
    }

    public static void setTextFromResourceID(Context context, View view, AttributeSet attrs) {
        if (view instanceof TextView) {
            TypedArray textArray = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.text, android.R.attr.hint});
            int resourceId = textArray.getResourceId(0, -1);
            if (resourceId != -1) {
                ((TextView) view).setText(resourceId);
            }
            resourceId = textArray.getResourceId(1, -1);
            if (resourceId != -1) {
                ((EditText) view).setHint(resourceId);
            }
        }
    }
}