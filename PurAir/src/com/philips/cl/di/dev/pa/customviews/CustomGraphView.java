package com.philips.cl.di.dev.pa.customviews;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.interfaces.GraphViewDataInterface;

/**
 * The Class CustomGraphView.
 */
public class CustomGraphView extends RelativeLayout {

	private float offset = 105f;

	/** The view width. */
	private float viewHeight, viewWidth;

	/** The list graph view data outdoor. */
	private List<GraphViewData> listGraphViewDataIndoor,
			listGraphViewDataOutdoor;

	/** The paint axis. */
	private Paint paintIndoor, paintOutdoor, paintAxis, paintText;

	/**
	 * Instantiates a new custom graph view.
	 * 
	 * @param context
	 *            the context
	 */
	public CustomGraphView(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new custom graph view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public CustomGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initIndoorPaint();
		initOutdoorPaint();
		initAxisPaint();
		initTextPaint();
		// Calculate View height and width
		viewHeight = getHeight();
		viewWidth = getWidth();

		// Draw both inDoor and outDoor graph
		drawAxis(canvas);
		drawIndoor(canvas);
		drawOutdoor(canvas);
		drawLabelsOnXAxis(canvas);

	}

	private void initTextPaint() {
		paintText = new Paint();
		paintText.setColor(Color.GRAY);
		paintText.setStyle(Style.FILL);
		paintText.setTextSize(20);

	}

	/**
	 * Inits the axis paint.
	 */
	private void initAxisPaint() {

		paintAxis = new Paint();
		paintAxis.setColor(Color.GRAY);
		paintAxis.setDither(true);
		paintAxis.setStrokeWidth(1f);
		paintAxis.setAntiAlias(true);
		paintAxis.setStyle(Style.STROKE);
		paintAxis.setStrokeJoin(Paint.Join.ROUND);
		paintAxis.setStrokeCap(Paint.Cap.ROUND);

	}

	/**
	 * Inits the outdoor paint.
	 */
	private void initOutdoorPaint() {

		paintOutdoor = new Paint();
		paintOutdoor.setColor(Color.BLUE);
		paintOutdoor.setDither(true);
		paintOutdoor.setStrokeWidth(8f);
		paintOutdoor.setAntiAlias(true);
		paintOutdoor.setStyle(Style.STROKE);
		paintOutdoor.setStrokeJoin(Paint.Join.ROUND); // set the join to round
														// you want
		paintOutdoor.setStrokeCap(Paint.Cap.ROUND);

	}

	/**
	 * Inits the indoor paint.
	 */
	private void initIndoorPaint() {
		paintIndoor = new Paint();
		paintIndoor.setColor(Color.GRAY);
		paintIndoor.setDither(true);
		paintIndoor.setStrokeWidth(8f);
		paintIndoor.setAntiAlias(true);
		paintIndoor.setStyle(Style.STROKE);
		paintIndoor.setStrokeJoin(Paint.Join.ROUND); // set the join to round
														// you want
		paintIndoor.setStrokeCap(Paint.Cap.ROUND);

	}

	/**
	 * Draw axis. Divides the view in 4 equal parts and draws the axis.
	 * 
	 * @param canvas
	 *            the canvas
	 */
	private void drawAxis(Canvas canvas) {
		canvas.drawLine(0, 0, 0, viewHeight, paintAxis);
		canvas.drawLine(viewWidth / 4, 0, viewWidth / 4, viewHeight, paintAxis);
		canvas.drawLine(viewWidth / 2, 0, viewWidth / 2, viewHeight, paintAxis);
		canvas.drawLine(3 * viewWidth / 4, 0, 3 * viewWidth / 4, viewHeight,
				paintAxis);
	}

	private void drawLabelsOnXAxis(Canvas canvas) {
		canvas.drawText("12am", 0, viewHeight - 10, paintText);
		canvas.drawText("6am", viewWidth / 4, viewHeight - 10, paintText);
		canvas.drawText("12pm", viewWidth / 2, viewHeight - 10, paintText);
		canvas.drawText("6pm", 3 * viewWidth / 4, viewHeight - 10, paintText);
	}

