package com.philips.platform.uid.view.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.philips.platform.uid.R;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class RatingStar extends AppCompatRatingBar {


    public RatingStar(Context context) {
        super(context);
    }

    public RatingStar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingStar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthOffset = getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_text) + getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_display_padding);
        setMeasuredDimension(getMeasuredWidth()+widthOffset ,getMeasuredHeight());
        setPadding(widthOffset,0,0,0);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        String label = String.valueOf(getRating()); //Change to new String attribute
        paint.setColor(Color.RED);
        paint.setTypeface(TypefaceUtils.load(getContext().getAssets(),"fonts/centralesansbook.ttf"));
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.uid_rating_bar_text));
        Rect bounds = new Rect();
        paint.getTextBounds(label, 0, label.length(), bounds);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
        canvas.drawText(label,0,yPos,paint);
    }
}
