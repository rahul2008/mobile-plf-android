package com.philips.cl.di.dev.pa.screens.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

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
		super(context);
		this.percent = percent;
		fwidth = getPxWithRespectToDip(width);
		fheight = getPxWithRespectToDip(height);
		ftopline = getPxWithRespectToDip(4);
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
		if (percent <= 50) {
			paint.setColor(Color.RED);
		} else {
			paint.setColor(Color.rgb(199, 21, 33));
		}
		
		canvas.drawRect(X, y, fwidth, fheight + ftopline, paint);
		
		/** Percentage Text*/
		Rect rect = new Rect();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(getPxWithRespectToDip(20));
        String txt = String.valueOf(percent) + "%";
        float x1 = (fwidth / 2) - getPxWithRespectToDip(25);
        float y1 = (fheight / 2) + getPxWithRespectToDip(10);
        canvas.drawText(txt, x1, y1, paint);
        paint.getTextBounds(txt, 0, txt.length(), rect);
	}
	
	private float getPxWithRespectToDip(int dip) {
		return getResources().getDisplayMetrics().density * dip;
	}

}
