package com.philips.cl.di.dev.pa.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.GraphConst;

public class AirView extends View {

	private int percent;
	public static final int X = 0;
	private float fwidth;
	private float fheight;
	private float ftopline;
	
	/**
	 * Constructor
	 * */
	public AirView(Context context, int percent, int width, int height) {
		this(context);
		this.percent = percent;
		fwidth = getPxWithRespectToDip(width);
		fheight = getPxWithRespectToDip(height);
		ftopline = getPxWithRespectToDip(4);
	}
	
	public AirView(Context context) {
		super(context);
	}
	
	public AirView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AirView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * Draw view
	 * */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		/** Draw rect and color*/
		float y = fheight - (fheight * percent ) / 100 + ftopline;
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		if (percent < 50) {
			paint.setColor(Color.RED);
		} else {
			paint.setColor(GraphConst.COLOR_STATE_BLUE);
		}
		
		float fh = fheight * 6;
		float height10Percent = fh / 100;
		
		if (percent != 0 && percent < height10Percent) {
			y = fheight + ftopline - getPxWithRespectToDip(6);
		}
		
		canvas.drawRect(X, y, fwidth, fheight + ftopline, paint);
	}
	
	private float getPxWithRespectToDip(int dip) {
		return getResources().getDisplayMetrics().density * dip;
	}

}
