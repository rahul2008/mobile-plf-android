package com.philips.cl.di.dev.pa.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.philips.cl.di.dev.pa.detail.utils.GraphConst;

public class AirView extends View {

	private int percent;
	private final int X = 0;
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
		float y = fheight - ((fheight * percent ) / 100) + ftopline;
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		if (percent < 50) {
			paint.setColor(Color.RED);
		} else {
			paint.setColor(GraphConst.COLOR_STATE_BLUE);
		}
		
		canvas.drawRect(X, y, fwidth, fheight + ftopline, paint);
		
		/** Percentage Text*/
		Rect rect = new Rect();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(getPxWithRespectToDip(20));
		paint.setTextAlign(Paint.Align.CENTER);
        String txt = String.valueOf(percent) + "%";
        float x1 = (canvas.getWidth() / 2);
        float y1 = (fheight / 2) + getPxWithRespectToDip(10);
        canvas.drawText(txt, x1, y1, paint);
        paint.getTextBounds(txt, 0, txt.length(), rect);
	}
	
	private float getPxWithRespectToDip(int dip) {
		return getResources().getDisplayMetrics().density * dip;
	}

}
