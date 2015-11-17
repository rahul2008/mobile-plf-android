package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import java.lang.reflect.Field;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class VectorDrawableImageView extends ImageView {

    public VectorDrawableImageView(final Context context) {
        super(context);
    }

    public VectorDrawableImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        getVectorDrawable(context, attrs);
    }

    public VectorDrawableImageView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getVectorDrawable(context, attrs);
    }

    private void getVectorDrawable(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.VectorDrawableImageView, 0, 0);
        String resPath = a.getString(R.styleable.VectorDrawableImageView_vectorsrc);
        a.recycle();
        int id = getResourceID(resPath.substring(resPath.lastIndexOf("/") + 1, resPath.lastIndexOf(".")), R.drawable.class);
        Drawable v = VectorDrawable.create(context, id);
        setImageDrawable(v);
    }

    public static int getResourceID(String resName, Class<?> resourceClass) {
        Field target = null;
        int resourceID = -1;
        try {
            target = resourceClass.getField(resName);
            try {
                resourceID = target.getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceID;
    }
}
