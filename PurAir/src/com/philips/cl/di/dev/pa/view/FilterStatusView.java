package com.philips.cl.di.dev.pa.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.Utils;

public class FilterStatusView extends ImageView {

	private Paint paint;
	private float length;
	private int viewWidth;
	private int color;
	private int filterStatusValue;
	public static final int PRE_FILTER = 1;
	public static final int MUL_CARE_FILTER = 2;
	public static final int ACT_CARBON_FILTER = 3;
	public static final int HEPA_FILTER = 4;

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
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
	}
	
	public void setFilterValue(int filterValue, int filterType) {
		filterStatusValue = filterValue;
		filterViewHandler.sendEmptyMessageDelayed(filterType, 100);//100 millisecond
	}
	
	@SuppressLint("HandlerLeak")
	private  Handler filterViewHandler = new Handler() {
		public void handleMessage(Message msg) {
			float length = 0.0f ;
			switch (msg.what) {
			case PRE_FILTER:
				length = viewWidth * filterStatusValue / (float) AppConstants.PRE_FILTER_MAX_VALUE;
				setColorAndLength(Utils.getPreFilterStatusColour(filterStatusValue), length);
				break;
			case MUL_CARE_FILTER:
				length = viewWidth * filterStatusValue / (float) AppConstants.MULTI_CARE_FILTER_MAX_VALUE;
				setColorAndLength(Utils.getMultiCareFilterStatusColour(filterStatusValue), length);
				break;
			case ACT_CARBON_FILTER:
				length = viewWidth * filterStatusValue / (float) AppConstants.ACTIVE_CARBON_FILTER_MAX_VALUE;
				setColorAndLength(Utils.getActiveCarbonFilterStatusColour(filterStatusValue), length);
				break;
			case HEPA_FILTER:
				length = viewWidth * filterStatusValue / (float) AppConstants.HEPA_FILTER_MAX_VALUE;
				setColorAndLength(Utils.getHEPAFilterStatusColour(filterStatusValue), length);
				break;
			default:
				break;
			}
		};
	};
}
