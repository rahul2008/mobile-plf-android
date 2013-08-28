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

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.utils.Utils;

public class BarOneDayView extends ImageView {

	float length;

	int color;

	private float offset = .20f;

	private List<ColoredPath> paths;

	public BarOneDayView(Context context) {
		super(context);
		init();
	}

	public BarOneDayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

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
	 * Draw graph.
	 * For drawing the graph for a day the aqi array must contain 24 values .
	 * if for a particular hour , the aqi is not available , replace it with 0 and then call this method.
	 *
	 * @param arrayAQIValues the array aqi values
	 */
	public void drawGraph(int[] arrayAQIValues) {
		paths = new ArrayList<ColoredPath>();
		float drawingLengthFactor = AppConstants.DAYWIDTH
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

	private Paint getPaint(int color) {
		Paint paint = new Paint();
		paint.setStrokeWidth(400f);
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		return paint;
	}

	private Path getPath(int index, float drawingLengthFactor) {
		Path path = new Path();
		Log.i("START X : ", index * drawingLengthFactor + "");
		path.moveTo((index * drawingLengthFactor) - offset, 0);
		path.lineTo((index + 1) * drawingLengthFactor, 0);
		return path;
	}

}

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
