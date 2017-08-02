package com.philips.platform.ths.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.philips.platform.ths.R;

@SuppressLint({"NewApi", "DrawAllocation"})
public class CircularImageView extends android.support.v7.widget.AppCompatImageView {

    private int mBorderWidth;
    private int mCanvasSize;
    private Bitmap mBitmap;
    private Paint mPaint;
    private Paint mPaintBorder;

    public CircularImageView(final Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.circularImageViewStyle);
    }

    @SuppressLint("Recycle")
    @SuppressWarnings("static-access")
    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // init paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Style.STROKE.STROKE);

    }

    public void setBorderWidth(int borderWidth) {
        mPaintBorder.setStrokeWidth(borderWidth);
        this.mBorderWidth = borderWidth;
        this.requestLayout();
        this.invalidate();

    }

    public void setBorderColor(int borderColor) {
        if (mPaintBorder != null)
            mPaintBorder.setColor(borderColor);
        this.invalidate();

    }

    public void addShadow() {
        setLayerType(LAYER_TYPE_SOFTWARE, mPaintBorder);
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        mBitmap = drawableToBitmap(getDrawable());
        if (mBitmap != null) {
            mCanvasSize = canvas.getWidth();
            if (canvas.getHeight() < mCanvasSize)
                mCanvasSize = canvas.getHeight();
            BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(
                    mBitmap, mCanvasSize, mCanvasSize, false),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);

            int circleCenter = (mCanvasSize - (mBorderWidth * 2)) / 2;
            canvas.drawCircle(circleCenter + mBorderWidth, circleCenter
                    + mBorderWidth, ((mCanvasSize - (mBorderWidth * 2)) / 2)
                    + mBorderWidth - 4.0f, mPaintBorder);
            canvas.drawCircle(circleCenter + mBorderWidth, circleCenter
                            + mBorderWidth,
                    ((mCanvasSize - (mBorderWidth * 2)) / 2) - 4.0f, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mWidth = measureWidth(widthMeasureSpec);
        int mHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // The parent has not imposed any constraint on the child.
            result = mCanvasSize;
        }
        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = mCanvasSize;
        }
        return (result + 2);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}