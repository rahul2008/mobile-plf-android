package com.philips.cl.di.dev.pa.customviews;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.detail.utils.DetailsAIQ;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;


public class GraphView extends View {
	
	private DisplayMetrics mDisplayMetrics;
	private DetailsAIQ mDetailsAIQ;
	private Paint paint;
	private String yaxisIndoorLabel[] = new String[4];
	private String yaxisOutdoorLabel[] = new String[6];
	private float graphWidh;
	private Coordinates coordinates;
	private boolean isOutdoor = false;
	
	public GraphView(Context context) {
		super(context);
	}
	
	public GraphView(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public GraphView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}
	/**
	 * Outdoor
	 * @param context
	 * @param yCoordinate
	 * @param coordinates
	 */
	public GraphView(Context context, float yCoordinate[], Coordinates coordinates) {
		super(context);
		
		this.isOutdoor = true;
		this.coordinates = coordinates;
		
		/** The Graph width.*/
		mDisplayMetrics = context.getResources().getDisplayMetrics();
		if (coordinates != null) {
			graphWidh = mDisplayMetrics.widthPixels - coordinates.getOdPaddingRight();
		}
		paint = new Paint();
		
		/** Outdoor, y axis label.*/
		yaxisOutdoorLabel[0] = context.getString(R.string.od_yaxis_label1);
		yaxisOutdoorLabel[1] = context.getString(R.string.od_yaxis_label2);
		yaxisOutdoorLabel[2] = context.getString(R.string.od_yaxis_label3);
		yaxisOutdoorLabel[3] = context.getString(R.string.od_yaxis_label4);
		yaxisOutdoorLabel[4] = context.getString(R.string.od_yaxis_label5);
		yaxisOutdoorLabel[5] = context.getString(R.string.od_yaxis_label6);
		
		if (coordinates != null && yCoordinate != null) {
			mDetailsAIQ = new DetailsAIQ(context, graphWidh, 
					yCoordinate, coordinates);
		}
	}

	/**
	 * Indoor
	 * @param context
	 * @param yCoordinates
	 * @param powerOnFlgs
	 * @param coordinates
	 * @param position
	 * @param indexBgImage
	 */
	public GraphView(Context context, List<float[]> yCoordinates,
			int powerOnFlgs[], Coordinates coordinates, int position, ImageView indexBgImage) {
		super(context);
		
		this.isOutdoor = false;
		this.coordinates = coordinates;
		
		/** The Graph width.*/
		mDisplayMetrics = context.getResources().getDisplayMetrics();
		if (coordinates != null) {
			graphWidh = mDisplayMetrics.widthPixels - coordinates.getIdPaddingRight();
		}
		paint = new Paint();
		
		/** Indoor, y axis label.*/
		yaxisIndoorLabel[0] = context.getString(R.string.id_yaxis_label1);
		yaxisIndoorLabel[1] = context.getString(R.string.id_yaxis_label2);
		yaxisIndoorLabel[2] = context.getString(R.string.id_yaxis_label3);
		yaxisIndoorLabel[3] = context.getString(R.string.id_yaxis_label4);
		
		if (coordinates != null && yCoordinates != null) {
			mDetailsAIQ = new DetailsAIQ(context, graphWidh, 
					yCoordinates, powerOnFlgs, coordinates, position);
		}
		
		mDetailsAIQ.setIndexImgBg(indexBgImage);
	}
	
	@Override
	protected void onDraw(final Canvas canvas) {
		drawBackground(canvas);
		if (isOutdoor) {
			drawOutdoorYaxisRect(canvas);
			drawOutdoorYLable(canvas);
		} else {
			drawIndoorYaxisRect(canvas);
			drawIndoorYLable(canvas);
		}
		if (mDetailsAIQ != null && canvas != null && paint != null) {
			mDetailsAIQ.draw(canvas, paint);
		}
	}
	
