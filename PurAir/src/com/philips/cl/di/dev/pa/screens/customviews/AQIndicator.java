package com.philips.cl.di.dev.pa.screens.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class AQIndicator.
 */
public class AQIndicator extends RelativeLayout {

	/** The start angle. */
	float fStartAngle = -290;
	
	/** The sweep angle. */
	float fSweepAngle = -320;
	
	/** The b use center. */
	boolean bUseCenter = false;
	
	/** The paint. */
	Paint paint;
	
	/** The rect f. */
	RectF rectF;
	
	/** The stroke width. */
	float fStrokeWidth = 35;
	
	/** The i radius. */
	int iRadius = 136 ; 

	/**
	 * Instantiates a new aQ indicator.
	 *
	 * @param context the context
	 */
	public AQIndicator(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new aQ indicator.
	 *
	 * @param context the context
	 * @param set the set
	 */
	public AQIndicator(Context context, AttributeSet set) {
		super(context, set);
	}
	
	/**
	 * Instantiates a new aQ indicator.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AQIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	/**
	 * Initialise aqi.
	 *
	 * @param fSweepAngle the f sweep angle
	 */
	public void initialiseAQI(float fSweepAngle) {
		this.fSweepAngle = -fSweepAngle;
	}

	

	/* (non-Javadoc)
	 * @see android.widget.RelativeLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(fStrokeWidth);
		paint.setStrokeCap(Cap.ROUND);
		canvas.drawArc(new RectF(new Rect(360-iRadius,257-iRadius, 360+iRadius, 257+iRadius)), fStartAngle, fSweepAngle, false, paint);
		
	}

}
