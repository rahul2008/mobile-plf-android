package com.philips.cl.di.dev.pa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageRound extends ImageView{

	private Paint paint;
	
	public ImageRound(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(1.0f);
		paint.setColor(0xCCCCCCCC);
	}

	public ImageRound(Context context) {
		super(context);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(1.0f);
		paint.setColor(0xCCCCCCCC);
	}
	
	public void color(int color){
		paint.setColor(color);
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), getWidth()/2, getHeight()/2, paint);
		canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, paint);
		super.onDraw(canvas);
	}
	
}