	/**The drawBackground(Canvas canvas) method.*/
	private void drawBackground(Canvas canvas) {
		
		if (coordinates != null && paint != null) {
			/** The white background of graph. */
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.TRANSPARENT);
			canvas.drawRect(GraphConst.GRAPH_BG_START_XAXIS,
					GraphConst.GRAPH_BG_START_YAXIS, graphWidh,
					coordinates.getIdGraphHeight(), paint);
		}
		
	}
	
	/** The draw Indoor YaxisRect(Canvas canvas) method.*/
	private void drawIndoorYaxisRect(Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			/** The y axis red rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_RED);
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY10(), 
					coordinates.getIdRectWidth(), coordinates.getIdY4(), paint);
			
			/** The y axis purple rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_PURPLE);
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY4(), 
					coordinates.getIdRectWidth(), coordinates.getIdY3(), paint);
			
			/** The y axis navy blue rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_MIDNIGHT_BLUE);
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY3(), 
					coordinates.getIdRectWidth(), coordinates.getIdY2(), paint);
			
			/** The y axis royal blue rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_STATE_BLUE);
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY2(), 
					coordinates.getIdRectWidth(), coordinates.getIdY0(), paint);
		}
	}

	/** The draw y-axis label.*/
	private void drawIndoorYLable( Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			Rect rect = new Rect();
			paint.setAntiAlias(true);
			paint.setColor(Color.GRAY);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(coordinates.getIdTxtSize());
	        canvas.drawText(yaxisIndoorLabel[0], coordinates.getIdRectMarginLeft() / 2, 
	        		coordinates.getIdY2() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisIndoorLabel[0], 0, 
	        		yaxisIndoorLabel[0].length(), rect);
	        canvas.drawText(yaxisIndoorLabel[1], coordinates.getIdRectMarginLeft() / 2, 
	        		coordinates.getIdY3() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisIndoorLabel[1], 0, 
	        		yaxisIndoorLabel[1].length(), rect);
	        canvas.drawText(yaxisIndoorLabel[2], coordinates.getIdRectMarginLeft() / 2, 
	        		coordinates.getIdY4() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisIndoorLabel[2], 0, 
	        		yaxisIndoorLabel[2].length(), rect);
	        canvas.drawText(yaxisIndoorLabel[3], coordinates.getIdRectMarginLeft() / 2, 
	        		coordinates.getIdY10() + coordinates.getIdYxTopLabelPadding(), paint);
	        paint.getTextBounds(yaxisIndoorLabel[3], 0, 
	        		yaxisIndoorLabel[3].length(), rect);
		}
        
	}
	
	
	
	/** The draw OutDoor YaxisRect(Canvas canvas) method.*/
	private void drawOutdoorYaxisRect(Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			/** The y axis red rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_RED);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY500(), 
					coordinates.getOdRectWidth(), coordinates.getOdY300(), paint);
			
			/** The y axis MediumVioletRed  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_DEEP_PINK);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY300(), 
					coordinates.getOdRectWidth(), coordinates.getOdY200(), paint);
			
			/** The y axis MediumOrchid  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_PURPLE);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY200(), 
					coordinates.getOdRectWidth(), coordinates.getOdY150(), paint);
			
			/** The y axis MediumPurple  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_INDIGO);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY150(), 
					coordinates.getOdRectWidth(), coordinates.getOdY100(), paint);
			
			/** The y axis RoyalBlue  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_ROYAL_BLUE);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY100(), 
					coordinates.getOdRectWidth(), coordinates.getOdY50(), paint);
			
			/** The y axis Turquoise  color rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(GraphConst.COLOR_DEEPSKY_BLUE);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY50(), 
					coordinates.getOdRectWidth(), coordinates.getOdY0(), paint);
		}
	}

	/** The draw outdoor y-axis label.*/
	private void drawOutdoorYLable( Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			Rect rect = new Rect();
			paint.setAntiAlias(true);
			paint.setColor(Color.GRAY);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize(coordinates.getIdTxtSize());
	        canvas.drawText(yaxisOutdoorLabel[0], coordinates.getOdRectMarginLeft() / 2, 
	        		coordinates.getOdY50() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisOutdoorLabel[0], 0, yaxisOutdoorLabel[0].length(), rect);
	        canvas.drawText(yaxisOutdoorLabel[1], coordinates.getOdRectMarginLeft() / 2, 
	        		coordinates.getOdY100() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisOutdoorLabel[1], 0, yaxisOutdoorLabel[1].length(), rect);
	        canvas.drawText(yaxisOutdoorLabel[2], coordinates.getOdRectMarginLeft() / 2, 
	        		coordinates.getOdY150() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisOutdoorLabel[2], 0, yaxisOutdoorLabel[2].length(), rect);
	        canvas.drawText(yaxisOutdoorLabel[3], coordinates.getOdRectMarginLeft() / 2, 
	        		coordinates.getOdY200() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisOutdoorLabel[3], 0, yaxisOutdoorLabel[3].length(), rect);
	        canvas.drawText(yaxisOutdoorLabel[4], coordinates.getOdRectMarginLeft() / 2, 
	        		coordinates.getOdY300() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yaxisOutdoorLabel[4], 0, yaxisOutdoorLabel[4].length(), rect);
	        canvas.drawText(yaxisOutdoorLabel[5], coordinates.getOdRectMarginLeft() / 2, 
	        		coordinates.getOdY500() + coordinates.getIdYxTopLabelPadding(), paint);
	        paint.getTextBounds(yaxisOutdoorLabel[5], 0, yaxisOutdoorLabel[5].length(), rect);
		}
	}

}
