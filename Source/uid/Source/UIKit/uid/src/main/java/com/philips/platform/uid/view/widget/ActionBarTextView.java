package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

/**
 * Text view suitable for keeping the text center relative to its parent size.
 * Suitable for using for title in actionbar or toolbar.
 */
public class ActionBarTextView extends AppCompatTextView {
    private boolean forceGravity;

    public ActionBarTextView(Context context) {
        super(context);
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
    }

    public ActionBarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
    }

    public ActionBarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
    }

    /**
     * Sets the gravity for the text and doesn't apply any computations to make it centric
     * @param gravity text gravity
     *                @since 3.0.0
     */
    public void setForcedGravity(int gravity) {
        setGravity(gravity);
        this.forceGravity = true;
    }

    /**
     * Removes any gravity if set via {@link #setForcedGravity(int)}.
     * It tries to put the text in center relative to its parent.
     * @since 3.0.0
     */
    @SuppressWarnings("unused")
    public void removeForcedGravity() {
        setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        this.forceGravity = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float translateX = getTranslateX();
        if (translateX > 0) {
            canvas.save();
            canvas.translate(translateX, 0);
        }

        super.onDraw(canvas);

        if (translateX > 0) {
            canvas.restore();
        }
    }

    private float getTranslateX() {
        if (forceGravity) {
            return 0;
        }

        float translateX = 0;
        if (getText() == null) {
            return 0;
        }

        float textPaintLength = getPaint().measureText(getText(), 0, length());
        int textViewWidth = getWidth();
        if (textPaintLength >= textViewWidth) {
            translateX = 0;
        } else if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            int width = parent.getWidth();
            float startDistance = getX();
            float endDistance = width - (startDistance + textViewWidth);
            float leftAdjustment = endDistance - startDistance;
            if (textPaintLength - (leftAdjustment + textViewWidth) < 0) {
                translateX = leftAdjustment + ((textViewWidth - leftAdjustment) - textPaintLength) / 2;
                //Reduce translation to fit the text window
                if (translateX + textPaintLength > textViewWidth) {
                    translateX = textViewWidth - textPaintLength;
                }
            }
        }
        return translateX;
    }
}