	/**
	 * Draw indoor.
	 * 
	 * @param canvas
	 *            the canvas
	 */
	private void drawIndoor(Canvas canvas) {

		Path path = new Path();
		for (int i = 0; i < listGraphViewDataIndoor.size(); i++) {
			if (i == 0) {
				path.moveTo((float) getScaledX(listGraphViewDataIndoor.get(i)
						.getX()), (float) getScaledY(listGraphViewDataIndoor
						.get(i).getY()));
			} else {
				path.lineTo((float) getScaledX(listGraphViewDataIndoor.get(i)
						.getX()), (float) getScaledY(listGraphViewDataIndoor
						.get(i).getY()));
			}
		}
		canvas.drawPath(path, paintIndoor);
	}

	/**
	 * Draw outdoor.
	 * 
	 * @param canvas
	 *            the canvas
	 */
	private void drawOutdoor(Canvas canvas) {

		Path path = new Path();
		for (int i = 0; i < listGraphViewDataOutdoor.size(); i++) {
			if (i == 0) {
				path.moveTo((float) getScaledX(listGraphViewDataOutdoor.get(i)
						.getX()), (float) getScaledY(listGraphViewDataOutdoor
						.get(i).getY()));
			} else {
				path.lineTo((float) getScaledX(listGraphViewDataOutdoor.get(i)
						.getX()), (float) getScaledY(listGraphViewDataOutdoor
						.get(i).getY()));
			}
		}
		canvas.drawPath(path, paintOutdoor);
	}

	/**
	 * one data set for a graph series.
	 */
	static public class GraphViewData implements GraphViewDataInterface,
			Parcelable {

		/** The value x. */
		public double valueX;

		/** The value y. */
		public double valueY;

		/**
		 * Instantiates a new graph view data.
		 * 
		 * @param valueX
		 *            the value x
		 * @param valueY
		 *            the value y
		 */
		public GraphViewData(double valueX, double valueY) {
			super();
			this.valueX = valueX;
			this.valueY = valueY;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.philips.cl.di.dev.pa.interfaces.GraphViewDataInterface#getX()
		 */
		@Override
		public double getX() {
			return valueX;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.philips.cl.di.dev.pa.interfaces.GraphViewDataInterface#getY()
		 */
		@Override
		public double getY() {
			return valueY;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {

			dest.writeDouble(valueX);
			dest.writeDouble(valueY);

		}

		public static final Parcelable.Creator<GraphViewData> CREATOR = new Parcelable.Creator<GraphViewData>() {
			public GraphViewData createFromParcel(Parcel in) {
				return new GraphViewData(in);
			}

			public GraphViewData[] newArray(int size) {
				return new GraphViewData[size];
			}
		};

		private GraphViewData(Parcel in) {
			valueX = in.readDouble();
			valueY = in.readDouble();
		}

	}

	/**
	 * Sets the graph data.
	 * 
	 * @param listGraphViewDataIndoor
	 *            the list graph view data indoor
	 * @param listGraphViewDataOutdoor
	 *            the list graph view data outdoor
	 */
	public void setGraphData(List<GraphViewData> listGraphViewDataIndoor,
			List<GraphViewData> listGraphViewDataOutdoor) {
		this.listGraphViewDataIndoor = listGraphViewDataIndoor;
		this.listGraphViewDataOutdoor = listGraphViewDataOutdoor;
		invalidate();
		requestLayout();
	}

	/**
	 * Gets the scaled x.
	 * 
	 * @param d
	 *            the d
	 * @return the scaled x
	 */
	private double getScaledX(double d) {
		return (viewWidth / AppConstants.HOURS) * d;
	}

	/**
	 * Gets the scaled y.
	 * 
	 * @param YValue
	 *            the y value
	 * @return the scaled y
	 */
	private double getScaledY(double YValue) {
		return ((viewHeight - offset) / AppConstants.MAX_AQI) * YValue;
	}
}
