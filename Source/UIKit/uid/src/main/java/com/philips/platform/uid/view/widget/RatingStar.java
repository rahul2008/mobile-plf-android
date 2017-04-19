package com.philips.platform.uid.view.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;

import com.philips.platform.uid.R;

import java.lang.reflect.Method;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class RatingStar extends AppCompatRatingBar {

    private int widthOffset;
    private Paint paint;
    private String label;
    private int height;
    private int width;

    public RatingStar(Context context) {
        super(context);
    }

    public RatingStar(Context context, AttributeSet attrs) {
        super(context, attrs);//, R.attr.ratingBarStyle);
    }

    public RatingStar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackground(new ColorDrawable(Color.YELLOW));
        if (isIndicator()) {
            width = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_display_width));
            height = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_display_height));
        } else {
            width = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_input_width));
            height = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_input_height));
        }
        setProgressDrawableCustom();
        widthOffset = getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_text) + getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_display_padding);
        setPadding(widthOffset,0,0,0);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(ViewCompat.getMeasuredWidthAndState(this),48);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initializePaint();
        label = String.valueOf(getRating());            //Change to new String attribute
        Rect bounds = new Rect();
        paint.getTextBounds(label, 0, label.length(), bounds);
        paint.setTextAlign(Paint.Align.CENTER);
//        int yPos = (int) ((canvas.getHeight() / 2) + ((paint.descent() + paint.ascent()) / 2)) ;
//        canvas.drawText(label,0,yPos,paint);
        drawTextCentred(canvas, paint, label,canvas.getWidth(),canvas.getHeight()/2);
    }


    public void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy){
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, 0, cy- textBounds.exactCenterY(), paint);
    }

    private void initializePaint(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTypeface(TypefaceUtils.load(getContext().getAssets(),"fonts/centralesansbook.ttf"));
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_text));
    }

    public void setProgressDrawableCustom() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setProgressDrawableTiled(getStarDrawable());
        } else {
            setProgressDrawable(tileify(this, getStarDrawable()));
        }
    }

    private Drawable getStarDrawable() {
        Drawable[] d = new Drawable[3];

        if (isIndicator()) {
            d[0] = getBitmapFromVector( R.drawable.uid_ratingbar_display_off);
            d[1] = d[0];
            d[2] = getBitmapFromVector( R.drawable.uid_ratingbar_display_on);
        } else {
            d[0] = getBitmapFromVector( R.drawable.uid_ratingbar_input_off);
            d[1] = d[0];
            d[2] = getBitmapFromVector( R.drawable.uid_ratingbar_input_on);
        }

        LayerDrawable star = new LayerDrawable(d);
        star.setId(0, android.R.id.background);
        star.setId(1, android.R.id.secondaryProgress);
        star.setId(2, android.R.id.progress);
        return star;
    }

    private BitmapDrawable getBitmapFromVector(int resID) {
        return new BitmapDrawable(getResources(),
                drawableToBitmap(VectorDrawableCompat.create(getContext().getResources(), resID, getContext().getTheme())));
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

    private Drawable tileify(android.widget.ProgressBar bar, Drawable d) {
        try {
            Method tileify = android.widget.ProgressBar.class.getDeclaredMethod("tileify", Drawable.class, Boolean.TYPE);
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
