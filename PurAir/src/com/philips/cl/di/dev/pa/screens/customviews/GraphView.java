package com.philips.cl.di.dev.pa.screens.customviews;

import java.util.List;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.detail.utils.DetailsAIQ;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;


public class GraphView extends View {
	
	private DisplayMetrics mDisplayMetrics;
	private DetailsAIQ mDetailsAIQ;
	private Paint paint;
	private String yLabels[] = new String[4];
	private String yODLabels[] = new String[6];
	private float graphWidh;
	private Coordinates coordinates;
	private boolean isOutdoor = false;
	
	/**
	 * The GraphicsView(Context context) constructor For outdoor.
	 * */
	public GraphView(Context context, float yAxisValues[], int powerOnFlgs[], 
			boolean isOutdoor, Coordinates coordinates) {
		super(context);
		
		this.isOutdoor = isOutdoor;
		this.coordinates = coordinates;
		
		/** The Graph width.*/
		mDisplayMetrics = context.getResources().getDisplayMetrics();
		if (coordinates != null) {
			graphWidh = mDisplayMetrics.widthPixels - coordinates.getOdPaddingRight();
		}
		paint = new Paint();
		
		/** Outdoor, y axis label.*/
		yODLabels[0] = context.getString(R.string.od_yaxis_label1);
		yODLabels[1] = context.getString(R.string.od_yaxis_label2);
		yODLabels[2] = context.getString(R.string.od_yaxis_label3);
		yODLabels[3] = context.getString(R.string.od_yaxis_label4);
		yODLabels[4] = context.getString(R.string.od_yaxis_label5);
		yODLabels[5] = context.getString(R.string.od_yaxis_label6);
		
		if (coordinates != null && yAxisValues != null) {
			mDetailsAIQ = new DetailsAIQ(context, graphWidh, 
					yAxisValues, null, powerOnFlgs, coordinates, 0, true);
		}
	}

	/**
	 * The GraphicsView(Context context) constructor for indoor.
	 * */
	public GraphView(Context context, float yAxisValues[], List<float[]> restValuelist,
			int powerOnFlgs[], Coordinates coordinates, int position, ImageView indexBgImg) {
		super(context);
		
		this.coordinates = coordinates;
		
		/** The Graph width.*/
		mDisplayMetrics = context.getResources().getDisplayMetrics();
		if (coordinates != null) {
			graphWidh = mDisplayMetrics.widthPixels - coordinates.getIdPaddingRight();
		}
		paint = new Paint();
		
		/** Indoor, y axis label.*/
		yLabels[0] = context.getString(R.string.id_yaxis_label1);
		yLabels[1] = context.getString(R.string.id_yaxis_label2);
		yLabels[2] = context.getString(R.string.id_yaxis_label3);
		yLabels[3] = context.getString(R.string.id_yaxis_label4);
		
		if (coordinates != null && yAxisValues != null) {
			mDetailsAIQ = new DetailsAIQ(context, graphWidh, 
					yAxisValues, restValuelist, powerOnFlgs, coordinates, position, false);
		}
		
		mDetailsAIQ.setIndexImgBg(indexBgImg);
	}
	
	@Override
	protected void onDraw(final Canvas canvas) {
		drawBackground(canvas);
		if (isOutdoor) {
			drawODYaxisRect(canvas);
			drawODYLable(canvas);
		} else {
			drawYaxisRect(canvas);
			drawYLable(canvas);
		}
		if (mDetailsAIQ != null) {
			mDetailsAIQ.draw(canvas, paint);
		}
	}
	
