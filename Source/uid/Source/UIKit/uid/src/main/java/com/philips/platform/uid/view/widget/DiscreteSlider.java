/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.CanvasCompat;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * UID Discrete Slider.
 * <p>
 * <P>UID Discrete Slider is an extension of UID Slider with added styles.
 * <p>
 * <P> Usage of Discrete Slider is exactly as the AppCompatSeekBar. To create a slider with uniform discrete points use the attribute android:max.
 * <P> To create a slider with non-uniform discrete points use the attribute discretePoints to pass the integer-array of non-uniform points in the range 0 - 100.
 * <BR>
 * <P> For example, to create a slider with 5 uniform discrete points. Use the attribute android:max="5"
 * <BR>
 * <P> Similarly, to create a slider with 5 non-uniform discrete points. Use the attribute discretePoints="@array/discretePoints". The integer-array discretePoints should have 5 values.
 * <BR>
 * <p>
 * <BR>
 */
public class DiscreteSlider extends Slider implements SeekBar.OnSeekBarChangeListener {

    private int[] discreteValues;
    private Drawable discretePointDrawable;
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener;
    private boolean progressUpdated;

    private Rect tickRect = new Rect();
    private Rect tickDrawableBounds = new Rect();
    private Rect thumbBounds = new Rect();

    //Add 2px for float calculation rounding error, failing which draws a bar between transparent dots.
    private static final int DISCRETE_FLOAT_ROUNDING_ERROR = 2;

    public DiscreteSlider(Context context) {
        super(context);
    }

