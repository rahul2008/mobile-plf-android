package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import com.philips.platform.uid.R;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * UID RatingBar.
 * <p>
 * <P>UID RatingBar is an extension of AppCompatRatingBar with added styles. UIDLibrary provides 3 styles for RatingBar that can be used.
 * <BR>UIDRatingBarInput
 * <BR>UIDRatingBarStandardDisplay
 * <BR>UIDRatingBarMiniDisplay
 * <p>
 * <P> A RatingBar can be prefixed with a Label by calling setText() or using attribute android:text. IsIndicator should be true in order to prefix the label.
 */

public class RatingBar extends AppCompatRatingBar {

    private static final String FONT_PATH = "fonts/centralesansmedium.ttf";
    private int widthOffset = 0;
    private Paint paint;
    private String text = null;
    private int height;
    private int width;
    private int textColor;
    private ColorDrawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.uidRatingBarStyle);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttributes(context, attrs, defStyleAttr);
        initializePaint();
        if (isIndicator()) {
            width = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_display_width));
            height = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_display_height));
        } else {
            width = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_input_width));
            height = (int) (getContext().getResources().getDimension(R.dimen.uid_rating_bar_input_height));
        }
        setProgressDrawableTiled(getStarDrawable());
    }

    private void processAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDRatingBar, defStyleAttr, R.style.UIDRatingBarStyle);
        textColor = typedArray.getColor(R.styleable.UIDRatingBar_uidRatingBarStarOnColor, -1);
        text = typedArray.getString(R.styleable.UIDRatingBar_android_text);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isIndicator() && text != null) {
            widthOffset = (int) (paint.measureText(text) + getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_display_padding));
        }
        if (isLayoutRTL()) {
            setPadding(0, 0, widthOffset, 0);
        } else {
            setPadding(widthOffset, 0, 0, 0);
        }
        setMeasuredDimension(resolveSizeAndState(width * getNumStars(), widthMeasureSpec, 0) + widthOffset, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isIndicator() && text != null) {
            float cx = isLayoutRTL() ? canvas.getWidth() : 0;
            drawTextCentred(canvas, paint, text, cx, canvas.getHeight() / 2);
        }
    }

    private void drawTextCentred(Canvas canvas, Paint paint, String text, float cx, float cy) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        float x = isLayoutRTL() ? cx - textBounds.centerX() : cx + textBounds.centerX();
        canvas.drawText(text, x, cy - textBounds.exactCenterY(), paint);
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private void initializePaint() {
        paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (textColor != -1) {
            paint.setColor(textColor);
        }
        paint.setTypeface(TypefaceUtils.load(getContext().getAssets(), FONT_PATH));
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_text));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    private Drawable getStarDrawable() {
        Drawable[] d = new Drawable[3];

        if (isIndicator()) {
            d[0] = getBitmapFromVector(R.drawable.uid_ratingbar_display_off);
            d[1] = transparentDrawable;
            d[2] = getBitmapFromVector(R.drawable.uid_ratingbar_display_on);
        } else {
            d[0] = getBitmapFromVector(R.drawable.uid_ratingbar_input_off);
            d[1] = transparentDrawable;
            d[2] = getBitmapFromVector(R.drawable.uid_ratingbar_input_on);
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

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
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

    /**
     * This API will help you to get the paint object that is used to draw the label in Rating Bar.
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * This API will help you to set the exact string value passed to Rating Bar label. Alternatively android:text can be used in xml.
     *
     * @param text String to be set to the Rating Bar label
     */
    public void setText(String text) {
        this.text = text;
        requestLayout();
        invalidate();
    }

    /**
     * This API will help you to set the string value passed to Rating Bar label through a string resource ID. Alternatively android:text can be used in xml.
     *
     * @param resID String resource ID to be set to the Rating Bar label
     */
    public void setText(int resID) {
        text = String.valueOf(getResources().getText(resID));
        requestLayout();
        invalidate();
    }
}