	/**The drawBackground(Canvas canvas) method.*/
	private void drawBackground(Canvas canvas) {
		
		if (coordinates != null && paint != null) {
			/** The white background of graph. */
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.WHITE);
			canvas.drawRect(GraphConst.GRAPH_BG_START_XAXIS,
					GraphConst.GRAPH_BG_START_YAXIS, graphWidh,
					coordinates.getIdGraphHeight(), paint);
		}
		
	}
	
	/** The draw Indoor YaxisRect(Canvas canvas) method.*/
	private void drawYaxisRect(Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			/** The y axis red rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.RED);
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY10_0(), 
					coordinates.getIdRectWidth(), coordinates.getIdY3_5(), paint);
			
			/** The y axis purple rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(128, 0, 128));
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY3_5(), 
					coordinates.getIdRectWidth(), coordinates.getIdY2_5(), paint);
			
			/** The y axis navy blue rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(0, 0, 128));
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY2_5(), 
					coordinates.getIdRectWidth(), coordinates.getIdY1_5(), paint);
			
			/** The y axis royal blue rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(65, 105, 225));
			canvas.drawRect(coordinates.getIdRectMarginLeft(), coordinates.getIdY1_5(), 
					coordinates.getIdRectWidth(), coordinates.getIdY0_0(), paint);
		}
	}

	/** The draw y-axis label.*/
	private void drawYLable( Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			Rect rect = new Rect();
			paint.setAntiAlias(true);
			paint.setColor(Color.GRAY);
			paint.setTextSize(coordinates.getIdTxtSize());
	        canvas.drawText(yLabels[0], 0, coordinates.getIdY1_5() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yLabels[0], 0, yLabels[0].length(), rect);
	        canvas.drawText(yLabels[1], 0, coordinates.getIdY2_5() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yLabels[1], 0, yLabels[1].length(), rect);
	        canvas.drawText(yLabels[2], 0, coordinates.getIdY3_5() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yLabels[2], 0, yLabels[2].length(), rect);
	        canvas.drawText(yLabels[3], 0, coordinates.getIdY10_0() + coordinates.getIdYxTopLabelPadding(), paint);
	        paint.getTextBounds(yLabels[3], 0, yLabels[3].length(), rect);
		}
        
	}
	
	/** The draw OutDoor YaxisRect(Canvas canvas) method.*/
	private void drawODYaxisRect(Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			/** The y axis red rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.RED);
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY500(), 
					coordinates.getOdRectWidth(), coordinates.getOdY300(), paint);
			
			/** The y axis MediumVioletRed  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(199, 21, 33));
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY300(), 
					coordinates.getOdRectWidth(), coordinates.getOdY200(), paint);
			
			/** The y axis MediumOrchid  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(186, 85, 211));
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY200(), 
					coordinates.getOdRectWidth(), coordinates.getOdY150(), paint);
			
			/** The y axis MediumPurple  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(147, 112, 219));
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY150(), 
					coordinates.getOdRectWidth(), coordinates.getOdY100(), paint);
			
			/** The y axis RoyalBlue  rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(65, 105, 225));
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY100(), 
					coordinates.getOdRectWidth(), coordinates.getOdY50(), paint);
			
			/** The y axis Turquoise  color rectangle*/
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(64, 225, 208));
			canvas.drawRect(coordinates.getOdRectMarginLeft(), coordinates.getOdY50(), 
					coordinates.getOdRectWidth(), coordinates.getOdY0(), paint);
		}
	}

	/** The draw outdoor y-axis label.*/
	private void drawODYLable( Canvas canvas) {
		if (coordinates != null && paint != null && canvas!= null) {
			Rect rect = new Rect();
			paint.setAntiAlias(true);
			paint.setColor(Color.GRAY);
			paint.setTextSize(coordinates.getIdTxtSize());
	        canvas.drawText(yODLabels[0], 0, coordinates.getOdY50() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yODLabels[0], 0, yODLabels[0].length(), rect);
	        canvas.drawText(yODLabels[1], 0, coordinates.getOdY100() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yODLabels[1], 0, yODLabels[1].length(), rect);
	        canvas.drawText(yODLabels[2], 0, coordinates.getOdY150() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yODLabels[2], 0, yODLabels[2].length(), rect);
	        canvas.drawText(yODLabels[3], 0, coordinates.getOdY200() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yODLabels[3], 0, yODLabels[3].length(), rect);
	        canvas.drawText(yODLabels[4], 0, coordinates.getOdY300() + coordinates.getIdYxLabelPadding(), paint);
	        paint.getTextBounds(yODLabels[4], 0, yODLabels[4].length(), rect);
	        canvas.drawText(yODLabels[5], 0, coordinates.getOdY500() + coordinates.getIdYxTopLabelPadding(), paint);
	        paint.getTextBounds(yODLabels[5], 0, yODLabels[5].length(), rect);
		}
	}

}
