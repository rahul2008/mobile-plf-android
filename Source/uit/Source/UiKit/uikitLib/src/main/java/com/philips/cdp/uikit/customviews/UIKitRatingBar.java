/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.lang.reflect.Method;


/**
 * <b></b> UIKitRatingBar is class to customize inbuilt Android RatingBar component to Philips style </b>
 * <p/>
 * <b></b>Inorder to use Custom RatingBar include the Following lines in the layout File</b><br>
 * <pre>&lt;com.philips.cdp.uikit.customviews.UIKitRatingBar
 *      android:id="@+id/ratingbig"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      android:layout_centerInParent="true"
 *      app:isratingbarbig="true"/&gt;
 *
 * </pre>
 * <b></b>For Big Star give  <pre>app:isratingbarbig="true"</b><br>
 * <b></b>For Small Star give  <pre>app:isratingbarbig="false"</b><br>
 */
public class UIKitRatingBar extends RatingBar {

    private boolean isBigStar;
    private int height;
    private int width;

    public UIKitRatingBar(Context context) {
        super(context);
    }

    public UIKitRatingBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.RatingBarView, 0, 0);
        isBigStar = a.getBoolean(R.styleable.RatingBarView_uikit_isratingbarbig, true);
        a.recycle();

        if (isBigStar) {
            width = (int) (getContext().getResources().getDimension(R.dimen.star_width));
            height = (int) (getContext().getResources().getDimension(R.dimen.star_height));
        } else {
            width = (int) (getContext().getResources().getDimension(R.dimen.star_width_small));
            height = (int) (getContext().getResources().getDimension(R.dimen.star_height_small));
        }

        setProgressDrawableCustom();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setProgressDrawableCustom() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setProgressDrawableTiled(getStarDrawable());
        } else {
            setProgressDrawable(tileify(this, getStarDrawable()));
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(width * getNumStars(), widthMeasureSpec, 0), height);
    }

    private Drawable getStarDrawable() {
        Drawable[] d = new Drawable[3];

        if (isBigStar) {
            d[0] = getBitmapFromVector( R.drawable.uikit_star_outlined);
            d[1] = d[0];
            d[2] = getBitmapFromVector( R.drawable.uikit_star_solid);
        } else {
            d[0] = getBitmapFromVector( R.drawable.uikit_star_outlined_small);
            d[1] = d[0];
            d[2] = getBitmapFromVector( R.drawable.uikit_star_solid_small);
        }

        LayerDrawable star = new LayerDrawable(d);
        star.setId(0, android.R.id.background);
        star.setId(1, android.R.id.secondaryProgress);
        star.setId(2, android.R.id.progress);
        return star;
    }

    private BitmapDrawable getBitmapFromVector(int resID) {
        return new BitmapDrawable(getResources(),
                drawableToBitmap(VectorDrawable.create(getContext(), resID)));
    }

    private Drawable tileify(ProgressBar bar, Drawable d) {
        try {
            Method tileify = ProgressBar.class.getDeclaredMethod("tileify", Drawable.class, Boolean.TYPE);
            tileify.setAccessible(true);
            Object o = tileify.invoke(bar, d, false);
            if (o instanceof Drawable) {
                d = (Drawable) o;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
        return d;
    }
}