    public DiscreteSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnSeekBarChangeListener(this);
        initializeAttr(context, attrs);
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        seekBarChangeListener = l;
    }

    private void initializeAttr(Context context, AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, new int[]{R.styleable.UIDDiscreteSlider_discretePoints});
        final int id = array.getResourceId(0, 0);

        if (id != 0) {
            discreteValues = getResources().getIntArray(id);
        }
        discretePointDrawable = getResources().getDrawable(R.drawable.uid_slider_tick_mark, getContext().getTheme());
        setTickMarkBounds();
        array.recycle();
    }

    private void setTickMarkBounds() {
        final int w = discretePointDrawable.getIntrinsicWidth();
        final int h = discretePointDrawable.getIntrinsicHeight();
        int discretePointDrawableMidW = w >= 0 ? (int) Math.ceil(w / 2) : 1;
        final int halfH = h >= 0 ? (int) Math.ceil((double) h / 2) : 1;
        discretePointDrawable.setBounds(-discretePointDrawableMidW, -halfH, discretePointDrawableMidW, halfH);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (discretePointDrawable == null || getThumb() == null) {
            super.onDraw(canvas);
            return;
        }

        int viewHeight = getHeight();
        int paddingStart = getPaddingStart();
        int canvasVerticalShift = viewHeight / 2;
        tickRect.top = canvasVerticalShift - discretePointDrawable.getIntrinsicHeight() / 2 - DISCRETE_FLOAT_ROUNDING_ERROR;
        tickRect.bottom = tickRect.top + discretePointDrawable.getIntrinsicHeight() + DISCRETE_FLOAT_ROUNDING_ERROR;

        discretePointDrawable.copyBounds(tickDrawableBounds);
        getThumb().copyBounds(thumbBounds);

        int tickWidth = tickDrawableBounds.right - tickDrawableBounds.left;
        int[] discretePoints = getDiscretePoints();
        int clipSaveCount = canvas.save();
        if (discretePoints != null) {
            for (float discretePoint : discretePoints) {
                tickRect.left = (int) discretePoint + paddingStart;
                tickRect.right = tickRect.left + tickWidth;
                //Don't clip area overlapping with ThumbRect. We could have used contains as well.
                if (!(thumbBounds.left < tickRect.left && thumbBounds.right > tickRect.right)) {
                    CanvasCompat.clipOutRect(canvas, tickRect);
                }
            }
        }

        super.onDraw(canvas);
        canvas.restoreToCount(clipSaveCount);

        if (discretePoints != null) {
            final int saveCount = canvas.save();
            float startX = discretePoints[0] + paddingStart;
            int totalShift = (int) (startX + tickWidth / 2);
            canvas.translate(startX + tickWidth / 2, canvasVerticalShift);
            if (!(thumbBounds.contains(totalShift, canvasVerticalShift))) {
                discretePointDrawable.draw(canvas);
            }
            for (int i = 1; i < discretePoints.length; i++) {
                startX = discretePoints[i] - discretePoints[i - 1];
                totalShift += startX;
                canvas.translate(startX, 0);
                if (!(thumbBounds.contains(totalShift, canvasVerticalShift))) {
                    discretePointDrawable.draw(canvas);
                }
            }
            canvas.restoreToCount(saveCount);
        }
    }

    private int[] getDiscretePoints() {
        int[] discretePoints = null;
        final float availableW = (getWidth() - getPaddingStart() - getPaddingEnd());
        if (getMax() == 100 && discreteValues != null && discreteValues.length > 0) {
            discretePoints = new int[discreteValues.length];
            for (int i = 0; i < discreteValues.length; i++) {
                discretePoints[i] = (int) (availableW * (discreteValues[i]) / 100);
            }
        } else if (getMax() > 1) {
            discretePoints = new int[getMax() - 1];
            int jumpValue = (int) (availableW / (float) getMax());
            for (int i = 1; i <= discretePoints.length; i++) {
                discretePoints[i - 1] = i * jumpValue;
            }
        }

        if (UIDUtils.isLayoutRTL(this) && discretePoints != null) {
            for (int i = 0; i < discretePoints.length; i++) {
                discretePoints[i] = (int) (availableW - discretePoints[i]);
            }
            reverseArrayElements(discretePoints);
        }
        return discretePoints;
    }

    private void reverseArrayElements(int[] discretePoints) {
        int start = 0;
        int end = discretePoints.length - 1;
        int tmp;
        while (end > start) {
            tmp = discretePoints[end];
            discretePoints[end] = discretePoints[start];
            discretePoints[start] = tmp;
            end--;
            start++;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progressUpdated) {
            progressUpdated = false;
        } else {
            if (fromUser) {
                if (discreteValues != null && getMax() == 100) {
                    int updatedProgress = getDiscreteProgress(progress);
                    updateListener(true, updatedProgress);
                    if (updatedProgress != progress) {
                        progressUpdated = true;
                        setProgress(progress);
                    }
                } else {
                    updateListener(true, progress);
                }
            } else {
                updateListener(false, progress);
            }
        }
    }

    private void updateListener(boolean fromUser, int progress) {
        if (seekBarChangeListener != null)
            seekBarChangeListener.onProgressChanged(this, progress, fromUser);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress((discreteValues != null && getMax() == 100) ? getDiscreteProgress(progress) : progress);
    }

    private int getDiscreteProgress(int progress) {
        int delta;
        int updatedProgress;
        if (Math.abs(0 - progress) < Math.abs(100 - progress)) {
            updatedProgress = 0;
            delta = Math.abs(0 - progress);
        } else {
            updatedProgress = 100;
            delta = Math.abs(100 - progress);
        }
        for (int discreteValue : discreteValues) {
            if (Math.abs(discreteValue - progress) < delta) {
                delta = Math.abs(discreteValue - progress);
                updatedProgress = discreteValue;
            }
        }
        return updatedProgress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (seekBarChangeListener != null) {
            seekBarChangeListener.onStartTrackingTouch(seekBar);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBarChangeListener != null) {
            seekBarChangeListener.onStopTrackingTouch(seekBar);
        }
    }

    /**
     * Sets tickMark drawable
     *
     * @param drawable Drawable to be set for tickMark
     *                 @since 3.0.1
     */
    public void setDiscretePointDrawable(@NonNull Drawable drawable) {
        discretePointDrawable = drawable;
        setTickMarkBounds();
        invalidate();
    }

    /**
     * Sets tickMark drawable from the resourceID
     *
     * @param drawable ResId of the Drawable
     *                 @since 3.0.1
     */
    @SuppressWarnings("unused")
    public void setDiscretePointDrawable(@DrawableRes int drawable) {
        discretePointDrawable = getResources().getDrawable(drawable, getContext().getTheme());
        setTickMarkBounds();
        invalidate();
    }

    /**
     * Sets the color for the drawable for discrete points
     *
     * @param color Color to be applied on drawable
     * @param mode  Usually SRC_ATOP
     *              @since 3.0.1
     */
    @SuppressWarnings("unused")
    public void setColorForDiscretePoint(int color, PorterDuff.Mode mode) {
        if (discretePointDrawable != null) {
            discretePointDrawable.setColorFilter(color, mode);
        }
        invalidate();
    }
}
