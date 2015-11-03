package com.philips.cdp.ui.catalog.ratingbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.lang.reflect.Method;

/**
 * Created by 310218660 on 10/30/2015.
 */
public class PhilipsRatingBar extends RatingBar {

    public PhilipsRatingBar(Context context) {
        super(context);
        Log.i("Spoorti", "Inside Consturctor 1");
    }

    public PhilipsRatingBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Log.i("Spoorti", "Inside Consturctor 2");
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

    @Override
    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(tileify(this, getDraw()));
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (this != null) {
            // TODO: Once ProgressBar's TODOs are gone, this can be done more
            // cleanly than mSampleTile
            final int width = (int) (getContext().getResources().getDimension(R.dimen.star_width));
            final int height = (int) (getContext().getResources().getDimension(R.dimen.star_height));
            setMeasuredDimension(resolveSizeAndState(width * getNumStars(), widthMeasureSpec, 0),
                    height);
        }
    }

    public Drawable getDraw() {
        if (getContext() == null)
            Log.i("Spoorti", "context is null");
        Drawable[] d = new Drawable[3];
        d[0] = new BitmapDrawable(drawableToBitmap(VectorDrawable.create(getContext(), R.drawable.uikit_star_outlined)));//getDrawable(R.drawable.ic_star_black_48dp);
        d[1] = new BitmapDrawable(drawableToBitmap(VectorDrawable.create(getContext(), R.drawable.uikit_star_outlined)));//getDrawable(R.drawable.ic_star_half_black_36dp);
        d[2] = new BitmapDrawable(drawableToBitmap(VectorDrawable.create(getContext(), R.drawable.uikit_star_solid)));//getDrawable(R.drawable.ic_star_black_48dp);

        LayerDrawable l = new LayerDrawable(d);
        l.setId(0, android.R.id.background);
        l.setId(1, android.R.id.secondaryProgress);
        l.setId(2, android.R.id.progress);
        return l;
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
