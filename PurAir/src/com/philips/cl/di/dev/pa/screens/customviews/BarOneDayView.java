package com.philips.cl.di.dev.pa.screens.customviews;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class BarOneDayView.
 */
public class BarOneDayView extends ImageView {

	/** The color. */
	int color;

	/** The offset for drawing a smooth graph. */
	private float offset = .20f;

	/** The list of paths. */
	private List<ColoredPath> paths;

	/**
	 * Instantiates a new bar one day view.
	 * 
	 * @param context
	 *            the context
	 */
	public BarOneDayView(Context context) {
		super(context);
		init();
	}

	/**
	 * Instantiates a new bar one day view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public BarOneDayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		if (paths != null && paths.size() > 0) {
			for (ColoredPath coloredPath : paths) {
				canvas.drawPath(coloredPath.getPath(), coloredPath.getPaint());
			}
		}

	}

	private void init() {
		if (!isInEditMode()) {
			setLayerType(View.LAYER_TYPE_HARDWARE, null);
		}
	}

	/**
	 * Draw graph. For drawing the graph for a day the aqi array must contain 24
	 * values . if for a particular hour , the aqi is not available , replace it
	 * with 0 and then call this method.
	 * 
	 * @param arrayAQIValues
	 *            the array aqi values
	 * @param width 
	 */
	public void drawGraph(int[] arrayAQIValues, int width) {
		paths = new ArrayList<ColoredPath>();
		float drawingLengthFactor = /*AppConstants.DAYWIDTH*/ width
				/ arrayAQIValues.length;
		int index = 0;
		for (int iAqi : arrayAQIValues) {
			color = Utils.getAQIColor(iAqi);
			paths.add(new ColoredPath(getPaint(color), getPath(index,
					drawingLengthFactor)));
			index++;
		}
		requestLayout();
		invalidate();
	}

	/**
	 * Gets the paint.
	 * 
	 * @param color
	 *            the color
	 * @return the paint
	 */
	private Paint getPaint(int color) {
		Paint paint = new Paint();
		paint.setStrokeWidth(400f/* height */);
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		return paint;
	}

	/**
	 * Gets the path.
	 * 
	 * @param index
	 *            the index
	 * @param drawingLengthFactor
	 *            the drawing length factor
	 * @return the path
	 */
	private Path getPath(int index, float drawingLengthFactor) {
		Path path = new Path();
		Log.i("START X : ", index * drawingLengthFactor + "");
		path.moveTo((index * drawingLengthFactor) - offset, 0);
		path.lineTo((index + 1) * drawingLengthFactor, 0);
		return path;
	}

}

/**
 * The Class ColoredPath. This class provides objects for different aqi values
 * with their colors.
 */

class ColoredPath {

	private Paint paint;
	private Path path;

	public ColoredPath(Paint paint, Path path) {
		this.paint = paint;
		this.path = path;
	}

	public Paint getPaint() {
		return paint;
	}

	public Path getPath() {
		return path;
	}
}
