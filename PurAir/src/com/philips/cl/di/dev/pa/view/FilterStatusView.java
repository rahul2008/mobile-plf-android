package com.philips.cl.di.dev.pa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.Utils;

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
		paint.setStrokeWidth(140f);
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

	public void setPrefilterValue(int filterValue) {
		float length = getWidth() * filterValue / AppConstants.PRE_FILTER_MAX_VALUE;
		setColorAndLength(Utils.getPreFilterStatusColour(filterValue), length);
	}
	
	public void setMultiCareFilterValue(int filterValue) {
		float length = getWidth() * filterValue / AppConstants.MULTI_CARE_FILTER_MAX_VALUE;
		setColorAndLength(Utils.getMultiCareFilterStatusColour(filterValue), length);
	}
	
	public void setActiveCarbonFilterValue(int filterValue) {
		float length = getWidth() * filterValue / AppConstants.ACTIVE_CARBON_FILTER_MAX_VALUE;
		setColorAndLength(Utils.getActiveCarbonFilterStatusColour(filterValue), length);
	}
	
	public void setHEPAfilterValue(int filterValue) {
		float length = getWidth() * filterValue / AppConstants.HEPA_FILTER_MAX_VALUE;
		setColorAndLength(Utils.getHEPAFilterStatusColour(filterValue), length);
	}
}
