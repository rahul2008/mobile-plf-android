package com.philips.cl.di.dev.pa.customviews;

import com.philips.cl.di.dev.pa.util.AppConstants;
import com.philips.cl.di.dev.pa.util.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FilterStatusView extends ImageView {

	Paint paint;

	float length;

	int color;

	public FilterStatusView(Context context) {
		super(context);
		init();
	}

	public FilterStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		canvas.drawLine(0, 0, length, 0, paint);
	}

	private void init() {
		paint = new Paint();
		paint.setStrokeWidth(100f);
		paint.setColor(color);
		paint.setStyle(Style.STROKE);

	}

	public void setColor(int color) {
		this.color = color;
		invalidate();
		requestLayout();
	}

	public void setLength(int length) {
		this.length = length;
		invalidate();
		requestLayout();
	}

	public void setColorAndLength(int color, float length) {
		this.color = color;
		this.length = length;
		init();
		invalidate();
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	public void setFilterValue(float filterValue) {
		float filterRange = (filterValue / (AppConstants.MAXIMUMFILTER - AppConstants.MINIMUNFILTER)) * 100;
		length = AppConstants.MAXWIDTH - (int) ((filterRange / 100) * AppConstants.MAXWIDTH);
		setColorAndLength(Utils.getFilterStatusColor(filterValue), length);
	}

}
